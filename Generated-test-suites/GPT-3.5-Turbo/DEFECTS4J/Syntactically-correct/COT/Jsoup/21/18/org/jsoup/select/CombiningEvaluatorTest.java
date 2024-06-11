
import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;

import java.util.Arrays;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

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

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);
        assertTrue(andEvaluator.matches(root, node));
    }
  
    @Test
    public void testAndDoesNotMatch() {
        Element root = new Element("root");
        Element node = new Element("node");

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

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);
        assertFalse(andEvaluator.matches(root, node));
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

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

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);
        assertTrue(orEvaluator.matches(root, node));
    }
  
    @Test
    public void testOrDoesNotMatch() {
        Element root = new Element("root");
        Element node = new Element("node");

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

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);
        assertFalse(orEvaluator.matches(root, node));
    }
  
    @Test
    public void testOrMatchesFirstClause() {
        Element root = new Element("root");
        Element node = new Element("node");

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

        List<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);
        assertTrue(orEvaluator.matches(root, node));
    }
  
    @Test
    public void testOrAddsEvaluator() {
        Element root = new Element("root");
        Element node = new Element("node");

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

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1);
        orEvaluator.add(evaluator2);
        assertTrue(orEvaluator.matches(root, node));
    }
}
