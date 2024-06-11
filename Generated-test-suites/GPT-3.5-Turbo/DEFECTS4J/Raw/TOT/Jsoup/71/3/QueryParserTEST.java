
package org.jsoup.select;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    private QueryParser parser;

    @Before
    public void setUp() {
        parser = new QueryParser("");
    }

    @Test
    public void testParse_returnsEvaluator() {
        Evaluator evaluator = QueryParser.parse("*");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withInvalidQuery_throwException() {
        try {
            QueryParser.parse("invalid query");
            fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            // Exception is expected
        }
    }

    @Test
    public void testParse_withValidQuery_returnsCorrectEvaluator() {
        Evaluator evaluator = QueryParser.parse("tagName[attr=value]");
        assertTrue(evaluator instanceof CombiningEvaluator.And);
    }

    @Test
    public void testParse_withQueryStartingWithCombinator_returnsEvaluatorWithRoot() {
        Evaluator evaluator = QueryParser.parse("> tagName");
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertTrue(((CombiningEvaluator.And) evaluator).evaluators().get(1) instanceof StructuralEvaluator.Root);
    }

    @Test
    public void testParse_withQueryContainingWhitespace_returnsEvaluatorWithParent() {
        Evaluator evaluator = QueryParser.parse("tagName attr");
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertTrue(((CombiningEvaluator.And) evaluator).evaluators().get(1) instanceof StructuralEvaluator.Parent);
    }

    @Test
    public void testParse_withMultipleCombinators_returnsCorrectEvaluator() {
        Evaluator evaluator = QueryParser.parse("tagName1 > tagName2 + tagName3 ~ tagName4");
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertEquals(5, ((CombiningEvaluator.And) evaluator).evaluators().size());
    }
}

