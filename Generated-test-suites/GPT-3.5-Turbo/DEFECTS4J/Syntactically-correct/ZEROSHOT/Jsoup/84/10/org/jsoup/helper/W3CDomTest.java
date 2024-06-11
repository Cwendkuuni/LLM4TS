import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Text;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.DocumentBuilderFactory;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Stack;

import static org.junit.Assert.assertEquals;

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
        assertEquals("org.w3c.dom.Document", w3cDoc.getClass().getName());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.jsoup.nodes.Element rootEl = new org.jsoup.nodes.Element("html");
        jsoupDoc.appendChild(rootEl);
        w3cDom.convert(jsoupDoc, w3cDoc);
        assertEquals("html", w3cDoc.getDocumentElement().getNodeName());
    }

    @Test
    public void testW3CBuilderHead() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
        w3cBuilder.head(sourceEl, 0);
        assertEquals("div", w3cBuilder.dest.getNodeName());
    }

    @Test
    public void testW3CBuilderTail() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
        w3cBuilder.head(sourceEl, 0);
        w3cBuilder.tail(sourceEl, 0);
        assertEquals("div", w3cBuilder.dest.getNodeName());
    }

    @Test
    public void testW3CBuilderCopyAttributes() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
        sourceEl.attr("id", "testId");
        Element destEl = w3cBuilder.doc.createElement("div");
        w3cBuilder.copyAttributes(sourceEl, destEl);
        assertEquals("testId", destEl.getAttribute("id"));
    }

    @Test
    public void testW3CBuilderUpdateNamespaces() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
        sourceEl.attr("xmlns:test", "testNamespace");
        String prefix = w3cBuilder.updateNamespaces(sourceEl);
        assertEquals("test", prefix);
        assertEquals("testNamespace", w3cBuilder.namespacesStack.peek().get(prefix));
    }

    @Test
    public void testAsString() throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = builder.newDocument();
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><root/>";
        assertEquals(expected, w3cDom.asString(w3cDoc));
    }
}