import org.junit.Test;
import org.junit.Assert;
import org.jsoup.select.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        // Valid CSS query
        String query = "div#id.class[attr='value']";
        Evaluator expected = new Evaluator.And(
            new Evaluator.Tag("div"),
            new Evaluator.Id("id"),
            new Evaluator.Class("class"),
            new Evaluator.AttributeWithValue("attr", "value")
        );
        Evaluator actual = QueryParser.parse(query);
        Assert.assertEquals(expected, actual);

        // Invalid CSS query
        query = "div[id='value'"; // Missing closing bracket
        try {
            actual = QueryParser.parse(query);
            Assert.fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            // Pass
        }
    }

    @Test
    public void testParse_combinator() {
        // Valid combinator
        String query = "div > span";
        Evaluator expected = new Evaluator.And(
            new StructuralEvaluator.Parent(new Evaluator.Tag("span")),
            new Evaluator.Tag("div")
        );
        Evaluator actual = QueryParser.parse(query);
        Assert.assertEquals(expected, actual);

        // Invalid combinator
        query = "div & span";
        try {
            actual = QueryParser.parse(query);
            Assert.fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            // Pass
        }
    }

    @Test
    public void testParse_subQuery() {
        // Valid subQuery
        String subQuery = "#id.class[attr='value']";
        String expected = "#id.class[attr='value']";
        String actual = consumeSubQuery(subQuery);
        Assert.assertEquals(expected, actual);

        // Invalid subQuery
        subQuery = "#id.class[attr='value'"; // Missing closing bracket
        try {
            actual = consumeSubQuery(subQuery);
            Assert.fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            // Pass
        }
    }

    private String consumeSubQuery(String subQuery) {
        QueryParser tester = new QueryParser(subQuery);
        return tester.consumeSubQuery();
    }

    // Add more test cases for each method in QueryParser class

}