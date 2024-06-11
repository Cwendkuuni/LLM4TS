import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        // Test case 1
        String query1 = "div.example";
        Evaluator evaluator1 = QueryParser.parse(query1);
        assertNotNull(evaluator1);

        // Test case 2
        String query2 = "#id";
        Evaluator evaluator2 = QueryParser.parse(query2);
        assertNotNull(evaluator2);

        // Test case 3
        String query3 = "a[href]";
        Evaluator evaluator3 = QueryParser.parse(query3);
        assertNotNull(evaluator3);

        // ... add more test cases for other methods in QueryParser class
    }

    @Test
    public void testCombinator() {
        // Test case 1
        QueryParser parser1 = new QueryParser("div.example");
        parser1.combinator('>');
        // assert the result of the combinator operation

        // Test case 2
        QueryParser parser2 = new QueryParser("div.example ~ p");
        parser2.combinator('~');
        // assert the result of the combinator operation

        // Test case 3
        QueryParser parser3 = new QueryParser("div.example + p");
        parser3.combinator('+');
        // assert the result of the combinator operation

        // ... add more test cases for the combinator method
    }

    // ... add more test methods for other methods in QueryParser class

}