import org.jsoup.select.Evaluator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {
    private QueryParser parser;

    @Before
    public void setUp() {
        parser = new QueryParser();
    }

    @Test
    public void testParse_withEmptyQuery_shouldReturnNull() {
        String query = "";

        Evaluator result = parser.parse(query);

        assertNull(result);
    }

    @Test
    public void testParse_withCommaInQuery_shouldReturnOrEvaluator() {
        String query = "div, p";

        Evaluator result = parser.parse(query);

        assertTrue(result instanceof CombiningEvaluator.Or);
    }

    @Test
    public void testParse_withInvalidCombinator_shouldThrowParseException() {
        String query = "* > div";

        try {
            parser.parse(query);
            fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Unknown combinator: >", e.getMessage());
        }
    }

    @Test
    public void testParse_withMultipleCombinators_shouldReturnAndEvaluator() {
        String query = "div > p + span ~ a";

        Evaluator result = parser.parse(query);

        assertTrue(result instanceof CombiningEvaluator.And);
    }

    @Test
    public void testParse_withIdSelector_shouldReturnIdEvaluator() {
        String query = "#myId";

        Evaluator result = parser.parse(query);

        assertTrue(result instanceof Evaluator.Id);
    }

    @Test
    public void testParse_withClassSelector_shouldReturnClassEvaluator() {
        String query = ".myClass";

        Evaluator result = parser.parse(query);

        assertTrue(result instanceof Evaluator.Class);
    }

    @Test
    public void testParse_withTagSelector_shouldReturnTagEvaluator() {
        String query = "div";

        Evaluator result = parser.parse(query);

        assertTrue(result instanceof Evaluator.Tag);
    }

    @Test
    public void testParse_withAttributeSelector_shouldReturnAttributeEvaluator() {
        String query = "[name='myName']";

        Evaluator result = parser.parse(query);

        assertTrue(result instanceof Evaluator.Attribute);
    }

    @Test
    public void testParse_withAttributeSelectorAndValue_shouldReturnAttributeWithValueEvaluator() {
        String query = "[name='myName'][value='myValue']";

        Evaluator result = parser.parse(query);

        assertTrue(result instanceof Evaluator.AttributeWithValue);
    }

    @Test
    public void testParse_withIndexLessThanSelector_shouldReturnIndexLessThanEvaluator() {
        String query = ":lt(5)";

        Evaluator result = parser.parse(query);

        assertTrue(result instanceof Evaluator.IndexLessThan);
    }

    @Test
    public void testParse_withIndexGreaterThanSelector_shouldReturnIndexGreaterThanEvaluator() {
        String query = ":gt(5)";

        Evaluator result = parser.parse(query);

        assertTrue(result instanceof Evaluator.IndexGreaterThan);
    }

    @Test
    public void testParse_withIndexEqualsSelector_shouldReturnIndexEqualsEvaluator() {
        String query = ":eq(5)";

        Evaluator result = parser.parse(query);

        assertTrue(result instanceof Evaluator.IndexEquals);
    }

    @Test
    public void testParse_withHasSelector_shouldReturnHasEvaluator() {
        String query = ":has(p)";

        Evaluator result = parser.parse(query);

        assertTrue(result instanceof StructuralEvaluator.Has);
    }

    @Test
    public void testParse_withContainsSelector_shouldReturnContainsTextEvaluator() {
        String query = ":contains('text')";

        Evaluator result = parser.parse(query);

        assertTrue(result instanceof Evaluator.ContainsText);
    }

    @Test
    public void testParse_withContainsOwnSelector_shouldReturnContainsOwnTextEvaluator() {
        String query = ":containsOwn('text')";

        Evaluator result = parser.parse(query);

        assertTrue(result instanceof Evaluator.ContainsOwnText);
    }

    @Test
    public void testParse_withMatchesSelector_shouldReturnMatchesEvaluator() {
        String query = ":matches('.*pattern.*')";

        Evaluator result = parser.parse(query);

        assertTrue(result instanceof Evaluator.Matches);
    }

    @Test
    public void testParse_withMatchesOwnSelector_shouldReturnMatchesOwnEvaluator() {
        String query = ":matchesOwn('.*pattern.*')";

        Evaluator result = parser.parse(query);

        assertTrue(result instanceof Evaluator.MatchesOwn);
    }

    @Test
    public void testParse_withNotSelector_shouldReturnNotEvaluator() {
        String query = ":not(div)";

        Evaluator result = parser.parse(query);

        assertTrue(result instanceof StructuralEvaluator.Not);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_withInvalidSelector_shouldThrowParseException() {
        String query = "invalid[selector]";

        parser.parse(query);
    }

    // Add more test cases for other methods if needed

}