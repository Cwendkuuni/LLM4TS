import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CleanerTest {
    private Cleaner cleaner;
    private Document dirtyDocument;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.basic());
        dirtyDocument = new Document("");
    }

    @Test
    public void testClean_NormalInput_CleanedDocument() {
        // Arrange
        Element dirtyBody = dirtyDocument.body();
        dirtyBody.append("<p>This is a paragraph.</p>");
        dirtyBody.append("<a href='http://example.com'>Link</a>");

        // Act
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Assert
        assertEquals("body", cleanDocument.body().tagName());
        assertTrue(cleanDocument.body().html().contains("<p>This is a paragraph.</p>"));
        assertTrue(cleanDocument.body().html().contains("<a href=\"http://example.com\">Link</a>"));
    }

    @Test
    public void testClean_EmptyInput_EmptyCleanedDocument() {
        // Act
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Assert
        assertEquals("body", cleanDocument.body().tagName());
        assertTrue(cleanDocument.body().html().isEmpty());
    }

    @Test
    public void testIsValid_ValidDocument_True() {
        // Arrange
        Element dirtyBody = dirtyDocument.body();
        dirtyBody.append("<p>This is a paragraph.</p>");

        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert
        assertTrue(isValid);
    }

    @Test
    public void testIsValid_InvalidDocument_False() {
        // Arrange
        Element dirtyBody = dirtyDocument.body();
        dirtyBody.append("<script>alert('XSS');</script>");

        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert
        assertTrue(!isValid);
    }

    @Test
    public void testCopySafeNodes_NormalInput_CleanedDocument() {
        // Arrange
        Element dirtyBody = dirtyDocument.body();
        dirtyBody.append("<p>This is a paragraph.</p>");
        dirtyBody.append("<a href='http://example.com'>Link</a>");

        // Act
        Document cleanDocument = Document.createShell("");
        Cleaner.ElementMeta meta = cleaner.createSafeElement(dirtyBody);

        int numDiscarded = cleaner.copySafeNodes(dirtyBody, cleanDocument.body());

        // Assert
        assertEquals(0, numDiscarded);
        assertEquals(2, cleanDocument.body().children().size());

        for (Element element : cleanDocument.body().children()) {
            assertTrue(element.tagName().equals("p") || element.tagName().equals("a"));
        }
    }

    @Test
    public void testCopySafeNodes_InvalidInput_CleanedDocumentWithDiscardedElements() {
        // Arrange
        Element dirtyBody = dirtyDocument.body();
        dirtyBody.append("<p>This is a paragraph.</p>");
        dirtyBody.append("<script>alert('XSS');</script>");

        // Act
        Document cleanDocument = Document.createShell("");
        Cleaner.ElementMeta meta = cleaner.createSafeElement(dirtyBody);

        int numDiscarded = cleaner.copySafeNodes(dirtyBody, cleanDocument.body());

        // Assert
        assertEquals(1, numDiscarded);
        assertEquals(1, cleanDocument.body().children().size());

        for (Element element : cleanDocument.body().children()) {
            assertTrue(element.tagName().equals("p"));
        }
    }

    @Test
    public void testCreateSafeElement_NormalInput_CleanedElement() {
        // Arrange
        Element dirtyElement = new Element("a");
        dirtyElement.attr("href", "http://example.com");
        dirtyElement.attr("onclick", "alert('XSS')");

        // Act
        Cleaner.ElementMeta meta = cleaner.createSafeElement(dirtyElement);

        // Assert
        assertEquals("a", meta.el.tagName());
        assertTrue(meta.el.attributes().size() == 1);
        assertTrue(meta.el.attributes().get("href").equals("http://example.com"));
        assertEquals(1, meta.numAttribsDiscarded);
    }
}