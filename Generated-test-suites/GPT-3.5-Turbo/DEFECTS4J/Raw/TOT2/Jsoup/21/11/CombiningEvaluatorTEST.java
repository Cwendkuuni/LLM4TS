import org.jsoup.nodes.Element;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        List<Evaluator> evaluators = Arrays.asList(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return root.tagName().equals("root") && node.tagName().equals("node");
            }
        });

        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);

        assertTrue(and.matches(root, node));
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        List<Evaluator> evaluators = Arrays.asList(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return root.tagName().equals("root") || node.tagName().equals("node");
            }
        });

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);

        assertTrue(or.matches(root, node));
    }

    @Test
    public void testOrAdd() {
        Element root = new Element("root");
        Element node = new Element("node");

        List<Evaluator> evaluators = Arrays.asList(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return root.tagName().equals("root");
            }
        });

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);
        or.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return node.tagName().equals("node");
            }
        });

        assertTrue(or.matches(root, node));
    }

    @Test
    public void testOrNoMatch() {
        Element root = new Element("root");
        Element node = new Element("node");

        List<Evaluator> evaluators = Arrays.asList(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return !root.tagName().equals("root") && !node.tagName().equals("node");
            }
        });

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);

        assertFalse(or.matches(root, node));
    }
}