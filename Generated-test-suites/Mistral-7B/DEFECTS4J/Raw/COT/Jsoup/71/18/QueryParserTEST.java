package org.jsoup.select;

import static org.junit.Assert.*;

import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;

import java.util.ArrayList;
import java.util.List;

public class QueryParserTest {

    @org.junit.Test
    public void testParseEmptyQuery() {
        String query = "";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.AllElements);
    }

    @org.junit.Test
    public void testParseSimpleTagQuery() {
        String query = "p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals(evaluator.toString(), "p");
    }

    @org.junit.Test
    public void testParseClassQuery() {
        String query = ".class1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals(evaluator.toString(), ".class1");
    }

    @org.junit.Test
    public void testParseIdQuery() {
        String query = "#id1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals(evaluator.toString(), "#id1");
    }

    @org.junit.Test(expected = org.jsoup.selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        String query = "!";
        QueryParser parser = new QueryParser(query);
        parser.parse();
        fail("Expected SelectorParseException");
    }

    @org.junit.Test
    public void testParseCombinedQuery() {
        String query = "p > .class1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) evaluator;
        Evaluator tagEvaluator = andEvaluator.getLeftEvaluator();
        Evaluator classEvaluator = andEvaluator.getRightEvaluator();
        assertTrue(tagEvaluator instanceof Evaluator.Tag);
        assertEquals(tagEvaluator.toString(), "p");
        assertTrue(classEvaluator instanceof Evaluator.Class);
        assertEquals(classEvaluator.toString(), ".class1");
    }

    @org.junit.Test
    public void testParseAttributeQuery() {
        String query = "input[type=text]";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.Attribute);
        AttributeEvaluator attributeEvaluator = (AttributeEvaluator) evaluator;
        assertEquals(attributeEvaluator.getKey(), "type");
        assertEquals(attributeEvaluator.getValue(), "text");
    }

    @org.junit.Test
    public void testParseAttributeValueQuery() {
        String query = "input[type='text']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.Attribute);
        AttributeEvaluator attributeEvaluator = (AttributeEvaluator) evaluator;
        assertEquals(attributeEvaluator.getKey(), "type");
        assertEquals(attributeEvaluator.getValue(), "'text'");
    }

    @org.junit.Test
    public void testParseAttributeStartingValueQuery() {
        String query = "input[type^='text']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueStarting);
        assertEquals(evaluator.toString(), "[type^='text']");
    }

    @org.junit.Test
    public void testParseAttributeEndingValueQuery() {
        String query = "input[type$='text']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueEnding);
        assertEquals(evaluator.toString(), "[type$='text']");
    }

    @org.junit.Test
    public void testParseAttributeContainsValueQuery() {
        String query = "input[type*='text']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueContaining);
        assertEquals(evaluator.toString(), "[type*='text']");
    }

    @org.junit.Test
    public void testParseAttributeMatchingValueQuery() {
        String query = "input[type~='text']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueMatching);
        assertEquals(evaluator.toString(), "[type~='text']");
    }

    @org.junit.Test(expected = org.jsoup.selector.SelectorParseException.class)
    public void testParseInvalidAttributeQuery() {
        String query = "input[type=text^]";
        QueryParser parser = new QueryParser(query);
        parser.parse();
        fail("Expected SelectorParseException");
    }

    @org.junit.Test(expected = org.jsoup.selector.SelectorParseException.class)
    public void testParseInvalidCombinedQuery() {
        String query = "p > .class1 ^ .class2";
        QueryParser parser = new QueryParser(query);
        parser.parse();
        fail("Expected SelectorParseException");
    }

    @org.junit.Test(expected = org.jsoup.selector.SelectorParseException.class)
    public void testParseInvalidCombinedQueryWithOr() {
        String query = "p > .class1, .class2";
        QueryParser parser = new QueryParser(query);
        parser.parse();
        fail("Expected SelectorParseException");
    }

    @org.junit.Test
    public void testParseComplexQuery() {
        String query = "p > .class1 > .class2[title*='test']:contains(Text)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);
        assertTrue(evaluators.size() == 1);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) evaluator;
        Evaluator tagEvaluator1 = andEvaluator.getLeftEvaluator();
        Evaluator classEvaluator1 = ((StructuralEvaluator.ImmediateParent) tagEvaluator1).getChildEvaluator();
        Evaluator classEvaluator2 = andEvaluator.getRightEvaluator();
        assertTrue(tagEvaluator1 instanceof Evaluator.Tag);
        assertEquals(tagEvaluator1.toString(), "p");
        assertTrue(classEvaluator1 instanceof Evaluator.Tag);
        assertEquals(classEvaluator1.toString(), ".");
        assertTrue(classEvaluator2 instanceof Evaluator.AttributeWithValueContaining);
        assertEquals(classEvaluator2.toString(), "[title*='test']");
        assertTrue(evaluator.toString().equals("p > .class1 > .class2[title*='test']:contains(Text)"));
    }

    @org.junit.Test(expected = org.jsoup.selector.SelectorParseException.class)
    public void testParseInvalidComplexQuery() {
        String query = "p > .class1 > .class2[title*='test']:containsText";
        QueryParser parser = new QueryParser(query);
        parser.parse();
        fail("Expected SelectorParseException");
    }
}