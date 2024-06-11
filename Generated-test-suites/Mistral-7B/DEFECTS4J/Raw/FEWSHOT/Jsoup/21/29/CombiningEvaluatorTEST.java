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
    public void testAndCombiningEvaluator() {
        Evaluator evaluator1 = new MockEvaluator("tagName1");
        Evaluator evaluator2 = new MockEvaluator("tagName2");
        Evaluator andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);

        Element element = new Element("test");
        element.tagName("tagName1");
        element.tagName("tagName2");

        assertTrue(andEvaluator.matches(null, element));
    }

    @Test
    public void testAndCombiningEvaluator_firstEvaluatorReturnsFalse() {
        Evaluator evaluator1 = new MockEvaluator("tagName1");
        Evaluator evaluator2 = new MockEvaluator("tagName2");
        Evaluator andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);

        Element element = new Element("test");

        assertFalse(andEvaluator.matches(null, element));
    }

    @Test
    public void testAndCombiningEvaluator_secondEvaluatorReturnsFalse() {
        Evaluator evaluator1 = new MockEvaluator("tagName1");
        Evaluator evaluator2 = new MockEvaluator("tagName3");
        Evaluator andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);

        Element element = new Element("test");
        element.tagName("tagName1");

        assertFalse(andEvaluator.matches(null, element));
    }

    @Test
    public void testOrCombiningEvaluator() {
        Evaluator evaluator1 = new MockEvaluator("tagName1");
        Evaluator evaluator2 = new MockEvaluator("tagName2");
        Evaluator orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));

        Element element1 = new Element("test1");
        element1.tagName("tagName1");

        Element element2 = new Element("test2");
        element2.tagName("tagName2");

        assertTrue(orEvaluator.matches(null, element1));
        assertTrue(orEvaluator.matches(null, element2));
    }

    @Test
    public void testOrCombiningEvaluator_firstEvaluatorMatches() {
        Evaluator evaluator1 = new MockEvaluator("tagName1");
        Evaluator evaluator2 = new MockEvaluator("tagName3");
        Evaluator orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));

        Element element = new Element("test");
        element.tagName("tagName1");

        assertTrue(orEvaluator.matches(null, element));
    }

    @Test
    public void testOrCombiningEvaluator_secondEvaluatorMatches() {
        Evaluator evaluator1 = new MockEvaluator("tagName1");
        Evaluator evaluator2 = new MockEvaluator("tagName2");
        Evaluator orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));

        Element element = new Element("test");
        element.tagName("tagName2");

        assertTrue(orEvaluator.matches(null, element));
    }

    @Test
    public void testOrCombiningEvaluator_multipleEvaluators() {
        Evaluator evaluator1 = new MockEvaluator("tagName1");
        Evaluator evaluator2 = new MockEvaluator("tagName2");
        Evaluator evaluator3 = new MockEvaluator("tagName3");
        Evaluator orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2, evaluator3));

        Element element1 = new Element("test1");
        element1.tagName("tagName1");

        Element element2 = new Element("test2");
        element2.tagName("tagName2");

        Element element3 = new Element("test3");
        element3.tagName("tagName3");

        assertTrue(orEvaluator.matches(null, element1));
        assertTrue(orEvaluator.matches(null, element2));
        assertTrue(orEvaluator.matches(null, element3));
    }

    @Test
    public void testOrCombiningEvaluator_noMatches() {
        Evaluator evaluator1 = new MockEvaluator("tagName1");
        Evaluator evaluator2 = new MockEvaluator("tagName3");
        Evaluator orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));

        Element element = new Element("test");

        assertFalse(orEvaluator.matches(null, element));
    }

    private static class MockEvaluator implements Evaluator {
        private final String tagName;

        MockEvaluator(String tagName) {
            this.tagName = tagName;
        }

        @Override
        public boolean matches(Element root, Element node) {
            return node.tagName().equalsIgnoreCase(tagName);
        }

        @Override
        public String toString() {
            return String.format("mockEvaluator(%s)", tagName);
        }
    }
}