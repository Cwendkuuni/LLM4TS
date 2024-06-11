import org.jsoup.select.Evaluator;
import org.jsoup.select.QueryParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse("div.class");
        assertNotNull(evaluator);
    }

    @Test
    public void testCombinator() {
        // Test combinator '>' for immediate children
        Evaluator evaluator = QueryParser.parse("div > span");
        assertNotNull(evaluator);

        // Test combinator ' ' for descendant elements
        evaluator = QueryParser.parse("div span");
        assertNotNull(evaluator);

        // Test combinator '+' for adjacent siblings
        evaluator = QueryParser.parse("div + p");
        assertNotNull(evaluator);

        // Test combinator '~' for general siblings
        evaluator = QueryParser.parse("div ~ p");
        assertNotNull(evaluator);

        // Test combinator ',' for grouping
        evaluator = QueryParser.parse("div, p");
        assertNotNull(evaluator);
    }

    @Test
    public void testById() {
        Evaluator evaluator = QueryParser.parse("#testId");
        assertNotNull(evaluator);
    }

    @Test
    public void testByClass() {
        Evaluator evaluator = QueryParser.parse(".testClass");
        assertNotNull(evaluator);
    }

    @Test
    public void testByTag() {
        Evaluator evaluator = QueryParser.parse("span");
        assertNotNull(evaluator);
    }

    @Test
    public void testByAttribute() {
        Evaluator evaluator = QueryParser.parse("[name=value]");
        assertNotNull(evaluator);
    }

    @Test
    public void testAllElements() {
        Evaluator evaluator = QueryParser.parse("*");
        assertNotNull(evaluator);
    }

    @Test
    public void testIndexLessThan() {
        Evaluator evaluator = QueryParser.parse(":lt(3)");
        assertNotNull(evaluator);
    }

    @Test
    public void testIndexGreaterThan() {
        Evaluator evaluator = QueryParser.parse(":gt(1)");
        assertNotNull(evaluator);
    }

    @Test
    public void testIndexEquals() {
        Evaluator evaluator = QueryParser.parse(":eq(2)");
        assertNotNull(evaluator);
    }

    @Test
    public void testNthChild() {
        Evaluator evaluator = QueryParser.parse(":nth-child(2n+1)");
        assertNotNull(evaluator);
    }

    @Test
    public void testFirstAndLastChild() {
        Evaluator evaluator = QueryParser.parse(":first-child");
        assertNotNull(evaluator);

        evaluator = QueryParser.parse(":last-child");
        assertNotNull(evaluator);
    }

    @Test
    public void testFirstAndLastOfType() {
        Evaluator evaluator = QueryParser.parse(":first-of-type");
        assertNotNull(evaluator);

        evaluator = QueryParser.parse(":last-of-type");
        assertNotNull(evaluator);
    }

    @Test
    public void testOnlyChildAndOfType() {
        Evaluator evaluator = QueryParser.parse(":only-child");
        assertNotNull(evaluator);

        evaluator = QueryParser.parse(":only-of-type");
        assertNotNull(evaluator);
    }

    @Test
    public void testEmpty() {
        Evaluator evaluator = QueryParser.parse(":empty");
        assertNotNull(evaluator);
    }

    @Test
    public void testRoot() {
        Evaluator evaluator = QueryParser.parse(":root");
        assertNotNull(evaluator);
    }

    @Test
    public void testHas() {
        Evaluator evaluator = QueryParser.parse(":has(p)");
        assertNotNull(evaluator);
    }
    
    @Test
    public void testContains() {
        Evaluator evaluator = QueryParser.parse(":contains('text')");
        assertNotNull(evaluator);

        evaluator = QueryParser.parse(":containsOwn('text')");
        assertNotNull(evaluator);
    }

    @Test
    public void testContainsData() {
        Evaluator evaluator = QueryParser.parse(":containsData('data')");
        assertNotNull(evaluator);
    }

    @Test
    public void testMatches() {
        Evaluator evaluator = QueryParser.parse(":matches(regex)");
        assertNotNull(evaluator);

        evaluator = QueryParser.parse(":matchesOwn(regex)");
        assertNotNull(evaluator);
    }

    @Test
    public void testNot() {
        Evaluator evaluator = QueryParser.parse(":not(p)");
        assertNotNull(evaluator);
    }

}