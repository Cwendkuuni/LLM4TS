import org.junit.Assert;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testCombinator() {
        String query = "div > span";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testAttribute() {
        String query = "div[class='name']";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testPseudoSelector() {
        String query = "div:contains('text')";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testNestedSelector() {
        String query = "div span";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testNotSelector() {
        String query = "div:not(.name)";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testAllElements() {
        String query = "*";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

}