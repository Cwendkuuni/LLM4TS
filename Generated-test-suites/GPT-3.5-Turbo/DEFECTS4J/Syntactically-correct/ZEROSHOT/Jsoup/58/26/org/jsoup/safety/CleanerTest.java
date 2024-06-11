import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    
    private Cleaner cleaner;
    private Whitelist whitelist;
    private Document dirtyDocument;
    private Document cleanDocument;
    
    @Before
    public void setUp() {
        whitelist = new Whitelist();
        cleaner = new Cleaner(whitelist);
        
        // Create a dirty document for testing
        String html = "<html><head></head><body><div>Test<div></body></html>";
        dirtyDocument = Parser.parse(html, "");
        
        // Create a clean document for comparison
        cleanDocument = Parser.parse("<html><head></head><body><div></div></body></html>");
    }
    
    @Test
    public void testClean() {
        Document result = cleaner.clean(dirtyDocument);
        Assert.assertEquals(cleanDocument, result);
    }
    
    @Test
    public void testIsValid() {
        boolean result = cleaner.isValid(dirtyDocument);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testCleaningVisitorHead() {
        Cleaner.CleaningVisitor visitor = cleaner.new CleaningVisitor(dirtyDocument.body(), cleanDocument.body());
        
        // Test with safe tag
        Element source = new Element(Tag.valueOf("div"), "");
        visitor.head(source, 0);
        Assert.assertEquals("div", cleanDocument.body().child(0).tagName());
        
        // Test with unsafe tag
        source = new Element(Tag.valueOf("script"), "");
        visitor.head(source, 0);
        Assert.assertEquals(0, cleanDocument.body().childNodeSize());
        
        // Test with text node
        source = new TextNode("Test", "");
        visitor.head(source, 0);
        Assert.assertEquals("Test", cleanDocument.body().text());
        
        // Test with data node and safe parent tag
        Element parent = new Element(Tag.valueOf("div"), "");
        Element dataNodeParent = new Element(Tag.valueOf("script"), "");
        dataNodeParent.appendChild(new DataNode("Data", ""));
        parent.appendChild(dataNodeParent);
        source = dataNodeParent.childNode(0);
        visitor.head(source, 0);
        Assert.assertEquals("Data", cleanDocument.body().child(2).childNode(0).toString());
    }
    
    @Test
    public void testCleaningVisitorTail() {
        Cleaner.CleaningVisitor visitor = cleaner.new CleaningVisitor(dirtyDocument.body(), cleanDocument.body());
        
        // Test with safe tag
        Element source = new Element(Tag.valueOf("div"), "");
        visitor.tail(source, 0);
        Assert.assertEquals(cleanDocument.body(), visitor.destination);
        
        // Test with unsafe tag
        source = new Element(Tag.valueOf("script"), "");
        visitor.tail(source, 0);
        Assert.assertEquals(cleanDocument.body(), visitor.destination);
    }
    
    @Test
    public void testCopySafeNodes() {
        int result = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
        Assert.assertEquals(1, result);
    }
    
    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("div"), "");
        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);
        
        Assert.assertEquals("div", meta.el.tagName());
    }
    
    @Test
    public void testElementMetaConstructor() {
        Element element = new Element(Tag.valueOf("div"), "", new Attributes());
        Cleaner.ElementMeta meta = new Cleaner.ElementMeta(element, 1);
        
        Assert.assertEquals(element, meta.el);
        Assert.assertEquals(1, meta.numAttribsDiscarded);
    }
}