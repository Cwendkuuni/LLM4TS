
import org.jsoup.select.*;
import org.junit.Test;
import java.util.regex.Pattern;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div.class";
        Evaluator parsedQuery = QueryParser.parse(query);
        assertNotNull(parsedQuery);
    }

    @Test
    public void testComma() {
        String query = ".class, #id";
        Evaluator parsedQuery = QueryParser.parse(query);
        assertNotNull(parsedQuery);
    }

    @Test
    public void testCombSpace() {
        String query = "div.class p";
        Evaluator parsedQuery = QueryParser.parse(query);
        assertNotNull(parsedQuery);
    }

    @Test
    public void testCombPlus() {
        String query = "div.class + p";
        Evaluator parsedQuery = QueryParser.parse(query);
        assertNotNull(parsedQuery);
    }

    @Test
    public void testCombTilde() {
        String query = "div.class ~ p";
        Evaluator parsedQuery = QueryParser.parse(query);
        assertNotNull(parsedQuery);
    }

    @Test(expected = SelectorParseException.class)
    public void testUnknownCombinator() {
        String query = "div.class @ p";
        QueryParser.parse(query);
    }

    @Test
    public void testId() {
        String query = "#id";
        Evaluator parsedQuery = QueryParser.parse(query);
        assertNotNull(parsedQuery);
    }

    @Test
    public void testClass() {
        String query = ".class";
        Evaluator parsedQuery = QueryParser.parse(query);
        assertNotNull(parsedQuery);
    }

    @Test
    public void testTag() {
        String query = "div";
        Evaluator parsedQuery = QueryParser.parse(query);
        assertNotNull(parsedQuery);
    }

    @Test
    public void testAttribute() {
        String query = "[href]";
        Evaluator parsedQuery = QueryParser.parse(query);
        assertNotNull(parsedQuery);
    }

    @Test
    public void testAttributeWithValue() {
        String query = "[class=value]";
        Evaluator parsedQuery = QueryParser.parse(query);
        assertNotNull(parsedQuery);
    }

    @Test
    public void testAttributeWithValueNot() {
        String query = "[class!=value]";
        Evaluator parsedQuery = QueryParser.parse(query);
        assertNotNull(parsedQuery);
    }

    @Test
    public void testAttributeWithValueStarting() {
        String query = "[class^=value]";
        Evaluator parsedQuery = QueryParser.parse(query);
        assertNotNull(parsedQuery);
    }

    @Test
    public void testAttributeWithValueEnding() {
        String query = "[class$=value]";
        Evaluator parsedQuery = QueryParser.parse(query);
        assertNotNull(parsedQuery);
    }

    @Test
    public void testAttributeWithValueContaining() {
        String query = "[class*=value]";
        Evaluator parsedQuery = QueryParser.parse(query);
        assertNotNull(parsedQuery);
    }

    @Test
    public void testAttributeWithValueMatching() {
        String query = "[class~=value]";
        Evaluator parsedQuery = QueryParser.parse(query);
        assertNotNull(parsedQuery);
    }

    @Test(expected = SelectorParseException.class)
    public void testUnknownAttributeQuery() {
        String query = "[class]";
        QueryParser.parse(query);
    }

    @Test
    public void testAllElements() {
        String query = "*";
        Evaluator parsedQuery = QueryParser.parse(query);
        assertNotNull(parsedQuery);
    }

    @Test
    public void testIndexLessThan() {
        String query = ":lt(5)";
        Evaluator parsedQuery = QueryParser.parse(query);
        assertNotNull(parsedQuery);
    }

    @Test
    public void testIndexGreaterThan() {
        String query = ":gt(5)";
        Evaluator parsedQuery = QueryParser.parse(query);
        assertNotNull(parsedQuery);
    }

    @Test
    public void testIndexEquals() {
        String query = ":eq(5)";
        Evaluator parsedQuery = QueryParser.parse(query);
        assertNotNull(parsedQuery);
    }

    @Test
    public void testHas() {
        String query = ":has(div)";
        Evaluator parsedQuery = QueryParser.parse(query);
        assertNotNull(parsedQuery);
    }

    @Test
    public void testContains() {
        String query = ":contains(text)";
        Evaluator parsedQuery = QueryParser.parse(query);
        assertNotNull(parsedQuery);
    }

    @Test
    public void testContainsOwn() {
        String query = ":containsOwn(text)";
        Evaluator parsedQuery = QueryParser.parse(query);
        assertNotNull(parsedQuery);
    }

    @Test
    public void testMatches() {
        String query = ":matches(regex)";
        Evaluator parsedQuery = QueryParser.parse(query);
        assertNotNull(parsedQuery);
    }

    @Test
    public void testMatchesOwn() {
        String query = ":matchesOwn(regex)";
        Evaluator parsedQuery = QueryParser.parse(query);
        assertNotNull(parsedQuery);
    }

    @Test
    public void testNot() {
        String query = ":not(div)";
        Evaluator parsedQuery = QueryParser.parse(query);
        assertNotNull(parsedQuery);
    }

    @Test(expected = SelectorParseException.class)
    public void testEmptyNotSelector() {
        String query = ":not()";
        QueryParser.parse(query);
    }

}
