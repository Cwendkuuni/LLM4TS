package org.jsoup.select.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Element;
import org.jsoup.helper.ElementFinder;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.And;
import org.jsoup.select.Or;
import org.jsoup.helper.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testCombiningEvaluatorConstructor() {
        CombiningEvaluator evaluator = new CombiningEvaluator();
        assertNotNull(evaluator);
        assertEquals(0, evaluator.evaluators.size());
    }

    @Test
    public void testAndConstructor() {
        Collection<Evaluator> evaluators = Arrays.asList(new ElementFinder("div"), new ElementFinder("p"));
        And andEvaluator = new And(evaluators);
        assertEquals(2, andEvaluator.evaluators.size());
        assertInstanceOf(And.class, andEvaluator);
    }

    @Test
    public void testAndMatches() {
        Element root = new Element("html");
        Element div = new Element("div");
        Element p = new Element("p");
        div.appendChild(p);
        root.appendChild(div);

        Collection<Evaluator> evaluators = Arrays.asList(new ElementFinder("div"), new ElementFinder("p"));
        And andEvaluator = new And(evaluators);

        assertTrue(andEvaluator.matches(root, div));
        assertFalse(andEvaluator.matches(root, p));
    }

    @Test
    public void testAndToString() {
        Collection<Evaluator> evaluators = Arrays.asList(new ElementFinder("div"), new ElementFinder("p"));
        And andEvaluator = new And(evaluators);

        assertEquals("div p", andEvaluator.toString());
    }

    @Test
    public void testOrConstructor() {
        Collection<Evaluator> evaluators = Arrays.asList(new ElementFinder("div"), new ElementFinder("p"));
        Or orEvaluator = new Or(evaluators);
        assertEquals(2, orEvaluator.evaluators.size());
        assertInstanceOf(Or.class, orEvaluator);
    }

    @Test
    public void testOrAddEvaluator() {
        Or orEvaluator = new Or();
        Evaluator evaluator = new ElementFinder("li");
        orEvaluator.add(evaluator);
        assertEquals(1, orEvaluator.evaluators.size());
        assertInstanceOf(Or.class, orEvaluator);
        assertInstanceOf(ElementFinder.class, orEvaluator.evaluators.get(0));
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("html");
        Element div = new Element("div");
        Element p = new Element("p");
        Element li = new Element("li");
        div.appendChild(p);
        root.appendChild(div);
        root.appendChild(li);

        Collection<Evaluator> evaluators = Arrays.asList(new ElementFinder("div"), new ElementFinder("p"), new ElementFinder("li"));
        Or orEvaluator = new Or(evaluators);

        assertTrue(orEvaluator.matches(root, div));
        assertTrue(orEvaluator.matches(root, p));
        assertTrue(orEvaluator.matches(root, li));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOrAddNullEvaluator() {
        Or orEvaluator = new Or();
        orEvaluator.add(null);
    }

    @Test
    public void testOrToString() {
        Collection<Evaluator> evaluators = Arrays.asList(new ElementFinder("div"), new ElementFinder("p"), new ElementFinder("li"));
        Or orEvaluator = new Or(evaluators);

        assertEquals(":or div p li", orEvaluator.toString());
    }
}