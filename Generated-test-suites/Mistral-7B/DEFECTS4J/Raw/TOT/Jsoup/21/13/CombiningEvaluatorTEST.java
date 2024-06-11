import org.junit.Test;
import org.jsoup.nodes.Element;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {

    @Test
    public void testAnd() {
        // Given
        Evaluator evaluator1 = new ElementStartsWithTagNameEvaluator("div");
        Evaluator evaluator2 = new ElementHasClassEvaluator("class1");
        And and = new And(evaluator1, evaluator2);
        Element element = new Element("div", null);
        element.addClass("class1");

        // When
        boolean result = and.matches(null, element);

        // Then
        assertTrue(result);
    }
}

// You can add more test cases for edge cases and error scenarios