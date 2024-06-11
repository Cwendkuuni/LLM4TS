package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.ParserConfigurationException;

public class W3CDomTest {

    @Test
    public void testFromJsoup() throws ParserConfigurationException {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><head/><body><h1 id='title'>Test</h1></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        assertEquals("html", w3cDoc.getDocumentElement().getTagName());
    }

    @Test
    public void testConvert() throws ParserConfigurationException {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><head/><body><h1 id='title'>Test</h1></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        Element rootElement = w3cDoc.getDocumentElement();

        W3CDom w3CDom = new W3CDom();
        w3CDom.convert(jsoupDoc, w3cDoc);

        assertEquals(jsoupDoc, w3cDoc); // The jsoup and W3C documents should be equal.
    }

    @Test
    public void testW3CBuilder() throws ParserConfigurationException {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html xmlns:html='http://www.w3.org/1999/xhtml' xmlns:x='http://www.w3.org/2005/xpath'><head/><body><h1 xmlns:a='http://example.com' id='title' class='test'>Test</h1></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        Element rootElement = w3cDoc.getDocumentElement();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        builder.head(jsoupDoc.child(0), 0);

        assertEquals("html", rootElement.getTagName());
        assertEquals("http://www.w3.org/1999/xhtml", rootElement.getNamespaceURI());
        assertEquals(null, rootElement.getPrefix());

        Element h1Element = (Element) rootElement.getElementsByTagName("h1").item(0);
        assertEquals("h1", h1Element.getTagName());
        assertEquals("http://www.w3.org/1999/xhtml", h1Element.getNamespaceURI());
        assertEquals("test", h1Element.getClassAttribute("class"));
        assertEquals("a", h1Element.getNamespaceURI().substring(h1Element.getNamespaceURI().lastIndexOf(":") + 1));
        assertEquals("http://example.com", h1Element.getPrefixes().getNamedItem("a").getNodeValue());
    }

    @Test
    public void testAsString() throws ParserConfigurationException, TransformerException {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><head/><body><h1 id='title'>Test</h1></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        String w3cDocAsString = new W3CDom().asString(w3cDoc);

        String expectedXml = ""
                + "<html xmlns='http://www.w3.org/1999/xhtml'>"
                + "<head/>"
                + "<body>"
                + "<h1 id='title'>Test</h1>"
                + "</body>"
                + "</html>";

        assertEquals(expectedXml, w3cDocAsString);
    }

    @Test
    public void testCatchesIllegalStateException() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.newDocument();
            fail("Should have thrown an IllegalStateException.");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testNormalizesNamespaces() throws ParserConfigurationException {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html xmlns:xhtml='http://www.w3.org/1999/xhtml' xmlns:x='http://www.w3.org/2001/XMLSchema' xmlns:xi='http://www.w3.org/2001/XInclude'><head/><body><h1 id='title'>Test</h1></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        assertEquals("http://www.w3.org/1999/xhtml", w3cDoc.getDocumentElement().getNamespaceURI());
        assertEquals("xhtml", w3cDoc.getDocumentElement().getPrefix());

        Element h1Element = (Element) w3cDoc.getDocumentElement().getElementsByTagName("h1").item(0);
        assertEquals("http://www.w3.org/2001/XMLSchema", h1Element.getNamespaceURI());
        assertEquals(null, h1Element.getPrefix());
    }

    @Test
    public void testEmptyDocument() throws ParserConfigurationException {
        Document w3cDoc = new W3CDom().fromJsoup(org.jsoup.Jsoup.parse(""));
        String w3cDocAsString = new W3CDom().asString(w3cDoc);
        assertEquals("<document/>", w3cDocAsString);
    }

    @Test
    public void testEmptyDocumentWithWhitespace() throws ParserConfigurationException {
        Document w3cDoc = new W3CDom().fromJsoup(org.jsoup.Jsoup.parse(" "));
        String w3cDocAsString = new W3CDom().asString(w3cDoc);
        assertEquals("<document/>", w3cDocAsString);
    }

    @Test
    public void testComment() throws ParserConfigurationException {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><!-- Test --></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        Comment comment = (Comment) w3cDoc.getDocumentElement().getChildNodes().item(0);
        assertEquals("Test", comment.getData());
    }

    @Test
    public void testTextNode() throws ParserConfigurationException {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><p>Test</p></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        Text textNode = (Text) w3cDoc.getDocumentElement().getElementsByTagName("body").item(0).getChildNodes().item(0);
        assertEquals("Test", textNode.getData());
    }
}