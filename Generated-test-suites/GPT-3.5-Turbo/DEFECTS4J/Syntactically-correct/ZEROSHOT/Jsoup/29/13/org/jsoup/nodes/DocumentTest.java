import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {
    private Document document;

    @Before
    public void setUp() {
        document = new Document("http://example.com");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");
        assertNotNull(shell);
        assertNotNull(shell.head());
        assertNotNull(shell.body());
    }
    
    @Test
    public void testHead() {
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("", title);

        String newTitle = "Test Title";
        document.title(newTitle);
        assertEquals(newTitle, document.title());

        document.title(null);
        assertEquals("", document.title());
    }

    @Test
    public void testCreateTime() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
    }

    @Test
    public void testNormalise() {
        Document normalised = document.normalise();
        assertNotNull(normalised);
        assertNotNull(normalised.getElementsByTag("html").first());
        assertNotNull(normalised.head());
        assertNotNull(normalised.body());
    }

    @Test
    public void testText() {
        String text = "Test Text";
        document.text(text);
        assertEquals(text, document.body().text());
    }

    @Test
    public void testOuterHtml() {
        assertEquals(document.html(), document.outerHtml());
    }

    @Test
    public void testClone() {
        Document clone = document.clone();
        assertNotNull(clone);
        assertNotSame(document, clone);
        assertEquals(document.outputSettings(), clone.outputSettings());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
    }
}