import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;
import org.jsoup.helper.StringUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CombiningEvaluatorTest {
    @Test
    public void testAnd_matches() {
        Element root = new Element("root");
        Element node = new Element("node");
        
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element element) {
                return true;
            }
        };
        
        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element element) {
                return true;
            }
        };
        
        Evaluator andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);
        boolean result = andEvaluator.matches(root, node);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testAnd_doesNotMatch() {
        Element root = new Element("root");
        Element node = new Element("node");
        
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element element) {
                return true;
            }
        };
        
        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element element) {
                return false;
            }
        };
        
        Evaluator andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);
        boolean result = andEvaluator.matches(root, node);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testOr_matches() {
        Element root = new Element("root");
        Element node = new Element("node");
        
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element element) {
                return false;
            }
        };
        
        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element element) {
                return true;
            }
        };
        
        Evaluator orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);
        boolean result = orEvaluator.matches(root, node);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testOr_doesNotMatch() {
        Element root = new Element("root");
        Element node = new Element("node");
        
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element element) {
                return false;
            }
        };
        
        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element element) {
                return false;
            }
        };
        
        Evaluator orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);
        boolean result = orEvaluator.matches(root, node);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testOr_withAnd_matches() {
        Element root = new Element("root");
        Element node = new Element("node");
        
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element element) {
                return false;
            }
        };
        
        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element element) {
                return true;
            }
        };
        
        Evaluator evaluator3 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element element) {
                return true;
            }
        };
        
        Evaluator andEvaluator = new CombiningEvaluator.And(evaluator2, evaluator3);
        Evaluator orEvaluator = new CombiningEvaluator.Or(evaluator1, andEvaluator);
        boolean result = orEvaluator.matches(root, node);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testOr_withAnd_doesNotMatch() {
        Element root = new Element("root");
        Element node = new Element("node");
        
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element element) {
                return false;
            }
        };
        
        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element element) {
                return false;
            }
        };
        
        Evaluator evaluator3 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element element) {
                return true;
            }
        };
        
        Evaluator andEvaluator = new CombiningEvaluator.And(evaluator2, evaluator3);
        Evaluator orEvaluator = new CombiningEvaluator.Or(evaluator1, andEvaluator);
        boolean result = orEvaluator.matches(root, node);
        
        Assert.assertFalse(result);
    }
}
