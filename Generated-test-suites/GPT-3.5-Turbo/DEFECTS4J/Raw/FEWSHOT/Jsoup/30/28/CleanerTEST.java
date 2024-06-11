package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    private Cleaner cleaner;
    
    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.basic());
    }
        
    @Test
    public void testClean() {
        Document dirtyDocument = createDirtyDocument();
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        // Assert that only allowed elements exist in the clean document
        assertFalse(cleanDocument.select("script").size() > 0);
        assertFalse(cleanDocument.select("style").size() > 0);
        assertFalse(cleanDocument.select("iframe").size() > 0);
        assertFalse(cleanDocument.select("link").size() > 0);
        assertFalse(cleanDocument.select("a").attr("href").contains("javascript"));
    }
    
    @Test
    public void testIsValid() {
        Document dirtyDocument = createDirtyDocument();
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        // Assert that the dirty document is not valid
        assertFalse(isValid);
    }
    
    private Document createDirtyDocument() {
        String html = "<html>\n" +
                "<head>\n" +
                "<script>alert('XSS Attack');</script>\n" +
                "<style>body {background-color: red;}</style>\n" +
                "<title>Dirty Document</title>\n" +
                "<link rel=\"stylesheet\" href=\"http://example.com/styles.css\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Dirty Document</h1>\n" +
                "<iframe src=\"http://example.com/popup.html\"></iframe>\n" +
                "<a href=\"javascript:alert('XSS Attack');\">Click me</a>\n" +
                "</body>\n" +
                "</html>";
        
        return Jsoup.parse(html);
    }
}