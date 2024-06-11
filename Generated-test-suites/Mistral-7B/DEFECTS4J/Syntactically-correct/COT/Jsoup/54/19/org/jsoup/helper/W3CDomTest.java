package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.parser.ParserConfigurationException;

import java.io.StringWriter;
import java.util.HashMap;

public class W3CDomTest {

    @Test
    public void testFromJsoup() throws ParserConfigurationException {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><div id='test'></div></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        Assert.assertNotNull(w3cDocument);
    }

    @Test
    public void testConvert() throws ParserConfigurationException {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><div id='test'><p>Hello World</p></div></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Assert.assertEquals("Hello World", w3cDocument.getElementsByTagName("p").item(0).getTextContent());
    }

    @Test
    public void testAsString() {
        String html = "<html><body><div id='test'><p>Hello World</p></div></body></html>";
        Document w3cDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new org.apache.xml.utils.XMLInputFactory().createXMLInput(new StringReader(html)));
        W3CDom w3CDom = new W3CDom();
        Document jsoupDocument = w3CDom.fromJsoup(w3cDocument);
        String w3cString = w3CDom.asString(jsoupDocument);

        Assert.assertEquals(html, w3cString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoupNullInput() {
        new W3CDom().fromJsoup(null);
    }

    @Test
    public void testConvertNullOut() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><div id='test'><p>Hello World</p></div></body></html>");
        new W3CDom().convert(jsoupDocument, null);
    }

    @Test
    public void testAsStringNullInput() {
        Document w3cDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        String w3cString = new W3CDom().asString(w3cDocument);

        Assert.assertNull(w3cString);
    }

    @Test
    public void testW3CBuilderNullInput() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body></body></html>");
        new W3CDom().convert(jsoupDocument, null);
    }

    @Test
    public void testW3CBuilderEmptyInput() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse(" ");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Assert.assertEquals("", w3cDocument.getDocumentElement().getNodeValue());
    }

    @Test
    public void testW3CBuilderComments() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><!-- test --><body><div id='test'><p>Hello World</p></div></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Comment comment = (Comment) w3cDocument.getElementsByTagName("!--").item(0);
        Assert.assertEquals(" test ", comment.getNodeValue());
    }

    @Test
    public void testW3CBuilderText() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><div id='test'><p>Hello World<br>Test</p></div></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Text text = (Text) w3cDocument.getElementsByTagName("p").item(0).getChildNodes().item(0);
        Assert.assertEquals("Hello World\nTest", text.getNodeValue());
    }

    @Test
    public void testW3CBuilderElementAttributes() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><div id='test' class='testClass'></div></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Element div = w3cDocument.getElementsByTagName("div").item(0);
        Assert.assertEquals("testClass", div.getAttribute("class"));
    }

    @Test
    public void testW3CBuilderElements() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><div id='test'><p>Hello World<br>Test</p></div></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Element div = w3cDocument.getElementsByTagName("div").item(0);
        Assert.assertEquals(1, div.getChildNodes().getLength());
        Assert.assertEquals("Hello World\nTest", div.getElementsByTagName("p").item(0).getTextContent());
    }

    @Test
    public void testW3CBuilderNestedElements() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><div id='test'><p><b>Hello World<i>Test</i></b></p></div></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Element div = w3cDocument.getElementsByTagName("div").item(0);
        Element p = (Element) div.getChildNodes().item(0);
        Assert.assertEquals(2, p.getChildNodes().getLength());
        Assert.assertEquals("<b>", p.getElementsByTagName("b").item(0).getNodeValue());
        Assert.assertEquals("Hello World", p.getElementsByTagName("i").item(0).getTextContent());
        Assert.assertEquals("</b>", p.getElementsByTagName("b").item(0).getNextSibling().getNodeValue());
    }

}