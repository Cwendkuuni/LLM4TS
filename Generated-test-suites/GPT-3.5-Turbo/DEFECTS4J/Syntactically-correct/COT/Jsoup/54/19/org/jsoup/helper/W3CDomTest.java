
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Node;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        W3CDom w3CDom = new W3CDom();
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        Assert.assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = new Document();
        W3CDom w3CDom = new W3CDom();
        w3CDom.convert(jsoupDoc, w3cDoc);
        Assert.assertEquals("", w3cDoc.getDocumentURI());
        Assert.assertNull(w3cDoc.getChildNodes());
    }

    @Test
    public void testW3CBuilderHead() {
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("tagName");
        Element destEl = new Document().createElement("tagName");
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(new Document());
        w3CBuilder.head(sourceEl, 1);
        Assert.assertEquals(destEl, w3CBuilder.dest);
    }

    @Test
    public void testW3CBuilderTail() {
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("tagName");
        Element destEl = new Document().createElement("tagName");
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(new Document());
        w3CBuilder.dest = destEl;
        w3CBuilder.tail(sourceEl, 1);
        Assert.assertNull(w3CBuilder.dest.getParentNode());
    }

    @Test
    public void testW3CBuilderCopyAttributes() {
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("tagName");
        sourceEl.attr("attr1", "value1");
        sourceEl.attr("attr2", "value2");
        Element destEl = new Document().createElement("tagName");
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(new Document());
        w3CBuilder.copyAttributes(sourceEl, destEl);
        Assert.assertEquals("value1", destEl.getAttribute("attr1"));
        Assert.assertEquals("value2", destEl.getAttribute("attr2"));
    }

    @Test
    public void testW3CBuilderUpdateNamespaces() {
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("tagName");
        sourceEl.attr("xmlns", "urn1");
        sourceEl.attr("xmlns:prefix", "urn2");
        Element destEl = new Document().createElement("tagName");
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(new Document());
        w3CBuilder.updateNamespaces(sourceEl);
        Assert.assertEquals("urn1", w3CBuilder.namespaces.get(""));
        Assert.assertEquals("urn2", w3CBuilder.namespaces.get("prefix"));
    }

    @Test
    public void testAsString() {
        Document w3cDoc = new Document();
        W3CDom w3CDom = new W3CDom();
        String stringDoc = w3CDom.asString(w3cDoc);
        Assert.assertNotNull(stringDoc);
    }
}
