package org.jsoup.select;
import org.junit.Test;
import org.jsoup.select.Evaluator;
import org.jsoup.select.QueryParser;

import static org.junit.Assert.assertEquals;

public class QueryParserTest {

    @Test
    public void testParse() {
        // Test basic CSS query
        Evaluator evaluator = QueryParser.parse("div");
        assertEquals("Evaluator.Tag", evaluator.getClass().getSimpleName());

        // Test query with combinator
        evaluator = QueryParser.parse("div > p");
        assertEquals("CombiningEvaluator.And", evaluator.getClass().getSimpleName());

        // Test query with attribute selector
        evaluator = QueryParser.parse("a[href]");
        assertEquals("Evaluator.Attribute", evaluator.getClass().getSimpleName());
    }

    @Test
    public void testCombinator() {
        Evaluator evaluator = QueryParser.parse("div > p");
        assertEquals("CombiningEvaluator.And", evaluator.getClass().getSimpleName());

        evaluator = QueryParser.parse("div + p");
        assertEquals("CombiningEvaluator.And", evaluator.getClass().getSimpleName());

        evaluator = QueryParser.parse("div ~ p");
        assertEquals("CombiningEvaluator.And", evaluator.getClass().getSimpleName());

        evaluator = QueryParser.parse("div, p");
        assertEquals("CombiningEvaluator.Or", evaluator.getClass().getSimpleName());
    }

    @Test
    public void testAttributeSelector() {
        Evaluator evaluator = QueryParser.parse("a[href]");
        assertEquals("Evaluator.Attribute", evaluator.getClass().getSimpleName());

        evaluator = QueryParser.parse("a[href=\"https://example.com\"]");
        assertEquals("Evaluator.AttributeWithValue", evaluator.getClass().getSimpleName());

        evaluator = QueryParser.parse("a[href^=\"https://\"]");
        assertEquals("Evaluator.AttributeWithValueStarting", evaluator.getClass().getSimpleName());

        evaluator = QueryParser.parse("a[href$=\".com\"]");
        assertEquals("Evaluator.AttributeWithValueEnding", evaluator.getClass().getSimpleName());

        evaluator = QueryParser.parse("a[href*=\"example\"]");
        assertEquals("Evaluator.AttributeWithValueContaining", evaluator.getClass().getSimpleName());

        evaluator = QueryParser.parse("a[href~=\"example\"]");
        assertEquals("Evaluator.AttributeWithValueMatching", evaluator.getClass().getSimpleName());
    }

    // TODO: Add more test cases for other methods

}

