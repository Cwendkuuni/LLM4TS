
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And();
        assertTrue(andEvaluator.matches(new Element("root"), new Element("node")));
    }

    @Test
    public void testAndDoesNotMatch() {
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And();
        assertFalse(andEvaluator.matches(new Element("root"), null));
    }

    @Test
    public void testAndWithEvaluatorsMatches() {
        Collection<Evaluator> evaluators = Arrays.asList(
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return true;
                    }
                },
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return true;
                    }
                }
        );
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);
        assertTrue(andEvaluator.matches(new Element("root"), new Element("node")));
    }

    @Test
    public void testAndWithEvaluatorsDoesNotMatch() {
        Collection<Evaluator> evaluators = Arrays.asList(
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return true;
                    }
                },
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return false;
                    }
                }
        );
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);
        assertFalse(andEvaluator.matches(new Element("root"), new Element("node")));
    }

    @Test
    public void testOrMatches() {
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or();
        assertTrue(orEvaluator.matches(new Element("root"), new Element("node")));
    }

    @Test
    public void testOrDoesNotMatch() {
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or();
        assertFalse(orEvaluator.matches(new Element("root"), null));
    }

    @Test
    public void testOrWithEvaluatorsMatches() {
        Collection<Evaluator> evaluators = Arrays.asList(
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return true;
                    }
                },
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return false;
                    }
                }
        );
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);
        assertTrue(orEvaluator.matches(new Element("root"), new Element("node")));
    }

    @Test
    public void testOrWithEvaluatorsDoesNotMatch() {
        Collection<Evaluator> evaluators = Arrays.asList(
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return false;
                    }
                },
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return false;
                    }
                }
        );
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);
        assertFalse(orEvaluator.matches(new Element("root"), new Element("node")));
    }
}
