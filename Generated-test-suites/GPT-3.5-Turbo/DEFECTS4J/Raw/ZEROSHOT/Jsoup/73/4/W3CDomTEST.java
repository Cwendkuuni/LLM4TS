import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.Document;
import org.jsoup.helper.W3CDom;

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
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);

        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("tag", "");
        w3cDom.convert(jsoupDoc, w3cDoc);
        assertEquals(w3cDoc.getDocumentElement().getTagName(), jsoupEl.tagName());
    }

    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);

        String expectedString = "<document></document>";
        String w3cDocString = w3cDom.asString(w3cDoc);
        assertEquals(expectedString, w3cDocString);
    }

    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("tag", "");
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        Document w3cDoc = null;

        org.jsoup.nodes.Attribute jsoupAttr = new org.jsoup.nodes.Attribute("key", "value");
        jsoupEl.attributes().put(jsoupAttr.getKey(), jsoupAttr.getValue());
        org.w3c.dom.Element w3cEl = w3cDoc.createElement("tag");
        w3cBuilder.copyAttributes(jsoupEl, w3cEl);

        assertEquals(jsoupAttr.getValue(), w3cEl.getAttribute(jsoupAttr.getKey()));
    }

    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("tag", "");
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);

        org.jsoup.nodes.Attribute attr = new org.jsoup.nodes.Attribute("xmlns", "namespace");
        jsoupEl.attributes().put(attr.getKey(), attr.getValue());
        String prefix = w3cBuilder.updateNamespaces(jsoupEl);

        assertEquals("", prefix);
        assertEquals(attr.getValue(), w3cBuilder.namespaces.get(prefix));
    }

}