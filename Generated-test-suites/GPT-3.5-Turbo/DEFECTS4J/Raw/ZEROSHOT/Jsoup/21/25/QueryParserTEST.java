import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div.some-class > a[href=example.com]";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testCombinator() {
        QueryParser queryParser = new QueryParser("");
        queryParser.combinator(',');
        queryParser.combinator('>');
        queryParser.combinator('+');
        queryParser.combinator('~');
        boolean exceptionThrown = false;
        try {
            queryParser.combinator('$');
        } catch (Selector.SelectorParseException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testConsumeSubQuery() {
        QueryParser queryParser = new QueryParser("");
        queryParser.consumeSubQuery();
    }

    @Test
    public void testFindElements() {
        QueryParser queryParser = new QueryParser("");
        queryParser.findElements();
    }

    @Test
    public void testById() {
        QueryParser queryParser = new QueryParser("");
        queryParser.byId();
    }

    @Test
    public void testByClass() {
        QueryParser queryParser = new QueryParser("");
        queryParser.byClass();
    }

    @Test
    public void testByTag() {
        QueryParser queryParser = new QueryParser("");
        queryParser.byTag();
    }

    @Test
    public void testByAttribute() {
        QueryParser queryParser = new QueryParser("");
        queryParser.byAttribute();
    }

    @Test
    public void testAllElements() {
        QueryParser queryParser = new QueryParser("");
        queryParser.allElements();
    }

    @Test
    public void testIndexLessThan() {
        QueryParser queryParser = new QueryParser("");
        queryParser.indexLessThan();
    }

    @Test
    public void testIndexGreaterThan() {
        QueryParser queryParser = new QueryParser("");
        queryParser.indexGreaterThan();
    }

    @Test
    public void testIndexEquals() {
        QueryParser queryParser = new QueryParser("");
        queryParser.indexEquals();
    }

    @Test
    public void testHas() {
        QueryParser queryParser = new QueryParser("");
        queryParser.has();
    }

    @Test
    public void testContains() {
        QueryParser queryParser = new QueryParser("");
        queryParser.contains(false);
        queryParser.contains(true);
    }

    @Test
    public void testMatches() {
        QueryParser queryParser = new QueryParser("");
        queryParser.matches(false);
        queryParser.matches(true);
    }

    @Test
    public void testNot() {
        QueryParser queryParser = new QueryParser("");
        queryParser.not();
    }
}