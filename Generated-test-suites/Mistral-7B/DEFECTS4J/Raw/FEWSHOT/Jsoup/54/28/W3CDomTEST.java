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
import org.w3c.dom.processing.NSResolver;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TraversalHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        String html = "<html><body><h1>Hello</h1></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        String html = "<html xmlns:html='http://www.w3.org/1999/xhtml' xml:lang='en'><body><h1 class='title'>Hello</h1></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = new W3CDom().newDocument();
        new W3CDom().convert(jsoupDoc, w3cDoc);
        assertNotNull(w3cDoc);
        assertEquals(html, new W3CDom().asString(w3cDoc));
    }

    @Test
    public void testConvertWithNamespace() {
        String html = "<html xmlns:html='http://www.w3.org/1999/xhtml' xml:lang='en'><body><html:h1 class='title'>Hello</html:h1></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = new W3CDom().newDocument();
        new W3CDom().convert(jsoupDoc, w3cDoc);
        assertNotNull(w3cDoc);
        assertEquals(html, new W3CDom().asString(w3cDoc));
        Element htmlTag = (Element) w3cDoc.getDocumentElement();
        assertSame(htmlTag.getTagName(), "HTML");
        assertEquals(htmlTag.getAttribute("xmlns:html"), "http://www.w3.org/1999/xhtml");
    }

    @Test
    public void testAsString() {
        String html = "<html><body><h1>Hello</h1></body></html>";
        Document w3cDoc = new W3CDom().newDocument();
        w3cDoc.appendChild(w3cDoc.createElement("html"));
        w3cDoc.getElementsByTagName("html").item(0).appendChild(w3cDoc.createElement("body"));
        w3cDoc.getElementsByTagName("body").item(0).appendChild(w3cDoc.createElement("h1"));
        w3cDoc.getElementsByTagName("h1").item(0).setTextContent("Hello");
        assertEquals(html, new W3CDom().asString(w3cDoc));
    }

    @Test
    public void testConvertWithAttributes() {
        String html = "<html xmlns:html='http://www.w3.org/1999/xhtml' xml:lang='en' class='foo'><body><h1 class='title'>Hello</h1></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = new W3CDom().newDocument();
        new W3CDom().convert(jsoupDoc, w3cDoc);
        Element htmlTag = (Element) w3cDoc.getDocumentElement();
        htmlTag.setAttribute("xmlns:html", "http://www.w3.org/1999/xhtml");
        htmlTag.setAttribute("xml:lang", "en");
        htmlTag.setAttribute("class", "foo");
        assertEquals(html, new W3CDom().asString(w3cDoc));
    }

    @Test
    public void testConvertWithNamespaceAndAttributes() {
        String html = "<html xmlns:html='http://www.w3.org/1999/xhtml' xml:lang='en' class='foo'><body><html:h1 class='title'>Hello</html:h1></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = new W3CDom().newDocument();
        new W3CDom().convert(jsoupDoc, w3cDoc);
        Element htmlTag = (Element) w3cDoc.getDocumentElement();
        htmlTag.setAttribute("xmlns:html", "http://www.w3.org/1999/xhtml");
        htmlTag.setAttribute("xml:lang", "en");
        htmlTag.setAttribute("class", "foo");
        Element h1Tag = (Element) w3cDoc.getElementsByTagNameNS("http://www.w3.org/1999/xhtml", "h1").item(0);
        h1Tag.setAttribute("class", "title");
        assertEquals(html, new W3CDom().asString(w3cDoc));
    }

    @Test
    public void testConvertWithComments() {
        String html = "<html><!-- comment1 --><body><!-- comment2 --><h1>Hello</h1><!-- comment3 --></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = new W3CDom().newDocument();
        new W3CDom().convert(jsoupDoc, w3cDoc);
        Element htmlTag = (Element) w3cDoc.getDocumentElement();
        htmlTag.appendChild(w3cDoc.createComment(" comment1 "));
        htmlTag.getElementsByTagName("body").item(0).appendChild(w3cDoc.createComment(" comment2 "));
        htmlTag.getElementsByTagName("body").item(0).appendChild(w3cDoc.createElement("h1"));
        htmlTag.getElementsByTagName("h1").item(0).setTextContent("Hello");
        htmlTag.getElementsByTagName("body").item(0).appendChild(w3cDoc.createComment(" comment3 "));
        assertEquals(html, new W3CDom().asString(w3cDoc));
    }

    @Test
    public void testAsStringWithNamespaces() {
        String xml = "<root xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:x='http://example.com'><x:a xsi:type='x:B'><x:c/></x:a></root>";
        Document w3cDoc = new DocumentBuilderFactory().newDocumentBuilder().parse(new StringReader(xml));
        assertEquals(xml, new W3CDom().asString(w3cDoc));
    }

    @Test
    public void testAsStringWithTraversal() throws Exception {
        String html = "<html><body><h1>Hello</h1><p>World</p></body></html>";
        Document w3cDoc = new DocumentBuilderFactory().newDocumentBuilder().parse(new InputSource(new StringReader(html)));
        TraversalHandler handler = new TraversalHandler() {
            private int count = 0;

            @Override
            public void startElement(Node node, int namespaceURI, String localName, String qName, Attributes attributes) {
                System.out.println(String.format("Started visiting %s with namespace %d", qName, namespaceURI));
                count++;
            }

            @Override
            public void endElement(Node node) {
                System.out.println(String.format("Finished visiting %s", node.getNodeName()));
            }
        };
        ((DocumentTraversal) w3cDoc).traverse(handler, NodeFilter.SHOW_ELEMENT, null, new NSResolver() {
            @Override
            public Node resolveNamespaceURI(String prefix, String uri) {
                if (prefix.equals("html")) {
                    return w3cDoc.getDocumentElement();
                }
                return null;
            }
        });
        assertEquals(2, count);
    }
}