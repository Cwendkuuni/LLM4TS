import org.junit.Test;
import org.jsoup.select.*;
import org.jsoup.helper.Validate;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test public void testParse() {
        String query = "div.foo#bar[attr=value]:has(span)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test public void testParse_emptyQuery() {
        String query = "";
        Evaluator evaluator = QueryParser.parse(query);
        assertNull(evaluator);
    }

    @Test public void testCombinator() {
        QueryParser parser = new QueryParser("");
        parser.combinator('>');
        assertEquals(1, parser.evals.size());
    }

    @Test public void testConsumeSubQuery() {
        QueryParser parser = new QueryParser("");
        parser.tq = new TokenQueue("span");
        String subQuery = parser.consumeSubQuery();
        assertEquals("span", subQuery);
    }

    @Test public void testFindElements_byTag() {
        QueryParser parser = new QueryParser("");
        parser.tq = new TokenQueue("span");
        parser.findElements();
        assertEquals(1, parser.evals.size());
    }

    @Test public void testFindElements_byClass() {
        QueryParser parser = new QueryParser("");
        parser.tq = new TokenQueue(".foo");
        parser.findElements();
        assertEquals(1, parser.evals.size());
    }

    @Test public void testFindElements_byId() {
        QueryParser parser = new QueryParser("");
        parser.tq = new TokenQueue("#bar");
        parser.findElements();
        assertEquals(1, parser.evals.size());
    }

    @Test public void testFindElements_byAttribute() {
        QueryParser parser = new QueryParser("");
        parser.tq = new TokenQueue("[attr=value]");
        parser.findElements();
        assertEquals(1, parser.evals.size());
    }

    @Test public void testFindElements_allElements() {
        QueryParser parser = new QueryParser("");
        parser.tq = new TokenQueue("*");
        parser.findElements();
        assertEquals(1, parser.evals.size());
    }

    @Test public void testFindElements_indexLessThan() {
        QueryParser parser = new QueryParser("");
        parser.tq = new TokenQueue(":lt(5)");
        parser.findElements();
        assertEquals(1, parser.evals.size());
    }

    @Test public void testFindElements_indexGreaterThan() {
        QueryParser parser = new QueryParser("");
        parser.tq = new TokenQueue(":gt(5)");
        parser.findElements();
        assertEquals(1, parser.evals.size());
    }

    @Test public void testFindElements_indexEquals() {
        QueryParser parser = new QueryParser("");
        parser.tq = new TokenQueue(":eq(5)");
        parser.findElements();
        assertEquals(1, parser.evals.size());
    }

    @Test public void testFindElements_has() {
        QueryParser parser = new QueryParser("");
        parser.tq = new TokenQueue(":has(span)");
        parser.findElements();
        assertEquals(1, parser.evals.size());
    }

    @Test public void testFindElements_contains() {
        QueryParser parser = new QueryParser("");
        parser.tq = new TokenQueue(":contains(text)");
        parser.findElements();
        assertEquals(1, parser.evals.size());
    }

    @Test public void testFindElements_containsOwn() {
        QueryParser parser = new QueryParser("");
        parser.tq = new TokenQueue(":containsOwn(text)");
        parser.findElements();
        assertEquals(1, parser.evals.size());
    }

    @Test public void testFindElements_matches() {
        QueryParser parser = new QueryParser("");
        parser.tq = new TokenQueue(":matches(regex)");
        parser.findElements();
        assertEquals(1, parser.evals.size());
    }

    @Test public void testFindElements_matchesOwn() {
        QueryParser parser = new QueryParser("");
        parser.tq = new TokenQueue(":matchesOwn(regex)");
        parser.findElements();
        assertEquals(1, parser.evals.size());
    }

    @Test public void testFindElements_not() {
        QueryParser parser = new QueryParser("");
        parser.tq = new TokenQueue(":not(span)");
        parser.findElements();
        assertEquals(1, parser.evals.size());
    }

}