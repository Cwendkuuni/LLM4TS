package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CombiningEvaluatorTest {
    private Element root;
    private Element element;
    
    @Before
    public void setup() {
        root = new Element("root");
        element = new Element("element");
    }
    
    @Test
    public void testAndEvaluatorMatches() {
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
        
        List<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);
        
        assertTrue(andEvaluator.matches(root, element));
    }
    
    @Test
    public void testAndEvaluatorDoesNotMatch() {
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
        
        List<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);
        
        assertFalse(andEvaluator.matches(root, element));
    }
    
    @Test
    public void testOrEvaluatorMatches() {
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
        
        List<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);
        
        assertTrue(orEvaluator.matches(root, element));
    }
    
    @Test
    public void testOrEvaluatorDoesNotMatch() {
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
        
        List<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);
        
        assertFalse(orEvaluator.matches(root, element));
    }
}