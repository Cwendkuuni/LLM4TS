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

    private W3CDom w3CDom = new W3CDom();

    @Test
    public void testFromJsoup() throws ParserConfigurationException {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
        Assert.assertNotNull(w3cDocument);
    }

    @Test
    public void testConvert() throws ParserConfigurationException {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
        w3CDom.convert(jsoupDocument, w3cDocument);
        Assert.assertEquals(jsoupDocument.childNodes().item(0).nodeName(), w3cDocument.getDocumentElement().getTagName());
    }

    @Test
    public void testW3CBuilder_Element() {
        Document w3cDocument = w3CDom.fromJsoup(org.jsoup.Jsoup.parse("<html><body><h1 id='test'>Hello World</h1></body></html>"));
        Element root = w3cDocument.getDocumentElement();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDocument);
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div");
        jsoupElement.setAttribute("class", "test");
        org.jsoup.nodes.Element jsoupChildElement = new org.jsoup.nodes.Element("p");
        jsoupChildElement.setText("Child Element");
        jsoupElement.appendChild(jsoupChildElement);
        builder.head(jsoupElement, 0);
        Assert.assertEquals(root.getElementsByTagName("div").item(0).getTagName(), "div");
        Assert.assertEquals(root.getElementsByTagName("div").item(0).getAttributes().getNamedItem("class").getNodeValue(), "test");
        Assert.assertEquals(root.getElementsByTagName("div").item(0).getChildNodes().getLength(), 1);
        Assert.assertEquals(root.getElementsByTagName("div").item(0).getChildNodes().item(0).getNodeName(), "p");
        Assert.assertEquals(root.getElementsByTagName("div").item(0).getChildNodes().item(0).getChildNodes().getLength(), 0);
    }

    @Test
    public void testW3CBuilder_Text() {
        Document w3cDocument = w3CDom.fromJsoup(org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>"));
        Element root = w3cDocument.getDocumentElement();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDocument);
        org.jsoup.nodes.TextNode jsoupTextNode = new org.jsoup.nodes.TextNode("Test Text");
        builder.head(jsoupTextNode, 0);
        Assert.assertEquals(root.getDocumentElement().getChildNodes().getLength(), 1);
        Assert.assertEquals(root.getDocumentElement().getChildNodes().item(0).getNodeName(), "#text");
        Assert.assertEquals(root.getDocumentElement().getChildNodes().item(0).getNodeValue(), "Test Text");
    }

    @Test
    public void testW3CBuilder_Comment() {
        Document w3cDocument = w3CDom.fromJsoup(org.jsoup.Jsoup.parse("<html><!-- Test Comment --></html>"));
        Element root = w3cDocument.getDocumentElement();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDocument);
        org.jsoup.nodes.Comment jsoupComment = new org.jsoup.nodes.Comment("Test Comment");
        builder.head(jsoupComment, 0);
        Assert.assertEquals(root.getChildNodes().getLength(), 1);
        Assert.assertEquals(root.getChildNodes().item(0).getNodeName(), "#comment");
        Assert.assertEquals(root.getChildNodes().item(0).getNodeValue(), "Test Comment");
    }

    @Test
    public void testW3CBuilder_DataNode() {
        Document w3cDocument = w3CDom.fromJsoup(org.jsoup.Jsoup.parse("<html><body><script type='text/javascript'>function test(){ alert('Hello World'); }</script></body></html>"));
        Element root = w3cDocument.getDocumentElement();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDocument);
        org.jsoup.nodes.DataNode jsoupDataNode = new org.jsoup.nodes.DataNode("test()");
        builder.head(jsoupDataNode, 0);
        Assert.assertEquals(root.getDocumentElement().getChildNodes().getLength(), 1);
        Assert.assertEquals(root.getDocumentElement().getChildNodes().item(0).getNodeName(), "#text");
        Assert.assertEquals(root.getDocumentElement().getChildNodes().item(0).getNodeValue(), "test()");
    }

    @Test(expected = IllegalStateException.class)
    public void testFromJsoup_NullDocument() {
        W3CDom w3CDom = new W3CDom();
        w3CDom.fromJsoup(null);
    }

    @Test
    public void testAsString() throws TransformerException {
        Document document = w3CDom.fromJsoup(org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>"));
        String result = w3CDom.asString(document);
        Assert.assertNotNull(result);
    }

}