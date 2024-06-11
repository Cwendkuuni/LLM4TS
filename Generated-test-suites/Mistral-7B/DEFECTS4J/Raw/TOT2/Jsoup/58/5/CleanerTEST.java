import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextDocument;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

public class CleanerTest {

    @Test
    public void testCleanMethod() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = new TextDocument("Testing the cleanup method.");
        dirtyDocument.body().setText("This is <b>test</b> document.");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><head></head><body></body></html>", cleanDocument.html());
        assertEquals("This is test", cleanDocument.body().text());
    }
}