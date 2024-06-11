package org.jsoup.select.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.And;
import org.jsoup.select.Or;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for CombiningEvaluator and its subclasses And, Or.
 */
public class CombiningEvaluatorTest {

    @Test
    public void testCombiningEvaluatorConstructor() {
        CombiningEvaluator ce = new CombiningEvaluator();
        Assert.assertNotNull(ce.evaluators);
        Assert.assertEquals(0, ce.evaluators.size());
    }

    @Test
    public void testCombiningEvaluatorAddEvaluators() {
        CombiningEvaluator ce = new CombiningEvaluator();
        Evaluator e1 = MockEvaluator.createMockEvaluator();
        Evaluator e2 = MockEvaluator.createMockEvaluator();

        ce.add(e1);
        ce.add(e2);

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(e1);
        expectedEvaluators.add(e2);

        Assert.assertEquals(expectedEvaluators, ce.evaluators);
    }

    @Test
    public void testAndEvaluatorConstructorWithCollection() {
        CombiningEvaluator.And and = new CombiningEvaluator.And(Arrays.asList(MockEvaluator.createMockEvaluator(), MockEvaluator.createMockEvaluator()));
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(MockEvaluator.createMockEvaluator());
        expectedEvaluators.add(MockEvaluator.createMockEvaluator());

        Assert.assertEquals(expectedEvaluators, and.evaluators);
    }

    @Test
    public void testAndEvaluatorConstructorWithEvaluators() {
        CombiningEvaluator.And and = new CombiningEvaluator.And(MockEvaluator.createMockEvaluator(), MockEvaluator.createMockEvaluator());
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(MockEvaluator.createMockEvaluator());
        expectedEvaluators.add(MockEvaluator.createMockEvaluator());

        Assert.assertEquals(expectedEvaluators, and.evaluators);
    }

    @Test
    public void testAndEvaluatorMatches() {
        CombiningEvaluator.And and = new CombiningEvaluator.And(MockEvaluator.createMockEvaluator(), MockEvaluator.createMockEvaluator());
        Element root = MockElement.createMockElement();
        Element node = MockElement.createMockElement();

        for (Evaluator evaluator : and.evaluators) {
            if (!evaluator.matches(root, node)) {
                Assert.assertFalse(and.matches(root, node));
                return;
            }
        }

        Assert.assertTrue(and.matches(root, node));
    }

    @Test
    public void testAndEvaluatorToString() {
        CombiningEvaluator.And and = new CombiningEvaluator.And(MockEvaluator.createMockEvaluator(), MockEvaluator.createMockEvaluator());
        String expectedToString = MockEvaluator.createMockEvaluator().toString() + " " + MockEvaluator.createMockEvaluator().toString();

        Assert.assertEquals(expectedToString, and.toString());
    }

    @Test
    public void testOrEvaluatorConstructorWithCollection() {
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(Arrays.asList(MockEvaluator.createMockEvaluator(), MockEvaluator.createMockEvaluator()));
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(MockEvaluator.createMockEvaluator());
        expectedEvaluators.add(MockEvaluator.createMockEvaluator());

        Assert.assertEquals(expectedEvaluators, or.evaluators);
    }

    @Test
    public void testOrEvaluatorAddEvaluator() {
        CombiningEvaluator.Or or = new CombiningEvaluator.Or();
        Evaluator e = MockEvaluator.createMockEvaluator();

        or.add(e);

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(MockEvaluator.createMockEvaluator());
        expectedEvaluators.add(e);

        Assert.assertEquals(expectedEvaluators, or.evaluators);
    }

    @Test
    public void testOrEvaluatorMatches() {
        CombiningEvaluator.Or or = new CombiningEvaluator.Or();
        Evaluator e1 = MockEvaluator.createMockEvaluator();
        Evaluator e2 = MockEvaluator.createMockEvaluator();

        or.add(e1);

        Element root = MockElement.createMockElement();
        Element node = MockElement.createMockElement();

        for (Evaluator evaluator : or.evaluators) {
            if (evaluator.matches(root, node)) {
                Assert.assertTrue(or.matches(root, node));
                return;
            }
        }

        Assert.assertFalse(or.matches(root, node));

        or.add(e2);

        for (Evaluator evaluator : or.evaluators) {
            if (evaluator.matches(root, node)) {
                Assert.assertTrue(or.matches(root, node));
                return;
            }
        }

        Assert.assertFalse(or.matches(root, node));

        or.evaluators.get(0).matches(root, node); // e1 matches

        Assert.assertTrue(or.matches(root, node));
    }

    @Test
    public void testOrEvaluatorToString() {
        CombiningEvaluator.Or or = new CombiningEvaluator.Or();
        Evaluator e1 = MockEvaluator.createMockEvaluator();
        Evaluator e2 = MockEvaluator.createMockEvaluator();

        or.add(e1);
        or.add(e2);

        String expectedToString = ":or[";
        expectedToString += e1.toString();
        expectedToString += ", ";
        expectedToString += e2.toString();
        expectedToString += "]";

        Assert.assertEquals(expectedToString, or.toString());
    }

    // Add test cases for edge cases and exception handling scenarios here

    private static Element MockElementCreateMockElement() {
        // Implement a method to create a mock Element
    }

    private static Evaluator MockEvaluatorCreateMockEvaluator() {
        // Implement a method to create a mock Evaluator
    }
}