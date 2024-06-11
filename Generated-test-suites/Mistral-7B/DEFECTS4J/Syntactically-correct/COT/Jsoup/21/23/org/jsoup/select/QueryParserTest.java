import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.QueryParser;
import org.jsoup.helper.StringUtil;
import org.jsoup.parser.TokenQueue;
import org.jsoup.parser.Selector;
import org.jsoup.parser.StructuralEvaluator;
import org.jsoup.parser.CombiningEvaluator;
import org.jsoup.parser.Evaluator;
import org.jsoup.parser.Evaluator.Id;
import org.jsoup.parser.Evaluator.Class;
import org.jsoup.parser.Evaluator.Tag;
import org.jsoup.parser.Evaluator.Attribute;
import org.jsoup.parser.Evaluator.AttributeStarting;
import org.jsoup.parser.Evaluator.AttributeWithValue;
import org.jsoup.parser.Evaluator.AttributeWithValueContaining;
import org.jsoup.parser.Evaluator.AttributeWithValueEnding;
import org.jsoup.parser.Evaluator.AttributeWithValueMatching;
import org.jsoup.parser.Evaluator.AttributeWithValueStarting;
import org.jsoup.parser.Evaluator.AttributeWithValueNot;
import org.jsoup.parser.Evaluator.AllElements;
import org.jsoup.parser.Evaluator.IndexLessThan;
import org.jsoup.parser.Evaluator.IndexGreaterThan;
import org.jsoup.parser.Evaluator.IndexEquals;
import org.jsoup.parser.Evaluator.Matches;
import org.jsoup.parser.Evaluator.MatchesOwn;
import org.jsoup.parser.Evaluator.Has;
import org.jsoup.parser.Evaluator.Not;
import org.jsoup.parser.StructuralEvaluator.Root;
import org.jsoup.parser.StructuralEvaluator.ImmediateParent;
import org.jsoup.parser.StructuralEvaluator.Parent;
import org.jsoup.parser.StructuralEvaluator.PreviousSibling;
import org.jsoup.parser.StructuralEvaluator.ImmediatePreviousSibling;

public class QueryParserTest {

    @Test
    public void testParse_EmptyQuery() {
        String query = "";
        Evaluator parser = QueryParser.parse(query);
        Assert.assertEquals(new CombiningEvaluator.And(new Evaluator.AllElements()), parser);
    }

    @Test
    public void testParse_TagSelector() {
        String query = "div";
        Evaluator parser = QueryParser.parse(query);
        Assert.assertEquals(new Evaluator.Tag("div"), parser);
    }

    @Test
    public void testParse_ClassSelector() {
        String query = ".button";
        Evaluator parser = QueryParser.parse(query);
        Assert.assertEquals(new Evaluator.Class("button"), parser);
    }

    @Test
    public void testParse_IDSelector() {
        String query = "#id1";
        Evaluator parser = QueryParser.parse(query);
        Assert.assertEquals(new Evaluator.Id("id1"), parser);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_InvalidQuery() {
        String query = "invalidquery";
        QueryParser.parse(query);
    }

    @Test
    public void testParse_CombinedSelectors() {
        String query = "div > p";
        Evaluator parser = QueryParser.parse(query);
        StructuralEvaluator parent = new StructuralEvaluator.Parent(new Evaluator.Tag("div"));
        StructuralEvaluator child = new Evaluator.Tag("p");
        Assert.assertEquals(new CombiningEvaluator.And(parent, new StructuralEvaluator.ImmediateChild(child)), parser);
    }

    @Test
    public void testParse_DescendantSelectors() {
        String query = "div p";
        Evaluator parser = QueryParser.parse(query);
        Assert.assertEquals(new CombiningEvaluator.And(new Evaluator.Tag("div"), new StructuralEvaluator.Descendant(new Evaluator.Tag("p"))), parser);
    }

    @Test
    public void testParse_AttributeSelectors() {
        String query = "div[class='button']";
        Evaluator parser = QueryParser.parse(query);
        Assert.assertEquals(new Evaluator.Class("button").getAttributeValue("div"), parser);
    }

    @Test
    public void testParse_AttributeValues() {
        String query = "div[class='button']:contains('Click')";
        Evaluator parser = QueryParser.parse(query);
        Assert.assertEquals(new Evaluator.Class("button").getAttributeValueContaining("div", "Click"), parser);
    }

    @Test
    public void testParse_PseudoClassSelectors() {
        String query = "div:first-child";
        Evaluator parser = QueryParser.parse(query);
        Assert.assertEquals(new IndexEquals(0), parser);
    }

    @Test
    public void testParse_PseudoElementSelectors() {
        String query = "div:before";
        Evaluator parser = QueryParser.parse(query);
        Assert.assertEquals(new StructuralEvaluator.PseudoElement("before", new Evaluator.Tag("div")), parser);
    }

    @Test
    public void testParse_NegatedSelectors() {
        String query = "div:not(.button)";
        Evaluator parser = QueryParser.parse(query);
        Assert.assertEquals(new StructuralEvaluator.Not(new Evaluator.Class("button").getAttributeValue("div")), parser);
    }

    @Test
    public void testParse_ComplexSelectors() {
        String query = "div#header > ul.menu > li > a:contains('Home')";
        Evaluator parser = QueryParser.parse(query);
        StructuralEvaluator parent1 = new StructuralEvaluator.Parent(new Evaluator.Id("header"));
        StructuralEvaluator parent2 = new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("ul"));
        StructuralEvaluator parent3 = new StructuralEvaluator.ImmediateChild(new Evaluator.Class("menu"));
        StructuralEvaluator child = new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("li"));
        StructuralEvaluator grandChild = new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("a"));
        Assert.assertEquals(new CombiningEvaluator.And(parent1, parent2, parent3, child, new Evaluator.ContainsText("Home")), parser);
    }
}