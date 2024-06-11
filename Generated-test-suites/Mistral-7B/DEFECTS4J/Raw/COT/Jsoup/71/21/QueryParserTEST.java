import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.QueryParser;
import org.jsoup.parser.TokenQueue;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.select.Evaluator;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.isA;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        String query = "";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertNull(evaluator);
    }

    @Test
    public void testParseSimpleTagSelector() {
        String query = "p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(Evaluator.Tag.class));
        Assert.assertEquals("p", evaluator.toString());
    }

    @Test
    public void testParseClassSelector() {
        String query = ".myClass";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(Evaluator.Class.class));
        Assert.assertEquals(".myClass", evaluator.toString());
    }

    @Test
    public void testParseIdSelector() {
        String query = "#myId";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(Evaluator.Id.class));
        Assert.assertEquals("#myId", evaluator.toString());
    }

    @Test (expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        String query = "invalidQuery";
        new QueryParser(query).parse();
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "[title]";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(Evaluator.Attribute.class));
        Assert.assertEquals("[title]", evaluator.toString());
    }

    @Test
    public void testParseAttributeWithValueSelector() {
        String query = "[title=test]";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(Evaluator.AttributeWithValue.class));
        Assert.assertEquals("[title=test]", evaluator.toString());
    }

    @Test
    public void testParseAttributeWithValueNotSelector() {
        String query = "[title!='test']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(Evaluator.AttributeWithValueNot.class));
        Assert.assertEquals("[title!='test']", evaluator.toString());
    }

    @Test
    public void testParseAttributeStartingSelector() {
        String query = "[title^='test']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(Evaluator.AttributeStarting.class));
        Assert.assertEquals("[title^='test']", evaluator.toString());
    }

    @Test
    public void testParseAttributeWithValueEndingSelector() {
        String query = "[title$='test']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(Evaluator.AttributeWithValueEnding.class));
        Assert.assertEquals("[title$='test']", evaluator.toString());
    }

    @Test
    public void testParseAttributeWithValueContainingSelector() {
        String query = "[title*='test']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(Evaluator.AttributeWithValueContaining.class));
        Assert.assertEquals("[title*='test']", evaluator.toString());
    }

    @Test
    public void testParseAttributeWithValueMatchingRegexSelector() {
        String query = "[title~='test']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(Evaluator.AttributeWithValueMatching.class));
        Assert.assertEquals("[title~='test']", evaluator.toString());
    }

    @Test
    public void testParsePseudoClassSelector() {
        String query = ":empty";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(Evaluator.IsEmpty.class));
        Assert.assertEquals(":empty", evaluator.toString());
    }

    @Test
    public void testParseRootSelector() {
        String query = ":root";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(Evaluator.IsRoot.class));
        Assert.assertEquals(":root", evaluator.toString());
    }

    @Test
    public void testParseOnlyChildSelector() {
        String query = ":only-child";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(Evaluator.IsOnlyChild.class));
        Assert.assertEquals(":only-child", evaluator.toString());
    }

    @Test
    public void testParseOnlyOfTypeSelector() {
        String query = ":only-of-type";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(Evaluator.IsOnlyOfType.class));
        Assert.assertEquals(":only-of-type", evaluator.toString());
    }

    @Test
    public void testParseFirstChildSelector() {
        String query = ":first-child";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(Evaluator.IsFirstChild.class));
        Assert.assertEquals(":first-child", evaluator.toString());
    }

    @Test
    public void testParseLastChildSelector() {
        String query = ":last-child";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(Evaluator.IsLastChild.class));
        Assert.assertEquals(":last-child", evaluator.toString());
    }

    @Test
    public void testParseFirstOfTypeSelector() {
        String query = ":first-of-type";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(Evaluator.IsFirstOfType.class));
        Assert.assertEquals(":first-of-type", evaluator.toString());
    }

    @Test
    public void testParseLastOfTypeSelector() {
        String query = ":last-of-type";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(Evaluator.IsLastOfType.class));
        Assert.assertEquals(":last-of-type", evaluator.toString());
    }

    @Test
    public void testParseHasSelector() {
        String query = ":has(p)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(StructuralEvaluator.Has.class));
        Assert.assertThat(evaluator.getChildEvaluator(), instanceOf(Evaluator.Tag.class));
        Assert.assertEquals("p", evaluator.getChildEvaluator().toString());
    }

    @Test
    public void testParseNthChildSelector() {
        String query = ":nth-child(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(Evaluator.IsNthChild.class));
        Assert.assertEquals(":nth-child(2)", evaluator.toString());
    }

    @Test
    public void testParseNthLastChildSelector() {
        String query = ":nth-last-child(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(Evaluator.IsNthLastChild.class));
        Assert.assertEquals(":nth-last-child(2)", evaluator.toString());
    }

    @Test
    public void testParseNthOfTypeSelector() {
        String query = ":nth-of-type(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(Evaluator.IsNthOfType.class));
        Assert.assertEquals(":nth-of-type(2)", evaluator.toString());
    }

    @Test
    public void testParseNthLastOfTypeSelector() {
        String query = ":nth-last-of-type(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(Evaluator.IsNthLastOfType.class));
        Assert.assertEquals(":nth-last-of-type(2)", evaluator.toString());
    }

    @Test
    public void testParseNotSelector() {
        String query = ":not(p)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(StructuralEvaluator.Not.class));
        Assert.assertThat(evaluator.getChildEvaluator(), instanceOf(Evaluator.Tag.class));
        Assert.assertEquals("p", evaluator.getChildEvaluator().toString());
    }

    @Test
    public void testParseDescendantSelector() {
        String query = "div p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(Evaluator.Descendant.class));
        Assert.assertEquals("div > p", evaluator.toString());
    }

    @Test
    public void testParseChildSelector() {
        String query = "div > p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(Evaluator.Child.class));
        Assert.assertEquals("div > p", evaluator.toString());
    }

    @Test
    public void testParseAncestorSelector() {
        String query = "div > p > .class";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(Evaluator.Ancestor.class));
        Assert.assertEquals("div > p > .class", evaluator.toString());
    }

    @Test
    public void testParsePrefixSelector() {
        String query = "div > p > .prefix-class:first-child";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(Evaluator.Prefix.class));
        Assert.assertEquals("div > p > .prefix-class:first-child", evaluator.toString());
    }

    @Test
    public void testParseCombinatorSelector() {
        String query = "div > p + .class";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(Evaluator.Combinator.class));
        Assert.assertEquals("div > p + .class", evaluator.toString());
    }

    @Test
    public void testParseCombinatorWithAttributeSelector() {
        String query = "div > p + [title]";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(Evaluator.CombinatorWithAttribute.class));
        Assert.assertEquals("div > p + [title]", evaluator.toString());
    }

    @Test
    public void testParseComplexSelector() {
        String query = "div > p > .class:first-child + .prefix-class:first-child";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(Evaluator.CombinatorWithAttribute.class));
        Assert.assertEquals("div > p > .class:first-child + .prefix-class:first-child", evaluator.toString());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidCombinatorSelector() {
        String query = "div > p > ~ .class";
        new QueryParser(query).parse();
    }

    @Test
    public void testParseTagSelectorWithNamespace() {
        String query = "html:body";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(Evaluator.Tag.class));
        Assert.assertEquals("html|body", evaluator.toString());
    }

    @Test
    public void testParseClassSelectorWithNamespace() {
        String query = "html>.class";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, isA(Evaluator.Class.class));
        Assert.assertEquals("html > .class", evaluator.toString());
    }
}