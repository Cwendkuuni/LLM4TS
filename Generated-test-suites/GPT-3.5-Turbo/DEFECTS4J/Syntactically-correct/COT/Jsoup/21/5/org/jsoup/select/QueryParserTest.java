import org.jsoup.select.Evaluator;
import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {

    private QueryParser queryParser;

    @Test
    public void testParse() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testCombinator() {
        queryParser = new QueryParser("div > p");
        queryParser.combinator('>');
        assertNotNull(queryParser.evals);

        queryParser = new QueryParser("div   p");
        queryParser.combinator(' ');
        assertNotNull(queryParser.evals);

        queryParser = new QueryParser("div + p");
        queryParser.combinator('+');
        assertNotNull(queryParser.evals);

        queryParser = new QueryParser("div ~ p");
        queryParser.combinator('~');
        assertNotNull(queryParser.evals);

        try {
            queryParser = new QueryParser("div ! p");
            queryParser.combinator('!');
            fail("Should throw SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            // Expected exception
        }
    }

    @Test
    public void testConsumeSubQuery() {
        queryParser = new QueryParser("#id.class");
        String subQuery = queryParser.consumeSubQuery();
        assertEquals("#id.class", subQuery);
        assertTrue(queryParser.tq.isEmpty());

        queryParser = new QueryParser("#id.class > div");
        subQuery = queryParser.consumeSubQuery();
        assertEquals("#id.class", subQuery);
        assertFalse(queryParser.tq.isEmpty());
        assertEquals("  div", queryParser.tq.remainder());
    }

    @Test
    public void testFindElements() {
        // Test all possible cases in findElements method
        queryParser = new QueryParser("#id");
        queryParser.findElements();

        queryParser = new QueryParser(".class");
        queryParser.findElements();

        queryParser = new QueryParser("div");
        queryParser.findElements();

        queryParser = new QueryParser("div[attr]");
        queryParser.findElements();

        queryParser = new QueryParser("*");
        queryParser.findElements();

        queryParser = new QueryParser(":lt(2)");
        queryParser.findElements();

        queryParser = new QueryParser(":gt(2)");
        queryParser.findElements();

        queryParser = new QueryParser(":eq(2)");
        queryParser.findElements();

        queryParser = new QueryParser(":has(p)");
        queryParser.findElements();

        queryParser = new QueryParser(":contains(text)");
        queryParser.findElements();

        queryParser = new QueryParser(":containsOwn(text)");
        queryParser.findElements();

        queryParser = new QueryParser(":matches(regex)");
        queryParser.findElements();

        queryParser = new QueryParser(":matchesOwn(regex)");
        queryParser.findElements();

        queryParser = new QueryParser(":not(p)");
        queryParser.findElements();

        try {
            queryParser = new QueryParser("unknown");
            queryParser.findElements();
            fail("Should throw SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            // Expected exception
        }
    }

    @Test
    public void testById() {
        queryParser = new QueryParser("#id");
        queryParser.byId();
        assertNotNull(queryParser.evals);
    }

    @Test
    public void testByClass() {
        queryParser = new QueryParser(".class");
        queryParser.byClass();
        assertNotNull(queryParser.evals);
    }

    @Test
    public void testByTag() {
        queryParser = new QueryParser("div");
        queryParser.byTag();
        assertNotNull(queryParser.evals);
    }

    @Test
    public void testByAttribute() {
        queryParser = new QueryParser("div[attr=value]");
        queryParser.byAttribute();
        assertNotNull(queryParser.evals);

        queryParser = new QueryParser("div[attr]");
        queryParser.byAttribute();
        assertNotNull(queryParser.evals);

        queryParser = new QueryParser("div[^attr]");
        queryParser.byAttribute();
        assertNotNull(queryParser.evals);

        try {
            queryParser = new QueryParser("div[=attr]");
            queryParser.byAttribute();
            fail("Should throw SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            // Expected exception
        }
    }

    @Test
    public void testAllElements() {
        queryParser = new QueryParser("*");
        queryParser.allElements();
        assertNotNull(queryParser.evals);
    }

    @Test
    public void testIndexLessThan() {
        queryParser = new QueryParser(":lt(2)");
        queryParser.indexLessThan();
        assertNotNull(queryParser.evals);
    }

    @Test
    public void testIndexGreaterThan() {
        queryParser = new QueryParser(":gt(2)");
        queryParser.indexGreaterThan();
        assertNotNull(queryParser.evals);
    }

    @Test
    public void testIndexEquals() {
        queryParser = new QueryParser(":eq(2)");
        queryParser.indexEquals();
        assertNotNull(queryParser.evals);
    }

    @Test
    public void testHas() {
        queryParser = new QueryParser(":has(p)");
        queryParser.has();
        assertNotNull(queryParser.evals);
    }

    @Test
    public void testContains() {
        queryParser = new QueryParser(":contains(text)");
        queryParser.contains(false);
        assertNotNull(queryParser.evals);

        queryParser = new QueryParser(":containsOwn(text)");
        queryParser.contains(true);
        assertNotNull(queryParser.evals);
    }

    @Test
    public void testMatches() {
        queryParser = new QueryParser(":matches(regex)");
        queryParser.matches(false);
        assertNotNull(queryParser.evals);

        queryParser = new QueryParser(":matchesOwn(regex)");
        queryParser.matches(true);
        assertNotNull(queryParser.evals);
    }

    @Test
    public void testNot() {
        queryParser = new QueryParser(":not(p)");
        queryParser.not();
        assertNotNull(queryParser.evals);
    }
}