package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Text;

public class W3CDomTest {

    private W3CDom w3CDom;
    private org.jsoup.nodes.Document jsoupDoc;
    private org.w3c.dom.Document w3cDoc;

    @Before
    public void setup() {
        w3CDom = new W3CDom();
        jsoupDoc = Parser.parse("<!--comment--><h1>Heading</h1><p>Paragraph</p>");
        w3cDoc = w3CDom.fromJsoup(jsoupDoc);
    }

    @Test
    public void testFromJsoup() {
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        Elements elements = jsoupDoc.body().getAllElements();
        for (Element element : elements) {
            assertNotNull(convertElement(element));
        }
    }

    private org.w3c.dom.Node convertElement(Element element) {
        org.w3c.dom.Node node = null;
        if (element instanceof org.jsoup.nodes.Element) {
            org.jsoup.nodes.Element jsoupElement = (org.jsoup.nodes.Element) element;
            if (jsoupElement.tagName().equals("h1")) {
                node = w3cDoc.createElement("h1");
                assertEquals(element.tagName(), node.getNodeName());
            } else if (jsoupElement.tagName().equals("p")) {
                node = w3cDoc.createElement("p");
                assertEquals(element.tagName(), node.getNodeName());
            }
            convertAttributes(jsoupElement, (org.w3c.dom.Element) node);
        } else if (element instanceof org.jsoup.nodes.TextNode) {
            org.jsoup.nodes.TextNode textNode = (org.jsoup.nodes.TextNode) element;
            node = w3cDoc.createTextNode(textNode.getWholeText());
            assertEquals(element.getWholeText(), node.getNodeValue());
        } else if (element instanceof org.jsoup.nodes.Comment) {
            org.jsoup.nodes.Comment comment = (org.jsoup.nodes.Comment) element;
            node = w3cDoc.createComment(comment.getData());
            assertEquals(element.getData(), node.getNodeValue());
        } else if (element instanceof org.jsoup.nodes.DataNode) {
            org.jsoup.nodes.DataNode dataNode = (org.jsoup.nodes.DataNode) element;
            node = w3cDoc.createTextNode(dataNode.getWholeData());
            assertEquals(element.getWholeData(), node.getNodeValue());
        }
        return node;
    }

    private void convertAttributes(org.jsoup.nodes.Element jsoupElement, org.w3c.dom.Element w3cElement) {
        for (org.jsoup.nodes.Attribute attribute : jsoupElement.attributes()) {
            String key = attribute.getKey().replaceAll("[^-a-zA-Z0-9_:.]", "");
            if (key.matches("[a-zA-Z_:][-a-zA-Z0-9_:.]*")) {
                w3cElement.setAttribute(key, attribute.getValue());
                assertNotNull(w3cElement.getAttribute(key));
            }
        }
    }

    @Test
    public void testAsString() {
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE html><html><!--comment--><head/><body><h1>Heading</h1><p>Paragraph</p></body></html>";
        assertEquals(expected, w3CDom.asString(w3cDoc));
    }

}