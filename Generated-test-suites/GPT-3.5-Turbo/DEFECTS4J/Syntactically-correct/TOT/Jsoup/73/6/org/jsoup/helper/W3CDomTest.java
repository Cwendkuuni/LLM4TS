import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import static org.junit.Assert.*;

public class W3CDomTest {

    private W3CDom w3CDom;

    @Before
    public void setUp() {
        w3CDom = new W3CDom();
    }

    @Test
    public void testFromJsoupNotNull() {
        org.jsoup.nodes.Document jsoupDoc = Parser.parse("<html><head></head><body></body></html>", "https://example.com/");
        assertNotNull(w3CDom.fromJsoup(jsoupDoc));
    }

    @Test
    public void testFromJsoupConversion() {
        org.jsoup.nodes.Document jsoupDoc = Parser.parse("<html><head></head><body></body></html>", "https://example.com/");
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);

        Element rootElement = w3cDoc.getDocumentElement();
        assertNotNull(rootElement);
        assertEquals("html", rootElement.getTagName());
    }

    @Test(expected = IllegalStateException.class)
    public void testFromJsoupInvalidDocument() {
        w3CDom.fromJsoup(null);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = Parser.parse("<div><p>Hello World!</p></div>", "https://example.com/");
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);

        org.w3c.dom.Element rootElement = w3cDoc.getDocumentElement();
        assertNotNull(rootElement);
        assertEquals("div", rootElement.getTagName());

        org.w3c.dom.Element childElement = (org.w3c.dom.Element) rootElement.getFirstChild();
        assertNotNull(childElement);
        assertEquals("p", childElement.getTagName());

        org.w3c.dom.Text text = (org.w3c.dom.Text) childElement.getFirstChild();
        assertNotNull(text);
        assertEquals("Hello World!", text.getWholeText());
    }

    @Test
    public void testConvertWithBlankLocation() {
        org.jsoup.nodes.Document jsoupDoc = Parser.parse("<html><head></head><body></body></html>");
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);

        assertEquals("", w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvertWithLocation() {
        org.jsoup.nodes.Document jsoupDoc = Parser.parse("<html><head></head><body></body></html>", "https://example.com/");
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);

        assertEquals("https://example.com/", w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvertWithNamespace() {
        org.jsoup.nodes.Document jsoupDoc = Parser.parse("<h:table xmlns:h=\"https://example.com/\"><h:tr><h:td>Apples</h:td></h:tr></h:table>", "https://example.com/");
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);

        org.w3c.dom.Element rootElement = w3cDoc.getDocumentElement();
        assertNotNull(rootElement);
        assertEquals("table", rootElement.getTagName());
        assertEquals("https://example.com/", rootElement.getNamespaceURI());

        org.w3c.dom.Element childElement = (org.w3c.dom.Element) rootElement.getFirstChild();
        assertNotNull(childElement);
        assertEquals("tr", childElement.getTagName());
        assertEquals("https://example.com/", childElement.getNamespaceURI());

        org.w3c.dom.Element grandChildElement = (org.w3c.dom.Element) childElement.getFirstChild();
        assertNotNull(grandChildElement);
        assertEquals("td", grandChildElement.getTagName());
        assertEquals("https://example.com/", grandChildElement.getNamespaceURI());

        org.w3c.dom.Text text = (org.w3c.dom.Text) grandChildElement.getFirstChild();
        assertNotNull(text);
        assertEquals("Apples", text.getWholeText());
    }

    @Test
    public void testConvertWithAttribute() {
        org.jsoup.nodes.Document jsoupDoc = Parser.parse("<div id=\"container\" class=\"main\"><p>Hello World!</p></div>", "https://example.com/");
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);

        org.w3c.dom.Element rootElement = w3cDoc.getDocumentElement();
        assertNotNull(rootElement);
        assertEquals("div", rootElement.getTagName());
        assertEquals("container", rootElement.getAttribute("id"));
        assertEquals("main", rootElement.getAttribute("class"));
    }

    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDoc = Parser.parse("<html><head></head><body></body></html>", "https://example.com/");
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);

        String str = w3CDom.asString(w3cDoc);
        assertNotNull(str);
        assertTrue(str.contains("<html>"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAsStringInvalidDocument() {
        w3CDom.asString(null);
    }

}