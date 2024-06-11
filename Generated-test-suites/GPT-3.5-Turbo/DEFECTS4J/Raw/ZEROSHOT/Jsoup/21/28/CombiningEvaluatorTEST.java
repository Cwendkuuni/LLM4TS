package org.jsoup.select;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator.*;

import java.util.ArrayList;
import java.util.List;

public class CombiningEvaluatorTest {
    private Element root;
    private Element node;

    @Before
    public void setUp() {
        // Create a sample HTML document
        Document doc = Document.createShell("");
        root = doc.createElement("div");
        node = doc.createElement("p");
        root.appendChild(node);
    }

    @Test
    public void testAndMatches() {
        List<Evaluator> evaluators = new ArrayList<Evaluator>();
        evaluators.add(new Evaluator.Class("div"));
        evaluators.add(new Evaluator.Tag("p"));
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        boolean match = andEvaluator.matches(root, node);

        Assert.assertTrue(match);
    }

    @Test
    public void testAndDoesNotMatch() {
        List<Evaluator> evaluators = new ArrayList<Evaluator>();
        evaluators.add(new Evaluator.Class("div"));
        evaluators.add(new Evaluator.Tag("span"));
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        boolean match = andEvaluator.matches(root, node);

        Assert.assertFalse(match);
    }

    @Test
    public void testAndToString() {
        List<Evaluator> evaluators = new ArrayList<Evaluator>();
        evaluators.add(new Evaluator.Class("div"));
        evaluators.add(new Evaluator.Tag("p"));
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        String toString = andEvaluator.toString();

        Assert.assertEquals("div p", toString);
    }

    @Test
    public void testOrMatches() {
        List<Evaluator> evaluators = new ArrayList<Evaluator>();
        evaluators.add(new Evaluator.Class("div"));
        evaluators.add(new Evaluator.Tag("span"));
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

        boolean match = orEvaluator.matches(root, node);

        Assert.assertTrue(match);
    }

    @Test
    public void testOrDoesNotMatch() {
        List<Evaluator> evaluators = new ArrayList<Evaluator>();
        evaluators.add(new Evaluator.Class("span"));
        evaluators.add(new Evaluator.Tag("a"));
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

        boolean match = orEvaluator.matches(root, node);

        Assert.assertFalse(match);
    }

    @Test
    public void testOrAddsEvaluator() {
        List<Evaluator> evaluators = new ArrayList<Evaluator>();
        evaluators.add(new Evaluator.Class("div"));
        evaluators.add(new Evaluator.Tag("p"));
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

        orEvaluator.add(new Evaluator.Id("id"));

        boolean match = orEvaluator.matches(root, node);

        Assert.assertTrue(match);
    }

    @Test
    public void testOrToString() {
        List<Evaluator> evaluators = new ArrayList<Evaluator>();
        evaluators.add(new Evaluator.Class("div"));
        evaluators.add(new Evaluator.Tag("p"));
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

        String toString = orEvaluator.toString();

        Assert.assertEquals(":or[div, p]", toString);
    }
}