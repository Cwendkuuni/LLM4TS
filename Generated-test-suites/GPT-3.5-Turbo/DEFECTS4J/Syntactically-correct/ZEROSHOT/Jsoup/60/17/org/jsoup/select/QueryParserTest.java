package org.jsoup.select;

import static org.junit.Assert.*;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div.menu a.active";
        Evaluator evaluator = QueryParser.parse(query);

        assertNotNull(evaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
    }

    @Test
    public void testParse_withCombinator() {
        String query = "div > a";
        Evaluator evaluator = QueryParser.parse(query);

        assertNotNull(evaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
    }

    @Test
    public void testParse_withAttribute() {
        String query = "[src^=https://]";
        Evaluator evaluator = QueryParser.parse(query);

        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.AttributeStarting);
    }

    @Test
    public void testParse_withPseudoSelector() {
        String query = ":has(div)";
        Evaluator evaluator = QueryParser.parse(query);

        assertNotNull(evaluator);
        assertTrue(evaluator instanceof StructuralEvaluator.Has);
    }

    @Test
    public void testParse_withNegation() {
        String query = ":not(a)";
        Evaluator evaluator = QueryParser.parse(query);

        assertNotNull(evaluator);
        assertTrue(evaluator instanceof StructuralEvaluator.Not);
    }
}