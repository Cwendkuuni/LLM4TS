package org.jsoup.select;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.jsoup.parser.TokenQueue;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        QueryParser parser = new QueryParser("");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AllElements);
    }

    @Test
    public void testParseTagQuery() {
        QueryParser parser = new QueryParser("tag");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("tag", evaluator.toString());
    }

    @Test
    public void testParseTagWithNamespaceQuery() {
        QueryParser parser = new QueryParser("namespace:tag");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("namespace:tag", evaluator.toString());
    }

    @Test
    public void testParseClassQuery() {
        QueryParser parser = new QueryParser(".class");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals(".class", evaluator.toString());
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
        QueryParser parser = new QueryParser("[class='class']");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Attribute);
        assertEquals("[class='class']", evaluator.toString());
    }

    @Test
    public void testParseAttributeWithValueQuery() {
        QueryParser parser = new QueryParser("[class='class' and class='other']");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
        assertEquals("[class='class' and class='other']", evaluator.toString());
    }

    @Test
    public void testParseAttributeStartingWithValueQuery() {
        QueryParser parser = new QueryParser("[class^='prefix']");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueStarting);
        assertEquals("[class^='prefix']", evaluator.toString());
    }

    @Test
    public void testParseAttributeEndingWithValueQuery() {
        QueryParser parser = new QueryParser("[class$='suffix']");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueEnding);
        assertEquals("[class$='suffix']", evaluator.toString());
    }

    @Test
    public void testParseAttributeContainingValueQuery() {
        QueryParser parser = new QueryParser("[class*='prefix']");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueContaining);
        assertEquals("[class*='prefix']", evaluator.toString());
    }

    @Test
    public void testParseAttributeMatchingValueQuery() {
        QueryParser parser = new QueryParser("[class~='prefix']");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueMatching);
        assertEquals("[class~='prefix']", evaluator.toString());
    }

    @Test
    public void testParseAttributeSubstringQuery() {
        QueryParser parser = new QueryParser("[class*='sub']");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithSubstring);
        assertEquals("[class*='sub']", evaluator.toString());
    }

    @Test
    public void testParsePseudoClassQuery() {
        QueryParser parser = new QueryParser(":first-child");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsFirstChild);
        assertEquals(":first-child", evaluator.toString());
    }

    @Test
    public void testParsePseudoClassWithAttributeQuery() {
        QueryParser parser = new QueryParser(":focus > input");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsFocused);
        assertEquals(":focus", evaluator.toString());
    }

    @Test
    public void testParseCombinatorQuery() {		
        QueryParser parser = new QueryParser("div > p > span");
        Evaluator evaluator = parser.parse();
        assertFalse(evaluator instanceof Evaluator.Tag);
        assertTrue(evaluator instanceof CombiningEvaluator);
        assertEquals("div > p > span", evaluator.toString());
    }

    @Test
    public void testParseCombinatorQueryWithOr() {
        QueryParser parser = new QueryParser("div > p > span, div > ul > li > a");
        Evaluator evaluator = parser.parse();
        assertFalse(evaluator instanceof Evaluator.Tag);
        assertTrue(evaluator instanceof CombiningEvaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.Or);
        assertEquals("div > p > span, div > ul > li > a", evaluator.toString());
    }

    @Test
    public void testParseCombinatorQueryWithAnd() {
        QueryParser parser = new QueryParser("div > p > span.red.active");
        Evaluator evaluator = parser.parse();
        assertFalse(evaluator instanceof Evaluator.Tag);
        assertTrue(evaluator instanceof CombiningEvaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertEquals("div > p > span.red.active", evaluator.toString());
    }

    @Test
    public void testParseNthChildQuery() {
        QueryParser parser = new QueryParser(":nth-child(2)");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsNthChild);
        assertEquals(":nth-child(2)", evaluator.toString());
    }

    @Test
    public void testParseNthChildQueryWithOfType() {
        QueryParser parser = new QueryParser(":nth-of-type(2)");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsNthOfType);
        assertEquals(":nth-of-type(2)", evaluator.toString());
    }

    @Test
    public void testParseNthChildQueryWithBackwards() {
        QueryParser parser = new QueryParser(":nth-last-child(2)");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsNthLastChild);
        assertEquals(":nth-last-child(2)", evaluator.toString());
    }

    @Test
    public void testParseNthChildQueryWithOfTypeAndBackwards() {
        QueryParser parser = new QueryParser(":nth-last-of-type(2)");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsNthLastOfType);
        assertEquals(":nth-last-of-type(2)", evaluator.toString());
    }

    @Test
    public void testParseNegatedQuery() {
        QueryParser parser = new QueryParser(":not(:first-child)");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Not);
        assertEquals(":not(:first-child)", evaluator.toString());
    }

    @Test
    public void testParseQueryWithMultipleSelectors() {
        QueryParser parser = new QueryParser("div > p, div > ul > li > a");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof CombiningEvaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.Or);
        assertEquals("div > p, div > ul > li > a", evaluator.toString());
    }
}