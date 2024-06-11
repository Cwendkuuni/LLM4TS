import org.jsoup.select.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        // typical use case
        Evaluator evaluator = QueryParser.parse("div.class#id[attr]");
        assertNotNull(evaluator);

        // edge case - empty query
        evaluator = QueryParser.parse("");
        assertNull(evaluator);

        // edge case - query with only whitespace
        evaluator = QueryParser.parse("   ");
        assertNull(evaluator);

        // error scenario - invalid query
        try {
            evaluator = QueryParser.parse("invalid query");
            fail("Should throw exception for invalid query");
        } catch (Selector.SelectorParseException e) {
            // pass
        }
    }

    @Test
    public void testParse() {
        // typical use case
        QueryParser parser = new QueryParser("div.class#id[attr]");
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);

        // edge case - empty query
        parser = new QueryParser("");
        evaluator = parser.parse();
        assertNull(evaluator);

        // edge case - query with only whitespace
        parser = new QueryParser("   ");
        evaluator = parser.parse();
        assertNull(evaluator);

        // error scenario - invalid query
        try {
            parser = new QueryParser("invalid query");
            evaluator = parser.parse();
            fail("Should throw exception for invalid query");
        } catch (Selector.SelectorParseException e) {
            // pass
        }
    }

    @Test
    public void testConsumeSubQuery() {
        QueryParser parser = new QueryParser("div.class#id");
        parser.parse();
        String subQuery = parser.consumeSubQuery();

        // typical use case
        assertEquals("div.class#id", subQuery);

        // edge case - empty subquery
        parser = new QueryParser("");
        parser.parse();
        subQuery = parser.consumeSubQuery();
        assertEquals("", subQuery);
    }

    @Test
    public void testFindElements() {
        QueryParser parser = new QueryParser("div.class#id[attr]");
        parser.parse();

        // typical use case
        parser.findElements();

        // edge case - query with only whitespace
        parser = new QueryParser("   ");
        parser.parse();
        parser.findElements();

        // error scenario - unhandled query
        try {
            parser = new QueryParser("invalid query");
            parser.parse();
            parser.findElements();
            fail("Should throw exception for unhandled query");
        } catch (Selector.SelectorParseException e) {
            // pass
        }
    }
}