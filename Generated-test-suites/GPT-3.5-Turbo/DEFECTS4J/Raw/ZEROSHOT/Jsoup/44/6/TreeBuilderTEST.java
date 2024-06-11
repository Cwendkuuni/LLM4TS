import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class TreeBuilderTest {

    private TreeBuilder treeBuilder;

    @Before
    public void setUp() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        treeBuilder.initialiseParse(input, baseUri, errors);
        
        Assert.assertNotNull(treeBuilder.doc);
        Assert.assertNotNull(treeBuilder.reader);
        Assert.assertNotNull(treeBuilder.tokeniser);
        Assert.assertNotNull(treeBuilder.stack);
        Assert.assertEquals(baseUri, treeBuilder.baseUri);
        Assert.assertNull(treeBuilder.currentToken);
        Assert.assertEquals(errors, treeBuilder.errors);
    }

    @Test
    public void testParse() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document document = treeBuilder.parse(input, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals(1, document.select("html").size());
        Assert.assertEquals(1, document.select("head").size());
        Assert.assertEquals(1, document.select("title").size());
        Assert.assertEquals("Test", document.title());
        Assert.assertEquals(1, document.select("body").size());
        Assert.assertEquals(1, document.select("h1").size());
        Assert.assertEquals("Hello World!", document.select("h1").text());
    }

    @Test
    public void testProcessStartTag() {
        String tagName = "div";
        
        boolean result = treeBuilder.processStartTag(tagName);
        
        Assert.assertFalse(result);
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        String tagName = "div";
        Attributes attrs = new Attributes();
        attrs.put("id", "test-id");
        
        boolean result = treeBuilder.processStartTag(tagName, attrs);
        
        Assert.assertFalse(result);
    }

    @Test
    public void testProcessEndTag() {
        String tagName = "div";
        
        boolean result = treeBuilder.processEndTag(tagName);
        
        Assert.assertFalse(result);
    }

    @Test
    public void testCurrentElement() {
        Assert.assertNull(treeBuilder.currentElement());
        
        Element element = new Element("div");
        treeBuilder.stack.add(element);
        
        Assert.assertEquals(element, treeBuilder.currentElement());
    }
}