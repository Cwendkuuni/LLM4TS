package org.jsoup.select;

import static org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for the QueryParser class.
 */
public class QueryParserTest {

    @org.junit.Test
    public void testParseEmptyQuery() {
        QueryParser parser = new QueryParser("");
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.AllElements);
    }

    @org.junit.Test
    public void testParseRootQuery() {
        QueryParser parser = new QueryParser("*");
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.IsRoot);
    }

    @org.junit.Test
    public void testParseTagQuery() {
        QueryParser parser = new QueryParser("div");
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.Tag);
    }

    @org.junit.Test
    public void testParseTagWithNamespaceQuery() {
        QueryParser parser = new QueryParser("html:div");
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.TagEndsWith);
    }

    @org.junit.Test
    public void testParseClassQuery() {
        QueryParser parser = new QueryParser(".class");
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.Class);
    }

    @org.junit.Test
    public void testParseIdQuery() {
        QueryParser parser = new QueryParser("#id");
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.Id);
    }

    @org.junit.Test
    public void testParseAttributeQuery() {
        QueryParser parser = new QueryParser("[attribute]");
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.Attribute);
    }

    @org.junit.Test
    public void testParseAttributeWithValueQuery() {
        QueryParser parser = new QueryParser("[attribute=value]");
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
    }

    @org.junit.Test
    public void testParseAttributeStartingWithValueQuery() {
        QueryParser parser = new QueryParser("[attribute^=value]");
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueStarting);
    }

    @org.junit.Test
    public void testParseAttributeEndsWithValueQuery() {
        QueryParser parser = new QueryParser("[attribute$=value]");
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueEnding);
    }

    @org.junit.Test
    public void testParseAttributeContainsValueQuery() {
        QueryParser parser = new QueryParser("[attribute*=value]");
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueContaining);
    }

    @org.junit.Test
    public void testParseAttributeMatchingValueQuery() {
        QueryParser parser = new QueryParser("[attribute~=value]");
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueMatching);
    }

    @org.junit.Test
    public void testParseAttributeNotValueQuery() {
        QueryParser parser = new QueryParser("[attribute!
kotlin
=value]");
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueNot);
    }

    @org.junit.Test
    public void testParseAttributeSubstringValueQuery() {
        QueryParser parser = new QueryParser("[attribute*=subString]");
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.AttributeSubstring);
    }

    @org.junit.Test
    public void testParseAttributeRegexValueQuery() {
        QueryParser parser = new QueryParser("[attribute=~regex]");
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.AttributeRegex);
    }

    @org.junit.Test
    public void testParseDescendantSelector() {
        QueryParser parser = new QueryParser("div > p");
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        CombiningEvaluator.And and = (CombiningEvaluator.And) evaluator;
        assertTrue(and.size() == 2);
        assertTrue(and.get(0) instanceof Evaluator.IsTag);
        assertTrue(and.get(0).matches("div"));
        assertTrue(and.get(1) instanceof Evaluator.IsAncestor);
        assertTrue(and.get(1).matches("p"));
    }

    @org.junit.Test
    public void testParseAdjacentSiblingSelector() {
        QueryParser parser = new QueryParser("div > p + ul");
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        CombiningEvaluator.And and = (CombiningEvaluator.And) evaluator;
        assertTrue(and.size() == 2);
        assertTrue(and.get(0) instanceof Evaluator.IsTag);
        assertTrue(and.get(0).matches("div"));
        assertTrue(and.get(1) instanceof Evaluator.IsPreviousSibling);
        assertTrue(and.get(1).matches("ul"));
    }

    @org.junit.Test
    public void testParseGeneralSiblingSelector() {
        QueryParser parser = new QueryParser("div > p ~ ul");
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        CombiningEvaluator.And and = (CombiningEvaluator.And) evaluator;
        assertTrue(and.size() == 2);
        assertTrue(and.get(0) instanceof Evaluator.IsTag);
        assertTrue(and.get(0).matches("div"));
        assertTrue(and.get(1) instanceof Evaluator.IsPreviousSiblingOrSame);
        assertTrue(and.get(1).matches("ul"));
    }

    @org.junit.Test
    public void testParseCombinatorError() {
        QueryParser parser = new QueryParser("div > p + x");
        try {
            Evaluator evaluator = parser.parse();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testParseNthChildSelector() {
        QueryParser parser = new QueryParser("div:nth-child(2)");
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.IsNthChild);
        Evaluator.IsNthChild nthChild = (Evaluator.IsNthChild) evaluator;
        assertEquals(2, nthChild.getNthChildIndex());
    }

    @org.junit.Test
    public void testParseNthLastChildSelector() {
        QueryParser parser = new QueryParser("div:nth-last-child(2)");
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.IsNthLastChild);
        Evaluator.IsNthLastChild nthLastChild = (Evaluator.IsNthLastChild) evaluator;
        assertEquals(2, nthLastChild.getNthLastChildIndex());
    }

    @org.junit.Test
    public void testParseNthOfTypeSelector() {
        QueryParser parser = new QueryParser("div:nth-of-type(2)");
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.IsNthOfType);
        Evaluator.IsNthOfType nthOfType = (Evaluator.IsNthOfType) evaluator;
        assertEquals(2, nthOfType.getNthOfTypeIndex());
    }

    @org.junit.Test
    public void testParseNthLastOfTypeSelector() {
        QueryParser parser = new QueryParser("div:nth-last-of-type(2)");
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.IsNthLastOfType);
        Evaluator.IsNthLastOfType nthLastOfType = (Evaluator.IsNthLastOfType) evaluator;
        assertEquals(2, nthLastOfType.getNthLastOfTypeIndex());
    }

    @org.junit.Test
    public void testParseHasSelector() {
        QueryParser parser = new QueryParser("div:has(p)");
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof StructuralEvaluator.Has);
        StructuralEvaluator.Has has = (StructuralEvaluator.Has) evaluator;
        assertTrue(has.getChildEvaluator() instanceof Evaluator.IsTag);
        assertTrue(has.getChildEvaluator().matches("p"));
    }

    @org.junit.Test
    public void testParseContainsTextSelector() {
        QueryParser parser = new QueryParser("div:contains(text)");
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.ContainsText);
        Evaluator.ContainsText containsText = (Evaluator.ContainsText) evaluator;
        assertEquals("text", containsText.getText());
    }

    @org.junit.Test
    public void testParseContainsOwnTextSelector() {
        QueryParser parser = new QueryParser("div:containsOwn(text)");
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.ContainsOwnText);
        Evaluator.ContainsOwnText containsOwnText = (Evaluator.ContainsOwnText) evaluator;
        assertEquals("text", containsOwnText.getText());
    }

    @org.junit.Test
    public void testParseContainsDataSelector() {
        QueryParser parser = new QueryParser("div:containsData(data)");
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.ContainsData);
        Evaluator.ContainsData containsData = (Evaluator.ContainsData) evaluator;
        assertEquals("data", containsData.getData());
    }

    @org.junit.Test
    public void testParseMatchesSelector() {
        QueryParser parser = new QueryParser("div:matches(regex)");
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.Matches);
        Evaluator.Matches matches = (Evaluator.Matches) evaluator;
        assertEquals(Pattern.compile(parser.getQuery().substring(6, parser.getQuery().length())), matches.getPattern());
    }

    @org.junit.Test
    public void testParseMatchesOwnSelector() {
        QueryParser parser = new QueryParser("div:matchesOwn(regex)");
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.MatchesOwn);
        Evaluator.MatchesOwn matchesOwn = (Evaluator.MatchesOwn) evaluator;
        assertEquals(Pattern.compile(parser.getQuery().substring(11, parser.getQuery().length())), matchesOwn.getPattern());
    }

    @org.junit.Test
    public void testParseNotSelector() {
        QueryParser parser = new QueryParser("div:not(p)");
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof StructuralEvaluator.Not);
        StructuralEvaluator.Not not = (StructuralEvaluator.Not) evaluator;
        assertTrue(not.getChildEvaluator() instanceof Evaluator.IsTag);
        assertTrue(not.getChildEvaluator().matches("p"));
    }
}