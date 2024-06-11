import static org.junit.Assert.*;

import org.jsoup.nodes.Element;
import org.junit.Test;

import java.util.Arrays;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(Arrays.asList(
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return true; // always true
                    }
                },
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return false; // always false
                    }
                }
        ));

        assertFalse(andEvaluator.matches(root, node));
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return false; // always false
                    }
                },
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return true; // always true
                    }
                }
        ));

        assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrEmptyEvaluators() {
        Element root = new Element("root");
        Element node = new Element("node");

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList());

        assertFalse(orEvaluator.matches(root, node));
    }

}