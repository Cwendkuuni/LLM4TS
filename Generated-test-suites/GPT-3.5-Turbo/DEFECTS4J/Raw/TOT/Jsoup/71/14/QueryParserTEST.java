import org.junit.Assert;
import org.junit.Test;
import org.jsoup.select.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertTrue(evaluator instanceof Evaluator.Tag);
        Assert.assertEquals("div", ((Evaluator.Tag) evaluator).tagName());

        query = "div#myId";
        evaluator = QueryParser.parse(query);
        Assert.assertTrue(evaluator instanceof Evaluator.And);
        Assert.assertTrue(((Evaluator.And) evaluator).evaluators().get(0) instanceof Evaluator.Tag);
        Assert.assertTrue(((Evaluator.And) evaluator).evaluators().get(1) instanceof Evaluator.Id);
        Assert.assertEquals("div", ((Evaluator.Tag) ((Evaluator.And) evaluator).evaluators().get(0)).tagName());
        Assert.assertEquals("myId", ((Evaluator.Id) ((Evaluator.And) evaluator).evaluators().get(1)).id());

        query = "div[class]";
        evaluator = QueryParser.parse(query);
        Assert.assertTrue(evaluator instanceof Evaluator.And);
        Assert.assertTrue(((Evaluator.And) evaluator).evaluators().get(0) instanceof Evaluator.Tag);
        Assert.assertTrue(((Evaluator.And) evaluator).evaluators().get(1) instanceof Evaluator.Attribute);
        Assert.assertEquals("div", ((Evaluator.Tag) ((Evaluator.And) evaluator).evaluators().get(0)).tagName());
        Assert.assertEquals("class", ((Evaluator.Attribute) ((Evaluator.And) evaluator).evaluators().get(1)).key());

        query = "div[class=value]";
        evaluator = QueryParser.parse(query);
        Assert.assertTrue(evaluator instanceof Evaluator.And);
        Assert.assertTrue(((Evaluator.And) evaluator).evaluators().get(0) instanceof Evaluator.Tag);
        Assert.assertTrue(((Evaluator.And) evaluator).evaluators().get(1) instanceof Evaluator.AttributeWithValue);
        Assert.assertEquals("div", ((Evaluator.Tag) ((Evaluator.And) evaluator).evaluators().get(0)).tagName());
        Assert.assertEquals("class", ((Evaluator.AttributeWithValue) ((Evaluator.And) evaluator).evaluators().get(1)).key());
        Assert.assertEquals("value", ((Evaluator.AttributeWithValue) ((Evaluator.And) evaluator).evaluators().get(1)).value());

        query = "div:not([class=value])";
        evaluator = QueryParser.parse(query);
        Assert.assertTrue(evaluator instanceof Evaluator.And);
        Assert.assertTrue(((Evaluator.And) evaluator).evaluators().get(0) instanceof Evaluator.Tag);
        Assert.assertTrue(((Evaluator.And) evaluator).evaluators().get(1) instanceof Evaluator.Not);
        Assert.assertTrue(((Evaluator.Not) ((Evaluator.And) evaluator).evaluators().get(1)).evaluator() instanceof Evaluator.AttributeWithValue);
        Assert.assertEquals("div", ((Evaluator.Tag) ((Evaluator.And) evaluator).evaluators().get(0)).tagName());
        Assert.assertEquals("class", ((Evaluator.AttributeWithValue) ((Evaluator.Not) ((Evaluator.And) evaluator).evaluators().get(1)).evaluator()).key());
        Assert.assertEquals("value", ((Evaluator.AttributeWithValue) ((Evaluator.Not) ((Evaluator.And) evaluator).evaluators().get(1)).evaluator()).value());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        String query = "]";
        QueryParser.parse(query);
    }

    // Add more test cases for each method

}