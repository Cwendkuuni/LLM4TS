import org.jsoup.select.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse_method() {
        // Test with valid CSS query
        Evaluator result = QueryParser.parse("#id");
        assertNotNull(result);

        // Test with empty CSS query
        Evaluator result2 = QueryParser.parse("");
        assertNotNull(result2);

        // Test with invalid CSS query
        Evaluator result3 = QueryParser.parse("[attr=value");
        assertNull(result3);
    }

    @Test
    public void testParse_emptyQuery() {
        Evaluator result = QueryParser.parse("");
        assertNotNull(result);
    }

    @Test
    public void testParse_combinators() {
        // Test with each combinator
        Evaluator result1 = QueryParser.parse("div, p");
        assertNotNull(result1);

        Evaluator result2 = QueryParser.parse("div > p");
        assertNotNull(result2);

        Evaluator result3 = QueryParser.parse("div + p");
        assertNotNull(result3);

        Evaluator result4 = QueryParser.parse("div ~ p");
        assertNotNull(result4);

        Evaluator result5 = QueryParser.parse("div p");
        assertNotNull(result5);
    }

    @Test
    public void testParse_validAttributeSelectors() {
        Evaluator result1 = QueryParser.parse("[name=value]");
        assertNotNull(result1);

        Evaluator result2 = QueryParser.parse("[name~=value]");
        assertNotNull(result2);

        Evaluator result3 = QueryParser.parse("[name^=value]");
        assertNotNull(result3);

        Evaluator result4 = QueryParser.parse("[name$=value]");
        assertNotNull(result4);

        Evaluator result5 = QueryParser.parse("[name*=value]");
        assertNotNull(result5);

        Evaluator result6 = QueryParser.parse("[name!=value]");
        assertNotNull(result6);
    }

    @Test
    public void testParse_invalidAttributeSelectors() {
        // Test with missing closing bracket
        Evaluator result1 = QueryParser.parse("[name=value");
        assertNull(result1);

        // Test with incorrect attribute selector syntax
        Evaluator result2 = QueryParser.parse("[name==value]");
        assertNull(result2);
    }
}