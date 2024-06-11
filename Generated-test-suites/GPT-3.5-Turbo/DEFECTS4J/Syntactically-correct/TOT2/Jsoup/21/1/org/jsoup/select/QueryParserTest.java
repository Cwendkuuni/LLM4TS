import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div.class > p#id";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
    }

    @Test
    public void testParse_Complex() {
        String query = "div.class, p#id, span[attr=value]";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
    }

    @Test
    public void testParse_EmptyQuery() {
        String query = "";
        Evaluator result = QueryParser.parse(query);
        assertNull(result);
    }

    @Test
    public void testParse_InvalidQuery() {
        String query = "div.class #id";
        try {
            QueryParser.parse(query);
            fail("Exception expected");
        } catch (Exception e) {
            // Expected exception
        }
    }

    @Test
    public void testFindElements_ById() {
        QueryParser parser = new QueryParser("div#test");
        parser.parse();
    }

    @Test
    public void testFindElements_ByClass() {
        QueryParser parser = new QueryParser("div.class");
        parser.parse();
    }

    @Test
    public void testFindElements_ByTag() {
        QueryParser parser = new QueryParser("span");
        parser.parse();
    }

    @Test
    public void testFindElements_ByAttribute() {
        QueryParser parser = new QueryParser("a[href^=https]");
        parser.parse();
    }

    @Test
    public void testFindElements_AllElements() {
        QueryParser parser = new QueryParser("*");
        parser.parse();
    }

    @Test
    public void testConsumeIndex() {
        QueryParser parser = new QueryParser("div:eq(2)");
        int index = parser.consumeIndex();
        assertEquals(2, index);
    }

    @Test
    public void testMatches() {
        QueryParser parser = new QueryParser("span:matchesOwn(regex)");
        parser.matches(false);
        parser.matches(true);
    }

    @Test
    public void testNot() {
        QueryParser parser = new QueryParser(":not(div.class)");
        parser.not();
    }
}