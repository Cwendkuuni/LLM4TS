package org.jsoup.nodes;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {
    private Document document;

    @Before
    public void setup() {
        document = new Document("http://example.com");
    }

    @Test
    public void testHead() {
        assertNotNull(document.head());
    }

    @Test
    public void testBody() {
        assertNotNull(document.body());
    }

    @Test
    public void testTitle() {
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testTitleNoElement() {
        assertEquals("", document.title());
    }

    @Test
    public void testTitleNull() {
        try {
            document.title(null);
            fail("Should throw exception for null title");
        } catch (IllegalArgumentException e) {
            // expected behavior
        }
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertEquals("div", element.tagName());
    }

    @Test
    public void testNormalise() {
        document.normalise();
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testOuterHtml() {
        document.text("Test Text");
        assertEquals("Test Text", document.outerHtml());
    }

    @Test
    public void testText() {
        document.text("Test Text");
        assertEquals("Test Text", document.text());
    }

    @Test
    public void testOutputSettings() {
        assertNotNull(document.outputSettings());
    }
}