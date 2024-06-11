import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
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

import static org.junit.Assert.*;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.relaxed());
    }

    @Test
    public void testClean() {
        String dirtyHtml = "<html><head><title>Test</title></head><body><h1>Hello</h1><script>alert('XSS')</script></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        Element body = cleanDocument.body();
        assertEquals(1, body.children().size());
        assertEquals("h1", body.child(0).tagName());
        assertNull(cleanDocument.select("script").first());
    }

    @Test
    public void testIsValid() {
        String validHtml = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        Document validDocument = Jsoup.parse(validHtml);
        
        boolean isValid = cleaner.isValid(validDocument);
        
        assertTrue(isValid);
    }
}