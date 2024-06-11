import org.jsoup.select.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse_startsWithCombinator_shouldReturnRootEvaluator() {
        Evaluator evaluator = QueryParser.parse(" > div");
        assertTrue(evaluator instanceof StructuralEvaluator.Root);
    }

    @Test
    public void testParse_startsWithCombinator_andComplexQuery_shouldReturnCombinatorEvaluator() {
        Evaluator evaluator = QueryParser.parse(" > div + ul");
        assertTrue(evaluator instanceof StructuralEvaluator.ImmediatePreviousSibling);
    }

    @Test
    public void testParse_startsWithCombinator_andEmptyQuery_shouldThrowException() {
        try {
            QueryParser.parse(" >");
            fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            // Exception expected
        }
    }

    @Test
    public void testParse_commaSeparatedQuery_shouldReturnOrEvaluator() {
        Evaluator evaluator = QueryParser.parse("div, span");
        assertTrue(evaluator instanceof CombiningEvaluator.Or);
    }

    @Test
    public void testParse_multipleCombosWithComma_shouldReturnOrEvaluator() {
        Evaluator evaluator = QueryParser.parse("div ul, span + p, .class > a");
        assertTrue(evaluator instanceof CombiningEvaluator.Or);
    }

    @Test
    public void testParse_invalidCombinator_shouldThrowException() {
        try {
            QueryParser.parse("div $ p");
            fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            // Exception expected
        }
    }

    @Test
    public void testParse_invalidAttributeQuery_shouldThrowException() {
        try {
            QueryParser.parse("div[ attr ]");
            fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            // Exception expected
        }
    }

    @Test
    public void testParse_invalidContainsQuery_shouldThrowException() {
        try {
            QueryParser.parse(":contains()");
            fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            // Exception expected
        }
    }
}