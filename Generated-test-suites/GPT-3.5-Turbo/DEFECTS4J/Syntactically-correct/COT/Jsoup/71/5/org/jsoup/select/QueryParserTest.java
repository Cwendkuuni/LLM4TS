import org.jsoup.select.Evaluator;
import org.jsoup.select.QueryParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {
    private QueryParser parser = new QueryParser("");

    @Test
    public void testParse() {
        Evaluator result = parser.parse("mock");
        assertNotNull(result);
    }

    @Test
    public void testParseInvalidQuery() {
        try {
            Evaluator result = parser.parse("[invalid");
            fail("Expected SelectorParseException was not thrown");
        } catch (Exception e) {
            assertEquals("org.jsoup.select.Selector$SelectorParseException: Could not parse query '%s': unexpected token at '%s'", e.getMessage());
        }
    }

    @Test
    public void testCombinator() {
        Evaluator result = parser.parse("a > b");
        assertNotNull(result);
    }

    @Test
    public void testFindElements() {
        parser.findElements();
    }

    @Test
    public void testById() {
        parser.byId();
    }

    @Test
    public void testByClass() {
        parser.byClass();
    }

    @Test
    public void testByTag() {
        parser.byTag();
    }

    @Test
    public void testByAttribute() {
        parser.byAttribute();
    }

    @Test
    public void testAllElements() {
        parser.allElements();
    }

    @Test
    public void testIndexLessThan() {
        parser.indexLessThan();
    }

    @Test
    public void testIndexGreaterThan() {
        parser.indexGreaterThan();
    }

    @Test
    public void testIndexEquals() {
        parser.indexEquals();
    }

    @Test
    public void testCssNthChild() {
        parser.cssNthChild(false, false);
    }

    @Test
    public void testHas() {
        parser.has();
    }

    @Test
    public void testContains() {
        parser.contains(false);
    }

    @Test
    public void testContainsOwn() {
        parser.contains(true);
    }

    @Test
    public void testContainsData() {
        parser.containsData();
    }

    @Test
    public void testMatches() {
        parser.matches(false);
    }

    @Test
    public void testMatchesOwn() {
        parser.matches(true);
    }

    @Test
    public void testNot() {
        parser.not();
    }
}