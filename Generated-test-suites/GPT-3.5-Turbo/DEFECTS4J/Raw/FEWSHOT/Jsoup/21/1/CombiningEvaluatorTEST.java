package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Element;
import org.junit.Test;

public class CombiningEvaluatorTest {
    
    @Test
    public void testAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node");
        Element child1 = new Element("child1");
        Element child2 = new Element("child2");
        
        Evaluator evaluator1 = new Evaluator.Tag("child1");
        Evaluator evaluator2 = new Evaluator.Tag("child2");
        
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);
        
        assertFalse(andEvaluator.matches(root, node));
        
        root.appendChild(child1);
        assertFalse(andEvaluator.matches(root, node));
        
        node.appendChild(child1);
        assertFalse(andEvaluator.matches(root, node));
        
        node.appendChild(child2);
        assertTrue(andEvaluator.matches(root, node));
    }
    
    @Test
    public void testAndToString() {
        Evaluator evaluator1 = new Evaluator.Tag("child1");
        Evaluator evaluator2 = new Evaluator.Tag("child2");
        
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);
        
        String expected = "child1 child2";
        assertEquals(expected, andEvaluator.toString());
    }
    
    @Test
    public void testOrMatches() {
        Element root = new Element("root");
        Element node = new Element("node");
        Element child1 = new Element("child1");
        Element child2 = new Element("child2");
        
        Evaluator evaluator1 = new Evaluator.Tag("child1");
        Evaluator evaluator2 = new Evaluator.Tag("child2");
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);
        
        assertFalse(orEvaluator.matches(root, node));
        
        root.appendChild(child1);
        assertTrue(orEvaluator.matches(root, node));
        
        node.appendChild(child1);
        assertTrue(orEvaluator.matches(root, node));
        
        node.appendChild(child2);
        assertTrue(orEvaluator.matches(root, node));
    }
    
    @Test
    public void testOrAdd() {
        Evaluator evaluator1 = new Evaluator.Tag("child1");
        Evaluator evaluator2 = new Evaluator.Tag("child2");
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1);
        orEvaluator.add(evaluator2);
        
        String expected = ":or[child1, child2]";
        assertEquals(expected, orEvaluator.toString());
    }
    
    @Test
    public void testOrToString() {
        Evaluator evaluator1 = new Evaluator.Tag("child1");
        Evaluator evaluator2 = new Evaluator.Tag("child2");
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);
        
        String expected = ":or[child1, child2]";
        assertEquals(expected, orEvaluator.toString());
    }
}