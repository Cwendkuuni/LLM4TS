import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Text;

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

public class W3CDomTest {
    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Element element = new Element("div");
        jsoupDoc.appendChild(element);
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);

        org.jsoup.nodes.TextNode textNode = new  TextNode("Hello", "");
        element.appendChild(textNode);
        Comment comment = new Comment("Test comment");
        element.appendChild(comment);

        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        builder.head(element, 0);
        builder.tail(element, 0);

        assertNotNull(w3cDoc.getDocumentElement());
        assertEquals("div", w3cDoc.getDocumentElement().getTagName());
        assertEquals("Hello", w3cDoc.getDocumentElement().getTextContent());
        assertEquals(1, w3cDoc.getDocumentElement().getChildNodes().getLength());
        assertEquals("#comment", w3cDoc.getDocumentElement().getChildNodes().item(1).getNodeName());
    }

    @Test
    public void testW3CBuilderHead() {
        Element jsoupElement = new Element("div");
        jsoupElement.appendChild(new TextNode("Hello", ""));
        Comment comment = new Comment("Test comment");
        jsoupElement.appendChild(comment);

        Document w3cDoc = createW3CDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        builder.head(jsoupElement, 0);

        assertNotNull(w3cDoc.getDocumentElement());
        assertEquals("div", w3cDoc.getDocumentElement().getTagName());
        assertEquals("Hello", w3cDoc.getDocumentElement().getTextContent());
        assertEquals(1, w3cDoc.getDocumentElement().getChildNodes().getLength());
        assertEquals("#comment", w3cDoc.getDocumentElement().getChildNodes().item(1).getNodeName());
    }

    @Test
    public void testW3CBuilderTail() {
        Element jsoupElement = new Element("div");
        jsoupElement.appendChild(new TextNode("Hello", ""));
        Comment comment = new Comment("Test comment");
        jsoupElement.appendChild(comment);

        Document w3cDoc = createW3CDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        builder.head(jsoupElement, 0);
        builder.tail(jsoupElement, 0);

        assertNull(w3cDoc.getDocumentElement().getParentNode());
    }

    @Test
    public void testCopyAttributes() {
        Element jsoupElement = new Element("div");
        jsoupElement.attr("name", "test");
        jsoupElement.attr("class", "test-class");

        Document w3cDoc = createW3CDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        Element w3cElement = w3cDoc.createElementNS("", "div");

        builder.copyAttributes(jsoupElement, w3cElement);

        assertEquals("test", w3cElement.getAttribute("name"));
        assertEquals("test-class", w3cElement.getAttribute("class"));
    }

    @Test
    public void testUpdateNamespaces() {
        Element jsoupElement = new Element("div");
        jsoupElement.attr("xmlns", "http://www.example.com");
        jsoupElement.attr("xmlns:prefix", "http://www.example.com/prefix");

        Document w3cDoc = createW3CDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        String tagPrefix = builder.updateNamespaces(jsoupElement);

        assertEquals("prefix", tagPrefix);
        assertEquals("http://www.example.com", builder.namespaces.get(""));
        assertEquals("http://www.example.com/prefix", builder.namespaces.get("prefix"));
    }

    @Test
    public void testAsString() {
        Document w3cDoc = createW3CDocument();
        W3CDom w3cDom = new W3CDom();
        String xml = w3cDom.asString(w3cDoc);

        assertNotNull(xml);
    }

    private Document createW3CDocument() {
        DocumentBuilder builder;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            return builder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
}