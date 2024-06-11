import org.junit.Test;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches_SingleEvaluator() {
        // Arrange
        Element root = new Element("root");
        Element node = new Element("node");

        Evaluator evaluator = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        CombiningEvaluator.And combiningEvaluator = new CombiningEvaluator.And(evaluator);

        // Act
        boolean result = combiningEvaluator.matches(root, node);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAndMatches_MultipleEvaluators_AllMatch() {
        // Arrange
        Element root = new Element("root");
        Element node = new Element("node");

        Collection<Evaluator> evaluators = new ArrayList<>();
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

        CombiningEvaluator.And combiningEvaluator = new CombiningEvaluator.And(evaluators);

        // Act
        boolean result = combiningEvaluator.matches(root, node);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAndMatches_MultipleEvaluators_OneDoesNotMatch() {
        // Arrange
        Element root = new Element("root");
        Element node = new Element("node");

        Collection<Evaluator> evaluators = new ArrayList<>();
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

        CombiningEvaluator.And combiningEvaluator = new CombiningEvaluator.And(evaluators);

        // Act
        boolean result = combiningEvaluator.matches(root, node);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAndToString() {
        // Arrange
        Collection<Evaluator> evaluators = new ArrayList<>();
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
                return true;
            }

            @Override
            public String toString() {
                return "Evaluator2";
            }
        });

        CombiningEvaluator.And combiningEvaluator = new CombiningEvaluator.And(evaluators);

        // Act
        String result = combiningEvaluator.toString();

        // Assert
        assertEquals("Evaluator1 Evaluator2", result);
    }

    @Test
    public void testOrMatches_SingleEvaluator() {
        // Arrange
        Element root = new Element("root");
        Element node = new Element("node");

        Evaluator evaluator = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        CombiningEvaluator.Or combiningEvaluator = new CombiningEvaluator.Or(evaluator);

        // Act
        boolean result = combiningEvaluator.matches(root, node);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testOrMatches_MultipleEvaluators_AllMatch() {
        // Arrange
        Element root = new Element("root");
        Element node = new Element("node");

        Collection<Evaluator> evaluators = new ArrayList<>();
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

        CombiningEvaluator.Or combiningEvaluator = new CombiningEvaluator.Or(evaluators);

        // Act
        boolean result = combiningEvaluator.matches(root, node);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testOrMatches_MultipleEvaluators_OneMatches() {
        // Arrange
        Element root = new Element("root");
        Element node = new Element("node");

        Collection<Evaluator> evaluators = new ArrayList<>();
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

        CombiningEvaluator.Or combiningEvaluator = new CombiningEvaluator.Or(evaluators);

        // Act
        boolean result = combiningEvaluator.matches(root, node);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testOrMatches_MultipleEvaluators_NoneMatch() {
        // Arrange
        Element root = new Element("root");
        Element node = new Element("node");

        Collection<Evaluator> evaluators = new ArrayList<>();
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

        CombiningEvaluator.Or combiningEvaluator = new CombiningEvaluator.Or(evaluators);

        // Act
        boolean result = combiningEvaluator.matches(root, node);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testOrAdd() {
        // Arrange
        Element root = new Element("root");
        Element node = new Element("node");

        CombiningEvaluator.Or combiningEvaluator = new CombiningEvaluator.Or();

        // Act
        combiningEvaluator.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });

        boolean result = combiningEvaluator.matches(root, node);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testOrToString() {
        // Arrange
        Collection<Evaluator> evaluators = new ArrayList<>();
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
                return true;
            }

            @Override
            public String toString() {
                return "Evaluator2";
            }
        });

        CombiningEvaluator.Or combiningEvaluator = new CombiningEvaluator.Or(evaluators);

        // Act
        String result = combiningEvaluator.toString();

        // Assert
        assertEquals(":or[Evaluator1, Evaluator2]", result);
    }

}