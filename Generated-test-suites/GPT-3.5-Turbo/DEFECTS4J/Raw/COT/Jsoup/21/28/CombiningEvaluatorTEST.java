
import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        // Arrange
        Element root = new Element("root");
        Element node = new Element("node");
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });
        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);

        // Act
        boolean result = and.matches(root, node);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testAndDoesNotMatch() {
        // Arrange
        Element root = new Element("root");
        Element node = new Element("node");
        List<Evaluator> evaluators = new ArrayList<>();
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
        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);

        // Act
        boolean result = and.matches(root, node);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testAndToString() {
        // Arrange
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });
        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);

        // Act
        String result = and.toString();

        // Assert
        Assert.assertEquals("[eval, eval]", result);
    }

    @Test
    public void testOrMatches() {
        // Arrange
        Element root = new Element("root");
        Element node = new Element("node");
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

        // Act
        boolean result = or.matches(root, node);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testOrDoesNotMatch() {
        // Arrange
        Element root = new Element("root");
        Element node = new Element("node");
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
                return false;
            }
        });
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);

        // Act
        boolean result = or.matches(root, node);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testOrToString() {
        // Arrange
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

        // Act
        String result = or.toString();

        // Assert
        Assert.assertEquals(":or[eval, eval]", result);
    }
}
