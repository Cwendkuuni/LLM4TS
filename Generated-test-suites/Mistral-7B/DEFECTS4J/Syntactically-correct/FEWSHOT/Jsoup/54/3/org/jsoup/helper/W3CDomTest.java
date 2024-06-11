package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.junit.Test;

public class W3CDomTest {
    @Test
    public void testFromJsoup() {
        Document jsoupDoc = org.jsoup.Jsoup.parse("<html><head></head><body><div id='test'>Hello World</div></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        assertEquals("<html><head></head><body><div id='test'>Hello World</div></body></html>", w3cDoc.getDocumentURI());

        Element root = w3cDoc.getDocumentElement();
        assertEquals("html", root.getTagName());

        Element body = (Element) root.getElementsByTagName("body").item(0);
        assertEquals("body", body.getTagName());

        Element div = (Element) body.getElementsByTagName("div").item(0);
        assertEquals("div", div.getTagName());
        assertEquals("Hello World", div.getTextContent());
        assertEquals("test", div.getAttribute("id"));
    }

    @Test
    public void testConvert() {
        Document jsoupDoc = org.jsoup.Jsoup.parse("<html><head></head><body><div id='test'>Hello World</div></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        W3CDom w3CDom = new W3CDom();
        w3CDom.convert(jsoupDoc, w3cDoc);

        assertEquals("<html><head></head><body><div id='test'>Hello World</div></body></html>", w3cDoc.getDocumentURI());

        Element root = w3cDoc.getDocumentElement();
        assertEquals("html", root.getTagName());

        Element body = (Element) root.getElementsByTagName("body").item(0);
        assertEquals("body", body.getTagName());

        Element div = (Element) body.getElementsByTagName("div").item(0);
        assertEquals("div", div.getTagName());
        assertEquals("Hello World", div.getTextContent());
        assertEquals("test", div.getAttribute("id"));
    }

    @Test
    public void testAsString() {
        Document w3cDoc = new W3CDom().fromJsoup(org.jsoup.Jsoup.parse("<html><head></head><body><div id='test'>Hello World</div></body></html>"));

        String w3cDocAsString = new W3CDom().asString(w3cDoc);

        assertEquals("<html><head></head><body><div id='test'>Hello World</div></body></html>", w3cDocAsString);
    }

    @Test
    public void testW3CBuilder() {
        Document w3cDoc = new Document();
        W3CDom w3CDom = new W3CDom();

        NodeTraversor traversor = new NodeTraversor(new NodeVisitor() {
            @Override
            public void head(Node node, int depth) {
                if (node instanceof TextNode) {
                    Text text = w3cDoc.createTextNode("Test Text " + depth);
                    w3cDoc.getDocumentElement().appendChild(text);
                }
            }

            @Override
            public void tail(Node node, int depth) {
                // no-op
            }
        });

        traversor.traverse(new Element("root"));

        w3CDom.convert(new org.jsoup.nodes.Document(), w3cDoc);

        assertEquals("Test Text 0Test Text 1", w3cDoc.getDocumentElement().getTextContent());

        for (int i = 0; i < 2; i++) {
            Element child = (Element) w3cDoc.getDocumentElement().getElementsByTagName("*").item(i + 1);
            assertEquals("", child.getTagName());
            assertEquals("Test Text " + (i + 1), child.getTextContent());
        }
    }
}