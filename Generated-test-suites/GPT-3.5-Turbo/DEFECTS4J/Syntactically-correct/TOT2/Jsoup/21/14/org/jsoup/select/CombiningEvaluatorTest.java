import org.jsoup.nodes.Element;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(Collections.emptyList());
        assertTrue(andEvaluator.matches(root, node));
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Collections.emptyList());
        assertFalse(orEvaluator.matches(root, node));
    }

    @Test
    public void testAndToString() {
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(Arrays.asList(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        }));
        assertTrue(andEvaluator.toString().contains(" "));
    }

    @Test
    public void testOrToString() {
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        }));
        assertTrue(orEvaluator.toString().contains(":or"));
    }
}