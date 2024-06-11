package org.jsoup.select.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.And;
import org.jsoup.select.Or;
import org.jsoup.helper.ElementFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Test class for CombiningEvaluator.
 */
public class CombiningEvaluatorTest {

    private CombiningEvaluator andEvaluator;
    private CombiningEvaluator orEvaluator;

    @Before
    public void setUp() {
        Element root = new Element("html");

        andEvaluator = new And(Arrays.asList(
                new ElementSelector("p"),
                new ElementSelector("strong")
        ));

        orEvaluator = new Or(Arrays.asList(
                new And(Arrays.asList(
                        new ElementSelector("p"),
                        new ElementSelector("strong")
                )),
                new ElementSelector("h1")
        ));
    }

    @Test
    public void testAndMethod() {
        Element node = new Element("p");
        node.appendText("Test");
        node.appendElement(new Element("strong"));
        node.appendText(" AND ");
        node.appendElement(new Element("strong"));

        assertTrue(andEvaluator.matches(root, node));
    }

    @Test
    public void testAndMethod_False() {
        Element node = new Element("p");
        node.appendText("Test");
        node.appendElement(new Element("strong"));

        assertFalse(andEvaluator.matches(root, node));
    }

    @Test
    public void testOrMethod() {
        Element p1 = new Element("p");
        p1.appendText("Test1");
        p1.appendElement(new Element("strong"));

        Element p2 = new Element("p");
        p2.appendText("Test2");
        p2.appendElement(new Element("strong"));

        Element h1 = new Element("h1");
        h1.appendText("Test3");

        orEvaluator.add(andEvaluator);
        orEvaluator.add(new ElementSelector("h1"));

        assertTrue(orEvaluator.matches(root, p1));
        assertTrue(orEvaluator.matches(root, p2));
        assertTrue(orEvaluator.matches(root, h1));
    }

    @Test
    public void testOrMethod_False() {
        Element node = new Element("p");
        node.appendText("Test");

        assertFalse(orEvaluator.matches(root, node));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOrMethod_NullEvaluator() {
        Or orEvaluator = new Or(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAndMethod_NullEvaluator() {
        And andEvaluator = new And(null);
    }
}