import org.jsoup.nodes.Element;
import org.junit.Test;
import org.jsoup.select.CombiningEvaluator.And;
import org.jsoup.select.CombiningEvaluator.Or;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node");
        
        And andEvaluator = new And(Arrays.asList(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        }));

        assertTrue(andEvaluator.matches(root, node));
    }

    @Test
    public void testAndNotMatches() {
        Element root = new Element("root");
        Element node = new Element("node");
        
        And andEvaluator = new And(Arrays.asList(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        }));

        assertFalse(andEvaluator.matches(root, node));
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("root");
        Element node = new Element("node");
        
        Or orEvaluator = new Or(Arrays.asList(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        }));

        assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrNotMatches() {
        Element root = new Element("root");
        Element node = new Element("node");
        
        Or orEvaluator = new Or(Arrays.asList(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        }));

        assertFalse(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrAdd() {
        Element root = new Element("root");
        Element node = new Element("node");
        
        Or orEvaluator = new Or(Arrays.asList(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        }));

        orEvaluator.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });

        assertTrue(orEvaluator.matches(root, node));
    }

}