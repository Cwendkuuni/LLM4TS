import org.junit.Test;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div#example";
        Evaluator evaluator = QueryParser.parse(query);
        // Assert the functionality of the parse method
    }

    @Test
    public void testCombinator() {
        QueryParser parser = new QueryParser("");
        parser.combinator(',');
        // Assert the functionality of the combinator method
    }

    @Test
    public void testParseSubQuery() {
        QueryParser parser = new QueryParser("");
        String subQuery = parser.consumeSubQuery();
        // Assert the functionality of the consumeSubQuery method
    }

    @Test
    public void testFindElements() {
        QueryParser parser = new QueryParser("");
        parser.findElements();
        // Assert the functionality of the findElements method
    }

    @Test
    public void testById() {
        QueryParser parser = new QueryParser("");
        parser.byId();
        // Assert the functionality of the byId method
    }

    @Test
    public void testByClass() {
        QueryParser parser = new QueryParser("");
        parser.byClass();
        // Assert the functionality of the byClass method
    }

    @Test
    public void testByTag() {
        QueryParser parser = new QueryParser("");
        parser.byTag();
        // Assert the functionality of the byTag method
    }

    @Test
    public void testByAttribute() {
        QueryParser parser = new QueryParser("");
        parser.byAttribute();
        // Assert the functionality of the byAttribute method
    }

    @Test
    public void testAllElements() {
        QueryParser parser = new QueryParser("");
        parser.allElements();
        // Assert the functionality of the allElements method
    }

    @Test
    public void testIndexLessThan() {
        QueryParser parser = new QueryParser("");
        parser.indexLessThan();
        // Assert the functionality of the indexLessThan method
    }

    @Test
    public void testIndexGreaterThan() {
        QueryParser parser = new QueryParser("");
        parser.indexGreaterThan();
        // Assert the functionality of the indexGreaterThan method
    }

    @Test
    public void testIndexEquals() {
        QueryParser parser = new QueryParser("");
        parser.indexEquals();
        // Assert the functionality of the indexEquals method
    }

    @Test
    public void testHas() {
        QueryParser parser = new QueryParser("");
        parser.has();
        // Assert the functionality of the has method
    }

    @Test
    public void testContains() {
        QueryParser parser = new QueryParser("");
        parser.contains(false);
        // Assert the functionality of the contains method
    }

    @Test
    public void testContainsOwn() {
        QueryParser parser = new QueryParser("");
        parser.contains(true);
        // Assert the functionality of the containsOwn method
    }

    @Test
    public void testMatches() {
        QueryParser parser = new QueryParser("");
        parser.matches(false);
        // Assert the functionality of the matches method
    }

    @Test
    public void testMatchesOwn() {
        QueryParser parser = new QueryParser("");
        parser.matches(true);
        // Assert the functionality of the matchesOwn method
    }

    @Test
    public void testNot() {
        QueryParser parser = new QueryParser("");
        parser.not();
        // Assert the functionality of the not method
    }
}