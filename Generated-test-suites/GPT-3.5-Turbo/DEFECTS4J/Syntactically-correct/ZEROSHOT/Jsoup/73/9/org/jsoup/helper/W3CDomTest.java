import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;

public class W3CDomTest {

    @Test
    public void testFromJsoupNotNull() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        org.jsoup.nodes.Element rootEl = new org.jsoup.nodes.Element("root");
        jsoupDoc.appendChild(rootEl);

        w3cDom.convert(jsoupDoc, w3cDoc);
        assertEquals("root", w3cDoc.getDocumentElement().getTagName());
    }

    @Test
    public void testCopyAttributes() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("test");
        sourceEl.attr("attr1", "value1");
        sourceEl.attr("attr2", "value2");

        Document w3cDoc = null; // Mock

        Element destEl = w3cDoc.createElementNS(null, "test");
        builder.copyAttributes(sourceEl, destEl);

        assertEquals("value1", destEl.getAttribute("attr1"));
        assertEquals("value2", destEl.getAttribute("attr2"));
    }

    @Test
    public void testUpdateNamespaces() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element("test");
        el.attr("xmlns", "http://www.w3.org/2000/xmlns/");
        el.attr("xmlns:prefix", "http://www.w3.org/2000/xmlns/prefix");

        String prefix = builder.updateNamespaces(el);

        assertEquals("", prefix);
        assertEquals("http://www.w3.org/2000/xmlns/", builder.namespaces.get(""));
        assertEquals("http://www.w3.org/2000/xmlns/prefix", builder.namespaces.get("prefix"));
    }

    @Test
    public void testAsString() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        String w3cString = w3cDom.asString(w3cDoc);
        assertNotNull(w3cString);
    }

}