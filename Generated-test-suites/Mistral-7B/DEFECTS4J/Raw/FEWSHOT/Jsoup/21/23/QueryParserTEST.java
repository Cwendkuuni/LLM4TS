package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse_EmptyQuery() {
        String query = "";
        Evaluator evaluator = QueryParser.parse(query);
        assertNull(evaluator);
    }

    @Test
    public void testParse_TagSelector() {
        String query = "p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.TAG, evaluator.getType());
        assertEquals("p", evaluator.getValue());
    }

    @Test
    public void testParse_ClassSelector() {
        String query = ".className";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.CLASS, evaluator.getType());
        assertEquals("className", evaluator.getValue());
    }

    @Test
    public void testParse_IdSelector() {
        String query = "#id";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ID, evaluator.getType());
        assertEquals("id", evaluator.getValue());
    }

    @Test
    public void testParse_AttributeSelector() {
        String query = "[title='test']";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ATTRIBUTE, evaluator.getType());
        assertEquals("title", evaluator.getAttributeName());
        assertEquals("test", evaluator.getAttributeValue());
    }

    @Test
    public void testParse_Combinator() {
        String query = "div p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.AND, evaluator.getType());
        List<Evaluator> children = ((CombiningEvaluator.And) evaluator).getChildren();
        assertEquals(2, children.size());
        Evaluator child1 = children.get(0);
        assertEquals(Evaluator.Type.TAG, child1.getType());
        assertEquals("div", child1.getValue());
        Evaluator child2 = children.get(1);
        assertEquals(Evaluator.Type.TAG, child2.getType());
        assertEquals("p", child2.getValue());
    }

    @Test
    public void testParse_DescendantSelector() {
        String query = "div p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.AND, evaluator.getType());
        List<Evaluator> children = ((CombiningEvaluator.And) evaluator).getChildren();
        assertEquals(2, children.size());
        Evaluator child1 = children.get(0);
        assertEquals(Evaluator.Type.TAG, child1.getType());
        assertEquals("div", child1.getValue());
        Evaluator child2 = children.get(1);
        assertEquals(Evaluator.Type.DESCENDANT, child2.getType());
        assertEquals("p", child2.getValue());
    }

    @Test
    public void testParse_ChildSelector() {
        String query = "div > p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.AND, evaluator.getType());
        List<Evaluator> children = ((CombiningEvaluator.And) evaluator).getChildren();
        assertEquals(2, children.size());
        Evaluator child1 = children.get(0);
        assertEquals(Evaluator.Type.TAG, child1.getType());
        assertEquals("div", child1.getValue());
        Evaluator child2 = children.get(1);
        assertEquals(Evaluator.Type.IMMEDIATE_CHILD, child2.getType());
        assertEquals("p", child2.getValue());
    }

    @Test
    public void testParse_PrevSiblingSelector() {
        String query = "p ~ p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.AND, evaluator.getType());
        List<Evaluator> children = ((CombiningEvaluator.And) evaluator).getChildren();
        assertEquals(2, children.size());
        Evaluator child1 = children.get(0);
        assertEquals(Evaluator.Type.TAG, child1.getType());
        assertEquals("p", child1.getValue());
        Evaluator child2 = children.get(1);
        assertEquals(Evaluator.Type.PREV_SIBLING, child2.getType());
        assertEquals("p", child2.getValue());
    }

    @Test
    public void testParse_HasSelector() {
        String query = "div:has(p)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.HAS, evaluator.getType());
        Evaluator child = evaluator.getChild();
        assertNotNull(child);
        assertEquals(Evaluator.Type.TAG, child.getType());
        assertEquals("p", child.getValue());
    }

    @Test
    public void testParse_ContainsSelector() {
        String query = "div:contains(test)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.CONTAINS, evaluator.getType());
        assertEquals("test", evaluator.getValue());
    }

    @Test
    public void testParse_ContainsOwnSelector() {
        String query = "div:containsOwn(test)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.CONTAINS_OWN, evaluator.getType());
        assertEquals("test", evaluator.getValue());
    }

    @Test
    public void testParse_MatchesSelector() {
        String query = "div:matches(p:first-child)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.MATCHES, evaluator.getType());
        Evaluator child = evaluator.getChild();
        assertNotNull(child);
        assertEquals(Evaluator.Type.CLASS, child.getType());
        assertEquals("first-child", child.getValue());
    }

    @Test
    public void testParse_MatchesOwnSelector() {
        String query = "div:matchesOwn(p:first-child)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.MATCHES_OWN, evaluator.getType());
        Evaluator child = evaluator.getChild();
        assertNotNull(child);
        assertEquals(Evaluator.Type.CLASS, child.getType());
        assertEquals("first-child", child.getValue());
    }

    @Test
    public void testParse_NotSelector() {
        String query = "div:not(.error)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.NOT, evaluator.getType());
        Evaluator child = evaluator.getChild();
        assertNotNull(child);
        assertEquals(Evaluator.Type.CLASS, child.getType());
        assertEquals("error", child.getValue());
    }
}