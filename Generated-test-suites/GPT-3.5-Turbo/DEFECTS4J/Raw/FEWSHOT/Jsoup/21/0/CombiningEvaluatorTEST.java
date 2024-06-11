package org.jsoup.select;

import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Element;
import org.junit.Test;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element root = new Element("html");
        Element node = new Element("div");
        
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        
        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);
        boolean result = andEvaluator.matches(root, node);
        assertEquals(true, result);
    }

    @Test
    public void testAndDoesNotMatch() {
        Element root = new Element("html");
        Element node = new Element("div");
        
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
        
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);
        boolean result = andEvaluator.matches(root, node);
        assertEquals(false, result);
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("html");
        Element node = new Element("div");
        
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
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);
        boolean result = orEvaluator.matches(root, node);
        assertEquals(true, result);
    }

    @Test
    public void testOrDoesNotMatch() {
        Element root = new Element("html");
        Element node = new Element("div");
        
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };
        
        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);
        boolean result = orEvaluator.matches(root, node);
        assertEquals(false, result);
    }
}