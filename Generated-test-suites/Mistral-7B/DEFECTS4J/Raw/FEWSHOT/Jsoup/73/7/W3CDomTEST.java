package org.jsoup.helper;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextDocument;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        String html = "<html><head></head><body><p>Hello World</p></body></html>";
        Document jsoupDoc = new TextDocument(html);
        Document w3cDoc = W3CDom.fromJsoup(jsoupDoc);

        assertEquals("html", w3cDoc.getDocumentElement().getTagName());
        assertEquals(1, w3cDoc.getDocumentElement().getChildNodes().getLength());

        Element head = (Element) w3cDoc.getDocumentElement().getElementsByTagName("head").item(0);
        assertEquals(0, head.getChildNodes().getLength());

        Element body = (Element) w3cDoc.getDocumentElement().getElementsByTagName("body").item(0);
        assertEquals(1, body.getChildNodes().getLength());
        Element p = (Element) body.getChildNodes().item(0);
        assertEquals("p", p.getTagName());
        assertEquals("Hello World", p.getTextContent());
    }

    @Test
    public void testConvert() {
        String html = "<html><head></head><body><p>Hello World</p></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        Document convertedDoc = new W3CDom().convert(jsoupDoc, w3cDoc);

        assertEquals(w3cDoc.getDocumentElement(), convertedDoc.getDocumentElement());

        NodeList jsoupChildren = jsoupDoc.getDocumentElement().childNodes();
        NodeList w3cChildren = w3cDoc.getDocumentElement().getChildNodes();

        assertEquals(jsoupChildren.getLength(), w3cChildren.getLength());

        for (int i = 0; i < jsoupChildren.getLength(); i++) {
            Node jsoupChild = jsoupChildren.item(i);
            Node w3cChild = w3cChildren.item(i);

            assertEquals(jsoupChild.getNodeType(), w3cChild.getNodeType());

            if (jsoupChild.getNodeType() == Node.ELEMENT_NODE) {
                Element jsoupElement = (Element) jsoupChild;
                Element w3cElement = (Element) w3cChild;

                assertEquals(jsoupElement.tagName(), w3cElement.getTagName());
                assertEquals(jsoupElement.attributes().size(), w3cElement.getAttributes().getLength());

                for (String key : jsoupElement.attributes().keySet()) {
                    Attribute jsoupAttribute = jsoupElement.attribute(key);
                    Attr w3cAttribute = (Attr) w3cElement.getAttributeNode(key);

                    assertEquals(jsoupAttribute.getValue(), w3cAttribute.getValue());
                }
            }
        }
    }

    @Test
    public void testAsString() {
        String html = "<html><head></head><body><p>Hello World</p></body></html>";
        Document w3cDoc = W3CDom.fromJsoup(Jsoup.parse(html));
        String w3cString = new W3CDom().asString(w3cDoc);

        String jsoupString = Jsoup.parse(w3cString).body().text();

        assertEquals(html, jsoupString);
    }
}