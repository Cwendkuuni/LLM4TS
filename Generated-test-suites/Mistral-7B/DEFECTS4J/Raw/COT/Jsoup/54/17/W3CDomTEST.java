package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document in = org.jsoup.Jsoup.parse("<html><body><h1 id='test'>Hello World</h1></body></html>");
        Document out = new W3CDom().fromJsoup(in);
        String expected = "<html><body><h1 id='test'>Hello World</h1></body></html>";
        String actual = this.asString(out);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFromJsoup_EmptyDocument() {
        org.jsoup.nodes.Document in = org.jsoup.Jsoup.parse("");
        Document out = new W3CDom().fromJsoup(in);
        String expected = "";
        String actual = this.asString(out);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testFromJsoup_NullDocument() {
        org.jsoup.nodes.Document in = null;
        new W3CDom().fromJsoup(in);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document in = org.jsoup.Jsoup.parse("<html><body><h1 id='test'>Hello World</h1></body></html>");
        Document doc = org.jsoup.Jsoup.parse("<html></html>");
        new W3CDom().convert(in, doc);
        String expected = "<html><body><h1 id='test'>Hello World</h1></body></html>";
        String actual = this.asString(doc);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConvert_EmptyDocument() {
        org.jsoup.nodes.Document in = org.jsoup.Jsoup.parse("");
        Document doc = org.jsoup.Jsoup.parse("<html></html>");
        new W3CDom().convert(in, doc);
        String expected = "";
        String actual = this.asString(doc);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConvert_NullDocument() {
        org.jsoup.nodes.Document in = null;
        Document doc = org.jsoup.Jsoup.parse("<html></html>");
        try {
            new W3CDom().convert(in, doc);
        } catch (Exception e) {
            // expected
        }
    }

    @Test
    public void testAsString() {
        Document doc = org.jsoup.Jsoup.parse("<html><body><h1 id='test'>Hello World</h1></body></html>");
        String expected = "<html><body><h1 id='test'>Hello World</h1></body></html>";
        String actual = new W3CDom().asString(doc);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAsString_EmptyDocument() {
        Document doc = org.jsoup.Jsoup.parse("<html></html>");
        String expected = "";
        String actual = new W3CDom().asString(doc);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAsString_NullDocument() {
        Document doc = null;
        try {
            String actual = new W3CDom().asString(doc);
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testW3CBuilder_ElementWithAttributes() {
        Document doc = org.jsoup.Jsoup.parse("<html><body><h1 id='test' class='testClass' title='Hello World'>Hello World</h1></body></html>");
        Element root = doc.getDocumentElement();
        new W3CDom.W3CBuilder(root).head(doc.getDocumentElement(), 0);
        Map<String, String> expectedAttributes = new HashMap<>();
        expectedAttributes.put("id", "test");
        expectedAttributes.put("class", "testClass");
        expectedAttributes.put("title", "Hello World");
        for (Map.Entry<String, String> entry : expectedAttributes.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Assert.assertEquals(key, root.getAttribute(key));
            Assert.assertEquals(value, root.getAttribute(key));
        }
    }

    @Test
    public void testW3CBuilder_ElementWithChildren() {
        Document doc = org.jsoup.Jsoup.parse("<html><body><h1 id='test'>Hello <b>World</b></h1></body></html>");
        Element root = doc.getDocumentElement();
        new W3CDom.W3CBuilder(root).head(doc.getDocumentElement(), 0);
        Element h1 = (Element) root.getElementsByTagName("h1").item(0);
        Element b = (Element) h1.getElementsByTagName("b").item(0);
        String expectedText = "Hello World";
        String actualText = b.getTextContent();
        Assert.assertEquals(expectedText, actualText);
    }

    @Test
    public void testW3CBuilder_ElementWithTextNode() {
        Document doc = org.jsoup.Jsoup.parse("<html><body><h1 id='test'>Hello World</h1></body></html>");
        Element root = doc.getDocumentElement();
        new W3CDom.W3CBuilder(root).head(doc.getDocumentElement(), 0);
        Element h1 = (Element) root.getElementsByTagName("h1").item(0);
        Text textNode = (Text) h1.getFirstChild();
        String expectedText = "Hello World";
        String actualText = textNode.getData();
        Assert.assertEquals(expectedText, actualText);
    }

    @Test
    public void testW3CBuilder_CommentNode() {
        Document doc = org.jsoup.Jsoup.parse("<html><body><!--This is a comment--></body></html>");
        Element root = doc.getDocumentElement();
        new W3CDom.W3CBuilder(root).head(doc.getDocumentElement(), 0);
        Comment comment = (Comment) root.getElementsByTagName("!--").item(0);
        String expectedComment = "This is a comment";
        String actualComment = comment.getData();
        Assert.assertEquals(expectedComment, actualComment);
    }

    @Test
    public void testW3CBuilder_DataNode() {
        Document doc = org.jsoup.Jsoup.parse("<html><body><script type='text/javascript'>alert('Hello World');</script></body></html>");
        Element root = doc.getDocumentElement();
        new W3CDom.W3CBuilder(root).head(doc.getDocumentElement(), 0);
        Text dataNode = (Text) root.getElementsByTagName("script").item(0).getFirstChild();
        String expectedData = "alert('Hello World');";
        String actualData = dataNode.getData();
        Assert.assertEquals(expectedData, actualData);
    }

    @Test
    public void testW3CBuilder_EmptyElement() {
        Document doc = org.jsoup.Jsoup.parse("<html><body><element></body></html>");
        Element root = doc.getDocumentElement();
        new W3CDom.W3CBuilder(root).head(doc.getDocumentElement(), 0);
        Element element = (Element) root.getElementsByTagName("element").item(0);
        String expectedEmpty = "";
        String actualEmpty = element.getTextContent();
        Assert.assertEquals(expectedEmpty, actualEmpty);
    }

    @Test
    public void testW3CBuilder_EmptyElementWithAttributes() {
        Document doc = org.jsoup.Jsoup.parse("<html><body><element id='test' class='testClass'></body></html>");
        Element root = doc.getDocumentElement();
        new W3CDom.W3CBuilder(root).head(doc.getDocumentElement(), 0);
        Element element = (Element) root.getElementsByTagName("element").item(0);
        Map<String, String> expectedAttributes = new HashMap<>();
        expectedAttributes.put("id", "test");
        expectedAttributes.put("class", "testClass");
        for (Map.Entry<String, String> entry : expectedAttributes.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Assert.assertEquals(key, element.getAttribute(key));
            Assert.assertEquals(value, element.getAttribute(key));
        }
    }
}