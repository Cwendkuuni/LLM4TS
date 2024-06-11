import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;
import java.util.List;

import static org.junit.Assert.*;

public class DocumentTest {

    Document document = new Document("http://example.com");

    @Test
    public void testCreateShell() {
        Document shellDoc = Document.createShell("http://example.com");
        assertNotNull(shellDoc);
        Element html = shellDoc.getElementsByTag("html").first();
        assertNotNull(html);
        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);
        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNull(head);
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNull(body);
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("", title);
    }

    @Test
    public void testSetTitle() {
        String title = "Test Title";
        document.title(title);
        assertEquals(title, document.title());
    }

    @Test
    public void testcreateElement() {
        Element element = document.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.tagName());
    }

    @Test
    public void testNormalise() {
        document.normalise();
        Element html = document.select("html").first();
        assertNotNull(html);
        Element head = document.head();
        assertNotNull(head);
        Element body = document.body();
        assertNotNull(body);
    }

    @Test
    public void testOuterHtml() {
        assertEquals(document.html(), document.outerHtml());
    }

    @Test
    public void testText() {
        String text = "Test Text";
        document.text(text);
        assertEquals(text, document.body().text());
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }
}