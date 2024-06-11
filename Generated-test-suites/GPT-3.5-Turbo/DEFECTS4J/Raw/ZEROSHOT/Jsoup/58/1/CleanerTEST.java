import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testClean() {
        // Arrange
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello, world!</p></body></html>");
        
        // Act
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        // Assert
        Assert.assertEquals("<html>\n" +
                " <head>\n" +
                "  <title>Test</title>\n" +
                " </head>\n" +
                " <body>\n" +
                "  <p>Hello, world!</p>\n" +
                " </body>\n" +
                "</html>", cleanDocument.outerHtml());
    }

    @Test
    public void testIsValid() {
        // Arrange
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello, world!</p></body></html>");
        
        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        // Assert
        Assert.assertTrue(isValid);
    }
}