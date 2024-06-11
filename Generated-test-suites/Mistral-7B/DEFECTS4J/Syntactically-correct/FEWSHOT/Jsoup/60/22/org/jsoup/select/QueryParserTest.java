package org.jsoup.select;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.jsoup.parser.TokenQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        QueryParser parser = new QueryParser("");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AllElements);
    }

    @Test
    public void testParseTagQuery() {
        QueryParser parser = new QueryParser("div");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("div", evaluator.toString());
    }

    @Test
    public void testParseClassQuery() {
        QueryParser parser = new QueryParser(".classname");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals(".classname", evaluator.toString());
    }

    @Test
    public void testParseIdQuery() {
        QueryParser parser = new QueryParser("#id");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals("#id", evaluator.toString());
    }

    @Test
    public void testParseAttributeQuery() {
        QueryParser parser = new QueryParser("[attribute]");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Attribute);
        assertEquals("[attribute]", evaluator.toString());
    }

    @Test
    public void testParseAttributeValueQuery() {
        QueryParser parser = new QueryParser("[attribute=value]");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
        assertEquals("[attribute=value]", evaluator.toString());
    }

    @Test
    public void testParseAttributeStartsWithQuery() {
        QueryParser parser = new QueryParser("[attribute^=value]");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueStarting);
        assertEquals("[attribute^=value]", evaluator.toString());
    }

    @Test
    public void testParseAttributeEndsWithQuery() {
        QueryParser parser = new QueryParser("[attribute$=value]");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueEnding);
        assertEquals("[attribute$=value]", evaluator.toString());
    }

    @Test
    public void testParseAttributeContainsQuery() {
        QueryParser parser = new QueryParser("[attribute*=value]");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueContaining);
        assertEquals("[attribute*=value]", evaluator.toString());
    }

    @Test
    public void testParseAttributeMatchingQuery() {
        QueryParser parser = new QueryParser("[attribute~=value]");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueMatching);
        assertEquals("[attribute~=value]", evaluator.toString());
    }

    @Test
    public void testParseAttributeNotQuery() {
        QueryParser parser = new QueryParser("[attribute!=[value]]");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueNot);
        assertEquals("[attribute!=[value]", evaluator.toString());
    }

    @Test
    public void testParseCombinedQuery() {
        QueryParser parser = new QueryParser("div.classname");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertEquals("div", ((CombiningEvaluator.And) evaluator).getLeft().toString());
        assertEquals(".classname", ((CombiningEvaluator.And) evaluator).getRight().toString());
    }

    @Test
    public void testParseCombinedQueryWithOr() {
        QueryParser parser = new QueryParser("div.class1, div.class2");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof CombiningEvaluator.Or);
        assertTrue(((CombiningEvaluator.Or) evaluator).getLeft() instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) ((CombiningEvaluator.Or) evaluator).getLeft()).toString());
        assertTrue(((CombiningEvaluator.Or) evaluator).getRight() instanceof Evaluator.Tag);
        assertEquals(".class2", ((Evaluator.Tag) ((CombiningEvaluator.Or) evaluator).getRight()).toString());
    }

    @Test
    public void testParseCombinedQueryWithDescendant() {
        QueryParser parser = new QueryParser("div > p");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof StructuralEvaluator.Descendant);
        assertEquals("div", ((StructuralEvaluator.Descendant) evaluator).getParent().toString());
        assertEquals("p", ((StructuralEvaluator.Descendant) evaluator).getChild().toString());
    }

    @Test
    public void testParseCombinedQueryWithAdjacent() {
        QueryParser parser = new QueryParser("div + p");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof StructuralEvaluator.AdjacentSibling);
        assertEquals("div", ((StructuralEvaluator.AdjacentSibling) evaluator).getPreviousSibling().toString());
        assertEquals("p", ((StructuralEvaluator.AdjacentSibling) evaluator).getTarget().toString());
    }

    @Test
    public void testParseCombinedQueryWithPreviousSibling() {
        QueryParser parser = new QueryParser("div ~ p");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof StructuralEvaluator.PreviousSibling);
        assertEquals("div", ((StructuralEvaluator.PreviousSibling) evaluator).getPreviousSibling().toString());
        assertEquals("p", ((StructuralEvaluator.PreviousSibling) evaluator).getTarget().toString());
    }

    @Test
    public void testParseCombinedQueryWithChildren() {
        QueryParser parser = new QueryParser("div > *");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof StructuralEvaluator.ImmediateChildren);
        assertEquals("div", evaluator.toString());
    }

    @Test
    public void testParseCombinedQueryWithLastChild() {
        QueryParser parser = new QueryParser("div:last-child");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsLastChild);
        assertEquals("div", evaluator.toString());
    }

    @Test
    public void testParseCombinedQueryWithFirstChild() {
        QueryParser parser = new QueryParser("div:first-child");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsFirstChild);
        assertEquals("div", evaluator.toString());
    }

    @Test
    public void testParseCombinedQueryWithFirstOfType() {
        QueryParser parser = new QueryParser("div:first-of-type");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsFirstOfType);
        assertEquals("div", evaluator.toString());
    }

    @Test
    public void testParseCombinedQueryWithLastOfType() {
        QueryParser parser = new QueryParser("div:last-of-type");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsLastOfType);
        assertEquals("div", evaluator.toString());
    }

    @Test
    public void testParseCombinedQueryWithOnlyChild() {
        QueryParser parser = new QueryParser("div:only-child");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsOnlyChild);
        assertEquals("div", evaluator.toString());
    }

    @Test
    public void testParseCombinedQueryWithOnlyOfType() {
        QueryParser parser = new QueryParser("div:only-of-type");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsOnlyOfType);
        assertEquals("div", evaluator.toString());
    }

    @Test
    public void testParseCombinedQueryWithEmpty() {
        QueryParser parser = new QueryParser("div:empty");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsEmpty);
        assertEquals("div", evaluator.toString());
    }

    @Test
    public void testParseCombinedQueryWithRoot() {
        QueryParser parser = new QueryParser(":root");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsRoot);
        assertEquals(":root", evaluator.toString());
    }

    @Test
    public void testParseCombinedQueryWithNthChild() {
        QueryParser parser = new QueryParser("div:nth-child(2)");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsNthChild);
        assertEquals("div", evaluator.toString());
        assertEquals(2, ((Evaluator.IsNthChild) evaluator).getIndex());
    }

    @Test
    public void testParseCombinedQueryWithNthLastChild() {
        QueryParser parser = new QueryParser("div:nth-last-child(2)");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsNthLastChild);
        assertEquals("div", evaluator.toString());
        assertEquals(2, ((Evaluator.IsNthLastChild) evaluator).getIndex());
    }

    @Test
    public void testParseCombinedQueryWithNthOfType() {
        QueryParser parser = new QueryParser("div:nth-of-type(2)");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsNthOfType);
        assertEquals("div", evaluator.toString());
        assertEquals(2, ((Evaluator.IsNthOfType) evaluator).getIndex());
    }

    @Test
    public void testParseCombinedQueryWithNthLastOfType() {
        QueryParser parser = new QueryParser("div:nth-last-of-type(2)");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsNthLastOfType);
        assertEquals("div", evaluator.toString());
        assertEquals(2, ((Evaluator.IsNthLastOfType) evaluator).getIndex());
    }

    @Test
    public void testParseCombinedQueryWithHas() {
        QueryParser parser = new QueryParser("div:has(p)");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof StructuralEvaluator.Has);
        assertEquals("div", evaluator.toString());
        assertTrue(((StructuralEvaluator.Has) evaluator).getChild() instanceof Evaluator.Tag);
        assertEquals("p", ((Evaluator.Tag) ((StructuralEvaluator.Has) evaluator).getChild()).toString());
    }

    @Test
    public void testParseCombinedQueryWithContains() {
        QueryParser parser = new QueryParser("div:contains(text)");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.ContainsText);
        assertEquals("div", evaluator.toString());
        assertEquals("text", ((Evaluator.ContainsText) evaluator).getText());
    }

    @Test
    public void testParseCombinedQueryWithContainsOwn() {
        QueryParser parser = new QueryParser("div:containsOwn(text)");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.ContainsOwnText);
        assertEquals("div", evaluator.toString());
        assertEquals("text", ((Evaluator.ContainsOwnText) evaluator).getText());
    }

    @Test
    public void testParseCombinedQueryWithContainsData() {
        QueryParser parser = new QueryParser("div:containsData(data)");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.ContainsData);
        assertEquals("div", evaluator.toString());
        assertEquals("data", ((Evaluator.ContainsData) evaluator).getData());
    }

    @Test
    public void testParseCombinedQueryWithMatches() {
        QueryParser parser = new QueryParser("div:matches(regex)");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Matches);
        assertEquals("div", evaluator.toString());
        assertEquals(Pattern.compile(parser.query), ((Evaluator.Matches) evaluator).getPattern());
    }

    @Test
    public void testParseCombinedQueryWithMatchesOwn() {
        QueryParser parser = new QueryParser("div:matchesOwn(regex)");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.MatchesOwn);
        assertEquals("div", evaluator.toString());
        assertEquals(Pattern.compile(parser.query), ((Evaluator.MatchesOwn) evaluator).getPattern());
    }

    @Test
    public void testParseCombinedQueryWithNot() {
        QueryParser parser = new QueryParser("div:not(p)");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof StructuralEvaluator.Not);
        assertEquals("div", evaluator.toString());
        assertTrue(((StructuralEvaluator.Not) evaluator).getChild() instanceof Evaluator.Tag);
        assertEquals("p", ((Evaluator.Tag) ((StructuralEvaluator.Not) evaluator).getChild()).toString());
    }
}