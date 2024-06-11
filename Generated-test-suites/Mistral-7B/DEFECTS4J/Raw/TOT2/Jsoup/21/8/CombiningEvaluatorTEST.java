import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {

    @Test
    public void testAndEvaluator() {
        List<Evaluator> evaluators = Arrays.asList(new ElementStartsWithEvaluator("div"), new ElementHasClassEvaluator("class1"));
        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);
        Element element = new Element("div");
        element.addClass("class1");
        assertTrue(and.matches(new Element("html"), element)); // true
    }
}