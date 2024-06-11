package org.jsoup.helper;

import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.w3c.dom.Document;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Stack;

/**
 * Test class for W3CDom.java
 */
public class W3CDomTest {

    /**
     * Test method for W3CDom.fromJsoup(org.jsoup.nodes.Document)
     */
    @Test
    public void testFromJsoup() {
        W3CDom w3CDom = new W3CDom();

        Document jsoupDocument = Document.fromUrl("http://example.com");
        Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);

        Document expectedDocument = getExpectedW3CDocument();
        Assert.assertEquals(expectedDocument.getDocumentElement().getTagName(), w3cDocument.getDocumentElement().getTagName());
        Assert.assertEquals(expectedDocument.getDocumentElement().getChildNodes().getLength(), w3cDocument.getDocumentElement().getChildNodes().getLength());

        for (int i = 0; i < expectedDocument.getDocumentElement().getChildNodes().getLength(); i++) {
            Node expectedChild = expectedDocument.getDocumentElement().getChildNodes().item(i);
            Node w3cChild = w3cDocument.getDocumentElement().getChildNodes().item(i);

            Assert.assertEquals(expectedChild.getNodeType(), w3cChild.getNodeType());
            if (expectedChild.getNodeType() == Node.ELEMENT_NODE) {
                Element expectedElement = (Element) expectedChild;
                Element w3cElement = (Element) w3cChild;

                Assert.assertEquals(expectedElement.getTagName(), w3cElement.getTagName());
                Assert.assertEquals(expectedElement.getAttribute("class"), w3cElement.getAttribute("class"));
                Assert.assertEquals(expectedElement.getTextContent(), w3cElement.getTextContent());

                for (int j = 0; j < expectedElement.getAttributes().getLength(); j++) {
                    Attribute expectedAttribute = expectedElement.getAttributes().item(j);
                    Attribute w3cAttribute = w3cElement.getAttributes().getNamedItem(expectedAttribute.getName());

                    Assert.assertEquals(expectedAttribute.getName(), w3cAttribute.getNodeName());
                    Assert.assertEquals(expectedAttribute.getValue(), w3cAttribute.getNodeValue());
                }
            }
        }
    }

    /**
     * Test method for W3CDom.convert(org.jsoup.nodes.Document, org.w3c.dom.Document)
     */
    @Test
    public void testConvert() {
        W3CDom w3CDom = new W3CDom();

        Document jsoupDocument = Document.fromUrl("http://example.com");
        Document w3cDocument = DocumentBuilderFactory.newInstance().newDocument();
        w3CDom.convert(jsoupDocument, w3cDocument);

        Document expectedDocument = getExpectedW3CDocument();
        Assert.assertEquals(expectedDocument.getDocumentElement().getTagName(), w3cDocument.getDocumentElement().getTagName());
        Assert.assertEquals(expectedDocument.getDocumentElement().getChildNodes().getLength(), w3cDocument.getDocumentElement().getChildNodes().getLength());

        for (int i = 0; i < expectedDocument.getDocumentElement().getChildNodes().getLength(); i++) {
            Node expectedChild = expectedDocument.getDocumentElement().getChildNodes().item(i);
            Node w3cChild = w3cDocument.getDocumentElement().getChildNodes().item(i);

            Assert.assertEquals(expectedChild.getNodeType(), w3cChild.getNodeType());
            if (expectedChild.getNodeType() == Node.ELEMENT_NODE) {
                Element expectedElement = (Element) expectedChild;
                Element w3cElement = (Element) w3cChild;

                Assert.assertEquals(expectedElement.getTagName(), w3cElement.getTagName());
                Assert.assertEquals(expectedElement.getTextContent(), w3cElement.getTextContent());

                for (int j = 0; j < expectedElement.getAttributes().getLength(); j++) {
                    Attribute expectedAttribute = expectedElement.getAttributes().item(j);
                    Attribute w3cAttribute = w3cElement.getAttributes().getNamedItem(expectedAttribute.getName());

                    Assert.assertEquals(expectedAttribute.getName(), w3cAttribute.getNodeName());
                    Assert.assertEquals(expectedAttribute.getValue(), w3cAttribute.getNodeValue());
                }
            }
        }
    }

    /**
     * Test method for W3CDom.asString(Document)
     */
    @Test
    public void testAsString() {
        W3CDom w3CDom = new W3CDom();

        Document jsoupDocument = Document.fromUrl("http://example.com");
        Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
        String w3cString = w3CDom.asString(w3cDocument);

        String expectedString = getExpectedString();
        Assert.assertEquals(expectedString, w3cString);
    }

    /**
     * Helper method to create an expected W3C document for testing
     *
     * @return Document
     */
    private Document getExpectedW3CDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element html = doc.createElement("html");
            html.setAttribute("xmlns", "http://www.w3.org/1999/xhtml");
            doc.appendChild(html);

            Element head = doc.createElement("head");
            html.appendChild(head);

            Element title = doc.createElement("title");
            title.setTextContent("Example Domain");
            head.appendChild(title);

            Element body = doc.createElement("body");
            html.appendChild(body);

            Element h1 = doc.createElement("h1");
            h1.setTextContent("Hello World!");
            body.appendChild(h1);

            Element p = doc.createElement("p");
            p.setTextContent("This is a paragraph.");
            body.appendChild(p);

            Element ul = doc.createElement("ul");
            body.appendChild(ul);

            Element li1 = doc.createElement("li");
            li1.setTextContent("List item 1");
            ul.appendChild(li1);

            Element li2 = doc.createElement("li");
            li2.setTextContent("List item 2");
            ul.appendChild(li2);

            return doc;
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Helper method to create an expected string for testing
     *
     * @return String
     */
    private String getExpectedString() {
        return "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>Example Domain</title></head><body><h1>Hello World!</h1><p>This is a paragraph.</p><ul><li>List item 1</li><li>List item 2</li></ul></body></html>";
    }
}