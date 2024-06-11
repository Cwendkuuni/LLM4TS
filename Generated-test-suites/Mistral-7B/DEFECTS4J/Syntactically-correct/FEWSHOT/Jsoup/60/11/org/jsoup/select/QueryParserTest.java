package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.jsoup.parser.TokenQueue;
import org.jsoup.helper.StringUtil;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        QueryParser parser = new QueryParser("");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.AllElements.class, eval.getClass());
    }

    @Test
    public void testParseTagQuery() {
        QueryParser parser = new QueryParser("div");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.Tag.class, eval.getClass());
        assertEquals("div", eval.toString());
    }

    @Test
    public void testParseClassQuery() {
        QueryParser parser = new QueryParser(".myClass");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.Class.class, eval.getClass());
        assertEquals(".myClass", eval.toString());
    }

    @Test
    public void testParseIdQuery() {
        QueryParser parser = new QueryParser("#myId");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.Id.class, eval.getClass());
        assertEquals("#myId", eval.toString());
    }

    @Test
    public void testParseAttributeQuery() {
        QueryParser parser = new QueryParser("[title]");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.Attribute.class, eval.getClass());
        assertEquals("[title]", eval.toString());
    }

    @Test
    public void testParseAttributeValueQuery() {
        QueryParser parser = new QueryParser("[title='Test']");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.AttributeWithValue.class, eval.getClass());
        assertEquals("[title='Test']", eval.toString());
    }

    @Test
    public void testParseAttributeStartingQuery() {
        QueryParser parser = new QueryParser("[title^='Test']");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.AttributeWithValueStarting.class, eval.getClass());
        assertEquals("[title^='Test']", eval.toString());
    }

    @Test
    public void testParseAttributeEndingQuery() {
        QueryParser parser = new QueryParser("[title$='Test']");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.AttributeWithValueEnding.class, eval.getClass());
        assertEquals("[title$='Test']", eval.toString());
    }

    @Test
    public void testParseAttributeContainingQuery() {
        QueryParser parser = new QueryParser("[title*='Test']");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.AttributeWithValueContaining.class, eval.getClass());
        assertEquals("[title*='Test']", eval.toString());
    }

    @Test
    public void testParseAttributeMatchingQuery() {
        QueryParser parser = new QueryParser("[title~='Test']");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.AttributeWithValueMatching.class, eval.getClass());
        assertEquals("[title~='Test']", eval.toString());
    }

    @Test
    public void testParseSubQuery() {
        QueryParser parser = new QueryParser("div[title~='Test']");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.CombiningEvaluator.And.class, eval.getClass());
        Evaluator tagEval = ((Evaluator.CombiningEvaluator.And) eval).getLeft();
        assertEquals(Evaluator.Tag.class, tagEval.getClass());
        assertEquals("div", tagEval.toString());
        Evaluator attrEval = ((Evaluator.CombiningEvaluator.And) eval).getRight();
        assertEquals(Evaluator.AttributeWithValueMatching.class, attrEval.getClass());
        assertEquals("[title~='Test']", attrEval.toString());
    }

    @Test
    public void testParseAttributeWithValueQuery() {
        QueryParser parser = new QueryParser("[title='Test']");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.AttributeWithValue.class, eval.getClass());
        assertEquals("[title='Test']", eval.toString());
    }

    @Test
    public void testParseAttributeWithValueNotQuery() {
        QueryParser parser = new QueryParser("[title!='Test']");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.AttributeWithValueNot.class, eval.getClass());
        assertEquals("[title!='Test']", eval.toString());
    }

    @Test
    public void testParseCombinatorQuery() { {
        QueryParser parser = new QueryParser("div > p");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.CombiningEvaluator.And.class, eval.getClass());
        Evaluator tag1Eval = ((Evaluator.CombiningEvaluator.And) eval).getLeft();
        assertEquals(Evaluator.Tag.class, tag1Eval.getClass());
        assertEquals("div", tag1Eval.toString());
        Evaluator combinatorEval = ((Evaluator.CombiningEvaluator.And) eval).getRight();
        assertEquals(Evaluator.StructuralEvaluator.ImmediateChild.class, combinatorEval.getClass());
        assertEquals("p", combinatorEval.toString());
    }
    }

    @Test
    public void testParseCombinatorPlusQuery() { {
        QueryParser parser = new QueryParser("div + p");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.CombiningEvaluator.And.class, eval.getClass());
        Evaluator tag1Eval = ((Evaluator.CombiningEvaluator.And) eval).getLeft();
        assertEquals(Evaluator.Tag.class, tag1Eval.getClass());
        assertEquals("div", tag1Eval.toString());
        Evaluator combinatorEval = ((Evaluator.CombiningEvaluator.And) eval).getRight();
        assertEquals(Evaluator.StructuralEvaluator.ImmediatePreviousSibling.class, combinatorEval.getClass());
        assertEquals("p", combinatorEval.toString());
    }
    }

    @Test
    public void testParseCombinatorTildeQuery() { {
        QueryParser parser = new QueryParser("div ~ p");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.CombiningEvaluator.And.class, eval.getClass());
        Evaluator tag1Eval = ((Evaluator.CombiningEvaluator.And) eval).getLeft();
        assertEquals(Evaluator.Tag.class, tag1Eval.getClass());
        assertEquals("div", tag1Eval.toString());
        Evaluator combinatorEval = ((Evaluator.CombiningEvaluator.And) eval).getRight();
        assertEquals(Evaluator.StructuralEvaluator.PreviousSibling.class, combinatorEval.getClass());
        assertEquals("p", combinatorEval.toString());
    }
    }

    @Test
    public void testParseCombinatorSpaceQuery() { {
        QueryParser parser = new QueryParser("div p");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.CombiningEvaluator.And.class, eval.getClass());
        Evaluator tag1Eval = ((Evaluator.CombiningEvaluator.And) eval).getLeft();
        assertEquals(Evaluator.Tag.class, tag1Eval.getClass());
        assertEquals("div", tag1Eval.toString());
        Evaluator tag2Eval = ((Evaluator.CombiningEvaluator.And) eval).getRight();
        assertEquals(Evaluator.Tag.class, tag2Eval.getClass());
        assertEquals("p", tag2Eval.toString());
    }
    }

    @Test
    public void testParseCombinatorCommaQuery() { {
        QueryParser parser = new QueryParser("div, p");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.CombiningEvaluator.Or.class, eval.getClass());
        Evaluator tag1Eval = ((Evaluator.CombiningEvaluator.Or) eval).getLeft();
        assertEquals(Evaluator.Tag.class, tag1Eval.getClass());
        assertEquals("div", tag1Eval.toString());
        Evaluator tag2Eval = ((Evaluator.CombiningEvaluator.Or) eval).getRight();
        assertEquals(Evaluator.Tag.class, tag2Eval.getClass());
        assertEquals("p", tag2Eval.toString());
    }
    }

    @Test
    public void testParsePseudoClassQuery() {
        QueryParser parser = new QueryParser(":first-child");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.IsFirstChild.class, eval.getClass());
        assertEquals(":first-child", eval.toString());
    }

    @Test
    public void testParsePseudoClassLastChildQuery() {
        QueryParser parser = new QueryParser(":last-child");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.IsLastChild.class, eval.getClass());
        assertEquals(":last-child", eval.toString());
    }

    @Test
    public void testParsePseudoClassFirstOfTypeQuery() {
        QueryParser parser = new QueryParser(":first-of-type");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.IsFirstOfType.class, eval.getClass());
        assertEquals(":first-of-type", eval.toString());
    }

    @Test
    public void testParsePseudoClassLastOfTypeQuery() {
        QueryParser parser = new QueryParser(":last-of-type");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.IsLastOfType.class, eval.getClass());
        assertEquals(":last-of-type", eval.toString());
    }

    @Test
    public void testParsePseudoClassOnlyChildQuery() {
        QueryParser parser = new QueryParser(":only-child");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.IsOnlyChild.class, eval.getClass());
        assertEquals(":only-child", eval.toString());
    }

    @Test
    public void testParsePseudoClassOnlyOfTypeQuery() {
        QueryParser parser = new QueryParser(":only-of-type");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.IsOnlyOfType.class, eval.getClass());
        assertEquals(":only-of-type", eval.toString());
    }

    @Test
    public void testParsePseudoClassEmptyQuery() {
        QueryParser parser = new QueryParser(":empty");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.IsEmpty.class, eval.getClass());
        assertEquals(":empty", eval.toString());
    }

    @Test
    public void testParsePseudoClassRootQuery() {
        QueryParser parser = new QueryParser(":root");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.IsRoot.class, eval.getClass());
        assertEquals(":root", eval.toString());
    }

    @Test
    public void testParseNthChildQuery() {
        QueryParser parser = new QueryParser(":nth-child(2)");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.IsNthChild.class, eval.getClass());
        assertEquals(":nth-child(2)", eval.toString());
    }

    @Test
    public void testParseNthChildLastQuery() {
        QueryParser parser = new QueryParser(":nth-last-child(2)");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.IsNthLastChild.class, eval.getClass());
        assertEquals(":nth-last-child(2)", eval.toString());
    }

    @Test
    public void testParseNthOfTypeQuery() {
        QueryParser parser = new QueryParser(":nth-of-type(2)");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.IsNthOfType.class, eval.getClass());
        assertEquals(":nth-of-type(2)", eval.toString());
    }

    @Test
    public void testParseNthOfTypeLastQuery() {
        QueryParser parser = new QueryParser(":nth-last-of-type(2)");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.IsNthLastOfType.class, eval.getClass());
        assertEquals(":nth-last-of-type(2)", eval.toString());
    }

    @Test
    public void testParseHasQuery() {
        QueryParser parser = new QueryParser(":has(p)");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.StructuralEvaluator.Has.class, eval.getClass());
        assertEquals(":has(p)", eval.toString());
    }

    @Test
    public void testParseContainsTextQuery() {
        QueryParser parser = new QueryParser(":contains(Test)");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.Evaluator.ContainsText.class, eval.getClass());
        assertEquals(":contains(Test)", eval.toString());
    }

    @Test
    public void testParseContainsOwnTextQuery() {
        QueryParser parser = new QueryParser(":containsOwn(Test)");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.Evaluator.ContainsOwnText.class, eval.getClass());
        assertEquals(":containsOwn(Test)", eval.toString());
    }

    @Test
    public void testParseContainsDataQuery() {
        QueryParser parser = new QueryParser(":containsData(Test)");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.Evaluator.ContainsData.class, eval.getClass());
        assertEquals(":containsData(Test)", eval.toString());
    }

    @Test
    public void testParseNotQuery() {
        QueryParser parser = new QueryParser(":not(p)");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.StructuralEvaluator.Not.class, eval.getClass());
        assertEquals(":not(p)", eval.toString());
    }

    @Test
    public void testParseComplexQuery() {
        QueryParser parser = new QueryParser("div:not(.error) > p.highlight");
        Evaluator eval = parser.parse();
        assertEquals(Evaluator.CombiningEvaluator.And.class, eval.getClass());
        Evaluator tag1Eval = ((Evaluator.CombiningEvaluator.And) eval).getLeft();
        assertEquals(Evaluator.StructuralEvaluator.Not.class, tag1Eval.getClass());
        assertEquals("div", ((Evaluator.StructuralEvaluator.Not) tag1Eval).getLeft().toString());
        assertEquals(".error", ((Evaluator.StructuralEvaluator.Not) tag1Eval).getRight().toString());
        Evaluator tag2Eval = ((Evaluator.CombiningEvaluator.And) eval).getRight();
        assertEquals(Evaluator.Tag.class, tag2Eval.getClass());
        assertEquals("p", tag2Eval.toString());
        assertEquals(".highlight", tag2Eval.getAttributes().get(Evaluator.Attribute.class.getName()).get(0).toString());
    }
}