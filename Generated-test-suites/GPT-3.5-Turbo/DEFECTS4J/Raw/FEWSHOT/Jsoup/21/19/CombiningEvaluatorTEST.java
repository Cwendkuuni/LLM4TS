package org.jsoup.select;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Element;
import org.junit.Test;

public class CombiningEvaluatorTest {
    
    @Test
    public void testAndMatches() {
        Element root = new Element("root");
        Element child1 = new Element("child1");
        Element child2 = new Element("child2");
        
        CombiningEvaluator evaluator = new CombiningEvaluator.And(new Evaluator.Tag("root"), new Evaluator.Tag("child1"));
        
        assertTrue(evaluator.matches(root, child1));
        assertFalse(evaluator.matches(root, child2));
    }
    
    @Test
    public void testAndToString() {
        CombiningEvaluator evaluator = new CombiningEvaluator.And(new Evaluator.Tag("root"), new Evaluator.Tag("child1"));
        
        String expected = "root child1";
        String actual = evaluator.toString();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testOrMatches() {
        Element root = new Element("root");
        Element child1 = new Element("child1");
        Element child2 = new Element("child2");
        
        CombiningEvaluator evaluator = new CombiningEvaluator.Or(new Evaluator.Tag("root"), new Evaluator.Tag("child1"));
        
        assertTrue(evaluator.matches(root, child1));
        assertTrue(evaluator.matches(root, child2));
    }
    
    @Test
    public void testOrToString() {
        CombiningEvaluator evaluator = new CombiningEvaluator.Or(new Evaluator.Tag("root"), new Evaluator.Tag("child1"));
        
        String expected = ":or[root, child1]";
        String actual = evaluator.toString();
        
        assertEquals(expected, actual);
    }
}