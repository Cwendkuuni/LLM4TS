package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;

/**
 * JUnit test file for XmlTreeBuilder class.
 */
public class XmlTreeBuilderTest {

    @Test
    public void testXmlTreeBuilderDefaultSettings() {
        String xmlString = "<root><child1><child2></child2></child1></root>";
        Document xmlDocument = Jsoup.parse(xmlString, Parser.xmlParser());
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document parsedXmlDocument = xmlTreeBuilder.parse(xmlString, null);
        assertDocumentEquals(xmlDocument, parsedXmlDocument);
    }

    @Test
    public void testXmlTreeBuilderWithBaseUri() {
        String xmlString = "<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><child1><child2></child2></child1></root>";
        Document xmlDocument = Jsoup.parse(xmlString, Parser.xmlParser());
        String baseUri = "http://example.com/xml";
        Document parsedXmlDocument = new XmlTreeBuilder().parse(xmlString, baseUri);
        assertDocumentEquals(xmlDocument, parsedXmlDocument);
        Element rootElement = parsedXmlDocument.select("root").first();
        assertEquals("http://example.com/xml", rootElement.baseUri());
    }

    @Test
    public void testXmlTreeBuilderWithParseSettings() {
        String xmlString = "<root><child1><child2></child2></child1></root>";
        ParseSettings parseSettings = ParseSettings.xml();
        Document xmlDocument = Jsoup.parse(xmlString, Parser.xmlParser(), parseSettings);
        Document parsedXmlDocument = new XmlTreeBuilder().parse(xmlString, null, parseSettings);
        assertDocumentEquals(xmlDocument, parsedXmlDocument);
    }

    @Test
    public void testXmlTreeBuilderWithParseErrorList() {
        String xmlStringWithError = "<root><child1><child2></child2><invalidTag>invalidTag</invalidTag></child1></root>";
        ParseErrorList parseErrorList = new ParseErrorList();
        Document xmlDocument = Jsoup.parse(xmlStringWithError, Parser.xmlParser(), parseErrorList);
        Document parsedXmlDocument = new XmlTreeBuilder().parse(xmlStringWithError, null, ParseSettings.preserveCase, parseErrorList);
        assertDocumentEquals(xmlDocument, parsedXmlDocument);
        List<ParseError> parseErrors = xmlDocument.outputSettings().parseErrors();
        assertEquals(1, parseErrors.size());
        ParseError parseError = parseErrors.get(0);
        assertEquals("Encountered unexpected tag <invalidTag>", parseError.getMessage());
    }

    @Test
    public void testXmlTreeBuilderWithFragment() {
        String xmlFragmentString = "<root><child1><child2></child2></child1></root>";
        String xmlString = "<root><child><child2></child2></child></root>";
        Document xmlDocument = Jsoup.parse(xmlString, Parser.xmlParser());
        Document parsedXmlDocument = new XmlTreeBuilder().parseFragment(xmlFragmentString, null);
        assertDocumentEquals(xmlDocument.select("root").first(), parsedXmlDocument.rootElement());
    }

    @Test
    public void testXmlTreeBuilderWithSelfClosingTag() {
        String xmlString = "<root><child1><child2 /></child1></root>";
        Document xmlDocument = Jsoup.parse(xmlString, Parser.xmlParser());
        Element rootElement = xmlDocument.select("root").first();
        assertNotNull(rootElement.child(0));
        assertFalse(rootElement.child(0).hasChildren());
        Document parsedXmlDocument = new XmlTreeBuilder().parse(xmlString, null);
        Element parsedRootElement = parsedXmlDocument.select("root").first();
        assertNotNull(parsedRootElement.child(0));
        assertFalse(parsedRootElement.child(0).hasChildren());
    }

    @Test
    public void testXmlTreeBuilderWithComment() {
        String xmlString = "<root><!-- This is a comment --><child1><child2></child2></child1></root>";
        Document xmlDocument = Jsoup.parse(xmlString, Parser.xmlParser());
        List<Node> xmlComments = xmlDocument.getAllElements().select("*[not(self::comment())]").select("comment()");
        Document parsedXmlDocument = new XmlTreeBuilder().parse(xmlString, null);
        List<Node> parsedComments = parsedXmlDocument.getAllElements().select("*[not(self::comment())]").select("comment()");
        assertEquals(xmlComments.size(), parsedComments.size());
    }

    @Test
    public void testXmlTreeBuilderWithDoctype() {
        String xmlString = "<!DOCTYPE root SYSTEM \"root.dtd\">";
        Document xmlDocument = Jsoup.parse(xmlString, Parser.xmlParser());
        Document parsedXmlDocument = new XmlTreeBuilder().parse(xmlString, null);
        DocumentType xmlDoctype = xmlDocument.doctype();
        DocumentType parsedDoctype = parsedXmlDocument.doctype();
        assertEquals(xmlDoctype.systemIdentifier(), parsedDoctype.systemIdentifier());
        assertEquals(xmlDoctype.publicIdentifier(), parsedDoctype.publicIdentifier());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXmlTreeBuilderWithInvalidInput() {
        new XmlTreeBuilder().parse("invalidInput", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXmlTreeBuilderWithInvalidBaseUri() {
        new XmlTreeBuilder().parse("<?xml version=\"1.0\"?>\r\n<root><child1><child2></child2></child1></root>", null, null, "invalidBaseUri");
    }

    @Test(expected = NullPointerException.class)
    public void testXmlTreeBuilderWithNullInput() {
        new XmlTreeBuilder().parse(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testXmlTreeBuilderWithNullBaseUri() {
        new XmlTreeBuilder().parse("<root><child1><child2></child2></child1></root>", null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testXmlTreeBuilderWithNullParseErrorList() {
        new XmlTreeBuilder().parse("<root><child1><child2></child2></child1></root>", null, null, null, null);
    }

    private void assertDocumentEquals(Document expectedDocument, Document actualDocument) {
        List<Node> expectedChildren = expectedDocument.childNodes();
        List<Node> actualChildren = actualDocument.childNodes();
        assertEquals(expectedChildren.size(), actualChildren.size());
        for (int i = 0; i < expectedChildren.size(); i++) {
            Node expectedChild = expectedChildren.get(i);
            Node actualChild = actualChildren.get(i);
            if (expectedChild instanceof Element) {
                Element expectedElement = (Element) expectedChild;
                Element actualElement = (Element) actualChild;
                assertElementEquals(expectedElement, actualElement);
            } else if (expectedChild instanceof TextNode) {
                TextNode expectedTextNode = (TextNode) expectedChild;
                TextNode actualTextNode = (TextNode) actualChild;
                assertEquals(expectedTextNode.text(), actualTextNode.text());
            } else if (expectedChild instanceof DocumentType) {
                DocumentType expectedDoctype = (DocumentType) expectedChild;
                DocumentType actualDoctype = (DocumentType) actualChild;
                assertEquals(expectedDoctype.name(), actualDoctype.name());
                assertEquals(expectedDoctype.systemIdentifier(), actualDoctype.systemIdentifier());
                assertEquals(expectedDoctype.publicIdentifier(), actualDoctype.publicIdentifier());
            } else if (expectedChild instanceof Comment) {
                Comment expectedComment = (Comment) expectedChild;
                Comment actualComment = (Comment) actualChild;
                assertEquals(expectedComment.data(), actualComment.data());
            } else {
                fail("Unexpected node type: " + expectedChild.getClass().getName());
            }
        }
    }

    private void assertElementEquals(Element expectedElement, Element actualElement) {
        assertEquals(expectedElement.nodeName(), actualElement.nodeName());
        assertEquals(expectedElement.tagName(), actualElement.tagName());
        assertEquals(expectedElement.baseUri(), actualElement.baseUri());
        List<Attribute> expectedAttributes = expectedElement.attributes();
        List<Attribute> actualAttributes = actualElement.attributes();
        assertEquals(expectedAttributes.size(), actualAttributes.size());
        for (Attribute expectedAttribute : expectedAttributes) {
            Attribute actualAttribute = actualAttributes.get(expectedAttribute.getKey());
            assertEquals(expectedAttribute.getKey(), actualAttribute.getKey());
            assertEquals(expectedAttribute.getValue(), actualAttribute.getValue());
        }
    }
}