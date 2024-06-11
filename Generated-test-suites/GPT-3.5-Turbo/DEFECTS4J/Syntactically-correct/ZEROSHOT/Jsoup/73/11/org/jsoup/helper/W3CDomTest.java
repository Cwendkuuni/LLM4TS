import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import static org.junit.Assert.*;

public class W3CDomTest {

    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDoc;
    private org.w3c.dom.Document w3cDoc;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
        jsoupDoc = Jsoup.parse("<html><head></head><body><div id=\"test\"></div></body></html>");
        w3cDoc = w3cDom.fromJsoup(jsoupDoc);
    }

    @Test
    public void testFromJsoup() {
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        NodeList nodeList = w3cDoc.getElementsByTagName("div");
        assertEquals(1, nodeList.getLength());
        assertEquals(Element.class, w3cBuilder.dest.getClass());
    }

    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Element sourceEl = jsoupDoc.selectFirst("div");
        Element el = w3cDoc.createElementNS("", sourceEl.tagName());
        w3cDom.convert(jsoupDoc, w3cDoc);
        w3cDom.copyAttributes(sourceEl, el);
        assertEquals("test", el.getAttribute("id"));
    }

    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element el = jsoupDoc.selectFirst("div");
        assertEquals("", w3cDom.updateNamespaces(el));
    }

    @Test
    public void testAsString() {
        String str = w3cDom.asString(w3cDoc);
        assertNotNull(str);
        assertTrue(str.contains("<html"));
    }
}