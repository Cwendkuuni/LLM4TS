package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.junit.Test;

/**
 * Test class for XmlTreeBuilder class.
 */
public class XmlTreeBuilderTest {

    @Test
    public void testXmlTreeBuilderParse() throws IOException {
        String xmlString = "<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:x=\"http://www.example.com/\"><element x=\"value\"><subElement/></element></root>";
        ParseSettings settings = new ParseSettings();
        settings.setPreserveCase(true);

        Document xmlDoc = XmlTreeBuilder.parse(xmlString, "");

        assertNotNull(xmlDoc);
        Element root = xmlDoc.rootElement();
        assertNotNull(root);
        assertTrue(root.tagName().equals("root"));

        List<Node> nodeList = xmlDoc.childNodes();
        assertEquals(2, nodeList.size());

        Node firstNode = nodeList.get(0);
        assertTrue(firstNode instanceof Element);
        Element firstElement = (Element) firstNode;
        assertTrue(firstElement.tagName().equals("element"));
        assertEquals(1, firstElement.childNodes().size());

        Node secondNode = nodeList.get(1);
        assertTrue(secondNode instanceof Element);
        Element secondElement = (Element) secondNode;
        assertTrue(secondElement.tagName().equals("subElement"));
        assertTrue(secondElement.childNodes().isEmpty());
    }

    @Test
    public void testXmlTreeBuilderParseFragment() throws IOException {
        String xmlFragment = "<element x=\"value\"><subElement/></element>";
        ParseSettings settings = new ParseSettings();
        settings.setPreserveCase(true);

        List<Node> nodeList = XmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), settings);

        assertNotNull(nodeList);
        assertEquals(2, nodeList.size());

        Node firstNode = nodeList.get(0);
        assertTrue(firstNode instanceof Element);
        Element firstElement = (Element) firstNode;
        assertTrue(firstElement.tagName().equals("element"));
        assertEquals(1, firstElement.childNodes().size());

        Node secondNode = nodeList.get(1);
        assertTrue(secondNode instanceof Element);
        Element secondElement = (Element) secondNode;
        assertTrue(secondElement.tagName().equals("subElement"));
        assertTrue(secondElement.childNodes().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXmlTreeBuilderInvalidInput() throws IOException {
        String invalidXml = "<root><element><commonError/>"; // missing closing tag for commonError
        ParseSettings settings = new ParseSettings();
        settings.setPreserveCase(true);

        XmlTreeBuilder.parse(invalidXml, "");
    }

    @Test
    public void testXmlTreeBuilderParseInvalidXmlWithComments() throws IOException {
        String invalidXml = "<root><!-- This is a comment --><element><commonError/><!-- This is another comment --></element>"; // missing closing tag for commonError
        ParseSettings settings = new ParseSettings();
        settings.setPreserveCase(true);

        Document xmlDoc = XmlTreeBuilder.parse(invalidXml, "");
        assertNotNull(xmlDoc);
        // TODO: Add assertions to check the presence and position of comments and the absence of the missing tag
    }

    @Test
    public void testXmlTreeBuilderParseXmlDeclaration() throws IOException {
        String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        ParseSettings settings = new ParseSettings();
        settings.setPreserveCase(true);

        Document xmlDoc = XmlTreeBuilder.parse(xmlDeclaration, "");
        assertNotNull(xmlDoc);
        Element xmlDeclarationNode = xmlDoc.childNodes().get(0);
        assertTrue(xmlDeclarationNode instanceof XmlDeclaration);
        XmlDeclaration xmlDeclarationElement = (XmlDeclaration) xmlDeclarationNode;
        assertEquals("xml", xmlDeclarationElement.tagName());
        assertEquals("UTF-8", xmlDeclarationElement.attributes().get("encoding").toString());
        assertTrue(xmlDeclarationElement.attributes().getBoolean("standalone", false));
    }

    private int assertEquals(int expected, int actual) {
        if (expected != actual) {
            throw new AssertionError("Expected: " + expected + ", but was: " + actual);
        }
        return expected;
    }
}