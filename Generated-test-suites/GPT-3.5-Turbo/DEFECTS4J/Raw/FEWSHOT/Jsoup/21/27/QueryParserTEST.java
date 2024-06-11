package org.jsoup.select;

import org.jsoup.helper.Validate;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "a.b > c ~ d";
        Evaluator evaluator = QueryParser.parse(query);

        assertTrue(evaluator instanceof CombiningEvaluator.And);
    }

    @Test
    public void testCombiningEvaluator() {
        CombiningEvaluator.And and = new CombiningEvaluator.And();

        Evaluator evaluator1 = new Evaluator.Tag("a");
        Evaluator evaluator2 = new Evaluator.Class("b");

        and.add(evaluator1);
        and.add(evaluator2);

        assertEquals(2, and.numEvalutors());

        List<Evaluator> evaluators = and.getEvaluators();

        assertEquals(evaluator1, evaluators.get(0));
        assertEquals(evaluator2, evaluators.get(1));
    }

    @Test
    public void testTagEvaluator() {
        Evaluator.Tag tag = new Evaluator.Tag("a");

        assertEquals("a", tag.getTag());
    }

    @Test
    public void testClassEvaluator() {
        Evaluator.Class cls = new Evaluator.Class("b");

        assertEquals("b", cls.getClassName());
    }

    @Test
    public void testIdEvaluator() {
        Evaluator.Id id = new Evaluator.Id("c");

        assertEquals("c", id.getId());
    }

    @Test
    public void testAttributeEvaluator() {
        Evaluator.Attribute attribute = new Evaluator.Attribute("d");

        assertEquals("d", attribute.getKey());
    }

    @Test
    public void testAttributeWithValue() {
        Evaluator.AttributeWithValue attributeWithValue = new Evaluator.AttributeWithValue("e", "f");

        assertEquals("e", attributeWithValue.getKey());
        assertEquals("f", attributeWithValue.getValue());
    }

    @Test
    public void testAttributeWithValueNot() {
        Evaluator.AttributeWithValueNot attributeWithValueNot = new Evaluator.AttributeWithValueNot("g", "h");

        assertEquals("g", attributeWithValueNot.getKey());
        assertEquals("h", attributeWithValueNot.getValue());
    }

    @Test
    public void testAttributeWithValueStarting() {
        Evaluator.AttributeWithValueStarting attributeWithValueStarting = new Evaluator.AttributeWithValueStarting("i", "j");

        assertEquals("i", attributeWithValueStarting.getKey());
        assertEquals("j", attributeWithValueStarting.getValue());
    }

    @Test
    public void testAttributeWithValueEnding() {
        Evaluator.AttributeWithValueEnding attributeWithValueEnding = new Evaluator.AttributeWithValueEnding("k", "l");

        assertEquals("k", attributeWithValueEnding.getKey());
        assertEquals("l", attributeWithValueEnding.getValue());
    }

    @Test
    public void testAttributeWithValueContaining() {
        Evaluator.AttributeWithValueContaining attributeWithValueContaining = new Evaluator.AttributeWithValueContaining("m", "n");

        assertEquals("m", attributeWithValueContaining.getKey());
        assertEquals("n", attributeWithValueContaining.getValue());
    }

    @Test
    public void testAttributeWithValueMatching() {
        Pattern pattern = Pattern.compile("o");
        Evaluator.AttributeWithValueMatching attributeWithValueMatching = new Evaluator.AttributeWithValueMatching("o", pattern);

        assertEquals("o", attributeWithValueMatching.getKey());
        assertEquals(pattern, attributeWithValueMatching.getPattern());
    }

    @Test
    public void testContainsText() {
        Evaluator.ContainsText containsText = new Evaluator.ContainsText("p");

        assertEquals("p", containsText.getText());
    }

    @Test
    public void testContainsOwnText() {
        Evaluator.ContainsOwnText containsOwnText = new Evaluator.ContainsOwnText("q");

        assertEquals("q", containsOwnText.getText());
    }

    @Test
    public void testMatches() {
        Pattern pattern = Pattern.compile("r");
        Evaluator.Matches matches = new Evaluator.Matches(pattern);

        assertEquals(pattern, matches.getPattern());
    }

    @Test
    public void testMatchesOwn() {
        Pattern pattern = Pattern.compile("s");
        Evaluator.MatchesOwn matchesOwn = new Evaluator.MatchesOwn(pattern);

        assertEquals(pattern, matchesOwn.getPattern());
    }

    @Test
    public void testIndexLessThan() {
        Evaluator.IndexLessThan indexLessThan = new Evaluator.IndexLessThan(1);

        assertEquals(1, indexLessThan.getIndex());
    }

    @Test
    public void testIndexGreaterThan() {
        Evaluator.IndexGreaterThan indexGreaterThan = new Evaluator.IndexGreaterThan(2);

        assertEquals(2, indexGreaterThan.getIndex());
    }

    @Test
    public void testIndexEquals() {
        Evaluator.IndexEquals indexEquals = new Evaluator.IndexEquals(3);

        assertEquals(3, indexEquals.getIndex());
    }

    @Test
    public void testNot() {
        Evaluator evaluator = new Evaluator.Tag("a");
        Evaluator.Not not = new Evaluator.Not(evaluator);

        assertEquals(evaluator, not.getEvalutor());
    }

    @Test
    public void testHas() {
        Evaluator evaluator = new Evaluator.Tag("b");
        Evaluator.Has has = new Evaluator.Has(evaluator);

        assertEquals(evaluator, has.getEvalutor());
    }

    @Test
    public void testStructuralEvaluator() {
        StructuralEvaluator.Parent parent = new StructuralEvaluator.Parent(new Evaluator.Tag("a"));

        assertEquals(parent.getEvalutor(), new Evaluator.Tag("a"));
    }

    @Test
    public void testStructuralEvaluatorRoot() {
        StructuralEvaluator.Root root = new StructuralEvaluator.Root();

        assertEquals(root.getEvalutor(), new Evaluator.AllElements());
    }

}