import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        //test case 1
        Whitelist whitelist1 = Whitelist.basic();
        Cleaner cleaner1 = new Cleaner(whitelist1);
        Document dirtyDocument1 = new Document("<html><head></head><body><p>Hello, world!</p></body></html>");
        Document cleanDocument1 = cleaner1.clean(dirtyDocument1);
        String expected1 = "<html>\n" +
                           " <head></head>\n" +
                           " <body>\n" +
                           "  <p>Hello, world!</p>\n" +
                           " </body>\n" +
                           "</html>\n";
        assertEquals(expected1, cleanDocument1.toString());

        //test case 2
        Whitelist whitelist2 = Whitelist.relaxed();
        Cleaner cleaner2 = new Cleaner(whitelist2);
        Document dirtyDocument2 = new Document("<html><head></head><body><script>alert('XSS');</script><p>Hello, world!</p></body></html>");
        Document cleanDocument2 = cleaner2.clean(dirtyDocument2);
        String expected2 = "<html>\n" +
                           " <head></head>\n" +
                           " <body>\n" +
                           "  <p>Hello, world!</p>\n" +
                           " </body>\n" +
                           "</html>\n";
        assertEquals(expected2, cleanDocument2.toString());
    }

    @Test
    public void testIsValid() {
        //test case 1
        Whitelist whitelist1 = Whitelist.basic();
        Cleaner cleaner1 = new Cleaner(whitelist1);
        Document dirtyDocument1 = new Document("<html><head></head><body><p>Hello, world!</p></body></html>");
        assertTrue(cleaner1.isValid(dirtyDocument1));

        //test case 2
        Whitelist whitelist2 = Whitelist.relaxed();
        Cleaner cleaner2 = new Cleaner(whitelist2);
        Document dirtyDocument2 = new Document("<html><head></head><body><script>alert('XSS');</script><p>Hello, world!</p></body></html>");
        assertFalse(cleaner2.isValid(dirtyDocument2));
    }

    @Test
    public void testCopySafeNodes() {
        //test case 1
        Whitelist whitelist1 = Whitelist.basic();
        Cleaner cleaner1 = new Cleaner(whitelist1);
        Element source1 = new Element(Tag.valueOf("div"), "");
        Element dest1 = new Element(Tag.valueOf("div"), "");
        int numDiscarded1 = cleaner1.copySafeNodes(source1, dest1);
        assertEquals(0, numDiscarded1);

        //test case 2
        Whitelist whitelist2 = Whitelist.relaxed();
        Cleaner cleaner2 = new Cleaner(whitelist2);
        Element source2 = new Element(Tag.valueOf("div"), "");
        Element dest2 = new Element(Tag.valueOf("div"), "");
        int numDiscarded2 = cleaner2.copySafeNodes(source2, dest2);
        assertEquals(0, numDiscarded2);
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Element source = new Element(Tag.valueOf("div"), "");
        ElementMeta elementMeta = cleaner.createSafeElement(source);
        assertEquals(source, elementMeta.el);
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}