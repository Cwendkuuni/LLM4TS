import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.parser.QueryParser;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.helper.TokenQueue;
import org.jsoup.helper.Selector;

public class QueryParserTest {

    @Test
    public void testParseSimpleSelector() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        List<Selector.Element> elements = Selector.select("div", Document.getByHtml("<div></div>"));

        assertTrue(elements.size() > 0);
        assertTrue(evaluator.matches(elements.get(0)));
    }

    @Test
    public void testParseIDSelector() {
        String query = "#myId";
        Evaluator evaluator = QueryParser.parse(query);
        List<Selector.Element> elements = Selector.select("#myId", Document.getByHtml("<div id='myId'></div>"));

        assertTrue(elements.size() == 1);
        assertTrue(evaluator.matches(elements.get(0)));
    }

    @Test
    public void testParseClassSelector() {
        String query = ".myClass";
        Evaluator evaluator = QueryParser.parse(query);
        List<Selector.Element> elements = Selector.select(".myClass", Document.getByHtml("<div class='myClass'></div>"));

        assertTrue(elements.size() > 0);
        assertTrue(evaluator.matches(elements.get(0)));
    }

    @Test
    public void testParseElementSelector() {
        String query = "p";
        Evaluator evaluator = QueryParser.parse(query);
        List<Selector.Element> elements = Selector.select("p", Document.getByHtml("<p></p>"));

        assertTrue(elements.size() == 1);
        assertTrue(evaluator.matches(elements.get(0)));
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "[href]";
        Evaluator evaluator = QueryParser.parse(query);
        List<Selector.Element> elements = Selector.select("a[href]", Document.getByHtml("<a href='https://example.com'>Link</a>"));

        assertTrue(elements.size() == 1);
        assertTrue(evaluator.matches(elements.get(0)));
    }

    @Test
    public void testParseAttributeSelectorWithValue() {
        String query = "[href='https://example.com']";
        Evaluator evaluator = QueryParser.parse(query);
        List<Selector.Element> elements = Selector.select("a[href='https://example.com']", Document.getByHtml("<a href='https://example.com'>Link</a>"));

        assertTrue(elements.size() == 1);
        assertTrue(evaluator.matches(elements.get(0)));
    }

    @Test
    public void testParseCombinatorSelector() {
        String query = "div > p";
        Evaluator evaluator = QueryParser.parse(query);
        List<Selector.Element> elements = Selector.select("div > p", Document.getByHtml("<div><p></p></div>"));

        assertTrue(elements.size() == 1);
        assertTrue(evaluator.matches(elements.get(0)));
    }

    @Test
    public void testParseCombinatorSelectorWithMultipleElements() {
        String query = "div > p > span";
        Evaluator evaluator = QueryParser.parse(query);
        List<Selector.Element> elements = Selector.select("div > p > span", Document.getByHtml("<div><p><span></span></p></div>"));

        assertTrue(elements.size() == 1);
        assertTrue(evaluator.matches(elements.get(0)));
    }

    @Test
    public void testParseComboinatorSelectorWithAdjacentSibling() {
        String query = "p + span";
        Evaluator evaluator = QueryParser.parse(query);
        List<Selector.Element> elements = Selector.select("p + span", Document.getByHtml("<p></p><span></span>"));

        assertTrue(elements.size() == 1);
        assertTrue(evaluator.matches(elements.get(0)));
    }

    @Test
    public void testParseCombinatorSelectorWithGeneralSibling() {
        String query = "p ~ span";
        Evaluator evaluator = QueryParser.parse(query);
        List<Selector.Element> elements = Selector.select("p ~ span", Document.getByHtml("<p></p><span></span>"));

        assertTrue(elements.size() > 1);
        assertTrue(evaluator.matches(elements.get(1)));
    }

    @Test
    public void testParseCombinatorSelectorWithNegativeComboinator() {
        String query = "div:not(.box)";
        Evaluator evaluator = QueryParser.parse(query);
        List<Selector.Element> elements = Selector.select("div.box", Document.getByHtml("<div class='box'></div>"));

        List<Selector.Element> notElements = Selector.select("div:not(.box)", Document.getByHtml("<div></div><div></div><div class='box'></div>"));

        assertTrue(elements.size() == 1);
        assertFalse(evaluator.matches(elements.get(0)));

        assertTrue(notElements.size() > 1);
        assertTrue(evaluator.matches(notElements.get(1)));
    }

    @Test(expected = org.jsoup.parser.Selector.SelectorParseException.class)
    public void testParseInvalidSelector() {
        String query = "div^p";
        QueryParser.parse(query);
    }

    @Test(expected = org.jsoup.parser.Selector.SelectorParseException.class)
    public void testParseInvalidAttributeSelector() {
        String query = "[h=ref]";
        QueryParser.parse(query);
    }
}