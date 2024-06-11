import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Comment;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;

import static org.junit.Assert.*;

public class W3CDomTest {
    private W3CDom w3cDom;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        org.jsoup.nodes.Element rootElement = new org.jsoup.nodes.Element("html");
        org.jsoup.nodes.Element childElement = new org.jsoup.nodes.Element("body");
        childElement.appendChild(new org.jsoup.nodes.TextNode("Hello World"));
        rootElement.appendChild(childElement);
        jsoupDoc.appendChild(rootElement);

        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
        assertEquals("html", w3cDoc.getDocumentElement().getTagName());
        assertEquals("body", w3cDoc.getDocumentElement().getChildNodes().item(0).getNodeName());
        assertEquals("Hello World", w3cDoc.getDocumentElement().getChildNodes().item(0).getTextContent());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        org.jsoup.nodes.Element rootElement = new org.jsoup.nodes.Element("html");
        org.jsoup.nodes.Element childElement = new org.jsoup.nodes.Element("body");
        childElement.appendChild(new org.jsoup.nodes.TextNode("Hello World"));
        rootElement.appendChild(childElement);
        jsoupDoc.appendChild(rootElement);

        org.w3c.dom.Document w3cDoc = null;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            w3cDoc = builder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        w3cDom.convert(jsoupDoc, w3cDoc);

        assertNotNull(w3cDoc);
        assertEquals("html", w3cDoc.getDocumentElement().getTagName());
        assertEquals("body", w3cDoc.getDocumentElement().getChildNodes().item(0).getNodeName());
        assertEquals("Hello World", w3cDoc.getDocumentElement().getChildNodes().item(0).getTextContent());
    }

    @Test
    public void testW3CBuilderHead() {
        org.jsoup.nodes.Element element = new org.jsoup.nodes.Element("testElement");
        org.jsoup.nodes.NodeVisitor nodeVisitor = w3cDom.new W3CBuilder(null);

        ((W3CDom.W3CBuilder)nodeVisitor).head(element, 0);

        assertNotNull(((W3CDom.W3CBuilder)nodeVisitor).doc);
        assertNotNull(((W3CDom.W3CBuilder)nodeVisitor).namespaces);
        assertNotNull(((W3CDom.W3CBuilder)nodeVisitor).dest);
    }

    @Test
    public void testW3CBuilderTail() {
        org.jsoup.nodes.Element element = new org.jsoup.nodes.Element("testElement");
        org.w3c.dom.Document w3cDoc = null;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            w3cDoc = builder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        org.jsoup.nodes.NodeVisitor nodeVisitor = w3cDom.new W3CBuilder(w3cDoc);

        ((W3CDom.W3CBuilder)nodeVisitor).dest = w3cDoc.createElement("testElement");
        ((W3CDom.W3CBuilder)nodeVisitor).tail(element, 0);

        assertNotNull(((W3CDom.W3CBuilder)nodeVisitor).dest);
    }

    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        org.jsoup.nodes.Element rootElement = new org.jsoup.nodes.Element("html");
        org.jsoup.nodes.Element childElement = new org.jsoup.nodes.Element("body");
        childElement.appendChild(new org.jsoup.nodes.TextNode("Hello World"));
        rootElement.appendChild(childElement);
        jsoupDoc.appendChild(rootElement);

        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String w3cString = w3cDom.asString(w3cDoc);

        assertNotNull(w3cString);
        assertTrue(w3cString.contains("Hello World"));
    }
}