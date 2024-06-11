import org.jsoup.parser.QueryParser;
import org.jsoup.select.Evaluator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class QueryParserTest {
    @Test
    public void testParseValidQuery() {
        String query = ".class1 #id1 > .class2";
        Evaluator evaluator = QueryParser.parse(query);
        // Add assertions here based on the structure of the Evaluator object
    }
}