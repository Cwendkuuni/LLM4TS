import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div > p";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertEquals(2, ((CombiningEvaluator.And) evaluator).numEvaluators());
    }

    @Test
    public void testParseWithComplexQuery() {
        String query = "div#id > p.class > span";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertEquals(3, ((CombiningEvaluator.And) evaluator).numEvaluators());
    }

    @Test
    public void testParseWithCommaSeparatedSelectors() {
        String query = "div, p, span";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof CombiningEvaluator.Or);
        assertEquals(3, ((CombiningEvaluator.Or) evaluator).numEvaluators());
    }

    @Test
    public void testParseWithInvalidQuery() {
        String query = "div p";
        Selector.SelectorParseException exception = assertThrows(Selector.SelectorParseException.class, () -> QueryParser.parse(query));
        assertEquals("Could not parse query 'div p': unexpected token at 'p'", exception.getMessage());
    }

    @Test
    public void testParseWithEmptyQuery() {
        String query = "";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AllElements);
    }

    // Add more test cases for the remaining methods

}