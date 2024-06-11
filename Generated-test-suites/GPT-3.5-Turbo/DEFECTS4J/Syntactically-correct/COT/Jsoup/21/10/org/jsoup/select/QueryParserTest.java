import org.jsoup.select.*;
import org.junit.Assert;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div .class[attr=value]";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_emptyQuery() {
        String query = "";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_combinators() {
        String query = "div > .class + p ~ span";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_multipleCombinators() {
        String query = "div > .class + p, span ~ p";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_has() {
        String query = "div:has(p)";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_contains() {
        String query = "div:contains(text)";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_containsOwn() {
        String query = "div:containsOwn(text)";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_matches() {
        String query = "div:matches(regex)";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_matchesOwn() {
        String query = "div:matchesOwn(regex)";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_not() {
        String query = "div:not(p)";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_indexLessThan() {
        String query = "div:lt(5)";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_indexGreaterThan() {
        String query = "div:gt(5)";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_indexEquals() {
        String query = "div:eq(5)";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_attribute() {
        String query = "div[attr=value]";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_attributeStarting() {
        String query = "div[^attr]";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_attributeWithValue() {
        String query = "div[attr=value]";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_attributeWithValueNot() {
        String query = "div[attr!=value]";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_attributeWithValueStarting() {
        String query = "div[attr^=value]";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_attributeWithValueEnding() {
        String query = "div[attr$=value]";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_attributeWithValueContaining() {
        String query = "div[attr*=value]";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_attributeWithValueMatching() {
        String query = "div[attr~=regex]";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_id() {
        String query = "#id";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_class() {
        String query = ".class";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_tag() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_allElements() {
        String query = "*";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_invalidQuery() {
        String query = "invalid query";
        QueryParser.parse(query);
    }

}