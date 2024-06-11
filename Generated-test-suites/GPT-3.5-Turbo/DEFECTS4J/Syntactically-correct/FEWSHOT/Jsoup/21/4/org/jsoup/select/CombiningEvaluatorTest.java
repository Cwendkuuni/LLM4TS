package org.jsoup.select;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

public class CombiningEvaluatorTest {
    private Element root;
    private Element node;
    
    @Before
    public void setup() {
        root = new Element("div");
        node = new Element("a");
        root.appendChild(node);
    }
    
    @Test
    public void testAndMatches() {
        Evaluator evaluator1 = new Evaluator.Tag("div");
        Evaluator evaluator2 = new Evaluator.Tag("a");
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);
        
        assertTrue(andEvaluator.matches(root, node));
    }
    
    @Test
    public void testAndDoesNotMatch() {
        Evaluator evaluator1 = new Evaluator.Tag("div");
        Evaluator evaluator2 = new Evaluator.Tag("span");
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);
        
        assertFalse(andEvaluator.matches(root, node));
    }
    
    @Test
    public void testOrMatches() {
        Evaluator evaluator1 = new Evaluator.Tag("a");
        Evaluator evaluator2 = new Evaluator.Tag("span");
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);
        
        assertTrue(orEvaluator.matches(root, node));
    }
    
    @Test
    public void testOrDoesNotMatch() {
        Evaluator evaluator1 = new Evaluator.Tag("div");
        Evaluator evaluator2 = new Evaluator.Tag("span");
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);
        
        assertFalse(orEvaluator.matches(root, node));
    }
}