import org.jsoup.helper.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.jsoup.nodes.Element jsoupRootEl = jsoupDoc.createElement("root");
        jsoupDoc.appendChild(jsoupRootEl);
        w3cDom.convert(jsoupDoc, w3cDoc);
        Element w3cRootEl = w3cDoc.getDocumentElement();
        assertNotNull(w3cRootEl);
        assertEquals("root", w3cRootEl.getTagName());
    }

    @Test
    public void testW3CBuilderHead() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.jsoup.nodes.Element jsoupEl = jsoupDoc.createElement("tag");
        jsoupDoc.appendChild(jsoupEl);
        W3CDom.W3CBuilder builder = w3cDom.new W3CBuilder(w3cDoc);
        builder.head(jsoupEl, 0);
        Element w3cEl = w3cDoc.getDocumentElement();
        assertNotNull(w3cEl);
        assertEquals("tag", w3cEl.getTagName());
    }

    @Test
    public void testW3CBuilderTail() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.jsoup.nodes.Element jsoupEl = jsoupDoc.createElement("tag");
        jsoupDoc.appendChild(jsoupEl);
        W3CDom.W3CBuilder builder = w3cDom.new W3CBuilder(w3cDoc);
        builder.head(jsoupEl, 0);
        builder.tail(jsoupEl, 0);
        Element w3cRootEl = w3cDoc.getDocumentElement();
        assertNotNull(w3cRootEl);
        assertEquals("", w3cRootEl.getTagName());
    }

    @Test
    public void testW3CBuilderHeadTextNode() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.jsoup.nodes.TextNode jsoupText = jsoupDoc.createTextNode("text");
        jsoupDoc.appendChild(jsoupText);
        W3CDom.W3CBuilder builder = w3cDom.new W3CBuilder(w3cDoc);
        builder.head(jsoupText, 0);
        Text w3cText = w3cDoc.getDocumentElement().getChildNodes().item(0);
        assertNotNull(w3cText);
        assertEquals("text", w3cText.getData());
    }

    @Test
    public void testW3CBuilderHeadComment() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.jsoup.nodes.Comment jsoupComment = jsoupDoc.createComment("comment");
        jsoupDoc.appendChild(jsoupComment);
        W3CDom.W3CBuilder builder = w3cDom.new W3CBuilder(w3cDoc);
        builder.head(jsoupComment, 0);
        Comment w3cComment = (Comment) w3cDoc.getDocumentElement().getChildNodes().item(0);
        assertNotNull(w3cComment);
        assertEquals("comment", w3cComment.getData());
    }

    @Test
    public void testW3CBuilderHeadDataNode() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.jsoup.nodes.DataNode jsoupData = jsoupDoc.createDataNode("data");
        jsoupDoc.appendChild(jsoupData);
        W3CDom.W3CBuilder builder = w3cDom.new W3CBuilder(w3cDoc);
        builder.head(jsoupData, 0);
        Text w3cData = (Text) w3cDoc.getDocumentElement().getChildNodes().item(0);
        assertNotNull(w3cData);
        assertEquals("data", w3cData.getData());
    }

    @Test
    public void testW3CBuilderCopyAttributes() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.jsoup.nodes.Element jsoupEl = jsoupDoc.createElement("tag");
        jsoupEl.attr("attr", "value");
        jsoupDoc.appendChild(jsoupEl);
        W3CDom.W3CBuilder builder = w3cDom.new W3CBuilder(w3cDoc);
        Element w3cEl = w3cDoc.createElementNS("", jsoupEl.tagName());
        builder.copyAttributes(jsoupEl, w3cEl);
        Attr w3cAttr = w3cEl.getAttributeNode("attr");
        assertNotNull(w3cAttr);
        assertEquals("value", w3cAttr.getValue());
    }

    @Test
    public void testW3CBuilderUpdateNamespaces() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.jsoup.nodes.Element jsoupEl = jsoupDoc.createElement("tag");
        jsoupEl.attr("xmlns", "namespace");
        jsoupDoc.appendChild(jsoupEl);
        W3CDom.W3CBuilder builder = w3cDom.new W3CBuilder(w3cDoc);
        String prefix = builder.updateNamespaces(jsoupEl);
        assertNotNull(prefix);
        assertEquals("", prefix);
        assertEquals("namespace", builder.namespaces.get(prefix));
    }

    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String xmlString = w3cDom.asString(w3cDoc);
        assertNotNull(xmlString);
        assertEquals("", xmlString);
    }
}