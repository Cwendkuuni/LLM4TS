package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testParse_ReaderBaseUri_NormalCase() {
        String xmlData = "<tag>Test XML Data</tag>";
        Reader reader = new StringReader(xmlData);
        String baseUri = "http://example.com";

        Document document = xmlTreeBuilder.parse(reader, baseUri);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals(1, document.getElementsByTag("tag").size());
        assertEquals("Test XML Data", document.getElementsByTag("tag").first().text());
    }

    @Test
    public void testParse_ReaderBaseUri_NullInput() {
        Reader reader = null;
        String baseUri = "http://example.com";

        Document document = xmlTreeBuilder.parse(reader, baseUri);

        assertNull(document);
    }

    @Test
    public void testParse_StringBaseUri_NormalCase() {
        String xmlData = "<tag>Test XML Data</tag>";
        String baseUri = "http://example.com";

        Document document = xmlTreeBuilder.parse(xmlData, baseUri);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals(1, document.getElementsByTag("tag").size());
        assertEquals("Test XML Data", document.getElementsByTag("tag").first().text());
    }

    @Test
    public void testParse_StringBaseUri_EmptyInput() {
        String xmlData = "";
        String baseUri = "http://example.com";

        Document document = xmlTreeBuilder.parse(xmlData, baseUri);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals(0, document.getAllElements().size());
    }

    @Test
    public void testParseFragment_NormalCase() {
        String xmlData = "<tag>Test XML Data<child>Child Data</child></tag>";
        String baseUri = "http://example.com";

        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlData, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());

        Element rootElement = (Element) nodes.get(0);
        assertEquals("tag", rootElement.tagName());
        assertEquals(2, rootElement.childNodes().size());

        Element childElement = (Element) rootElement.childNode(1);
        assertEquals("child", childElement.tagName());
        assertEquals("Child Data", childElement.text());
    }

    @Test
    public void testParseFragment_EmptyFragment() {
        String xmlData = "";
        String baseUri = "http://example.com";

        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlData, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);

        assertNotNull(nodes);
        assertEquals(0, nodes.size());
    }

    @Test
    public void testProcess_StartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("tag");

        Element element = xmlTreeBuilder.insert(startTag);

        assertNotNull(element);
        assertEquals("tag", element.tagName());
    }

    @Test
    public void testProcess_EndTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("tag");

        Element startElement = xmlTreeBuilder.insert(startTag);

        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName("tag");
        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcess_Comment() {
        Token.Comment commentToken = new Token.Comment("Test Comment");

        xmlTreeBuilder.insert(commentToken);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertEquals("<!--Test Comment-->", xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }

    @Test
    public void testProcess_Character() {
        Token.Character characterToken = new Token.Character();
        characterToken.data("Test Data");

        xmlTreeBuilder.insert(characterToken);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertEquals("Test Data", xmlTreeBuilder.currentElement().childNode(0).toString());
    }

    @Test
    public void testProcess_Doctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.setName("html");
        doctype.setPublicIdentifier("-//W3C//DTD XHTML 1.0 Strict//EN");
        doctype.setSystemIdentifier("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");

        xmlTreeBuilder.insert(doctype);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">",
                xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcess_UnknownToken() {
        Token token = new Token();
        token.type = Token.TokenType.Character;

        xmlTreeBuilder.process(token);
    }

}