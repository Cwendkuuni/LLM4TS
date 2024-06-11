package org.jsoup.select;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testAndEvaluator() {
        Evaluator evaluator1 = new ElementStartsWithTagNameEvaluator("p");
        Evaluator evaluator2 = new ElementHasClassEvaluator("class1");
        And andEvaluator = new And(Arrays.asList(evaluator1, evaluator2));

        Element element = new Element("p");
        element.classes().add("class1");

        assertTrue(andEvaluator.matches(null, element));

        Element otherElement = new Element("div");
        assertFalse(andEvaluator.matches(null, otherElement));

        Element emptyElement = null;
        assertFalse(andEvaluator.matches(null, emptyElement));
    }

    @Test
    public void testOrEvaluator() {
        Evaluator evaluator1 = new ElementStartsWithTagNameEvaluator("p");
        Evaluator evaluator2 = new ElementHasClassEvaluator("class1");
        Or orEvaluator = new Or(Arrays.asList(evaluator1, evaluator2));

        Element element1 = new Element("p");
        element1.classes().add("class1");

        Element element2 = new Element("p");

        assertTrue(orEvaluator.matches(null, element1));
        assertTrue(orEvaluator.matches(null, element2));

        Element otherElement = new Element("div");
        assertFalse(orEvaluator.matches(null, otherElement));

        Element emptyElement = null;
        assertFalse(orEvaluator.matches(null, emptyElement));

        // Test adding another evaluator
        Evaluator evaluator3 = new ElementHasAttrEvaluator("data-test", "test");
        orEvaluator.add(evaluator3);
        Element elementWithDataTest = new Element("p");
        elementWithDataTest.attr("data-test", "test");
        assertTrue(orEvaluator.matches(null, elementWithDataTest));
    }

    @Test
    public void testAndWithEmptyList() {
        And andEvaluator = new And(Collections.emptyList());
        Element emptyElement = null;
        assertTrue(andEvaluator.matches(null, emptyElement));
    }

    @Test
    public void testOrWithEmptyList() {
        Or orEvaluator = new Or(Collections.emptyList());
        Element emptyElement = null;
        assertTrue(orEvaluator.matches(null, emptyElement));
    }
}