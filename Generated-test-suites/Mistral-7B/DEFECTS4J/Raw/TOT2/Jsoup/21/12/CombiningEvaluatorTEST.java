import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;
import org.jsoup.select.Elements;
import org.junit.Test;
import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

    @Test
    public void testAndEvaluator() {
        Evaluator evaluator1 = new ElementStartsWithEvaluator("div");
        Evaluator evaluator2 = new ElementHasAttributeEvaluator("class", "test");
        CombiningEvaluator andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);
        Element root = Element.parse("<html><body><div class='test'></div></body></html>");
        Elements nodes = root.select(andEvaluator.toString());
        assertTrue(nodes.size() > 0);
    }
}