import static org.junit.Assert.assertEquals;
import org.jsoup.*;
import org.junit.Test;
import org.w3c.dom.*;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertEquals("html", w3cDoc.getDocumentElement().getTagName());
    }

    @Test
    public void testConvert() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Document out = w3cDom.convert(jsoupDoc, w3cDoc);
        assertEquals("html", out.getDocumentElement().getTagName());
    }

    @Test
    public void testW3CBuilderHeadElement() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
        Document doc = w3cBuilder.doc;
        w3cBuilder.head(sourceEl, 0);
        assertEquals("div", doc.getDocumentElement().getTagName());
    }

    @Test
    public void testW3CBuilderHeadTextNode() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.TextNode sourceText = new org.jsoup.nodes.TextNode("test");
        Document doc = w3cBuilder.doc;
        w3cBuilder.head(sourceText, 0);
        Element element = (Element) doc.getDocumentElement().getChildNodes().item(0);
        assertEquals("test", element.getFirstChild().getNodeValue());
    }

    @Test
    public void testW3CBuilderHeadComment() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Comment sourceComment = new org.jsoup.nodes.Comment("test");
        Document doc = w3cBuilder.doc;
        w3cBuilder.head(sourceComment, 0);
        Element element = (Element) doc.getDocumentElement().getChildNodes().item(0);
        assertEquals("<!--test-->", element.toString());
    }

    @Test
    public void testW3CBuilderHeadDataNode() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.DataNode sourceData = new org.jsoup.nodes.DataNode("test");
        Document doc = w3cBuilder.doc;
        w3cBuilder.head(sourceData, 0);
        Element element = (Element) doc.getDocumentElement().getChildNodes().item(0);
        assertEquals("test", element.getFirstChild().getNodeValue());
    }

    @Test
    public void testW3CBuilderTail() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
        Document doc = w3cBuilder.doc;
        w3cBuilder.head(sourceEl, 0);
        w3cBuilder.tail(sourceEl, 0);
        assertEquals(null, w3cBuilder.dest);
    }

    @Test
    public void testCopyAttributes() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
        sourceEl.attr("class", "test");
        Element element = w3cBuilder.doc.createElementNS("", "div");
        w3cBuilder.copyAttributes(sourceEl, element);
        assertEquals("test", element.getAttribute("class"));
    }

    @Test
    public void testUpdateNamespaces() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element("div");
        el.attr("xmlns:ns1", "ns1-value");
        el.attr("xmlns:ns2", "ns2-value");
        el.attr("xmlns", "default-ns-value");
        String prefix = w3cBuilder.updateNamespaces(el);
        assertEquals("", prefix);
        assertEquals("ns1-value", w3cBuilder.namespaces.get("ns1"));
        assertEquals("ns2-value", w3cBuilder.namespaces.get("ns2"));
        assertEquals("default-ns-value", w3cBuilder.namespaces.get(""));
    }

    @Test
    public void testAsString() {
        W3CDom w3cDom = new W3CDom();
        Document doc = Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
        String result = w3cDom.asString(doc);
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<html>" +
                "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
                "<title>Test</title>" +
                "</head>" +
                "<body>" +
                "<p>Hello World</p>" +
                "</body></html>";
        assertEquals(expected, result);
    }
}