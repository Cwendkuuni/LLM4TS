package org.jsoup.select;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element root = new Element("div");
        Element node = new Element("span");
        
        Evaluator evaluator1 = new EvaluatorTag("div");
        Evaluator evaluator2 = new EvaluatorTag("span");
        
        CombiningEvaluator.And and = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2));
        boolean result = and.matches(root, node);
        
        Assert.assertTrue(result);
    }

    @Test
    public void testAndDoesNotMatch() {
        Element root = new Element("div");
        Element node = new Element("span");
        
        Evaluator evaluator1 = new EvaluatorTag("div");
        Evaluator evaluator2 = new EvaluatorTag("p");
        
        CombiningEvaluator.And and = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2));
        boolean result = and.matches(root, node);
        
        Assert.assertFalse(result);
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("div");
        Element node = new Element("span");
        
        Evaluator evaluator1 = new EvaluatorTag("div");
        Evaluator evaluator2 = new EvaluatorTag("span");
        
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));
        boolean result = or.matches(root, node);
        
        Assert.assertTrue(result);
    }

    @Test
    public void testOrDoesNotMatch() {
        Element root = new Element("div");
        Element node = new Element("span");
        
        Evaluator evaluator1 = new EvaluatorTag("div");
        Evaluator evaluator2 = new EvaluatorTag("p");
        
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));
        boolean result = or.matches(root, node);
        
        Assert.assertFalse(result);
    }

    @Test
    public void testOrWithAdditionalEvaluator() {
        Element root = new Element("div");
        Element node = new Element("span");
        
        Evaluator evaluator1 = new EvaluatorTag("div");
        Evaluator evaluator2 = new EvaluatorTag("span");
        Evaluator evaluator3 = new EvaluatorTag("p");
        
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));
        or.add(evaluator3);
        
        boolean result = or.matches(root, node);
        
        Assert.assertTrue(result);
    }

}