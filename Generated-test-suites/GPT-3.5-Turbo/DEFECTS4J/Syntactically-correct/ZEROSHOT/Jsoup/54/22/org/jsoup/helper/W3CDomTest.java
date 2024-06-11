import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = new Document("https://www.example.com");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = new Document("https://www.example.com");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        Document out = new Document();
        w3cDom.convert(jsoupDoc, out);
        assertEquals(jsoupDoc.location(), out.location());
    }

    @Test
    public void testW3CBuilder() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(new org.w3c.dom.Document());
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
        builder.head(sourceEl, 1);
        assertNotNull(builder.dest);
        assertFalse(builder.namespaces.isEmpty());
        builder.tail(sourceEl, 1);
        assertNull(builder.dest);
    }

    @Test
    public void testCopyAttributes() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(new org.w3c.dom.Document());
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
        org.jsoup.nodes.Attribute attribute = new org.jsoup.nodes.Attribute("class", "test");
        sourceEl.attributes().put(attribute);
        org.w3c.dom.Element destEl = builder.doc.createElementNS("http://www.w3.org/1999/xhtml", "div");
        builder.copyAttributes(sourceEl, destEl);
        assertEquals("test", destEl.getAttribute("class"));
    }

    @Test
    public void testUpdateNamespaces() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(new org.w3c.dom.Document());
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element("div");
        el.attributes().put("xmlns", "http://www.w3.org/1999/xhtml");
        String prefix = builder.updateNamespaces(el);
        assertTrue(builder.namespaces.containsKey(prefix));
        assertEquals("http://www.w3.org/1999/xhtml", builder.namespaces.get(prefix));
    }

    @Test
    public void testAsString() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = new Document("https://www.example.com");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        String result = w3cDom.asString(w3cDoc);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}