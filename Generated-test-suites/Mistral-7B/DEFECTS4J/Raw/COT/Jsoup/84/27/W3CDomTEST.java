package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.jaxb.JAXBElement;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class W3CDomTest {

    private static final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        Assert.assertNotNull(w3cDoc);
        Assert.assertEquals("html", w3cDoc.getDocumentElement().getTagName());

        Element body = w3cDoc.getDocumentElement().getElementsByTagName("body").item(0);
        Assert.assertEquals("body", body.getTagName());

        Element h1 = (Element) body.getElementsByTagName("h1").item(0);
        Assert.assertEquals("h1", h1.getTagName());
        Assert.assertEquals("Hello World", h1.getTextContent());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><h1 xmlns:foo='http://example.com' xmlns:bar='http://other.example.com'>Hello World</h1></body></html>");
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocument();
        new W3CDom().convert(jsoupDoc, w3cDoc);

        Element h1 = (Element) w3cDoc.getDocumentElement().getElementsByTagName("h1").item(0);
        Assert.assertEquals("h1", h1.getTagName());
        Assert.assertNotNull(h1.getAttribute("xmlns:foo"));
        Assert.assertNotNull(h1.getAttribute("xmlns:bar"));
    }

    @Test(expected = IllegalStateException.class)
    public void testFromJsoupWithNullDocument() {
        new W3CDom().fromJsoup(null);
    }

    @Test
    public void testAsString() {
        Document w3cDoc = factory.newDocument();
        Element root = w3cDoc.createElement("html");
        Element body = w3cDoc.createElement("body");
        Element h1 = w3cDoc.createElement("h1");
        h1.setTextContent("Hello World");
        root.appendChild(body);
        body.appendChild(h1);
        w3cDoc.appendChild(root);

        String expected = "<html><body><h1>Hello World</h1></body></html>";
        String result = new W3CDom().asString(w3cDoc);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAsStringWithComments() {
        Document w3cDoc = factory.newDocument();
        Element root = w3cDoc.createElement("html");
        Element body = w3cDoc.createElement("body");
        Element h1 = w3cDoc.createElement("h1");
        h1.setTextContent("Hello World");
        root.appendChild(body);
        body.appendChild(h1);

        Comment comment = w3cDoc.createComment("This is a comment");
        body.appendChild(comment);

        String expected = "<html><body><h1>Hello World</h1><!--This is a comment--></body></html>";
        String result = new W3CDom().asString(w3cDoc);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAsStringWithTextNodes() {
        Document w3cDoc = factory.newDocument();
        Element root = w3cDoc.createElement("html");
        Element body = w3cDoc.createElement("body");
        Text text1 = w3cDoc.createTextNode("Hello ");
        Text text2 = w3cDoc.createTextNode("World");
        root.appendChild(body);
        body.appendChild(text1);
        body.appendChild(text2);

        String expected = "<html><body>Hello World</body></html>";
        String result = new W3CDom().asString(w3cDoc);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAsStringWithAttribute() {
        Document w3cDoc = factory.newDocument();
        Element root = w3cDoc.createElement("html");
        Element body = w3cDoc.createElement("body");
        Element h1 = w3cDoc.createElement("h1");
        h1.setTextContent("Hello World");
        h1.setAttribute("class", "greeting");
        root.appendChild(body);
        body.appendChild(h1);
        w3cDoc.appendChild(root);

        String expected = "<html><body><h1 class='greeting'>Hello World</h1></body></html>";
        String result = new W3CDom().asString(w3cDoc);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAsStringWithEmptyDocument() {
        Document w3cDoc = factory.newDocument();

        String expected = "<html></html>";
        String result = new W3CDom().asString(w3cDoc);

        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testAsStringWithNullDocument() {
        String result = new W3CDom().asString(null);
    }

}