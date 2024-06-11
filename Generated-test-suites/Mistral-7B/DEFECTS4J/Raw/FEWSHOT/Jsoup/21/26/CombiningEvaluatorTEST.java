package org.jsoup.select;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class CombiningEvaluatorTest {

    @Test
    public void testAndEvaluator() {
        Evaluator ev1 = new ElementStartsWith("a");
        Evaluator ev2 = new ElementHasClass("class1");
        And andEvaluator = new And(Arrays.asList(ev1, ev2));

        Element element = new Element("a");
        element.classes().add("class1");

        assertTrue(andEvaluator.matches(null, element));
    }

    @Test
    public void testAndEvaluator_false() {
        Evaluator ev1 = new ElementStartsWith("a");
        Evaluator ev2 = new ElementHasClass("class2");
        And andEvaluator = new And(Arrays.asList(ev1, ev2));

        Element element = new Element("b");
        element.classes().add("class1");

        assertFalse(andEvaluator.matches(null, element));
    }

    @Test
    public void testAndEvaluator_null() {
        Evaluator ev1 = new ElementStartsWith("a");
        Evaluator ev2 = new ElementHasClass("class1");
        And andEvaluator = new And(Arrays.asList(ev1, ev2));

        assertFalse(andEvaluator.matches(null, null));
    }

    @Test
    public void testOrEvaluator() {
        Evaluator ev1 = new ElementStartsWith("a");
        Evaluator ev2 = new ElementHasClass("class1");
        Or orEvaluator = new Or(Arrays.asList(ev1, ev2));

        Element element1 = new Element("a");
        element1.classes().add("class1");

        assertTrue(orEvaluator.matches(null, element1));

        Element element2 = new Element("b");
        element2.classes().add("class2");

        assertFalse(orEvaluator.matches(null, element2));

        orEvaluator.add(new ElementHasText("text"));

        Element element3 = new Element("a");
        element3.classes().add("class1");
        element3.text("text");

        assertTrue(orEvaluator.matches(null, element3));
    }

    @Test
    public void testOrEvaluator_null() {
        Evaluator ev1 = new ElementStartsWith("a");
        Or orEvaluator = new Or(Arrays.asList(ev1));

        assertFalse(orEvaluator.matches(null, null));
    }
}