package org.jsoup.select;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class CombiningEvaluatorTest {
    
    @Test
    public void testAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node");
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            public boolean matches(Element root, Element node) {
                return node.tagName().equals("node");
            }
        });
        
        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);
        
        assertTrue(and.matches(root, node));
    }
    
    @Test
    public void testAndNotMatches() {
        Element root = new Element("root");
        Element node = new Element("node");
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            public boolean matches(Element root, Element node) {
                return node.tagName().equals("invalid");
            }
        });
        
        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);
        
        assertFalse(and.matches(root, node));
    }
    
    @Test
    public void testAndToString() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            public boolean matches(Element root, Element node) {
                return node.tagName().equals("node");
            }
            
            public String toString() {
                return "Evaluator 1";
            }
        });
        evaluators.add(new Evaluator() {
            public boolean matches(Element root, Element node) {
                return node.text().equals("text");
            }
            
            public String toString() {
                return "Evaluator 2";
            }
        });
        
        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);
        
        assertEquals("Evaluator 1 Evaluator 2", and.toString());
    }
    
    @Test
    public void testOrMatches() {
        Element root = new Element("root");
        Element node = new Element("node");
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            public boolean matches(Element root, Element node) {
                return node.tagName().equals("invalid");
            }
        });
        
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);
        
        assertTrue(or.matches(root, node));
    }
    
    @Test
    public void testOrNotMatches() {
        Element root = new Element("root");
        Element node = new Element("node");
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            public boolean matches(Element root, Element node) {
                return node.tagName().equals("invalid");
            }
        });
        
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);
        
        assertFalse(or.matches(root, node));
    }
    
    @Test
    public void testOrToString() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            public boolean matches(Element root, Element node) {
                return node.tagName().equals("node");
            }
            
            public String toString() {
                return "Evaluator 1";
            }
        });
        evaluators.add(new Evaluator() {
            public boolean matches(Element root, Element node) {
                return node.text().equals("text");
            }
            
            public String toString() {
                return "Evaluator 2";
            }
        });
        
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);
        
        assertEquals(":or[Evaluator 1, Evaluator 2]", or.toString());
    }
}