import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        // Typical use case
        String query = "div > a";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);

        // Edge case
        query = "div a";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);

        // Error scenario
        query = "";
        try {
            evaluator = QueryParser.parse(query);
            fail("IllegalArgumentException not thrown for empty query");
        } catch (Selector.SelectorParseException e) {
            // Exception expected
        }
    }

    @Test
    public void testParseCSSNthChild() {
        // Typical use case
        String nthChild = ":nth-child(5n+2)";
        QueryParser queryParser = new QueryParser(nthChild);
        queryParser.cssNthChild(false, false);

        // Edge case
        nthChild = ":nth-child(2n)";
        queryParser = new QueryParser(nthChild);
        queryParser.cssNthChild(false, false);

        // Error scenario
        nthChild = ":nth-child(abc)";
        try {
            queryParser = new QueryParser(nthChild);
            queryParser.cssNthChild(false, false);
            fail("NumberFormatException not thrown for non-numeric nth-child query");
        } catch (NumberFormatException e) {
            // Exception expected
        }
    }

    @Test
    public void testIndexLessThan() {
        // Typical use case
        String indexLessThan = ":lt(5)";
        QueryParser queryParser = new QueryParser(indexLessThan);
        queryParser.indexLessThan();

        // Edge case
        indexLessThan = ":lt(0)";
        queryParser = new QueryParser(indexLessThan);
        queryParser.indexLessThan();

        // Error scenario
        indexLessThan = ":lt(abc)";
        try {
            queryParser = new QueryParser(indexLessThan);
            queryParser.indexLessThan();
            fail("NumberFormatException not thrown for non-numeric index in :lt query");
        } catch (NumberFormatException e) {
            // Exception expected
        }
    }

}