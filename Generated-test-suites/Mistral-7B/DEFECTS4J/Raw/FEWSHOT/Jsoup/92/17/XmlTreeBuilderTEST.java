package org.jsoup.parser;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder builder;
    private Parser parser;
    private Document doc;

    @Before
    public void setup() {
        builder = new XmlTreeBuilder();
        parser = mock(Parser.class);
        doc = mock(Document.class);
    }

    @Test
    public void testProcessStartTag() {
        Element element = new Element("tag");
        StartTag startTag = new StartTag("tag", false, new Attribute[] {});
        parser.parseToken(startTag);

        when(parser.parseToken(any())).thenReturn(null);

        builder.initialiseParse(new StringReader(""), "", parser);
        builder.parseFragment("", "", parser);
        builder.runParser();

        builder.insert(startTag);

        assertEquals(element, builder.doc.getElementsByTagName("tag").get(0));
    }

    @Test
    public void testProcessEndTag() {
        Element parentElement = new Element("parent");
        Element childElement = new Element("child");
        parentElement.appendChild(childElement);
        builder.doc.appendChild(parentElement);

        EndTag endTag = new EndTag("child");

        when(parser.parseToken(any())).thenReturn(null);

        builder.initialiseParse(new StringReader(""), "", parser);
        builder.parseFragment("", "", parser);
        builder.runParser();

        builder.insertNode(childElement);
        builder.popStackToClose(endTag);

        assertTrue(parentElement.removeChild(childElement));
    }

    @Test
    public void testProcessComment() {
        Comment comment = new Comment("comment");

        Token commentToken = new Token.Comment("comment");

        when(parser.parseToken(any())).thenReturn(commentToken);

        builder.initialiseParse(new StringReader(""), "", parser);
        builder.parseFragment("", "", parser);
        builder.runParser();

        builder.insert(commentToken);

        assertEquals(comment, builder.doc.childNodes().get(0));
    }

    @Test
    public void testProcessCharacter() {
        TextNode textNode = new TextNode("text");

        Token characterToken = new Token.Character('t', "text");

        when(parser.parseToken(any())).thenReturn(characterToken);

        builder.initialiseParse(new StringReader(""), "", parser);
        builder.parseFragment("", "", parser);
        builder.runParser();

        builder.insert(characterToken);

        assertEquals(textNode, builder.doc.childNodes().get(0));
    }

    @Test
    public void testProcessDoctype() {
        DocumentType documentType = new DocumentType("html", "", "");

        Token documentTypeToken = new Token.Doctype("html");

        when(parser.parseToken(any())).thenReturn(documentTypeToken);

        builder.initialiseParse(new StringReader(""), "", parser);
        builder.parseFragment("", "", parser);
        builder.runParser();

        builder.insert(documentTypeToken);

        assertEquals(documentType, builder.doc.childNodes().get(0));
    }

    @Test
    public void testParseFragment() {
        String fragment = "<root><child>text</child></root>";
        List<Node> expectedNodes = new ArrayList<>();
        expectedNodes.add(new Element("root"));
        Element rootElement = new Element("root");
        rootElement.appendChild(new Element("child").setTextNode("text"));
        expectedNodes.add(rootElement.getChild(0));

        when(parser.parseToken(any())).thenReturn(null);

        List<Node> nodes = builder.parseFragment(fragment, "", parser);

        assertEquals(expectedNodes.size(), nodes.size());
        for (int i = 0; i < expectedNodes.size(); i++) {
            assertEquals(expectedNodes.get(i).nodeName(), nodes.get(i).nodeName());
        }
    }

    @Test
    public void testParseFragmentWithContext() {
        String fragment = "<root><child>text</child></root>";
        Element context = new Element("context");

        when(parser.parseToken(any())).thenReturn(null);

        List<Node> nodes = builder.parseFragment(fragment, context, "", parser);

        assertEquals(context.getChildNodes().size(), nodes.size());
        for (int i = 0; i < context.getChildNodes().size(); i++) {
            assertEquals(context.getChildNodes().get(i).nodeName(), nodes.get(i).nodeName());
        }
    }
}