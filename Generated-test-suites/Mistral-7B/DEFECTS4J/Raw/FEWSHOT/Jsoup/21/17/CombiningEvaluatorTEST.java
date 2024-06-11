package org.jsoup.select;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombiningEvaluatorTest {
    @Test
    public void testAndEvaluator() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new ElementStartsEvaluator("div"));
        evaluators.add(new ElementHasClassEvaluator("class", "foo"));

        And andEvaluator = new And(evaluators);

        Element element = new Element("div");
        element.classes().add("foo");
        assertTrue(andEvaluator.matches(null, element));

        element.classes().clear();
        assertFalse(andEvaluator.matches(null, element));

        element.classes().add("bar");
        assertFalse(andEvaluator.matches(null, element));
    }

    @Test
    public void testOrEvaluator() {
        List<Evaluator> evaluators1 = new ArrayList<>();
        evaluators1.add(new ElementStartsWithTextEvaluator("h1", "Hello"));

        List<Evaluator> evaluators2 = new ArrayList<>();
        evaluators2.add(new ElementHasClassEvaluator("class", "greet"));

        Or orEvaluator1 = new Or(evaluators1);
        Or orEvaluator2 = new Or(evaluators2);

        CombiningEvaluator andOrEvaluator = new And(Arrays.asList(orEvaluator1, orEvaluator2));

        Element element = new Element("h1");
        element.text("Hello World");
        assertTrue(andOrEvaluator.matches(null, element));

        element.text("Hello");
        assertTrue(andOrEvaluator.matches(null, element));

        element.text("Hello There");
        assertFalse(andOrEvaluator.matches(null, element));

        element.classes().add("greet");
        assertTrue(andOrEvaluator.matches(null, element));

        element.classes().clear();
        assertFalse(andOrEvaluator.matches(null, element));
    }

    @Test
    public void testOrEvaluatorWithMultipleEvaluators() {
        List<Evaluator> evaluators1 = new ArrayList<>();
        evaluators1.add(new ElementStartsWithTextEvaluator("h1", "Hello"));
        evaluators1.add(new ElementHasClassEvaluator("class", "greet"));

        List<Evaluator> evaluators2 = new ArrayList<>();
        evaluators2.add(new ElementStartsWithTextEvaluator("h2", "World"));
        evaluators2.add(new ElementHasClassEvaluator("class", "greet"));

        Or orEvaluator1 = new Or(evaluators1);
        Or orEvaluator2 = new Or(evaluators2);

        CombiningEvaluator andOrEvaluator = new And(Arrays.asList(orEvaluator1, orEvaluator2));

        Element element = new Element("h1");
        element.text("Hello World");
        element.classes().add("greet");
        assertTrue(andOrEvaluator.matches(null, element));

        element.text("Hello");
        assertFalse(andOrEvaluator.matches(null, element));

        element.text("Hello There");
        assertFalse(andOrEvaluator.matches(null, element));

        element.classes().clear();
        assertFalse(andOrEvaluator.matches(null, element));

        element = new Element("h2");
        element.text("Hello World");
        element.classes().add("greet");
        assertFalse(andOrEvaluator.matches(null, element));
    }

    @Test
    public void testAddEvaluatorToOrEvaluator() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new ElementStartsWithTextEvaluator("h1", "Hello"));
        Or orEvaluator = new Or(evaluators);

        Element element = new Element("h1");
        element.text("Hello World");
        element.classes().add("greet");
        assertTrue(orEvaluator.matches(null, element));

        orEvaluator.add(new ElementHasClassEvaluator("class", "foo"));
        assertFalse(orEvaluator.matches(null, element));

        element.classes().remove("greet");
        assertFalse(orEvaluator.matches(null, element));

        element.classes().add("foo");
        assertTrue(orEvaluator.matches(null, element));
    }
}