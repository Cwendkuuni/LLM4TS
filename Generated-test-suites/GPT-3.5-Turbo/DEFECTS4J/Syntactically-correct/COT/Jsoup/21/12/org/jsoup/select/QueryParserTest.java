import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div#id.class[attr]";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testCombinator() {
        QueryParser queryParser = new QueryParser("");
        queryParser.evals.add(new Evaluator.Id("id"));
        queryParser.combinator('>');
        assertNotNull(queryParser.evals);
    }

    @Test
    public void testConsumeSubQuery() {
        QueryParser queryParser = new QueryParser("");
        queryParser.tq = new TokenQueue("> div#id.class[attr]");
        String subQuery = queryParser.consumeSubQuery();
        assertEquals(" div#id.class[attr]", subQuery);
    }

    @Test
    public void testFindElementsById() {
        QueryParser queryParser = new QueryParser("");
        queryParser.tq = new TokenQueue("div#id");
        queryParser.byId();
        assertNotNull(queryParser.evals);
    }

    @Test
    public void testFindByClass() {
        QueryParser queryParser = new QueryParser("");
        queryParser.tq = new TokenQueue("div.class");
        queryParser.byClass();
        assertNotNull(queryParser.evals);
    }

    @Test
    public void testFindByTag() {
        QueryParser queryParser = new QueryParser("");
        queryParser.tq = new TokenQueue("div");
        queryParser.byTag();
        assertNotNull(queryParser.evals);
    }

    @Test
    public void testFindByAttribute() {
        QueryParser queryParser = new QueryParser("");
        queryParser.tq = new TokenQueue("[attr=value]");
        queryParser.byAttribute();
        assertNotNull(queryParser.evals);
    }

    @Test
    public void testFindAllElements() {
        QueryParser queryParser = new QueryParser("");
        queryParser.allElements();
        assertNotNull(queryParser.evals);
    }

    @Test
    public void testIndexLessThan() {
        QueryParser queryParser = new QueryParser("");
        queryParser.tq = new TokenQueue(":lt(2)");
        queryParser.indexLessThan();
        assertNotNull(queryParser.evals);
    }

    @Test
    public void testIndexGreaterThan() {
        QueryParser queryParser = new QueryParser("");
        queryParser.tq = new TokenQueue(":gt(2)");
        queryParser.indexGreaterThan();
        assertNotNull(queryParser.evals);
    }

    @Test
    public void testIndexEquals() {
        QueryParser queryParser = new QueryParser("");
        queryParser.tq = new TokenQueue(":eq(2)");
        queryParser.indexEquals();
        assertNotNull(queryParser.evals);
    }

    @Test
    public void testHas() {
        QueryParser queryParser = new QueryParser("");
        queryParser.tq = new TokenQueue(":has(div)");
        queryParser.has();
        assertNotNull(queryParser.evals);
    }

    @Test
    public void testContains() {
        QueryParser queryParser = new QueryParser("");
        queryParser.tq = new TokenQueue(":contains(text)");
        queryParser.contains(false);
        assertNotNull(queryParser.evals);
    }

    @Test
    public void testContainsOwn() {
        QueryParser queryParser = new QueryParser("");
        queryParser.tq = new TokenQueue(":containsOwn(text)");
        queryParser.contains(true);
        assertNotNull(queryParser.evals);
    }

    @Test
    public void testMatches() {
        QueryParser queryParser = new QueryParser("");
        queryParser.tq = new TokenQueue(":matches(regex)");
        queryParser.matches(false);
        assertNotNull(queryParser.evals);
    }

    @Test
    public void testMatchesOwn() {
        QueryParser queryParser = new QueryParser("");
        queryParser.tq = new TokenQueue(":matchesOwn(regex)");
        queryParser.matches(true);
        assertNotNull(queryParser.evals);
    }

    @Test
    public void testNot() {
        QueryParser queryParser = new QueryParser("");
        queryParser.tq = new TokenQueue(":not(div)");
        queryParser.not();
        assertNotNull(queryParser.evals);
    }
}

