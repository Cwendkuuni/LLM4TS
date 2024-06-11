import org.jsoup.select.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div.class > p";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
    }

    @Test
    public void testIdSelector() {
        QueryParser qp = new QueryParser("#id");
        qp.byId();
        // Add assertion here
    }

    @Test
    public void testClassSelector() {
        QueryParser qp = new QueryParser(".class");
        qp.byClass();
        // Add assertion here
    }

    // Add more test cases for other public methods with edge cases and exceptions

}