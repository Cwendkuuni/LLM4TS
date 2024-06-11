import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.TextNode;

public class W3CDomTest {

    private W3CDom w3CDom;

    @Before
    public void setup() {
        w3CDom = new W3CDom();
    }

    @Test
    public void testFromJsoupNotNull() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test(expected = IllegalStateException.class)
    public void testFromJsoupNull() {
        w3CDom.fromJsoup(null);
    }

    @Test
    public void testConvertWithLocation() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        jsoupDoc.setBaseUri("http://example.com");
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertEquals("http://example.com", w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvertWithoutLocation() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertNull(w3cDoc.getDocumentURI());
    }

    @Test
    public void testW3CBuilderHeadElement() {
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
        Document w3cDoc = w3CDom.fromJsoup(Jsoup.parse(""));
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);

        w3cBuilder.head(sourceEl, 0);

        assertEquals(w3cDoc.getDocumentElement().getNodeName(), "div");
    }

    @Test
    public void testW3CBuilderHeadTextNode() {
        org.jsoup.nodes.TextNode sourceText = new org.jsoup.nodes.TextNode("Hello World");
        Document w3cDoc = w3CDom.fromJsoup(Jsoup.parse(""));
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);

        w3cBuilder.head(sourceText, 0);

        assertEquals(w3cDoc.createTextNode("").getNodeValue(), sourceText.getWholeText());
    }

    @Test
    public void testW3CBuilderHeadComment() {
        org.jsoup.nodes.Comment sourceComment = new org.jsoup.nodes.Comment("This is a comment");
        Document w3cDoc = w3CDom.fromJsoup(Jsoup.parse(""));
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);

        w3cBuilder.head(sourceComment, 0);

        assertEquals(w3cDoc.getComments().item(0).getNodeValue(), sourceComment.getData());
    }

    @Test
    public void testW3CBuilderHeadDataNode() {
        org.jsoup.nodes.DataNode sourceData = new org.jsoup.nodes.DataNode("Some data");
        Document w3cDoc = w3CDom.fromJsoup(Jsoup.parse(""));
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);

        w3cBuilder.head(sourceData, 0);

        assertEquals(w3cDoc.createTextNode("").getNodeValue(), sourceData.getWholeData());
    }

    @Test
    public void testW3CBuilderTailElement() {
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
        Document w3cDoc = w3CDom.fromJsoup(Jsoup.parse(""));
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);

        w3cBuilder.head(sourceEl, 0);
        w3cBuilder.tail(sourceEl, 0);

        assertEquals(w3cDoc.getDocumentElement(), w3cDoc.getDocumentElement().getLastChild());
    }

    @Test
    public void testW3CBuilderTailNonElement() {
        org.jsoup.nodes.TextNode sourceText = new org.jsoup.nodes.TextNode("Hello World");
        Document w3cDoc = w3CDom.fromJsoup(Jsoup.parse(""));
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);

        w3cBuilder.head(sourceText, 0);
        w3cBuilder.tail(sourceText, 0);

        assertEquals(w3cDoc.createTextNode("").getNodeValue(), sourceText.getWholeText());
    }

    @Test
    public void testAsString() {
        Document w3cDoc = w3CDom.fromJsoup(Jsoup.parse("<html><body><h1>Hello World</h1></body></html>"));
        String w3cDocString = w3CDom.asString(w3cDoc);
        assertTrue(w3cDocString.contains("<html>"));
        assertTrue(w3cDocString.contains("<body>"));
        assertTrue(w3cDocString.contains("<h1>"));
    }

}