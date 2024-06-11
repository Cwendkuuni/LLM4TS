package org.jsoup.select;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testAndEvaluator() {
        Evaluator evaluator1 = new ElementStartsWithEvaluator("div");
        Evaluator evaluator2 = new ElementHasClassEvaluator("class", "active");
        And andEvaluator = new And(Arrays.asList(evaluator1, evaluator2));

        Element element = new Element("div");
        element.classes().add("active");

        Assert.assertTrue(andEvaluator.matches(null, element));
    }

    @Test
    public void testAndEvaluator_false() {
        Evaluator evaluator1 = new ElementStartsWithEvaluator("div");
        Evaluator evaluator2 = new ElementHasClassEvaluator("class", "error");
        And andEvaluator = new And(Arrays.asList(evaluator1, evaluator2));

        Element element = new Element("div");
        element.classes().add("error");

        Assert.assertFalse(andEvaluator.matches(null, element));
    }

    @Test
    public void testAndEvaluator_empty() {
        And andEvaluator = new And(new ArrayList<>());

        Assert.assertFalse(andEvaluator.matches(null, null));
    }

    @Test
    public void testOrEvaluator() {
        Evaluator evaluator1 = new ElementStartsWithEvaluator("div");
        Evaluator evaluator2 = new ElementHasClassEvaluator("class", "active");
        Or orEvaluator = new Or(Arrays.asList(evaluator1, evaluator2));

        Element element1 = new Element("div");
        element1.classes().add("active");

        Element element2 = new Element("p");
        element2.classes().add("active");

        Assert.assertTrue(orEvaluator.matches(null, element1));
        Assert.assertTrue(orEvaluator.matches(null, element2));
    }

    @Test
    public void testOrEvaluator_false() {
        Evaluator evaluator1 = new ElementStartsWithEvaluator("div");
        Evaluator evaluator2 = new ElementHasClassEvaluator("class", "active");
        Or orEvaluator = new Or(Arrays.asList(evaluator1, new ElementHasTextEvaluator("p", "error")));

        Element element = new Element("div");
        element.classes().add("active");

        Assert.assertFalse(orEvaluator.matches(null, element));
    }

    @Test
    public void testOrEvaluator_addEvaluator() {
        Or orEvaluator = new Or();
        orEvaluator.add(new ElementStartsWithEvaluator("div"));
        orEvaluator.add(new ElementHasClassEvaluator("class", "active"));

        Element element = new Element("div");
        element.classes().add("active");

        Assert.assertTrue(orEvaluator.matches(null, element));
    }
}