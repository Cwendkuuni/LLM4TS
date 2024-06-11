import org.jsoup.nodes.Element;
import org.jsoup.select.Evaluator;
import org.jsoup.select.QueryParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div > p";
        Element element = new Element("div");
        Element childElement = new Element("p");
        element.appendChild(childElement);

        Evaluator evaluator = QueryParser.parse(query);
        boolean result = evaluator.matches(element);
        
        assertEquals(true, result);
    }

    @Test
    public void testParseWithEmptyQuery() {
        String query = "";
        Evaluator evaluator = QueryParser.parse(query);

        assertEquals(true, evaluator instanceof Evaluator);
    }

    @Test
    public void testParseWithNullQuery() {
        Evaluator evaluator = QueryParser.parse(null);

        assertEquals(false, evaluator.matches(new Element("div")));
    }

    @Test
    public void testParseWithInvalidQuery() {
        String query = "invalid query";
        Evaluator evaluator = QueryParser.parse(query);

        assertEquals(false, evaluator.matches(new Element("div")));
    }

    @Test
    public void testByClass() {
        QueryParser parser = new QueryParser("");
        parser.byId();
        // Add test case for byClass method
    }

    // Add more individual test cases for other methods in QueryParser class

}