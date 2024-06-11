import org.jsoup.parser.QueryParser;
import org.jsoup.parser.Selector;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class QueryParserTest {

    @Test
    public void testParseSimpleSelector() {
        String query = "div.class1";
        Evaluator expected = new Evaluator.Class("class1");
        Evaluator actual = QueryParser.parse(query);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseComplexSelector() {
        String query = "div > ul > li:first-child";
        Evaluator expected = new CombiningEvaluator.And(
                new Evaluator.Tag("div"),
                new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("ul")),
                new Evaluator.IsFirstChild()
        );
        Evaluator actual = QueryParser.parse(query);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "input[type='text']";
        Evaluator expected = new Evaluator.Attribute(new Evaluator.AttributeName("type"), "text");
        Evaluator actual = QueryParser.parse(query);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseErrorSelector() {
        String query = "div.class1#id"; // should not be valid
        try {
            QueryParser.parse(query);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseComplexSelectorWithId() {
        String query = "div#id > ul > li:first-child";
        Evaluator expected = new CombiningEvaluator.And(
                new Evaluator.Id("id"),
                new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("ul")),
                new Evaluator.IsFirstChild()
        );
        Evaluator actual = QueryParser.parse(query);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseComplexSelectorWithClassAndId() {
        String query = "div.class1#id";
        Evaluator expected = new CombiningEvaluator.And(
                new Evaluator.Class("class1"),
                new Evaluator.Id("id")
        );
        Evaluator actual = QueryParser.parse(query);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseComplexSelectorWithCombinators() {
        String query = "div > ul > li ~ p";
        Evaluator expected = new CombiningEvaluator.And(
                new Evaluator.Tag("div"),
                new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("ul")),
                new Evaluator.Tag("li"),
                new StructuralEvaluator.PreviousSibling(new Evaluator.Tag("p"))
        );
        Evaluator actual = QueryParser.parse(query);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseComplexSelectorWithPseudoClasses() {
        String query = "li:last-child";
        Evaluator expected = new Evaluator.IsLastChild();
        Evaluator actual = QueryParser.parse(query);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseComplexSelectorWithNthChild() {
        String query = "li:nth-child(3)";
        Evaluator expected = new Evaluator.IsNthChild(1, 2); // 1 based indexing
        Evaluator actual = QueryParser.parse(query);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseComplexSelectorWithNthLastChild() {
        String query = "li:nth-last-child(3)";
        Evaluator expected = new Evaluator.IsNthLastChild(1, 2); // 1 based indexing
        Evaluator actual = QueryParser.parse(query);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseComplexSelectorWithNthOfType() {
        String query = "li:nth-of-type(3)";
        Evaluator expected = new Evaluator.IsNthOfType(1, 2); // 1 based indexing
        Evaluator actual = QueryParser.parse(query);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseComplexSelectorWithNthLastOfType() {
        String query = "li:nth-last-of-type(3)";
        Evaluator expected = new Evaluator.IsNthLastOfType(1, 2); // 1 based indexing
        Evaluator actual = QueryParser.parse(query);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseComplexSelectorWithPseudoElements() {
        String query = "input:checked";
        Evaluator expected = new Evaluator.IsChecked();
        Evaluator actual = QueryParser.parse(query);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseComplexSelectorWithDescendantSelector() {
        String query = "div > ul > li > a";
        Evaluator expected = new CombiningEvaluator.And(
                new Evaluator.Tag("a"),
                new StructuralEvaluator.DescendantOf(new Evaluator.Tag("li")), // li is not a direct child
                new StructuralEvaluator.DescendantOf(new Evaluator.Tag("ul")),
                new StructuralEvaluator.DescendantOf(new Evaluator.Tag("div"))
        );
        Evaluator actual = QueryParser.parse(query);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseComplexSelectorWithCombinatorError() {
        String query = "div > ul > li > p + li"; // + should not be a combinator forli's
        try {
            QueryParser.parse(query);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseComplexSelectorWithAttributeEquals() {
        String query = "input[type='text']";
        Evaluator expected = new Evaluator.Attribute(new Evaluator.AttributeName("type"), "text");
        Evaluator actual = QueryParser.parse(query);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseComplexSelectorWithAttributeNotEquals() {
        String query = "input:not([type='text'])";
        Evaluator expected = new StructuralEvaluator.Not(new Evaluator.Attribute(new Evaluator.AttributeName("type"), "text"));
        Evaluator actual = QueryParser.parse(query);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseComplexSelectorWithAttributeStartsWith() {
        String query = "input[data-test='test-']";
        Evaluator expected = new Evaluator.AttributeWithValueStarting(new Evaluator.AttributeName("data-test"), "test-");
        Evaluator actual = QueryParser.parse(query);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseComplexSelectorWithAttributeEndsWith() {
        String query = "input[data-test='test-end']";
        Evaluator expected = new Evaluator.AttributeWithValueEnding(new Evaluator.AttributeName("data-test"), "test-end");
        Evaluator actual = QueryParser.parse(query);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseComplexSelectorWithAttributeContaining() {
        String query = "input[data-test='test-contains']";
        Evaluator expected = new Evaluator.AttributeWithValueContaining(new Evaluator.AttributeName("data-test"), "test-contains");
        Evaluator actual = QueryParser.parse(query);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseComplexSelectorWithAttributeMatching() {
        String query = "input[data-test='(.*)test-match(.*)']";
        Evaluator expected = new Evaluator.AttributeWithValueMatching(new Evaluator.AttributeName("data-test"), Pattern.compile("(.*)test-match(.*)"));
        Evaluator actual = QueryParser.parse(query);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseComplexSelectorWithMultipleConditions() {
        String query = "input[data-test='test']:disabled";
        Evaluator expected = new CombiningEvaluator.And(
                new Evaluator.Attribute(new Evaluator.AttributeName("data-test"), "test"),
                new Evaluator.IsDisabled()
        );
        Evaluator actual = QueryParser.parse(query);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseComplexSelectorWithMultipleConditionsAnd() {
        String query = "input:disabled:checked";
        Evaluator expected = new CombiningEvaluator.And(
                new Evaluator.IsDisabled(),
                new Evaluator.IsChecked()
        );
        Evaluator actual = QueryParser.parse(query);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseComplexSelectorWithMultipleConditionsOr() {
        String query = "input:disabled:checked or input:enabled";
        Evaluator expected = new CombiningEvaluator.Or(
                new CombiningEvaluator.And(
                        new Evaluator.IsDisabled(),
                        new Evaluator.IsChecked()
                ),
                new Evaluator.IsEnabled()
        );
        Evaluator actual = QueryParser.parse(query);
        Assert.assertEquals(expected, actual);
    }
}