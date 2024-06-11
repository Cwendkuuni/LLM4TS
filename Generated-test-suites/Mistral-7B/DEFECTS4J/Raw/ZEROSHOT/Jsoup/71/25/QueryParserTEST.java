package org.jsoup.select;

import static org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;

import java.util.List;

public class QueryParserTest {

    @org.junit.Test
    public void testParseSimpleSelector() {
        String query = "div.class1";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new Evaluator.Class("class1"));
        assertEquals(expectedEvaluators, evaluator.toList());
    }

    @org.junit.Test
    public void testParseCombinator() {
        String query = "div > p";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new StructuralEvaluator.ImmediateChild());
        expectedEvaluators.add(new Evaluator.Tag("p"));
        assertEquals(expectedEvaluators, evaluator.toList());
    }

    @org.junit.Test
    public void testParseAttributeSelector() {
        String query = "input[type='text']";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("input"));
        expectedEvaluators.add(new Evaluator.Attribute("type"));
        expectedEvaluators.add(new Evaluator.AttributeWithValue("text"));
        assertEquals(expectedEvaluators, evaluator.toList());
    }

    @org.junit.Test
    public void testParseClassSelectorWithSpaces() {
        String query = "div .class1";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new Evaluator.Class("class1"));
        assertEquals(expectedEvaluators, evaluator.toList());
    }

    @org.junit.Test
    public void testParseIDSelector() {
        String query = "#myId";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Id("myId"));
        assertEquals(expectedEvaluators, evaluator.toList());
    }

    @org.junit.Test
    public void testParseMultipleCombinator() {
        String query = "div > p > span";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new StructuralEvaluator.ImmediateChild());
        expectedEvaluators.add(new Evaluator.Tag("p"));
        expectedEvaluators.add(new StructuralEvaluator.ImmediateChild());
        expectedEvaluators.add(new Evaluator.Tag("span"));
        assertEquals(expectedEvaluators, evaluator.toList());
    }

    @org.junit.Test
    public void testParseAttributeSelectorWithOperator() {
        String query = "input[type!='button']";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("input"));
        expectedEvaluators.add(new Evaluator.Attribute("type"));
        expectedEvaluators.add(new Evaluator.AttributeWithValueNot("button"));
        assertEquals(expectedEvaluators, evaluator.toList());
    }

    @org.junit.Test
    public void testParseAttributeSelectorWithMultipleValues() {
        String query = "input[type='text' or type='password']";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("input"));
        expectedEvaluators.add(new CombiningEvaluator.Or(
            new Evaluator.Attribute("type"),
            new Evaluator.AttributeWithValue("text"),
            new Evaluator.AttributeWithValue("password")
        ));
        assertEquals(expectedEvaluators, evaluator.toList());
    }

    @org.junit.Test
    public void testParsePseudoClassSelector() {
        String query = ":emphasized";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new StructuralPseudoClass("emphasized"));
        assertEquals(expectedEvaluators, evaluator.toList());
    }

    @org.junit.Test
    public void testParsePseudoElementSelector() {
        String query = "input:checked:first-child::label";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("input"));
        expectedEvaluators.add(new StructuralPseudoClass("checked"));
        expectedEvaluators.add(new StructuralPseudoElement("label"));
        expectedEvaluators.add(new Evaluator.IsFirstChild());
        assertEquals(expectedEvaluators, evaluator.toList());
    }

    @org.junit.Test
    public void testParseComplexSelector() {
        String query = "div > ul > li > input[type='text']:not([name='password']):checked:first-child::label > span.icon";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new StructuralEvaluator.ImmediateChild());
        expectedEvaluators.add(new Evaluator.Tag("ul"));
        expectedEvaluators.add(new StructuralEvaluator.ImmediateChild());
        expectedEvaluators.add(new Evaluator.Tag("li"));
        expectedEvaluators.add(new Evaluator.Tag("input"));
        expectedEvaluators.add(new Evaluator.Attribute("type"));
        expectedEvaluators.add(new Evaluator.AttributeWithValue("text"));
        expectedEvaluators.add(new StructuralEvaluator.Not(
            new Evaluator.Attribute("name"),
            new Evaluator.AttributeWithValue("password")
        ));
        expectedEvaluators.add(new StructuralPseudoClass("checked"));
        expectedEvaluators.add(new StructuralPseudoElement("label"));
        expectedEvaluators.add(new Evaluator.IsFirstChild());
        expectedEvaluators.add(new Evaluator.Tag("span"));
        expectedEvaluators.add(new Evaluator.Class("icon"));
        assertEquals(expectedEvaluators, evaluator.toList());
    }
}

class StructuralPseudoClass {
    private final String name;

    StructuralPseudoClass(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "StructuralPseudoClass{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public List<Evaluator> toList() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new StructuralPseudoClass(name));
        return evaluators;
    }
}

class StructuralPseudoElement {
    private final String name;

    StructuralPseudoElement(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "StructuralPseudoElement{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public List<Evaluator> toList() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new StructuralPseudoElement(name));
        return evaluators;
    }
}