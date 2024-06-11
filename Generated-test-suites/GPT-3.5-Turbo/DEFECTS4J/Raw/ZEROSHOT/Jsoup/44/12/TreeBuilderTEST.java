import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.junit.Assert;

import java.util.ArrayList;

public class TreeBuilderTest {
    
    private TreeBuilder treeBuilder;
    private ParseErrorList errors;
    
    @Before
    public void setUp() {
        treeBuilder = new TreeBuilderImpl();
        errors = new ParseErrorList();
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<html><body><h1>Hello World</h1></body></html>";
        
        treeBuilder.initialiseParse(input, "http://example.com", errors);
        
        Assert.assertEquals("http://example.com", treeBuilder.baseUri);
        Assert.assertEquals("String input must not be null", input, treeBuilder.reader.getString());
        Assert.assertEquals(errors, treeBuilder.errors);
        Assert.assertNotNull(treeBuilder.doc);
        Assert.assertNotNull(treeBuilder.tokeniser);
        Assert.assertNotNull(treeBuilder.stack);
    }
    
    @Test
    public void testParse() {
        String input = "<html><body><h1>Hello World</h1></body></html>";
        
        Document document = treeBuilder.parse(input, "http://example.com", errors);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("http://example.com", document.baseUri());
    }
    
    @Test
    public void testRunParser() {
        String input = "<html><body><h1>Hello World</h1></body></html>";
        
        treeBuilder.initialiseParse(input, "http://example.com", errors);
        treeBuilder.runParser();
        
        Assert.assertEquals(0, treeBuilder.stack.size());
    }
    
    @Test
    public void testProcessStartTag() {
        String name = "h1";
        
        boolean result = treeBuilder.processStartTag(name);
        
        Assert.assertTrue(result);
        Assert.assertEquals(name, treeBuilder.currentToken.name());
    }
    
    @Test
    public void testProcessStartTagWithAttrs() {
        String name = "h1";
        Attributes attrs = new Attributes();
        
        boolean result = treeBuilder.processStartTag(name, attrs);
        
        Assert.assertTrue(result);
        Assert.assertEquals(name, treeBuilder.currentToken.name());
        Assert.assertEquals(attrs, treeBuilder.currentToken.attributes());
    }
    
    @Test
    public void testProcessEndTag() {
        String name = "h1";
        
        boolean result = treeBuilder.processEndTag(name);
        
        Assert.assertTrue(result);
        Assert.assertEquals(name, treeBuilder.currentToken.name());
    }
    
    @Test
    public void testCurrentElement() {
        Element element = new Element("h1");
        treeBuilder.stack.add(element);
        
        Element currentElement = treeBuilder.currentElement();
        
        Assert.assertEquals(element, currentElement);
    }
}