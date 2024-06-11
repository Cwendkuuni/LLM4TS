import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        // Typical use case
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Parser.parseBodyFragment("<p>This is a <strong>test</strong></p>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertEquals("<p>This is a <strong>test</strong></p>", cleanDocument.body().html());
    }

    @Test
    public void testIsValid() {
        // Typical use case
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Parser.parseBodyFragment("<p>This is a <strong>test</strong></p>");
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testCleaningVisitor() {
        // Edge case: source node is not an Element
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Element root = new Element(Tag.valueOf("div"), "");
        CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, root);
        TextNode textNode = new TextNode("This is a text node", "");
        
        assertDoesNotThrow(() -> cleaningVisitor.head(textNode, 0));
    }

    @Test
    public void testCopySafeNodes() {
        // Edge case: root and destination elements are same
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Element root = new Element(Tag.valueOf("div"), "");
        CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, root);
        
        assertDoesNotThrow(() -> cleaner.copySafeNodes(root, root));
    }

    @Test
    public void testCreateSafeElement() {
        // Error scenario: source tag is not safe according to whitelist
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceEl = new Element(Tag.valueOf("script"), "");
        
        ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        assertEquals(1, elementMeta.numAttribsDiscarded);
    }

    @Test
    public void testCleaningVisitorTail() {
        // Error scenario: source node is not an Element
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Element root = new Element(Tag.valueOf("div"), "");
        CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, root);
        TextNode textNode = new TextNode("This is a text node", "");
        
        assertDoesNotThrow(() -> cleaningVisitor.tail(textNode, 0));
    }
}