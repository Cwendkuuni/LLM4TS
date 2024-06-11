package org.jsoup.select;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Element;
import org.junit.Test;

public class CombiningEvaluatorTest {
    @Test
    public void testAndEvaluatorMatches() {
        Element root = new Element("div");
        Element node = new Element("p");
        Evaluator evaluator1 = new Evaluator.Tag("div");
        Evaluator evaluator2 = new Evaluator.Id("myId");
        Evaluator evaluator3 = new Evaluator.Attribute("class", "myClass");
        Evaluator evaluator4 = new Evaluator.AttributeStarting("data");
        
        CombiningEvaluator.And evaluator = new CombiningEvaluator.And(evaluator1, evaluator2, evaluator3, evaluator4);
        assertTrue(evaluator.matches(root, node));
        
        Element node2 = new Element("div");
        assertFalse(evaluator.matches(root, node2));
    }
    
    @Test
    public void testOrEvaluatorMatches() {
        Element root = new Element("div");
        Element node = new Element("p");
        Evaluator evaluator1 = new Evaluator.Tag("p");
        Evaluator evaluator2 = new Evaluator.Id("myId");
        Evaluator evaluator3 = new Evaluator.Attribute("class", "myClass");
        Evaluator evaluator4 = new Evaluator.AttributeStarting("data");
        
        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(evaluator1, evaluator2, evaluator3, evaluator4);
        assertTrue(evaluator.matches(root, node));
        
        Element node2 = new Element("div");
        assertFalse(evaluator.matches(root, node2));
    }
}