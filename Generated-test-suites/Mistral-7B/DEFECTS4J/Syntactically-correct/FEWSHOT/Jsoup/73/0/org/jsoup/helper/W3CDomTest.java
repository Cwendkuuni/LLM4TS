package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head></head><body><div id='test'></div></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        Assert.assertNotNull(w3cDocument);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><div id='test'><p>Hello World</p></div></body></html>");
        Document w3cDocument = DocumentBuilderFactory.newInstance().newDocument();
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Element html = w3cDocument.getDocumentElement();
        Assert.assertEquals("html", html.getTagName());

        Element head = (Element) html.getElementsByTagName("head").item(0);
        Element title = (Element) head.getElementsByTagName("title").item(0);
        Assert.assertEquals("Test", title.getTextContent());

        Element body = html.getElementsByTagName("body").item(0);
        Element div = (Element) body.getElementsByTagName("div").item(0);
        Element p = (Element) div.getElementsByTagName("p").item(0);
        Assert.assertEquals("Hello World", p.getTextContent());
    }

    @Test
    public void testAsString() {
        Document w3cDocument = DocumentBuilderFactory.newInstance().newDocument();
        Element html = w3cDocument.createElement("html");
        Element head = w3cDocument.createElement("head");
        Element title = w3cDocument.createElement("title");
        title.setTextContent("Test");
        head.appendChild(title);
        html.appendChild(head);

        Element body = w3cDocument.createElement("body");
        Element div = w3cDocument.createElement("div");
        Element p = w3cDocument.createElement("p");
        p.setTextContent("Hello World");
        div.appendChild(p);
        body.appendChild(div);
        html.appendChild(body);

        w3cDocument.appendChild(html);

        String w3cDocumentAsString = new W3CDom().asString(w3cDocument);
        String expectedResult = "<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<div>\n<p>Hello World</p>\n</div>\n</body>\n</html>";
        Assert.assertEquals(expectedResult, w3cDocumentAsString);
    }

    @Test
    public void testW3CBuilder() {
        Document w3cDocument = DocumentBuilderFactory.newInstance().newDocument();
        Element html = w3cDocument.createElement("html");
        html.setAttribute("xmlns", "http://www.w3.org/1999/xhtml");
        w3cDocument.appendChild(html);

        Element head = w3cDocument.createElement("head");
        html.appendChild(head);

        Element title = w3cDocument.createElement("title");
        title.setTextContent("Test");
        head.appendChild(title);

        Element body = w3cDocument.createElement("body");
        html.appendChild(body);

        Element div = w3cDocument.createElement("div");
        div.setAttribute("id", "test");
        body.appendChild(div);

        Element p = w3cDocument.createElement("p");
        p.setTextContent("Hello World");
        div.appendChild(p);

        new W3CDom.W3CBuilder(w3cDocument).head(jsoupToW3c(org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><div id='test'><p>Hello World</p></div></body></html>"), w3cDocument), 0);

        Element jsoupHtml = jsoupToW3c(org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><div id='test'><p>Hello World</p></div></body></html>"), w3cDocument).getDocumentElement();

        Assert.assertEquals(jsoupHtml.getElementsByTagName("html").item(0), html);
        Assert.assertEquals(jsoupHtml.getElementsByTagName("head").item(0), head);
        Assert.assertEquals(jsoupHtml.getElementsByTagName("title").item(0), title);
        Assert.assertEquals(jsoupHtml.getElementsByTagName("body").item(0), body);
        Assert.assertEquals(jsoupHtml.getElementsByTagName("div").item(0), div);
        Assert.assertEquals(jsoupHtml.getElementsByTagName("p").item(0), p);
    }

    private static org.jsoup.nodes.Document jsoupToW3c(org.jsoup.nodes.Document jsoupDocument, Document w3cDocument) {
        new W3CDom().convert(jsoupDocument, w3cDocument);
        return jsoupDocument;
    }

}