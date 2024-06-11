import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {
    
    @Test
    public void testParse() {
        String query = "div.test";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testCombinator() {
        QueryParser parser = new QueryParser("div.test > p");
        parser.combinator('>');
        List<Evaluator> evals = parser.getEvals();
        assertEquals(2, evals.size());
    }
    
    @Test
    public void testConsumeSubQuery() {
        QueryParser parser = new QueryParser("div.test > p");
        String subQuery = parser.consumeSubQuery();
        assertEquals("div.test", subQuery);
    }
    
    @Test
    public void testFindElements() {
        QueryParser parser = new QueryParser("div.test");
        parser.findElements();
        List<Evaluator> evals = parser.getEvals();
        assertEquals(1, evals.size());
    }
    
    @Test
    public void testById() {
        QueryParser parser = new QueryParser("#id");
        parser.byId();
        List<Evaluator> evals = parser.getEvals();
        assertEquals(1, evals.size());
    }
    
    @Test
    public void testByClass() {
        QueryParser parser = new QueryParser(".class");
        parser.byClass();
        List<Evaluator> evals = parser.getEvals();
        assertEquals(1, evals.size());
    }
    
    @Test
    public void testByTag() {
        QueryParser parser = new QueryParser("div");
        parser.byTag();
        List<Evaluator> evals = parser.getEvals();
        assertEquals(1, evals.size());
    }
    
    @Test
    public void testByAttribute() {
        QueryParser parser = new QueryParser("[name=value]");
        parser.byAttribute();
        List<Evaluator> evals = parser.getEvals();
        assertEquals(1, evals.size());
    }
    
    @Test
    public void testAllElements() {
        QueryParser parser = new QueryParser("*");
        parser.allElements();
        List<Evaluator> evals = parser.getEvals();
        assertEquals(1, evals.size());
    }
    
    @Test
    public void testIndexLessThan() {
        QueryParser parser = new QueryParser(":lt(2)");
        parser.indexLessThan();
        List<Evaluator> evals = parser.getEvals();
        assertEquals(1, evals.size());
    }
    
    @Test
    public void testIndexGreaterThan() {
        QueryParser parser = new QueryParser(":gt(2)");
        parser.indexGreaterThan();
        List<Evaluator> evals = parser.getEvals();
        assertEquals(1, evals.size());
    }
    
    @Test
    public void testIndexEquals() {
        QueryParser parser = new QueryParser(":eq(2)");
        parser.indexEquals();
        List<Evaluator> evals = parser.getEvals();
        assertEquals(1, evals.size());
    }
    
    @Test
    public void testHas() {
        QueryParser parser = new QueryParser(":has(div)");
        parser.has();
        List<Evaluator> evals = parser.getEvals();
        assertEquals(1, evals.size());
    }
    
    @Test
    public void testContains() {
        QueryParser parser = new QueryParser(":contains(text)");
        parser.contains(false);
        List<Evaluator> evals = parser.getEvals();
        assertEquals(1, evals.size());
    }
    
    @Test
    public void testContainsOwn() {
        QueryParser parser = new QueryParser(":containsOwn(text)");
        parser.contains(true);
        List<Evaluator> evals = parser.getEvals();
        assertEquals(1, evals.size());
    }
    
    @Test
    public void testMatches() {
        QueryParser parser = new QueryParser(":matches(regex)");
        parser.matches(false);
        List<Evaluator> evals = parser.getEvals();
        assertEquals(1, evals.size());
    }
    
    @Test
    public void testMatchesOwn() {
        QueryParser parser = new QueryParser(":matchesOwn(regex)");
        parser.matches(true);
        List<Evaluator> evals = parser.getEvals();
        assertEquals(1, evals.size());
    }
    
    @Test
    public void testNot() {
        QueryParser parser = new QueryParser(":not(div)");
        parser.not();
        List<Evaluator> evals = parser.getEvals();
        assertEquals(1, evals.size());
    }
}