import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.select.Evaluator;
import org.jsoup.select.QueryParser;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testCombinator() {
        QueryParser parser = new QueryParser("");
        parser.combinator('>');
    }

    @Test
    public void testParseSubQuery() {
        QueryParser parser = new QueryParser("");
        parser.consumeSubQuery();
    }

    @Test
    public void testFindElements() {
        QueryParser parser = new QueryParser("");
        parser.findElements();
    }

    @Test
    public void testById() {
        QueryParser parser = new QueryParser("");
        parser.byId();
    }

    @Test
    public void testByClass() {
        QueryParser parser = new QueryParser("");
        parser.byClass();
    }

    @Test
    public void testByTag() {
        QueryParser parser = new QueryParser("");
        parser.byTag();
    }

    @Test
    public void testByAttribute() {
        QueryParser parser = new QueryParser("");
        parser.byAttribute();
    }

    @Test
    public void testAllElements() {
        QueryParser parser = new QueryParser("");
        parser.allElements();
    }

    @Test
    public void testIndexLessThan() {
        QueryParser parser = new QueryParser("");
        parser.indexLessThan();
    }

    @Test
    public void testIndexGreaterThan() {
        QueryParser parser = new QueryParser("");
        parser.indexGreaterThan();
    }

    @Test
    public void testIndexEquals() {
        QueryParser parser = new QueryParser("");
        parser.indexEquals();
    }

    @Test
    public void testHas() {
        QueryParser parser = new QueryParser("");
        parser.has();
    }

    @Test
    public void testContains() {
        QueryParser parser = new QueryParser("");
        parser.contains(false);
    }

    @Test
    public void testContainsOwn() {
        QueryParser parser = new QueryParser("");
        parser.contains(true);
    }

    @Test
    public void testMatches() {
        QueryParser parser = new QueryParser("");
        parser.matches(false);
    }

    @Test
    public void testMatchesOwn() {
        QueryParser parser = new QueryParser("");
        parser.matches(true);
    }

    @Test
    public void testNot() {
        QueryParser parser = new QueryParser("");
        parser.not();
    }

}