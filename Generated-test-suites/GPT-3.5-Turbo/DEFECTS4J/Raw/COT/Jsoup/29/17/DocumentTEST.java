import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocument() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        
        assertEquals(document.nodeName(), "#root");
        assertEquals(document.baseUri(), baseUri);
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        
        assertEquals(document.nodeName(), "#root");
        assertEquals(document.baseUri(), baseUri);
        
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testHead() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);

        assertNull(document.head());

        document.appendElement("head");

        assertNotNull(document.head());
    }

    @Test
    public void testBody() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);

        assertNull(document.body());

        document.appendElement("body");

        assertNotNull(document.body());
    }

    @Test
    public void testTitle() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);

        assertEquals(document.title(), "");

        document.title("Test Title");

        assertEquals(document.title(), "Test Title");
    }

    @Test
    public void testElement() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);

        Element element = document.createElement("div");

        assertEquals(element.tagName(), "div");
        assertEquals(element.baseUri(), baseUri);
    }

    @Test
    public void testNormalise() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);

        Element html = document.appendElement("html");
        html.appendElement("head");
        html.appendElement("body");

        document.normalise();

        assertNotNull(document.head());
        assertNotNull(document.body());

        assertEquals(document.head().parentNode(), html);
        assertEquals(document.body().parentNode(), html);
    }

    @Test
    public void testText() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);

        assertEquals(document.text(""), document);

        Element body = document.appendElement("body");
        body.appendText("Test Text");

        assertEquals(document.text(), "Test Text");
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);

        Element body = document.appendElement("body");
        body.appendElement("div").text("Test Div");

        assertEquals(document.outerHtml(), "<body><div>Test Div</div></body>");
    }

    @Test
    public void testClone() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);

        Document clone = document.clone();

        assertEquals(clone.baseUri(), baseUri);
        assertEquals(clone.outputSettings(), document.outputSettings());
    }

    @Test
    public void testOutputSettings() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        document.outputSettings(outputSettings);

        assertEquals(document.outputSettings(), outputSettings);
    }

    @Test
    public void testQuirksMode() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);

        document.quirksMode(Document.QuirksMode.quirks);
        
        assertEquals(document.quirksMode(), Document.QuirksMode.quirks);
    }
}