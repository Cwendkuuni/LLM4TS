import org.jsoup.select.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {

    private QueryParser queryParser;

    @Before
    public void setUp() {
        queryParser = new QueryParser("");
    }

    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse("div");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withCombinators() {
        Evaluator evaluator = QueryParser.parse("div > p:first-child + span");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withEmptyQuery() {
        Evaluator evaluator = QueryParser.parse("");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withUnknownCombinator() {
        try {
            Evaluator evaluator = QueryParser.parse("div > p#myId");
            fail("Should throw SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            // Expected exception
        }
    }

    @Test
    public void testParse_withInvalidAttributeQuery() {
        try {
            Evaluator evaluator = QueryParser.parse("div[");
            fail("Should throw SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            // Expected exception
        }
    }

    @Test
    public void testParse_withInvalidNthChildQuery() {
        try {
            Evaluator evaluator = QueryParser.parse("div:nth-child(a)");
            fail("Should throw SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            // Expected exception
        }
    }

    @Test
    public void testParse_withContainsTextQuery() {
        Evaluator evaluator = QueryParser.parse("div:contains(text)");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withContainsOwnTextQuery() {
        Evaluator evaluator = QueryParser.parse("div:containsOwn(text)");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withContainsDataQuery() {
        Evaluator evaluator = QueryParser.parse("div:containsData(data)");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withMatchesQuery() {
        Evaluator evaluator = QueryParser.parse("div:matches(regex)");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withMatchesOwnQuery() {
        Evaluator evaluator = QueryParser.parse("div:matchesOwn(regex)");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withNotQuery() {
        Evaluator evaluator = QueryParser.parse("div:not(.class)");
        assertNotNull(evaluator);
    }

}