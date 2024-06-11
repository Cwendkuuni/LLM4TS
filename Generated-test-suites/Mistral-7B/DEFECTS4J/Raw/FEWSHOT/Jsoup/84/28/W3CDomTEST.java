package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Stack;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head/><body><p>foo</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        String w3cDocumentAsString = new W3CDom().asString(w3cDocument);
        Assert.assertEquals("<html><head/><body><p>foo</p></body></html>", w3cDocumentAsString);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>foo</title></head><body><p>bar</p></body></html>");
        Document w3cDocument = new Document();
        new W3CDom().convert(jsoupDocument, w3cDocument);

        // Check title element
        Element titleElement = (Element) w3cDocument.getElementsByTagName("title").item(0);
        Assert.assertEquals("title", titleElement.getTagName());
        Assert.assertEquals("foo", titleElement.getTextContent());

        // Check body element and its child p element
        Element bodyElement = (Element) w3cDocument.getElementsByTagName("body").item(0);
        Assert.assertEquals("body", bodyElement.getTagName());
        Element pElement = (Element) bodyElement.getElementsByTagName("p").item(0);
        Assert.assertEquals("p", pElement.getTagName());
        Assert.assertEquals("bar", pElement.getTextContent());
    }

    @Test
    public void testNamespaces() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html xmlns:foo='http://example.com' xmlns:bar='http://another-example.com'><head/><body><p xmlns:baz='http://yet-another-example.com'>foo bar baz</p></body></html>");
        Document w3cDocument = new Document();
        new W3CDom().convert(jsoupDocument, w3cDocument);

        // Check namespaces
        HashMap<String, String> currentNamespaces = namespacesStackTop(w3cDocument);
        Assert.assertEquals(3, currentNamespaces.size());
        Assert.assertEquals("http://example.com", currentNamespaces.get("foo"));
        Assert.assertEquals("http://another-example.com", currentNamespaces.get("bar"));
        Assert.assertEquals("http://yet-another-example.com", currentNamespaces.get("baz"));

        // Check elements with namespaces
        Element pElement = (Element) w3cDocument.getElementsByTagName("p").item(0);
        String namespace = pElement.getNamespaceURI();
        Assert.assertEquals("http://yet-another-example.com", namespace);
        String localName = pElement.getLocalName();
        Assert.assertEquals("p", localName);
        String prefix = pElement.getPrefix();
        Assert.assertEquals("baz", prefix);
        Assert.assertEquals("foo bar baz", pElement.getTextContent());
    }

    @Test
    public void testCOMMENTs() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><!-- This is a comment --><body></body></html>");
        Document w3cDocument = new Document();
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Comment w3cComment = (Comment) w3cDocument.getElementsByTagName("comment").item(0);
        Assert.assertEquals("This is a comment", w3cComment.getData());
    }

    @Test
    public void testTextNodes() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head/><body><p>foo bar baz</p></body></html>");
        Document w3cDocument = new Document();
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Text w3cTextNode1 = (Text) w3cDocument.getElementsByTagName("text").item(0);
        Text w3cTextNode2 = (Text) w3cDocument.getElementsByTagName("text").item(1);
        Text w3cTextNode3 = (Text) w3cDocument.getElementsByTagName("text").item(2);
        Assert.assertEquals("foo ", w3cTextNode1.getTextContent());
        Assert.assertEquals(" bar ", w3cTextNode2.getTextContent());
        Assert.assertEquals("baz", w3cTextNode3.getTextContent());
    }

    @Test
    public void testEmptyDocument() {
        Document w3cDocument = new Document();
        String w3cDocumentAsString = new W3CDom().asString(w3cDocument);
        Assert.assertEquals("", w3cDocumentAsString);
    }

    private HashMap<String, String> namespacesStackTop(Document document) {
        return namespacesStackTop(document, document);
    }

    private HashMap<String, String> namespacesStackTop(Document document, Element element) {
        Stack<HashMap<String, String>> namespaceStack = new Stack<>();
        HashMap<String, String> currentNamespaces = new HashMap<>();
        while (element != null) {
            namespaceStack.push(currentNamespaces);
            currentNamespaces = (HashMap<String, String>) element.getNamespaceURI();
            element = (Element) element.getParentNode();
        }
        return namespaceStack.peek();
    }
}