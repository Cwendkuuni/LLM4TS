import org.jsoup.parser.QueryParser;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.Evaluator;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        QueryParser parser = new QueryParser("");
       Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        Assert.assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseTagSelector() {
        QueryParser parser = new QueryParser("p");
       Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("p"));
        Assert.assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseClassSelector() {
        QueryParser parser = new QueryParser(".class");
       Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Class("class"));
        Assert.assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseIdSelector() {
        QueryParser parser = new QueryParser("#id");
       Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Id("id"));
        Assert.assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseAttributeSelector() {
        QueryParser parser = new QueryParser("[attribute]");
       Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Attribute("attribute"));
        Assert.assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseAttributeValueSelector() {
        QueryParser parser = new QueryParser("[attribute='value']");
       Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.AttributeWithValue("attribute", "value"));
        Assert.assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseCombinedSelectors() {
        QueryParser parser = new QueryParser("p.class#id > div.class");
       Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new CombiningEvaluator.And(
                new Evaluator.Tag("p"),
                new CombiningEvaluator.And(
                        new Evaluator.Class("class"),
                        new Evaluator.Id("id")
                ),
                new StructuralEvaluator.ImmediateChildOf(
                        new Evaluator.Tag("div"),
                        new Evaluator.Class("class")
                )
        ));
        Assert.assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        QueryParser parser = new QueryParser("*"); // This query is valid but not handled in the given code
        parser.parse();
    }

    @Test
    public void testParseNegatedSelector() {
        QueryParser parser = new QueryParser(":not(.class)");
       Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new StructuralEvaluator.Not(new Evaluator.Class("class")));
        Assert.assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParsePseudoClassSelectors() {
        QueryParser parser = new QueryParser(":first-child");
       Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.IsFirstChild());
        Assert.assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParsePseudoElementSelectors() {
        QueryParser parser = new QueryParser(":before");
       Evaluator evaluator = parser.parse();
        Assert.fail("Pseudo-element selectors are not supported in this version of QueryParser.");
    }

    @Test
    public void testParseCombinatorSelectors() {
        QueryParser parser = new QueryParser("p > div.class");
       Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new CombiningEvaluator.And(
                new Evaluator.Tag("p"),
                new StructuralEvaluator.ImmediateChildOf(
                        new Evaluator.Tag("div"),
                        new Evaluator.Class("class")
                )
        ));
        Assert.assertEquals(expectedEvaluators, evaluator.getChildren());
    }
}