import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;

public class CombiningEvaluatorTest {
    @Test
    public void testAnd() {
        // Given: two evaluators that should match the same element
        Evaluator evaluator1 = new ElementSelector("p");
        Evaluator evaluator2 = new ElementSelector("strong");
        And andEvaluator = new And(evaluator1, evaluator2);

        // When: we test an element that matches both evaluators
        Document document = Jsoup.parse("<p><strong>Test</strong></p>");
        Element element = document.selectFirst("p strong");

        // Then: the And evaluator should return true
        assertTrue(andEvaluator.matches(document, element));
    }

    // ...
}

class ElementSelector extends ElementFilter {
    private String selector;

    ElementSelector(String selector) {
        this.selector = selector;
    }

    @Override
    public boolean matches(Element element) {
        return element.selectorMatches(this.selector);
    }
}