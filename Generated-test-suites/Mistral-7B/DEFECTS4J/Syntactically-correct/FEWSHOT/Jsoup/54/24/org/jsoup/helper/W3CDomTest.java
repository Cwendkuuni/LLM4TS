package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.processing.Processor;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><p id='p1'>Hello World</p></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
        assertEquals("http://jsoup.org/", w3cDoc.getDocumentURI());

        Element html = w3cDoc.getDocumentElement();
        assertSame(html.getTagName(), "html");

        Element body = (Element) XPathFactory.newInstance().newXPath().compile("/html/body").evaluate(w3cDoc, XPathConstants.NODE);
        assertSame(body.getTagName(), "body");

        Element p = (Element) XPathFactory.newInstance().newXPath().compile("/html/body/p").evaluate(w3cDoc, XPathConstants.NODE);
        assertSame(p.getTagName(), "p");
        assertNotNull(p.getTextContent());
        assertEquals("Hello World", p.getTextContent());
        assertEquals("p1", p.getId());

        String w3cXml = new W3CDom().asString(w3cDoc);
        assertNotNull(w3cXml);
        assertEquals("<html><body><p id=\"p1\">Hello World</p></body></html>", w3cXml);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><p id='p1'>Hello World</p></body></html>");
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        new W3CDom().convert(jsoupDoc, w3cDoc);

        assertNotNull(w3cDoc.getDocumentElement());
        assertSame(w3cDoc.getDocumentElement().getTagName(), "html");

        Element html = w3cDoc.getDocumentElement();
        Element body = (Element) XPathFactory.newInstance().newXPath().compile("/html/body", w3cDoc).evaluate();
        Element p = (Element) XPathFactory.newInstance().newXPath().compile("/html/body/p", w3cDoc).evaluate();

        assertSame(body.getParentElement(), html);
        assertSame(p.getParentElement(), body);

        Text textNode = (Text) XPathFactory.newInstance().newXPath().compile("/html/body/p/text()", w3cDoc).evaluate();
        assertSame(textNode.getParentNode(), p);

        Comment commentNode = (Comment) XPathFactory.newInstance().newXPath().compile("/html/body/comment()", w3cDoc).evaluate();
        assertSame(commentNode.getParentNode(), body);
    }

    @Test(expected = IllegalStateException.class)
    public void testConvertWithNullDocument() {
        new W3CDom().convert(null, null);
    }
}