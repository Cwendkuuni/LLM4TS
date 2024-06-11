import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.jsoup.select.Selector;
import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse("div[class=test]");
        assertNotNull(evaluator);
    }

    @Test
    public void testCombinator() {
        QueryParser parser = new QueryParser("div.test > p");
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testNestedSelector() {
        Evaluator evaluator = QueryParser.parse("div a[href*=example.com]");
        assertNotNull(evaluator);
    }

    @Test
    public void testIndexLessThan() {
        Evaluator evaluator = QueryParser.parse("div p:lt(3)");
        assertNotNull(evaluator);
    }

    @Test
    public void testIndexGreaterThan() {
        Evaluator evaluator = QueryParser.parse("div p:gt(1)");
        assertNotNull(evaluator);
    }

    @Test
    public void testIndexEquals() {
        Evaluator evaluator = QueryParser.parse("div p:eq(2)");
        assertNotNull(evaluator);
    }

    @Test
    public void testAttributeStarting() {
        Evaluator evaluator = QueryParser.parse("div[class^=test]");
        assertNotNull(evaluator);
    }

    @Test
    public void testAttributeEnding() {
        Evaluator evaluator = QueryParser.parse("div[class$=test]");
        assertNotNull(evaluator);
    }

    @Test
    public void testAttributeContaining() {
        Evaluator evaluator = QueryParser.parse("div[class*=test]");
        assertNotNull(evaluator);
    }
    
    @Test
    public void testEmptyInputException() {
        try {
            QueryParser.parse("");
            fail("Should have thrown an IllegalArgumentException");
        } catch (Selector.SelectorParseException e) {
            // as expected
        }
    }

    @Test
    public void testUnknownCombinatorException() {
        try {
            QueryParser.parse("div > p - span");
            fail("Should have thrown a SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            // as expected
        }
    }

    @Test
    public void testUnexpectedTokenException() {
        try {
            QueryParser.parse("div ^ p");
            fail("Should have thrown a SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            // as expected
        }
    }

}