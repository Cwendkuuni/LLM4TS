package org.jsoup.select;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Element;
import org.junit.Test;

public class CombiningEvaluatorTest {
    @Test
    public void testAndMatchesAllEvaluators() {
        Element root = new Element("");
        Element node = new Element("");
        
        Evaluator evaluator1 = new Evaluator() {
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        
        Evaluator evaluator2 = new Evaluator() {
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);
        assertTrue(andEvaluator.matches(root, node));
    }
    
    @Test
    public void testAndDoesNotMatchAnyEvaluator() {
        Element root = new Element("");
        Element node = new Element("");
        
        Evaluator evaluator1 = new Evaluator() {
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        
        Evaluator evaluator2 = new Evaluator() {
            public boolean matches(Element root, Element node) {
                return false;
            }
        };
        
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);
        assertFalse(andEvaluator.matches(root, node));
    }
    
    @Test
    public void testOrMatchesAnyEvaluator() {
        Element root = new Element("");
        Element node = new Element("");
        
        Evaluator evaluator1 = new Evaluator() {
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        
        Evaluator evaluator2 = new Evaluator() {
            public boolean matches(Element root, Element node) {
                return false;
            }
        };
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);
        assertTrue(orEvaluator.matches(root, node));
    }
    
    @Test
    public void testOrDoesNotMatchAnyEvaluator() {
        Element root = new Element("");
        Element node = new Element("");
        
        Evaluator evaluator1 = new Evaluator() {
            public boolean matches(Element root, Element node) {
                return false;
            }
        };
        
        Evaluator evaluator2 = new Evaluator() {
            public boolean matches(Element root, Element node) {
                return false;
            }
        };
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);
        assertFalse(orEvaluator.matches(root, node));
    }
    
    @Test
    public void testOrMatchesOneEvaluator() {
        Element root = new Element("");
        Element node = new Element("");
        
        Evaluator evaluator1 = new Evaluator() {
            public boolean matches(Element root, Element node) {
                return false;
            }
        };
        
        Evaluator evaluator2 = new Evaluator() {
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);
        assertTrue(orEvaluator.matches(root, node));
    }
}