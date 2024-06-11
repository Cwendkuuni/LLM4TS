package org.jsoup.select;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {

    @Test
    public void testCombiningEvaluatorAnd() {
        List<Evaluator> evaluators = Arrays.asList(
                new ElementNameEvaluator("p"),
                new ElementClassEvaluator("selected")
        );

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        Element element = new Element("p");
        element.classes().add("selected");

        assertTrue(andEvaluator.matches(null, element));
    }

    @Test
    public void testCombiningEvaluatorAndMultiple() {
        List<Evaluator> evaluators = Arrays.asList(
                new ElementNameEvaluator("p"),
                new ElementClassEvaluator("selected"),
                new ElementClassEvaluator("error")
        );

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        Element element = new Element("p");
        element.classes().add("selected");
        element.classes().add("error");

        assertTrue(andEvaluator.matches(null, element));
    }

    @Test
    public void testCombiningEvaluatorAndEmpty() {
        List<Evaluator> evaluators = new ArrayList<>();

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        Element element = new Element("p");

        assertFalse(andEvaluator.matches(null, element));
    }

    @Test
    public void testCombiningEvaluatorAndNullElement() {
        List<Evaluator> evaluators = Arrays.asList(
                new ElementNameEvaluator("p"),
                new ElementClassEvaluator("selected")
        );

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        assertFalse(andEvaluator.matches(null, null));
    }

    @Test
    public void testCombiningEvaluatorOr() {
        List<Evaluator> evaluators1 = Arrays.asList(
                new ElementNameEvaluator("p"),
                new ElementClassEvaluator("selected")
        );

        List<Evaluator> evaluators2 = Arrays.asList(
                new ElementNameEvaluator("div"),
                new ElementClassEvaluator("error")
        );

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators1);
        orEvaluator.add(new CombiningEvaluator.And(evaluators2));

        Element element = new Element("p");
        element.classes().add("selected");

        assertFalse(orEvaluator.matches(null, element));

        Element element2 = new Element("div");
        element2.classes().add("error");

        assertTrue(orEvaluator.matches(null, element2));
    }

    @Test
    public void testCombiningEvaluatorOrMultiple() {
        List<Evaluator> evaluators1 = Arrays.asList(
                new ElementNameEvaluator("p"),
                new ElementClassEvaluator("selected")
        );

        List<Evaluator> evaluators2 = Arrays.asList(
                new ElementNameEvaluator("div"),
                new ElementClassEvaluator("error")
        );

        List<Evaluator> evaluators3 = Arrays.asList(
                new ElementNameEvaluator("span"),
                new ElementClassEvaluator("highlighted")
        );

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators1);
        orEvaluator.add(new CombiningEvaluator.And(evaluators2));
        orEvaluator.add(new CombiningEvaluator.And(evaluators3));

        Element element = new Element("p");
        element.classes().add("selected");

        assertFalse(orEvaluator.matches(null, element));

        Element element2 = new Element("div");
        element2.classes().add("error");

        assertTrue(orEvaluator.matches(null, element2));

        Element element3 = new Element("span");
        element3.classes().add("highlighted");

        assertTrue(orEvaluator.matches(null, element3));
    }
}