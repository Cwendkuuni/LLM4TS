package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;
import org.jsoup.helper.StringUtil;
import org.jsoup.parser.TokenQueue;

/**
 * Test cases for QueryParser class.
 */
public class QueryParserTest {

    @Test
    public void testParseSimpleSelectors() {
        String query = "#id1 .class1";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Id("id1"));
        evaluators.add(new Evaluator.Class("class1"));
        assertEquals(evaluators, evaluator.toList());
    }

    @Test
    public void testParseDescendantSelectors() {
        String query = "div p";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Tag("div"));
        evaluators.add(new StructuralEvaluator.Descendant());
        evaluators.add(new Evaluator.Tag("p"));
        assertEquals(evaluators, evaluator.toList());
    }

    @Test
    public void testParseAttributeSelectors() {
        String query = "[title='Test']";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Attribute("title").setValue("Test"));
        assertEquals(evaluators, evaluator.toList());
    }

    @Test
    public void testParsePseudoClassSelectors() {
        String query = ":hover";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(new Evaluator.PseudoClass("hover"), evaluator);
    }

    @Test
    public void testParseCombinatorSelectors() {
        String query = "div > p";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Tag("div"));
        evaluators.add(new StructuralEvaluator.Child());
        evaluators.add(new Evaluator.Tag("p"));
        assertEquals(evaluators, evaluator.toList());
    }

    @Test
    public void testParseComplexSelectors() {
        String query = "#id1 > .class1:hover, #id2 ~ .class2";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Id("id1"));
        evaluators.add(new StructuralEvaluator.ImmediateChild());
        evaluators.add(new Evaluator.Class("class1").withPseudoClass("hover"));
        evaluators.add(new Evaluator.Id("id2"));
        evaluators.add(new StructuralEvaluator.PreviousSibling());
        evaluators.add(new Evaluator.Class("class2"));
        assertEquals(evaluators, evaluator.toList());
    }

    @Test
    public void testParseAttributeWithValueSelectors() {
        String query = "[title='Test']";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Attribute("title").setValue("Test"));
        assertEquals(evaluators, evaluator.toList());
    }

    @Test
    public void testParseAttributeWithValueStartingSelectors() {
        String query = "[title^='Test']";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Attribute("title").withPrefix("Test"));
        assertEquals(evaluators, evaluator.toList());
    }

    @Test
    public void testParseAttributeWithValueEndingSelectors() {
        String query = "[title$='Test']";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Attribute("title").withSuffix("Test"));
        assertEquals(evaluators, evaluator.toList());
    }

    @Test
    public void testParseAttributeWithValueContainingSelectors() {
        String query = "[title*='Test']";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Attribute("title").contains("Test"));
        assertEquals(evaluators, evaluator.toList());
    }

    @Test
    public void testParseAttributeWithValueMatchingSelectors() {
        String query = "[title~='Test']";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Attribute("title").matchesRegExp("Test"));
        assertEquals(evaluators, evaluator.toList());
    }

    @Test
    public void testParseAttributeWithValueMatchingSelectorsUsingRegex() {
        String query = "[title~='Test.*']";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Attribute("title").matchesRegExp("Test.*"));
        assertEquals(evaluators, evaluator.toList());
    }

    @Test
    public void testParsePseudoElementSelectors() {
        String query = "p::first-letter";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(new Evaluator.PseudoElement("first-letter").withTag("p"), evaluator);
    }

    @Test
    public void testParsePseudoElementSelectorsWithAttribute() {
        String query = "input:-checked + label";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.PseudoElement("checked").withTag("input"));
        evaluators.add(new StructuralEvaluator.PreviousSibling());
        evaluators.add(new Evaluator.Tag("label"));
        assertEquals(evaluators, evaluator.toList());
    }

    // TODO: Add more test cases for other selectors like :lt, :gt, :eq, :has, :contains, :not
}