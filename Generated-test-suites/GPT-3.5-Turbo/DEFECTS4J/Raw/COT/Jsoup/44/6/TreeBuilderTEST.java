

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class TreeBuilderTest {
    private TreeBuilder treeBuilder;

    @Before
    public void setup(){
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        treeBuilder.initialiseParse(input, baseUri, errors);
        
        Document doc = treeBuilder.doc;
        CharacterReader reader = treeBuilder.reader;
        Tokeniser tokeniser = treeBuilder.tokeniser;
        ArrayList<Element> stack = treeBuilder.stack;
        String actualBaseUri = treeBuilder.baseUri;
        
        // Check that the properties are set correctly
        Assert.assertNotNull(doc);
        Assert.assertEquals(baseUri, actualBaseUri);
        Assert.assertNotNull(reader);
        Assert.assertNotNull(stack);
        Assert.assertNotNull(tokeniser);
    }

    @Test
    public void testParse() {
        String input = "<html><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        Document doc = treeBuilder.parse(input, baseUri, errors);

        // Check that the document is parsed correctly
        Assert.assertNotNull(doc);
        Assert.assertEquals(baseUri, doc.baseUri());
        Assert.assertEquals("html", doc.tagName());
        Assert.assertEquals(1, doc.children().size());
        Element body = doc.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.tagName());
    }

    @Test
    public void testRunParser() {
        String input = "<html><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        treeBuilder.initialiseParse(input, baseUri, errors);
        treeBuilder.runParser();

        // Check that the parser runs without any exceptions
    }

    @Test
    public void testProcessStartTag() {
        String tagName = "div";
        boolean result = treeBuilder.processStartTag(tagName);

        // Check that the start tag is processed correctly
        Assert.assertTrue(result);
        Assert.assertNotNull(treeBuilder.currentToken);
        Assert.assertEquals(Token.TokenType.StartTag, treeBuilder.currentToken.type);
        Assert.assertEquals(tagName, treeBuilder.currentToken.name());
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        String tagName = "div";
        Attributes attrs = new Attributes();
        attrs.put("id", "divTag");
        
        boolean result = treeBuilder.processStartTag(tagName, attrs);

        // Check that the start tag with attributes is processed correctly
        Assert.assertTrue(result);
        Assert.assertNotNull(treeBuilder.currentToken);
        Assert.assertEquals(Token.TokenType.StartTag, treeBuilder.currentToken.type);
        Assert.assertEquals(tagName, treeBuilder.currentToken.name());
        Assert.assertEquals(attrs, treeBuilder.currentToken.attributes());
    }

    @Test
    public void testProcessEndTag() {
        String tagName = "div";
        boolean result = treeBuilder.processEndTag(tagName);

        // Check that the end tag is processed correctly
        Assert.assertTrue(result);
        Assert.assertNotNull(treeBuilder.currentToken);
        Assert.assertEquals(Token.TokenType.EndTag, treeBuilder.currentToken.type);
        Assert.assertEquals(tagName, treeBuilder.currentToken.name());
    }

    @Test
    public void testCurrentElement() {
        Element element = new Element("div");
        treeBuilder.stack.add(element);

        Element currentElement = treeBuilder.currentElement();

        // Check that the current element is correctly retrieved from the stack
        Assert.assertNotNull(currentElement);
        Assert.assertSame(element, currentElement);
    }
}

