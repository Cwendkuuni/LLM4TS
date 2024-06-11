package org.jsoup.select.test;

import org.jsoup.helper.ElementFinder;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class CombiningEvaluatorTest {

    private ElementFinder finder;

    @Before
    public void setUp() {
        finder = new ElementFinder();
    }

    @After
    public void tearDown() {
        finder = null;
    }

    // Testing the constructor and getter of CombiningEvaluator
    @Test
    public void testCombiningEvaluatorConstruction() {
        CombiningEvaluator ce = new CombiningEvaluator();
        assertNotNull(ce);
        assertNotNull(ce.evaluators);
        assertEquals(0, ce.evaluators.size());

        CombiningEvaluator ce2 = new CombiningEvaluator(Arrays.asList(new ElementMatcher(), new ElementMatcher()));
        assertNotNull(ce2);
        assertNotNull(ce2.evaluators);
        assertEquals(2, ce2.evaluators.size());
    }

    // Testing the And class

    // Testing the constructor of And class
    @Test
    public void testAndConstructor() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new ElementMatcher());
        evaluators.add(new ElementMatcher());
        And and = new And(evaluators);
        assertNotNull(and);
        assertEquals(2, and.evaluators.size());
    }

    // Testing the matches method of And class with both evaluators returning true
    @Test
    public void testAndMatchesBothTrue() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new ElementMatcher());
        evaluators.add(new ElementMatcher());
        And and = new And(evaluators);
        Element element = new Element("div");
        assertTrue(and.matches(finder.findRootElement(), element));
    }

    // Testing the matches method of And class with one evaluator returning false
    @Test
    public void testAndMatchesOneFalse() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new ElementMatcher());
        evaluators.add(new Not(new ElementMatcher()));
        And and = new And(evaluators);
        Element element = new Element("div");
        assertFalse(and.matches(finder.rootElement(), element));
    }

    // Testing the toString method of And class
    @Test
    public void testAndToString() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new ElementMatcher());
        evaluators.add(new ElementMatcher());
        And and = new And(evaluators);
        String expected = "ElementMatcher ElementMatcher";
        String actual = and.toString();
        assertEquals(expected, actual);
    }

    // Testing edge cases and exception handling for And class

    // Testing the matches method of And class with empty evaluators list
    @Test(expected = IllegalArgumentException.class)
    public void testAndEmptyList() {
        And and = new And(new ArrayList<>());
    }

    // Testing the matches method of And class with null evaluator
    @Test(expected = NullPointerException.class)
    public void testAndNullEvaluator() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(null);
        And and = new And(evaluators);
    }

    // Testing the Or class

    // Testing the constructor of Or class
    @Test
    public void testOrConstructor() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new ElementMatcher());
        evaluators.add(new ElementMatcher());
        Or or = new Or(evaluators);
        assertNotNull(or);
        assertEquals(2, or.evaluators.size());
    }

    // Testing the add method of Or class
    @Test
    public void testOrAddEvaluator() {
        Or or = new Or();
        Evaluator evaluator = new ElementMatcher();
        or.add(evaluator);
        assertTrue(or.evaluators.contains(evaluator));
    }

    // Testing the matches method of Or class with all evaluators returning false
    @Test
    public void testOrMatchesAllFalse() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new ElementMatcher());
        evaluators.add(new ElementMatcher());
        evaluators.add(new ElementMatcher());
        Or or = new Or(evaluators);
        Element element = new Element("div");
        assertFalse(or.matches(finder.rootElement(), element));
    }

    // Testing the matches method of Or class with at least one evaluator returning true
    @Test
    public void testOrMatchesOneTrue() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new ElementMatcher());
        evaluators.add(new Not(new ElementMatcher()));
        evaluators.add(new ElementMatcher());
        Or or = new Or(evaluators);
        Element element = new Element("div");
        assertTrue(or.matches(finder.rootElement(), element));
    }

    // Testing the toString method of Or class
    @Test
    public void testOrToString() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new ElementMatcher());
        evaluators.add(new ElementMatcher());
        Or or = new Or(evaluators);
        String expected = ":or ElementMatcher ElementMatcher";
        String actual = or.toString();
        assertEquals(expected, actual);
    }

    // Testing edge cases and exception handling for Or class

    // Testing the matches method of Or class with empty evaluators list
    @Test(expected = IllegalArgumentException.class)
    public void testOrEmptyList() {
        Or or = new Or(new ArrayList<>());
    }

    // Testing the matches method of Or class with null evaluator
    @Test(expected = NullPointerException.class)
    public void testOrNullEvaluator() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(null);
        Or or = new Or(evaluators);
    }

    // Testing the matches method of Or class with infinite recursion
    @Test(expected = StackOverflowError.class)
    public void testOrInfiniteRecursion() {
        Or or = new Or();
        Evaluator evaluator = or;
        or.add(evaluator);
        evaluator = new ElementMatcher();
        or.add(evaluator);
        evaluator = or;
        or.add(evaluator);
    }
}