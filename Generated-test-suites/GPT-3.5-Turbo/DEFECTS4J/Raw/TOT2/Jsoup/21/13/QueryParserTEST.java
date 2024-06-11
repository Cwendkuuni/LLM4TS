import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div > p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithExtraSpace() {
        String query = "div  p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithMultipleCombinators() {
        String query = "div > p + span";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithComma() {
        String query = "div, p, span";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithAttribute() {
        String query = "a[href^=http]";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithId() {
        String query = "div#main-content";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithClass() {
        String query ="div.container";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithPseudoSelector() {
        String query = "div:has(p)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithContainsSelector() {
        String query = "div:contains(text)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithMatchesSelector() {
        String query = "div:matches(regex)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithNotSelector() {
        String query = "div:not(p)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
}