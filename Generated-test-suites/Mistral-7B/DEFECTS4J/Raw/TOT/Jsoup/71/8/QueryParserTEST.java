package org.jsoup.select.test;

import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test for QueryParser class
 */
public class QueryParserTest {

    @Test
    public void testParseSimpleSelector() {
        String query = "div.class1";
        Evaluator evaluator = QueryParser.parse(query);

        List<String> expectedElements = new ArrayList<>();
        expectedElements.add("div");
        expectedElements.add("class1");

        Assert.assertEquals(expectedElements.size(), evaluator.size());
        for (int i = 0; i < evaluator.size(); i++) {
            Evaluator eval = evaluator.get(i);
            Assert.assertEquals(expectedElements.get(i), eval.getClass().getSimpleName());
        }
    }

    @Test
    public void testParseComplexSelector() {
        String query = "div.class1 > div.class2 > ul > li:containsOwn(apple)";

        Evaluator evaluator = QueryParser.parse(query);

        List<String> expectedElements = new ArrayList<>();
        expectedElements.add("div");
        expectedElements.add("class1");
        expectedElements.add("div");
        expectedElements.add("class2");
        expectedElements.add("ul");
        expectedElements.add("li");
        expectedElements.add("containsOwn");
        expectedElements.add("apple");

        Assert.assertEquals(expectedElements.size(), evaluator.size());
        for (int i = 0; i < evaluator.size(); i++) {
            Evaluator eval = evaluator.get(i);
            Assert.assertEquals(expectedElements.get(i), eval.getClass().getSimpleName());
        }
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidSelector() {
        String query = "div.class1 > div.class2 > ul > li:contains(apple)"; // missing 'Own'

        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidCombinator() {
        String query = "div.class1 >~ div.class2 > ul > li:contains(apple)"; // unsupported combinator ~

        QueryParser.parse(query);
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "input[type=text][name=username]";

        Evaluator evaluator = QueryParser.parse(query);

        List<String> expectedElements = new ArrayList<>();
        expectedElements.add("input");
        expectedElements.add("type");
        expectedElements.add("text");
        expectedElements.add("name");
        expectedElements.add("username");

        Assert.assertEquals(expectedElements.size(), evaluator.size());
        for (int i = 0; i < evaluator.size(); i++) {
            Evaluator eval = evaluator.get(i);
            Assert.assertEquals(expectedElements.get(i), eval.getClass().getSimpleName());
        }
    }

    @Test
    public void testParsePseudoClassSelector() {
        String query = "input:disabled";

        Evaluator evaluator = QueryParser.parse(query);

        List<String> expectedElements = new ArrayList<>();
        expectedElements.add("input");
        expectedElements.add("isDisabled");

        Assert.assertEquals(expectedElements.size(), evaluator.size());
        for (int i = 0; i < evaluator.size(); i++) {
            Evaluator eval = evaluator.get(i);
            Assert.assertEquals(expectedElements.get(i), eval.getClass().getSimpleName());
        }
    }

    @Test
    public void testParsePseudoElementSelector() {
        String query = "input:focus > label";

        Evaluator evaluator = QueryParser.parse(query);

        List<String> expectedElements = new ArrayList<>();
        expectedElements.add("input");
        expectedElements.add("isFocused");
        expectedElements.add("label");

        Assert.assertEquals(expectedElements.size(), evaluator.size());
        for (int i = 0; i < evaluator.size(); i++) {
            Evaluator eval = evaluator.get(i);
            Assert.assertEquals(expectedElements.get(i), eval.getClass().getSimpleName());
        }
    }
}