package org.jsoup.select;
import org.jsoup.select.Evaluator;
import org.jsoup.select.QueryParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParseSimpleQuery() {
        String query = ".class1";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseEmptyQuery() {
        String query = "";
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseQueryWithOnlyWhiteSpaces() {
        String query = "   ";
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseQueryWithInvalidSyntax() {
        String query = "!.class1";
        QueryParser.parse(query);
    }
}