import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.Assert;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });
        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);
        Assert.assertTrue(and.matches(new Element("root"), new Element("node")));
    }

    @Test
    public void testOrMatches() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        });
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);
        Assert.assertTrue(or.matches(new Element("root"), new Element("node")));
    }

    @Test
    public void testOrAdd() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        });
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);
        or.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });
        Assert.assertTrue(or.matches(new Element("root"), new Element("node")));
    }

    @Test
    public void testOrToString() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        });
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);
        Assert.assertEquals(":or[" + evaluators.toString() + "]", or.toString());
    }
}