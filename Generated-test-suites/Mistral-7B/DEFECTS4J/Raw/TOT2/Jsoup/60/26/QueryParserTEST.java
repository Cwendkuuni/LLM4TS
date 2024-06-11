import org.jsoup.parser.QueryParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        QueryParser parser = new QueryParser("");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseTagQuery() {
        QueryParser parser = new QueryParser("div");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("div"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseIdQuery() {
        QueryParser parser = new QueryParser("#id");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Id("id"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseClassQuery() {
        QueryParser parser = new QueryParser(".class");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Class("class"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseAttributeQuery() {
        QueryParser parser = new QueryParser("[attr=value]");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Attribute("attr", "value"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseMultipleSelectorsQuery() {
        QueryParser parser = new QueryParser("div.class#id > p");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new StructuralEvaluator.ImmediateParent(new Evaluator.Tag("div")));
        expected.add(new Evaluator.Class("class"));
        expected.add(new Evaluator.Tag("p"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        QueryParser parser = new QueryParser("div[invalid=selector]");
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidAttributeQuery() {
        QueryParser parser = new QueryParser("[invalid=selector]");
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidCombinatorQuery() {
        QueryParser parser = new QueryParser("div^p");
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseMissingCombinatorQuery() {
        QueryParser parser = new QueryParser("div.class id");
    }

    @Test
    public void testParseTagAndIdQuery() {
        QueryParser parser = new QueryParser("div#id");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Id("id"));
        expected.add(new Evaluator.Tag("div"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseTagAndClassQuery() {
        QueryParser parser = new QueryParser("div.class");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("div"));
        expected.add(new Evaluator.Class("class"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseTagAndAttributeQuery() {
        QueryParser parser = new QueryParser("div[attr=value]");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("div"));
        expected.add(new Evaluator.Attribute("attr", "value"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseMultipleTagsAndAttributesQuery() {
        QueryParser parser = new QueryParser("div.class#id.otherclass[attr=value2] > p.tag");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new StructuralEvaluator.ImmediateParent(new Evaluator.Tag("div")));
        expected.add(new Evaluator.Class("class"));
        expected.add(new Evaluator.Id("id"));
        expected.add(new Evaluator.Tag("p"));
        expected.add(new Evaluator.Tag("tag"));
        expected.add(new Evaluator.Attribute("attr", "value2"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseAttributeValueContainsSingleQuotes() {
        QueryParser parser = new QueryParser("[title='Example']");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.AttributeWithValue("title", "'Example'"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseAttributeValueContainsDoubleQuotes() {
        QueryParser parser = new QueryParser("[title=\"Example\"]");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.AttributeWithValue("title", "\"Example\""));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseAttributeValueWithoutQuotes() {
        QueryParser parser = new QueryParser("[title=Example]");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.AttributeWithValue("title", "Example"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseAttributeValueWithAsterisk() {
        QueryParser parser = new QueryParser("[title*='Example']");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.AttributeWithValueContaining("title", "Example"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseMultipleAttributeValuesWithAsterisk() {
        QueryParser parser = new QueryParser("[title*='Example' class*='class']");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.AttributeWithValueContaining("title", "Example"));
        expected.add(new Evaluator.AttributeWithValueContaining("class", "class"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseAttributeValueWithTilde() {
        QueryParser parser = new QueryParser("[title~='Example']");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.AttributeWithValueMatching("title", Pattern.compile(".*Example.*")));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseMultipleAttributeValuesWithTilde() {
        QueryParser parser = new QueryParser("[title~='Example' class~='class']");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.AttributeWithValueMatching("title", Pattern.compile(".*Example.*")));
        expected.add(new Evaluator.AttributeWithValueMatching("class", Pattern.compile(".*class.*")));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseMultipleSelectorsWithCombinator() {
        QueryParser parser = new QueryParser("div.class > p.tag");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new StructuralEvaluator.ImmediateParent(new Evaluator.Tag("div")));
        expected.add(new Evaluator.Class("class"));
        expected.add(new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("p")));
        expected.add(new Evaluator.Tag("tag"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseMultipleSelectorsWithDifferentCombinator() {
        QueryParser parser = new QueryParser("div.class > p ~ .tag");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new StructuralEvaluator.ImmediateParent(new Evaluator.Tag("div")));
        expected.add(new Evaluator.Class("class"));
        expected.add(new StructuralEvaluator.NextSibling(new Evaluator.Tag("p")));
        expected.add(new Evaluator.Tag("tag"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseMultipleSelectorsWithCombinatorAndAttribute() {
        QueryParser parser = new QueryParser("div.class.attribute > p.tag[attr=value]");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new StructuralEvaluator.ImmediateParent(new Evaluator.Tag("div")));
        expected.add(new Evaluator.Class("class"));
        expected.add(new StructuralEvaluator.ImmediateParent(new Evaluator.Tag("p")));
        expected.add(new Evaluator.Tag("tag"));
        expected.add(new Evaluator.Attribute("attr", "value"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseMultipleSelectorsWithMultipleCombinator() {
        QueryParser parser = new QueryParser("div.class > p > .tag");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new StructuralEvaluator.ImmediateParent(new Evaluator.Tag("div")));
        expected.add(new Evaluator.Class("class"));
        expected.add(new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("p")));
        expected.add(new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("tag")));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseMultipleSelectorsWithMultipleCombinatorAndAttribute() {
        QueryParser parser = new QueryParser("div.class > p > .tag[attr=value]");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new StructuralEvaluator.ImmediateParent(new Evaluator.Tag("div")));
        expected.add(new Evaluator.Class("class"));
        expected.add(new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("p")));
        expected.add(new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("tag")));
        expected.add(new Evaluator.Attribute("attr", "value"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseNthChildSelector() {
        QueryParser parser = new QueryParser("div:nth-child(2)");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.IsNthChild(2, 0));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseNthLastChildSelector() {
        QueryParser parser = new QueryParser("div:nth-last-child(2)");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.IsNthLastChild(2, 0));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseNthOfTypeSelector() {
        QueryParser parser = new QueryParser("div:nth-of-type(2)");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.IsNthOfType(2, 0));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseNthLastOfTypeSelector() {
        QueryParser parser = new QueryParser("div:nth-last-of-type(2)");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.IsNthLastOfType(2, 0));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseTagWithNamespace() {
        QueryParser parser = new QueryParser("xmlns:html div");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("div", "html"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseClassWithNamespace() {
        QueryParser parser = new QueryParser("xmlns:html .class");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Class("class", "html"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseIdWithNamespace() {
        QueryParser parser = new QueryParser("xmlns:html #id");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Id("id", "html"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseMultipleSelectorsWithNamespace() {
        QueryParser parser = new QueryParser("xmlns:html div.class#id.otherclass > p.tag[attr=value]");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new StructuralEvaluator.ImmediateParent(new Evaluator.Tag("div", "html")));
        expected.add(new Evaluator.Class("class", "html"));
        expected.add(new Evaluator.Id("id", "html"));
        expected.add(new StructuralEvaluator.ImmediateParent(new Evaluator.Tag("p", "html")));
        expected.add(new Evaluator.Tag("tag", "html"));
        expected.add(new Evaluator.Attribute("attr", "value", "html"));
        Assert.assertEquals(expected, evaluator.getChildren());
    }
}