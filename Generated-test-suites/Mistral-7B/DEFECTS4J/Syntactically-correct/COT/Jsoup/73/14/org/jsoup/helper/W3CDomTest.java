package org.jsoup.helper;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Text;
import org.w3c.dom.Comment;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.isInstanceOf;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><div id='test'></div></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        Assert.assertThat(w3cDoc, notNullValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testFromJsoupWithNullDocument() {
        new W3CDom().fromJsoup(null);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><div id='test'><p>Test</p></div></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        new W3CDom().convert(jsoupDoc, w3cDoc);

        Element rootEl = (Element) w3cDoc.getDocumentElement();
        Element divEl = (Element) rootEl.getElementsByTagName("div").item(0);
        Element pEl = (Element) divEl.getElementsByTagName("p").item(0);

        Assert.assertThat(rootEl.getTagName(), is("html"));
        Assert.assertThat(divEl.getTagName(), is("div"));
        Assert.assertThat(pEl.getTagName(), is("p"));
        Assert.assertThat(pEl.getTextContent(), is("Test"));
    }

    @Test(expected = IllegalStateException.class)
    public void testConvertWithNullJsoupDocument() {
        Document w3cDoc = new Document();
        new W3CDom().convert(null, w3cDoc);
    }

    @Test
    public void testAsString() {
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocument();
        Element rootEl = w3cDoc.createElement("html");
        rootEl.setAttribute("xmlns", "http://www.w3.org/1999/xhtml");
        Element bodyEl = w3cDoc.createElement("body");
        rootEl.appendChild(bodyEl);
        Element divEl = w3cDoc.createElement("div");
        bodyEl.appendChild(divEl);
        Text textNode = w3cDoc.createTextNode("Test");
        divEl.appendChild(textNode);

        new W3CDom().convert(null, w3cDoc);

        String w3cString = new W3CDom().asString(w3cDoc);
        String expectedXml = "" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<body>\n" +
                "<div>\n" +
                "Test\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";

        Assert.assertThat(w3cString, is(expectedXml));
    }

    @Test(expected = IllegalStateException.class)
    public void testAsStringWithNullDocument() {
        new W3CDom().asString(null);
    }

    @Test
    public void testW3CBuilderHandlesTextNodes() {
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocument();
        Element rootEl = w3cDoc.createElement("html");
        rootEl.setAttribute("xmlns", "http://www.w3.org/1999/xhtml");
        Element bodyEl = w3cDoc.createElement("body");
        rootEl.appendChild(bodyEl);
        Text textNode = w3cDoc.createTextNode("Test");
        bodyEl.appendChild(textNode);

        new W3CDom().convert(null, w3cDoc);

        NodeVisitor visitor = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><p>Test</p></body></html>");
        NodeTraversor.traverse(visitor, jsoupDoc.child(0));

        org.jsoup.nodes.Text textNodeJsoup = (org.jsoup.nodes.Text) jsoupDoc.child(1);
        Text textNodeW3c = (Text) visitor.dest;

        Assert.assertThat(textNodeW3c.getTextContent(), is("Test"));
    }

    @Test
    public void testW3CBuilderHandlesComments() {
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocument();
        Element rootEl = w3cDoc.createElement("html");
        rootEl.setAttribute("xmlns", "http://www.w3.org/1999/xhtml");
        Element bodyEl = w3cDoc.createElement("body");
        rootEl.appendChild(bodyEl);
        Comment comment = w3cDoc.createComment("Test Comment");
        bodyEl.appendChild(comment);

        new W3CDom().convert(null, w3cDoc);

        NodeVisitor visitor = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><!--Test Comment--></body></html>");
        NodeTraversor.traverse(visitor, jsoupDoc.child(0));

        org.jsoup.nodes.Comment commentJsoup = (org.jsoup.nodes.Comment) jsoupDoc.child(1);
        Comment commentW3c = (Comment) visitor.dest;

        Assert.assertThat(commentW3c.getData(), is("Test Comment"));
    }

    @Test
    public void testW3CBuilderHandlesDataNodes() {
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocument();
        Element rootEl = w3cDoc.createElement("html");
        rootEl.setAttribute("xmlns", "http://www.w3.org/1999/xhtml");
        Element bodyEl = w3cDoc.createElement("body");
        rootEl.appendChild(bodyEl);
        Element dataEl = w3cDoc.createElement("data");
        dataEl.setTextContent("Test Data");
        bodyEl.appendChild(dataEl);

        new W3CDom().convert(null, w3cDoc);

        NodeVisitor visitor = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><data>Test Data</data></body></html>");
        NodeTraversor.traverse(visitor, jsoupDoc.child(0));

        org.jsoup.nodes.DataNode dataNodeJsoup = (org.jsoup.nodes.DataNode) jsoupDoc.child(1);
        Element dataElW3c = (Element) visitor.dest;

        Assert.assertThat(dataElW3c.getTextContent(), is("Test Data"));
        Assert.assertThat(dataElW3c.getNodeName(), is("data"));
    }

}