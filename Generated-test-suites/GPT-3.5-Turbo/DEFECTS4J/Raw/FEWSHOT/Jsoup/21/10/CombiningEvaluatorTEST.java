package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator.And;
import org.jsoup.select.CombiningEvaluator.Or;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element root = new Element("html");
        Element node = new Element("div");

        // Test with no evaluators
        And and = new And();
        assertTrue(and.matches(root, node));

        // Test with one evaluator that returns true
        and = new And(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });
        assertTrue(and.matches(root, node));

        // Test with one evaluator that returns false
        and = new And(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        });
        assertFalse(and.matches(root, node));

        // Test with multiple evaluators, all returning true
        and = new And(Arrays.asList(
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return true;
                    }
                },
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return true;
                    }
                }
        ));
        assertTrue(and.matches(root, node));

        // Test with multiple evaluators, one returning false
        and = new And(Arrays.asList(
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return true;
                    }
                },
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return false;
                    }
                }
        ));
        assertFalse(and.matches(root, node));
    }

    @Test
    public void testAndToString() {
        And and = new And(Arrays.asList(
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return true;
                    }

                    @Override
                    public String toString() {
                        return "evaluator1";
                    }
                },
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return true;
                    }

                    @Override
                    public String toString() {
                        return "evaluator2";
                    }
                }
        ));

        assertEquals("evaluator1 evaluator2", and.toString());
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("html");
        Element node = new Element("div");

        // Test with no evaluators
        Or or = new Or();
        assertFalse(or.matches(root, node));

        // Test with one evaluator that returns true
        or = new Or(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });
        assertTrue(or.matches(root, node));

        // Test with one evaluator that returns false
        or = new Or(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        });
        assertFalse(or.matches(root, node));

        // Test with multiple evaluators, all returning true
        or = new Or(Arrays.asList(
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return true;
                    }
                },
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return true;
                    }
                }
        ));
        assertTrue(or.matches(root, node));

        // Test with multiple evaluators, one returning true
        or = new Or(Arrays.asList(
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return true;
                    }
                },
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return false;
                    }
                }
        ));
        assertTrue(or.matches(root, node));

        // Test with multiple evaluators, all returning false
        or = new Or(Arrays.asList(
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return false;
                    }
                },
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return false;
                    }
                }
        ));
        assertFalse(or.matches(root, node));
    }

    @Test
    public void testOrAdd() {
        Or or = new Or();

        // Test adding one evaluator
        or.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });
        assertTrue(or.matches(new Element("html"), new Element("div")));

        // Test adding multiple evaluators
        List<Evaluator> evaluators = Arrays.asList(
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return true;
                    }
                },
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return false;
                    }
                }
        );
        or.add(evaluators);
        assertTrue(or.matches(new Element("html"), new Element("div")));
    }

    @Test
    public void testOrToString() {
        Or or = new Or(Arrays.asList(
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return true;
                    }

                    @Override
                    public String toString() {
                        return "evaluator1";
                    }
                },
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return true;
                    }

                    @Override
                    public String toString() {
                        return "evaluator2";
                    }
                }
        ));

        assertEquals(":or[evaluator1, evaluator2]", or.toString());
    }
}