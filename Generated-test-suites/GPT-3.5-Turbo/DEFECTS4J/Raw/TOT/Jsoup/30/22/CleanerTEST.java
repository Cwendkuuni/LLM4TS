
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean_returnsCleanedDocument() {
        // Arrange
        Document dirtyDocument = Jsoup.parseBodyFragment("<p>Dirty <b>HTML</b></p>");

        // Act
        Document cleanedDocument = cleaner.clean(dirtyDocument);

        // Assert
        Assert.assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <p>Dirty <b>HTML</b></p>\n" +
                " </body>\n" +
                "</html>", cleanedDocument.html());
    }

    @Test
    public void testIsValid_returnsTrueForValidDocument() {
        // Arrange
        Document validDocument = Jsoup.parseBodyFragment("<p>Valid HTML</p>");

        // Act
        boolean isValid = cleaner.isValid(validDocument);

        // Assert
        Assert.assertTrue(isValid);
    }

    @Test
    public void testIsValid_returnsFalseForInvalidDocument() {
        // Arrange
        Document invalidDocument = Jsoup.parseBodyFragment("<p class=\"evil\" onclick=\"alert('xss')\">Invalid HTML</p>");

        // Act
        boolean isValid = cleaner.isValid(invalidDocument);

        // Assert
        Assert.assertFalse(isValid);
    }
}
