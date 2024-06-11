
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.*;
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
        Document dirtyDocument = Jsoup.parse("<html><body><p>Hello, World!</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><head></head><body><p>Hello, World!</p></body></html>", cleanDocument.outerHtml());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = Jsoup.parse("<html><body><p>Hello, World!</p></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Element root = new Element(Tag.valueOf("div"), "");
        Element destination = new Element(Tag.valueOf("div"), "");

        Element child1 = new Element(Tag.valueOf("p"), "");
        child1.appendText("Hello");
        root.appendChild(child1);

        Element child2 = new Element(Tag.valueOf("p"), "");
        child2.appendText("World!");
        root.appendChild(child2);

        int numDiscarded = cleaner.copySafeNodes(root, destination);
        assertEquals(0, numDiscarded);
        assertEquals("<div><p>Hello</p><p>World!</p></div>", destination.outerHtml());
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceElement = new Element(Tag.valueOf("a"), "");
        sourceElement.attr("href", "http://example.com");
        sourceElement.attr("target", "_blank");

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        Element destElement = elementMeta.el;
        assertEquals("a", destElement.tagName());
        assertEquals("http://example.com", destElement.attr("href"));
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}

