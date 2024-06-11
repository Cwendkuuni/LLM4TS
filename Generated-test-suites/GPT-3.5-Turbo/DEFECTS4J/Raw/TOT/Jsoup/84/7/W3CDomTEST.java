import org.jsoup.helper.*;
import org.jsoup.nodes.*;
import org.w3c.dom.*;
import org.junit.*;
import static org.junit.Assert.*;

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
        Document w3cDoc = new Document();

        W3CDom w3cDom = new W3CDom();
        w3cDom.convert(jsoupDoc, w3cDoc);

        assertNotNull(w3cDoc.getDocumentURI());
    }

    @Test
    public void testW3CBuilderHead() {
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("tag");
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(new Document());

        builder.head(sourceEl, 0);

        assertNotNull(builder.dest);
    }

    @Test
    public void testW3CBuilderHeadTextNode() {
        org.jsoup.nodes.TextNode sourceText = new org.jsoup.nodes.TextNode("text");
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(new Document());

        builder.head(sourceText, 0);

        assertNotNull(builder.dest);
    }

    @Test
    public void testW3CBuilderHeadComment() {
        org.jsoup.nodes.Comment sourceComment = new org.jsoup.nodes.Comment("comment");
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(new Document());

        builder.head(sourceComment, 0);

        assertNotNull(builder.dest);
    }

    @Test
    public void testW3CBuilderHeadDataNode() {
        org.jsoup.nodes.DataNode sourceData = new org.jsoup.nodes.DataNode("data");
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(new Document());

        builder.head(sourceData, 0);

        assertNotNull(builder.dest);
    }

    @Test
    public void testW3CBuilderTail() {
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("tag");
        Element dest = new Element("dest");
        dest.appendChild(sourceEl);

        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(new Document());
        builder.dest = dest;

        builder.tail(sourceEl, 0);

        assertNull(builder.dest.getParentNode());
        assertEquals(0, builder.namespacesStack.size());
    }

    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("tag");
        sourceEl.attr("attr1", "value1");
        sourceEl.attr("attr2", "value2");

        Element el = new Element("el");

        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(new Document());
        builder.copyAttributes(sourceEl, el);

        assertNotNull(el.getAttribute("attr1"));
        assertNotNull(el.getAttribute("attr2"));
    }

    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element("tag");

        el.attr("xmlns", "namespace");

        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(new Document());
        String prefix = builder.updateNamespaces(el);

        assertEquals("", prefix);
        assertEquals("namespace", builder.namespacesStack.peek().get(prefix));
    }

    @Test
    public void testAsString() {
        Document doc = new Document();
        Element el = doc.createElementNS("namespace", "tag");
        doc.appendChild(el);

        W3CDom w3cDom = new W3CDom();
        String output = w3cDom.asString(doc);

        assertNotNull(output);
    }

}