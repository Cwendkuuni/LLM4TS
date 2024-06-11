import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import static org.junit.Assert.*;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        W3CDom w3CDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        W3CDom w3CDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        w3CDom.convert(jsoupDoc, w3cDoc);
        assertNotNull(w3cDoc.getDocumentURI());
    }

    @Test
    public void testAsString() {
        W3CDom w3CDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        w3CDom.convert(jsoupDoc, w3cDoc);
        String result = w3CDom.asString(w3cDoc);
        assertNotNull(result);
    }

    @Test
    public void testW3CBuilderHead() {
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
        Element element = w3CBuilder.doc.createElement(sourceEl.tagName());
        w3CBuilder.head(sourceEl, 0);
        assertNotNull(element);
    }

    @Test
    public void testW3CBuilderTail() {
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
        Element element = w3CBuilder.doc.createElement(sourceEl.tagName());
        w3CBuilder.dest = element;
        w3CBuilder.tail(sourceEl, 0);
        assertNull(w3CBuilder.dest);
    }

    @Test
    public void testW3CBuilderCopyAttributes() {
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
        Element element = w3CBuilder.doc.createElement(sourceEl.tagName());
        sourceEl.attributes().put("attr", "value");
        w3CBuilder.copyAttributes(sourceEl, element);
        String attrValue = element.getAttribute("attr");
        assertEquals("value", attrValue);
    }

    @Test
    public void testW3CBuilderUpdateNamespaces() {
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
        sourceEl.attributes().put("xmlns:prefix", "namespaceValue");
        String prefix = w3CBuilder.updateNamespaces(sourceEl);
        assertEquals("prefix", prefix);
    }
}