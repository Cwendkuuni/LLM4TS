import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

    private Cleaner cleaner;
    private Whitelist whitelist;

    @Before
    public void setUp() {
        whitelist = new Whitelist();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        Document dirtyDocument = new Document("<html><head><title>Dirty Document</title></head><body><a href=\"http://example.com\">Link</a></body></html>");
        Document cleanedDocument = cleaner.clean(dirtyDocument);

        assertEquals("Dirty Document", cleanedDocument.title());
        assertEquals(1, cleanedDocument.body().getElementsByTag("a").size());
        assertEquals("Link", cleanedDocument.body().getElementsByTag("a").text());
        assertEquals("http://example.com", cleanedDocument.body().getElementsByTag("a").attr("href"));
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = new Document("<html><head><title>Dirty Document</title></head><body><a href=\"http://example.com\">Link</a></body></html>");

        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCleaningVisitorHeadSafeTag() {
        Element root = new Element(Tag.valueOf("html"));
        Element destination = new Element(Tag.valueOf("body"));
        Cleaner.CleaningVisitor cleaningVisitor = new Cleaner.CleaningVisitor(root, destination);

        Element source = new Element(Tag.valueOf("a"));
        source.attr("href", "http://example.com");
        int depth = 0;

        cleaningVisitor.head(source, depth);

        assertEquals(1, destination.children().size());
        assertEquals("a", destination.children().get(0).tagName());
        assertEquals("http://example.com", destination.children().get(0).attr("href"));
    }

    @Test
    public void testCleaningVisitorHeadNonSafeTag() {
        Element root = new Element(Tag.valueOf("html"));
        Element destination = new Element(Tag.valueOf("body"));
        Cleaner.CleaningVisitor cleaningVisitor = new Cleaner.CleaningVisitor(root, destination);

        Element source = new Element(Tag.valueOf("script"));
        int depth = 0;

        cleaningVisitor.head(source, depth);

        assertEquals(0, destination.children().size());
    }

    @Test
    public void testCleaningVisitorHeadTextNode() {
        Element root = new Element(Tag.valueOf("html"));
        Element destination = new Element(Tag.valueOf("body"));
        Cleaner.CleaningVisitor cleaningVisitor = new Cleaner.CleaningVisitor(root, destination);

        TextNode source = new TextNode("Test Text", "");
        int depth = 0;

        cleaningVisitor.head(source, depth);

        assertEquals(1, destination.childNodes().size());
        assertEquals("Test Text", destination.childNodes().get(0).toString());
    }

    @Test
    public void testCleaningVisitorHeadDataNode() {
        Element root = new Element(Tag.valueOf("html"));
        Element destination = new Element(Tag.valueOf("head"));
        Cleaner.CleaningVisitor cleaningVisitor = new Cleaner.CleaningVisitor(root, destination);

        Element parent = new Element(Tag.valueOf("script"));
        DataNode source = new DataNode("Data", "");
        parent.appendChild(source);
        int depth = 0;

        cleaningVisitor.head(source, depth);
        assertEquals(1, destination.childNodes().size());
        assertEquals("Data", destination.childNodes().get(0).toString());
    }

    @Test
    public void testCleaningVisitorTailSafeTag() {
        Element root = new Element(Tag.valueOf("html"));
        Element destination = new Element(Tag.valueOf("body"));
        Cleaner.CleaningVisitor cleaningVisitor = new Cleaner.CleaningVisitor(root, destination);

        Element source = new Element(Tag.valueOf("a"));
        int depth = 0;

        cleaningVisitor.head(source, depth);
        cleaningVisitor.tail(source, depth);

        assertEquals(destination, cleaningVisitor.destination);
    }

    @Test
    public void testCleaningVisitorTailUnsafeTag() {
        Element root = new Element(Tag.valueOf("html"));
        Element destination = new Element(Tag.valueOf("body"));
        Cleaner.CleaningVisitor cleaningVisitor = new Cleaner.CleaningVisitor(root, destination);

        Element source = new Element(Tag.valueOf("script"));
        int depth = 0;

        cleaningVisitor.head(source, depth);
        cleaningVisitor.tail(source, depth);

        assertNotEquals(destination, cleaningVisitor.destination);
    }

    @Test
    public void testCopySafeNodes() {
        Element source = new Element(Tag.valueOf("body"));
        Element dest = new Element(Tag.valueOf("body"));
        source.append("<a href=\"http://example.com\">Link</a>");

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals(0, numDiscarded);
        assertEquals(1, dest.children().size());
        assertEquals("a", dest.children().get(0).tagName());
        assertEquals("http://example.com", dest.children().get(0).attr("href"));
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("a"));
        sourceEl.attr("href", "http://example.com");
        sourceEl.attr("onclick", "alert('Hello');");

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        assertNotNull(elementMeta.el);
        assertEquals(1, elementMeta.numAttribsDiscarded);
        assertEquals("a", elementMeta.el.tagName());
        assertEquals("http://example.com", elementMeta.el.attr("href"));
    }

}