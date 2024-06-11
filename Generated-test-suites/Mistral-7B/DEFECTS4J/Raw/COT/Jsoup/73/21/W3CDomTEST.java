package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.parser.ParserConfigurationException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;

public class W3CDomTest {

    @Test
    public void testFromJsoup() throws ParserConfigurationException {
        org.jsoup.nodes.Document in = org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        Document out = new W3CDom().fromJsoup(in);
        assertNotNull(out);
    }

    @Test
    public void testConvert() throws ParserConfigurationException, TransformerException {
        org.jsoup.nodes.Document in = org.jsoup.Jsoup.parse("<html><body><h1 xmlns:test='http://test.com'>Hello World</h1></body></html>");
        Document out = DocumentBuilderFactory.newInstance().newDocument();
        new W3CDom().convert(in, out);

        assertEquals("http://test.com", out.getDocumentElement().getNamespaceURI());
        assertEquals("h1", out.getDocumentElement().getTagName());
        assertEquals("Hello World", out.getDocumentElement().getTextContent());
    }

    @Test
    public void testAsString() throws ParserConfigurationException, TransformerException {
        org.jsoup.nodes.Document in = org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        Document out = new W3CDom().fromJsoup(in);
        String result = new W3CDom().asString(out);

        String expected = "<html><body><h1>Hello World</h1></body></html>";
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoupNullInput() {
        new W3CDom().fromJsoup(null);
    }

    @Test
    public void testConvertNullInput() {
        Document out = new Document();
        new W3CDom().convert(null, out);
    }

}