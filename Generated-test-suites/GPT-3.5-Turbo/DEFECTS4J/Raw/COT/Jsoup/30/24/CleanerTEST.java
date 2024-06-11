
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testClean() {
        // Arrange
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());
        Document dirtyDocument = new Document("");
        dirtyDocument.body().appendElement("div").text("Hello, world!");
        
        // Act
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        // Assert
        Assert.assertEquals("<html><head></head><body><div>Hello, world!</div></body></html>", cleanDocument.outerHtml());
    }

    @Test
    public void testIsValid() {
        // Arrange
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());
        Document dirtyDocument = new Document("");
        dirtyDocument.body().appendElement("div").text("Hello, world!");
        
        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        // Assert
        Assert.assertTrue(isValid);
    }
}
