import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        ArrayList<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });
        
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) { 
                return false;
            }
        });
        
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);
        
        Element root = new Element("root");
        Element node = new Element("node");
        
        Assert.assertFalse(andEvaluator.matches(root, node));
    }
    
    @Test
    public void testAndToString() {
        ArrayList<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });
        
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        });
        
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);
        
        String expected = "evaluator1 evaluator2";
        String actual = andEvaluator.toString();
        
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testOrMatches() {
        ArrayList<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });
        
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        });
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);
        
        Element root = new Element("root");
        Element node = new Element("node");
        
        Assert.assertTrue(orEvaluator.matches(root, node));
    }
    
    @Test
    public void testOrToString() {
        ArrayList<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });
        
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        });
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);
        
        String expected = ":or[evaluator1, evaluator2]";
        String actual = orEvaluator.toString();
        
        Assert.assertEquals(expected, actual);
    }
}