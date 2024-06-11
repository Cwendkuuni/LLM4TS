package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node");
        
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        
        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };
        
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2));
        assertFalse(andEvaluator.matches(root, node));
    }
    
    @Test
    public void testOrMatches() {
        Element root = new Element("root");
        Element node = new Element("node");
        
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };
        
        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));
        assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void testAddMethod() {
        Element root = new Element("root");
        Element node = new Element("node");

        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1));
        orEvaluator.add(evaluator2);
        assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void testEmptyInitialEvaluatorsInOr() {
        Element root = new Element("root");
        Element node = new Element("node");

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList());
        assertFalse(orEvaluator.matches(root, node));
    }

    @Test
    public void testToStringAnd() {
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2));
        assertEquals(":or[" + evaluator1.toString() + " " + evaluator2.toString() + "]", andEvaluator.toString());
    }

    @Test
    public void testToStringOr() {
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));
        assertEquals(evaluator1.toString() + " " + evaluator2.toString(), orEvaluator.toString());
    }
}