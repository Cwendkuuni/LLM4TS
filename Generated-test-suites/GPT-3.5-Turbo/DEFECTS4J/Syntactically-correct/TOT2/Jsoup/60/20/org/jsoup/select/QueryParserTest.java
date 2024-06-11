import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class QueryParserTest {
    
    private QueryParser parser;

    @Before
    public void setUp() {
        parser = new QueryParser("");
    }

    @Test
    public void testParse() {
        Evaluator evaluator = parser.parse("div#id > .class");
        assertNotNull(evaluator);
    }

    @Test
    public void testCombinator() {
        parser.combinator('>');
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testConsumeSubQuery() {
        assertEquals("", parser.consumeSubQuery());
    }

    @Test
    public void testFindElements() {
        parser.findElements();
        assertEquals(0, parser.evals.size());
    }

    @Test
    public void testAllElements() {
        parser.allElements();
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testIndexLessThan() {
        parser.indexLessThan();
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testIndexGreaterThan() {
        parser.indexGreaterThan();
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testIndexEquals() {
        parser.indexEquals();
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testCssNthChild() {
        parser.cssNthChild(false, false);
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testById() {
        parser.byId();
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testByClass() {
        parser.byClass();
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testByTag() {
        parser.byTag();
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testByAttribute() {
        parser.byAttribute();
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testContains() {
        parser.contains(false);
        assertEquals(1, parser.evals.size());

        parser.contains(true);
        assertEquals(2, parser.evals.size());
    }

    @Test
    public void testMatches() {
        parser.matches(false);
        assertEquals(1, parser.evals.size());

        parser.matches(true);
        assertEquals(2, parser.evals.size());
    }

    @Test
    public void testNot() {
        parser.not();
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testHas() {
        parser.has();
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testContainsData() {
        parser.containsData();
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testParseWithException() {
        try {
            parser.parse("(invalid query)");
            fail("Should have thrown a SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            // Pass
        }
    }
}