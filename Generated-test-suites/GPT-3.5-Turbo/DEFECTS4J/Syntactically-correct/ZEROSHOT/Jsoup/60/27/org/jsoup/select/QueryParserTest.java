package org.jsoup.select;

import org.junit.Assert;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse("div.class > p#id[attr=value]");
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_InvalidQuery() {
        try {
            Evaluator evaluator = QueryParser.parse("div.class > p#id[attr=value");
            Assert.fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            // expected
        }
    }

    @Test
    public void testParse_EmptyQuery() {
        Evaluator evaluator = QueryParser.parse("");
        Assert.assertTrue(evaluator instanceof Evaluator.AllElements);
    }

    @Test
    public void testParse_ComplexQuery() {
        Evaluator evaluator = QueryParser.parse("div.class > p#id[attr=value], a.class");
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_WhitespaceQuery() {
        Evaluator evaluator = QueryParser.parse(" ");
        Assert.assertTrue(evaluator instanceof Evaluator.AllElements);
    }

    @Test
    public void testParse_OnlyCombinator() {
        Evaluator evaluator = QueryParser.parse(",");
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_SubQuery() {
        Evaluator evaluator = QueryParser.parse("div#id");
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_Combinator() {
        Evaluator evaluator = QueryParser.parse("> p");
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_AttributeQuery() {
        Evaluator evaluator = QueryParser.parse("[attr=value]");
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_IdQuery() {
        Evaluator evaluator = QueryParser.parse("#id");
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_ClassQuery() {
        Evaluator evaluator = QueryParser.parse(".class");
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_TagQuery() {
        Evaluator evaluator = QueryParser.parse("div");
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testParse_InvalidCombinator() {
        try {
            Evaluator evaluator = QueryParser.parse("div.class ^ p#id[attr=value]");
            Assert.fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            // expected
        }
    }

    @Test
    public void testParse_InvalidAttributeQuery() {
        try {
            Evaluator evaluator = QueryParser.parse("[attr!=value");
            Assert.fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            // expected
        }
    }

    @Test
    public void testParse_InvalidIdQuery() {
        try {
            Evaluator evaluator = QueryParser.parse("#");
            Assert.fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            // expected
        }
    }

    @Test
    public void testParse_InvalidClassQuery() {
        try {
            Evaluator evaluator = QueryParser.parse(". ");
            Assert.fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            // expected
        }
    }

    @Test
    public void testParse_InvalidTagQuery() {
        try {
            Evaluator evaluator = QueryParser.parse("div#");
            Assert.fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            // expected
        }
    }
}