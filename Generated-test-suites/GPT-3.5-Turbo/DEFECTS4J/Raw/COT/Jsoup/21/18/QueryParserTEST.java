import org.jsoup.select.*;
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
        QueryParser parser = new QueryParser("");
        parser.combinator('>');
        assertNotNull(parser.evals);
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testConsumeSubQuery() {
        QueryParser parser = new QueryParser("");
        String subQuery = parser.consumeSubQuery();
        assertNotNull(subQuery);
    }

    @Test
    public void testFindElements() {
        QueryParser parser = new QueryParser("");
        parser.findElements();
        assertNotNull(parser.evals);
        assertFalse(parser.evals.isEmpty());
    }

    @Test
    public void testById() {
        QueryParser parser = new QueryParser("");
        parser.byId();
        assertNotNull(parser.evals);
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testByClass() {
        QueryParser parser = new QueryParser("");
        parser.byClass();
        assertNotNull(parser.evals);
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testByTag() {
        QueryParser parser = new QueryParser("");
        parser.byTag();
        assertNotNull(parser.evals);
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testByAttribute() {
        QueryParser parser = new QueryParser("");
        parser.byAttribute();
        assertNotNull(parser.evals);
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testAllElements() {
        QueryParser parser = new QueryParser("");
        parser.allElements();
        assertNotNull(parser.evals);
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testIndexLessThan() {
        QueryParser parser = new QueryParser("");
        parser.indexLessThan();
        assertNotNull(parser.evals);
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testIndexGreaterThan() {
        QueryParser parser = new QueryParser("");
        parser.indexGreaterThan();
        assertNotNull(parser.evals);
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testIndexEquals() {
        QueryParser parser = new QueryParser("");
        parser.indexEquals();
        assertNotNull(parser.evals);
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testHas() {
        QueryParser parser = new QueryParser("");
        parser.has();
        assertNotNull(parser.evals);
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testContains() {
        QueryParser parser = new QueryParser("");
        parser.contains(false);
        assertNotNull(parser.evals);
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testContainsOwn() {
        QueryParser parser = new QueryParser("");
        parser.contains(true);
        assertNotNull(parser.evals);
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testMatches() {
        QueryParser parser = new QueryParser("");
        parser.matches(false);
        assertNotNull(parser.evals);
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testMatchesOwn() {
        QueryParser parser = new QueryParser("");
        parser.matches(true);
        assertNotNull(parser.evals);
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testNot() {
        QueryParser parser = new QueryParser("");
        parser.not();
        assertNotNull(parser.evals);
        assertEquals(1, parser.evals.size());
    }
}