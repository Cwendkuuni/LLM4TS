import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTest {
    private Document document;

    @Before
    public void setup() {
        document = Document.createShell("http://example.com");
    }

    @Test
    public void testCreateShell() {
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("html", document.nodeName());
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        assertEquals("", document.title());
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testNormalise() {
        document.normalise();
        // Add test cases for normalise method
    }

    @Test
    public void testOuterHtml() {
        assertEquals("", document.outerHtml());
    }

    @Test
    public void testText() {
        document.text("Example Text");
        // Add test cases for text method
    }

    @Test
    public void testEscapeMode() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
    }

    @Test
    public void testCharset() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
    }

    @Test
    public void testPrettyPrint() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertEquals(true, outputSettings.prettyPrint());
    }

    @Test
    public void testIndentAmount() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertEquals(1, outputSettings.indentAmount());
    }
}