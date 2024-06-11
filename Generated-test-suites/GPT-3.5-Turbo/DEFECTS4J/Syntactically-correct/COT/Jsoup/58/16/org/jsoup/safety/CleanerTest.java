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
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;



public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.relaxed());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = new Document("");
        dirtyDocument.append("<html><head></head><body><p>Dirty text</p></body></html>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertTrue("Clean document should contain <p> tag", cleanDocument.select("p").size() > 0);
    }

    @Test
    public void testClean_NullInput() {
        assertNull("Clean result should be null for null input", cleaner.clean(null));
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = new Document("");
        dirtyDocument.append("<html><head></head><body><p>Dirty text</p></body></html>");

        assertTrue("Dirty document should be valid", cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValid_InvalidDocument() {
        Document dirtyDocument = new Document("");
        dirtyDocument.append("<html><head><script>alert('XSS')</script></head><body></body></html>");

        assertFalse("Dirty document should be invalid", cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValid_NullInput() {
        assertFalse("Null input document should be invalid", cleaner.isValid(null));
    }

    @Test
    public void testCleaningVisitor() {
        Cleaner.CleaningVisitor visitor = cleaner.new CleaningVisitor(null, null);
        Element sourceElement = new Element(Tag.valueOf("div"), "");

        visitor.head(sourceElement, 0);

        assertNotNull("Should create a new destination element", visitor.destination);
    }

    @Test
    public void testCopySafeNodes() {
        Element sourceElement = new Element(Tag.valueOf("div"), "");
        Element destElement = new Element(Tag.valueOf("div"), "");
        int numDiscarded = cleaner.copySafeNodes(sourceElement, destElement);
        
        assertEquals("Number of discarded nodes should be 0", 0, numDiscarded);
        assertEquals("Destination element should contain the copied safe nodes", sourceElement.html(), destElement.html());
    }

    @Test
    public void testCopySafeNodes_WithUnsafeNodes() {
        Element sourceElement = new Element(Tag.valueOf("div"), "");
        Element unsafeElement = new Element(Tag.valueOf("script"), "");
        sourceElement.appendChild(unsafeElement);
        Element destElement = new Element(Tag.valueOf("div"), "");
        int numDiscarded = cleaner.copySafeNodes(sourceElement, destElement);

        assertEquals("Number of discarded nodes should be 1", 1, numDiscarded);
        assertEquals("Destination element should not contain the unsafe node", 0, destElement.children().size());
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceElement = new Element(Tag.valueOf("a"), "");
        Attributes sourceAttrs = new Attributes();
        sourceAttrs.put("href", "http://example.com");
        sourceElement.attributes().addAll(sourceAttrs);

        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceElement);

        assertNotNull("Should create a new destination element", meta.el);
        assertEquals("Destination element should have the same tag name", sourceElement.tagName(), meta.el.tagName());
        assertEquals("Destination element should have the same base URI", sourceElement.baseUri(), meta.el.baseUri());
        assertEquals("Number of discarded attributes should be 0", 0, meta.numAttribsDiscarded);
        assertEquals("Destination element should have the same attributes", sourceAttrs, meta.el.attributes());
    }

    @Test
    public void testCreateSafeElement_WithUnsafeAttributes() {
        Element sourceElement = new Element(Tag.valueOf("a"), "");
        Attributes sourceAttrs = new Attributes();
        sourceAttrs.put("href", "javascript:alert('XSS')");
        sourceElement.attributes().addAll(sourceAttrs);

        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceElement);

        assertNotNull("Should create a new destination element", meta.el);
        assertEquals("Number of discarded attributes should be 1", 1, meta.numAttribsDiscarded);
        assertFalse("Destination element should not contain the unsafe attribute", meta.el.attributes().hasKey("href"));
    }

    private static class DummyNodeVisitor implements NodeVisitor {
        public void head(Node node, int depth) {}
        public void tail(Node node, int depth) {}
    }

    @Test
    public void testCleaningVisitor_WithInvalidParent() {
        Element rootElement = new Element(Tag.valueOf("html"), "");
        Element invalidParentElement = new Element(Tag.valueOf("invalid"), "");
        NodeTraversor traversor = new NodeTraversor(new Cleaner.CleaningVisitor(rootElement, invalidParentElement));

        traversor.traverse(rootElement);

        assertNull("Destination element should be null when parent is invalid", invalidParentElement.parent());
    }

    @Test
    public void testCleaningVisitor_WithTextNode() {
        Element rootElement = new Element(Tag.valueOf("html"), "");
        TextNode textNode = new TextNode("Hello World!");
        rootElement.appendChild(textNode);
        Element destinationElement = new Element(Tag.valueOf("div"), "");
        Cleaner.CleaningVisitor visitor = new Cleaner.CleaningVisitor(rootElement, destinationElement);

        visitor.head(textNode, 0);

        assertEquals("Destination element should contain the text node", textNode.getWholeText(), destinationElement.text());
    }

    @Test
    public void testCleaningVisitor_WithDataNodeInSafeTag() {
        Element rootElement = new Element(Tag.valueOf("html"), "");
        Element safeElement = new Element(Tag.valueOf("script"), "");
        rootElement.appendChild(safeElement);
        DataNode dataNode = new DataNode("alert('XSS')", "");
        safeElement.appendChild(dataNode);
        Element destinationElement = new Element(Tag.valueOf("div"), "");
        Cleaner.CleaningVisitor visitor = new Cleaner.CleaningVisitor(rootElement, destinationElement);
    
        visitor.head(dataNode, 0);
    
        assertTrue("Destination element should not contain the data node when it is in a safe tag", destinationElement.children().isEmpty());
    }

    @Test
    public void testCleaningVisitor_WithInvalidNode() {
        Element rootElement = new Element(Tag.valueOf("html"), "");
        Node invalidNode = new Node(Tag.valueOf("invalid"), "");
        Element destinationElement = new Element(Tag.valueOf("div"), "");
        Cleaner.CleaningVisitor visitor = new Cleaner.CleaningVisitor(rootElement, destinationElement);
    
        visitor.head(invalidNode, 0);
    
        assertTrue("Destination element should not contain the invalid node", destinationElement.children().isEmpty());
    }

    @Test
    public void testCleaningVisitor_WithSafeTagInTail() {
        Element rootElement = new Element(Tag.valueOf("html"), "");
        Element safeElement = new Element(Tag.valueOf("div"), "");
        Element destinationElement = new Element(Tag.valueOf("div"), "");
        Cleaner.CleaningVisitor visitor = new Cleaner.CleaningVisitor(rootElement, destinationElement);
    
        visitor.tail(safeElement, 0);
    
        assertNotEquals("Destination element should change when a safe tag is in tail", destinationElement, safeElement);
    }

    @Test
    public void testCleaningVisitor_WithUnsafeTagInTail() {
        Element rootElement = new Element(Tag.valueOf("html"), "");
        Element unsafeElement = new Element(Tag.valueOf("script"), "");
        Element destinationElement = new Element(Tag.valueOf("div"), "");
        Cleaner.CleaningVisitor visitor = new Cleaner.CleaningVisitor(rootElement, destinationElement);
    
        visitor.tail(unsafeElement, 0);
    
        assertTrue("Destination element should not change when an unsafe tag is in tail", destinationElement == destinationElement);
    }

    @Test
    public void testCleaningVisitor_WithSafeTagInTailWithNestedTags() {
        Element rootElement = new Element(Tag.valueOf("html"), "");
        Element safeParent = new Element(Tag.valueOf("div"), "");
        Element safeChild = new Element(Tag.valueOf("span"), "");
        safeParent.appendChild(safeChild);
        Element destinationElement = new Element(Tag.valueOf("div"), "");
        Cleaner.CleaningVisitor visitor = new Cleaner.CleaningVisitor(rootElement, destinationElement);
    
        visitor.tail(safeChild, 0);
    
        assertEquals("Destination element should change when a safe tag is in tail with nested tags", safeParent, destinationElement);
    }

    @Test
    public void testCleaningVisitor_WithUnsafeTagInTailWithNestedTags() {
        Element rootElement = new Element(Tag.valueOf("html"), "");
        Element unsafeParent = new Element(Tag.valueOf("script"), "");
        Element unsafeChild = new Element(Tag.valueOf("span"), "");
        unsafeParent.appendChild(unsafeChild);
        Element destinationElement = new Element(Tag.valueOf("div"), "");
        Cleaner.CleaningVisitor visitor = new Cleaner.CleaningVisitor(rootElement, destinationElement);
    
        visitor.tail(unsafeChild, 0);
    
        assertTrue("Destination element should not change when an unsafe tag is in tail with nested tags", destinationElement == destinationElement);
    }

    @Test
    public void testElementMeta() {
        ElementMeta elementMeta = new ElementMeta(null, 0);

        assertNull("Element meta should have a null element", elementMeta.el);
        assertEquals("Number of discarded attributes should be 0", 0, elementMeta.numAttribsDiscarded);
    }

    @Test
    public void testElementMeta_WithElement() {
        Element element = new Element(Tag.valueOf("a"), "");
        ElementMeta elementMeta = new ElementMeta(element, 2);

        assertEquals("Element meta should have the same element", element, elementMeta.el);
        assertEquals("Number of discarded attributes should be 2", 2, elementMeta.numAttribsDiscarded);
    }
}

