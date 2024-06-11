package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.namespace.QName;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class W3CDomTest {

    @Test
    public void testFromJsoup() throws ParserConfigurationException {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse(html);

        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        Assert.assertNotNull(w3cDocument);
    }

    @Test
    public void testConvert() throws ParserConfigurationException, TransformerException {
        String html = "<html xmlns:test=\"http://test.com\"><head><title>Test</title></head><body xmlns:foo=\"http://foo.com\"><p xmlns:bar=\"http://bar.com\" xmlns:baz=\"http://baz.com\">Hello World</p></body></html>";
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse(html);

        Document w3cDocument = DocumentBuilderFactory.newInstance().newDocument();
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Element rootElement = w3cDocument.getDocumentElement();
        Assert.assertEquals("html", rootElement.getTagName());

        Map<String, String> namespaces = namespaces(rootElement);
        Assert.assertEquals("http://test.com", namespaces.get("test"));
        Assert.assertEquals("http://foo.com", namespaces.get("foo"));
        Assert.assertEquals("http://bar.com", namespaces.get("bar"));
        Assert.assertEquals("http://baz.com", namespaces.get("baz"));

        Element pElement = getElementByTagName(rootElement, "p");
        Assert.assertEquals("p", pElement.getTagName());

        Text textNode = pElement.getTextContent().getTextContent();
        Assert.assertEquals("Hello World", textNode.getData());
    }

    @Test
    public void testAsString() throws ParserConfigurationException, TransformerException {
        String html = "<html xmlns:test=\"http://test.com\"><head><title>Test</title></head><body xmlns:foo=\"http://foo.com\"><p xmlns:bar=\"http://bar.com\" xmlns:baz=\"http://baz.com\">Hello World</p></body></html>";
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse(html);

        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        String w3cString = new W3CDom().asString(w3cDocument);

        Document parsedW3cDocument = DocumentBuilderFactory.newInstance().newDocument();
        DocumentBuilderFactory.newInstance().newDocumentBuilder().parseNewDocument(new DOMSource(new StringReader(w3cString)));
        Document w3cDocumentParsed = parsedW3cDocument.getDocumentElement();

        Assert.assertEquals(w3cDocumentParsed.getDocumentElement().getTagName(), w3cDocument.getDocumentElement().getTagName());
    }

    private Element getElementByTagName(Element parentElement, String tagName) {
        NodeTraversor nodeTraversor = new NodeTraversor();
        nodeTraversor.addVisitor(new NodeVisitor() {
            @Override
            public void head(Node node, int position) {
                if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals(tagName)) {
                    return;
                }
            }

            @Override
            public void tail(Node node, int position) {
                // do nothing
            }
        });
        nodeTraversor.traverse(parentElement);
        return (Element) nodeTraversor.getCurrentNode();
    }

    private Map<String, String> namespaces(Element element) {
        NodeIterator ni = element.getAttributes().getNamedItemNodes();
        Map<String, String> result = new HashMap<>();
        while (ni.hasNext()) {
            Node node = ni.next();
            if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("xmlns")) {
                String prefix = node.getNodeName().substring(6);
                String uri = node.getTextContent().getTextContent();
                result.put(prefix, uri);
            }
        }
        return result;
    }
}