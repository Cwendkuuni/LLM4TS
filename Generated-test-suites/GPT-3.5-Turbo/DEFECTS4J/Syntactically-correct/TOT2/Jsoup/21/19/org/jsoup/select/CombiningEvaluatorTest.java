import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element rootElement = new Element("div");
        Element childElement = new Element("span");

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(
                new Evaluator.Tag("div"),
                new Evaluator.Tag("span")
        );

        assertTrue(andEvaluator.matches(rootElement, childElement));
    }

    @Test
    public void testAndNotMatches() {
        Element rootElement = new Element("div");
        Element childElement = new Element("span");

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(
                new Evaluator.Tag("div"),
                new Evaluator.Tag("p")
        );

        assertFalse(andEvaluator.matches(rootElement, childElement));
    }

    @Test
    public void testOrMatches() {
        Element rootElement = new Element("div");
        Element childElement = new Element("span");

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(
                Arrays.asList(new Evaluator.Tag("div"), new Evaluator.Tag("p"))
        );

        assertTrue(orEvaluator.matches(rootElement, childElement));
    }

    @Test
    public void testOrNotMatches() {
        Element rootElement = new Element("div");
        Element childElement = new Element("span");

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(
                new Evaluator.Tag("p")
        );

        assertFalse(orEvaluator.matches(rootElement, childElement));
    }
}