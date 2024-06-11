
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {
    private Document document;

    @Before
    public void setUp() {
        document = Jsoup.parse("<html><head><title>Test Document</title></head><body><p>Hello, World!</p></body></html>");
    }

    @After
    public void tearDown() {
        document = null;
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
        String title = document.title();
        assertNotNull(title);
        assertEquals("Test Document", title);
    }

    @Test
    public void testSetTitle() {
        document.title("New Title");
        assertEquals("New Title", document.title());
    }

    @Test
    public void testCreateElement() {
        Element p = document.createElement("p");
        assertNotNull(p);
        assertEquals("p", p.nodeName());
    }

    @Test
    public void testNormalise() {
        document.normalise();
        assertEquals("<html><head><title>Test Document</title></head><body><p>Hello, World!</p></body></html>", document.outerHtml());
    }

    @Test
    public void testText() {
        document.text("New Text");
        assertEquals("New Text", document.body().text());
    }
}
