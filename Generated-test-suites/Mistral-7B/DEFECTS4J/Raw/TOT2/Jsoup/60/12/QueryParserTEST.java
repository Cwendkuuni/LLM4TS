import org.jsoup.parser.QueryParser;
import org.jsoup.parser.QueryParser.Evaluator;
import org.jsoup.parser.Selector;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        String query = "";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
        Assert.assertTrue(evaluator.isEmpty());
    }

    @Test
    public void testParseTagQuery() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
        Assert.assertArrayEquals(expectedEvaluators.toArray(new Evaluator[0]), evaluator.toArray());
    }

    @Test
    public void testParseClassQuery() {
        String query = ".foo";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Class("foo"));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
        Assert.assertArrayEquals(expectedEvaluators.toArray(new Evaluator[0]), evaluator.toArray());
    }

    @Test
    public void testParseIdQuery() {
        String query = "#foo";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Id("foo"));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
        Assert.assertArrayEquals(expectedEvaluators.toArray(new Evaluator[0]), evaluator.toArray());
    }

    @Test
    public void testParseAttributeQuery() {
        String query = "[title=foo]";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Attribute("title", "foo"));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
        Assert.assertArrayEquals(expectedEvaluators.toArray(new Evaluator[0]), evaluator.toArray());
    }

    @Test
    public void testParseMultipleSelectorsQuery() {
        String query = "div.foo, .bar";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new Evaluator.Class("foo"));
        expectedEvaluators.add(new Evaluator.Tag("."));
        expectedEvaluators.add(new Evaluator.Class("bar"));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
        Assert.assertArrayEquals(expectedEvaluators.toArray(new Evaluator[0]), evaluator.toArray());
    }

    @Test
    public void testParseCombinedSelectorsQuery() {
        String query = "div > p, div + p";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new CombiningEvaluator.And(
                new Evaluator.Tag("div"),
                new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("p"))
        ));
        expectedEvaluators.add(new CombiningEvaluator.And(
                new Evaluator.Tag("div"),
                new StructuralEvaluator.PreviousSibling(new Evaluator.Tag("p"))
        ));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
        Assert.assertArrayEquals(expectedEvaluators.toArray(new Evaluator[0]), evaluator.toArray());
    }

    @Test
    public void testParsePseudoClassQuery() {
        String query = ":empty";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.IsEmpty());
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
        Assert.assertArrayEquals(expectedEvaluators.toArray(new Evaluator[0]), evaluator.toArray());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        String query = "div#invalid";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
        Assert.fail("Should throw Selector.SelectorParseException for query: " + query);
    }

    @Test
    public void testParseComplexQuery() {
        String query = "div#foo > p:first-child:has(> a[href^=\"https\"]):not(> a[href^=\"mailto\"]):containsOwn(java)";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Id("foo"));
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("p")));
        expectedEvaluators.add(new StructuralEvaluator.Has(
                new StructuralEvaluator.Descendant(new Evaluator.Tag("a"), 1),
                new Evaluator.AttributeWithValueStarting("href", "https")
        ));
        expectedEvaluators.add(new StructuralEvaluator.Not(
                new StructuralEvaluator.Descendant(new Evaluator.Tag("a"), 1),
                new Evaluator.AttributeWithValueStarting("href", "mailto")
        ));
        expectedEvaluators.add(new Evaluator.ContainsOwnText("java"));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
        Assert.assertArrayEquals(expectedEvaluators.toArray(new Evaluator[0]), evaluator.toArray());
    }
}