import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.Document;
import org.jsoup.nodes.Element;

public class W3CDomTest {

    private W3CDom w3CDom = new W3CDom();

    @Test
    public void testFromJsoup() {
        // Test with null jsoup document
        try {
            w3CDom.fromJsoup(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }

        // Test with valid jsoup document
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("https://www.example.com");
        Element rootEl = new Element("div");
        jsoupDoc.appendChild(rootEl);

        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);

        assertEquals("https://www.example.com", w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvert() {
        // Test with null jsoup document
        try {
            w3CDom.convert(null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }

        // Test with valid jsoup document
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("https://www.example.com");
        Element rootEl = new Element("div");
        jsoupDoc.appendChild(rootEl);

        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        Document newW3cDoc = w3CDom.factory.newDocumentBuilder().newDocument();
        w3CDom.convert(jsoupDoc, newW3cDoc);

        assertEquals(w3cDoc.getDocumentURI(), newW3cDoc.getDocumentURI());
    }

    @Test
    public void testW3CBuilderHead() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");

        // Test with Element node
        builder.head(sourceEl, 0);

        assertNotNull(builder.dest);
        assertEquals("div", builder.dest.getTagName());

        // Test with TextNode
        org.jsoup.nodes.TextNode sourceText = new org.jsoup.nodes.TextNode("Text");
        builder.head(sourceText, 0);

        assertNotNull(builder.dest);
        assertEquals("Text", builder.dest.getTextContent());

        // Test with Comment node
        org.jsoup.nodes.Comment sourceComment = new org.jsoup.nodes.Comment("Comment");
        builder.head(sourceComment, 0);

        assertNotNull(builder.dest);
        assertEquals("<!--Comment-->", builder.dest.getTextContent());

        // Test with DataNode
        org.jsoup.nodes.DataNode sourceData = new org.jsoup.nodes.DataNode("Data");
        builder.head(sourceData, 0);

        assertNotNull(builder.dest);
        assertEquals("Data", builder.dest.getTextContent());
    }

    @Test
    public void testW3CBuilderTail() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");

        // Test with Element node
        builder.head(sourceEl, 0);
        builder.tail(sourceEl, 0);

        assertNull(builder.dest.getParentNode());
    }

    @Test
    public void testW3CBuilderUpdateNamespaces() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element("prefix:div");
        el.attr("xmlns", "http://www.example.com");
        el.attr("xmlns:prefix", "http://www.example.com");

        String prefix = builder.updateNamespaces(el);

        assertEquals("prefix", prefix);
        assertEquals("http://www.example.com", builder.namespaces.get(""));
        assertEquals("http://www.example.com", builder.namespaces.get("prefix"));
    }

    @Test
    public void testAsString() {
        // Test with null document
        try {
            w3CDom.asString(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }

        // Test serialization
        Document doc = w3CDom.factory.newDocumentBuilder().newDocument();
        Element rootEl = doc.createElement("div");
        doc.appendChild(rootEl);

        String stringDoc = w3CDom.asString(doc);

        assertNotNull(stringDoc);
        assertTrue(stringDoc.contains("<div></div>"));
    }
}