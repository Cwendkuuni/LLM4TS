import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.createShell("http://example.com");
        Element body = dirtyDocument.body();
        Element div = body.appendElement("div");
        div.text("Hello, World!");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("clean method should remove all elements except body", 1, cleanDocument.body().children().size());
        assertEquals("clean method should maintain text content", "Hello, World!", cleanDocument.body().text());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document validDocument = Document.createShell("http://example.com");
        Element body = validDocument.body();
        body.appendElement("p");

        assertTrue("isValid method should return true for valid document", cleaner.isValid(validDocument));

        Document invalidDocument = Document.createShell("http://example.com");
        Element head = invalidDocument.head();
        head.appendElement("title");

        assertFalse("isValid method should return false for invalid document", cleaner.isValid(invalidDocument));
    }

    // Include additional test cases for edge cases and error scenarios as needed

}