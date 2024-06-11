
import org.junit.Assert;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testCombinator() {
        QueryParser queryParser = new QueryParser("");
        queryParser.combinator(' ');
        Assert.assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testConsumeSubQuery() {
        QueryParser queryParser = new QueryParser("");
        String subQuery = queryParser.consumeSubQuery();
        Assert.assertNotNull(subQuery);
    }

    @Test
    public void testFindElements() {
        QueryParser queryParser = new QueryParser("");
        queryParser.findElements();
        Assert.assertEquals(0, queryParser.evals.size());
    }

    @Test
    public void testById() {
        QueryParser queryParser = new QueryParser("");
        queryParser.byId();
        Assert.assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testByClass() {
        QueryParser queryParser = new QueryParser("");
        queryParser.byClass();
        Assert.assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testByTag() {
        QueryParser queryParser = new QueryParser("");
        queryParser.byTag();
        Assert.assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testByAttribute() {
        QueryParser queryParser = new QueryParser("");
        queryParser.byAttribute();
        Assert.assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testAllElements() {
        QueryParser queryParser = new QueryParser("");
        queryParser.allElements();
        Assert.assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testIndexLessThan() {
        QueryParser queryParser = new QueryParser("");
        queryParser.indexLessThan();
        Assert.assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testIndexGreaterThan() {
        QueryParser queryParser = new QueryParser("");
        queryParser.indexGreaterThan();
        Assert.assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testIndexEquals() {
        QueryParser queryParser = new QueryParser("");
        queryParser.indexEquals();
        Assert.assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testHas() {
        QueryParser queryParser = new QueryParser("");
        queryParser.has();
        Assert.assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testContains() {
        QueryParser queryParser = new QueryParser("");
        queryParser.contains(false);
        Assert.assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testContainsOwn() {
        QueryParser queryParser = new QueryParser("");
        queryParser.contains(true);
        Assert.assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testMatches() {
        QueryParser queryParser = new QueryParser("");
        queryParser.matches(false);
        Assert.assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testMatchesOwn() {
        QueryParser queryParser = new QueryParser("");
        queryParser.matches(true);
        Assert.assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testNot() {
        QueryParser queryParser = new QueryParser("");
        queryParser.not();
        Assert.assertEquals(1, queryParser.evals.size());
    }
}
