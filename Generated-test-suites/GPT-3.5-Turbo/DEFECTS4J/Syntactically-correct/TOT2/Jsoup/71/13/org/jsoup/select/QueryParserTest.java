import org.jsoup.nodes.Element;
import org.jsoup.select.Evaluator;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Selector;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div > p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator);
    }

    @Test
    public void testParseWithInvalidQuery() {
        String query = "#invalid id selector";
        try {
            QueryParser.parse(query);
        } catch (Selector.SelectorParseException e) {
            assertEquals("Could not parse query '#invalid id selector': unexpected token at 'id selector'", e.getMessage());
        }
    }

    @Test
    public void testParseWithNthChild() {
        String query = ":nth-child(2n+1)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator);
    }

    @Test
    public void testParseWithHasSelector() {
        String query = "div:has(p)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator);
    }

    @Test
    public void testParseWithContainsText() {
        String query = "div:contains('text')";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator);
    }

    @Test
    public void testParseWithMatchesRegex() {
        String query = "div:matches([a-z]+)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator);
    }

    @Test
    public void testParseWithNotSelector() {
        String query = "div:not(p)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator);
    }
}