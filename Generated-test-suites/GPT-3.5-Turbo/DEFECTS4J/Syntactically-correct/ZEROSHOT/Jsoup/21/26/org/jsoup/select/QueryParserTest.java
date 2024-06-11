import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse("div.test");
        assertNotNull(evaluator);
    }
    
    @Test
    public void testCombinator() {
        QueryParser queryParser = new QueryParser("div.test");
        queryParser.combinator('>');
        assertEquals(1, queryParser.evals.size());
    }
    
    @Test
    public void testConsumeSubQuery() {
        QueryParser queryParser = new QueryParser("div.test");
        String subQuery = queryParser.consumeSubQuery();
        assertEquals("div.test", subQuery);
    }
    
    @Test
    public void testFindElements() {
        QueryParser queryParser = new QueryParser("div.test");
        queryParser.findElements();
        assertEquals(1, queryParser.evals.size());
    }
    
    @Test
    public void testById() {
        QueryParser queryParser = new QueryParser("#testId");
        queryParser.byId();
        assertEquals(1, queryParser.evals.size());
    }
    
    @Test
    public void testByClass() {
        QueryParser queryParser = new QueryParser(".testClass");
        queryParser.byClass();
        assertEquals(1, queryParser.evals.size());
    }
    
    @Test
    public void testByTag() {
        QueryParser queryParser = new QueryParser("div");
        queryParser.byTag();
        assertEquals(1, queryParser.evals.size());
    }
    
    @Test
    public void testByAttribute() {
        QueryParser queryParser = new QueryParser("[id=testId]");
        queryParser.byAttribute();
        assertEquals(1, queryParser.evals.size());
    }
    
    @Test
    public void testAllElements() {
        QueryParser queryParser = new QueryParser("*");
        queryParser.allElements();
        assertEquals(1, queryParser.evals.size());
    }
    
    @Test
    public void testIndexLessThan() {
        QueryParser queryParser = new QueryParser(":lt(5)");
        queryParser.indexLessThan();
        assertEquals(1, queryParser.evals.size());
    }
    
    @Test
    public void testIndexGreaterThan() {
        QueryParser queryParser = new QueryParser(":gt(5)");
        queryParser.indexGreaterThan();
        assertEquals(1, queryParser.evals.size());
    }
    
    @Test
    public void testIndexEquals() {
        QueryParser queryParser = new QueryParser(":eq(5)");
        queryParser.indexEquals();
        assertEquals(1, queryParser.evals.size());
    }
    
    @Test
    public void testHas() {
        QueryParser queryParser = new QueryParser(":has(div)");
        queryParser.has();
        assertEquals(1, queryParser.evals.size());
    }
    
    @Test
    public void testContains() {
        QueryParser queryParser = new QueryParser(":contains(test)");
        queryParser.contains(false);
        assertEquals(1, queryParser.evals.size());
    }
    
    @Test
    public void testContainsOwn() {
        QueryParser queryParser = new QueryParser(":containsOwn(test)");
        queryParser.contains(true);
        assertEquals(1, queryParser.evals.size());
    }
    
    @Test
    public void testMatches() {
        QueryParser queryParser = new QueryParser(":matches(regex)");
        queryParser.matches(false);
        assertEquals(1, queryParser.evals.size());
    }
    
    @Test
    public void testMatchesOwn() {
        QueryParser queryParser = new QueryParser(":matchesOwn(regex)");
        queryParser.matches(true);
        assertEquals(1, queryParser.evals.size());
    }
    
    @Test
    public void testNot() {
        QueryParser queryParser = new QueryParser(":not(div)");
        queryParser.not();
        assertEquals(1, queryParser.evals.size());
    }
}