import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {
    private QueryParser parser;

    @Before
    public void setup() {
        parser = new QueryParser("");
    }

    @Test
    public void testParse_ValidQuery_ReturnsEvaluator() {
        String query = "div.class > p";
        Evaluator evaluator = QueryParser.parse(query);

        assertNotNull(evaluator);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_InvalidQuery_ThrowsSelectorParseException() {
        String query = "[attribute]";
        QueryParser.parse(query);
    }

    @Test
    public void testParse_EmptyQuery_ReturnsAllElementsEvaluator() {
        String query = "";
        Evaluator evaluator = QueryParser.parse(query);

        assertTrue(evaluator instanceof Evaluator.AllElements);
    }

    @Test
    public void testParse_QueryWithCombinators_ReturnsCorrectEvaluator() {
        String query = "div > p + span ~ a";
        Evaluator evaluator = QueryParser.parse(query);

        assertTrue(evaluator instanceof CombiningEvaluator.And);
        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) evaluator;

        assertTrue(andEvaluator.evals.get(0) instanceof Evaluator.Tag);
        assertTrue(andEvaluator.evals.get(1) instanceof Evaluator.ImmediateParent);
        assertTrue(andEvaluator.evals.get(2) instanceof Evaluator.ImmediatePreviousSibling);
        assertTrue(andEvaluator.evals.get(3) instanceof Evaluator.PreviousSibling);
        assertTrue(andEvaluator.evals.get(4) instanceof Evaluator.Tag);
    }

    @Test
    public void testParse_QueryWithAttributeSelectors_ReturnsCorrectEvaluator() {
        String query = "a[href], input[type=text][required], [attribute=value]";
        Evaluator evaluator = QueryParser.parse(query);

        assertTrue(evaluator instanceof CombiningEvaluator.Or);
        CombiningEvaluator.Or orEvaluator = (CombiningEvaluator.Or) evaluator;

        assertTrue(orEvaluator.evals.get(0) instanceof Evaluator.Attribute);
        assertTrue(orEvaluator.evals.get(1) instanceof Evaluator.AttributeWithValue);
        assertTrue(orEvaluator.evals.get(2) instanceof Evaluator.AttributeWithValue);
    }
}