package org.jsoup.select;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        List<Evaluator> evaluators = new ArrayList<Evaluator>();
        evaluators.add(new Evaluator.Tag("div"));
        evaluators.add(new Evaluator.Id("myId"));

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        Element root = new Element("body");
        Element node = new Element("div");
        node.attr("id", "myId");

        assertTrue(andEvaluator.matches(root, node));
    }

    @Test
    public void testAndDoesNotMatch() {
        List<Evaluator> evaluators = new ArrayList<Evaluator>();
        evaluators.add(new Evaluator.Tag("div"));
        evaluators.add(new Evaluator.Id("myId"));

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        Element root = new Element("body");
        Element node = new Element("div");
        node.attr("id", "notMyId");

        assertFalse(andEvaluator.matches(root, node));
    }

    @Test
    public void testOrMatches() {
        List<Evaluator> evaluators = new ArrayList<Evaluator>();
        evaluators.add(new Evaluator.Tag("div"));
        evaluators.add(new Evaluator.Id("myId"));

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

        Element root = new Element("body");
        Element node1 = new Element("div");
        Element node2 = new Element("span");
        node2.attr("id", "myId");

        assertTrue(orEvaluator.matches(root, node1));
        assertTrue(orEvaluator.matches(root, node2));
    }

    @Test
    public void testOrDoesNotMatch() {
        List<Evaluator> evaluators = new ArrayList<Evaluator>();
        evaluators.add(new Evaluator.Tag("div"));
        evaluators.add(new Evaluator.Id("myId"));

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

        Element root = new Element("body");
        Element node = new Element("span");
        node.attr("id", "notMyId");

        assertFalse(orEvaluator.matches(root, node));
    }

}