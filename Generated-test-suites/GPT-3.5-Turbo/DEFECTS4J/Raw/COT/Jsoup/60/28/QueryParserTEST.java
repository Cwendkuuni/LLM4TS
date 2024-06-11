import org.junit.jupiter.api.Test;
import org.jsoup.select.QueryParser;

import static org.junit.jupiter.api.Assertions.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div.class > span[attr=\"value\"]";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParseWithComma() {
        String query = "div.class, span[attr=\"value\"]";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithMultipleCombinators() {
        String query = "div.class > span[attr=\"value\"] + p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
    }

    // Edge Cases and Exception Handling
    
    @Test
    public void testParseWithEmptyQuery() {
        String query = "";
        QueryParser parser = new QueryParser(query);
        assertThrows(SelectorSelectorParseException.class, () -> {
            Evaluator evaluator = parser.parse();
        });
    }
    
    @Test
    public void testParseWithInvalidCombinator() {
        String query = "div.class & span[attr=\"value\"]";
        QueryParser parser = new QueryParser(query);
        assertThrows(SelectorSelectorParseException.class, () -> {
            Evaluator evaluator = parser.parse();
        });
    }
    
    @Test
    public void testParseWithInvalidAttributeQuery() {
        String query = "div[class=\"value\"]";
        QueryParser parser = new QueryParser(query);
        assertThrows(SelectorSelectorParseException.class, () -> {
            Evaluator evaluator = parser.parse();
        });
    }
    
    @Test
    public void testParseWithInvalidPseudoSelector() {
        String query = "div:invalid";
        QueryParser parser = new QueryParser(query);
        assertThrows(SelectorSelectorParseException.class, () -> {
            Evaluator evaluator = parser.parse();
        });
    }
}