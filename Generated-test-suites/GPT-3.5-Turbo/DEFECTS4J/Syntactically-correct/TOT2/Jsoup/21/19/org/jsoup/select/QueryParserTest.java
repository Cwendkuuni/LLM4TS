import static org.junit.Assert.*;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        QueryParser parser = new QueryParser("p > .class");
        Evaluator evaluator = parser.parse();

        assertNotNull(evaluator);
        // Add more assertions as needed
    }

    @Test
    public void testParseWithCombinators() {
        QueryParser parser = new QueryParser("p + span");
        Evaluator evaluator = parser.parse();

        assertNotNull(evaluator);
        // Add more assertions as needed
    }

    @Test
    public void testParseWithMultipleCombinators() {
        QueryParser parser = new QueryParser("div #id > .class span");
        Evaluator evaluator = parser.parse();

        assertNotNull(evaluator);
        // Add more assertions as needed
    }

    @Test
    public void testParseWithIndex() {
        QueryParser parser = new QueryParser("p:eq(2)");
        Evaluator evaluator = parser.parse();

        assertNotNull(evaluator);
        // Add more assertions as needed
    }

    // Add more test cases for other methods in QueryParser class

}