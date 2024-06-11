package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ParserTest {
    private static final String HTML_WITH_COMMENT = "<html><!-- This is a comment --></html>";
    private static final String XML_DECLARATION_HTML = "<html xmlns=\"http://www.w3.org/1999/xhtml\">";
    private static final String HTML_WITH_EMPTY_TAG = "<html><img src=\"image.png\"/></html>";
    private static final String HTML_WITH_SELF_CLOSED_TAG = "<html><div/></html>";
    private static final String HTML_WITH_BASE_TAG = "<html><base href=\"/test\"/><body></body></html>";
    private static final String HTML_WITH_UNKNOWN_CHAR = "<html>&#123;</html>";
    private static final String HTML_WITH_MISSING_END_TAG = "<html><div";
    private static final String HTML_WITH_UNRECOGNIZED_TAG = "<html><mytag></mytag></html>";
    private static final String HTML_WITH_IMplicit_parent = "<html><div><mytag></mytag></div></html>";
    private static final String HTML_WITH_Data_tag = "<html><title>Test</title></html>";
    private Document doc;

    @Before
    public void setUp() {
        doc = Parser.parse(HTML_WITH_COMMENT, "");
    }

    @Test
    public void testParseComment() {
        assertEquals("This is a comment", doc.getElementsByTagName("comment").item(0).getTextContent());
    }

    @Test
    public void testParseXmlDeclaration() {
        doc = Parser.parse(XML_DECLARATION_HTML, "");
        assertEquals("http://www.w3.org/1999/xhtml", doc.getDocumentElement().getNamespaceURI());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyInput() {
        Parser parser = new Parser("", "", false);
        parser.parse();
        fail("Expected an IllegalArgumentException");
    }

    @Test
    public void testParseEmptyTag() {
        doc = Parser.parse(HTML_WITH_EMPTY_TAG, "");
        Element img = doc.getElementsByTagName("img").item(0);
        assertTrue(img.getNodeType() == Node.ELEMENT_NODE);
        assertTrue(img.hasAttributes());
        assertEquals(1, img.getAttributes().size());
        assertEquals("image.png", img.getAttribute("src"));
        assertEquals(0, img.getChildNodes().getLength());
    }

    @Test
    public void testParseSelfClosedTag() {
        doc = Parser.parse(HTML_WITH_SELF_CLOSED_TAG, "");
        Element div = doc.getElementsByTagName("div").item(0);
        assertTrue(div.getNodeType() == Node.ELEMENT_NODE);
        assertTrue(div.hasAttributes());
        assertEquals(0, div.getChildNodes().getLength());
    }

    @Test
    public void testParseBaseTag() {
        doc = Parser.parse(HTML_WITH_BASE_TAG, "");
        assertEquals("/test", doc.getBaseURI());
    }

    @Test(expected = IllegalStateException.class)
    public void testParseUnknownChar() {
        Parser parser = new Parser(HTML_WITH_UNKNOWN_CHAR, "", false);
        parser.parse();
        fail("Expected an IllegalStateException");
    }

    @Test
    public void testParseMissingEndTag() {
        try {
            Parser parser = new Parser(HTML_WITH_MISSING_END_TAG, "", false);
            parser.parse();
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("unexpected end of input", e.getMessage());
        }
    }

    @Test
    public void testParseUnrecognizedTag() {
        try {
            Parser parser = new Parser(HTML_WITH_UNRECOGNIZED_TAG, "", false);
            parser.parse();
            fail("Expected an IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testParseImplicitParentTag() {
        doc = Parser.parse(HTML_WITH_IMplicit_parent, "");
        Element div = doc.getElementsByTagName("div").item(0);
        Element mytag = div.getElementsByTagName("mytag").item(0);
        assertEquals("div", div.getTagName());
        assertEquals("mytag", mytag.getTagName());
        assertTrue(mytag.getParentNode() == div);
    }

    @Test
    public void testParseDataTag() {
        doc = Parser.parse(HTML_WITH_Data_tag, "");
        Element title = doc.getElementsByTagName("title").item(0);
        assertEquals("Test", title.getTextContent());
    }
}