package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.*;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class W3CDomTest {

    private W3CDom w3cDom;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            w3cDom.convert(jsoupDoc, w3cDoc);
            assertNotNull(w3cDoc);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testW3CBuilderHeadElement() {
        Document w3cDoc = createW3CDocument();
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("div", "");
        NodeVisitor nodeVisitor = new W3CDom.W3CBuilder(w3cDoc);
        nodeVisitor.head(jsoupEl, 0);
        NodeList nodeList = w3cDoc.getElementsByTagName("div");
        assertEquals(1, nodeList.getLength());
        assertTrue(nodeList.item(0) instanceof Element);
    }

    @Test
    public void testW3CBuilderHeadTextNode() {
        Document w3cDoc = createW3CDocument();
        org.jsoup.nodes.TextNode jsoupText = new org.jsoup.nodes.TextNode("text", "");
        NodeVisitor nodeVisitor = new W3CDom.W3CBuilder(w3cDoc);
        nodeVisitor.head(jsoupText, 0);
        NodeList nodeList = w3cDoc.getElementsByTagName("#text");
        assertEquals(1, nodeList.getLength());
        assertTrue(nodeList.item(0) instanceof Text);
    }

    @Test
    public void testW3CBuilderHeadComment() {
        Document w3cDoc = createW3CDocument();
        org.jsoup.nodes.Comment jsoupComment = new org.jsoup.nodes.Comment("comment", "");
        NodeVisitor nodeVisitor = new W3CDom.W3CBuilder(w3cDoc);
        nodeVisitor.head(jsoupComment, 0);
        NodeList nodeList = w3cDoc.getElementsByTagName("#comment");
        assertEquals(1, nodeList.getLength());
        assertTrue(nodeList.item(0) instanceof Comment);
    }

    @Test
    public void testW3CBuilderHeadDataNode() {
        Document w3cDoc = createW3CDocument();
        org.jsoup.nodes.DataNode jsoupData = new org.jsoup.nodes.DataNode("data", "");
        NodeVisitor nodeVisitor = new W3CDom.W3CBuilder(w3cDoc);
        nodeVisitor.head(jsoupData, 0);
        NodeList nodeList = w3cDoc.getElementsByTagName("#text");
        assertEquals(1, nodeList.getLength());
        assertTrue(nodeList.item(0) instanceof Text);
    }

    @Test
    public void testW3CBuilderTail() {
        Document w3cDoc = createW3CDocument();
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("div", "");
        NodeVisitor nodeVisitor = new W3CDom.W3CBuilder(w3cDoc);
        nodeVisitor.head(jsoupEl, 0);
        nodeVisitor.tail(jsoupEl, 0);
        Element root = w3cDoc.getDocumentElement();
        assertEquals(root, ((W3CDom.W3CBuilder) nodeVisitor).dest);
    }

    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("div", "");
        Attr attribute = new Attr() {
            @Override
            public String getName() {
                return "attribute";
            }

            @Override
            public boolean getSpecified() {
                return false;
            }

            @Override
            public String getValue() {
                return "value";
            }

            @Override
            public void setValue(String value) throws DOMException {

            }

            @Override
            public Element getOwnerElement() {
                return null;
            }

            @Override
            public TypeInfo getSchemaTypeInfo() {
                return null;
            }

            @Override
            public boolean isId() {
                return false;
            }
        };
        jsoupEl.attributes().put(attribute);
        Document w3cDoc = createW3CDocument();
        Element w3cEl = w3cDoc.createElement("div");
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        w3cBuilder.copyAttributes(jsoupEl, w3cEl);
        assertEquals("value", w3cEl.getAttribute("attribute"));
    }

    @Test
    public void testUpdateNamespaces() {
        Document w3cDoc = createW3CDocument();
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("div", "");
        jsoupEl.attr("xmlns", "http://www.w3.org/1999/xhtml");
        jsoupEl.attr("xmlns:pref", "http://www.example.com");
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        String prefix = w3cBuilder.updateNamespaces(jsoupEl);
        assertEquals("pref", prefix);
        assertEquals("http://www.w3.org/1999/xhtml", w3cBuilder.namespaces.get(""));
        assertEquals("http://www.example.com", w3cBuilder.namespaces.get("pref"));
    }

    @Test
    public void testAsString() {
        Document w3cDoc = createW3CDocument();
        W3CDom w3cDom = new W3CDom();
        String result = w3cDom.asString(w3cDoc);
        assertNotNull(result);
    }

    private Document createW3CDocument() {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            return builder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
}