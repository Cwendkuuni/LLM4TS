import static org.junit.Assert.*;
import org.junit.Test;

public class QueryParserTest {

    // Test case for parse() method
    @Test
    public void testParse() {
        String query = "div.test";
        Evaluator e = QueryParser.parse(query);
        assertNotNull(e);
    }

    // Test case for combinator() method with '>'
    @Test
    public void testCombinatorGreater() {
        QueryParser qp = new QueryParser("div > p");
        qp.combinator('>');
        List<Evaluator> evals = qp.getEvals();
        assertNotNull(evals);
        assertEquals(1, evals.size());
        assertTrue(evals.get(0) instanceof CombiningEvaluator.And);
    }

    // Test case for combinator() method with ' '
    @Test
    public void testCombinatorSpace() {
        QueryParser qp = new QueryParser("div p");
        qp.combinator(' ');
        List<Evaluator> evals = qp.getEvals();
        assertNotNull(evals);
        assertEquals(1, evals.size());
        assertTrue(evals.get(0) instanceof CombiningEvaluator.And);
    }

    // Test case for combinator() method with '+'
    @Test
    public void testCombinatorPlus() {
        QueryParser qp = new QueryParser("div + p");
        qp.combinator('+');
        List<Evaluator> evals = qp.getEvals();
        assertNotNull(evals);
        assertEquals(1, evals.size());
        assertTrue(evals.get(0) instanceof CombiningEvaluator.And);
    }

    // Test case for combinator() method with '~'
    @Test
    public void testCombinatorTilde() {
        QueryParser qp = new QueryParser("div ~ p");
        qp.combinator('~');
        List<Evaluator> evals = qp.getEvals();
        assertNotNull(evals);
        assertEquals(1, evals.size());
        assertTrue(evals.get(0) instanceof CombiningEvaluator.And);
    }

    // Test case for combinator() method with unknown combinator
    @Test(expected = Selector.SelectorParseException.class)
    public void testCombinatorUnknown() {
        QueryParser qp = new QueryParser("div ? p");
        qp.combinator('?');
    }

    // Test case for consumeSubQuery() method
    @Test
    public void testConsumeSubQuery() {
        QueryParser qp = new QueryParser("div > p");
        String subQuery = qp.consumeSubQuery();
        assertNotNull(subQuery);
        assertFalse(subQuery.isEmpty());
    }

    // Test case for findElements() method
    @Test
    public void testFindElements() {
        QueryParser qp = new QueryParser("div.test");
        qp.findElements();
        List<Evaluator> evals = qp.getEvals();
        assertNotNull(evals);
        assertEquals(1, evals.size());
        assertTrue(evals.get(0) instanceof Evaluator.Class);
    }

    // Test case for byId() method
    @Test
    public void testById() {
        QueryParser qp = new QueryParser("#test");
        qp.byId();
        List<Evaluator> evals = qp.getEvals();
        assertNotNull(evals);
        assertEquals(1, evals.size());
        assertTrue(evals.get(0) instanceof Evaluator.Id);
    }

    // Test case for byClass() method
    @Test
    public void testByClass() {
        QueryParser qp = new QueryParser(".test");
        qp.byClass();
        List<Evaluator> evals = qp.getEvals();
        assertNotNull(evals);
        assertEquals(1, evals.size());
        assertTrue(evals.get(0) instanceof Evaluator.Class);
    }

    // Test case for byTag() method
    @Test
    public void testByTag() {
        QueryParser qp = new QueryParser("div");
        qp.byTag();
        List<Evaluator> evals = qp.getEvals();
        assertNotNull(evals);
        assertEquals(1, evals.size());
        assertTrue(evals.get(0) instanceof Evaluator.Tag);
    }

    // Test case for byAttribute() method
    @Test
    public void testByAttribute() {
        QueryParser qp = new QueryParser("[name=test]");
        qp.byAttribute();
        List<Evaluator> evals = qp.getEvals();
        assertNotNull(evals);
        assertEquals(1, evals.size());
        assertTrue(evals.get(0) instanceof Evaluator.Attribute);
    }

    // Test case for allElements() method
    @Test
    public void testAllElements() {
        QueryParser qp = new QueryParser("*");
        qp.allElements();
        List<Evaluator> evals = qp.getEvals();
        assertNotNull(evals);
        assertEquals(1, evals.size());
        assertTrue(evals.get(0) instanceof Evaluator.AllElements);
    }

    // Test case for indexLessThan() method
    @Test
    public void testIndexLessThan() {
        QueryParser qp = new QueryParser(":lt(5)");
        qp.indexLessThan();
        List<Evaluator> evals = qp.getEvals();
        assertNotNull(evals);
        assertEquals(1, evals.size());
        assertTrue(evals.get(0) instanceof Evaluator.IndexLessThan);
    }

    // Test case for indexGreaterThan() method
    @Test
    public void testIndexGreaterThan() {
        QueryParser qp = new QueryParser(":gt(5)");
        qp.indexGreaterThan();
        List<Evaluator> evals = qp.getEvals();
        assertNotNull(evals);
        assertEquals(1, evals.size());
        assertTrue(evals.get(0) instanceof Evaluator.IndexGreaterThan);
    }

    // Test case for indexEquals() method
    @Test
    public void testIndexEquals() {
        QueryParser qp = new QueryParser(":eq(5)");
        qp.indexEquals();
        List<Evaluator> evals = qp.getEvals();
        assertNotNull(evals);
        assertEquals(1, evals.size());
        assertTrue(evals.get(0) instanceof Evaluator.IndexEquals);
    }

    // Test case for has() method
    @Test
    public void testHas() {
        QueryParser qp = new QueryParser(":has(div)");
        qp.has();
        List<Evaluator> evals = qp.getEvals();
        assertNotNull(evals);
        assertEquals(1, evals.size());
        assertTrue(evals.get(0) instanceof StructuralEvaluator.Has);
    }

    // Test case for contains() method
    @Test
    public void testContains() {
        QueryParser qp = new QueryParser(":contains(text)");
        qp.contains(false);
        List<Evaluator> evals = qp.getEvals();
        assertNotNull(evals);
        assertEquals(1, evals.size());
        assertTrue(evals.get(0) instanceof Evaluator.ContainsText);
    }

    // Test case for containsOwn() method
    @Test
    public void testContainsOwn() {
        QueryParser qp = new QueryParser(":containsOwn(text)");
        qp.contains(true);
        List<Evaluator> evals = qp.getEvals();
        assertNotNull(evals);
        assertEquals(1, evals.size());
        assertTrue(evals.get(0) instanceof Evaluator.ContainsOwnText);
    }

    // Test case for matches() method
    @Test
    public void testMatches() {
        QueryParser qp = new QueryParser(":matches(regex)");
        qp.matches(false);
        List<Evaluator> evals = qp.getEvals();
        assertNotNull(evals);
        assertEquals(1, evals.size());
        assertTrue(evals.get(0) instanceof Evaluator.Matches);
    }

    // Test case for matchesOwn() method
    @Test
    public void testMatchesOwn() {
        QueryParser qp = new QueryParser(":matchesOwn(regex)");
        qp.matches(true);
        List<Evaluator> evals = qp.getEvals();
        assertNotNull(evals);
        assertEquals(1, evals.size());
        assertTrue(evals.get(0) instanceof Evaluator.MatchesOwn);
    }

    // Test case for not() method
    @Test
    public void testNot() {
        QueryParser qp = new QueryParser(":not(div)");
        qp.not();
        List<Evaluator> evals = qp.getEvals();
        assertNotNull(evals);
        assertEquals(1, evals.size());
        assertTrue(evals.get(0) instanceof StructuralEvaluator.Not);
    }

}