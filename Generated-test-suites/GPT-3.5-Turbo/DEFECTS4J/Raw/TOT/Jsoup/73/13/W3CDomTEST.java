import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Document;

import static org.junit.Assert.*;

public class W3CDomTest {

    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDocument;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
        jsoupDocument = Jsoup.parse("<html><body><div>Test Content</div></body></html>");
    }

    @Test
    public void testFromJsoup() {
        Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        assertNotNull(w3cDocument);
        // assert conversion correctness by comparing with original jsoup document
        assertEquals(jsoupDocument.toString(), w3cDom.asString(w3cDocument));
    }

    @Test
    public void testConvert() {
        Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);

        // Create a new W3C Document for comparison
        Document newW3cDocument = w3cDom.factory.newDocumentBuilder().newDocument();
        w3cDom.convert(jsoupDocument, newW3cDocument);

        // assert conversion correctness by comparing with original converted w3c document
        assertEquals(w3cDom.asString(w3cDocument), w3cDom.asString(newW3cDocument));
    }

    @Test
    public void testW3CBuilderHead() {
        Document w3cDocument = w3cDom.factory.newDocumentBuilder().newDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDocument);

        // Test creation of W3C element when the source node is an element
        org.jsoup.nodes.Element sourceElement = jsoupDocument.select("div").first();
        builder.head(sourceElement, 0);
        Element createdElement = (Element) w3cDocument.getElementsByTagName("div").item(0);
        assertNotNull(createdElement);
        assertEquals(sourceElement.tagName(), createdElement.getTagName());

        // Test creation of W3C text node when the source node is a text node
        org.jsoup.nodes.TextNode sourceText = new org.jsoup.nodes.TextNode("Test Text", "");
        builder.head(sourceText, 1);
        Node createdTextNode = w3cDocument.getElementsByTagName("#text").item(0);
        assertNotNull(createdTextNode);
        assertEquals(sourceText.getWholeText(), createdTextNode.getNodeValue());

        // Test creation of W3C comment when the source node is a comment node
        org.jsoup.nodes.Comment sourceComment = new org.jsoup.nodes.Comment("Test Comment");
        builder.head(sourceComment, 2);
        Node createdCommentNode = w3cDocument.getElementsByTagName("#comment").item(0);
        assertNotNull(createdCommentNode);
        assertEquals(sourceComment.getData(), createdCommentNode.getNodeValue());

        // Test creation of W3C text node when the source node is a data node
        org.jsoup.nodes.DataNode sourceData = new org.jsoup.nodes.DataNode("Test Data", "");
        builder.head(sourceData, 3);
        Node createdDataNode = w3cDocument.getElementsByTagName("#text").item(1);
        assertNotNull(createdDataNode);
        assertEquals(sourceData.getWholeData(), createdDataNode.getNodeValue());

        // Test creation of W3C element when the source node is an unhandled node
        builder.head(null, 4);
        assertEquals(0, w3cDocument.getChildNodes().getLength());
    }

    @Test
    public void testW3CBuilderTail() {
        Document w3cDocument = w3cDom.factory.newDocumentBuilder().newDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDocument);

        org.jsoup.nodes.Element sourceElement1 = jsoupDocument.select("div").first();
        builder.head(sourceElement1, 0);
        assertEquals(1, w3cDocument.getChildNodes().getLength());

        org.jsoup.nodes.Element sourceElement2 = jsoupDocument.select("body").first();
        builder.head(sourceElement2, 1);
        assertEquals(1, w3cDocument.getElementsByTagName("div").item(0).getChildNodes().getLength());

        builder.tail(sourceElement2, 1);
        assertNull(builder.dest);
        assertEquals(1, w3cDocument.getChildNodes().getLength());
    }

    @Test
    public void testCopyAttributes() {
        Document w3cDocument = w3cDom.factory.newDocumentBuilder().newDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDocument);

        org.jsoup.nodes.Element sourceElement = jsoupDocument.select("div").first();
        Element destElement = w3cDocument.createElementNS("", "dummy");

        builder.copyAttributes(sourceElement, destElement);

        assertEquals(sourceElement.attributes().size(), destElement.getAttributes().getLength());
        for (int i = 0; i < sourceElement.attributes().size(); i++) {
            org.jsoup.nodes.Attribute jsoupAttribute = sourceElement.attributes().get(i);
            String expectedValue = jsoupAttribute.getValue();
            String actualValue = destElement.getAttribute(jsoupAttribute.getKey());
            assertEquals(expectedValue, actualValue);
        }
    }

    @Test
    public void testUpdateNamespaces() {
        Document w3cDocument = w3cDom.factory.newDocumentBuilder().newDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDocument);

        org.jsoup.nodes.Element element1 = new org.jsoup.nodes.Element("div");
        element1.attr("xmlns", "http://www.example.com");
        String prefix1 = builder.updateNamespaces(element1);
        assertEquals("", prefix1);

        org.jsoup.nodes.Element element2 = new org.jsoup.nodes.Element("prefix:div");
        element2.attr("xmlns:prefix", "http://www.example.com");
        String prefix2 = builder.updateNamespaces(element2);
        assertEquals("prefix", prefix2);

        org.jsoup.nodes.Element element3 = new org.jsoup.nodes.Element("p");
        element3.attr("class", "test");
        String prefix3 = builder.updateNamespaces(element3);
        assertEquals("", prefix3);

        assertEquals(2, builder.namespaces.size());
        assertTrue(builder.namespaces.containsKey(""));
        assertTrue(builder.namespaces.containsKey("prefix"));
        assertEquals("http://www.example.com", builder.namespaces.get(""));
        assertEquals("http://www.example.com", builder.namespaces.get("prefix"));
    }

    @Test
    public void testAsString() {
        Document w3cDocument = w3cDom.factory.newDocumentBuilder().newDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDocument);

        org.jsoup.nodes.Element element = jsoupDocument.select("div").first();
        builder.head(element, 0);

        String w3cString = w3cDom.asString(w3cDocument);
        assertTrue(w3cString.contains("<div>Test Content</div>"));
    }
}