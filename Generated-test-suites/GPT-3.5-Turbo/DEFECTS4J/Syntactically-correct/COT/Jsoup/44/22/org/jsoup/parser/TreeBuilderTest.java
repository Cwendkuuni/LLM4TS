import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TreeBuilderTest {

    @Test
    public void testParse() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        
        String input = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com";
        
        Document parsedDocument = treeBuilder.parse(input, baseUri);
        
        Assert.assertNotNull(parsedDocument);
        Assert.assertEquals("Test Page", parsedDocument.title());
        
        Element bodyElement = parsedDocument.body();
        Assert.assertNotNull(bodyElement);
        
        Element h1Element = bodyElement.child(0);
        Assert.assertNotNull(h1Element);
        Assert.assertEquals("Hello World", h1Element.text());
    }

    @Test
    public void testProcessStartTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        
        String tagName = "div";
        boolean result = treeBuilder.processStartTag(tagName);
        
        Assert.assertFalse(result);
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        
        String tagName = "span";
        Attributes attrs = new Attributes();
        attrs.put("class", "highlight");
        
        boolean result = treeBuilder.processStartTag(tagName, attrs);
        
        Assert.assertFalse(result);
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        
        String tagName = "div";
        boolean result = treeBuilder.processEndTag(tagName);
        
        Assert.assertFalse(result);
    }

    @Test
    public void testCurrentElement() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        
        Element element = treeBuilder.currentElement();
        
        Assert.assertNull(element);
    }
}