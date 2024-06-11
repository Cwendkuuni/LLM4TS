import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic()); // Using basic whitelist for testing
    }

    // Test clean method with a valid document
    @Test
    public void testClean_ValidDocument_ReturnsCleanedDocument() {
        // Arrange
        String dirtyHtml = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        // Act
        Document cleanedDocument = cleaner.clean(dirtyDocument);

        // Assert
        assertEquals("<html>\n"
                + " <head></head>\n"
                + " <body>\n"
                + "  <h1>Hello World!</h1>\n"
                + " </body>\n"
                + "</html>", cleanedDocument.html());
    }

    // Test clean method with a document containing unsafe tags and attributes
    @Test
    public void testClean_UnsafeTagsAndAttributes_ReturnsCleanedDocumentWithoutUnsafeTagsAndAttributes() {
        // Arrange
        String dirtyHtml = "<html><head><title>Test</title></head><body><script>alert('Hello');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        // Act
        Document cleanedDocument = cleaner.clean(dirtyDocument);

        // Assert
        assertEquals("<html>\n"
                + " <head></head>\n"
                + " <body></body>\n"
                + "</html>", cleanedDocument.html());
    }

    // Test isValid method with a valid document
    @Test
    public void testIsValid_ValidDocument_ReturnsTrue() {
        // Arrange
        String dirtyHtml = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert
        assertTrue(isValid);
    }

    // Test isValid method with an invalid document containing unsafe tags
    @Test
    public void testIsValid_UnsafeTags_ReturnsFalse() {
        // Arrange
        String dirtyHtml = "<html><head><title>Test</title></head><body><script>alert('Hello');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert
        assertFalse(isValid);
    }

    // Test copySafeNodes method with a source element containing safe and unsafe nodes
    @Test
    public void testCopySafeNodes_SourceElementWithSafeAndUnsafeNodes_ReturnsNumDiscardedNodes() {
        // Arrange
        String dirtyHtml = "<div><p>Safe Node</p><script>Unsafe Node</script></div>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(dirtyDocument.body(), dirtyDocument.body());

        // Act
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), dirtyDocument.body());

        // Assert
        assertEquals(1, numDiscarded);
    }

    // Test createSafeElement method with a source element containing safe and unsafe attributes
    @Test
    public void testCreateSafeElement_SourceElementWithSafeAndUnsafeAttributes_ReturnsElementWithSafeAttributes() {
        // Arrange
        String sourceTag = "a";
        String unsafeAttribute = "onclick";
        String safeAttribute = "href";
        Cleaner.ElementMeta elementMeta;

        // Act
        Element sourceElement = new Element(Tag.valueOf(sourceTag), "");
        sourceElement.attr(unsafeAttribute, "alert('Hello');");
        sourceElement.attr(safeAttribute, "https://www.example.com");
        elementMeta = cleaner.createSafeElement(sourceElement);

        // Assert
        assertEquals(1, elementMeta.numAttribsDiscarded);
        assertEquals(1, elementMeta.el.attributes().size());
        assertTrue(elementMeta.el.hasAttr(safeAttribute));
        assertFalse(elementMeta.el.hasAttr(unsafeAttribute));
    }

    // Test createSafeElement method with a source element containing enforced attributes
    @Test
    public void testCreateSafeElement_SourceElementWithEnforcedAttributes_ReturnsElementWithEnforcedAttributes() {
        // Arrange
        String sourceTag = "a";
        String enforcedAttribute = "target";
        Cleaner.ElementMeta elementMeta;

        // Act
        Element sourceElement = new Element(Tag.valueOf(sourceTag), "");
        sourceElement.attr(enforcedAttribute, "_blank");
        elementMeta = cleaner.createSafeElement(sourceElement);

        // Assert
        assertEquals(0, elementMeta.numAttribsDiscarded);
        assertEquals(1, elementMeta.el.attributes().size());
        assertTrue(elementMeta.el.hasAttr(enforcedAttribute));
        assertEquals("_blank", elementMeta.el.attr(enforcedAttribute));
    }
}