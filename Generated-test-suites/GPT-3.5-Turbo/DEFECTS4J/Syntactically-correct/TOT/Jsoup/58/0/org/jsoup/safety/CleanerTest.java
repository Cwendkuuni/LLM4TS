import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class CleanerTest {

    private static Cleaner cleaner;

    @BeforeClass
    public static void setUp() {
        cleaner = new Cleaner(new Whitelist());
    }

    @Test
    public void testClean() {
        // Typical use case
        Document dirtyDocument = new Document("");
        dirtyDocument.appendElement("html")
                .appendElement("head")
                .appendElement("title").text("Title")
                .parent()
                .parent()
                .appendElement("body")
                .appendElement("h1").text("Header 1")
                .parent();
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element bodyElement = cleanDocument.body();
        Assert.assertEquals(1, bodyElement.childNodeSize());
        Element h1Element = bodyElement.child(0);
        Assert.assertEquals("h1", h1Element.tagName());
        Assert.assertEquals("Header 1", h1Element.text());

        // Edge case - empty document
        dirtyDocument = new Document("");
        cleanDocument = cleaner.clean(dirtyDocument);
        bodyElement = cleanDocument.body();
        Assert.assertNull(bodyElement);

        // Error scenario - null document
        Assert.assertThrows(NullPointerException.class, () -> cleaner.clean(null));
    }

    @Test
    public void testIsValid() {
        // Typical use case
        Document dirtyDocument = new Document("");
        dirtyDocument.appendElement("html")
                .appendElement("head")
                .appendElement("title").text("Title")
                .parent()
                .parent()
                .appendElement("body")
                .appendElement("h1").text("Header 1")
                .parent();
        boolean isValid = cleaner.isValid(dirtyDocument);
        Assert.assertTrue(isValid);

        // Edge case - empty document
        dirtyDocument = new Document("");
        isValid = cleaner.isValid(dirtyDocument);
        Assert.assertTrue(isValid);

        // Error scenario - null document
        Assert.assertThrows(NullPointerException.class, () -> cleaner.isValid(null));
    }
}