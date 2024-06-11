package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

/**
 * Tests for the XmlTreeBuilder class.
 */
public class XmlTreeBuilderTest {

    /**
     * Test method for {@link XmlTreeBuilder#defaultSettings()}.
     */
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = XmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
    }

    /**
     * Test method for {@link XmlTreeBuilder#parse(Reader, String)}.
     */
    @Test
    public void testParseReader() {
        String xmlInput = "<root><element>Hello World</element></root>";
        String baseUri = "http://example.com";

        Document expected = Jsoup.parse(xmlInput, baseUri);

        ParseErrorList errorList = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase();
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Document actual = builder.parse(new StringReader(xmlInput), baseUri);

        assertEquals(expected.html(), actual.html());
        assertEquals(errorList.size(), builder.errors().size());
        assertEquals(errorList, builder.errors());
    }

    /**
     * Test method for {@link XmlTreeBuilder#parse(String, String)}.
     */
    @Test
    public void testParseString() {
        String xmlInput = "<root><element>Hello World</element></root>";
        String baseUri = "http://example.com";

        Document expected = Jsoup.parse(xmlInput, baseUri);

        ParseErrorList errorList = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase();
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Document actual = builder.parse(xmlInput, baseUri);

        assertEquals(expected.html(), actual.html());
        assertEquals(errorList.size(), builder.errors().size());
        assertEquals(errorList, builder.errors());
    }

    /**
     * Test method for {@link XmlTreeBuilder#initialiseParse(Reader, String, ParseErrorList, ParseSettings)}.
     */
    @Test (expected = AssertionError.class)
    public void testInitialiseParseInvalidBaseUri() {
        String xmlInput = "<root><element>Hello World</element></root>";
        String baseUri = null;

        ParseErrorList errorList = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase();
        XmlTreeBuilder builder = new XmlTreeBuilder();

        builder.initialiseParse(new StringReader(xmlInput), baseUri, errorList, settings);
    }

    /**
     * Test method for {@link XmlTreeBuilder#parse(Reader, String, ParseErrorList, ParseSettings)}.
     */
    @Test (expected = AssertionError.class)
    public void testParseReaderNullInput() {
        String baseUri = "http://example.com";
        ParseErrorList errorList = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase();
        XmlTreeBuilder builder = new XmlTreeBuilder();

        builder.parse(null, baseUri, errorList, settings);
    }

    /**
     * Test method for {@link XmlTreeBuilder#process(Token)}.
     */
    @Test
    public void testProcessStartTag() {
        String xmlInput = "<root><element><subElement/></element></root>";
        String baseUri = "http://example.com";

        ParseErrorList errorList = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase();
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Document doc = builder.parse(new StringReader(xmlInput), baseUri);

        Element rootElement = doc.element("root");
        Element element = rootElement.element("element");
        Element subElement = element.element("subElement");

        Token startTagToken = doc.outputSettings().getToken(0, 0);
        assertNotNull(startTagToken);
        assertEquals("root", startTagToken.asStartTag().name());

        builder.process(startTagToken);

        assertEquals(1, doc.childNodes().size());
        assertEquals("root", doc.childNodes().get(0).nodeName());
    }

    /**
     * Test method for {@link XmlTreeBuilder#popStackToClose(Token.EndTag)}.
     */
    @Test
    public void testPopStackToClose() {
        String xmlInput = "<root><element></element></root>";
        String baseUri = "http://example.com";

        ParseErrorList errorList = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase();
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Document doc = builder.parse(new StringReader(xmlInput), baseUri);

        Element rootElement = doc.element("root");
        Element element = rootElement.element("element");

        Token startTagToken = doc.outputSettings().getToken(0, 0);
        Token endTagToken = doc.outputSettings().getToken(1, 0);

        builder.popStackToClose(endTagToken);

        assertEquals(1, doc.childNodes().size());
        assertEquals("root", doc.childNodes().get(0).nodeName());
    }

    /**
     * Test method for {@link XmlTreeBuilder#parseFragment(String, String, ParseErrorList, ParseSettings)}.
     */
    @Test
    public void testParseFragment() {
        String xmlFragment = "<root><element>Hello World</element></root>";
        String baseUri = "http://example.com";

        ParseErrorList errorList = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase();
        XmlTreeBuilder builder = new XmlTreeBuilder();

        List<Node> expectedNodes = Jsoup.parse(xmlFragment, baseUri).childNodes();
        List<Node> actualNodes = builder.parseFragment(xmlFragment, baseUri, errorList, settings);

        assertEquals(expectedNodes.size(), actualNodes.size());
        for (int i = 0; i < expectedNodes.size(); i++) {
            Node expectedNode = expectedNodes.get(i);
            Node actualNode = actualNodes.get(i);
            assertEquals(expectedNode.nodeName(), actualNode.nodeName());
            assertEquals(expectedNode.text(), actualNode.text());
            assertEquals(expectedNode.attributes().size(), actualNode.attributes().size());
            for (String name : expectedNode.attributes().keySet()) {
                assertEquals(expectedNode.attribute(name), actualNode.attribute(name));
            }
        }
    }
}