import org.jsoup.nodes.Element;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element rootElement = new Element("root");
        Element childElement = new Element("child");

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(
                new Evaluator.Tag("root"),
                new Evaluator.Tag("child")
        );

        assertTrue(andEvaluator.matches(rootElement, childElement));
    }

    @Test
    public void testAndDoesNotMatch() {
        Element rootElement = new Element("root");
        Element childElement = new Element("child");

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(
                new Evaluator.Tag("root"),
                new Evaluator.Tag("another")
        );

        assertFalse(andEvaluator.matches(rootElement, childElement));
    }

    @Test
    public void testOrMatches() {
        Element rootElement = new Element("root");
        Element childElement = new Element("child");

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(
                Arrays.asList(
                        new Evaluator.Tag("root"),
                        new Evaluator.Tag("another")
                )
        );

        assertTrue(orEvaluator.matches(rootElement, childElement));
    }

    @Test
    public void testOrDoesNotMatch() {
        Element rootElement = new Element("root");
        Element childElement = new Element("child");

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(
                Arrays.asList(
                        new Evaluator.Tag("one"),
                        new Evaluator.Tag("two")
                )
        );

        assertFalse(orEvaluator.matches(rootElement, childElement));
    }

}