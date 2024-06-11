import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);

        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Tag);
    }

    @Test
    public void testCombinator() {
        String query = "div > span";
        Evaluator evaluator = QueryParser.parse(query);

        assertNotNull(evaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.And);

        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) evaluator;
        assertTrue(andEvaluator.evals.size() == 2);
        assertTrue(andEvaluator.evals.get(0) instanceof Evaluator.Tag);
        assertTrue(andEvaluator.evals.get(1) instanceof StructuralEvaluator.ImmediateParent);
    }

    @Test
    public void testAttribute() {
        String query = "[class]";
        Evaluator evaluator = QueryParser.parse(query);

        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Attribute);
    }

    @Test
    public void testIndexEquals() {
        String query = ":eq(3)";
        Evaluator evaluator = QueryParser.parse(query);

        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.IndexEquals);
    }

    @Test
    public void testHas() {
        String query = ":has(span)";
        Evaluator evaluator = QueryParser.parse(query);

        assertNotNull(evaluator);
        assertTrue(evaluator instanceof StructuralEvaluator.Has);
    }

    @Test
    public void testContains() {
        String query = ":contains(text)";
        Evaluator evaluator = QueryParser.parse(query);

        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.ContainsText);
    }

    @Test
    public void testContainsOwn() {
        String query = ":containsOwn(text)";
        Evaluator evaluator = QueryParser.parse(query);

        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.ContainsOwnText);
    }

    @Test
    public void testContainsData() {
        String query = ":containsData(data)";
        Evaluator evaluator = QueryParser.parse(query);

        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.ContainsData);
    }

    @Test
    public void testMatches() {
        String query = ":matches(regex)";
        Evaluator evaluator = QueryParser.parse(query);

        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Matches);
    }

    @Test
    public void testNot() {
        String query = ":not(span)";
        Evaluator evaluator = QueryParser.parse(query);

        assertNotNull(evaluator);
        assertTrue(evaluator instanceof StructuralEvaluator.Not);
    }

}