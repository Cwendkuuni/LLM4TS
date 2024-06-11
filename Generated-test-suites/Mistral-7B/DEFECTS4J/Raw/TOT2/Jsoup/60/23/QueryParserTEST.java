import org.jsoup.parser.QueryParser;
import org.jsoup.parser.TokenQueue;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class QueryParserTest {

    @Test
    public void testParse_EmptyQuery() {
        String query = "";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        Assert.assertEquals("Empty query should return an empty Evaluator", expectedEvaluators.toArray(), evaluator.toArray());
    }

    @Test
    public void testParse_SingleTagSelector() {
        String query = "div";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        Assert.assertEquals("Single tag selector should return a Tag Evaluator", expectedEvaluators.toArray(), evaluator.toArray());
    }

    @Test
    public void testParse_ClassSelector() {
        String query = ".class-name";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Class("class-name"));
        Assert.assertEquals("Class selector should return a Class Evaluator", expectedEvaluators.toArray(), evaluator.toArray());
    }

    @Test
    public void testParse_IDSelector() {
        String query = "#id";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Id("id"));
        Assert.assertEquals("ID selector should return an Id Evaluator", expectedEvaluators.toArray(), evaluator.toArray());
    }

    @Test
    public void testParse_Combinator_Descendant() {
        String query = "div p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new CombiningEvaluator.Descendant(new Evaluator.Tag("p")));
        Assert.assertEquals("Descendant combinator should return a Descendant Evaluator", expectedEvaluators.toArray(), evaluator.toArray());
    }

    @Test
    public void testParse_Combinator_Child() {
        String query = "div > p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("p")));
        Assert.assertEquals("Child combinator should return an ImmediateChild Evaluator", expectedEvaluators.toArray(), evaluator.toArray());
    }

    @Test
    public void testParse_Combinator_PreviousSibling() {
        String query = "p ~ p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("p"));
        expectedEvaluators.add(new StructuralEvaluator.PreviousSibling(new Evaluator.Tag("p")));
        Assert.assertEquals("PreviousSibling combinator should return a PreviousSibling Evaluator", expectedEvaluators.toArray(), evaluator.toArray());
    }

    @Test
    public void testParse_AttributeSelector_Equals() {
        String query = "div[class='class-name']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new AttributeEvaluator.Equals("class", "class-name"));
        Assert.assertEquals("AttributeSelector equals should return an Equals Attribute Evaluator", expectedEvaluators.toArray(), evaluator.toArray());
    }

    @Test
    public void testParse_AttributeSelector_NotEquals() {
        String query = "div[class!='class-name']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new AttributeEvaluator.NotEquals("class", "class-name"));
        Assert.assertEquals("AttributeSelector not equals should return a NotEquals Attribute Evaluator", expectedEvaluators.toArray(), evaluator.toArray());
    }

    @Test
    public void testParse_AttributeSelector_StartsWith() {
        String query = "div[class^='class-prefix']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new AttributeEvaluator.StartsWith("class", "class-prefix"));
        Assert.assertEquals("AttributeSelector starts with should return a StartsWith Attribute Evaluator", expectedEvaluators.toArray(), evaluator.toArray());
    }

    @Test
    public void testParse_AttributeSelector_EndsWith() {
        String query = "div[class$='class-suffix']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new AttributeEvaluator.EndsWith("class", "class-suffix"));
        Assert.assertEquals("AttributeSelector ends with should return an EndsWith Attribute Evaluator", expectedEvaluators.toArray(), evaluator.toArray());
    }

    @Test
    public void testParse_AttributeSelector_Contains() {
        String query = "div[class*='class-substring']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new AttributeEvaluator.Contains("class", "class-substring"));
        Assert.assertEquals("AttributeSelector contains should return a Contains Attribute Evaluator", expectedEvaluators.toArray(), evaluator.toArray());
    }

    @Test
    public void testParse_AttributeSelector_MatchesRegex() {
        String query = "div[class~='class-regex']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new AttributeEvaluator.MatchesRegex("class", Pattern.compile("class-regex")));
        Assert.assertEquals("AttributeSelector matches regex should return a MatchesRegex Attribute Evaluator", expectedEvaluators.toArray(), evaluator.toArray());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_InvalidQuery() {
        String query = "div[class='class-name' foo=bar]";
        QueryParser parser = new QueryParser(query);
        parser.parse();
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_InvalidCombinator() {
        String query = "div~>p";
        QueryParser parser = new QueryParser(query);
        parser.parse();
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_MissingCombinator() {
        String query = "div class-name p";
        QueryParser parser = new QueryParser(query);
        parser.parse();
    }
}