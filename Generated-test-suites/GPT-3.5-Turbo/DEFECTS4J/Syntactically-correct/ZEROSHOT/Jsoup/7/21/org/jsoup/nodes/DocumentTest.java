import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testConstructor() {
        Document document = new Document("baseUri");
        assertNotNull(document);
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("baseUri");
        assertNotNull(document);
        Element html = document.getElementsByTag("html").first();
        Element head = document.getElementsByTag("head").first();
        Element body = document.getElementsByTag("body").first();
        assertNotNull(html);
        assertNotNull(head);
        assertNotNull(body);
    }

    @Test
    public void testHead() {
        Document document = new Document("baseUri");
        Element head = document.head();
        assertNull(head);
        Element html = document.appendElement("html");
        head = document.head();
        assertNull(head);
        Element newHead = html.appendElement("head");
        assertNotNull(newHead);
        head = document.head();
        assertNotNull(head);
        assertEquals(newHead, head);
    }

    @Test
    public void testBody() {
        Document document = new Document("baseUri");
        Element body = document.body();
        assertNull(body);
        Element html = document.appendElement("html");
        body = document.body();
        assertNull(body);
        Element newBody = html.appendElement("body");
        assertNotNull(newBody);
        body = document.body();
        assertNotNull(body);
        assertEquals(newBody, body);
    }

    @Test
    public void testTitle() {
        Document document = new Document("baseUri");
        String title = document.title();
        assertEquals("", title);
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element titleElement = head.appendElement("title");
        titleElement.text("Test Title");
        title = document.title();
        assertEquals("Test Title", title);
        document.title("New Title");
        title = document.title();
        assertEquals("New Title", title);
    }

    @Test
    public void testNormalise() {
        Document document = new Document("baseUri");
        document.normalise();
        assertNotNull(document.getElementsByTag("html").first());
        assertNotNull(document.getElementsByTag("head").first());
        assertNotNull(document.getElementsByTag("body").first());
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("baseUri");
        String outerHtml = document.outerHtml();
        assertEquals(document.html(), outerHtml);
    }

    @Test
    public void testText() {
        Document document = new Document("baseUri");
        document.text("Test Text");
        assertEquals("Test Text", document.body().text());
    }

    @Test
    public void testOutputSettings() {
        Document document = new Document("baseUri");
        Document.OutputSettings outputSettings = document.outputSettings();

        Entities.EscapeMode escapeMode = outputSettings.escapeMode();
        assertNotNull(escapeMode);
        assertEquals(Entities.EscapeMode.base, escapeMode);

        Charset charset = outputSettings.charset();
        assertNotNull(charset);
        assertEquals(Charset.forName("UTF-8"), charset);

        boolean prettyPrint = outputSettings.prettyPrint();
        assertTrue(prettyPrint);

        int indentAmount = outputSettings.indentAmount();
        assertEquals(1, indentAmount);
    }
}