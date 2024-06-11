
import static org.junit.Assert.*;
import org.jsoup.select.*;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        // Test case #1
        String query1 = "div";
        Evaluator result1 = QueryParser.parse(query1);
        assertNotNull(result1);
        
        // Test case #2
        String query2 = "#id";
        Evaluator result2 = QueryParser.parse(query2);
        assertNotNull(result2);
        
        // Test case #3
        String query3 = ".class";
        Evaluator result3 = QueryParser.parse(query3);
        assertNotNull(result3);
        
        // Test case #4
        String query4 = "div > .class";
        Evaluator result4 = QueryParser.parse(query4);
        assertNotNull(result4);
        
        // Test case #5
        String query5 = "div + .class";
        Evaluator result5 = QueryParser.parse(query5);
        assertNotNull(result5);
        
        // Test case #6
        String query6 = "div ~ .class";
        Evaluator result6 = QueryParser.parse(query6);
        assertNotNull(result6);
        
        // Test case #7
        String query7 = "div, .class";
        Evaluator result7 = QueryParser.parse(query7);
        assertNotNull(result7);
        
        // Test case #8
        String query8 = "div[attr=value]";
        Evaluator result8 = QueryParser.parse(query8);
        assertNotNull(result8);
        
        // Test case #9
        String query9 = "div:lt(5)";
        Evaluator result9 = QueryParser.parse(query9);
        assertNotNull(result9);
        
        // Test case #10
        String query10 = "div:gt(5)";
        Evaluator result10 = QueryParser.parse(query10);
        assertNotNull(result10);
        
        // Test case #11
        String query11 = "div:eq(5)";
        Evaluator result11 = QueryParser.parse(query11);
        assertNotNull(result11);
        
        // Test case #12
        String query12 = "div:has(span)";
        Evaluator result12 = QueryParser.parse(query12);
        assertNotNull(result12);
        
        // Test case #13
        String query13 = "div:contains(text)";
        Evaluator result13 = QueryParser.parse(query13);
        assertNotNull(result13);
        
        // Test case #14
        String query14 = "div:containsOwn(text)";
        Evaluator result14 = QueryParser.parse(query14);
        assertNotNull(result14);
        
        // Test case #15
        String query15 = "div:containsData(text)";
        Evaluator result15 = QueryParser.parse(query15);
        assertNotNull(result15);
        
        // Test case #16
        String query16 = "div:matches(regex)";
        Evaluator result16 = QueryParser.parse(query16);
        assertNotNull(result16);
        
        // Test case #17
        String query17 = "div:matchesOwn(regex)";
        Evaluator result17 = QueryParser.parse(query17);
        assertNotNull(result17);
        
        // Test case #18
        String query18 = "div:not(span)";
        Evaluator result18 = QueryParser.parse(query18);
        assertNotNull(result18);
        
    }
}

