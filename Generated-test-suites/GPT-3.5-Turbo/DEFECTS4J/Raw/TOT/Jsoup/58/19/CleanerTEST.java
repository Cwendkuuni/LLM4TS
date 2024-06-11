import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

public class CleanerTest {
    private Document dirtyDocument;
    private Whitelist whitelist;
    private Cleaner cleaner;

    @Before
    public void setUp() {
        dirtyDocument = Jsoup.parse("<html><head><title>Dirty Document</title></head><body><div>Test Div</div></body></html>");
        whitelist = Whitelist.relaxed();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testCleanMethod() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        
        String expectedOutput = "<html><head></head><body><div>Test Div</div></body></html>";
        assertEquals(expectedOutput, cleanDocument.html());
    }

    @Test
    public void testIsValidMethod() {
        assertTrue(cleaner.isValid(dirtyDocument));
        
        Document dirtyDocumentWithUnsafeTags = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
        assertFalse(cleaner.isValid(dirtyDocumentWithUnsafeTags));
    }

    @Test
    public void testCleaningVisitor() {
        Element root = dirtyDocument.body();
        Element destination = new Element(Tag.valueOf("div"), "");

        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);
        
        // Test head() method with safe element
        Element safeElement = new Element(Tag.valueOf("div"), "");
        cleaningVisitor.head(safeElement, 0);
        assertEquals(0, cleaningVisitor.numDiscarded);
        assertEquals(1, destination.children().size());
        
        // Test head() method with unsafe element
        Element unsafeElement = new Element(Tag.valueOf("script"), "");
        cleaningVisitor.head(unsafeElement, 0);
        assertEquals(1, cleaningVisitor.numDiscarded);
        assertEquals(1, destination.children().size());

        // Test head() method with TextNode
        TextNode textNode = new TextNode("Test Text", "");
        cleaningVisitor.head(textNode, 0);
        assertEquals(1, cleaningVisitor.numDiscarded);
        assertEquals(2, destination.children().size());

        // Test head() method with DataNode
        DataNode dataNode = new DataNode("Test Data", "");
        cleaningVisitor.head(dataNode, 0);
        assertEquals(1, cleaningVisitor.numDiscarded);
        assertEquals(3, destination.children().size());

        // Test head() method with comment node
        Comment commentNode = new Comment("Test Comment", "");
        cleaningVisitor.head(commentNode, 0);
        assertEquals(2, cleaningVisitor.numDiscarded);
        assertEquals(3, destination.children().size());
        
        // Test tail() method
        cleaningVisitor.tail(unsafeElement, 0);
        assertEquals(destination, cleaningVisitor.destination);
    }

    @Test
    public void testCopySafeNodes() {
        Element source = dirtyDocument.body();
        Element dest = new Element(Tag.valueOf("div"), "");

        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        String sourceTag = "div";
        Element sourceEl = new Element(Tag.valueOf(sourceTag), "");
        sourceEl.attr("style", "color: red;"); // Unsafe attribute

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        Element dest = elementMeta.el;

        assertEquals(sourceTag, dest.tagName());
        assertEquals(1, dest.attributes().size());
        assertTrue(dest.attributes().hasKey("style"));

        String expectedOutput = "<div style=\"color: red;\"></div>";
        assertEquals(expectedOutput, dest.outerHtml());
    }

    @Test
    public void testElementMeta() {
        Element dest = new Element(Tag.valueOf("div"), "");
        Cleaner.ElementMeta elementMeta = new Cleaner.ElementMeta(dest, 2);

        assertEquals(dest, elementMeta.el);
        assertEquals(2, elementMeta.numAttribsDiscarded);
    }
}