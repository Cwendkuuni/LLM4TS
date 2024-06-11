import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return root.tagName().equals("root") && node.tagName().equals("node");
            }
        });

        assertTrue(andEvaluator.matches(root, node));
    }

    @Test
    public void testAndToString() {
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });

        assertTrue(andEvaluator.toString().equals(""));
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return root.tagName().equals("root") && node.tagName().equals("node");
            }
        });

        assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrToString() {
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });

        assertTrue(orEvaluator.toString().equals(":or[]"));
    }

}