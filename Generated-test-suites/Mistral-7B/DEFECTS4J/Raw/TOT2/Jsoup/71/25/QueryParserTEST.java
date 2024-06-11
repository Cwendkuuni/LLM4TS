import org.jsoup.parser.QueryParser;
import org.jsoup.parser.Selector;
import org.jsoup.parser.TokenQueue;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class QueryParserTest {

    @Test
    public void testParseSimpleSelector() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        List<Selector> expectedSelectors = new ArrayList<>();
        expectedSelectors.add(new Selector("div"));
        Assert.assertEquals(expectedSelectors, evaluator.getSelectors());
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "input[type='text']";
        Evaluator evaluator = QueryParser.parse(query);
        List<Selector> expectedSelectors = new ArrayList<>();
        expectedSelectors.add(new Selector("input").withAttribute("type", "text"));
        Assert.assertEquals(expectedSelectors, evaluator.getSelectors());
    }

    @Test
    public void testParseClassSelector() {
        String query = ".button";
        Evaluator evaluator = QueryParser.parse(query);
        List<Selector> expectedSelectors = new ArrayList<>();
        expectedSelectors.add(new Selector("*").withClass("button"));
        Assert.assertEquals(expectedSelectors, evaluator.getSelectors());
    }

    @Test
    public void testParseIdSelector() {
        String query = "#header";
        Evaluator evaluator = QueryParser.parse(query);
        List<Selector> expectedSelectors = new ArrayList<>();
        expectedSelectors.add(new Selector("#header"));
        Assert.assertEquals(expectedSelectors, evaluator.getSelectors());
    }

    @Test
    public void testParseNegativeAttributeSelector() {
        String query = "input:not([type='text'])";
        Evaluator evaluator = QueryParser.parse(query);
        List<Selector> expectedSelectors = new ArrayList<>();
        expectedSelectors.add(new StructuralEvaluator.Not(new Selector("input").withAttribute("type", "text")));
        Assert.assertEquals(expectedSelectors, evaluator.getSelectors());
    }

    @Test
    public void testParseCombinedSelectors() {
        String query = "div.button > input:not([type='button'])";
        Evaluator evaluator = QueryParser.parse(query);
        List<Selector> expectedSelectors = new ArrayList<>();
        Selector divButton = new Selector("div").withClass("button");
        expectedSelectors.add(new StructuralEvaluator.And(
                new StructuralEvaluator.Descendant(divButton),
                new StructuralEvaluator.Not(new Selector("input").withAttribute("type", "button")))
        );
        Assert.assertEquals(expectedSelectors, evaluator.getSelectors());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidSelector() {
        String query = "div*#header";
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidCombinedSelector() {
        String query = "div.button > input~ input:not([type='button'])";
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseUnknownCombinator() {
        String query = "div.button > ^ input:not([type='button'])";
    }
}