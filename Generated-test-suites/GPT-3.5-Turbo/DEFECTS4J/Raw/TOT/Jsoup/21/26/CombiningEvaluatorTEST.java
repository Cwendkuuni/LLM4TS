import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CombiningEvaluatorTest {
    private CombiningEvaluator.And andEvaluator;
    private CombiningEvaluator.Or orEvaluator;

    @BeforeEach
    void setUp() {
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

        andEvaluator = new CombiningEvaluator.And(evaluators);
        orEvaluator = new CombiningEvaluator.Or(evaluators);
    }

    @Test
    void andMatches_AllEvaluatorsMatch_ReturnsTrue() {
        Element root = new Element("root");
        Element node = new Element("node");

        assertTrue(andEvaluator.matches(root, node));
    }

    @Test
    void andMatches_OneEvaluatorDoesNotMatch_ReturnsFalse() {
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
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        assertFalse(andEvaluator.matches(root, node));
    }

    @Test
    void orMatches_OneEvaluatorMatches_ReturnsTrue() {
        Element root = new Element("root");
        Element node = new Element("node");

        assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    void orMatches_NoEvaluatorMatches_ReturnsFalse() {
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
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        });

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

        assertFalse(orEvaluator.matches(root, node));
    }

    @Test
    void orMatches_AllEvaluatorsMatch_ReturnsTrue() {
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
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

        assertTrue(orEvaluator.matches(root, node));
    }
}