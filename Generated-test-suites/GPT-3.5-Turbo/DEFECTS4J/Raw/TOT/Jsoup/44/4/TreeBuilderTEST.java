import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;

@RunWith(JUnit4.class)
public class TreeBuilderTest {

    private TreeBuilder treeBuilder;
    private ParseErrorList errors;

    @Before
    public void setup() {
        treeBuilder = new TreeBuilderImpl();
        errors = new ParseErrorList();
    }

    @Test
    public void parse_ShouldReturnParsedDocument_WhenValidInputIsProvided() {
        String input = "<html><head></head><body></body></html>";
        String baseUri = "https://www.example.com";
        Document document = treeBuilder.parse(input, baseUri, errors);

        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
        // Check if document contains html, head, and body elements
        Assert.assertNotNull(document.getElementsByTag("html"));
        Assert.assertNotNull(document.getElementsByTag("head"));
        Assert.assertNotNull(document.getElementsByTag("body"));
    }

    @Test
    public void process_ShouldReturnTrue_WhenStartTagTokenIsProcessed() {
        String tagName = "div";
        Token token = new Token.StartTag().name(tagName);
        boolean result = treeBuilder.process(token);
        Assert.assertTrue(result);
        
        // Check if the currentElement is updated correctly
        Element currentElement = treeBuilder.currentElement();
        Assert.assertNotNull(currentElement);
        Assert.assertEquals(tagName, currentElement.tagName());
    }

    @Test
    public void process_ShouldReturnTrue_WhenStartTagTokenWithAttributesIsProcessed() {
        String tagName = "a";
        Attributes attrs = new Attributes();
        attrs.put("href", "https://www.example.com");
        attrs.put("target", "_blank");
        Token token = new Token.StartTag().nameAttr(tagName, attrs);
        boolean result = treeBuilder.process(token);
        Assert.assertTrue(result);
        
        // Check if the currentElement is updated correctly
        Element currentElement = treeBuilder.currentElement();
        Assert.assertNotNull(currentElement);
        Assert.assertEquals(tagName, currentElement.tagName());
        Assert.assertEquals(attrs, currentElement.attributes());
    }

    @Test
    public void process_ShouldReturnTrue_WhenEndTagTokenIsProcessed() {
        String tagName = "div";
        Token token = new Token.EndTag().name(tagName);
        boolean result = treeBuilder.process(token);
        Assert.assertTrue(result);
    }

    @Test
    public void process_ShouldReturnFalse_WhenInvalidTokenIsProcessed() {
        Token token = new Token.Doctype();
        boolean result = treeBuilder.process(token);
        Assert.assertFalse(result);
    }

}