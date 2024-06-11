import org.jsoup.parser.QueryParser;
import org.jsoup.parser.TokenQueue;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        QueryParser queryParser = new QueryParser("");
        Evaluator evaluator = queryParser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
        Assert.assertTrue(evaluator.isEmpty());
    }

    @Test
    public void testParseTagSelector() {
        String query = "p";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("p"));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
        Assert.assertTrue(evaluator.equals(expectedEvaluators.get(0)));
    }

    @Test
    public void testParseClassSelector() {
        String query = ".class1";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Class("class1"));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
        Assert.assertTrue(evaluator.equals(expectedEvaluators.get(0)));
    }

    @Test
    public void testParseIDSelector() {
        String query = "#id1";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Id("id1"));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
        Assert.assertTrue(evaluator.equals(expectedEvaluators.get(0)));
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "[title]";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Attribute("title"));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
        Assert.assertTrue(evaluator.equals(expectedEvaluators.get(0)));
    }

    @Test
    public void testParseAttributeWithValueSelector() {
        String query = "[title='Test']";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.AttributeWithValue("title", "Test"));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
        Assert.assertTrue(evaluator.equals(expectedEvaluators.get(0)));
    }

    @Test
    public void testParseAttributeWithValueStartingSelector() {
        String query = "[title^='Test']";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.AttributeWithValueStarting("title", "Test"));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
        Assert.assertTrue(evaluator.equals(expectedEvaluators.get(0)));
    }

    @Test
    public void testParseAttributeWithValueEndingSelector() {
        String query = "[title$='Test']";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.AttributeWithValueEnding("title", "Test"));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
        Assert.assertTrue(evaluator.equals(expectedEvaluators.get(0)));
    }

    @Test
    public void testParseAttributeWithValueContainingSelector() {
        String query = "[title*='Test']";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.AttributeWithValueContaining("title", "Test"));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
        Assert.assertTrue(evaluator.equals(expectedEvaluators.get(0)));
    }

    @Test
    public void testParseAttributeWithValueMatchingSelector() {
        String query = "[title~='Test']";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.AttributeWithValueMatching("title", Pattern.compile("Test")));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
        Assert.assertTrue(evaluator.equals(expectedEvaluators.get(0)));
    }

    @Test
    public void testParseCombinedSelectors() {
        String query = "p.class1";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("p"));
        expectedEvaluators.add(new Evaluator.Class("class1"));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
        Assert.assertTrue(evaluator.equals(ExpectedEvaluators.combine(expectedEvaluators)));
    }

    @Test
    public void testParseCombinedSelectorsWithDescendantSelector() {
        String query = "p > ul > li";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("p"));
        expectedEvaluators.add(new StructuralEvaluator.Descendant(new Evaluator.Tag("ul")));
        expectedEvaluators.add(new Evaluator.Tag("li"));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
        Assert.assertTrue(evaluator.equals(ExpectedEvaluators.combine(expectedEvaluators)));
    }

    @Test
    public void testParseCombinedSelectorsWithCombinatorError() {
        String query = "p > ul + li"; // '+' should be replaced with ' ' for testing
        QueryParser queryParser = new QueryParser(query);
        Assert.assertThrows(Selector.SelectorParseException.class, () -> queryParser.parse());
    }

    @Test
    public void testParseComplexSelectors() {
        String query = "div.class1 > ul > li:contains('Test'):first-child";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new Evaluator.Class("class1"));
        expectedEvaluators.add(new StructuralEvaluator.Descendant(new Evaluator.Tag("ul")));
        expectedEvaluators.add(new Evaluator.ContainsText("Test"));
        expectedEvaluators.add(new Evaluator.IsFirstChild());
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
        Assert.assertTrue(evaluator.equals(ExpectedEvaluators.combine(expectedEvaluators)));
    }

    @Test
    public void testParseErrorConditions() {
        String query = "p#id1#id2"; // should throw an error
        QueryParser queryParser = new QueryParser(query);
        Assert.assertThrows(Selector.SelectorParseException.class, () -> queryParser.parse());
    }

    @Test
    public void testParseNegativeSelectors() {
        String query = ":not(p)";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new StructuralEvaluator.Not(new Evaluator.Tag("p")));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
        Assert.assertTrue(evaluator.equals(expectedEvaluators.get(0)));
    }

    @Test
    public void testParsePseudoClassSelectors() {
        String query = "p:empty";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.IsEmpty());
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
        Assert.assertTrue(evaluator.equals(expectedEvaluators.get(0)));
    }

    @Test
    public void testParsePseudoClassSelectorsWithAttribute() {
        String query = "a:visited";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.PseudoClass("visited"));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
        Assert.assertTrue(evaluator.equals(expectedEvaluators.get(0)));
    }

    @Test
    public void testParseNthChildSelectors() {
        String query = "li:nth-child(2)";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.IsNthChild(1, 2));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
        Assert.assertTrue(evaluator.equals(expectedEvaluators.get(0)));
    }

    @Test
    public void testParseNthOfTypeSelectors() {
        String query = "li:nth-of-type(2)";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.IsNthOfType(1, 2));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
        Assert.assertTrue(evaluator.equals(expectedEvaluators.get(0)));
    }

    @Test
    public void testParseNthLastChildSelectors() {
        String query = "li:nth-last-child(2)";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.IsNthLastChild(1, 2));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
        Assert.assertTrue(evaluator.equals(expectedEvaluators.get(0)));
    }

    @Test
    public void testParseNthLastOfTypeSelectors() {
        String query = "li:nth-last-of-type(2)";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.IsNthLastOfType(1, 2));
        Assert.assertEquals(expectedEvaluators.size(), evaluator.size());
        Assert.assertTrue(evaluator.equals(expectedEvaluators.get(0)));
    }
}