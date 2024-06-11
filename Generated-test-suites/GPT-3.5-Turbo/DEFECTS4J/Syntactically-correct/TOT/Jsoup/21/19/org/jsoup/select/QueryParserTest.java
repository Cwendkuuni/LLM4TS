import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void parse_withCombinator_shouldReturnRootEvaluator() {
        String query = ",div";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof StructuralEvaluator.Root);
    }

    @Test
    public void parse_withNoCombinator_shouldReturnAndEvaluator() {
        String query = "div";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof CombiningEvaluator.And);
    }

    @Test
    public void parse_withMultipleCombinators_shouldReturnCorrectEvaluator() {
        String query = "div+span";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof CombiningEvaluator.And);
        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) result;
        assertTrue(andEvaluator.evals.get(1) instanceof StructuralEvaluator.ImmediatePreviousSibling);
    }

    @Test
    public void parse_withOrCombinator_shouldReturnOrEvaluator() {
        String query = "div, span";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof CombiningEvaluator.Or);
    }

    @Test
    public void parse_withMultipleSelectors_shouldReturnAndEvaluator() {
        String query = "div, span";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof CombiningEvaluator.And);
        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) result;
        for (Evaluator evaluator : andEvaluator.evals) {
            assertTrue(evaluator instanceof CombiningEvaluator.Or);
        }
    }

    @Test
    public void parse_withInvalidSelector_shouldThrowException() {
        try {
            String query = "div [";
            QueryParser.parse(query);
            fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Could not parse query 'div [': unexpected token at '['", e.getMessage());
        }
    }

    @Test
    public void parse_withValidClassSelector_shouldReturnClassEvaluator() {
        String query = ".class";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof Evaluator.Class);
    }

    @Test
    public void parse_withValidIdSelector_shouldReturnIdEvaluator() {
        String query = "#id";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof Evaluator.Id);
    }

    @Test
    public void parse_withValidAttributeSelector_shouldReturnAttributeEvaluator() {
        String query = "[key=value]";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof Evaluator.AttributeWithValue);
    }

    @Test
    public void parse_withInvalidAttributeSelector_shouldThrowException() {
        try {
            String query = "[key==value]";
            QueryParser.parse(query);
            fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Could not parse attribute query '[key==value]': unexpected token at '==value]'", e.getMessage());
        }
    }

    @Test
    public void parse_withValidPseudoSelector_shouldReturnPseudoSelectorEvaluator() {
        String query = ":has(span)";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof StructuralEvaluator.Has);
    }

    @Test
    public void parse_withInvalidPseudoSelector_shouldThrowException() {
        try {
            String query = ":has";
            QueryParser.parse(query);
            fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Could not parse query ':has': unexpected token at ''", e.getMessage());
        }
    }

    @Test
    public void parse_withValidContainsSelector_shouldReturnContainsTextEvaluator() {
        String query = ":contains(text)";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof Evaluator.ContainsText);
    }

    @Test
    public void parse_withInvalidContainsSelector_shouldThrowException() {
        try {
            String query = ":contains";
            QueryParser.parse(query);
            fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Could not parse query ':contains': unexpected token at ''", e.getMessage());
        }
    }

    @Test
    public void parse_withValidMatchesSelector_shouldReturnMatchesEvaluator() {
        String query = ":matches(regex)";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof Evaluator.Matches);
    }

    @Test
    public void parse_withInvalidMatchesSelector_shouldThrowException() {
        try {
            String query = ":matches";
            QueryParser.parse(query);
            fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Could not parse query ':matches': unexpected token at ''", e.getMessage());
        }
    }

    @Test
    public void parse_withValidNotSelector_shouldReturnNotEvaluator() {
        String query = ":not(span)";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof StructuralEvaluator.Not);
    }

    @Test
    public void parse_withInvalidNotSelector_shouldThrowException() {
        try {
            String query = ":not";
            QueryParser.parse(query);
            fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Could not parse query ':not': unexpected token at ''", e.getMessage());
        }
    }

}