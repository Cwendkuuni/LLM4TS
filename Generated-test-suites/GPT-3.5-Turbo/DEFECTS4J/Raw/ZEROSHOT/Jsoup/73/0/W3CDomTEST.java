import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.*;
import org.w3c.dom.*;

public class W3CDomTest {

    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDoc;
    private Document w3cDoc;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
        jsoupDoc = new org.jsoup.nodes.Document("");
        w3cDoc = w3cDom.fromJsoup(jsoupDoc);
    }

    @Test
    public void testFromJsoup() {
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        DocumentBuilder builder;
        try {
            builder = w3cDom.factory.newDocumentBuilder();
            Document out = builder.newDocument();
            w3cDom.convert(jsoupDoc, out);
            assertNotNull(out);
        } catch (ParserConfigurationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div");
        jsoupElement.attr("class", "my-class");
        Element w3cElement = w3cDoc.createElementNS(null, jsoupElement.tagName());
        w3cDom.copyAttributes(jsoupElement, w3cElement);
        assertEquals("my-class", w3cElement.getAttribute("class"));
    }

    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div");
        jsoupElement.attr("xmlns:ns1", "http://example.com");
        Element w3cElement = w3cDoc.createElementNS(null, jsoupElement.tagName());
        String prefix = w3cDom.updateNamespaces(jsoupElement);
        assertEquals("ns1", prefix);
        assertEquals("http://example.com", w3cElement.getAttribute("xmlns:ns1"));
    }

    @Test
    public void testAsString() {
        String result = w3cDom.asString(w3cDoc);
        assertNotNull(result);
    }
}