import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CleanerTest {
    private Cleaner cleaner;
    
    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = Parser.parseBodyFragment("<p>Hello, <script>alert('XSS');</script>World!</p>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        String expectedOutput = "<html>\n <head></head>\n <body>\n  <p>Hello, World!</p>\n </body>\n</html>";
        String actualOutput = cleanDocument.outerHtml();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testIsValid_ValidDocument() {
        Document validDocument = Parser.parseBodyFragment("<div><p>Hello</p></div>");
        boolean isValid = cleaner.isValid(validDocument);

        assertTrue(isValid);
    }

    @Test
    public void testIsValid_InvalidDocument() {
        Document invalidDocument = Parser.parseBodyFragment("<span><a href=\"javascript:alert('XSS')\">Link</a></span>");
        boolean isValid = cleaner.isValid(invalidDocument);

        assertFalse(isValid);
    }
    
    @Test
    public void testCopySafeNodes() {
        // Add test cases for copySafeNodes method
    }
    
    @Test
    public void testCreateSafeElement() {
        // Add test cases for createSafeElement method
    }
}