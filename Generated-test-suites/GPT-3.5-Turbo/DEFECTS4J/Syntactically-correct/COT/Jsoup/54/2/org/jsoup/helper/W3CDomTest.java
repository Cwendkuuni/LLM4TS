import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.*;
import org.w3c.dom.*;

public class W3CDomTest {
    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDoc;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
        jsoupDoc = Jsoup.parse("<html><head><title>Test Document</title></head><body><h1>Heading</h1><p>Paragraph</p></body></html>");
    }

    @Test
    public void testFromJsoup() {
        Document doc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(doc);
        assertEquals("Test Document", doc.getElementsByTagName("title").item(0).getTextContent());
        assertEquals("Heading", doc.getElementsByTagName("h1").item(0).getTextContent());
        assertEquals("Paragraph", doc.getElementsByTagName("p").item(0).getTextContent());
    }

    @Test
    public void testConvert() {
        Document doc = w3cDom.fromJsoup(jsoupDoc);
        Document out = w3cDom.convert(doc);
        assertNotNull(out);
        assertEquals("Test Document", out.getElementsByTagName("title").item(0).getTextContent());
        assertEquals("Heading", out.getElementsByTagName("h1").item(0).getTextContent());
        assertEquals("Paragraph", out.getElementsByTagName("p").item(0).getTextContent());
    }

    @Test
    public void testAsString() {
        Document doc = w3cDom.fromJsoup(jsoupDoc);
        String result = w3cDom.asString(doc);
        assertNotNull(result);
        assertTrue(result.contains("<title>Test Document</title>"));
        assertTrue(result.contains("<h1>Heading</h1>"));
        assertTrue(result.contains("<p>Paragraph</p>"));
    }

    @Test
    public void testConvertWithBlankLocation() {
        org.jsoup.nodes.Document blankLocation = Jsoup.parse("", "");
        Document doc = w3cDom.fromJsoup(blankLocation);
        assertNotNull(doc);
        assertEquals("", doc.getDocumentURI());
    }

    @Test(expected = IllegalStateException.class)
    public void testConvertWithParserConfigurationException() {
        org.jsoup.nodes.Document invalidDoc = Jsoup.parse("");
        w3cDom.convert(invalidDoc, null);
    }

    @Test
    public void testConvertWithInvalidElement() {
        org.jsoup.nodes.Element invalidElement = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("invalid"), "");
        Document doc = w3cDom.fromJsoup(invalidElement);
        assertNull(doc);
    }

    @Test
    public void testConvertWithTextNode() {
        org.jsoup.nodes.TextNode textNode = new org.jsoup.nodes.TextNode("Test", "");
        Document doc = w3cDom.fromJsoup(textNode);
        assertNotNull(doc);
        assertEquals("Test", doc.getTextContent());
    }

    @Test
    public void testConvertWithComment() {
        org.jsoup.nodes.Comment comment = new org.jsoup.nodes.Comment("Test");
        Document doc = w3cDom.fromJsoup(comment);
        assertNotNull(doc);
        assertEquals("<!--Test-->", w3cDom.asString(doc));
    }

    @Test
    public void testConvertWithDataNode() {
        org.jsoup.nodes.DataNode dataNode = new org.jsoup.nodes.DataNode("Test", "");
        Document doc = w3cDom.fromJsoup(dataNode);
        assertNotNull(doc);
        assertEquals("Test", doc.getTextContent());
    }
}