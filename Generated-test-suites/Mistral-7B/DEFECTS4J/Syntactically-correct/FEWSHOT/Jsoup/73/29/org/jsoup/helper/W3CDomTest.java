package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringWriter;
import java.util.HashMap;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1 id='title'>Hello World</h1></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        Assert.assertNotNull(w3cDocument);
        Assert.assertEquals("html", w3cDocument.getDocumentElement().getTagName());
        Element titleElement = (Element) NodeTraversor.traverse(new NameFilter("h1"), w3cDocument).find();
        Assert.assertNotNull(titleElement);
        Assert.assertEquals("title", titleElement.getTagName());
        Assert.assertEquals("Hello World", titleElement.getTextContent());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1 id='title'>Hello World</h1></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        Document convertedJsoupDocument = new W3CDom().fromJsoup(jsoupDocument);
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Assert.assertNotSame(jsoupDocument, convertedJsoupDocument);
        Assert.assertEquals(w3cDocument.getDocumentElement().getTagName(), convertedJsoupDocument.getDocumentElement().getTagName());
        Element titleElement = (Element) NodeTraversor.traverse(new NameFilter("h1"), w3cDocument).find();
        Assert.assertNotNull(titleElement);
        Assert.assertEquals("title", titleElement.getTagName());
        Assert.assertEquals("Hello World", titleElement.getTextContent());
    }

    @Test
    public void testAsString() {
        Document w3cDocument = DocumentBuilderFactory.newInstance().newDocument();
        Element root = w3cDocument.createElement("html");
        Element body = w3cDocument.createElement("body");
        Element title = w3cDocument.createElement("h1");
        title.setTextContent("Hello World");
        body.appendChild(title);
        root.appendChild(body);
        w3cDocument.appendChild(root);

        String w3cString = new W3CDom().asString(w3cDocument);
        String jsoupString = org.jsoup.Jsoup.parse(w3cString).body().html();

        Assert.assertEquals(jsoupString, w3cString);
    }

    private static NodeVisitor newNameFilter(final String name) {
        return new NodeVisitor() {
            @Override
            public void head(Node node, int depth) {
                if (node.getNodeName().equals(name)) {
                    throw new RuntimeException("Found node: " + node);
                }
                visit(node, depth);
            }

            @Override
            public void tail(Node node, int depth) {
                visit(node, depth);
            }
        };
    }
}