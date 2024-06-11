import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        // Test case 1
        String query1 = "div#id.class[attr=value]";
        Evaluator evaluator1 = QueryParser.parse(query1);
        assertNotNull(evaluator1);

        // Test case 2
        String query2 = "h1";
        Evaluator evaluator2 = QueryParser.parse(query2);
        assertNotNull(evaluator2);

        // Test case 3
        String query3 = ":not(p)";
        Evaluator evaluator3 = QueryParser.parse(query3);
        assertNotNull(evaluator3);

        // Add more test cases here
    }

    @Test
    public void testCombinator() {
        // Test case 1
        QueryParser parser1 = new QueryParser("div > p");
        parser1.parse();
        // Assert result
        
        // Test case 2
        QueryParser parser2 = new QueryParser("div + p");
        parser2.parse();
        // Assert result

        // Test case 3
        QueryParser parser3 = new QueryParser("div ~ p");
        parser3.parse();
        // Assert result

        // Add more test cases here
    }

    // Add more test methods here
    
}