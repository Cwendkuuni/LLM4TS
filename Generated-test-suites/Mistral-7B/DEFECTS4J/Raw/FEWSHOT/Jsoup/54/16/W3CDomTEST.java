package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head/><body><p>Hello World</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        Assert.assertNotNull(w3cDocument);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head/><body><p>Hello World</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Assert.assertEquals(1, w3cDocument.getDocumentElement().getChildNodes().getLength());
        Element body = (Element) w3cDocument.getDocumentElement().getChildNodes().item(0);
        Assert.assertEquals(1, body.getChildNodes().getLength());
        Text textNode = (Text) body.getChildNodes().item(0);
        Assert.assertEquals("Hello World", textNode.getNodeValue());
    }

    @Test
    public void testAsString() {
        Document w3cDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element html = w3cDocument.createElement("html");
        Element head = w3cDocument.createElement("head");
        Element title = w3cDocument.createElement("title");
        title.setTextNode("Test Title");
        head.appendChild(title);
        html.appendChild(head);

        Element body = w3cDocument.createElement("body");
        Text textNode = w3cDocument.createTextNode("Test Body");
        body.appendChild(textNode);
        html.appendChild(body);

        w3cDocument.appendChild(html);

        String xmlString = new W3CDom().asString(w3cDocument);
        Assert.assertNotNull(xmlString);
        Assert.assertTrue(xmlString.contains("<title>Test Title</title>"));
        Assert.assertTrue(xmlString.contains("<body><textNode>Test Body</textNode></body>"));
    }

    @Test
    public void testNamespaces() {
        Document w3cDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element html = w3cDocument.createElementNS("http://www.w3.org/1999/xhtml", "html");
        Element head = w3cDocument.createElementNS("http://www.w3.org/1999/xhtml", "head");
        Element title = w3cDocument.createElementNS("http://www.w3.org/2005/Atom", "title");
        title.setTextNode("Test Title");
        head.appendChild(title);
        html.appendChild(head);

        w3cDocument.appendChild(html);

        W3CDom w3CDom = new W3CDom();
        Document jsoupDocument = w3CDom.fromJsoup(org.jsoup.Jsoup.parse(w3CDom.asString(w3cDocument)));
        NodeTraversor traversor = new NodeTraversor(new NodeVisitor() {
            @Override
            public void head(Node node, int depth) {
                if (node instanceof org.jsoup.nodes.Element) {
                    Element el = (Element) node;
                    Map<String, String> namespaces = el.namespaces();
                    for (Map.Entry<String, String> namespaceEntry : namespaces.entrySet()) {
                        Assert.assertEquals(namespaceEntry.getValue(), new W3CDom().updateNamespaces(el).trim());
                    }
                }
            }

            @Override
            public void tail(Node node, int depth) {

            }
        });

        traversor.traverse(jsoupDocument.child(0));
    }
}