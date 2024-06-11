package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.w3c.dom.Comment;
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
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><h1 id='test'>Test</h1></body></html>");
        Document w3cDoc = W3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() throws ParserConfigurationException {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html xmlns:test='http://www.example.com'><body><h1 xmlns:foo='http://www.example.org' id='test'>Test</h1></body></html>");
        Document w3cDoc = W3CDom.newDocument();
        W3CDom.convert(jsoupDoc, w3cDoc);

        Element html = w3cDoc.getDocumentElement();
        assertNotNull(html);
        assertEquals("html", html.getTagName());

        assertEquals(1, html.getChildNodes().getLength());

        Element body = (Element) html.getChildNodes().item(0);
        assertEquals("body", body.getTagName());

        assertEquals(1, body.getChildNodes().getLength());

        Element h1 = (Element) body.getChildNodes().item(0);
        assertEquals("h1", h1.getTagName());

        assertEquals("test", h1.getAttribute("id"));

        Map<String, String> namespaces = namespaces(html);
        assertEquals(2, namespaces.size());
        assertEquals("http://www.example.com", namespaces.get("test"));
        assertEquals("http://www.example.org", namespaces.get("foo"));
    }

    @Test
    public void testAsString() throws ParserConfigurationException, TransformerException {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><h1 id='test'>Test</h1></body></html>");
        Document w3cDoc = W3CDom.fromJsoup(jsoupDoc);
        String xmlString = W3CDom.asString(w3cDoc);

        assertTrue(xmlString.contains("<html>"));
        assertTrue(xmlString.contains("<body>"));
        assertTrue(xmlString.contains("<h1 id='test'>Test</h1>"));
        assertTrue(xmlString.contains("</body>"));
        assertTrue(xmlString.contains("</html>"));
    }

    private Map<String, String> namespaces(Element element) {
        Stack<HashMap<String, String>> stack = new Stack<>();
        HashMap<String, String> map = new HashMap<>();
        stack.push(map);

        NodeVisitor visitor = new NodeVisitor() {
            @Override
            public void head(Node node, int depth) {
                if (node instanceof Element) {
                    HashMap<String, String> namespaces = stack.peek();
                    updateNamespaces((Element) node, namespaces);
                }
            }
        };

        NodeTraversor.traverse(visitor, element);
        return stack.peek();
    }

    private void updateNamespaces(Element element, Map<String, String> namespaces) {
        Attributes attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Attribute attribute = attributes.item(i);

            String key = attribute.getNodeName();
            if (key.equals("xmlns")) {
                namespaces.put("", attribute.getNodeValue());
            } else if (key.startsWith("xmlns:")) {
                String prefix = key.substring(6);
                namespaces.put(prefix, attribute.getNodeValue());
            }
        }
    }
}