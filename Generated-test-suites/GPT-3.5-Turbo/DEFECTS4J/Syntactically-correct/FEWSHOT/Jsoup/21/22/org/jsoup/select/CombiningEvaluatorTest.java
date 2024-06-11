package org.jsoup.select;

import org.jsoup.nodes.Element;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombiningEvaluatorTest {
    @Test
    public void testAndMatches() {
        Element root = new Element("div");
        Element node = new Element("span");
        
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new TagEvaluator("div"));
        evaluators.add(new AttributeEvaluator("class", "container"));
        
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);
        
        assertTrue(andEvaluator.matches(root, node));
    }
    
    @Test
    public void testAndDoesNotMatch() {
        Element root = new Element("div");
        Element node = new Element("span");
        
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new TagEvaluator("div"));
        evaluators.add(new AttributeEvaluator("class", "container"));
        
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);
        
        assertFalse(andEvaluator.matches(root, node));
    }
    
    @Test
    public void testOrMatches() {
        Element root = new Element("div");
        Element node = new Element("span");
        
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new TagEvaluator("div"));
        evaluators.add(new AttributeEvaluator("class", "container"));
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);
        
        assertTrue(orEvaluator.matches(root, node));
    }
    
    @Test
    public void testOrDoesNotMatch() {
        Element root = new Element("div");
        Element node = new Element("span");
        
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new TagEvaluator("span"));
        evaluators.add(new AttributeEvaluator("class", "container"));
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);
        
        assertFalse(orEvaluator.matches(root, node));
    }
}