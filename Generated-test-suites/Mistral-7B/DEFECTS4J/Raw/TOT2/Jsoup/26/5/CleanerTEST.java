import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import static org.jsoup.node.Jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testCleanerWithValidWhitelist() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<p>This is a valid HTML</p>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<p></p>", cleanDocument.html());
    }

    @Test
    public void testIsValidWithValidDocument() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<p>This is a valid HTML</p>");
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodesWithValidSourceAndDestination() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><p><span>This is a valid HTML</span></p></body></html>");
        Element source = dirtyDocument.select("body > p > span").first();
        Element dest = new Element(Tag.valueOf("div"));
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(0, numDiscarded);
    }
}