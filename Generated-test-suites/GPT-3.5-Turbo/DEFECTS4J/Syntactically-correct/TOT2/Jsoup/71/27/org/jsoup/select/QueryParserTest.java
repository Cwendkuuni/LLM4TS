import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div > p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithInvalidQuery() {
        String query = "div >>> p";
        try {
            Evaluator evaluator = QueryParser.parse(query);
            fail("Should have thrown SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            // Expected exception
        }
    }

    @Test
    public void testCombinator() {
        QueryParser queryParser = new QueryParser("div > p");
        queryParser.parse();
        // Add test case for combinator method
    }

    @Test
    public void testConsumeSubQuery() {
        QueryParser queryParser = new QueryParser("div [attr=value]");
        String subQuery = queryParser.consumeSubQuery();
        assertEquals("[attr=value]", subQuery);
    }

    @Test
    public void testFindElementsById() {
        QueryParser queryParser = new QueryParser("#id");
        queryParser.findElements();
        // Add test case for finding elements by id
    }

    @Test
    public void testFindElementsByClass() {
        QueryParser queryParser = new QueryParser(".class");
        queryParser.findElements();
        // Add test case for finding elements by class
    }

    @Test
    public void testFindElementsByTag() {
        QueryParser queryParser = new QueryParser("div");
        queryParser.findElements();
        // Add test case for finding elements by tag
    }

    // Add more test cases for other public methods and edge cases

}