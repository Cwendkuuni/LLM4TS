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
        Evaluator eval = parser.parse("");
        assertNotNull(eval);
    }

    @Test
    public void testCombinator() {
        parser.combinator(',');
        parser.combinator('>');
        parser.combinator('+');
        parser.combinator('~');
        parser.combinator(' ');
    }

    @Test
    public void testConsumeSubQuery() {
        String subQuery = parser.consumeSubQuery();
        assertNotNull(subQuery);
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
        parser.cssNthChild(true, false);
        parser.cssNthChild(false, true);
        parser.cssNthChild(true, true);
    }

    @Test
    public void testHas() {
        parser.has();
    }

    @Test
    public void testContains() {
        parser.contains(false);
        parser.contains(true);
    }

    @Test
    public void testContainsData() {
        parser.containsData();
    }

    @Test
    public void testMatches() {
        parser.matches(false);
        parser.matches(true);
    }

    @Test
    public void testNot() {
        parser.not();
    }

}