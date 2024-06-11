package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node");
        CombiningEvaluator.And evaluator = new CombiningEvaluator.And(Arrays.asList(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        }));
        assertTrue(evaluator.matches(root, node));
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("root");
        Element node = new Element("node");
        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(Arrays.asList(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        }));
        assertFalse(evaluator.matches(root, node));
    }
}