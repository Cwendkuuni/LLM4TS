import static org.junit.Assert.*;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse1() {
        String query = "div > p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse2() {
        String query = "div + p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse3() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse4() {
        String query = "div[class=test]";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse5() {
        String query = "div#id";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse6() {
        String query = "div:contains(test)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse7() {
        String query = "div:nth-child(2)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse8() {
        String query = "div:not(.class)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse9() {
        String query = "div *";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse10() {
        String query = "div:first-child";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse11() {
        String query = "div:matches(test)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse12() {
        String query = "div:has(p)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse13() {
        String query = "div ~ p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse14() {
        String query = "div:empty";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

}