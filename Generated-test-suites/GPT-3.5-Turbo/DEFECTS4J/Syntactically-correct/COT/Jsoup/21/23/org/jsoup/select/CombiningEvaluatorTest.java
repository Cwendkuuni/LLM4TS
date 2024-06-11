package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element root = null;
        Element node = null;
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Tag("div"));
        evaluators.add(new Evaluator.Attribute("class", "example"));
        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);

        assertTrue(and.matches(root, node));
    }

    @Test
    public void testAndDoesNotMatch() {
        Element root = null;
        Element node = null;
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Tag("div"));
        evaluators.add(new Evaluator.Attribute("class", "example"));
        evaluators.add(new Evaluator.Attribute("id", "test"));
        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);

        assertFalse(and.matches(root, node));
    }

    @Test
    public void testAndToString() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Tag("div"));
        evaluators.add(new Evaluator.Attribute("class", "example"));
        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);

        assertEquals("div .example", and.toString());
    }

    @Test
    public void testOrMatches() {
        Element root = null;
        Element node = null;
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Tag("div"));
        evaluators.add(new Evaluator.Attribute("class", "example"));
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);

        assertTrue(or.matches(root, node));
    }

    @Test
    public void testOrDoesNotMatch() {
        Element root = null;
        Element node = null;
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Tag("span"));
        evaluators.add(new Evaluator.Attribute("class", "example"));
        evaluators.add(new Evaluator.Attribute("id", "test"));
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);

        assertFalse(or.matches(root, node));
    }

    @Test
    public void testOrToString() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Tag("div"));
        evaluators.add(new Evaluator.Attribute("class", "example"));
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);

        assertEquals(":or[div, .example]", or.toString());
    }

}