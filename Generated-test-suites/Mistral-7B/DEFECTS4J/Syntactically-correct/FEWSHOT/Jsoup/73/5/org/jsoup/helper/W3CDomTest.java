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
import org.w3c.dom. processing.NSResolver;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class W3CDomTest {

    @Test
    public void testFromJsoup() throws Exception {
        String html = "<html><head><title>Test</title></head><body><p id='test'>Hello World</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
        assertEquals("Test", w3cDoc.getDocumentElement().getElementsByTagName("title").item(0).getTextContent());
        assertEquals("Hello World", w3cDoc.getDocumentElement().getElementsByTagName("p").item(0).getTextContent());
        assertEquals("http://example.com/test.html", w3cDoc.getDocumentURI().toString());

        // Test with namespaces
        String htmlWithNamespace = "<html xmlns:html='http://www.w3.org/1999/xhtml' xml:lang='en' xmlns:x='http://www.w3.org/2005/xpath'><html:head><title>Test</title></html:head><body><p id='test'>Hello World</p></body></html>";
        org.jsoup.nodes.Document jsoupDocWithNamespace = org.jsoup.Jsoup.parse(htmlWithNamespace, new NSResolver() {
            @Override
            public String getNamespaceURI(String prefix) {
                if (prefix.equals("html")) {
                    return "http://www.w3.org/1999/xhtml";
                }
                return null;
            }
        });
        Document w3cDocWithNamespace = new W3CDom().fromJsoup(jsoupDocWithNamespace);
        assertEquals("http://www.w3.org/1999/xhtml", w3cDocWithNamespace.getDocumentElement().getNamespaceURI());
    }

    @Test
    public void testConvert() throws Exception {
        String html = "<html><head><title>Test</title></head><body><p id='test'>Hello World</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        Document w3cDocConverted = new Document();
        new W3CDom().convert(jsoupDoc, w3cDocConverted);
        assertSame(w3cDoc, w3cDocConverted);
    }

    @Test
    public void testAsString() throws Exception {
        String html = "<html><head><title>Test</title></head><body><p id='test'>Hello World</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        String w3cDocAsString = new W3CDom().asString(w3cDoc);

        String expected = "<html><head><title>Test</title></head><body><p id='test'>Hello World</p></body></html>";
        assertEquals(expected, w3cDocAsString);
    }

    @Test
    public void testAsStringWithNamespaces() throws Exception {
        String htmlWithNamespace = "<html xmlns:html='http://www.w3.org/1999/xhtml' xml:lang='en' xmlns:x='http://www.w3.org/2005/xpath'><html:head><title>Test</title></html:head><body><p id='test'>Hello World</p></body></html>";
        org.jsoup.nodes.Document jsoupDocWithNamespace = org.jsoup.Jsoup.parse(htmlWithNamespace, new NSResolver() {
            @Override
            public String getNamespaceURI(String prefix) {
                if (prefix.equals("html")) {
                    return "http://www.w3.org/1999/xhtml";
                }
                return null;
            }
        });
        Document w3cDocWithNamespace = new W3CDom().fromJsoup(jsoupDocWithNamespace);
        String w3cDocWithNamespaceAsString = new W3CDom().asString(w3cDocWithNamespace);

        String expectedWithNamespace = "<html xmlns:html='http://www.w3.org/1999/xhtml' xml:lang='en' xmlns:x='http://www.w3.org/2005/xpath'><html:head><title>Test</title></html:head><body><p id='test'>Hello World</p></body></html>";
        assertEquals(expectedWithNamespace, w3cDocWithNamespaceAsString);
    }

    @Test
    public void testEmptyDocument() throws Exception {
        Document emptyW3CDoc = DocumentBuilderFactory.newInstance().newDocument();
        String emptyW3CDocAsString = new W3CDom().asString(emptyW3CDoc);
        String expectedEmpty = "<document></document>";
        assertEquals(expectedEmpty, emptyW3CDocAsString);
    }

    @Test
    public void testComment() throws Exception {
        String html = "<html><!--This is a comment--><head><title>Test</title></head><body></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        Comment comment = (Comment) w3cDoc.getDocumentElement().getElementsByTagName("comment").item(0);
        String commentText = comment.getTextContent();
        assertEquals("This is a comment", commentText);
    }

    @Test
    public void testTextNode() throws Exception {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        Text textNode = (Text) w3cDoc.getDocumentElement().getElementsByTagName("p").item(0).getFirstChild();
        String textNodeText = textNode.getTextContent();
        assertEquals("Hello World", textNodeText);
    }
}