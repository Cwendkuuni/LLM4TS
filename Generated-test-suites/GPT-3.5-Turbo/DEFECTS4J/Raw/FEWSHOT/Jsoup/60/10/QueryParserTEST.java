import org.jsoup.select.*;
import org.jsoup.helper.Validate;
import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {
    @Test
    public void testParse() {
        // Test case 1: CSS query starts with a combinator
        Evaluator evaluator1 = QueryParser.parse("div > span");
        assertNotNull(evaluator1);
        
        // Test case 2: CSS query ends with a combinator
        Evaluator evaluator2 = QueryParser.parse("div span >");
        assertNotNull(evaluator2);
        
        // Test case 3: CSS query contains multiple combinators
        Evaluator evaluator3 = QueryParser.parse("div > span + a ~ b");
        assertNotNull(evaluator3);
        
        // Test case 4: Empty CSS query
        Evaluator evaluator4 = QueryParser.parse("");
        assertNotNull(evaluator4);
        
        // Test case 5: CSS query with attribute selector
        Evaluator evaluator5 = QueryParser.parse("[name='test']");
        assertNotNull(evaluator5);
        
        // Test case 6: CSS query with pseudo class selector
        Evaluator evaluator6 = QueryParser.parse(":first-child");
        assertNotNull(evaluator6);
        
        // Test case 7: CSS query with pseudo element selector
        Evaluator evaluator7 = QueryParser.parse("::after");
        assertNotNull(evaluator7);
        
        // Test case 8: CSS query with combination of attribute and pseudo class selectors
        Evaluator evaluator8 = QueryParser.parse("[name='test']:first-child");
        assertNotNull(evaluator8);
        
        // Test case 9: CSS query with combination of combinator and attribute selectors
        Evaluator evaluator9 = QueryParser.parse("div > span[name='test']");
        assertNotNull(evaluator9);
    }
}