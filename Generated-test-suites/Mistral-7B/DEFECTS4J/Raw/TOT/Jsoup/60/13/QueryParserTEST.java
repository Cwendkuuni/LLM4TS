import org.jsoup.parser.QueryParser;
import org.jsoup.parser.Selector;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class QueryParserTest {

    @Test
    public void testParseSimpleSelector() {
        String query = "div.red";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Selector> expectedSelectors = Arrays.asList(
                new Selector("div"),
                new SelectorClass("red")
        );

        Assert.assertEquals(expectedSelectors.toString(), evaluator.toStringTree());
    }

    @Test
    public void testParseComplexSelectorWithCombinators() {
        String query = "div > p:first-child";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Selector> expectedSelectors = Arrays.asList(
                new Selector("div"),
                new StructuralEvaluator.ImmediateChild(new SelectorTagName("p")),
                new Evaluator.IsFirstChild()
        );

        Assert.assertEquals(expectedSelectors.toString(), evaluator.toStringTree());
    }

    @Test
    public void testParseComplexSelectorWithAttributes() {
        String query = "input[type='text'][value='email']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Selector> expectedSelectors = Arrays.asList(
                new Selector("input"),
                new AttributeEvaluator("type", "text"),
                new AttributeEvaluator("value", "email")
        );

        Assert.assertEquals(expectedSelectors.toString(), evaluator.toStringTree());
    }

    @Test
    public void testParseComplexSelectorWithPseudoClasses() {
        String query = "li:last-child";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Selector> expectedSelectors = Arrays.asList(
                new Selector("li"),
                new Evaluator.IsLastChild()
        );

        Assert.assertEquals(expectedSelectors.toString(), evaluator.toStringTree());
    }

    @Test
    public void testParseComplexSelectorWithNegation() {
        String query = "p:not(.error)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Selector> expectedSelectors = Arrays.asList(
                new Selector("p"),
                new StructuralEvaluator.Not(new SelectorClass("error"))
        );

        Assert.assertEquals(expectedSelectors.toString(), evaluator.toStringTree());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testThrowsExceptionForInvalidSelector() {
        String query = "div[class=red]:not(p):nth-child(-1)";
        new QueryParser(query).parse();
    }
}