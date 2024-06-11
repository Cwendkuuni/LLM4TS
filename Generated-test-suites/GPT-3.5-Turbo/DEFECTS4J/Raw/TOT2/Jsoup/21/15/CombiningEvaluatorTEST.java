import org.jsoup.nodes.Element;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
                return false;
            }
        };
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2));
        assertFalse(andEvaluator.matches(root, node));
    }

    @Test
    public void testOrMatches() {
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
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));
        assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrAdd() {
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
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1));
        orEvaluator.add(evaluator2);
        assertFalse(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrEmpty() {
        Element root = new Element("root");
        Element node = new Element("node");
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(new ArrayList<>());
        assertFalse(orEvaluator.matches(root, node));
    }
}