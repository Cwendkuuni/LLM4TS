package org.jsoup.select;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import org.jsoup.helper.StringUtil;
import org.jsoup.parser.TokenQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.internal.Normalizer;

/**
 * Unit tests for QueryParser class.
 */
public class QueryParserTest {

    @Test
    public void testParseSimpleSelectors() {
        Evaluator expected;
        Evaluator parsed;

        String query = "tag";
        expected = new Evaluator.Tag("tag");
        parsed = QueryParser.parse(query);
        assertEquals(expected, parsed);

        query = "tag.class";
        expected = new Evaluator.Class("class");;
        parsed = QueryParser.parse(query);
        assertEquals(expected, parsed.getChild(0));
        assertEquals("tag", parsed.getClass().getSimpleName());

        query = "tag#id";
        expected = new Evaluator.Id("id");
        parsed = QueryParser.parse(query);
        assertEquals(expected, parsed);

        query = "#id";
        expected = new Evaluator.Id("id");
        parsed = QueryParser.parse(query);
        assertEquals(expected, parsed);

        query = "tag.class#id";
        expected = new Evaluator.Class("class");
        parsed = QueryParser.parse(query);
        assertEquals(expected, parsed.getChild(0));
        expected = new Evaluator.Id("id");
        assertEquals(expected, parsed.getChild(1));
        assertEquals("tag", parsed.getClass().getSimpleName());
    }

    @Test
    public void testParseCombinedSelectors() {
        Evaluator expected;
        Evaluator parsed;

        String query = "tag > tag > tag";
        expected = new CombiningEvaluator.And(
                new Evaluator.Tag("tag"),
                new StructuralEvaluator.Descendant(new Evaluator.Tag("tag"))
        );
        parsed = QueryParser.parse(query);
        assertEquals(expected, parsed);

        query = "tag > tag + tag";
        expected = new CombiningEvaluator.And(
                new Evaluator.Tag("tag"),
                new StructuralEvaluator.Descendant(new Evaluator.Tag("tag")),
                new StructuralEvaluator.AdjacentSibling(new Evaluator.Tag("tag"))
        );
        parsed = QueryParser.parse(query);
        assertEquals(expected, parsed);

        query = "tag ~ tag";
        expected = new CombiningEvaluator.And(
                new Evaluator.Tag("tag"),
                new StructuralEvaluator.PreviousSibling(new Evaluator.Tag("tag"))
        );
        parsed = QueryParser.parse(query);
        assertEquals(expected, parsed);

        query = "tag, tag";
        expected = new CombiningEvaluator.Or(
                new Evaluator.Tag("tag")
        );
        parsed = QueryParser.parse(query);
        assertEquals(expected, parsed);
        assertEquals(2, parsed.getChildren().size());
        assertEquals("tag", parsed.getChildren().get(1).getClass().getSimpleName());
    }

    @Test
    public void testParseAttributeSelectors() {
        String query;
        Evaluator parsed;

        query = "tag[attr]";
        parsed = QueryParser.parse(query);
        assertEquals(new Evaluator.Attribute("attr"), parsed);

        query = "tag[attr=value]";
        parsed = QueryParser.parse(query);
        assertEquals(new Evaluator.AttributeWithValue("attr", "value"), parsed);

        query = "tag[attr=value i]";
        parsed = QueryParser.parse(query);
        assertEquals(new Evaluator.AttributeWithValue("attr", "value i"), parsed);

        query = "tag[attr^='value']";
        parsed = QueryParser.parse(query);
        assertEquals(new Evaluator.AttributeStarting("attr", "value"), parsed);

        query = "tag[attr$='value']";
        parsed = QueryParser.parse(query);
        assertEquals(new Evaluator.AttributeEnding("attr", "value"), parsed);

        query = "tag[attr*='value']";
        parsed = QueryParser.parse(query);
        assertEquals(new Evaluator.AttributeWithValueContaining("attr", "value"), parsed);

        query = "tag[attr~='value']";
        parsed = QueryParser.parse(query);
        assertEquals(new Evaluator.AttributeWithValueMatching("attr", Pattern.compile("value")), parsed);
    }

    @Test
    public void testParsePseudoSelectors() {
        String query;
        Evaluator parsed;

        query = "tag:first-child";
        parsed = QueryParser.parse(query);
        assertEquals(new Evaluator.IsFirstChild(), parsed);

        query = "tag:last-child";
        parsed = QueryParser.parse(query);
        assertEquals(new Evaluator.IsLastChild(), parsed);

        query = "tag:only-child";
        parsed = QueryParser.parse(query);
        assertEquals(new Evaluator.IsOnlyChild(), parsed);

        query = "tag:only-of-type";
        parsed = QueryParser.parse(query);
        assertEquals(new Evaluator.IsOnlyOfType(), parsed);

        query = "tag:empty";
        parsed = QueryParser.parse(query);
        assertEquals(new Evaluator.IsEmpty(), parsed);

        query = "tag:root";
        parsed = QueryParser.parse(query);
        assertEquals(new Evaluator.IsRoot(), parsed);

        query = "tag:nth-child(n)";
        parsed = QueryParser.parse(query);
        assertEquals(new Evaluator.IsNthChild(1, Integer.parseInt(query.substring(query.indexOf("(")+1, query.indexOf(")")))), parsed);

        query = "tag:nth-last-child(n)";
        parsed = QueryParser.parse(query);
        assertEquals(new Evaluator.IsNthLastChild(1, Integer.parseInt(query.substring(query.indexOf("(")+1, query.indexOf(")")))), parsed);

        query = "tag:nth-of-type(n)";
        parsed = QueryParser.parse(query);
        assertEquals(new Evaluator.IsNthOfType(1, Integer.parseInt(query.substring(query.indexOf("(")+1, query.indexOf(")")))), parsed);

        query = "tag:nth-last-of-type(n)";
        parsed = QueryParser.parse(query);
        assertEquals(new Evaluator.IsNthLastOfType(1, Integer.parseInt(query.substring(query.indexOf("(")+1, query.indexOf(")")))), parsed);

        query = "tag:contains(text)";
        parsed = QueryParser.parse(query);
        assertEquals(new Evaluator.ContainsText("text"), parsed);

        query = "tag:containsOwn(text)";
        parsed = QueryParser.parse(query);
        assertEquals(new Evaluator.ContainsOwnText("text"), parsed);

        query = "tag:matches(regex)";
        parsed = QueryParser.parse(query);
        assertEquals(new Evaluator.Matches(Pattern.compile(query.substring(query.indexOf("(")+1, query.indexOf(")")))), parsed);

        query = "tag:matchesOwn(regex)";
        parsed = QueryParser.parse(query);
        assertEquals(new Evaluator.MatchesOwn(Pattern.compile(query.substring(query.indexOf("(")+1, query.indexOf(")")))), parsed);

        query = "tag:has(selector)";
        parsed = QueryParser.parse(query);
        assertEquals(new StructuralEvaluator.Has(QueryParser.parse(query.substring(query.indexOf("(")+1, query.indexOf(")")))), parsed);
    }

    @Test
    public void testParseInvalidSelectors() {
        String query;
        try {
            QueryParser.parse(query = "tag");
            assertFalse("Expected Selector.SelectorParseException for invalid query: " + query, true);
        } catch (Selector.SelectorParseException e) {
            // expected
        }

        try {
            QueryParser.parse(query = "tag >");
            assertFalse("Expected Selector.SelectorParseException for invalid query: " + query, true);
        } catch (Selector.SelectorParseException e) {
            // expected
        }

        try {
            QueryParser.parse(query = "tag . class");
            assertFalse("Expected Selector.SelectorParseException for invalid query: " + query, true);
        } catch (Selector.SelectorParseException e) {
            // expected
        }

        try {
            QueryParser.parse(query = "tag.class > tag > tag");
            assertFalse("Expected Selector.SelectorParseException for invalid query: " + query, true);
        } catch (Selector.SelectorParseException e) {
            // expected
        }

        try {
            QueryParser.parse(query = "tag[attr=value i]");
            assertFalse("Expected Selector.SelectorParseException for invalid query: " + query, true);
        } catch (Selector.SelectorParseException e) {
            // expected
        }

        try {
            QueryParser.parse(query = "tag:not(selector)");
            assertFalse("Expected Selector.SelectorParseException for invalid query: " + query, true);
        } catch (Selector.SelectorParseException e) {
            // expected
        }

        try {
            QueryParser.parse(query = "tag:not selector");
            assertFalse("Expected Selector.SelectorParseException for invalid query: " + query, true);
        } catch (Selector.SelectorParseException e) {
            // expected
        }

        try {
            QueryParser.parse(query = "tag:nth-child(-1)");
            assertFalse("Expected Selector.SelectorParseException for invalid query: " + query, true);
        } catch (Selector.SelectorParseException e) {
            // expected
        }

        try {
            QueryParser.parse(query = "tag:nth-child(-2)");
            assertFalse("Expected Selector.SelectorParseException for invalid query: " + query, true);
        } catch (Selector.SelectorParseException e) {
            // expected
        }

        try {
            QueryParser.parse(query = "tag:nth-child(n+1)");
            assertFalse("Expected Selector.SelectorParseException for invalid query: " + query, true);
        } catch (Selector.SelectorParseException e) {
            // expected
        }

        try {
            QueryParser.parse(query = "tag:nth-child(n+2)");
            assertFalse("Expected Selector.SelectorParseException for invalid query: " + query, true);
        } catch (Selector.SelectorParseException e) {
            // expected
        }

        try {
            QueryParser.parse(query = "tag:nth-child(n+3)");
            assertFalse("Expected Selector.SelectorParseException for invalid query: " + query, true);
        } catch (Selector.SelectorParseException e) {
            // expected
        }
    }
}