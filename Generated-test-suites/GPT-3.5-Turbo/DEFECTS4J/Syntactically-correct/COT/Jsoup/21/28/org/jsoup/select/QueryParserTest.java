import static org.junit.Assert.*;
import org.junit.*;
import org.jsoup.select.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse("div");
        assertNotNull(evaluator);
    }

    @Test
    public void testCommaSelector() {
        Evaluator evaluator = QueryParser.parse("div, span");
        assertNotNull(evaluator);
    }

    @Test
    public void testChildCombinator() {
        Evaluator evaluator = QueryParser.parse("div > span");
        assertNotNull(evaluator);
    }

    @Test
    public void testAdjacentSiblingCombinator() {
        Evaluator evaluator = QueryParser.parse("div + span");
        assertNotNull(evaluator);
    }

    @Test
    public void testGeneralSiblingCombinator() {
        Evaluator evaluator = QueryParser.parse("div ~ span");
        assertNotNull(evaluator);
    }

    @Test
    public void testIdSelector() {
        Evaluator evaluator = QueryParser.parse("#myId");
        assertNotNull(evaluator);
    }

    @Test
    public void testClassSelector() {
        Evaluator evaluator = QueryParser.parse(".myClass");
        assertNotNull(evaluator);
    }

    @Test
    public void testTagAndClassSelector() {
        Evaluator evaluator = QueryParser.parse("div.myClass");
        assertNotNull(evaluator);
    }

    @Test
    public void testAttributeSelector() {
        Evaluator evaluator = QueryParser.parse("[data-test]");
        assertNotNull(evaluator);
    }

    @Test
    public void testAttributeWithValueSelector() {
        Evaluator evaluator = QueryParser.parse("[data-test=value]");
        assertNotNull(evaluator);
    }

    @Test
    public void testAttributeWithValueNotSelector() {
        Evaluator evaluator = QueryParser.parse("[data-test!=value]");
        assertNotNull(evaluator);
    }

    @Test
    public void testAttributeWithValueStartingSelector() {
        Evaluator evaluator = QueryParser.parse("[data-test^=value]");
        assertNotNull(evaluator);
    }

    @Test
    public void testAttributeWithValueEndingSelector() {
        Evaluator evaluator = QueryParser.parse("[data-test$=value]");
        assertNotNull(evaluator);
    }

    @Test
    public void testAttributeWithValueContainingSelector() {
        Evaluator evaluator = QueryParser.parse("[data-test*=value]");
        assertNotNull(evaluator);
    }

    @Test
    public void testAttributeWithValueMatchingSelector() {
        Evaluator evaluator = QueryParser.parse("[data-test~=value]");
        assertNotNull(evaluator);
    }

    @Test
    public void testAllElementsSelector() {
        Evaluator evaluator = QueryParser.parse("*");
        assertNotNull(evaluator);
    }

    @Test
    public void testIndexLessThanSelector() {
        Evaluator evaluator = QueryParser.parse(":lt(3)");
        assertNotNull(evaluator);
    }

    @Test
    public void testIndexGreaterThanSelector() {
        Evaluator evaluator = QueryParser.parse(":gt(3)");
        assertNotNull(evaluator);
    }

    @Test
    public void testIndexEqualsSelector() {
        Evaluator evaluator = QueryParser.parse(":eq(3)");
        assertNotNull(evaluator);
    }

    @Test
    public void testHasSelector() {
        Evaluator evaluator = QueryParser.parse(":has(div)");
        assertNotNull(evaluator);
    }

    @Test
    public void testContainsSelector() {
        Evaluator evaluator = QueryParser.parse(":contains(text)");
        assertNotNull(evaluator);
    }

    @Test
    public void testContainsOwnSelector() {
        Evaluator evaluator = QueryParser.parse(":containsOwn(text)");
        assertNotNull(evaluator);
    }

    @Test
    public void testMatchesSelector() {
        Evaluator evaluator = QueryParser.parse(":matches(regex)");
        assertNotNull(evaluator);
    }

    @Test
    public void testMatchesOwnSelector() {
        Evaluator evaluator = QueryParser.parse(":matchesOwn(regex)");
        assertNotNull(evaluator);
    }

    @Test
    public void testNotSelector() {
        Evaluator evaluator = QueryParser.parse(":not(div)");
        assertNotNull(evaluator);
    }
}