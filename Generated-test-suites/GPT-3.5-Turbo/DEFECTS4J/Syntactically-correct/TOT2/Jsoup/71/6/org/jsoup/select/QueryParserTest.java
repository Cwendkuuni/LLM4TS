import org.jsoup.select.*;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.Connection;

import static org.junit.Assert.*;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "a[href]";
        Evaluator eval = QueryParser.parse(query);
        assertNotNull(eval);
        assertTrue(eval instanceof Evaluator.Attribute);
        assertEquals("href", ((Evaluator.Attribute) eval).getKey());
    }

    @Test
    public void testCombinator() {
        QueryParser qp = new QueryParser("");
        qp.combinator('>');
        assertEquals(1, qp.evals.size());
        assertTrue(qp.evals.get(0) instanceof StructuralEvaluator.Root);
    }

    @Test
    public void testById() {
        QueryParser qp = new QueryParser("");
        qp.byId();
        assertEquals(1, qp.evals.size());
        assertTrue(qp.evals.get(0) instanceof Evaluator.Id);
    }

    @Test
    public void testByClass() {
        QueryParser qp = new QueryParser("");
        qp.byClass();
        assertEquals(1, qp.evals.size());
        assertTrue(qp.evals.get(0) instanceof Evaluator.Class);
    }

    @Test
    public void testByTag() {
        QueryParser qp = new QueryParser("");
        qp.byTag();
        assertEquals(1, qp.evals.size());
        assertTrue(qp.evals.get(0) instanceof Evaluator.Tag);
    }

    @Test
    public void testByAttribute() {
        QueryParser qp = new QueryParser("");
        qp.byAttribute();
        assertEquals(1, qp.evals.size());
        assertTrue(qp.evals.get(0) instanceof Evaluator.Attribute);
    }

    @Test
    public void testAllElements() {
        QueryParser qp = new QueryParser("");
        qp.allElements();
        assertEquals(1, qp.evals.size());
        assertTrue(qp.evals.get(0) instanceof Evaluator.AllElements);
    }

    @Test
    public void testIndexLessThan() {
        QueryParser qp = new QueryParser("");
        qp.indexLessThan();
        assertEquals(1, qp.evals.size());
        assertTrue(qp.evals.get(0) instanceof Evaluator.IndexLessThan);
    }

    @Test
    public void testIndexGreaterThan() {
        QueryParser qp = new QueryParser("");
        qp.indexGreaterThan();
        assertEquals(1, qp.evals.size());
        assertTrue(qp.evals.get(0) instanceof Evaluator.IndexGreaterThan);
    }

    @Test
    public void testIndexEquals() {
        QueryParser qp = new QueryParser("");
        qp.indexEquals();
        assertEquals(1, qp.evals.size());
        assertTrue(qp.evals.get(0) instanceof Evaluator.IndexEquals);
    }

    @Test
    public void testHas() {
        QueryParser qp = new QueryParser("");
        qp.has();
        assertEquals(1, qp.evals.size());
        assertTrue(qp.evals.get(0) instanceof StructuralEvaluator.Has);
    }

    @Test
    public void testContains() {
        QueryParser qp = new QueryParser("");
        qp.contains(false);
        assertEquals(1, qp.evals.size());
        assertTrue(qp.evals.get(0) instanceof Evaluator.ContainsText);
    }

    @Test
    public void testContainsOwn() {
        QueryParser qp = new QueryParser("");
        qp.contains(true);
        assertEquals(1, qp.evals.size());
        assertTrue(qp.evals.get(0) instanceof Evaluator.ContainsOwnText);
    }

    @Test
    public void testContainsData() {
        QueryParser qp = new QueryParser("");
        qp.containsData();
        assertEquals(1, qp.evals.size());
        assertTrue(qp.evals.get(0) instanceof Evaluator.ContainsData);
    }

    @Test
    public void testMatches() {
        QueryParser qp = new QueryParser("");
        qp.matches(false);
        assertEquals(1, qp.evals.size());
        assertTrue(qp.evals.get(0) instanceof Evaluator.Matches);
    }

    @Test
    public void testNot() {
        QueryParser qp = new QueryParser("");
        qp.not();
        assertEquals(1, qp.evals.size());
        assertTrue(qp.evals.get(0) instanceof StructuralEvaluator.Not);
    }
}