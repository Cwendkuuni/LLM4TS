package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.processing.NSResolver;
import org.xml.namespace.QName;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

public class W3CDomTest {

    private static DocumentBuilderFactory factory;
    private static DocumentBuilder builder;
    private static Document jsoupDocument;
    private static Document w3cDocument;

    @Before
    public void setUp() throws Exception {
        factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
        String xml = "<html><body><h1 id=\"title\">Test</h1><p>Hello World!</p></body></html>";
        jsoupDocument = org.jsoup.Jsoup.parse(xml);
        w3cDocument = W3CDom.fromJsoup(jsoupDocument);
    }

    @Test
    public void testFromJsoup() {
        assertNotNull(w3cDocument);
    }

    @Test
    public void testFromJsoupEmptyDocument() {
        org.jsoup.nodes.Document emptyDocument = org.jsoup.nodes.Document.empty();
        Document emptyW3CDocument = W3CDom.fromJsoup(emptyDocument);
        assertEquals("", emptyW3CDocument.getDocumentURI());
        assertNull(emptyW3CDocument.getDocumentElement());
    }

    @Test
    public void testConvert() throws Exception {
        assertEquals(jsoupDocument.documentElement().getTagName(), w3cDocument.getDocumentElement().getTagName());
        assertEquals(jsoupDocument.childNodes().getLength(), w3cDocument.getChildNodes().getLength());

        NodeTraversor traversal = new NodeTraversor();
        traversal.addVisitor(new NodeVisitor() {
            @Override
            public void head(Node node, int depth) {
                if (node instanceof Element) {
                    Element element = (Element) node;
                    assertEquals(element.tagName(), element.getTagName());
                    assertEquals(element.getTextContent(), element.getTextContent());

                    for (Attribute attribute : element.getAttributes()) {
                        assertEquals(attribute.getName(), attribute.getNodeName());
                        assertEquals(attribute.getValue(), attribute.getTextContent());
                    }

                    assertEquals(element.getNamespaceURI(), element.getNamespaceURI());
                    assertEquals(element.getPrefix(), element.getPrefix());
                }
            }

            @Override
            public void tail(Node node, int depth) {}
        });

        NodeTraversor.traverse(traversal, jsoupDocument.childNodes().get(0));
        NodeTraversor.traverse(traversal, w3cDocument.getChildNodes().get(0));
    }

    @Test
    public void testAsString() throws Exception {
        String w3cString = W3CDom.asString(w3cDocument);
        String jsoupString = jsoupDocument.toString();
        assertEquals(jsoupString, w3cString);
    }

    @Test
    public void testNamespaceResolution() throws Exception {
        String xpathExpression = "//*:h1";
        XPathFactory xPathfactory = XPathFactory.newInstance();
        NSResolver nsResolver = new NSResolver() {
            @Override
            public String getNamespaceURI(String prefix) {
                if (prefix.equals("xmlns")) {
                    return "http://www.w3.org/2000/xmlns/";
                }
                return null;
            }

            @Override
            public String getPrefix(String uri) {
                return null;
            }
        };

        Object result = xPathfactory.newXPath().compile(xpathExpression).evaluate(w3cDocument, nsResolver);
        assertEquals(XPathConstants.NODE, result);

        Element h1Element = (Element) result;
        assertEquals("title", h1Element.getTagName());
    }
}