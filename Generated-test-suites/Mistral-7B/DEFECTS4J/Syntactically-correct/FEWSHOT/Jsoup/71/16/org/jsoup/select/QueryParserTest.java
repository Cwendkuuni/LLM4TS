package org.jsoup.select;

import static org.junit.Assert.*;

import org.jsoup.select.QueryParser;

import java.util.List;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        Evaluator evaluator = QueryParser.parse("");
        assertTrue(evaluator instanceof Evaluator.AllElements);
    }

    @Test
    public void testParseTagQuery() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.class, evaluator.getClass());
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) evaluator).getTagName());
    }

    @Test
    public void testParseClassQuery() {
        String query = ".red";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.class, evaluator.getClass());
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals("red", ((Evaluator.Class) evaluator).getClassName());
    }

    @Test
    public void testParseIdQuery() {
        String query = "#myId";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.class, evaluator.getClass());
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals("myId", ((Evaluator.Id) evaluator).getId());
    }

    @Test
    public void testParseAttributeQuery() {
        String query = "[title='Hello']";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.class, evaluator.getClass());
        assertTrue(evaluator instanceof Evaluator.Attribute);
        assertEquals("title", ((Evaluator.Attribute) evaluator).getAttributeName());
        assertEquals("Hello", ((Evaluator.Attribute) evaluator).getValue());
    }

    @Test
    public void testParseAttributeQueryWithOperator() {
        String query = "[title!='Hello']";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.class, evaluator.getClass());
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueNot);
        assertEquals("title", ((Evaluator.AttributeWithValueNot) evaluator).getAttributeName());
        assertEquals("Hello", ((Evaluator.AttributeWithValueNot) evaluator).getValue());
    }

    @Test
    public void testParseAttributeQueryWithStartsWithOperator() {
        String query = "[title^='Hello']";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.class, evaluator.getClass());
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueStarting);
        assertEquals("title", ((Evaluator.AttributeWithValueStarting) evaluator).getAttributeName());
        assertEquals("Hello", ((Evaluator.AttributeWithValueStarting) evaluator).getValue());
    }

    @Test
    public void testParseAttributeQueryWithEndsWithOperator() {
        String query = "[title$='Hello']";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.class, evaluator.getClass());
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueEnding);
        assertEquals("title", ((Evaluator.AttributeWithValueEnding) evaluator).getAttributeName());
        assertEquals("Hello", ((Evaluator.AttributeWithValueEnding) evaluator).getValue());
    }

    @Test
    public void testParseAttributeQueryWithContainsOperator() {
        String query = "[title*='Hello']";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.class, evaluator.getClass());
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueContaining);
        assertEquals("title", ((Evaluator.AttributeWithValueContaining) evaluator).getAttributeName());
        assertEquals("Hello", ((Evaluator.AttributeWithValueContaining) evaluator).getValue());
    }

    @Test
    public void testParseAttributeQueryWithMatchingOperator() {
        String query = "[title~='Hello']";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.class, evaluator.getClass());
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueMatching);
        assertEquals("title", ((Evaluator.AttributeWithValueMatching) evaluator).getAttributeName());
        Pattern pattern = ((Evaluator.AttributeWithValueMatching) evaluator).getPattern();
        Matcher matcher = pattern.matcher("Hello World");
        assertTrue(matcher.find());
    }

    @Test
    public void testParseDescendantSelector() {
        String query = "div > p";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.class, evaluator.getClass());
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        List<Evaluator> children = ((CombiningEvaluator.And) evaluator).getChildren();
        assertEquals(2, children.size());
        Evaluator firstChild = children.get(0);
        assertTrue(firstChild instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) firstChild).getTagName());
        Evaluator secondChild = children.get(1);
        assertTrue(secondChild instanceof Evaluator.ImmediateChild);
        assertEquals("p", ((Evaluator.ImmediateChild) secondChild).getTagName());
    }

    @Test
    public void testParseAdjacentSiblingSelector() {
        String query = "h1 + p";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.class, evaluator.getClass());
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        List<Evaluator> children = ((CombiningEvaluator.And) evaluator).getChildren();
        assertEquals(2, children.size());
        Evaluator firstChild = children.get(0);
        assertTrue(firstChild instanceof Evaluator.Tag);
        assertEquals("h1", ((Evaluator.Tag) firstChild).getTagName());
        Evaluator secondChild = children.get(1);
        assertTrue(secondChild instanceof Evaluator.ImmediatePreviousSibling);
        assertEquals("p", ((Evaluator.ImmediatePreviousSibling) secondChild).getTagName());
    }

    @Test
    public void testParseGeneralSiblingSelector() {
        String query = "h1 ~ p";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.class, evaluator.getClass());
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        List<Evaluator> children = ((CombiningEvaluator.And) evaluator).getChildren();
        assertEquals(2, children.size());
        Evaluator firstChild = children.get(0);
        assertTrue(firstChild instanceof Evaluator.Tag);
        assertEquals("h1", ((Evaluator.Tag) firstChild).getTagName());
        Evaluator secondChild = children.get(1);
        assertTrue(secondChild instanceof Evaluator.PreviousSibling);
        assertEquals("p", ((Evaluator.PreviousSibling) secondChild).getTagName());
    }

    @Test
    public void testParseAttributeSelectorWithValue() {
        String query = "[data-test='button']";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.class, evaluator.getClass());
        assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
        assertEquals("data-test", ((Evaluator.AttributeWithValue) evaluator).getAttributeName());
        assertEquals("button", ((Evaluator.AttributeWithValue) evaluator).getValue());
    }

    @Test
    public void testParseAttributeSelectorWithOperatorAndValue() {
        String query = "[data-test!='button']";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.class, evaluator.getClass());
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueNot);
        assertEquals("data-test", ((Evaluator.AttributeWithValueNot) evaluator).getAttributeName());
        assertEquals("button", ((Evaluator.AttributeWithValueNot) evaluator).getValue());
    }

    @Test
    public void testParsePseudoClassSelector() {
        String query = "input:checked";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.class, evaluator.getClass());
        assertTrue(evaluator instanceof Evaluator.PseudoClass);
        assertEquals("checked", ((Evaluator.PseudoClass) evaluator).getPseudoClass());
    }

    @Test
    public void testParseNegatedPseudoClassSelector() {
        String query = "input:not(:checked)";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.class, evaluator.getClass());
        assertTrue(evaluator instanceof StructuralEvaluator.Not);
        Evaluator child = ((StructuralEvaluator.Not) evaluator).getChild();
        assertEquals(Evaluator.class, child.getClass());
        assertTrue(child instanceof Evaluator.PseudoClass);
        assertEquals("checked", ((Evaluator.PseudoClass) child).getPseudoClass());
    }

    @Test
    public void testParseCombinedSelectors() {
        String query = "div > p:first-child, div > ul > li:not(:last-child)";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.class, evaluator.getClass());
        assertTrue(evaluator instanceof CombiningEvaluator.Or);
        List<Evaluator> children = ((CombiningEvaluator.Or) evaluator).getChildren();
        assertEquals(2, children.size());
        Evaluator firstChild = children.get(0);
        assertTrue(firstChild instanceof CombiningEvaluator.And);
        List<Evaluator> firstChildChildren = ((CombiningEvaluator.And) firstChild).getChildren();
        assertEquals(2, firstChildChildren.size());
        Evaluator firstChildFirstChild = firstChildChildren.get(0);
        assertTrue(firstChildFirstChild instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) firstChildFirstChild).getTagName());
        Evaluator firstChildSecondChild = firstChildChildren.get(1);
        assertTrue(firstChildSecondChild instanceof Evaluator.ImmediateChild);
        assertEquals("p", ((Evaluator.ImmediateChild) firstChildSecondChild).getTagName());
        Evaluator secondChild = children.get(1);
        assertTrue(secondChild instanceof CombiningEvaluator.And);
        List<Evaluator> secondChildChildren = ((CombiningEvaluator.And) secondChild).getChildren();
        assertEquals(3, secondChildChildren.size());
        Evaluator secondChildFirstChild = secondChildChildren.get(0);
        assertTrue(secondChildFirstChild instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) secondChildFirstChild).getTagName());
        Evaluator secondChildSecondChild = secondChildChildren.get(1);
        assertTrue(secondChildSecondChild instanceof Evaluator.Tag);
        assertEquals("ul", ((Evaluator.Tag) secondChildSecondChild).getTagName());
        Evaluator secondChildThirdChild = secondChildChildren.get(2);
        assertTrue(secondChildThirdChild instanceof Evaluator.Not);
        Evaluator secondChildThirdChildChild = ((Evaluator.Not) secondChildThirdChild).getChild();
        assertTrue(secondChildThirdChildChild instanceof Evaluator.Tag);
        assertEquals("li", ((Evaluator.Tag) secondChildThirdChildChild).getTagName());
    }
}