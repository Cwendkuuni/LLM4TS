import org.jsoup.nodes.*;
import org.jsoup.parser.Jsoup;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setup() {
        Whitelist whitelist = Whitelist.basic(); // Using basic whitelist for testing
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        // Typical use case - Ensuring only allowed tags and attributes are present
        Document dirtyDocument = Jsoup.parse("<html><body><ul><li>Item 1</li><li>Item 2</li></ul></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element body = cleanDocument.body();
        Assert.assertEquals("ul", body.child(0).tagName());
        Assert.assertEquals("li", body.child(0).child(0).tagName());
        Assert.assertEquals("Item 1", body.child(0).child(0).text());
        Assert.assertEquals("li", body.child(0).child(1).tagName());
        Assert.assertEquals("Item 2", body.child(0).child(1).text());

        // Edge case - Testing with empty body
        Document emptyDocument = Jsoup.parse("<html><body></body></html>");
        Document cleanEmptyDoc = cleaner.clean(emptyDocument);
        Assert.assertEquals("", cleanEmptyDoc.body().html());

        // Error scenario - Testing with frameset document
        Document framesetDocument = Jsoup.parse("<html><frameset><frame src='frame.html'></frameset></html>");
        Document cleanFramesetDoc = cleaner.clean(framesetDocument);
        Assert.assertEquals("", cleanFramesetDoc.body().html());
    }

    @Test
    public void testIsValid() {
        // Typical use case - Valid document with allowed tags and attributes
        Document validDocument = Jsoup.parse("<html><body><p>This is a paragraph</p></body></html>");
        boolean isValid = cleaner.isValid(validDocument);
        Assert.assertTrue(isValid);

        // Edge case - Document with invalid tag and attribute
        Document invalidDocument = Jsoup.parse("<html><body><p onclick='alert(\"Hello World\")'>This is a paragraph</p></body></html>");
        boolean isInvalid = cleaner.isValid(invalidDocument);
        Assert.assertFalse(isInvalid);

        // Error scenario - Document with content in head
        Document headContentDocument = Jsoup.parse("<html><head><title>Test</title></head><body><p>This is a paragraph</p></body></html>");
        boolean hasHeadContent = cleaner.isValid(headContentDocument);
        Assert.assertFalse(hasHeadContent);
    }
}