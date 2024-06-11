import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.select.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div.container";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withInvalidQuery() {
        String query = "div.[container]";
        try {
            Evaluator evaluator = QueryParser.parse(query);
        } catch (Selector.SelectorParseException e) {
            assertEquals("Could not parse query 'div.[container]': unexpected token at '['", e.getMessage());
        }
    }

    @Test
    public void testParse_withCombinator() {
        String query = "div.container > p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withMultipleCombinators() {
        String query = "div.container > p + span ~ a";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withAttributeSelector() {
        String query = "a[href^=https]";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withPseudoSelector() {
        String query = "div:first-child";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withMultipleSelectors() {
        String query = "div.container, p#paragraph, .bold";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withInvalidSelectors() {
        String query = "h1 >";
        try {
            Evaluator evaluator = QueryParser.parse(query);
        } catch (Selector.SelectorParseException e) {
            assertEquals("Could not parse query 'h1 >': unexpected token at end of query", e.getMessage());
        }
    }

}