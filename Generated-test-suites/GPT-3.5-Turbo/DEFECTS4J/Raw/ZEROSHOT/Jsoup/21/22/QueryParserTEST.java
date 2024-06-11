package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div#id.class[attr=\"value\"]";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withCombinators() {
        String query = "div > span + p ~ a";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withMultipleSelectors() {
        String query = "div, span, p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withNestedSelectors() {
        String query = "div span p a";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withOr() {
        String query = "div, span p, a";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withWhitespace() {
        String query = "div span";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withClassSelector() {
        String query = ".className";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withIdSelector() {
        String query = "#itemId";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withTagSelector() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withAttributeSelector() {
        String query = "[attr=\"value\"]";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withAllElementsSelector() {
        String query = "*";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withIndexLessThanSelector() {
        String query = ":lt(2)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withIndexGreaterThanSelector() {
        String query = ":gt(2)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withIndexEqualsSelector() {
        String query = ":eq(2)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withHasSelector() {
        String query = ":has(span)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withContainsSelector() {
        String query = ":contains(text)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withContainsOwnSelector() {
        String query = ":containsOwn(text)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withMatchesSelector() {
        String query = ":matches(regex)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withMatchesOwnSelector() {
        String query = ":matchesOwn(regex)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withNotSelector() {
        String query = ":not(div)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

}