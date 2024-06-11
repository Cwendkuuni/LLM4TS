import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean_typicalCase() {
        // Arrange
        String dirtyHtml = "<html><head></head><body><div id='content'>Hello World!</div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Act
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Assert
        Element cleanBody = cleanDocument.body();
        assertNotNull(cleanBody);
        assertEquals(1, cleanBody.children().size());
        Element contentDiv = cleanBody.getElementById("content");
        assertNotNull(contentDiv);
        assertEquals("Hello World!", contentDiv.text());
    }

    @Test
    public void testClean_emptyDocument() {
        // Arrange
        Document dirtyDocument = new Document("");

        // Act
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Assert
        Element cleanBody = cleanDocument.body();
        assertNotNull(cleanBody);
        assertEquals(0, cleanBody.children().size());
    }

    @Test
    public void testClean_nestedTags() {
        // Arrange
        String dirtyHtml = "<html><head></head><body><div><p><span>Test</span></p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Act
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Assert
        Element cleanBody = cleanDocument.body();
        assertNotNull(cleanBody);
        assertEquals(1, cleanBody.children().size());
        Element div = cleanBody.getElementsByTag("div").first();
        assertNotNull(div);
        assertEquals(1, div.children().size());
        Element p = div.getElementsByTag("p").first();
        assertNotNull(p);
        assertEquals(1, p.children().size());
        Element span = p.getElementsByTag("span").first();
        assertNotNull(span);
        assertEquals("Test", span.text());
    }

    @Test
    public void testIsValid_ValidDocument() {
        // Arrange
        String dirtyHtml = "<html><head></head><body><div id='content'>Hello World!</div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert
        assertTrue(isValid);
    }

    @Test
    public void testIsValid_InvalidDocument() {
        // Arrange
        String dirtyHtml = "<html><head></head><body><div><script>alert('XSS Attack!');</script></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert
        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes_typicalCase() {
        // Arrange
        String sourceHtml = "<html><head></head><body><div id='content'>Hello World!</div></body></html>";
        Document sourceDocument = Jsoup.parse(sourceHtml);
        Element sourceBody = sourceDocument.body();
        Element destBody = new Element(Tag.valueOf("body"), "");
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Act
        int numDiscarded = cleaner.copySafeNodes(sourceBody, destBody);

        // Assert
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCopySafeNodes_nestedTags() {
        // Arrange
        String sourceHtml = "<html><head></head><body><div><p><span>Test</span></p></div></body></html>";
        Document sourceDocument = Jsoup.parse(sourceHtml);
        Element sourceBody = sourceDocument.body();
        Element destBody = new Element(Tag.valueOf("body"), "");
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Act
        int numDiscarded = cleaner.copySafeNodes(sourceBody, destBody);

        // Assert
        assertEquals(0, numDiscarded);
        assertEquals(1, destBody.children().size());
        Element div = destBody.getElementsByTag("div").first();
        assertNotNull(div);
        assertEquals(1, div.children().size());
        Element p = div.getElementsByTag("p").first();
        assertNotNull(p);
        assertEquals(1, p.children().size());
        Element span = p.getElementsByTag("span").first();
        assertNotNull(span);
        assertEquals("Test", span.text());
    }

    @Test
    public void testCreateSafeElement_typicalCase() {
        // Arrange
        Element sourceElement = new Element(Tag.valueOf("div"), "");
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Act
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        // Assert
        assertNotNull(elementMeta);
        assertNotNull(elementMeta.el);
        assertEquals("div", elementMeta.el.tagName());
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElement_discardedAttributes() {
        // Arrange
        Element sourceElement = new Element(Tag.valueOf("div"), "");
        sourceElement.attr("class", "red");
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Act
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        // Assert
        assertNotNull(elementMeta);
        assertNotNull(elementMeta.el);
        assertEquals("div", elementMeta.el.tagName());
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }

}