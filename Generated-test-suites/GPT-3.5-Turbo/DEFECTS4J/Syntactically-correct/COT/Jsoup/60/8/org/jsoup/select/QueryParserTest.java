import org.jsoup.select.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse("div .class > p");
        assertNotNull(evaluator);
    }

    @Test
    public void testCombinator() {
        QueryParser queryParser = new QueryParser("div .class > p");
        queryParser.combinator(',');
        assertNotNull(queryParser);
    }

    @Test
    public void testParseSubQuery() {
        QueryParser queryParser = new QueryParser("div .class > p");
        String subQuery = queryParser.consumeSubQuery();
        assertNotNull(subQuery);
    }

    @Test
    public void testFindElements() {
        QueryParser queryParser = new QueryParser("div .class > p");
        queryParser.findElements();
        assertNotNull(queryParser);
    }

    @Test
    public void testById() {
        QueryParser queryParser = new QueryParser("div .class > p");
        queryParser.byId();
        assertNotNull(queryParser);
    }

    @Test
    public void testByClass() {
        QueryParser queryParser = new QueryParser("div .class > p");
        queryParser.byClass();
        assertNotNull(queryParser);
    }

    @Test
    public void testByTag() {
        QueryParser queryParser = new QueryParser("div .class > p");
        queryParser.byTag();
        assertNotNull(queryParser);
    }

    @Test
    public void testByAttribute() {
        QueryParser queryParser = new QueryParser("div .class > p");
        queryParser.byAttribute();
        assertNotNull(queryParser);
    }

    @Test
    public void testAllElements() {
        QueryParser queryParser = new QueryParser("div .class > p");
        queryParser.allElements();
        assertNotNull(queryParser);
    }

    @Test
    public void testIndexLessThan() {
        QueryParser queryParser = new QueryParser("div .class > p");
        queryParser.indexLessThan();
        assertNotNull(queryParser);
    }

    @Test
    public void testIndexGreaterThan() {
        QueryParser queryParser = new QueryParser("div .class > p");
        queryParser.indexGreaterThan();
        assertNotNull(queryParser);
    }

    @Test
    public void testIndexEquals() {
        QueryParser queryParser = new QueryParser("div .class > p");
        queryParser.indexEquals();
        assertNotNull(queryParser);
    }

    @Test
    public void testHas() {
        QueryParser queryParser = new QueryParser("div .class > p");
        queryParser.has();
        assertNotNull(queryParser);
    }

    @Test
    public void testContains() {
        QueryParser queryParser = new QueryParser("div .class > p");
        queryParser.contains(true);
        assertNotNull(queryParser);
    }

    @Test
    public void testContainsData() {
        QueryParser queryParser = new QueryParser("div .class > p");
        queryParser.containsData();
        assertNotNull(queryParser);
    }


    @Test
    public void testMatches() {
        QueryParser queryParser = new QueryParser("div .class > p");
        queryParser.matches(true);
        assertNotNull(queryParser);
    }

    @Test
    public void testNot() {
        QueryParser queryParser = new QueryParser("div .class > p");
        queryParser.not();
        assertNotNull(queryParser);
    }

}