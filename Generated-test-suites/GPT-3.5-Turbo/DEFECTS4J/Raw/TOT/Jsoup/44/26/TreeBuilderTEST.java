import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.TreeBuilder;

import static org.junit.Assert.*;

public class TreeBuilderTest {
    private TreeBuilder treeBuilder;
    
    @Before
    public void setUp() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                // Implementation for process() method
                return false;
            }
        };
    }
    
    @Test
    public void parse_shouldReturnValidDocument() {
        String input = "<html><head><title>Test Document</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = treeBuilder.parse(input, baseUri);
        
        assertNotNull("Document object should not be null", doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
        assertEquals(1, doc.childNodeSize());
        
        Element head = doc.head();
        assertNotNull("Head element should not be null", head);
        assertEquals("head", head.tagName());
        assertEquals(1, head.childNodeSize());
        
        Element title = head.selectFirst("title");
        assertNotNull("Title element should not be null", title);
        assertEquals("Test Document", title.text());

        Element body = doc.body();
        assertNotNull("Body element should not be null", body);
        assertEquals("body", body.tagName());
        assertEquals(1, body.childNodeSize());
        
        Element h1 = body.selectFirst("h1");
        assertNotNull("H1 element should not be null", h1);
        assertEquals("Hello World!", h1.text());
    }
    
    @Test
    public void parse_shouldReturnDocumentWithNoErrors() {
        String input = "<html><head><title>Test Document</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errorList = ParseErrorList.noTracking();
        
        Document doc = treeBuilder.parse(input, baseUri, errorList);
        
        assertNull("Error list should be null", errorList.get(0));
    }
    
    @Test
    public void processStartTag_shouldReturnTrueWhenValidNameProvided() {
        String tagName = "div";
        
        boolean result = treeBuilder.processStartTag(tagName);
        
        assertTrue("processStartTag should return true for valid tag name", result);
    }
    
    @Test
    public void processStartTag_shouldAddAttributesToTokenWhenValidNameAndAttributesProvided() {
        String tagName = "div";
        Attributes attrs = new Attributes();
        attrs.put("id", "myDiv");
        attrs.put("class", "myClass");
        
        boolean result = treeBuilder.processStartTag(tagName, attrs);
        
        assertTrue("processStartTag should return true for valid tag name and attributes", result);
        assertEquals("div", treeBuilder.start.name());
        assertEquals(attrs, treeBuilder.start.attributes());
    }
    
    @Test
    public void processEndTag_shouldReturnTrueWhenValidNameProvided() {
        String tagName = "div";
        
        boolean result = treeBuilder.processEndTag(tagName);
        
        assertTrue("processEndTag should return true for valid tag name", result);
    }
    
    @Test
    public void currentElement_shouldReturnNullWhenStackIsEmpty() {
        Element element = treeBuilder.currentElement();
        
        assertNull("currentElement should return null when stack is empty", element);
    }
    
    @Test
    public void currentElement_shouldReturnTopElementInStack() {
        Element element1 = new Element("div");
        Element element2 = new Element("p");
        
        treeBuilder.stack.add(element1);
        treeBuilder.stack.add(element2);
        
        Element current = treeBuilder.currentElement();
        
        assertEquals(element2, current);
    }
}