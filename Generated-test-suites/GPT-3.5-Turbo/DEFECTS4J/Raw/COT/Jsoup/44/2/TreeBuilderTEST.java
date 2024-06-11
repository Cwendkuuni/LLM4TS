
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class TreeBuilderTest {

    private TreeBuilder treeBuilder;

    @Before
    public void setup() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                // Do nothing for test purposes
                return false;
            }
        };
    }

    @Test
    public void testParse() {
        String input = "<html><head><title>Test</title></head><body><p>Hello, world!</p></body></html>";
        String baseUri = "http://example.com";

        Document document = treeBuilder.parse(input, baseUri);

        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertEquals("html", document.tagName());

        Element headElement = document.head();
        Assert.assertEquals("head", headElement.tagName());

        Element titleElement = document.head().selectFirst("title");
        Assert.assertEquals("title", titleElement.tagName());
        Assert.assertEquals("Test", titleElement.text());

        Element bodyElement = document.body();
        Assert.assertEquals("body", bodyElement.tagName());

        Element pElement = document.body().selectFirst("p");
        Assert.assertEquals("p", pElement.tagName());
        Assert.assertEquals("Hello, world!", pElement.text());
    }

    @Test
    public void testParseWithErrors() {
        String input = "<html><head><title>Test</title></head><body><p>Hello, world!</p>";
        String baseUri = "http://example.com";

        ParseErrorList errors = new ParseErrorList();
        Document document = treeBuilder.parse(input, baseUri, errors);

        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertEquals("html", document.tagName());

        Element headElement = document.head();
        Assert.assertEquals("head", headElement.tagName());

        Element titleElement = document.head().selectFirst("title");
        Assert.assertEquals("title", titleElement.tagName());
        Assert.assertEquals("Test", titleElement.text());

        Element bodyElement = document.body();
        Assert.assertEquals("body", bodyElement.tagName());

        Element pElement = document.body().selectFirst("p");
        Assert.assertEquals("p", pElement.tagName());
        Assert.assertEquals("Hello, world!", pElement.text());

        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unexpected end tag (p)", errors.get(0).getError());
        Assert.assertEquals(1, errors.get(0).getPosition());
    }

    @Test
    public void testProcessStartTag() {
        treeBuilder.stack = new ArrayList<>();
        treeBuilder.stack.add(new Element("div"));

        boolean result = treeBuilder.processStartTag("p");

        Assert.assertTrue(result);
        Assert.assertEquals(2, treeBuilder.stack.size());
        Assert.assertEquals("p", treeBuilder.stack.get(1).tagName());
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        treeBuilder.stack = new ArrayList<>();
        treeBuilder.stack.add(new Element("div"));

        Attributes attrs = new Attributes();
        attrs.put("id", "testId");

        boolean result = treeBuilder.processStartTag("p", attrs);

        Assert.assertTrue(result);
        Assert.assertEquals(2, treeBuilder.stack.size());
        Assert.assertEquals("p", treeBuilder.stack.get(1).tagName());

        Element pElement = treeBuilder.stack.get(1);
        Assert.assertEquals("testId", pElement.attr("id"));
    }

    @Test
    public void testProcessEndTag() {
        treeBuilder.stack = new ArrayList<>();
        treeBuilder.stack.add(new Element("div"));
        treeBuilder.stack.add(new Element("p"));

        boolean result = treeBuilder.processEndTag("p");

        Assert.assertTrue(result);
        Assert.assertEquals(1, treeBuilder.stack.size());
        Assert.assertEquals("div", treeBuilder.stack.get(0).tagName());
    }

    @Test
    public void testCurrentElement() {
        treeBuilder.stack = new ArrayList<>();
        treeBuilder.stack.add(new Element("div"));
        treeBuilder.stack.add(new Element("p"));

        Element currentElement = treeBuilder.currentElement();

        Assert.assertEquals("p", currentElement.tagName());
    }
}
