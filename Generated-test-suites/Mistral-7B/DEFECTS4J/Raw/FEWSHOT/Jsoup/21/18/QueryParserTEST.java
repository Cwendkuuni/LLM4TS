package org.jsoup.select;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.StringUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        Evaluator evaluator = QueryParser.parse("");
        assertNull(evaluator);
    }

    @Test
    public void testParseSingleElementSelector() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals(evaluator.toString(), query);
    }

    @Test
    public void testParseMultipleElementSelectors() {
        String query = "div, p";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof CombiningEvaluator.Or);
        List<Evaluator> children = ((CombiningEvaluator.Or) evaluator).getChildren();
        assertEquals(children.size(), 2);
        assertTrue(children.get(0) instanceof Evaluator.Tag);
        assertEquals(children.get(0).toString(), "div");
        assertTrue(children.get(1) instanceof Evaluator.Tag);
        assertEquals(children.get(1).toString(), "p");
    }

    @Test
    public void testParseClassSelector() {
        String query = ".button";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals(evaluator.toString(), query);
    }

    @Test
    public void testParseIDSelector() {
        String query = "#header";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals(evaluator.toString(), query);
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "[href]";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Attribute);
        assertEquals(evaluator.toString(), query);
    }

    @Test
    public void testParseAttributeValueSelector() {
        String query = "[href='https://example.com']";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
        assertEquals(evaluator.toString(), query);
    }

    @Test
    public void testParseAttributeValueSelectorWithNotOperator() {
        String query = "[href!='https://example.com']";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof CombiningEvaluator.Not);
        Evaluator child = ((CombiningEvaluator.Not) evaluator).getChildren().get(0);
        assertTrue(child instanceof Evaluator.AttributeWithValue);
        assertEquals(child.toString(), "[href='https://example.com']");
    }

    @Test
    public void testParseAttributeValueSelectorWithStartsWithOperator() {
        String query = "[href^='https']";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueStarting);
        assertEquals(evaluator.toString(), query);
    }

    @Test
    public void testParseAttributeValueSelectorWithEndsWithOperator() {
        String query = "[href$='.html']";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueEnding);
        assertEquals(evaluator.toString(), query);
    }

    @Test
    public void testParseAttributeValueSelectorWithContainsOperator() {
        String query = "[title*='apple']";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueContaining);
        assertEquals(evaluator.toString(), query);
    }

    @Test
    public void testParseAttributeValueSelectorWithMatchesOperator() {
        String query = "[title~='banana']";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueMatching);
        assertTrue(((Evaluator.AttributeWithValueMatching) evaluator).getPattern().matcher("banana apple").matches());
        assertTrue(((Evaluator.AttributeWithValueMatching) evaluator).getPattern().matcher("apple banana").matches());
    }

    @Test
    public void testParseComplexSelector() {
        String query = "button.button-primary:not([disabled])";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        List<Evaluator> children = ((CombiningEvaluator.And) evaluator).getChildren();
        assertEquals(children.size(), 2);
        Evaluator firstChild = children.get(0);
        assertTrue(firstChild instanceof CombiningEvaluator.And);
        List<Evaluator> firstGrandChildren = ((CombiningEvaluator.And) firstChild).getChildren();
        assertEquals(firstGrandChildren.size(), 2);
        assertTrue(firstGrandChildren.get(0) instanceof Evaluator.Class);
        assertEquals(firstGrandChildren.get(0).toString(), ".button-primary");
        assertTrue(firstGrandChildren.get(1) instanceof Evaluator.Not);
        Evaluator secondGrandChild = ((Evaluator.Not) firstGrandChildren.get(1)).getChildren().get(0);
        assertTrue(secondGrandChild instanceof Evaluator.Attribute);
        assertEquals(secondGrandChild.toString(), "[disabled]");
        Evaluator secondChild = children.get(1);
        assertTrue(secondChild instanceof Evaluator.Tag);
        assertEquals(secondChild.toString(), "button");
    }
}