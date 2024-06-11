
import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        // Create mock evaluators
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

        // Create instance of And evaluator with mock evaluators
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);

        // Test matches method
        assertFalse(andEvaluator.matches(null, null));
    }

    @Test
    public void testAndToString() {
        // Create mock evaluators
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

        // Create instance of And evaluator with mock evaluators
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);

        // Test toString method
        assertTrue(andEvaluator.toString().startsWith(":or"));
    }

    @Test
    public void testOrMatches() {
        // Create mock evaluators
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

        // Create instance of Or evaluator with mock evaluators
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);

        // Test matches method
        assertTrue(orEvaluator.matches(null, null));
    }

    @Test
    public void testOrAdd() {
        // Create mock evaluator
        Evaluator evaluator = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        // Create instance of Or evaluator
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or();

        // Add evaluator using add method
        orEvaluator.add(evaluator);

        // Test matches method
        assertTrue(orEvaluator.matches(null, null));
    }

    @Test
    public void testOrToString() {
        // Create mock evaluator
        Evaluator evaluator = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        // Create instance of Or evaluator with mock evaluator
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator);

        // Test toString method
        assertTrue(orEvaluator.toString().startsWith(":or"));
    }
}
