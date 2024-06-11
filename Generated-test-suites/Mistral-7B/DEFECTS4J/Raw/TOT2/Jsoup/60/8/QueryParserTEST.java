import org.jsoup.parser.QueryParser;
import org.jsoup.parser.Selector;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class QueryParserTest {

    @Test
    public void testParseSimpleSelector() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        Selector selector = evaluator.compile();
        List<Element> elements = Elements.emptyList(); // replace with actual elements or mocks
        List<Element> result = selector.select(elements);
        Assert.assertEquals(1, result.size());
    }

    @Test
    public void testParseClassSelector() {
        String query = ".button";
        Evaluator evaluator = QueryParser.parse(query);
        Selector selector = evaluator.compile();
        List<Element> elements = Elements.list(new Element("div", "class", "button"));
        List<Element> result = selector.select(elements);
        Assert.assertEquals(1, result.size());
    }

    @Test
    public void testParseIdSelector() {
        String query = "#header";
        Evaluator evaluator = QueryParser.parse(query);
        Selector selector = evaluator.compile();
        List<Element> elements = Elements.list(new Element("div", "id", "header"));
        List<Element> result = selector.select(elements);
        Assert.assertEquals(1, result.size());
    }

    @Test
    public void testParseTagSelector() {
        String query = "p";
        Evaluator evaluator = QueryParser.parse(query);
        Selector selector = evaluator.compile();
        List<Element> elements = Elements.list(new Element("p"), new Element("span"), new Element("div"));
        List<Element> result = selector.select(elements);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("p", result.get(0).tagName());
    }

    @Test
    public void testParseCombinedSelectors() {
        String query = "div.button";
        Evaluator evaluator = QueryParser.parse(query);
        Selector selector = evaluator.compile();
        List<Element> elements = Elements.list(new Element("div", "class", "button"), new Element("div", "class", "button-secondary"), new Element("p", "class", "button"));
        List<Element> result = selector.select(elements);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("div", result.get(0).tagName());
        Assert.assertEquals("button", result.get(0).classes().get(0));
    }

    @Test
    public void testParseDescendantSelectors() {
        String query = "div > p";
        Evaluator evaluator = QueryParser.parse(query);
        Selector selector = evaluator.compile();
        List<Element> elements = Elements.list(new Element("div", null, null, new Element("p")), new Element("div", null, null, new Element("span")), new Element("p", null, null));
        List<Element> result = selector.select(elements);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("div > p", selector.toString());
    }

    @Test
    public void testParseAttributeSelectors() {
        String query = "input[type='text']";
        Evaluator evaluator = QueryParser.parse(query);
        Selector selector = evaluator.compile();
        List<Element> elements = Elements.list(new Element("input", "tagName", "input", "type", "text"), new Element("button", "tagName", "button", "type", "text"), new Element("div", "tagName", "div", null, null));
        List<Element> result = selector.select(elements);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("input[type='text']", selector.toString());
    }

    @Test
    public void testParsePseudoClassSelectors() {
        String query = "p:first-child";
        Evaluator evaluator = QueryParser.parse(query);
        Selector selector = evaluator.compile();
        List<Element> elements = Elements.list(new Element("p", null, null, new Element("div")), new Element("p", null, null, new Element("p")), new Element("p", null, null, new Element("p")));
        List<Element> result = selector.select(elements);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("p:first-child", selector.toString());
    }

    @Test
    public void testParsePseudoElementSelectors() {
        String query = "ul > li:first-child > a:link";
        Evaluator evaluator = QueryParser.parse(query);
        Selector selector = evaluator.compile();
        List<Element> elements = Elements.list(new Element("ul", null, null, new Element("li", null, null, new Element("a", "tagName", "a", "href", "https://example.com"))),
                new Element("ul", null, null, new Element("li", null, null, new Element("span"))),
                new Element("ul", null, null, new Element("li", null, null, new Element("a", "tagName", "a", "href", "https://google.com"))));
        List<Element> result = selector.select(elements);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("ul > li:first-child > a:link", selector.toString());
    }

    @Test
    public void testParseNegatedSelectors() {
        String query = "div:not(.button)";
        Evaluator evaluator = QueryParser.parse(query);
        Selector selector = evaluator.compile();
        List<Element> elements = Elements.list(new Element("div", "class", "button"), new Element("div", "class", "button-primary"), new Element("div", null, null));
        List<Element> result = selector.select(elements);
        Assert.assertEquals(2, result.size());
        Assert.assertFalse(result.get(0).classes().contains("button"));
        Assert.assertEquals("div:not(.button)", selector.toString());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidSelector() {
        String query = "div.buttonx";
        QueryParser.parse(query);
    }

	// Test for edge cases, error scenarios, and other complex selectors goes here

    @Test
    public void testParseComplexSelectors() {
        String query = "ul > li > a[href$='.pdf']:not(.disabled)";
        Evaluator evaluator = QueryParser.parse(query);
        Selector selector = evaluator.compile();
        List<Element> elements = Elements.list(new Element("ul", null, null, new Element("li", null, null, new Element("a", "tagName", "a", "href", "https://example.com/document.pdf"), new Attribute("class", "disabled"))),
                new Element("ul", null, null, new Element("li", null, null, new Element("a", "tagName", "a", "href", "https://example.com/document.pdf"))),
                new Element("ul", null, null, new Element("li", null, null, new Element("a", "tagName", "a", "href", "https://example.com/document.docx"))),
                new Element("ul", null, null, new Element("li", null, null, new Element("div"))));
        List<Element> result = selector.select(elements);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("ul > li > a[href$='.pdf']:not(.disabled)", selector.toString());
    }

    @Test
    public void testParseComplexCombinedSelectors() {
        String query = "div.button:not(.disabled) > ul > li > a[href$='.pdf']";
        Evaluator evaluator = QueryParser.parse(query);
        Selector selector = evaluator.compile();
        List<Element> elements = Elements.list(new Element("div", "class", "button", "class", "disabled"),
                new Element("div", "class", "button", null),
                new Element("div", "class", "button-disabled", null),
                new Element("ul", null, null, new Element("li", null, null, new Element("a", "tagName", "a", "href", "https://example.com/document.pdf"))),
                new Element("ul", null, null, new Element("li", null, null, new Element("a", "tagName", "a", "href", "https://example.com/document.pdf"))),
                new Element("ul", null, null, new Element("li", null, null, new Element("div"))),
                new Element("ul", null, null, new Element("li", null, null, new Element("a", "tagName", "a", "href", "https://example.com/document.docx"))));
        List<Element> result = selector.select(elements);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("div.button:not(.disabled) > ul > li > a[href$='.pdf']", selector.toString());
    }
}