import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CleanerTest {
    private Cleaner cleaner;
    private Document dirtyDocument;

    @Before
    public void setUp() {
        Whitelist whitelist = new Whitelist();
        cleaner = new Cleaner(whitelist);
        dirtyDocument = new Document("http://example.com");
    }

    @Test
    public void testClean() {
        // Arrange
        Element body = dirtyDocument.appendElement("body");
        body.appendElement("p").text("Hello, World!");

        // Act
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Assert
        assertNotNull(cleanDocument);
        assertEquals("http://example.com", cleanDocument.baseUri());
        assertEquals(1, cleanDocument.body().children().size());
        Element p = cleanDocument.body().child(0);
        assertEquals("p", p.tagName());
        assertEquals("Hello, World!", p.text());
    }

    @Test
    public void testIsValid_validDocumentWithSafeTagsAndAttributes() {
        // Arrange
        Element body = dirtyDocument.appendElement("body");
        body.appendElement("p").text("Hello, World!");

        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert
        assertTrue(isValid);
    }

    @Test
    public void testIsValid_invalidDocumentWithUnsafeTagsAndAttributes() {
        // Arrange
        Element body = dirtyDocument.appendElement("body");
        body.appendElement("script").text("alert('Hello, World!');");

        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert
        assertTrue(!isValid);
    }

    @Test
    public void testCopySafeNodes() {
        // Arrange
        Element source = dirtyDocument.appendElement("div");
        source.appendElement("p").text("Hello, World!");
        Element dest = dirtyDocument.appendElement("div");

        // Act
        int numDiscarded = cleaner.copySafeNodes(source, dest);

        // Assert
        assertEquals(0, numDiscarded);
        assertEquals(1, dest.children().size());
        Element p = dest.child(0);
        assertEquals("p", p.tagName());
        assertEquals("Hello, World!", p.text());
    }

    @Test
    public void testCreateSafeElement() {
        // Arrange
        Element sourceEl = new Element(Tag.valueOf("div"), "http://example.com");
        sourceEl.appendText("Hello, World!");

        // Act
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        // Assert
        assertNotNull(elementMeta.el);
        assertEquals("div", elementMeta.el.tagName());
        assertEquals("Hello, World!", elementMeta.el.text());
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}