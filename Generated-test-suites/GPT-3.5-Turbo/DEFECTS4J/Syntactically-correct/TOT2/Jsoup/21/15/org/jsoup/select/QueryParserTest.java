import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div p";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        // Add more assertions for valid query parsing
    }

    @Test
    public void testCombiningEvaluator() {
        // Test combinator method for different cases (>, +, ~, etc.)
    }

    @Test
    public void testByTag() {
        QueryParser qp = new QueryParser("p");
        qp.byTag();
        // Check if correct Evaluator.Tag object is added
    }

    @Test
    public void testByAttribute() {
        // Test byAttribute method for different attribute conditions
    }

    @Test
    public void testAllElements() {
        QueryParser qp = new QueryParser("*");
        qp.allElements();
        // Check if Evaluator.AllElements is added
    }

    @Test
    public void testIndexLessThan() {
        QueryParser qp = new QueryParser(":lt(5)");
        qp.indexLessThan();
        // Check if Evaluator.IndexLessThan is created with correct index
    }

    @Test
    public void testHas() {
        QueryParser qp = new QueryParser(":has(div)");
        qp.has();
        // Check if Evaluator.Has is created with correct sub query
    }

    @Test
    public void testContains() {
        QueryParser qp = new QueryParser(":contains(text)");
        qp.contains(false);
        // Check if Evaluator.ContainsText is created with correct text
    }

    @Test
    public void testMatches() {
        QueryParser qp = new QueryParser(":matches(regex)");
        qp.matches(false);
        // Check if Evaluator.Matches is created with correct regex
    }

    @Test
    public void testNot() {
        QueryParser qp = new QueryParser(":not(div)");
        qp.not();
        // Check if StructuralEvaluator.Not is created with correct sub query
    }
}