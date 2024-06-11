package org.jsoup.select.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;
import org.jsoup.select.And;
import org.jsoup.select.Or;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for CombiningEvaluator and its subclasses And, Or.
 */
public class CombiningEvaluatorTest {

    @Test
    public void testAnd() {
        Evaluator evaluator1 = new ElementStartsWithEvaluator("div");
        Evaluator evaluator2 = new ElementHasClassEvaluator("class1");
        And and = new And(Arrays.asList(evaluator1, evaluator2));

        Element element = new Element("div");
        element.addClass("class1");

        assertTrue(and.matches(element, element));
    }

    @Test
    public void testAnd_false() {
        Evaluator evaluator1 = new ElementStartsWithEvaluator("div");
        Evaluator evaluator2 = new ElementHasClassEvaluator("class2");
        And and = new And(Arrays.asList(evaluator1, evaluator2));

        Element element = new Element("div");
        element.addClass("class1");

        assertFalse(and.matches(element, element));
    }

    @Test
    public void testOr() {
        Evaluator evaluator1 = new ElementStartsWithEvaluator("div");
        Evaluator evaluator2 = new ElementHasClassEvaluator("class1");
        Or or = new Or(Arrays.asList(evaluator1, evaluator2));

        Element element1 = new Element("div");
        element1.addClass("class1");

        Element element2 = new Element("span");
        element2.addClass("class2");

        assertTrue(or.matches(element1, element1));
        assertTrue(or.matches(element2, element2));
    }

    @Test
    public void testOr_false() {
        Evaluator evaluator1 = new ElementStartsWithEvaluator("div");
        Evaluator evaluator2 = new ElementHasClassEvaluator("class1");
        Or or = new Or(Arrays.asList(evaluator1, evaluator2));

        Element element = new Element("span");
        element.addClass("class2");

        assertFalse(or.matches(element, element));
    }

    @Test(expected = IllegalStateException.class)
    public void testCombiningEvaluator_nullEvaluators() {
        new CombiningEvaluator(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAnd_emptyEvaluators() {
        new And(new ArrayList<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOr_emptyEvaluators() {
        new Or(new ArrayList<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOr_oneEvaluator() {
        Or or = new Or(Arrays.asList(new ElementStartsWithEvaluator("div")));
        or.add(new ElementStartsWithEvaluator("span")); // should throw IAE
    }
}