import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class W3CDomTest {

    private W3CDom w3cDom;
    private Document jsoupDoc;

    @Before
    public void setUp() throws Exception {
        w3cDom = new W3CDom();
        jsoupDoc = new org.jsoup.nodes.Document("");
    }

    @Test
    public void testFromJsoup() throws Exception {
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        assertEquals(org.w3c.dom.Document.class, w3cDoc.getClass());
    }

    @Test
    public void testConvert() throws Exception {
        org.w3c.dom.Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        w3cDom.convert(jsoupDoc, w3cDoc);
        assertNotNull(w3cDoc.getDocumentElement());
    }

    @Test
    public void testW3CBuilder() throws Exception {
        org.w3c.dom.Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("div", "");
        w3cBuilder.head(jsoupEl, 0);
        assertNotNull(w3cBuilder.dest);
        assertEquals("div", w3cBuilder.dest.getNodeName());

        org.jsoup.nodes.TextNode jsoupText = new org.jsoup.nodes.TextNode("test text", "");
        w3cBuilder.head(jsoupText, 0);
        assertNotNull(w3cBuilder.dest);
        assertEquals("test text", w3cBuilder.dest.getTextContent());

        org.jsoup.nodes.Comment jsoupComment = new org.jsoup.nodes.Comment("test comment", "");
        w3cBuilder.head(jsoupComment, 0);
        assertNotNull(w3cBuilder.dest);
        assertEquals("test comment", w3cBuilder.dest.getNodeValue());

        org.jsoup.nodes.DataNode jsoupData = new org.jsoup.nodes.DataNode("test data", "");
        w3cBuilder.head(jsoupData, 0);
        assertNotNull(w3cBuilder.dest);
        assertEquals("test data", w3cBuilder.dest.getNodeValue());

        w3cBuilder.tail(jsoupEl, 0);
        assertNull(w3cBuilder.dest);
    }

    @Test
    public void testCopyAttributes() throws Exception {
        org.w3c.dom.Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        org.w3c.dom.Element w3cEl = w3cDoc.createElement("div");
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("div", "");
        jsoupEl.attr("id", "testId");
        jsoupEl.attr("class", "testClass");

        w3cBuilder.copyAttributes(jsoupEl, w3cEl);
        assertEquals("testId", w3cEl.getAttribute("id"));
        assertEquals("testClass", w3cEl.getAttribute("class"));
    }

    @Test
    public void testUpdateNamespaces() throws Exception {
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("div", "");
        jsoupEl.attr("xmlns", "testNamespace");
        jsoupEl.attr("xmlns:prefix", "testPrefix");
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);

        String prefix = w3cBuilder.updateNamespaces(jsoupEl);
        assertEquals("testPrefix", prefix);
        assertEquals("testNamespace", w3cBuilder.namespaces.get("testPrefix"));
    }

    @Test
    public void testAsString() throws Exception {
        org.w3c.dom.Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element w3cEl = w3cDoc.createElement("div");
        w3cDoc.appendChild(w3cEl);

        String result = w3cDom.asString(w3cDoc);
        assertNotNull(result);
        assertTrue(result.contains("<div"));
    }
}