package org.jsoup.select;
import org.jsoup.select.Evaluator;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Selector;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        String cssQuery = "#id .class > tag[attr=value]";
        Evaluator evaluator = QueryParser.parse(cssQuery);
        assertNotNull(evaluator);
    }

    @Test
    public void testParseEmptyQuery() {
        Evaluator evaluator = QueryParser.parse("");
        assertNotNull(evaluator);
    }

    @Test
    public void testParseInvalidQuery() {
        try {
            QueryParser.parse("[invalid query]");
            fail("Expected Selector.SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            // expected exception
        }
    }

    @Test
    public void testParseAttributeWithStartingValue() {
        Evaluator evaluator = QueryParser.parse("tag[attr^=value]");
        assertNotNull(evaluator);
    }

    // Add more test cases for other public methods in QueryParser class

}