import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.w3c.dom.Document;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for W3CDom.
 */
public class W3CDomTest {

    /**
     * Test converting a jsoup document to a w3c document.
     */
    @Test
    public void testFromJsoup() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        Document jsoupDocument = Jsoup.parse(html);
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        assertNotNull(w3cDocument);
        assertEquals("<html xmlns:xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>Test</title></head><body><p>Hello World!</p></body></html>", asString(w3cDocument));
    }

    /**
     * Test converting a jsoup document to a w3c document with namespaces.
     */
    @Test
    public void testFromJsoupWithNamespaces() {
        String html = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xmlns:test=\"http://example.com/\"><head><title>Test</title></head><body><p xmlns:test=\"http://example.com/\">Hello World!</p></body></html>";
        Document jsoupDocument = Jsoup.parse(html);
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        assertNotNull(w3cDocument);
        assertEquals(html, asString(w3cDocument));
    }

    /**
     * Test converting a jsoup document to a w3c document with an empty document.
     */
    @Test
    public void testFromJsoupEmpty() {
        Document jsoupDocument = new Document();
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        assertNotNull(w3cDocument);
        assertEquals("<document/>", asString(w3cDocument));
    }

    /**
     * Test converting a jsoup document to a w3c document with an error.
     */
    @Test(expected = IllegalStateException.class)
    public void testFromJsoupError() {
        String html = "<html><head><title>Test</title></head><body><p>Hello <script> alert('Hello World!'); </script></p></body></html>";
        Document jsoupDocument = Jsoup.parse(html);
        new W3CDom().fromJsoup(jsoupDocument);
    }

    /**
     * Test converting a jsoup document to a w3c document and serializing it to a string.
     */
    @Test
    public void testAsString() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        Document jsoupDocument = Jsoup.parse(html);
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        String result = new W3CDom().asString(w3cDocument);

        assertEquals("<html xmlns:xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>Test</title></head><body><p>Hello World!</p></body></html>", result);
    }

    /**
     * Helper method to convert a w3c document to a string.
     */
    private String asString(Document doc) {
        return new W3CDom().asString(doc);
    }

}