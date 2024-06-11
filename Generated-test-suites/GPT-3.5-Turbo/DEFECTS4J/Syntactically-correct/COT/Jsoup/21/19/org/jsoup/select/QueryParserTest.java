
import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Pattern;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertTrue(evaluator instanceof Evaluator.Tag);
        Assert.assertEquals("div", ((Evaluator.Tag) evaluator).tagName());

        query = "#id";
        evaluator = QueryParser.parse(query);
        Assert.assertTrue(evaluator instanceof Evaluator.Id);
        Assert.assertEquals("id", ((Evaluator.Id) evaluator).id());

        query = ".class";
        evaluator = QueryParser.parse(query);
        Assert.assertTrue(evaluator instanceof Evaluator.Class);
        Assert.assertEquals("class", ((Evaluator.Class) evaluator).className());

        query = "[attr=value]";
        evaluator = QueryParser.parse(query);
        Assert.assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
        Assert.assertEquals("attr", ((Evaluator.AttributeWithValue) evaluator).key());
        Assert.assertEquals("value", ((Evaluator.AttributeWithValue) evaluator).value());

        query = ":contains(text)";
        evaluator = QueryParser.parse(query);
        Assert.assertTrue(evaluator instanceof Evaluator.ContainsText);
        Assert.assertEquals("text", ((Evaluator.ContainsText) evaluator).text());

        query = ":matches(regex)";
        evaluator = QueryParser.parse(query);
        Assert.assertTrue(evaluator instanceof Evaluator.Matches);
        Assert.assertEquals(Pattern.compile("regex"), ((Evaluator.Matches) evaluator).pattern());

        query = ":not(selector)";
        evaluator = QueryParser.parse(query);
        Assert.assertTrue(evaluator instanceof StructuralEvaluator.Not);
        Assert.assertTrue(((StructuralEvaluator.Not) evaluator).has(el -> true));
    }

    @Test
    public void testParseCombinator() {
        String query = "div > span";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertTrue(evaluator instanceof CombiningEvaluator.And);
        Assert.assertEquals(2, ((CombiningEvaluator.And) evaluator).numElMatchers());
        Assert.assertTrue(((CombiningEvaluator.And) evaluator).has(el -> true));
        Assert.assertTrue(((CombiningEvaluator.And) evaluator).has(el -> true));

        query = "div + span";
        evaluator = QueryParser.parse(query);
        Assert.assertTrue(evaluator instanceof CombiningEvaluator.And);
        Assert.assertEquals(2, ((CombiningEvaluator.And) evaluator).numElMatchers());
        Assert.assertTrue(((CombiningEvaluator.And) evaluator).has(el -> true));
        Assert.assertTrue(((CombiningEvaluator.And) evaluator).has(el -> true));

        query = "div ~ span";
        evaluator = QueryParser.parse(query);
        Assert.assertTrue(evaluator instanceof CombiningEvaluator.And);
        Assert.assertEquals(2, ((CombiningEvaluator.And) evaluator).numElMatchers());
        Assert.assertTrue(((CombiningEvaluator.And) evaluator).has(el -> true));
        Assert.assertTrue(((CombiningEvaluator.And) evaluator).has(el -> true));
    }

    @Test
    public void testParseMultipleSelect() {
        String query = "div, span";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertTrue(evaluator instanceof CombiningEvaluator.Or);
        Assert.assertEquals(2, ((CombiningEvaluator.Or) evaluator).numElMatchers());
        Assert.assertTrue(((CombiningEvaluator.Or) evaluator).has(el -> true));
        Assert.assertTrue(((CombiningEvaluator.Or) evaluator).has(el -> true));
    }

    @Test
    public void testParseWhitespace() {
        String query = "div span";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertTrue(evaluator instanceof CombiningEvaluator.And);
        Assert.assertEquals(2, ((CombiningEvaluator.And) evaluator).numElMatchers());
        Assert.assertTrue(((CombiningEvaluator.And) evaluator).has(el -> true));
        Assert.assertTrue(((CombiningEvaluator.And) evaluator).has(el -> true));
    }

    @Test
    public void testParseEmptyQuery() {
        String query = "";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertTrue(evaluator instanceof Evaluator.AllElements);
    }

    @Test
    public void testParseInvalidQuery() {
        String query = "div)";
        try {
            QueryParser.parse(query);
            Assert.fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            // expected
        }

        query = ":not()";
        try {
            QueryParser.parse(query);
            Assert.fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            // expected
        }
    }
}
