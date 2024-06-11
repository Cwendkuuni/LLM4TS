package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class CleanerTest {

    @org.junit.Before
    public void setUp() {
        // Initialize the whitelist for testing
        Whitelist whitelist = Whitelist.simpleText();
    }

    @org.junit.Test
    public void testCreateCleaner() {
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    @org.junit.Test
    public void testCleanDocument() {
        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
        Document cleanedDocument = new Cleaner(whitelist).clean(dirtyDocument);
        assertFalse(cleanedDocument.body().text().contains("alert('XSS')"));
        assertTrue(cleanedDocument.body().text().isEmpty());
    }

    @org.junit.Test
    public void testIsValidDocument() {
        Document invalidDocument = Jsoup.parse("<html><head><script>alert('XSS');</script></head><body></body></html>");
        assertFalse(new Cleaner(whitelist).isValid(invalidDocument));

        Document validDocument = Jsoup.parse("<html><body><p>Hello World!</p></body></html>");
        assertTrue(new Cleaner(whitelist).isValid(validDocument));
    }

    @org.junit.Test
    public void testCleaningVisitor_SafeTag() {
        Document dirtyDocument = Jsoup.parse("<html><body><p id='test' class='test'>Hello <strong>World!</strong></p></body></html>");
        Element root = dirtyDocument.body();
        Element destination = Jsoup.parse("<html><body></body></html>").body();

        CleaningVisitor cleaningVisitor = new Cleaner.CleaningVisitor(root, destination);
        NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
        traversor.traverse(root);

        Element safeElement = (Element) cleaningVisitor.destination.child(0);
        assertTrue(safeElement.tagName().equals("p"));
        assertEquals(2, safeElement.children().size());
        Element strongElement = (Element) safeElement.child(1);
        assertTrue(strongElement.tagName().equals("strong"));
        assertEquals("World!", strongElement.text());

        assertFalse(destination.child(1).getClass().isInstance(root.child(1).getClass()));
    }

    @org.junit.Test
    public void testCleaningVisitor_UnsafeTag() {
        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
        Element root = dirtyDocument.body();
        Element destination = Jsoup.parse("<html><body></body></html>").body();

        CleaningVisitor cleaningVisitor = new Cleaner.CleaningVisitor(root, destination);
        NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
        traversor.traverse(root);

        assertNull(destination.child(0));
    }

    @org.junit.Test
    public void testCleaningVisitor_TextNode() {
        Document dirtyDocument = Jsoup.parse("<html><body><p>Hello <script>World!</script></p></body></html>");
        Element root = dirtyDocument.body();
        Element destination = Jsoup.parse("<html><body></body></html>").body();

        CleaningVisitor cleaningVisitor = new Cleaner.CleaningVisitor(root, destination);
        NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
        traversor.traverse(root);

        TextNode textNode = (TextNode) destination.child(0);
        assertEquals("Hello ", textNode.text());
    }

    @org.junit.Test
    public void testCleaningVisitor_DataNode() {
        Document dirtyDocument = Jsoup.parse("<html><body><img src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQIW2P8z8BASsDEhjzQScCQgQ tBHrGAAAAXElEQVR4Ae3U5Q6wCQgGjBcjvALwCwI3PjIjcHF2YXJzVGxzGx//z8AAAD3R/q6SjGwF8YXjzAAAAASUVORK5CYII='></body></html>");
        Element root = dirtyDocument.body();
        Element destination = Jsoup.parse("<html><body></body></html>").body();

        CleaningVisitor cleaningVisitor = new Cleaner.CleaningVisitor(root, destination);
        NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
        traversor.traverse(root);

        DataNode dataNode = (DataNode) destination.child(0);
        assertEquals("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQIW2P8z8BASsDEhjzQScCQgQ tBHrGAAAAXElEQVR4Ae3U5Q6wCQgGjBcjvALwCwI3PjIjcHF2YXJzVGxzGx//z8AAAD3R/q6SjGwF8YXjzAAAAASUVORK5CYII", dataNode.data());
    }

    @org.junit.Test
    public void testCopySafeNodes() {
        Document dirtyDocument = Jsoup.parse("<html><body><p id='test' class='test'><script>alert('XSS');</script></p></body></html>");
        Element source = dirtyDocument.body();
        Element destination = Jsoup.parse("<html><body></body></html>").body();

        int numDiscarded = new Cleaner().copySafeNodes(source, destination);
        assertTrue(numDiscarded > 0);
        assertFalse(destination.child(0).getClass().isInstance(source.child(0).getClass()));

        Element safeElement = (Element) destination.child(0);
        assertTrue(safeElement.tagName().equals("p"));
        assertEquals("", safeElement.attr("id"));
        assertEquals("", safeElement.attr("class"));
        assertFalse(safeElement.children().isEmpty());
        Element child = (Element) safeElement.child(0);
        assertEquals("", child.attr("id"));
        assertEquals("", child.attr("class"));
        assertTrue(child.text().isEmpty());
    }

    @org.junit.Test
    public void testCreateSafeElement() {
        Element sourceElement = Jsoup.parse("<html><body><p id='test' class='test'><strong>Hello World!</strong></p></body></html>").body().getElementsByTag("p").get(0);
        Element safeElement = new Cleaner().createSafeElement(sourceElement);

        assertTrue(safeElement.tagName().equals("p"));
        assertEquals("", safeElement.attr("id"));
        assertEquals("", safeElement.attr("class"));
        assertTrue(safeElement.children().size() > 0);
        Element child = (Element) safeElement.child(0);
        assertTrue(child.tagName().equals("strong"));
        assertEquals("World!", child.text());
    }

    @org.junit.Test
    public void testIsSafeTag() {
        Whitelist whitelist = Whitelist.simpleText();

        assertFalse(whitelist.isSafeTag("script"));
        assertTrue(whitelist.isSafeTag("p"));
    }

    @org.junit.Test
    public void testIsSafeAttribute() {
        Whitelist whitelist = Whitelist.simpleText();

        assertFalse(whitelist.isSafeAttribute("p", null, new Attribute("onclick", "alert('XSS')")));
        assertTrue(whitelist.isSafeAttribute("p", null, new Attribute("id", "test")));
    }

    @org.junit.Test
    public void testGetEnforcedAttributes() {
        Whitelist whitelist = Whitelist.simpleText();

        List<Attribute> enforcedAttributes = whitelist.getEnforcedAttributes("p");
        assertFalse(enforcedAttributes.isEmpty());
        assertTrue(enforcedAttributes.get(0).getName().equals("id"));
        assertEquals("test", enforcedAttributes.get(0).getValue());
    }
}