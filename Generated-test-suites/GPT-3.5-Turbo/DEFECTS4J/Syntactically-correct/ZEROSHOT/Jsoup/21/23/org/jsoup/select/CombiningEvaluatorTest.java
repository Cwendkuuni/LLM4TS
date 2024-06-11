import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CombiningEvaluatorTest {

    private CombiningEvaluator.And andEvaluator;
    private CombiningEvaluator.Or orEvaluator;

    @Before
    public void setUp() {
        Collection<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        });

        andEvaluator = new CombiningEvaluator.And(evaluators);

        orEvaluator = new CombiningEvaluator.Or(evaluators);
    }

    @Test
    public void testAndMatchesAllEvaluators() {
        Element root = new Element("root");
        Element node = new Element("node");

        boolean result = andEvaluator.matches(root, node);

        Assert.assertFalse(result);
    }

    @Test
    public void testOrMatchesAnyEvaluator() {
        Element root = new Element("root");
        Element node = new Element("node");

        boolean result = orEvaluator.matches(root, node);

        Assert.assertTrue(result);
    }

    @Test
    public void testOrAdd() {
        Element root = new Element("root");
        Element node = new Element("node");

        orEvaluator.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });

        boolean result = orEvaluator.matches(root, node);

        Assert.assertTrue(result);
    }
}