// AndTest.java
import org.jsoup.nodes.Element;
import org.jsoup.helper.ElementNotFoundException;
import org.junit.Test;
import static org.junit.Assert.*;

public class AndTest {
    @Test
    public void testAndEvaluatorWithTwoMatchingEvaluators() {
        Evaluator evaluator1 = new ElementStartsWithTagNameEvaluator("p");
        Evaluator evaluator2 = new ElementHasTextEvaluator("text");
        And and = new And(evaluator1, evaluator2);
        Element root = new Element("html");
        Element node = new Element("body").add("p", "text");
        root.appendChild(node);

        assertTrue(and.matches(root, node));
    }

    @Test(expected = ElementNotFoundException.class)
    public void testAndEvaluatorWithOneDoesNotMatch() {
        Evaluator evaluator1 = new ElementStartsWithTagNameEvaluator("p");
        Evaluator evaluator2 = new ElementHasNoTextEvaluator();
        And and = new And(evaluator1, evaluator2);
        Element root = new Element("html").add("body");

        assertFalse(and.matches(root, null));
    }
}