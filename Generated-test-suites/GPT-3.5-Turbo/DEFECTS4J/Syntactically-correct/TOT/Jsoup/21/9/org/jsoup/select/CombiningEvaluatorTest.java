import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {

    private final Element root = new Element("root");
    private final Element node1 = new Element("node1");
    private final Element node2 = new Element("node2");

    @Test
    public void testAndEvaluatorMatches() {
        Evaluator mockEvaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        Evaluator mockEvaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(Arrays.asList(mockEvaluator1, mockEvaluator2));

        assertTrue(andEvaluator.matches(root, node1));
        assertTrue(andEvaluator.matches(root, node2));
    }

    @Test
    public void testAndEvaluatorDoesNotMatch() {
        Evaluator mockEvaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        Evaluator mockEvaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(Arrays.asList(mockEvaluator1, mockEvaluator2));

        assertFalse(andEvaluator.matches(root, node1));
        assertFalse(andEvaluator.matches(root, node2));
    }

    @Test
    public void testOrEvaluatorMatches() {
        Evaluator mockEvaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        Evaluator mockEvaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(mockEvaluator1, mockEvaluator2));

        assertTrue(orEvaluator.matches(root, node1));
        assertTrue(orEvaluator.matches(root, node2));
    }

    @Test
    public void testOrEvaluatorDoesNotMatch() {
        Evaluator mockEvaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        Evaluator mockEvaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(mockEvaluator1, mockEvaluator2));

        assertFalse(orEvaluator.matches(root, node1));
        assertFalse(orEvaluator.matches(root, node2));
    }

    @Test
    public void testOrEvaluatorWithAndClauseMatches() {
        Evaluator mockEvaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        Evaluator mockEvaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        Evaluator mockEvaluator3 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(mockEvaluator1, mockEvaluator2));
        orEvaluator.add(mockEvaluator3);

        assertTrue(orEvaluator.matches(root, node1));
        assertTrue(orEvaluator.matches(root, node2));
    }

    @Test
    public void testOrEvaluatorWithAndClauseDoesNotMatch() {
        Evaluator mockEvaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        Evaluator mockEvaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        Evaluator mockEvaluator3 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(mockEvaluator1, mockEvaluator2));
        orEvaluator.add(mockEvaluator3);

        assertFalse(orEvaluator.matches(root, node1));
        assertFalse(orEvaluator.matches(root, node2));
    }

    @Test
    public void testOrEvaluatorWithoutAndClauseMatches() {
        Evaluator mockEvaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Collections.singletonList(mockEvaluator1));

        assertTrue(orEvaluator.matches(root, node1));
        assertTrue(orEvaluator.matches(root, node2));
    }

    @Test
    public void testOrEvaluatorWithoutAndClauseDoesNotMatch() {
        Evaluator mockEvaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Collections.singletonList(mockEvaluator1));

        assertFalse(orEvaluator.matches(root, node1));
        assertFalse(orEvaluator.matches(root, node2));
    }
}