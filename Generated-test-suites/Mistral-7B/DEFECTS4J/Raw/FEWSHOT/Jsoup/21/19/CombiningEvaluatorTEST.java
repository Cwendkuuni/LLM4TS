package org.jsoup.select;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testAndEvaluator() {
        Evaluator evaluator1 = new ElementStartsWithTagNameEvaluator("a");
        Evaluator evaluator2 = new ElementHasClassEvaluator("active");

        CombiningEvaluator and = new CombiningEvaluator.And(evaluator1, evaluator2);

        Element element = new Element("a").addClass("active");
        assertTrue(and.matches(null, element));

        Element elementWithoutClass = new Element("a");
        assertFalse(and.matches(null, elementWithoutClass));

        Element elementWithoutTag = new Element("div");
        assertFalse(and.matches(null, elementWithoutTag));
    }

    @Test
    public void testOrEvaluator() {
        Evaluator evaluator1 = new ElementStartsWithTagNameEvaluator("a");
        Evaluator evaluator2 = new ElementHasClassEvaluator("active");
        Evaluator evaluator3 = new ElementHasClassEvaluator("disabled");

        CombiningEvaluator or = new CombiningEvaluator.Or(evaluator1, evaluator2, evaluator3);

        Element element1 = new Element("a").addClass("active");
        assertTrue(or.matches(null, element1));

        Element element2 = new Element("a").addClass("disabled");
        assertTrue(or.matches(null, element2));

        Element element3 = new Element("a").addClass("active").addClass("disabled");
        assertTrue(or.matches(null, element3));

        Element element4 = new Element("a");
        assertFalse(or.matches(null, element4));

        Element element5 = new Element("div");
        assertFalse(or.matches(null, element5));
    }

    @Test
    public void testAndWithMultipleEvaluators() {
        Evaluator evaluator1 = new ElementStartsWithTagNameEvaluator("a");
        Evaluator evaluator2 = new ElementHasClassEvaluator("active");
        Evaluator evaluator3 = new ElementHasClassEvaluator("disabled");

        CombiningEvaluator and = new CombiningEvaluator.And(arr -> {
            List<Evaluator> evaluators = new ArrayList<>();
            evaluators.add(evaluator1);
            evaluators.add(evaluator2);
            evaluators.add(evaluator3);
            return new CombiningEvaluator.And(evaluators);
        });

        Element element1 = new Element("a").addClass("active");
        assertFalse(and.matches(null, element1));

        Element element2 = new Element("a").addClass("active").addClass("disabled");
        assertFalse(and.matches(null, element2));

        Element element3 = new Element("a").addClass("active").removeClass("disabled");
        assertTrue(and.matches(null, element3));

        Element element4 = new Element("a").removeClass("disabled");
        assertTrue(and.matches(null, element4));

        Element element5 = new Element("a").addClass("active");
        assertTrue(and.matches(null, element5));

        Element element6 = new Element("a");
        assertFalse(and.matches(null, element6));

        Element element7 = new Element("div");
        assertFalse(and.matches(null, element7));
    }

    @Test
    public void testOrWithMultipleEvaluators() {
        Evaluator evaluator1 = new ElementStartsWithTagNameEvaluator("a");
        Evaluator evaluator2 = new ElementHasClassEvaluator("active");
        Evaluator evaluator3 = new ElementHasClassEvaluator("disabled");

        CombiningEvaluator or = new CombiningEvaluator.Or();
        or.add(evaluator1);
        or.add(evaluator2);
        or.add(evaluator3);

        Element element1 = new Element("a").addClass("active");
        assertTrue(or.matches(null, element1));

        Element element2 = new Element("a").addClass("disabled");
        assertTrue(or.matches(null, element2));

        Element element3 = new Element("a").addClass("active").addClass("disabled");
        assertTrue(or.matches(null, element3));

        Element element4 = new Element("a");
        assertFalse(or.matches(null, element4));

        Element element5 = new Element("div");
        assertFalse(or.matches(null, element5));

        or.remove(evaluator1);

        assertFalse(or.matches(null, element1));
    }
}