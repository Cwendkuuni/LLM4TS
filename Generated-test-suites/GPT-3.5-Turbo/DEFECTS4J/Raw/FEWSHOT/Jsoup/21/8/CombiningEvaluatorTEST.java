package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Evaluator evaluator1 = new Evaluator.Attribute("name", "John");
        Evaluator evaluator2 = new Evaluator.Attribute("age", "25");

        CombiningEvaluator.And and = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2));

        Element root = new Element("root");
        root.attr("name", "John");
        root.attr("age", "25");

        assertTrue(and.matches(root, root));
    }

    @Test
    public void testAndDoesNotMatch() {
        Evaluator evaluator1 = new Evaluator.Attribute("name", "John");
        Evaluator evaluator2 = new Evaluator.Attribute("age", "25");

        CombiningEvaluator.And and = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2));

        Element root = new Element("root");
        root.attr("name", "John");
        root.attr("age", "26");

        assertFalse(and.matches(root, root));
    }

    @Test
    public void testOrMatches() {
        Evaluator evaluator1 = new Evaluator.Attribute("name", "John");
        Evaluator evaluator2 = new Evaluator.Attribute("age", "25");

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));

        Element root = new Element("root");
        root.attr("name", "John");
        root.attr("age", "30");

        assertTrue(or.matches(root, root));
    }

    @Test
    public void testOrDoesNotMatch() {
        Evaluator evaluator1 = new Evaluator.Attribute("name", "John");
        Evaluator evaluator2 = new Evaluator.Attribute("age", "25");

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));

        Element root = new Element("root");
        root.attr("name", "David");
        root.attr("age", "30");

        assertFalse(or.matches(root, root));
    }
}