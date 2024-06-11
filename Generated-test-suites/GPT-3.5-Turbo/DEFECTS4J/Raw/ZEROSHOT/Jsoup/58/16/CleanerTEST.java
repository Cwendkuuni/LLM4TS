import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {

    private Cleaner cleaner;
    private Document document;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.relaxed());
        document = Parser.parseBodyFragment("<div><p>Hello, World!</p></div>");
    }

    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(document);
        assertNotNull(cleanDocument);
        assertEquals("<html>\n <head></head>\n <body>\n  <div>\n   <p>Hello, World!</p>\n  </div>\n </body>\n</html>\n", cleanDocument.toString());
    }

    @Test
    public void testIsValid() {
        assertTrue(cleaner.isValid(document));
    }
}