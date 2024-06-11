package org.jsoup.select;

import org.junit.Test;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        CombiningEvaluator evaluator = new CombiningEvaluator.And(new Evaluator.Tag("a"), new Evaluator.Attribute("href"));
        Element root = new Element("div");
        Element node = new Element("a");
        node.attr("href", "test.com");
        root.appendChild(node);

        assertTrue(evaluator.matches(root, node));
    }

    @Test
    public void testAndDoesNotMatch() {
        CombiningEvaluator evaluator = new CombiningEvaluator.And(new Evaluator.Tag("a"), new Evaluator.Attribute("href"));
        Element root = new Element("div");
        Element node = new Element("a");
        node.attr("src", "test.com");
        root.appendChild(node);

        assertFalse(evaluator.matches(root, node));
    }

    @Test
    public void testOrMatches() {
        CombiningEvaluator evaluator = new CombiningEvaluator.Or(new Evaluator.Tag("a"), new Evaluator.Tag("b"));
        Element root = new Element("div");
        Element node = new Element("a");
        root.appendChild(node);

        assertTrue(evaluator.matches(root, node));
    }

    @Test
    public void testOrDoesNotMatch() {
        CombiningEvaluator evaluator = new CombiningEvaluator.Or(new Evaluator.Tag("a"), new Evaluator.Tag("b"));
        Element root = new Element("div");
        Element node = new Element("c");
        root.appendChild(node);

        assertFalse(evaluator.matches(root, node));
    }

    @Test
    public void testOrMultipleEvaluatorsMatches() {
        CombiningEvaluator evaluator = new CombiningEvaluator.Or(new Evaluator.Tag("a"), new Evaluator.Attribute("href"), new Evaluator.Class("active"));
        Element root = new Element("div");
        Element node = new Element("a");
        node.attr("href", "test.com");
        root.appendChild(node);

        assertTrue(evaluator.matches(root, node));
    }

    @Test
    public void testOrMultipleEvaluatorsDoesNotMatch() {
        CombiningEvaluator evaluator = new CombiningEvaluator.Or(new Evaluator.Tag("a"), new Evaluator.Attribute("href"), new Evaluator.Class("active"));
        Element root = new Element("div");
        Element node = new Element("c");
        root.appendChild(node);

        assertFalse(evaluator.matches(root, node));
    }

    @Test
    public void testOrAddEvaluatorMatches() {
        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(new Evaluator.Tag("a"), new Evaluator.Tag("b"));

        evaluator.add(new Evaluator.Tag("c"));

        Element root = new Element("div");
        Element node = new Element("c");
        root.appendChild(node);

        assertTrue(evaluator.matches(root, node));
    }

    @Test
    public void testOrAddEvaluatorDoesNotMatch() {
        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(new Evaluator.Tag("a"), new Evaluator.Tag("b"));

        evaluator.add(new Evaluator.Tag("c"));

        Element root = new Element("div");
        Element node = new Element("d");
        root.appendChild(node);

        assertFalse(evaluator.matches(root, node));
    }

    @Test
    public void testAndOverrideToString() {
        CombiningEvaluator.And evaluator = new CombiningEvaluator.And(new Evaluator.Tag("a"), new Evaluator.Attribute("href"));
        assertEquals("a [href]", evaluator.toString());
    }

    @Test
    public void testOrOverrideToString() {
        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(new Evaluator.Tag("a"), new Evaluator.Tag("b"));
        assertEquals(":or[a, b]", evaluator.toString());
    }
}