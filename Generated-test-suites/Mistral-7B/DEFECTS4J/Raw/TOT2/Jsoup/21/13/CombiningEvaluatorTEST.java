// AndTest.java
import org.junit.Test;
import org.jsoup.nodes.Element;
import org.jsoup.helper.Elements;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AndTest {
    @Test
    public void testAndWithTrueElements() {
        List<Evaluator> evaluators = Arrays.asList(new ElementStartsWithEvaluator("div"), new ElementHasClassEvaluator("class1"));
        And andEvaluator = new And(evaluators);

        Element root = Elements.parseHtml("<div class='class1'></div>").first();
        Element node = root;

        assertTrue(andEvaluator.matches(root, node));
    }

    @Test
    public void testAndWithFalseElements() {
        List<Evaluator> evaluators = Arrays.asList(new ElementStartsWithEvaluator("div"), new ElementHasClassEvaluator("class2"));
        And andEvaluator = new And(evaluators);

        Element root = Elements.parseHtml("<div></div>").first();
        Element node = Elements.parseHtml("<p class='class2'>").first();

        assertFalse(andEvaluator.matches(root, node));
    }
}