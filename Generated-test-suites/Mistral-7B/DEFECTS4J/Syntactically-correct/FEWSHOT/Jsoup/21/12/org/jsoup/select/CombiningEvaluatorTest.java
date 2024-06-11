package org.jsoup.select;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class CombiningEvaluatorTest {

    @Test
    public void testAndEvaluator() {
        List<Evaluator> evaluators = Arrays.asList(
                new TagNameEvaluator("p"),
                new ClassNameEvaluator("red")
        );

        And andEvaluator = new And(evaluators);

        Element element = new Element("p");
        element.classes().add("red");

        assertTrue(andEvaluator.matches(null, element));
    }

    @Test
    public void testAndEvaluator_false() {
        List<Evaluator> evaluators = Arrays.asList(
                new TagNameEvaluator("p"),
                new ClassNameEvaluator("blue")
        );

        And andEvaluator = new And(evaluators);

        Element element = new Element("p");
        element.classes().add("blue");

        assertFalse(andEvaluator.matches(null, element));
    }

    @Test
    public void testOrEvaluator() {
        List<Evaluator> evaluators1 = Arrays.asList(
                new TagNameEvaluator("p"),
                new ClassNameEvaluator("red")
        );

        List<Evaluator> evaluators2 = Arrays.asList(
                new TagNameEvaluator("li"),
                new ClassNameEvaluator("blue")
        );

        Or orEvaluator = new Or(evaluators1);
        orEvaluator.add(new And(evaluators2));

        Element element1 = new Element("p");
        element1.classes().add("red");

        Element element2 = new Element("li");
        element2.classes().add("blue");

        assertTrue(orEvaluator.matches(null, element1));
        assertTrue(orEvaluator.matches(null, element2));
    }

    @Test
    public void testOrEvaluator_false() {
        List<Evaluator> evaluators1 = Arrays.asList(
                new TagNameEvaluator("p"),
                new ClassNameEvaluator("red")
        );

        List<Evaluator> evaluators2 = Arrays.asList(
                new TagNameEvaluator("li"),
                new ClassNameEvaluator("red")
        );

        Or orEvaluator = new Or(evaluators1);
        orEvaluator.add(new And(evaluators2));

        Element element1 = new Element("p");
        element1.classes().add("blue");

        Element element2 = new Element("li");
        element2.classes().add("green");

        assertFalse(orEvaluator.matches(null, element1));
        assertFalse(orEvaluator.matches(null, element2));
    }
}