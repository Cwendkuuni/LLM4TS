import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

    private CombiningEvaluator.And andEvaluator;
    private CombiningEvaluator.Or orEvaluator;

    @Before
    public void setup() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }

            @Override
            public String toString() {
                return "Evaluator1";
            }
        });
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }

            @Override
            public String toString() {
                return "Evaluator2";
            }
        });
        andEvaluator = new CombiningEvaluator.And(evaluators);

        List<Evaluator> initialEvaluators = new ArrayList<>();
        initialEvaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }

            @Override
            public String toString() {
                return "Evaluator3";
            }
        });
        initialEvaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }

            @Override
            public String toString() {
                return "Evaluator4";
            }
        });
        orEvaluator = new CombiningEvaluator.Or(initialEvaluators);
    }

    @Test
    public void testAndMatches_AllEvaluatorsMatch_ReturnsTrue() {
        Element rootElement = new Element("div");
        Element nodeElement = new Element("p");
        assertTrue(andEvaluator.matches(rootElement, nodeElement));
    }

    @Test
    public void testAndMatches_OneEvaluatorDoesNotMatch_ReturnsFalse() {
        Element rootElement = new Element("div");
        Element nodeElement = new Element("p");
        andEvaluator.evaluators.get(1).matches(rootElement, nodeElement);
        assertFalse(andEvaluator.matches(rootElement, nodeElement));
    }

    @Test
    public void testAndToString_ReturnsJoinedString() {
        assertEquals("Evaluator1 Evaluator2", andEvaluator.toString());
    }

    @Test
    public void testOrMatches_AllEvaluatorsDoNotMatch_ReturnsFalse() {
        Element rootElement = new Element("div");
        Element nodeElement = new Element("p");
        assertFalse(orEvaluator.matches(rootElement, nodeElement));
    }

    @Test
    public void testOrMatches_OneEvaluatorMatches_ReturnsTrue() {
        Element rootElement = new Element("div");
        Element nodeElement = new Element("p");
        orEvaluator.evaluators.get(1).matches(rootElement, nodeElement);
        assertTrue(orEvaluator.matches(rootElement, nodeElement));
    }

    @Test
    public void testOrToString_ReturnsFormattedString() {
        assertEquals(":or[Evaluator3, Evaluator4]", orEvaluator.toString());
    }
}