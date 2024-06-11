import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Element;
import org.junit.Test;

public class CombiningEvaluatorTest {
    @Test
    public void testAndMatches() {
        Element root = createRootElement();
        Element node = createElement();

        // Test case 1
        ElementEvaluator evaluator1 = createElementEvaluator(true);
        ElementEvaluator evaluator2 = createElementEvaluator(true);
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);
        assertTrue(andEvaluator.matches(root, node));

        // Test case 2
        evaluator1 = createElementEvaluator(true);
        evaluator2 = createElementEvaluator(false);
        andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);
        assertFalse(andEvaluator.matches(root, node));

        // Test case 3
        evaluator1 = createElementEvaluator(false);
        evaluator2 = createElementEvaluator(true);
        andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);
        assertFalse(andEvaluator.matches(root, node));

        // Test case 4
        evaluator1 = createElementEvaluator(false);
        evaluator2 = createElementEvaluator(false);
        andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);
        assertFalse(andEvaluator.matches(root, node));
    }

    @Test
    public void testAndToString() {
        ElementEvaluator evaluator1 = createElementEvaluator(true);
        ElementEvaluator evaluator2 = createElementEvaluator(true);
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);
        String expected = evaluator1.toString() + " " + evaluator2.toString();
        assertEquals(expected, andEvaluator.toString());
    }

    @Test
    public void testOrMatches() {
        Element root = createRootElement();
        Element node = createElement();

        // Test case 1
        ElementEvaluator evaluator1 = createElementEvaluator(true);
        ElementEvaluator evaluator2 = createElementEvaluator(true);
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);
        assertTrue(orEvaluator.matches(root, node));

        // Test case 2
        evaluator1 = createElementEvaluator(true);
        evaluator2 = createElementEvaluator(false);
        orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);
        assertTrue(orEvaluator.matches(root, node));

        // Test case 3
        evaluator1 = createElementEvaluator(false);
        evaluator2 = createElementEvaluator(true);
        orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);
        assertTrue(orEvaluator.matches(root, node));

        // Test case 4
        evaluator1 = createElementEvaluator(false);
        evaluator2 = createElementEvaluator(false);
        orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);
        assertFalse(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrAdd() {
        ElementEvaluator evaluator1 = createElementEvaluator(true);
        ElementEvaluator evaluator2 = createElementEvaluator(true);
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1);
        orEvaluator.add(evaluator2);
        String expected = String.format(":or[%s, %s]", evaluator1.toString(), evaluator2.toString());
        assertEquals(expected, orEvaluator.toString());
    }

    @Test
    public void testOrToString() {
        ElementEvaluator evaluator1 = createElementEvaluator(true);
        ElementEvaluator evaluator2 = createElementEvaluator(true);
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);
        String expected = String.format(":or[%s, %s]", evaluator1.toString(), evaluator2.toString());
        assertEquals(expected, orEvaluator.toString());
    }

    private Element createRootElement() {
        return new Element("root");
    }

    private Element createElement() {
        return new Element("node");
    }

    private ElementEvaluator createElementEvaluator(boolean result) {
        return new ElementEvaluator(result);
    }

    private class ElementEvaluator implements Evaluator {
        private boolean result;

        public ElementEvaluator(boolean result) {
            this.result = result;
        }

        @Override
        public boolean matches(Element root, Element node) {
            return result;
        }

        @Override
        public String toString() {
            return result ? "true" : "false";
        }
    }
}