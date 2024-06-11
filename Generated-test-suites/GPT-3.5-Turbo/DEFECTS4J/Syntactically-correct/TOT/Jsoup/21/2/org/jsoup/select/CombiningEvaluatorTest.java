import org.jsoup.nodes.Element;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

    private static CombiningEvaluator.And andEvaluator;
    private static CombiningEvaluator.Or orEvaluator;

    @BeforeClass
    public static void setUp() {
        Collection<Evaluator> evaluators = new ArrayList<>();
        
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true; // Mock evaluator always returns true
            }
        });
        
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false; // Mock evaluator always returns false
            }
        });

        andEvaluator = new CombiningEvaluator.And(evaluators);
        
        evaluators.remove(0); // Remove evaluator that always returns true
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true; // Mock evaluator always returns true
            }
        });

        orEvaluator = new CombiningEvaluator.Or(evaluators);
    }

    @Test
    public void testAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node");
        
        assertTrue(andEvaluator.matches(root, node));
    }
    
    @Test
    public void testAndDoesNotMatch() {
        Element root = new Element("root");
        Element node = new Element("node");

        assertFalse(andEvaluator.matches(root, node));
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("root");
        Element node = new Element("node");
        
        assertTrue(orEvaluator.matches(root, node));
    }
    
    @Test
    public void testOrDoesNotMatch() {
        Element root = new Element("root");
        Element node = new Element("node");

        assertFalse(orEvaluator.matches(root, node));
    }
    
    @Test
    public void testOrWithEmptyEvaluatorList() {
        Collection<Evaluator> emptyEvaluators = new ArrayList<>();
        CombiningEvaluator.Or emptyOrEvaluator = new CombiningEvaluator.Or(emptyEvaluators);
        
        Element root = new Element("root");
        Element node = new Element("node");
        
        assertFalse(emptyOrEvaluator.matches(root, node));
    }
    
    @Test
    public void testOrWithSingleEvaluator() {
        Collection<Evaluator> singleEvaluatorList = new ArrayList<>();
        singleEvaluatorList.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false; // Mock evaluator always returns false
            }
        });
        
        CombiningEvaluator.Or singleEvaluatorOrEvaluator = new CombiningEvaluator.Or(singleEvaluatorList);
        
        Element root = new Element("root");
        Element node = new Element("node");

        assertFalse(singleEvaluatorOrEvaluator.matches(root, node));
    }
}