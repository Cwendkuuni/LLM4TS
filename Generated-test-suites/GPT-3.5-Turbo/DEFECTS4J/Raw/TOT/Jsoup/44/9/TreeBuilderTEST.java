import org.junit.Test;
import org.junit.Assert;
import org.jsoup.parser.CharacterReader;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TreeBuilder;

import java.util.ArrayList;

public class TreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        TreeBuilder treeBuilder = new TreeBuilderStub();
        treeBuilder.initialiseParse("input", "baseUri", null);
        Document doc = treeBuilder.doc;
        ArrayList<Element> stack = treeBuilder.stack;
        Assert.assertNotNull(doc);
        Assert.assertEquals("baseUri", treeBuilder.baseUri);
        Assert.assertEquals("input", treeBuilder.reader.getInput());
        Assert.assertEquals(32, stack.size());
    }

    @Test
    public void testParseWithNoErrors() {
        TreeBuilder treeBuilder = new TreeBuilderStub();
        Document doc = treeBuilder.parse("input", "baseUri");
        Assert.assertNotNull(doc);
        Assert.assertEquals("baseUri", treeBuilder.baseUri);
        Assert.assertEquals("input", treeBuilder.reader.getInput());
    }

    @Test
    public void testParseWithErrors() {
        TreeBuilder treeBuilder = new TreeBuilderStub();
        ParseErrorList errors = new ParseErrorList();
        Document doc = treeBuilder.parse("input", "baseUri", errors);
        Assert.assertNotNull(doc);
        Assert.assertEquals("baseUri", treeBuilder.baseUri);
        Assert.assertEquals("input", treeBuilder.reader.getInput());
        Assert.assertEquals(errors, treeBuilder.errors);
    }

    @Test
    public void testRunParserWithEOFToken() {
        TreeBuilder treeBuilder = new TreeBuilderStub();
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), null);
        treeBuilder.tokeniser = tokeniser;
        treeBuilder.runParser();
        Assert.assertEquals(Token.TokenType.EOF, tokeniser.read().type);
    }

    @Test
    public void testProcessStartTag() {
        TreeBuilder treeBuilder = new TreeBuilderStub();
        Assert.assertTrue(treeBuilder.processStartTag("test"));
        Assert.assertEquals("test", treeBuilder.start.name());
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        TreeBuilder treeBuilder = new TreeBuilderStub();
        Attributes attrs = new Attributes();
        attrs.put("attr1", "value1");
        attrs.put("attr2", "value2");
        Assert.assertTrue(treeBuilder.processStartTag("test", attrs));
        Assert.assertEquals("test", treeBuilder.start.name());
        Assert.assertEquals(attrs, treeBuilder.start.getAttributes());
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder treeBuilder = new TreeBuilderStub();
        Assert.assertTrue(treeBuilder.processEndTag("test"));
        Assert.assertEquals("test", treeBuilder.end.name());
    }

    @Test
    public void testCurrentElement() {
        TreeBuilder treeBuilder = new TreeBuilderStub();
        ArrayList<Element> stack = treeBuilder.stack;
        Element element = new Element("test");
        stack.add(element);
        Assert.assertEquals(element, treeBuilder.currentElement());
    }
}

class TreeBuilderStub extends TreeBuilder {
    @Override
    protected boolean process(Token token) {
        // Implementation not needed for testing
        return true;
    }
}