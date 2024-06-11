import org.jsoup.nodes.Element;
import org.junit.Test;
import org.jsoup.helper.Elements;
import static org.junit.Assert.*;

public class AndTest {
    @Test
    public void testAndEvaluator() {
        Element root = Element.parse("<html><body><div class='a'><span class='b c'></span></div></body></html>");
        List<Evaluator> evaluators = Arrays.asList(
                new ClassNameEvaluator("a"),
                new ClassNameEvaluator("b")
        );
        And andEvaluator = new And(evaluators);
        assertTrue(andEvaluator.matches(root, root));
        Element div = root.selectFirst("div");
        Element span = div.selectFirst("span");
        assertTrue(andEvaluator.matches(root, span));
    }
}