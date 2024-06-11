import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class CleanerTest {
    private static final Whitelist WHITELIST = Whitelist.basic();
    private Cleaner cleaner;

    @Before
    public void setup() {
        cleaner = new Cleaner(WHITELIST);
    }

    @Test
    public void testClean_validInput() {
        Document dirtyDocument = Jsoup.parseBodyFragment("<p>Hello, <b>world!</b></p>");
        Document cleanedDocument = cleaner.clean(dirtyDocument);
        assertTrue(cleanedDocument.body().hasText());
    }

    @Test
    public void testClean_invalidInput() {
        Document dirtyDocument = Jsoup.parseBodyFragment("<p>Hello, <script>maliciousCode()</script>world!</p>");
        Document cleanedDocument = cleaner.clean(dirtyDocument);
        assertFalse(cleanedDocument.body().hasText());
    }

    @Test
    public void testIsValid_validInput() {
        Document dirtyDocument = Jsoup.parseBodyFragment("<p>Hello, <b>world!</b></p>");
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValid_invalidInput() {
        Document dirtyDocument = Jsoup.parseBodyFragment("<p>Hello, <script>maliciousCode()</script>world!</p>");
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCleaningVisitor_head() {
        Element sourceEl = new Element("div");
        sourceEl.appendText("Hello, world!");

        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(sourceEl, new Element("div"));
        cleaningVisitor.head(sourceEl, 0);

        assertEquals("Hello, world!", cleaningVisitor.destination.text());
    }

    @Test
    public void testCleaningVisitor_tail() {
        Element sourceEl = new Element("div");
        Element destination = new Element("div");
        Element parent = new Element("div");
        parent.appendChild(destination);

        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(sourceEl, parent);
        cleaningVisitor.tail(sourceEl, 0);

        assertEquals(parent, cleaningVisitor.destination);
    }

    @Test
    public void testCopySafeNodes() {
        Element source = new Element("div");
        Element dest = new Element("div");

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element("div");
        sourceEl.attr("data-test", "value");

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        assertEquals("div", elementMeta.el.tagName());
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElement_discardedAttributes() {
        Element sourceEl = new Element("div");
        sourceEl.attr("onclick", "alert('Hello')");
        sourceEl.attr("onmouseover", "alert('Hi')");
        sourceEl.attr("data-test", "value");

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        assertEquals("div", elementMeta.el.tagName());
        assertEquals(2, elementMeta.numAttribsDiscarded);
    }
}