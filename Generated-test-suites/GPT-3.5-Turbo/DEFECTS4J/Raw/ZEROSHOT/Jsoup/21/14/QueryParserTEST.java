import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class QueryParserTest {

    private QueryParser queryParser;

    @Before
    public void setUp() {
        queryParser = new QueryParser("test");
    }

    @Test
    public void testParse() {
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testCombinator() {
        queryParser.combinator('>');
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testConsumeSubQuery() {
        String subQuery = queryParser.consumeSubQuery();
        assertNotNull(subQuery);
    }

    @Test
    public void testFindElements() {
        queryParser.findElements();
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testById() {
        queryParser.byId();
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testByClass() {
        queryParser.byClass();
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testByTag() {
        queryParser.byTag();
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testByAttribute() {
        queryParser.byAttribute();
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testAllElements() {
        queryParser.allElements();
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testIndexLessThan() {
        queryParser.indexLessThan();
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testIndexGreaterThan() {
        queryParser.indexGreaterThan();
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testIndexEquals() {
        queryParser.indexEquals();
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testHas() {
        queryParser.has();
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testContains() {
        queryParser.contains(false);
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testContainsOwn() {
        queryParser.contains(true);
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testMatches() {
        queryParser.matches(false);
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testMatchesOwn() {
        queryParser.matches(true);
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testNot() {
        queryParser.not();
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
    }
}