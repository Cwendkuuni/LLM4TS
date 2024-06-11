import org.jsoup.select.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse("div");
        assertNotNull(evaluator);
    }

    @Test
    public void testCombinator() {
        QueryParser parser = new QueryParser("");
        parser.combinator('>');
        assertFalse(parser.evals.isEmpty());
    }

    @Test
    public void testParseSubQuery() {
        QueryParser parser = new QueryParser("");
        String subQuery = parser.consumeSubQuery();
        assertNotNull(subQuery);
    }

    @Test
    public void testFindElements() {
        QueryParser parser = new QueryParser("");
        parser.findElements();
        assertFalse(parser.evals.isEmpty());
    }

    @Test
    public void testById() {
        QueryParser parser = new QueryParser("");
        parser.byId();
        assertFalse(parser.evals.isEmpty());
    }

    @Test
    public void testByClass() {
        QueryParser parser = new QueryParser("");
        parser.byClass();
        assertFalse(parser.evals.isEmpty());
    }

    @Test
    public void testByTag() {
        QueryParser parser = new QueryParser("");
        parser.byTag();
        assertFalse(parser.evals.isEmpty());
    }

    @Test
    public void testByAttribute() {
        QueryParser parser = new QueryParser("");
        parser.byAttribute();
        assertFalse(parser.evals.isEmpty());
    }

    @Test
    public void testAllElements() {
        QueryParser parser = new QueryParser("");
        parser.allElements();
        assertFalse(parser.evals.isEmpty());
    }

    @Test
    public void testIndexLessThan() {
        QueryParser parser = new QueryParser("");
        parser.indexLessThan();
        assertFalse(parser.evals.isEmpty());
    }

    @Test
    public void testIndexGreaterThan() {
        QueryParser parser = new QueryParser("");
        parser.indexGreaterThan();
        assertFalse(parser.evals.isEmpty());
    }

    @Test
    public void testIndexEquals() {
        QueryParser parser = new QueryParser("");
        parser.indexEquals();
        assertFalse(parser.evals.isEmpty());
    }

    @Test
    public void testCssNthChild() {
        QueryParser parser = new QueryParser("");
        parser.cssNthChild(false, false);
        assertFalse(parser.evals.isEmpty());
    }

    @Test
    public void testHas() {
        QueryParser parser = new QueryParser("");
        parser.has();
        assertFalse(parser.evals.isEmpty());
    }

    @Test
    public void testContains() {
        QueryParser parser = new QueryParser("");
        parser.contains(false);
        assertFalse(parser.evals.isEmpty());
    }

    @Test
    public void testContainsData() {
        QueryParser parser = new QueryParser("");
        parser.containsData();
        assertFalse(parser.evals.isEmpty());
    }

    @Test
    public void testMatches() {
        QueryParser parser = new QueryParser("");
        parser.matches(false);
        assertFalse(parser.evals.isEmpty());
    }

    @Test
    public void testNot() {
        QueryParser parser = new QueryParser("");
        parser.not();
        assertFalse(parser.evals.isEmpty());
    }
}