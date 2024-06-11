import org.jsoup.parser.QueryParser;
import org.jsoup.parser.TokenQueue;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class QueryParserTest {

    @Test
    public void testParse_EmptyQuery() {
        QueryParser parser = new QueryParser("");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_SingleElement() {
        QueryParser parser = new QueryParser("div");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("div"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_ClassSelector() {
        QueryParser parser = new QueryParser(".class");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Class("class"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_IdSelector() {
        QueryParser parser = new QueryParser("#id");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Id("id"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_AttributeSelector() {
        QueryParser parser = new QueryParser("[attr]");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Attribute("attr"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_AttributeWithValueSelector() {
        QueryParser parser = new QueryParser("[attr=value]");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.AttributeWithValue("attr", "value"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_CombinedSelectors() {
        QueryParser parser = new QueryParser("div.class");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("div"));
        expected.add(new Evaluator.Class("class"));
        Assert.assertEquals(expected, evaluator.getChildren());

        parser = new QueryParser(".class#id");
        evaluator = parser.parse();
        expected = new ArrayList<>();
        expected.add(new Evaluator.Class("class"));
        expected.add(new Evaluator.Id("id"));
        Assert.assertEquals(expected, evaluator.getChildren());

        parser = new QueryParser("div#id.class");
        evaluator = parser.parse();
        expected = new ArrayList<>();
        expected.add(new Evaluator.Id("id"));
        expected.add(new Evaluator.Class("class"));
        expected.add(new Evaluator.Tag("div"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_InvalidSelector() {
        QueryParser parser = new QueryParser("#id[attr");
        parser.parse();
    }

    @Test
    public void testParse_TagNameWithNamespace() {
        QueryParser parser = new QueryParser("html:body");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("html"));
        expected.add(new Evaluator.Tag("body"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_InvalidCombinedSelector() {
        QueryParser parser = new QueryParser("div#id>");
    }

    @Test
    public void testParse_DescendantSelector() {
        QueryParser parser = new QueryParser("div p");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("div"));
        expected.add(new CombiningEvaluator.Descendant(new Evaluator.Tag("p")));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_ChildSelector() {
        QueryParser parser = new QueryParser("div > p");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("div"));
        expected.add(new CombiningEvaluator.Child(new Evaluator.Tag("p")));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_AdjacentSiblingSelector() {
        QueryParser parser = new QueryParser("div + p");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("div"));
        expected.add(new StructuralEvaluator.AdjacentSibling(new Evaluator.Tag("p")));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_GeneralSiblingSelector() {
        QueryParser parser = new QueryParser("div ~ p");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("div"));
        expected.add(new StructuralEvaluator.GeneralSibling(new Evaluator.Tag("p")));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_AttributeSelectorWithValueStarting() {
        QueryParser parser = new QueryParser("[attr^='value']");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.AttributeWithValueStarting("attr", "value"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_AttributeSelectorWithValueEnding() {
        QueryParser parser = new QueryParser("[attr$='value']");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.AttributeWithValueEnding("attr", "value"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_AttributeSelectorWithValueContaining() {
        QueryParser parser = new QueryParser("[attr*='value']");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.AttributeWithValueContaining("attr", "value"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_AttributeSelectorWithValueMatching() {
        QueryParser parser = new QueryParser("[attr~='value']");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.AttributeWithValueMatching("attr", Pattern.compile("value")));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_NegatedAttributeSelector() {
        QueryParser parser = new QueryParser("[!attr]");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Not(new Evaluator.Attribute("attr")));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_NegatedElementSelector() {
        QueryParser parser = new QueryParser(":not(div)");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new StructuralEvaluator.Not(new Evaluator.Tag("div")));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_PseudoClassSelector_EmptyString() {
        QueryParser parser = new QueryParser(":empty");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.IsEmpty());
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_PseudoClassSelector_FirstChild() {
        QueryParser parser = new QueryParser(":first-child");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.IsFirstChild());
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_PseudoClassSelector_LastChild() {
        QueryParser parser = new QueryParser(":last-child");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.IsLastChild());
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_PseudoClassSelector_FirstOfType() {
        QueryParser parser = new QueryParser(":first-of-type");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.IsFirstOfType());
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_PseudoClassSelector_LastOfType() {
        QueryParser parser = new QueryParser(":last-of-type");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.IsLastOfType());
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_PseudoClassSelector_OnlyChild() {
        QueryParser parser = new QueryParser(":only-child");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.IsOnlyChild());
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_PseudoClassSelector_OnlyOfType() {
        QueryParser parser = new QueryParser(":only-of-type");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.IsOnlyOfType());
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_PseudoClassSelector_NthChild() {
        QueryParser parser = new QueryParser(":nth-child(2)");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.IsNthChild(2, 0));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_PseudoClassSelector_NthLastChild() {
        QueryParser parser = new QueryParser(":nth-last-child(2)");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.IsNthLastChild(2, 0));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_PseudoClassSelector_NthOfType() {
        QueryParser parser = new QueryParser(":nth-of-type(2)");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.IsNthOfType(2, 0));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_PseudoClassSelector_NthLastOfType() {
        QueryParser parser = new QueryParser(":nth-last-of-type(2)");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.IsNthLastOfType(2, 0));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_PseudoClassSelector_Has() {
        QueryParser parser = new QueryParser(":has(p)");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new StructuralEvaluator.Has(new Evaluator.Tag("p")));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_PseudoClassSelector_Contains() {
        QueryParser parser = new QueryParser(":contains(text)");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.ContainsText("text"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_PseudoClassSelector_ContainsOwn() {
        QueryParser parser = new QueryParser(":containsOwn(text)");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.ContainsOwnText("text"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_PseudoClassSelector_ContainsData() {
        QueryParser parser = new QueryParser(":containsData(data)");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.ContainsData("data"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_PseudoClassSelector_Matches() {
        QueryParser parser = new QueryParser(":matches(regex)");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Matches(Pattern.compile("regex")));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_PseudoClassSelector_MatchesOwn() {
        QueryParser parser = new QueryParser(":matchesOwn(regex)");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.MatchesOwn(Pattern.compile("regex")));
        Assert.assertEquals(expected, evaluator.getChildren());
    }
}