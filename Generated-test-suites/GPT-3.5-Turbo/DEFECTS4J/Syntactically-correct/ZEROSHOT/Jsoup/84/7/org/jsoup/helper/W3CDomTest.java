import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.*;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        W3CDom w3cDom = new W3CDom();
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Parser.parse(html, "");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        Element rootElement = w3cDoc.getDocumentElement();
        assertEquals("html", rootElement.getTagName());
    }

    @Test
    public void testConvert() {
        W3CDom w3cDom = new W3CDom();
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Parser.parse(html, "");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Document convertedDoc = w3cDom.convert(jsoupDoc, w3cDoc);
        assertNotNull(convertedDoc);
        Element rootElement = convertedDoc.getDocumentElement();
        assertEquals("html", rootElement.getTagName());
    }

    @Test
    public void testCopyAttributes() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("div");
        jsoupEl.attr("id", "myDiv");
        Element w3cEl = new Element("div");
        w3cBuilder.copyAttributes(jsoupEl, w3cEl);
        assertEquals("myDiv", w3cEl.getAttribute("id"));
    }

    @Test
    public void testUpdateNamespaces() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("div");
        jsoupEl.attr("xmlns", "http://www.w3.org/1999/xhtml");
        jsoupEl.attr("xmlns:custom", "http://www.example.com");
        String prefix = w3cBuilder.updateNamespaces(jsoupEl);
        assertEquals("", w3cBuilder.namespacesStack.peek().get(""));
        assertEquals("http://www.w3.org/1999/xhtml", w3cBuilder.namespacesStack.peek().get("custom"));
        assertEquals("", prefix);
    }

    @Test
    public void testAsString() {
        W3CDom w3cDom = new W3CDom();
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Parser.parse(html, "");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String str = w3cDom.asString(w3cDoc);
        assertNotNull(str);
        assertTrue(str.contains("<html"));
        assertTrue(str.contains("<head"));
        assertTrue(str.contains("<title"));
        assertTrue(str.contains("<body"));
        assertTrue(str.contains("<h1"));
        assertTrue(str.contains("Hello World!"));
    }
}