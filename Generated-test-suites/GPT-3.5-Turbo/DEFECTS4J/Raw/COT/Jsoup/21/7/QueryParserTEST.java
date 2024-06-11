
import org.jsoup.select.Evaluator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QueryParserTest {
    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse("div#id.class");
        assertNotNull(evaluator);
    }

    @Test
    public void testComma() {
        Evaluator evaluator = QueryParser.parse("div, span");
        assertNotNull(evaluator);
    }

    @Test
    public void testGreaterThan() {
        Evaluator evaluator = QueryParser.parse("div > span");
        assertNotNull(evaluator);
    }

    @Test
    public void testImmediateNextSibling() {
        Evaluator evaluator = QueryParser.parse("div + span");
        assertNotNull(evaluator);
    }

    @Test
    public void testGeneralSibling() {
        Evaluator evaluator = QueryParser.parse("div ~ span");
        assertNotNull(evaluator);
    }

    @Test
    public void testId() {
        Evaluator evaluator = QueryParser.parse("#id");
        assertNotNull(evaluator);
    }

    @Test
    public void testClass() {
        Evaluator evaluator = QueryParser.parse(".class");
        assertNotNull(evaluator);
    }

    @Test
    public void testTag() {
        Evaluator evaluator = QueryParser.parse("div");
        assertNotNull(evaluator);
    }

    @Test
    public void testAttribute() {
        Evaluator evaluator = QueryParser.parse("[attr]");
        assertNotNull(evaluator);
    }

    @Test
    public void testAttributeWithValue() {
        Evaluator evaluator = QueryParser.parse("[attr=value]");
        assertNotNull(evaluator);
    }

    @Test
    public void testAttributeWithValueNot() {
        Evaluator evaluator = QueryParser.parse("[attr!=value]");
        assertNotNull(evaluator);
    }

    @Test
    public void testAttributeWithValueStarting() {
        Evaluator evaluator = QueryParser.parse("[attr^=value]");
        assertNotNull(evaluator);
    }

    @Test
    public void testAttributeWithValueEnding() {
        Evaluator evaluator = QueryParser.parse("[attr$=value]");
        assertNotNull(evaluator);
    }

    @Test
    public void testAttributeWithValueContaining() {
        Evaluator evaluator = QueryParser.parse("[attr*=value]");
        assertNotNull(evaluator);
    }

    @Test
    public void testAttributeWithValueMatching() {
        Evaluator evaluator = QueryParser.parse("[attr~=value]");
        assertNotNull(evaluator);
    }

    @Test
    public void testAllElements() {
        Evaluator evaluator = QueryParser.parse("*");
        assertNotNull(evaluator);
    }

    @Test
    public void testIndexLessThan() {
        Evaluator evaluator = QueryParser.parse(":lt(3)");
        assertNotNull(evaluator);
    }

    @Test
    public void testIndexGreaterThan() {
        Evaluator evaluator = QueryParser.parse(":gt(3)");
        assertNotNull(evaluator);
    }

    @Test
    public void testIndexEquals() {
        Evaluator evaluator = QueryParser.parse(":eq(3)");
        assertNotNull(evaluator);
    }

    @Test
    public void testHas() {
        Evaluator evaluator = QueryParser.parse(":has(div)");
        assertNotNull(evaluator);
    }

    @Test
    public void testContains() {
        Evaluator evaluator = QueryParser.parse(":contains(text)");
        assertNotNull(evaluator);
    }

    @Test
    public void testContainsOwn() {
        Evaluator evaluator = QueryParser.parse(":containsOwn(text)");
        assertNotNull(evaluator);
    }

    @Test
    public void testMatches() {
        Evaluator evaluator = QueryParser.parse(":matches(regex)");
        assertNotNull(evaluator);
    }

    @Test
    public void testMatchesOwn() {
        Evaluator evaluator = QueryParser.parse(":matchesOwn(regex)");
        assertNotNull(evaluator);
    }

    @Test
    public void testNot() {
        Evaluator evaluator = QueryParser.parse(":not(div)");
        assertNotNull(evaluator);
    }
}

