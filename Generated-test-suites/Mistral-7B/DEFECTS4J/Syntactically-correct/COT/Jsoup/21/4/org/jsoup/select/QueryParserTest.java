import org.jsoup.helper.StringUtil;
import org.jsoup.parser.TokenQueue;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.jsoup.select.QueryParser.combinators;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div > p:first-child";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        // Basic functionality test
        Assert.assertNotNull(evaluator);

        // Edge cases and exception handling tests go here
    }

    @Test
    public void testCombinator_GreaterThan() {
        String query = "div > p:first-child";
        QueryParser parser = new QueryParser(query);
        Evaluator rootEvaluator = parser.parse();
        Evaluator childEvaluator = new StructuralEvaluator.ImmediateChild(rootEvaluator);
        Evaluator firstChildEvaluator = new StructuralEvaluator.FirstChild(childEvaluator);

        Evaluator queryEvaluator = parser.parse(query);

        Assert.assertEquals(queryEvaluator, firstChildEvaluator);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testCombinator_Unknown() {
        String query = "div ~ unknown:child";
        new QueryParser(query).parse();
    }

    @Test
    public void testFindElements_ById() {
        String query = "#id";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        Assert.assertEquals(evaluator.getClass(), Evaluator.Id.class);
        Assert.assertEquals(evaluator.toString(), "#id");
    }

    @Test
    public void testFindElements_ByClass() {
        String query = ".class";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        Assert.assertEquals(evaluator.getClass(), Evaluator.Class.class);
        Assert.assertEquals(evaluator.toString(), ".class");
    }

    @Test
    public void testFindElements_ByTag() {
        String query = "tag";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        Assert.assertEquals(evaluator.getClass(), Evaluator.Tag.class);
        Assert.assertEquals(evaluator.toString(), "tag");
    }

    @Test
    public void testFindElements_ByAttribute() {
        String query = "[attr=value]";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        Assert.assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
        Assert.assertEquals(evaluator.toString(), "[attr=value]");
    }

    @Test
    public void testFindElements_AllElements() {
        String query = "*";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        Assert.assertEquals(evaluator.getClass(), Evaluator.AllElements.class);
        Assert.assertEquals(evaluator.toString(), "*");
    }

    // Add more test cases here for other methods

    @Test
    public void testParse_EmptyQuery() {
        QueryParser parser = new QueryParser("");
        Assert.assertNull(parser.parse());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_InvalidQuery() {
        String query = "invalid:query";
        new QueryParser(query).parse();
    }
}