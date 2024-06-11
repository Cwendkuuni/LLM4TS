import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {
    private Cleaner cleaner;
    private Whitelist whitelist;

    @Before
    public void setUp() {
        whitelist = Whitelist.basic();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        Document dirtyDocument = Jsoup.parseBodyFragment("<div><script>alert('XSS');</script></div><p>Safe text</p>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <div></div>\n" +
                "  <p>Safe text</p>\n" +
                " </body>\n" +
                "</html>", cleanDocument.toString());
    }

    @Test
    public void testIsValid() {
        Document validDocument = Jsoup.parseBodyFragment("<p>Valid text</p>");
        assertTrue(cleaner.isValid(validDocument));

        Document invalidDocument = Jsoup.parseBodyFragment("<div><script>alert('XSS');</script></div>");
        assertFalse(cleaner.isValid(invalidDocument));
    }

    @Test
    public void testCleaningVisitorHeadSafeTag() {
        Element root = new Element("div");
        Element destination = new Element("div");
        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);

        Element safeTag = new Element("p");
        cleaningVisitor.head(safeTag, 0);
        assertEquals("<div><div><p></p></div></div>", destination.toString());
    }

    @Test
    public void testCleaningVisitorHeadSafeTagWithSafeAttributes() {
        Element root = new Element("div");
        Element destination = new Element("div");
        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);

        Element safeTag = new Element("a");
        safeTag.attributes().put("href", "https://example.com");
        cleaningVisitor.head(safeTag, 0);
        assertEquals("<div><div><a href=\"https://example.com\"></a></div></div>", destination.toString());
    }

    @Test
    public void testCleaningVisitorHeadSafeTagWithUnsafeAttribute() {
        Element root = new Element("div");
        Element destination = new Element("div");
        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);

        Element safeTag = new Element("a");
        safeTag.attributes().put("href", "javascript:alert('XSS')");
        cleaningVisitor.head(safeTag, 0);
        assertEquals("<div><div><a></a></div></div>", destination.toString());
    }

    @Test
    public void testCleaningVisitorHeadUnsafeTag() {
        Element root = new Element("div");
        Element destination = new Element("div");
        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);

        Element unsafeTag = new Element("script");
        cleaningVisitor.head(unsafeTag, 0);
        assertEquals("<div><div></div></div>", destination.toString());
    }

    @Test
    public void testCleaningVisitorHeadTextNode() {
        Element root = new Element("div");
        Element destination = new Element("div");
        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);

        Element parentTag = new Element("p");
        parentTag.appendChild(new TextNode("Safe text"));
        cleaningVisitor.head(parentTag, 0);
        assertEquals("<div><div><p>Safe text</p></div></div>", destination.toString());
    }

    @Test
    public void testCleaningVisitorHeadDataNodeWithSafeParentTag() {
        Element root = new Element("div");
        Element destination = new Element("div");
        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);

        Element safeParentTag = new Element("p");
        safeParentTag.attributes().put("class", "safe");
        Element dataParentTag = new Element("script");
        dataParentTag.appendChild(new DataNode("alert('XSS')"));
        safeParentTag.appendChild(dataParentTag);
        cleaningVisitor.head(safeParentTag, 0);
        assertEquals("<div><div><p><script>alert('XSS')</script></p></div></div>", destination.toString());
    }

    @Test
    public void testCleaningVisitorHeadDataNodeWithUnsafeParentTag() {
        Element root = new Element("div");
        Element destination = new Element("div");
        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);

        Element unsafeParentTag = new Element("div");
        Element dataParentTag = new Element("script");
        dataParentTag.appendChild(new DataNode("alert('XSS')"));
        unsafeParentTag.appendChild(dataParentTag);
        cleaningVisitor.head(unsafeParentTag, 0);
        assertEquals("<div><div></div></div>", destination.toString());
    }

    @Test
    public void testCleaningVisitorTailSafeTag() {
        Element root = new Element("div");
        Element destination = new Element("div");
        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);

        Element safeTag = new Element("p");
        cleaningVisitor.tail(safeTag, 0);
        assertEquals("<div></div>", destination.toString());
    }

    @Test
    public void testCleaningVisitorTailUnsafeTag() {
        Element root = new Element("div");
        Element destination = new Element("div");
        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);

        Element unsafeTag = new Element("script");
        cleaningVisitor.tail(unsafeTag, 0);
        assertEquals("<div></div>", destination.toString());
    }

    @Test
    public void testCleaningVisitorTailTextNode() {
        Element root = new Element("div");
        Element destination = new Element("div");
        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);

        Element parentTag = new Element("p");
        parentTag.appendChild(new TextNode("Safe text"));
        cleaningVisitor.tail(parentTag, 0);
        assertEquals("<div></div>", destination.toString());
    }

    @Test
    public void testCleanSafeTag() {
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(new Element("p"));
        assertEquals("<p></p>", elementMeta.el.toString());
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

    @Test
    public void testCleanSafeTagWithSafeAttribute() {
        Element safeTag = new Element("a");
        safeTag.attributes().put("href", "https://example.com");
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(safeTag);
        assertEquals("<a href=\"https://example.com\"></a>", elementMeta.el.toString());
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

    @Test
    public void testCleanSafeTagWithUnsafeAttribute() {
        Element safeTag = new Element("a");
        safeTag.attributes().put("href", "javascript:alert('XSS')");
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(safeTag);
        assertEquals("<a></a>", elementMeta.el.toString());
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }

    @Test
    public void testCleanUnsafeTag() {
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(new Element("script"));
        assertEquals("<script></script>", elementMeta.el.toString());
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

    @Test
    public void testCopySafeNodes() {
        Element source = Jsoup.parseBodyFragment("<div><script>alert('XSS');</script></div><p>Safe text</p>").body();
        Element dest = new Element("div");
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(1, numDiscarded);
        assertEquals("<div></div><p>Safe text</p>", dest.toString());
    }
}