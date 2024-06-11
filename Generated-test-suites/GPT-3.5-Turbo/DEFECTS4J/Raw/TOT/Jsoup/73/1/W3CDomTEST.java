import org.junit.Before;
import org.junit.Test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Text;

import static org.junit.Assert.*;

public class W3CDomTest {

    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDoc;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
        jsoupDoc = Jsoup.parse("<html><head><title>Test Document</title></head><body><h1>Test Heading</h1><p>Test paragraph</p></body></html>");
    }

    @Test
    public void testFromJsoup() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        assertTrue(w3cDoc instanceof org.w3c.dom.Document);
        assertEquals("Test Document", w3cDoc.getDocumentElement().getElementsByTagName("title").item(0).getTextContent());
        assertEquals("Test Heading", w3cDoc.getDocumentElement().getElementsByTagName("h1").item(0).getTextContent());
    }

    @Test
    public void testConvert() {
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Document out = w3cDom.convert(jsoupDoc, w3cDoc);

        assertNotNull(out);
        assertEquals(jsoupDoc.location(), out.getDocumentURI());
        assertEquals("Test Document", out.getDocumentElement().getElementsByTagName("title").item(0).getTextContent());
        assertEquals("Test Heading", out.getDocumentElement().getElementsByTagName("h1").item(0).getTextContent());
    }

    @Test
    public void testW3CBuilderHead() {
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.Element jsoupEl = jsoupDoc.createElement("div");

        builder.head(jsoupEl, 0);

        Element w3cEl = w3cDoc.createElementNS("", jsoupEl.tagName()); // should create a new element in W3C document
        assertEquals(w3cEl, builder.dest);
    }

    @Test
    public void testW3CBuilderTail() {
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.Element jsoupEl = jsoupDoc.createElement("div");
        Element w3cEl = w3cDoc.createElementNS("", jsoupEl.tagName()); // creates a new element in W3C document

        builder.dest = w3cEl;
        builder.tail(jsoupEl, 0);

        assertEquals(w3cEl.getParentNode(), builder.dest); // should undescend and set dest to parent element
    }

    @Test
    public void testW3CBuilderHeadTextNode() {
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.TextNode jsoupTextNode = jsoupDoc.createTextNode("Test Text");

        builder.head(jsoupTextNode, 0);

        Text w3cText = w3cDoc.createTextNode(jsoupTextNode.getWholeText()); // should create a new text node in W3C document
        assertEquals(w3cText, builder.dest.getFirstChild());
    }

    @Test
    public void testW3CBuilderHeadComment() {
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.Comment jsoupComment = jsoupDoc.createComment("Test Comment");

        builder.head(jsoupComment, 0);

        Comment w3cComment = w3cDoc.createComment(jsoupComment.getData()); // should create a new comment node in W3C document
        assertEquals(w3cComment, builder.dest.getFirstChild());
    }

    @Test
    public void testW3CBuilderHeadDataNode() {
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.DataNode jsoupDataNode = jsoupDoc.createDataNode("Test Data");

        builder.head(jsoupDataNode, 0);

        Text w3cData = w3cDoc.createTextNode(jsoupDataNode.getWholeData()); // should create a new data node in W3C document
        assertEquals(w3cData, builder.dest.getFirstChild());
    }

    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Element jsoupEl = jsoupDoc.createElement("div");
        jsoupEl.attr("id", "testId");
        jsoupEl.attr("class", "testClass");

        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Element w3cEl = w3cDoc.createElementNS("", jsoupEl.tagName());

        w3cDom.copyAttributes(jsoupEl, w3cEl);

        assertEquals("testId", w3cEl.getAttribute("id"));
        assertEquals("testClass", w3cEl.getAttribute("class"));
    }

    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element jsoupEl = jsoupDoc.createElement("div");
        jsoupEl.attr("xmlns", "http://www.example.com");
        jsoupEl.attr("xmlns:namespace1", "http://www.namespace1.com");
        jsoupEl.attr("xmlns:namespace2", "http://www.namespace2.com");
        
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Element w3cEl = w3cDoc.createElementNS("", jsoupEl.tagName());

        String prefix = w3cDom.updateNamespaces(jsoupEl);

        assertEquals("", prefix); // no prefix because 'xmlns' attribute represents the default namespace
        assertEquals("http://www.example.com", w3cEl.getNamespaceURI());

        assertEquals("http://www.example.com", w3cDom.namespaces.get(""));
        assertEquals("http://www.namespace1.com", w3cDom.namespaces.get("namespace1"));
        assertEquals("http://www.namespace2.com", w3cDom.namespaces.get("namespace2"));
    }

    @Test
    public void testAsString() {
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String docString = w3cDom.asString(w3cDoc);

        assertTrue(docString.contains("<html"));
        assertTrue(docString.contains("<head"));
        assertTrue(docString.contains("<title"));
        assertTrue(docString.contains("<body"));
        assertTrue(docString.contains("<h1"));
        assertTrue(docString.contains("<p"));
    }

}