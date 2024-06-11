import org.jsoup.select.*;
import org.jsoup.nodes.Element;
import org.jsoup.parser.TokenQueue;

import static org.junit.Assert.*;
import org.junit.Test;

public class QueryParserTest {
    @Test
    public void parse_validQuery_returnsEvaluator() {
        String query = "div#myId";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void parse_emptyQuery_throwsException() {
        String query = "";
        try {
            Evaluator evaluator = QueryParser.parse(query);
            fail("Expected SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Could not parse query '': unexpected token at ''", e.getMessage());
        }
    }

    @Test
    public void parse_queryWithCombinator_returnsEvaluator() {
        String query = "div > span";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void parse_queryWithInvalidCombinator_throwsException() {
        String query = "div $ span";
        try {
            Evaluator evaluator = QueryParser.parse(query);
            fail("Expected SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Unknown combinator: $", e.getMessage());
        }
    }

    @Test
    public void parse_queryWithAttribute_returnsEvaluator() {
        String query = "div[class=test]";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void parse_queryWithInvalidAttribute_throwsException() {
        String query = "div[@class=test]";
        try {
            Evaluator evaluator = QueryParser.parse(query);
            fail("Expected SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Could not parse attribute query '[@class=test]': unexpected token at '[@class=test]'", e.getMessage());
        }
    }

    @Test
    public void parse_queryWithPseudoSelector_returnsEvaluator() {
        String query = "div:first-child";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void parse_invalidPseudoSelector_throwsException() {
        String query = "div:not";
        try {
            Evaluator evaluator = QueryParser.parse(query);
            fail("Expected SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Could not parse query 'div:not': unexpected token at 'div:not'", e.getMessage());
        }
    }
}

