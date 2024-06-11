import org.jsoup.helper.Validate;
import org.jsoup.Jsoup;
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
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {
    
    @Test
    public void testClean() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><p>This is a test</p></body></html>");
        Document clean = cleaner.clean(dirtyDocument);
        
        assertNotNull(clean);
        assertEquals("<html><head></head><body><p>This is a test</p></body></html>", clean.html());
    }
    
    @Test
    public void testIsValid() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><p>This is a test</p></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertTrue(isValid);
    }
    
    @Test
    public void testCopySafeNodes() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        
        Element source = new Element("div");
        Element dest = new Element("div");
        
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        
        assertEquals(0, numDiscarded);
        assertEquals(source.html(), dest.html());
    }
    
    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Element sourceEl = new Element("p");
        sourceEl.attr("class", "test");
        
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        
        assertNotNull(elementMeta.el);
        assertEquals(sourceEl.tagName(), elementMeta.el.tagName());
        assertEquals(sourceEl.baseUri(), elementMeta.el.baseUri());
        assertEquals(sourceEl.attributes(), elementMeta.el.attributes());
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

}