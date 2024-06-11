package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        QueryParser parser = new QueryParser("");
        Evaluator evaluator = parser.parse();
        assertNull(evaluator);
    }

    @Test
    public void testParseTagSelector() {
        String query = "p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("p", ((Evaluator.Tag) evaluator).tagName());
    }

    @Test
    public void testParseTagWithNamespaceSelector() {
        String query = "html:head";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.TagEndsWith);
        assertEquals("head", ((Evaluator.TagEndsWith) evaluator).tagName());
        assertEquals("html", ((Evaluator.TagEndsWith) evaluator).namespace());
    }

    @Test
    public void testParseClassSelector() {
        String query = ".class1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals("class1", ((Evaluator.Class) evaluator).className());
    }

    @Test
    public void testParseIdSelector() {
        String query = "#id1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals("id1", ((Evaluator.Id) evaluator).id());
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "[title='Test']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Attribute);
        assertEquals("title", ((Evaluator.Attribute) evaluator).attributeName());
        assertEquals("Test", ((Evaluator.Attribute) evaluator).attributeValue());
    }

    @Test
    public void testParseAttributeWithValueStartingSelector() {
        String query = "[title^='Test']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueStarting);
        assertEquals("title", ((Evaluator.AttributeWithValueStarting) evaluator).attributeName());
        assertEquals("Test", ((Evaluator.AttributeWithValueStarting) evaluator).valuePrefix());
    }

    @Test
    public void testParseAttributeWithValueEndingSelector() {
        String query = "[title$='Test']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueEnding);
        assertEquals("title", ((Evaluator.AttributeWithValueEnding) evaluator).attributeName());
        assertEquals("Test", ((Evaluator.AttributeWithValueEnding) evaluator).valueSuffix());
    }

    @Test
    public void testParseAttributeWithValueContainingSelector() {
        String query = "[title*='Test']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueContaining);
        assertEquals("title", ((Evaluator.AttributeWithValueContaining) evaluator).attributeName());
        assertEquals("Test", ((Evaluator.AttributeWithValueContaining) evaluator).valueSearchString());
    }

    @Test
    public void testParseAttributeWithValueMatchingSelector() {
        String query = "[title~='Test']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueMatching);
        assertEquals("title", ((Evaluator.AttributeWithValueMatching) evaluator).attributeName());
        assertEquals("Test", ((Evaluator.AttributeWithValueMatching) evaluator).valueRegex().pattern().pattern());
    }

    @Test
    public void testParseAttributeWithValueNotSelector() {
        String query = "[title!='Test']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueNot);
        assertEquals("title", ((Evaluator.AttributeWithValueNot) evaluator).attributeName());
        assertEquals("Test", ((Evaluator.AttributeWithValueNot) evaluator).attributeValue());
    }

    @Test
    public void testParseCombinedSelectors() {
        String query = "div.class1 p#id1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new Evaluator.Class("class1"));
        expectedEvaluators.add(new Evaluator.Tag("p"));
        expectedEvaluators.add(new Evaluator.Id("id1"));
        assertEquals(expectedEvaluators, parser.evals());
    }

    @Test
    public void testParseCombinedSelectorsWithCombinatorSpace() {   
        String query = "div .class1 p #id1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new CombiningEvaluator.Child(new Evaluator.Class("class1")));
        expectedEvaluators.add(new Evaluator.Tag("p"));
        expectedEvaluators.add(new Evaluator.Id("id1"));
        assertEquals(expectedEvaluators, parser.evals());
    }

    @Test
    public void testParseCombinedSelectorsWithCombinatorGreaterThan() {
        String query = "div > p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("p")));
        assertEquals(expectedEvaluators, parser.evals());
    }

    @Test
    public void testParseCombinedSelectorsWithCombinatorPlus() {
        String query = "div + p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new StructuralEvaluator.PreviousSibling(new Evaluator.Tag("p")));
        assertEquals(expectedEvaluators, parser.evals());
    }

    @Test
    public void testParseCombinedSelectorsWithCombinatorTilde() {
        String query = "div ~ p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new StructuralEvaluator.PreviousSiblingOrSame(new Evaluator.Tag("p")));
        assertEquals(expectedEvaluators, parser.evals());
    }

    @Test
    public void testParseCombinedSelectorsWithCombinatorComma() {
        String query = "div, p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new Evaluator.Tag("p"));
        assertEquals(expectedEvaluators, parser.evals());
    }

    @Test
    public void testParseCombinedSelectorsWithCombiningOr() {
        String query = "div, p, ul li";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new Evaluator.Tag("p"));
        expectedEvaluators.add(new Evaluator.Tag("ul"));
        expectedEvaluators.add(new Evaluator.Tag("li"));
        assertEquals(expectedEvaluators, parser.evals());

        Evaluator orEvaluator = ((CombiningEvaluator.Or) evaluator).rightMostEvaluator();
        assertEquals(expectedEvaluators.size() - 1, orEvaluator.depth());
    }
}