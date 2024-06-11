import org.junit.Test;

import static org.junit.Assert.*;

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
    public void testAndNotMatches() {
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
    public void testOrNotMatches() {
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
}