package org.jsoup.select;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {
    @Test
    public void testAndMatches() {
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return node.tagName().equals("div");
            }
        };

        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return node.hasClass("hidden");
            }
        };

        CombiningEvaluator.And combiningEvaluator = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2));

        Element divElement = new Element("div");
        assertTrue(combiningEvaluator.matches(divElement, divElement));

        Element hiddenElement = new Element("div").addClass("hidden");
        assertFalse(combiningEvaluator.matches(divElement, hiddenElement));
    }

    @Test
    public void testAndToString() {
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return node.tagName().equals("div");
            }
        };

        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return node.hasClass("hidden");
            }
        };

        CombiningEvaluator.And combiningEvaluator = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2));

        String expected = "div.hidden";
        assertEquals(expected, combiningEvaluator.toString());
    }

    @Test
    public void testOrMatches() {
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return node.tagName().equals("div");
            }
        };

        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return node.hasClass("hidden");
            }
        };

        CombiningEvaluator.Or combiningEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));

        Element divElement = new Element("div");
        assertTrue(combiningEvaluator.matches(divElement, divElement));

        Element hiddenElement = new Element("div").addClass("hidden");
        assertTrue(combiningEvaluator.matches(divElement, hiddenElement));

        Element pElement = new Element("p");
        assertFalse(combiningEvaluator.matches(divElement, pElement));
    }

    @Test
    public void testOrAdd() {
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return node.tagName().equals("div");
            }
        };

        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return node.hasClass("hidden");
            }
        };

        Evaluator evaluator3 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return node.id().equals("myId");
            }
        };

        CombiningEvaluator.Or combiningEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));
        combiningEvaluator.add(evaluator3);

        Element divElement = new Element("div");
        assertTrue(combiningEvaluator.matches(divElement, divElement));

        Element hiddenElement = new Element("div").addClass("hidden");
        assertTrue(combiningEvaluator.matches(divElement, hiddenElement));

        Element pElement = new Element("p");
        assertFalse(combiningEvaluator.matches(divElement, pElement));

        Element idElement = new Element("div").attr("id", "myId");
        assertTrue(combiningEvaluator.matches(divElement, idElement));
    }

    @Test
    public void testOrToString() {
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return node.tagName().equals("div");
            }
        };

        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return node.hasClass("hidden");
            }
        };

        CombiningEvaluator.Or combiningEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));

        String expected = ":or[div, div.hidden]";
        assertEquals(expected, combiningEvaluator.toString());
    }
}