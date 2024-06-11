import org.junit.Before;
import org.junit.Test;

import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class CombiningEvaluatorTest {
    
    private Element root;
    private Element node;
    
    @Before
    public void setup() {
        root = new Element("div");
        node = new Element("p");
    }
    
    @Test
    public void testAnd_matchesReturnsTrue() {
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
        
        Evaluator evaluator3 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        
        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluator1, evaluator2, evaluator3);
        
        assertTrue(and.matches(root, node));
    }
    
    @Test
    public void testAnd_matchesReturnsFalse() {
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
        
        Evaluator evaluator3 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        
        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluator1, evaluator2, evaluator3);
        
        assertFalse(and.matches(root, node));
    }
    
    @Test
    public void testOr_matchesReturnsTrue() {
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
        
        Evaluator evaluator3 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };
        
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluator1, evaluator2, evaluator3);
        
        assertTrue(or.matches(root, node));
    }
    
    @Test
    public void testOr_matchesReturnsFalse() {
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
        
        Evaluator evaluator3 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };
        
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluator1, evaluator2, evaluator3);
        
        assertFalse(or.matches(root, node));
    }
    
    @Test
    public void testOr_addEvaluator() {
        Evaluator evaluator = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        
        CombiningEvaluator.Or or = new CombiningEvaluator.Or();
        or.add(evaluator);
        
        assertTrue(or.matches(root, node));
    }
}