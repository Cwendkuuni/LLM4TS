import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

public class CleanerTest {

    private Cleaner cleaner;
    private Whitelist whitelist;

    @Before
    public void setup() {
        whitelist = Whitelist.basic();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean_ValidDocument_ReturnsCleanedDocument() {
        // Arrange
        String dirtyHtml = "<html><head><title>Dirty Document</title></head><body><div>Hello World!</div></body></html>";
        Document dirtyDocument = Parser.parse(bodyHtml, "");

        // Act
        Document cleanedDocument = cleaner.clean(dirtyDocument);

        // Assert
        Assert.assertEquals("<html><head></head><body><div>Hello World!</div></body></html>", cleanedDocument.html());
    }

    @Test
    public void testClean_DirtyDocumentWithUnsafeTags_TagsAreRemoved() {
        // Arrange
        String dirtyHtml = "<html><head><title>Dirty Document</title></head><body><script>alert('Unsafe!');</script><p>Hello World!</p></body></html>";
        Document dirtyDocument = Parser.parse(dirtyHtml, "");

        // Act
        Document cleanedDocument = cleaner.clean(dirtyDocument);

        // Assert
        Assert.assertEquals("<html><head></head><body><p>Hello World!</p></body></html>", cleanedDocument.html());
    }

    @Test
    public void testIsValid_ValidDocument_ReturnsTrue() {
        // Arrange
        String dirtyHtml = "<html><head><title>Dirty Document</title></head><body><div>Hello World!</div></body></html>";
        Document dirtyDocument = Parser.parse(dirtyHtml, "");

        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert
        Assert.assertTrue(isValid);
    }

    @Test
    public void testIsValid_DirtyDocumentWithUnsafeTags_ReturnsFalse() {
        // Arrange
        String dirtyHtml = "<html><head><title>Dirty Document</title></head><body><script>alert('Unsafe!');</script><p>Hello World!</p></body></html>";
        Document dirtyDocument = Parser.parse(dirtyHtml, "");

        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert
        Assert.assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes_DirtyDocumentWithSafeAndUnsafeTags_ReturnsCleanedDocument() {
        // Arrange
        String dirtyHtml = "<html><head><title>Dirty Document</title></head><body><script>alert('Unsafe!');</script><p>Hello World!</p></body></html>";
        Document dirtyDocument = Parser.parse(dirtyHtml, "");
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        // Assert
        Assert.assertEquals("<body><p>Hello World!</p></body>", cleanDocument.body().html());
    }

}