package org.jsoup.select;

import static org.junit.Assert.*;

import org.junit.Test;

import org.jsoup.parser.TokenQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryParserTest {

    @Test
    public void testParseSimpleTagSelector() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseIdSelector() {
        String query = "#id1";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Id("id1"));
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseClassSelector() {
        String query = ".class1";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Class("class1"));
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseTagWithNamespaceSelector() {
        String query = "ns:tag";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("tag", "ns"));
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseAttributeSelectorWithValue() {
        String query = "[attr='value']";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Attribute("attr", "value"));
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseAttributeSelectorWithStartValue() {
        String query = "[attr^='value']";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.AttributeWithValueStarting("attr", "value"));
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseAttributeSelectorWithEndValue() {
        String query = "[attr$='value']";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.AttributeWithValueEnding("attr", "value"));
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseAttributeSelectorWithContainsValue() {
        String query = "[attr*='value']";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.AttributeWithValueContaining("attr", "value"));
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseAttributeSelectorWithMatchValue() {
        String query = "[attr~='value']";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.AttributeWithValueMatching("attr", Pattern.compile("value")));
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseAttributeSelectorWithNotValue() {
        String query = "[attr!='value']";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.AttributeWithValueNot("attr", "value"));
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseCombinedSelectors() {
        String query = "div.class1 > p:first-child";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        StructuralEvaluator.Parent parent = new StructuralEvaluator.Parent(new Evaluator.Tag("div"));
        StructuralEvaluator.ImmediateChild immediateChild = new StructuralEvaluator.ImmediateChild(parent, new Evaluator.Tag("p"));
        Evaluator.IsFirstChild firstChild = new Evaluator.IsFirstChild();
        expectedEvaluators.add(immediateChild);
        expectedEvaluators.add(firstChild);
        StructuralEvaluator and = new StructuralEvaluator.And(parent, immediateChild, firstChild);
        expectedEvaluators.add(and);
        StructuralEvaluator.Class classEvaluator = new StructuralEvaluator.Class("class1");
        expectedEvaluators.add(classEvaluator);
        StructuralEvaluator.And and1 = new StructuralEvaluator.And(parent, classEvaluator, immediateChild, firstChild);
        expectedEvaluators.add(and1);
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseComplexSelector() {
        String query = "div > .class1.active:not([title]) > ul > li:nth-child(2) > input:contains('Search')";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        StructuralEvaluator.Parent divParent = new StructuralEvaluator.Parent(new Evaluator.Tag("div"));
        StructuralEvaluator.ImmediateChild divChild = new StructuralEvaluator.ImmediateChild(divParent, new Evaluator.Class("class1"));
        StructuralEvaluator.Attribute attribute1 = new StructuralEvaluator.AttributeWithValueNot("title", "");
        StructuralEvaluator.And and1 = new StructuralEvaluator.And(divChild, attribute1);
        StructuralEvaluator.ImmediateChild ulChild = new StructuralEvaluator.ImmediateChild(and1, new Evaluator.Tag("ul"));
        StructuralEvaluator.NthChild nthChild1 = new StructuralEvaluator.IsNthChild(1, 2);
        StructuralEvaluator.ImmediateChild liChild = new StructuralEvaluator.ImmediateChild(ulChild, nthChild1);
        StructuralEvaluator.Tag tag = new StructuralEvaluator.Tag("input");
        StructuralEvaluator.ContainsText containsText = new StructuralEvaluator.ContainsText("Search");
        StructuralEvaluator.And and2 = new StructuralEvaluator.And(liChild, tag, containsText);
        expectedEvaluators.add(and2);
        StructuralEvaluator and3 = new StructuralEvaluator.And(divParent, divChild, and1, ulChild, liChild, tag, containsText);
        expectedEvaluators.add(and3);
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }
}