import org.jsoup.select.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class QueryParserTest {

    private QueryParser queryParser;

    @Before
    public void setUp() {
        queryParser = new QueryParser("sample query");
    }

    @Test
    public void testParse() {
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithCombinator() {
        Evaluator evaluator = queryParser.parse();
        assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
    }

    @Test
    public void testParseWithEmptyQuery() {
        queryParser = new QueryParser("");
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithInvalidQuery() {
        queryParser = new QueryParser(null);
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithAttributeEvals() {
        Evaluator evaluator = queryParser.parse();
        assertTrue(evaluator instanceof CombiningEvaluator.And);
    }

    @Test
    public void testParseWithOtherCombinators() {
        Evaluator evaluator = queryParser.parse();
        assertTrue(evaluator instanceof CombiningEvaluator.And);
    }

    @Test
    public void testParseWithUnknownCombinator() {
        queryParser = new QueryParser("unknown query");
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testCssNthChild() {
        queryParser = new QueryParser("nth-child query");
        queryParser.cssNthChild(true, false);
        queryParser.cssNthChild(false, true);
        queryParser.cssNthChild(true, true);
    }

    @Test
    public void testAllElements() {
        queryParser.allElements();
    }

    @Test
    public void testIndexLessThan() {
        queryParser.indexLessThan();
    }

    @Test
    public void testIndexGreaterThan() {
        queryParser.indexGreaterThan();
    }

    @Test
    public void testIndexEquals() {
        queryParser.indexEquals();
    }

    @Test
    public void testHas() {
        queryParser.has();
    }

    @Test
    public void testContains() {
        queryParser.contains(false);
        queryParser.contains(true);
    }

    @Test
    public void testContainsData() {
        queryParser.containsData();
    }

    @Test
    public void testMatches() {
        queryParser.matches(false);
        queryParser.matches(true);
    }

    @Test
    public void testNot() {
        queryParser.not();
    }
}