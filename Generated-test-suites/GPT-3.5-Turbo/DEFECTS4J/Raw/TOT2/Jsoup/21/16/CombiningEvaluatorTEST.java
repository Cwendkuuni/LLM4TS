import org.jsoup.nodes.Element;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element rootElement = new Element("root");
        Element nodeElement = new Element("node");

        CombiningEvaluator.And and = new CombiningEvaluator.And(Arrays.asList(
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

        assertFalse(and.matches(rootElement, nodeElement));
    }

    @Test
    public void testOrMatches() {
        Element rootElement = new Element("root");
        Element nodeElement = new Element("node");

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(Arrays.asList(
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

        assertFalse(or.matches(rootElement, nodeElement));
    }

    @Test
    public void testOrAdd() {
        Element rootElement = new Element("root");
        Element nodeElement = new Element("node");

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(Arrays.asList(
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return false;
                    }
                }
        ));

        or.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });

        assertTrue(or.matches(rootElement, nodeElement));
    }
}