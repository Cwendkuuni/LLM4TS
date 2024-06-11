package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root>Test</root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        Document doc = xmlTreeBuilder.getDocument();
        assertTrue(doc.outputSettings().syntax() == Document.OutputSettings.Syntax.xml);
        assertEquals(input, doc.toString());
        assertEquals(baseUri, doc.baseUri());
        assertTrue(xmlTreeBuilder.getStack().size() == 1);
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", "attr1=value1 attr2=value2", false);
        Element el = xmlTreeBuilder.insert(startTag);

        assertEquals("tag", el.tagName());
        assertEquals("http://example.com", el.baseUri());
        assertEquals("value1", el.attr("attr1"));
        assertEquals("value2", el.attr("attr2"));
        assertTrue(el.childNodes().isEmpty());
        assertTrue(xmlTreeBuilder.getStack().size() == 1);
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.getStack().add(new Element("tag"));
        xmlTreeBuilder.getStack().add(new Element("div"));

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(1, xmlTreeBuilder.getStack().size());
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("Test", false);
        xmlTreeBuilder.insert(commentToken);

        Element el = (Element) xmlTreeBuilder.getStack().get(0);
        assertEquals("Test", el.childNodes().get(0).outerHtml());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("Test");
        xmlTreeBuilder.insert(characterToken);

        Element el = (Element) xmlTreeBuilder.getStack().get(0);
        assertEquals("Test", el.childNodes().get(0).outerHtml());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctypeToken = new Token.Doctype("html");
        xmlTreeBuilder.insert(doctypeToken);

        Element el = (Element) xmlTreeBuilder.getStack().get(0);
        assertTrue(el.childNodes().get(0) instanceof DocumentType);
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<div>Test</div>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(inputFragment, baseUri, errors);
        xmlTreeBuilder.runParser();

        Document doc = xmlTreeBuilder.getDocument();
        assertTrue(doc.outputSettings().syntax() == Document.OutputSettings.Syntax.xml);
        assertEquals(inputFragment, doc.toString());
        assertEquals(baseUri, doc.baseUri());
        assertTrue(doc.childNodes().get(0) instanceof Element);
    }
}