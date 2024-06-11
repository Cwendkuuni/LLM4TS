package org.jsoup.select;
import static org.junit.Assert.*;

import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div > span";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithSpecialChars() {
        String query = "div[class^=prefix] span";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithMultipleCombining() {
        String query = "div > span, p + a";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithEmptyQuery() {
        String query = "";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithInvalidQuery() {
        String query = "div span]";
        try {
            QueryParser.parse(query);
            fail("Expected Selector.SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            // Pass
        }
    }

}