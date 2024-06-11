import org.jsoup.select.*;
import org.junit.Test;
import java.util.regex.Pattern;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse("div#myId.myClass[attr=value]:containsOwn(text)");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_emptyQuery() {
        Evaluator evaluator = QueryParser.parse("");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_combinator() {
        Evaluator evaluator = QueryParser.parse("div > p + span ~ a");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_allElements() {
        Evaluator evaluator = QueryParser.parse("*");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_id() {
        Evaluator evaluator = QueryParser.parse("#myId");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_class() {
        Evaluator evaluator = QueryParser.parse(".myClass");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_tag() {
        Evaluator evaluator = QueryParser.parse("div");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_attribute() {
        Evaluator evaluator = QueryParser.parse("[attr=value]");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_indexLessThan() {
        Evaluator evaluator = QueryParser.parse(":lt(5)");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_indexGreaterThan() {
        Evaluator evaluator = QueryParser.parse(":gt(5)");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_indexEquals() {
        Evaluator evaluator = QueryParser.parse(":eq(5)");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_has() {
        Evaluator evaluator = QueryParser.parse(":has(div)");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_contains() {
        Evaluator evaluator = QueryParser.parse(":contains(text)");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_containsOwn() {
        Evaluator evaluator = QueryParser.parse(":containsOwn(text)");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_containsData() {
        Evaluator evaluator = QueryParser.parse(":containsData(data)");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_matches() {
        Evaluator evaluator = QueryParser.parse(":matches(regex)");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_matchesOwn() {
        Evaluator evaluator = QueryParser.parse(":matchesOwn(regex)");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_not() {
        Evaluator evaluator = QueryParser.parse(":not(selector)");
        assertNotNull(evaluator);
    }

    // Add more test cases for comprehensive testing

}