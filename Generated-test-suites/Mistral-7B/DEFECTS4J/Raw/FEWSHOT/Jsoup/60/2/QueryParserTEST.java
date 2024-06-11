package org.jsoup.select;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        QueryParser parser = new QueryParser("");
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testParseTagSelector() {
        String query = "div";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Tag);
    }

    @Test
    public void testParseClassSelector() {
        String query = ".button";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Class);
    }

    @Test
    public void testParseIdSelector() {
        String query = "#header";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Id);
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "[title]";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Attribute);
    }

    @Test
    public void testParseCombinedSelectors() {
        String query = "div.button#header";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertTrue(((CombiningEvaluator.And) evaluator).getSize() == 3);
        assertTrue(((CombiningEvaluator.And) evaluator).getEvaluators().get(0) instanceof Evaluator.Tag);
        assertTrue(((CombiningEvaluator.And) evaluator).getEvaluators().get(1) instanceof Evaluator.Class);
        assertTrue(((CombiningEvaluator.And) evaluator).getEvaluators().get(2) instanceof Evaluator.Id);
    }

    @Test
    public void testParseDescendantSelector() {
        String query = "div p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertTrue(((CombiningEvaluator.And) evaluator).getSize() == 2);
        assertTrue(((CombiningEvaluator.And) evaluator).getEvaluators().get(0) instanceof Evaluator.Tag);
        assertTrue(((CombiningEvaluator.And) evaluator).getEvaluators().get(1) instanceof Evaluator.ImmediateChild);
    }

    @Test
    public void testParseAttributeValueSelector() {
        String query = "input[type=text]";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertTrue(((CombiningEvaluator.And) evaluator).getSize() == 2);
        assertTrue(((CombiningEvaluator.And) evaluator).getEvaluators().get(0) instanceof Evaluator.Tag);
        assertTrue(((CombiningEvaluator.And) evaluator).getEvaluators().get(1) instanceof Evaluator.AttributeWithValue);
    }

    @Test
    public void testParseAttributeValueSelectorWithOperator() {
        String query = "input[type!='button']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertTrue(((CombiningEvaluator.And) evaluator).getSize() == 2);
        assertTrue(((CombiningEvaluator.And) evaluator).getEvaluators().get(0) instanceof Evaluator.Tag);
        assertTrue(((CombiningEvaluator.And) evaluator).getEvaluators().get(1) instanceof Evaluator.AttributeWithValueNot);
    }

    @Test
    public void testParseAttributeValueSelectorWithPrefix() {
        String query = "input[data-type]";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertTrue(((CombiningEvaluator.And) evaluator).getSize() == 2);
        assertTrue(((CombiningEvaluator.And) evaluator).getEvaluators().get(0) instanceof Evaluator.Tag);
        assertTrue(((CombiningEvaluator.And) evaluator).getEvaluators().get(1) instanceof Evaluator.AttributeWithValue);
    }

    @Test
    public void testParsePseudoClassSelector() {
        String query = "input:disabled";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.PseudoClass);
    }

    @Test
    public void testParseNthChildSelector() {
        String query = "li:nth-child(3)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.IsNthChild);
        assertTrue(((Evaluator.IsNthChild) evaluator).getIndex() == 3);
    }

    @Test
    public void testParseNthOfTypeSelector() {
        String query = "li:nth-of-type(3)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.IsNthOfType);
        assertTrue(((Evaluator.IsNthOfType) evaluator).getIndex() == 3);
    }

    @Test
    public void testParseNthLastChildSelector() {
        String query = "li:nth-last-child(3)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.IsNthLastChild);
        assertTrue(((Evaluator.IsNthLastChild) evaluator).getIndex() == 3);
    }

    @Test
    public void testParseNthLastOfTypeSelector() {
        String query = "li:nth-last-of-type(3)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.IsNthLastOfType);
        assertTrue(((Evaluator.IsNthLastOfType) evaluator).getIndex() == 3);
    }

    @Test
    public void testParseHasSelector() {
        String query = "li:has(.active)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Has);
    }

    @Test
    public void testParseContainsSelector() {
        String query = "li:contains(text)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.ContainsText);
    }

    @Test
    public void testParseContainsDataSelector() {
        String query = "li:containsData(data)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.ContainsData);
    }

    @Test
    public void testParseNotSelector() {
        String query = "li:not(.active)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Not);
    }
}