import org.jsoup.nodes.Element;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import org.jsoup.select.CombiningEvaluator.And;
import org.jsoup.select.CombiningEvaluator.Or;

import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        And andEvaluator = new And(Arrays.asList(
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

        assertFalse(andEvaluator.matches(root, node));
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        Or orEvaluator = new Or(Arrays.asList(
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return false;
                    }
                },
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return true;
                    }
                }
        ));

        assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void testAddEvaluatorToOr() {
        Element root = new Element("root");
        Element node = new Element("node");

        Or orEvaluator = new Or(Arrays.asList(
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return false;
                    }
                }
        ));

        orEvaluator.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });

        assertTrue(orEvaluator.matches(root, node));
    }

    // Additional test cases for edge cases and exception scenarios can be added here

}