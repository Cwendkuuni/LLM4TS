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
import static org.junit.Assert.*;
import org.junit.Test;

public class CleanerTest {
    
    @Test
    public void testClean() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        
        // Test with null document
        assertNull(cleaner.clean(null));
        
        // Test with valid document
        Document dirtyDocument = new Document("");
        Element body = new Element("body");
        Element div = new Element("div");
        div.text("Hello World");
        body.appendChild(div);
        dirtyDocument.appendChild(body);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><head></head><body><div>Hello World</div></body></html>", cleanDocument.toString());
        
        // Test with empty document
        dirtyDocument = new Document("");
        cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><head></head><body></body></html>", cleanDocument.toString());
    }
    
    @Test
    public void testIsValid() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        
        // Test with null document
        assertFalse(cleaner.isValid(null));
        
        // Test with valid document
        Document dirtyDocument = new Document("");
        Element body = new Element("body");
        Element div = new Element("div");
        div.text("Hello World");
        body.appendChild(div);
        dirtyDocument.appendChild(body);
        assertTrue(cleaner.isValid(dirtyDocument));
        
        // Test with invalid document
        dirtyDocument = new Document("");
        Element head = new Element("head");
        Element script = new Element("script");
        script.text("alert('Hello World');");
        head.appendChild(script);
        dirtyDocument.appendChild(head);
        assertFalse(cleaner.isValid(dirtyDocument));
    }
    
    @Test
    public void testCleaningVisitor() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(null, null);
        
        // Test with valid element
        Element element = new Element("div");
        cleaningVisitor.head(element, 0);
        cleaningVisitor.tail(element, 0);
        assertNotNull(cleaningVisitor.destination);
        
        // Test with invalid element
        cleaningVisitor.head(element, 0);
        cleaningVisitor.tail(element, 0);
        assertNull(cleaningVisitor.destination);
        
        // Test with text node
        TextNode textNode = new TextNode("Hello World");
        cleaningVisitor.head(textNode, 0);
        assertEquals("Hello World", cleaningVisitor.destination.text());
    }
    
    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        
        // Test with valid source and destination elements
        Element source = new Element("div");
        Element dest = new Element("body");
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(0, numDiscarded);
        
        // Test with invalid source and valid destination elements
        source = new Element("script");
        numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(1, numDiscarded);
        
        // Test with valid source and invalid destination elements
        dest = new Element("head");
        numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(1, numDiscarded);
    }
    
    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        
        // Test with valid source element
        Element source = new Element("div");
        ElementMeta meta = cleaner.createSafeElement(source);
        assertNotNull(meta.el);
        assertEquals(0, meta.numAttribsDiscarded);
        
        // Test with invalid source element
        source = new Element("script");
        meta = cleaner.createSafeElement(source);
        assertNull(meta.el);
        assertEquals(0, meta.numAttribsDiscarded);
    }
    
    
}