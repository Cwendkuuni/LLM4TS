import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;

public class CombiningEvaluatorTest {

    private Element root = new Element("root");
    private Element node = new Element("node");

    @Test
    public void andMatches_allEvaluatorsMatch_returnsTrue() {
        CombiningEvaluator.And evaluator = new CombiningEvaluator.And(
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

        boolean result = evaluator.matches(root, node);

        Assert.assertTrue(result);
    }

    @Test
    public void andMatches_anyEvaluatorDoesNotMatch_returnsFalse() {
        CombiningEvaluator.And evaluator = new CombiningEvaluator.And(
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

        boolean result = evaluator.matches(root, node);

        Assert.assertFalse(result);
    }

    @Test
    public void orMatches_anyEvaluatorMatches_returnsTrue() {
        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(
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

        boolean result = evaluator.matches(root, node);

        Assert.assertTrue(result);
    }

    @Test
    public void orMatches_noEvaluatorMatches_returnsFalse() {
        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(
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

        boolean result = evaluator.matches(root, node);

        Assert.assertFalse(result);
    }

    @Test
    public void orAdd_singleEvaluatorMatches_returnsTrue() {
        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(Collections.emptyList());
        evaluator.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });

        boolean result = evaluator.matches(root, node);

        Assert.assertTrue(result);
    }
}