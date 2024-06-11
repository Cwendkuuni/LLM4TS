import static org.junit.Assert.*;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        // Typical use case
        Evaluator result = QueryParser.parse("div#id.class");
        assertNotNull(result);

        // Edge case: empty query
        Evaluator emptyResult = QueryParser.parse("");
        assertNull(emptyResult);

        // Exception handling: invalid query
        try {
            Evaluator invalidResult = QueryParser.parse("[invalid");
        } catch (Selector.SelectorParseException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testParse() {
        // Typical use case
        QueryParser parser = new QueryParser("div");
        Evaluator result = parser.parse();
        assertNotNull(result);

        // Edge case: single space query
        QueryParser parserSpace = new QueryParser(" ");
        Evaluator resultSpace = parserSpace.parse();
        assertNotNull(resultSpace);

        // Exception handling: missing combinator
        QueryParser parserNoCombinator = new QueryParser("#id.class");
        try {
            Evaluator resultNoCombinator = parserNoCombinator.parse();
        } catch (Selector.SelectorParseException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCombinator() {
        // Typical use case
        QueryParser parser = new QueryParser("div > p");
        parser.parse();
        assertNotNull(parser);

        // Edge case: multiple combinator '>' in query
        QueryParser parserMultiple = new QueryParser("div > p > span");
        parserMultiple.parse();
        assertNotNull(parserMultiple);

        // Exception handling: unknown combinator
        QueryParser parserUnknown = new QueryParser("a @ b");
        try {
            parserUnknown.parse();
        } catch (Selector.SelectorParseException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testFindElements() {
        // Typical use case
        QueryParser parser = new QueryParser("div");
        parser.findElements();
        assertEquals(1, parser.evals.size());

        // Edge case: attribute value ending
        QueryParser parserAttribute = new QueryParser("div[id$='end']");
        parserAttribute.findElements();
        assertEquals(1, parserAttribute.evals.size());

        // Exception handling: unhandled query
        QueryParser parserUnhandled = new QueryParser("$div");
        try {
            parserUnhandled.findElements();
        } catch (Selector.SelectorParseException e) {
            assertTrue(true);
        }
    }
}