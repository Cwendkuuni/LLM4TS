package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.ParserConfigurationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class W3CDomTest {

    @Test
    public void testFromJsoup() throws ParserConfigurationException {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><p>Hello World</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        Assert.assertNotNull(w3cDocument);
        Element rootElement = w3cDocument.getDocumentElement();
        assertNotNull(rootElement);
        assertEquals("html", rootElement.getTagName());

        Element bodyElement = (Element) rootElement.getElementsByTagName("body").item(0);
        assertNotNull(bodyElement);
        assertEquals("body", bodyElement.getTagName());

        Text textNode = (Text) bodyElement.getElementsByTagName("p").item(0).getFirstChild();
        assertNotNull(textNode);
        assertEquals("Hello World", textNode.getNodeValue());
    }

    @Test
    public void testAsString() {
        Document w3cDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element rootElement = w3cDocument.createElement("html");
        Element bodyElement = w3cDocument.createElement("body");
        Text textNode = w3cDocument.createTextNode("Hello World");

        rootElement.appendChild(bodyElement);
        bodyElement.appendChild(textNode);
        w3cDocument.appendChild(rootElement);

        String w3cString = new W3CDom().asString(w3cDocument);

        assertEquals("<html><body>Hello World</body></html>", w3cString);
    }
}