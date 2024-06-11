import static org.junit.Assert.*;
import org.junit.Test;

public class TreeBuilderTest {

    @Test
    public void testParse() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        String baseUri = "http://example.com";
        TreeBuilder treeBuilder = new TreeBuilder();
        Document actual = treeBuilder.parse(input, baseUri);
        assertEquals("<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>", actual.html());
    }

    @Test
    public void testParseWithErrors() {
        String input = "<html><head><title>Test<title></head><body><h1>Hello</h1></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        TreeBuilder treeBuilder = new TreeBuilder();
        Document actual = treeBuilder.parse(input, baseUri, errors);
        assertEquals("<html><head><title>Test<title></title></head><body><h1>Hello</h1></body></html>", actual.html());
        assertTrue(errors.hasErrors());
    }

    @Test
    public void testProcessStartTag() {
        String name = "div";
        TreeBuilder treeBuilder = new TreeBuilder();
        assertTrue(treeBuilder.processStartTag(name));
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        String name = "img";
        Attributes attrs = new Attributes();
        attrs.put("src", "image.png");
        TreeBuilder treeBuilder = new TreeBuilder();
        assertTrue(treeBuilder.processStartTag(name, attrs));
    }

    @Test
    public void testProcessEndTag() {
        String name = "div";
        TreeBuilder treeBuilder = new TreeBuilder();
        assertTrue(treeBuilder.processEndTag(name));
    }

    @Test
    public void testCurrentElement() {
        Element element = new Element("div");
        TreeBuilder treeBuilder = new TreeBuilder();
        treeBuilder.stack.add(element);
        assertEquals(element, treeBuilder.currentElement());
    }
}