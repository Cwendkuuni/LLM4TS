package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;

import org.jsoup.helper.MockUtil;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.TreeBuilder;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        assertEquals(ParseSettings.preserveCase, xmlTreeBuilder.defaultSettings());
    }

    @Test
    public void testInitialiseParse() {
        Reader input = mock(Reader.class);
        String baseUri = "http://example.com";
        Parser parser = mock(Parser.class);

        xmlTreeBuilder.initialiseParse(input, baseUri, parser);

        MockUtil.verifyPrivateMethod(xmlTreeBuilder, "initialiseParse", input, baseUri, parser);
        assertEquals(xmlTreeBuilder.stack.size(), 1);
        assertEquals(xmlTreeBuilder.doc.outputSettings().syntax(), Document.OutputSettings.Syntax.xml);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        Reader input = new StringReader("<html></html>");
        String baseUri = "http://example.com";

        Document document = xmlTreeBuilder.parse(input, baseUri);

        assertEquals("html", document.nodeName());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<html></html>";
        String baseUri = "http://example.com";

        Document document = xmlTreeBuilder.parse(input, baseUri);

        assertEquals("html", document.nodeName());
    }

    @Test
    public void testProcessStartTagToken() {
        Token.StartTag startTag = mock(Token.StartTag.class);
        Tag tag = mock(Tag.class);
        when(startTag.name()).thenReturn("div");
        when(startTag.attributes()).thenReturn(Collections.emptyMap());
        when(tag.isKnownTag()).thenReturn(true);
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testProcessEndTagToken() {
        Token.EndTag endTag = mock(Token.EndTag.class);
        when(endTag.tagName).thenReturn("div");

        xmlTreeBuilder.stack.add(new Element("div"));

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessCommentToken() {
        Token.Comment commentToken = mock(Token.Comment.class);
        when(commentToken.getData()).thenReturn("comment");

        xmlTreeBuilder.insert(commentToken);

        Comment comment = (Comment) xmlTreeBuilder.currentElement().childNodes().get(0);
        assertEquals("comment", comment.getData());
    }

    @Test
    public void testProcessCharacterToken() {
        Token.Character token = mock(Token.Character.class);
        when(token.getData()).thenReturn("text");
        when(token.isCData()).thenReturn(false);

        xmlTreeBuilder.insert(token);

        TextNode textNode = (TextNode) xmlTreeBuilder.currentElement().childNodes().get(0);
        assertEquals("text", textNode.getWholeText());
    }

    @Test
    public void testProcessCharacterCDataToken() {
        Token.Character token = mock(Token.Character.class);
        when(token.getData()).thenReturn("text");
        when(token.isCData()).thenReturn(true);

        xmlTreeBuilder.insert(token);

        CDataNode cDataNode = (CDataNode) xmlTreeBuilder.currentElement().childNodes().get(0);
        assertEquals("text", cDataNode.getWholeText());
    }

    @Test
    public void testProcessDoctypeToken() {
        Token.Doctype doctype = mock(Token.Doctype.class);
        when(doctype.getName()).thenReturn("html");
        when(doctype.getPublicIdentifier()).thenReturn("public");
        when(doctype.getSystemIdentifier()).thenReturn("system");

        xmlTreeBuilder.insert(doctype);

        Document document = (Document) xmlTreeBuilder.doc;
        DocumentType documentType = (DocumentType) document.childNodes().get(0);
        assertEquals("html", documentType.nodeName());
        assertEquals("public", documentType.getPublicIdentifier());
        assertEquals("system", documentType.getSystemIdentifier());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = mock(Token.EndTag.class);
        when(endTag.tagName).thenReturn("div");

        xmlTreeBuilder.stack.add(new Element("div"));

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testParseFragment() {
        String input = "<div></div>";
        String baseUri = "http://example.com";
        Parser parser = mock(Parser.class);
        xmlTreeBuilder.initialiseParse(new StringReader(input), baseUri, parser);

        List<Node> nodes = xmlTreeBuilder.parseFragment(input, baseUri);

        assertEquals(1, nodes.size());
        assertEquals("div", nodes.get(0).nodeName());
    }

    @Test
    public void testParseFragmentWithContext() {
        String input = "<div></div>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        Parser parser = mock(Parser.class);
        xmlTreeBuilder.initialiseParse(new StringReader(input), baseUri, parser);

        List<Node> nodes = xmlTreeBuilder.parseFragment(input, context, baseUri, parser);

        assertEquals(1, nodes.size());
        assertEquals("div", nodes.get(0).nodeName());
    }
}