import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CombiningEvaluatorTest {

    private Element root;
    private Element node;

    @Test
    public void testAndMatches() {
        List<Evaluator> evaluators = Arrays.asList(
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

        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);
        Assert.assertTrue(and.matches(root, node));
    }

    @Test
    public void testAndDoesNotMatch() {
        List<Evaluator> evaluators = Arrays.asList(
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

        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);
        Assert.assertFalse(and.matches(root, node));
    }

    @Test
    public void testOrMatches() {
        List<Evaluator> evaluators = Arrays.asList(
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return false;
                    }
                },
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return true;
                    }
                }
        );

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);
        Assert.assertTrue(or.matches(root, node));
    }

    @Test
    public void testOrDoesNotMatch() {
        List<Evaluator> evaluators = Arrays.asList(
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

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);
        Assert.assertFalse(or.matches(root, node));
    }
}