
package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

    private CombiningEvaluator.And andEvaluator;
    private CombiningEvaluator.Or orEvaluator;

    @Before
    public void setUp() {
        andEvaluator = new CombiningEvaluator.And();
        orEvaluator = new CombiningEvaluator.Or();
    }

    @Test
    public void testAndMatches_WhenAllEvaluatorsMatch_ReturnsTrue() {
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        Evaluator evaluator3 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        andEvaluator.add(evaluator1);
        andEvaluator.add(evaluator2);
        andEvaluator.add(evaluator3);

        assertTrue(andEvaluator.matches(null, null));
    }

    @Test
    public void testAndMatches_WhenAnyEvaluatorDoesNotMatch_ReturnsFalse() {
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };
        Evaluator evaluator3 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        andEvaluator.add(evaluator1);
        andEvaluator.add(evaluator2);
        andEvaluator.add(evaluator3);

        assertFalse(andEvaluator.matches(null, null));
    }

    @Test
    public void testAndToString_ReturnsEvaluatorsSeparatedBySpace() {
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        Evaluator evaluator3 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        andEvaluator.add(evaluator1);
        andEvaluator.add(evaluator2);
        andEvaluator.add(evaluator3);

        assertEquals("Evaluator Evaluator Evaluator", andEvaluator.toString());
    }

    @Test
    public void testOrMatches_WhenAnyEvaluatorMatches_ReturnsTrue() {
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };
        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        Evaluator evaluator3 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        orEvaluator.add(evaluator1);
        orEvaluator.add(evaluator2);
        orEvaluator.add(evaluator3);

        assertTrue(orEvaluator.matches(null, null));
    }

    @Test
    public void testOrMatches_WhenNoEvaluatorMatches_ReturnsFalse() {
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };
        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };
        Evaluator evaluator3 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        orEvaluator.add(evaluator1);
        orEvaluator.add(evaluator2);
        orEvaluator.add(evaluator3);

        assertFalse(orEvaluator.matches(null, null));
    }

    @Test
    public void testOrToString_ReturnsEvaluatorsWrappedIntoAnAndEvaluator() {
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        Evaluator evaluator3 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        orEvaluator.add(evaluator1);
        orEvaluator.add(evaluator2);
        orEvaluator.add(evaluator3);

        assertEquals(":or[Evaluator Evaluator Evaluator]", orEvaluator.toString());
    }
}

