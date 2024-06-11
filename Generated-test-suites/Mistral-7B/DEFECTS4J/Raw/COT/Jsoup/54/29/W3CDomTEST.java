package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
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
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1>Test</h1></body></html>");
        Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
        Assert.assertNotNull(w3cDocument);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1 id='test'>Test</h1></body></html>");
        Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
        w3CDom.convert(jsoupDocument, w3cDocument);

        Element root = (Element) w3cDocument.getDocumentElement();
        Assert.assertEquals("html", root.getTagName());

        Element body = (Element) root.getElementsByTagName("body").item(0);
        Assert.assertNotNull(body);

        Element h1 = (Element) body.getElementsByTagName("h1").item(0);
        Assert.assertNotNull(h1);
        Assert.assertEquals("Test", h1.getTextContent());
        Assert.assertEquals("test", h1.getAttribute("id"));
    }

    @Test
    public void testAsString() throws ParserConfigurationException {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1>Test</h1></body></html>");
        Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
        String result = w3CDom.asString(w3cDocument);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.contains("<html>"));
        Assert.assertTrue(result.contains("<body>"));
        Assert.assertTrue(result.contains("<h1>Test</h1>"));
        Assert.assertTrue(result.contains("</body>"));
        Assert.assertTrue(result.contains("</html>"));
    }

    @Test(expected = IllegalStateException.class)
    public void testFromJsoupNullDocument() {
        W3CDom w3CDom = new W3CDom();
        w3CDom.fromJsoup(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertNullDocument() {
        W3CDom w3CDom = new W3CDom();
        Document w3cDocument = null;
        w3CDom.convert(null, w3cDocument);
    }

}