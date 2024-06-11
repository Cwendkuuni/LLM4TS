import org.junit.Test;
import org.junit.Assert;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.jsoup.helper.Validate;

public class QueryParserTest {

    @Test
    public void testParse_whenQueryIsEmpty_throwException() {
        String query = "";
        try {
            Evaluator evaluator = QueryParser.parse(query);
        } catch (Selector.SelectorParseException e) {
            Assert.assertEquals("Could not parse query '': unexpected token at ''", e.getMessage());
        }
    }

    @Test
    public void testParse_whenQueryContainsUnknownCombinator_throwException() {
        String query = "div > p @ span";
        try {
            Evaluator evaluator = QueryParser.parse(query);
        } catch (Selector.SelectorParseException e) {
            Assert.assertEquals("Unknown combinator: @", e.getMessage());
        }
    }

    @Test
    public void testParse_whenQueryContainsValidSelector_parseSuccessfully() {
        String query = "div > p span";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertTrue(evaluator instanceof CombiningEvaluator.And);
        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) evaluator;
        Assert.assertEquals(3, andEvaluator.evals.size());
        Assert.assertTrue(andEvaluator.evals.get(0) instanceof Evaluator.Tag);
        Assert.assertTrue(andEvaluator.evals.get(1) instanceof StructuralEvaluator.ImmediateParent);
        Assert.assertTrue(andEvaluator.evals.get(2) instanceof Evaluator.Tag);
    }

    @Test
    public void testParse_whenQueryContainsValidSelectorWithAttribute_parseSuccessfully() {
        String query = "a[href^=https]";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertTrue(evaluator instanceof Evaluator.AttributeStarting);
        Evaluator.AttributeStarting attrEvaluator = (Evaluator.AttributeStarting) evaluator;
        Assert.assertEquals("href", attrEvaluator.key);
        Assert.assertEquals("https", attrEvaluator.value);
    }
}