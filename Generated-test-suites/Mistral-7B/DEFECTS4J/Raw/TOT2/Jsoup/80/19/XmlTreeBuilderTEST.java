package org.jsoup.parser; // or the package where XmlTreeBuilder is located

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = XmlTreeBuilder.defaultSettings();
        String xmlString = "<root><element attribute=\"value\"></element></root>";
        Document document = XmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
        Element root = document.rootElement();
        Element element = root.child(0);
        assertEquals("value", element.attribute("attribute"));
    }

    @Test
    public void testParse() {
        String xmlString = "<root><element attribute=\"value\"></element></root>";
        Document document = XmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
        assertEquals("root", document.tagName());
        Element root = document.rootElement();
        Element element = root.child(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attribute"));
    }

    @Test
    public void testInitialiseParse() {
        String xmlString = "<root><element attribute=\"value\"></element></root>";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "http://example.com", errors, settings);
        Document document = xmlTreeBuilder.doc;
        assertEquals("root", document.tagName());
        Element root = document.rootElement();
        Element element = root.child(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attribute"));
    }

    @Test
    public void testProcessStartTag() {
        String xmlString = "<root><element attribute=\"value\"></element></root>";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
        Element root = document.rootElement();
        Element element = root.child(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attribute"));

        Token startTagToken = new Token.StartTag("element", null, null, null, null, null);
        Element newElement = XmlTreeBuilder.this.insert(startTagToken);
        assertEquals("element", newElement.tagName());
        assertEquals("value", newElement.attribute("attribute"));
    }

    @Test
    public void testProcessEndTag() {
        String xmlString = "<root><element><subElement attribute=\"value\"></subElement></element></root>";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
        Element root = document.rootElement();
        Element element = root.child(0);
        Element subElement = element.child(0);

        Token endTagToken = new Token.EndTag("subElement");
        XmlTreeBuilder.this.popStackToClose(endTagToken);
        Element newElement = stack.get(stack.size() - 1);
        assertEquals("subElement", newElement.tagName());
    }

    @Test(expected = RuntimeException.class)
    public void testValidateFail() {
        String xmlString = "<root></root>"; // this xml does not contain any tag or comment
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        XmlTreeBuilder.this.initialiseParse(new StringReader(xmlString), "http://example.com", errors, settings);
        XmlTreeBuilder.this.runParser();
    }
}