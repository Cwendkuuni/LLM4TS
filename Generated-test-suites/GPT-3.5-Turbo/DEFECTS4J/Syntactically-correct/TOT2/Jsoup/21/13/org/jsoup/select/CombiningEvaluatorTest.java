import org.jsoup.nodes.Element;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node");
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return root != null && node != null;
            }
        });
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return root.tagName().equals("root") && node.tagName().equals("node");
            }
        });

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);
        assertTrue(andEvaluator.matches(root, node));
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("root");
        Element node = new Element("node");
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return root.tagName().equals("root");
            }
        });
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return node.tagName().equals("node");
            }
        });

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);
        assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrWithNoEvaluators() {
        Element root = new Element("root");
        Element node = new Element("node");
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(new ArrayList<>());
        assertFalse(orEvaluator.matches(root, node));
    }

}