package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;

public class QueryParserTest {

    @Test
    public void testParse_EmptyQuery() {
        String query = "";
        Evaluator result = QueryParser.parse(query);
        assertNull(result);
    }

    @Test
    public void testParse_TagSelector() {
        String query = "p";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertEquals(Evaluator.class, result.getClass());
        assertEquals(Evaluator.Tag.class, ((Evaluator) result).getClass());
        assertEquals("p", ((Evaluator.Tag) result).getValue());
    }

    @Test
    public void testParse_ClassSelector() {
        String query = ".class-name";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertEquals(Evaluator.class, result.getClass());
        assertEquals(Evaluator.Class.class, ((Evaluator) result).getClass());
        assertEquals("class-name", ((Evaluator.Class) result).getValue());
    }

    @Test
    public void testParse_IdSelector() {
        String query = "#id";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertEquals(Evaluator.class, result.getClass());
        assertEquals(Evaluator.Id.class, ((Evaluator) result).getClass());
        assertEquals("id", ((Evaluator.Id) result).getValue());
    }

    @Test
    public void testParse_AttributeSelector() {
        String query = "[attr='value']";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertEquals(Evaluator.class, result.getClass());
        assertEquals(Evaluator.Attribute.class, ((Evaluator) result).getClass());
        assertEquals("attr", ((Evaluator.Attribute) result).getName());
        assertEquals("value", ((Evaluator.Attribute) result).getValue());
    }

    @Test
    public void testParse_AttributeWithValueContainingSelector() {
        String query = "[attr*='value']";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertEquals(Evaluator.class, result.getClass());
        assertEquals(Evaluator.AttributeWithValueContaining.class, ((Evaluator) result).getClass());
        assertEquals("attr", ((Evaluator.AttributeWithValueContaining) result).getName());
        assertEquals("value", ((Evaluator.AttributeWithValueContaining) result).getValue());
    }

    @Test
    public void testParse_CombinedSelectors() {
        String query = "div p.class-name";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertEquals(Evaluator.class, result.getClass());
        assertEquals(CombiningEvaluator.And.class, ((Evaluator) result).getClass());
        assertEquals(2, ((CombiningEvaluator.And) result).getChildren().size());

        Evaluator child1 = ((CombiningEvaluator.And) result).getChildren().get(0);
        assertEquals(Evaluator.class, child1.getClass());
        assertEquals(Evaluator.Tag.class, ((Evaluator) child1).getClass());
        assertEquals("div", ((Evaluator.Tag) child1).getValue());

        Evaluator child2 = ((CombiningEvaluator.And) result).getChildren().get(1);
        assertEquals(Evaluator.class, child2.getClass());
        assertEquals(Evaluator.Class.class, ((Evaluator) child2).getClass());
        assertEquals("class-name", ((Evaluator.Class) child2).getValue());
    }

    @Test
    public void testParse_CombinedSelectorsWithNegation() {
        String query = "div:not(p)";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertEquals(Evaluator.class, result.getClass());
        assertEquals(StructuralEvaluator.Not.class, ((Evaluator) result).getClass());
        assertEquals(1, ((StructuralEvaluator.Not) result).getChild().size());

        Evaluator child = ((StructuralEvaluator.Not) result).getChild().get(0);
        assertEquals(Evaluator.class, child.getClass());
        assertEquals(Evaluator.Tag.class, ((Evaluator) child).getClass());
        assertEquals("div", ((Evaluator.Tag) child).getValue());
    }

    @Test
    public void testParse_PseudoClassSelectors() {
        String query = ":first-child";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertEquals(Evaluator.class, result.getClass());
        assertEquals(Evaluator.IsFirstChild.class, ((Evaluator) result).getClass());
    }

    @Test
    public void testParse_NthChildSelector() {
        String query = "li:nth-child(2)";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertEquals(Evaluator.class, result.getClass());
        assertEquals(Evaluator.IsNthChild.class, ((Evaluator) result).getClass());
        assertEquals(2, ((Evaluator.IsNthChild) result).getIndex());
    }

    @Test
    public void testParse_NthLastChildSelector() {
        String query = "li:nth-last-child(2)";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertEquals(Evaluator.class, result.getClass());
        assertEquals(Evaluator.IsNthLastChild.class, ((Evaluator) result).getClass());
        assertEquals(2, ((Evaluator.IsNthLastChild) result).getIndex());
    }

    @Test
    public void testParse_NthOfTypeSelector() {
        String query = "li:nth-of-type(2)";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertEquals(Evaluator.class, result.getClass());
        assertEquals(Evaluator.IsNthOfType.class, ((Evaluator) result).getClass());
        assertEquals(2, ((Evaluator.IsNthOfType) result).getIndex());
    }

    @Test
    public void testParse_NthLastOfTypeSelector() {
        String query = "li:nth-last-of-type(2)";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertEquals(Evaluator.class, result.getClass());
        assertEquals(Evaluator.IsNthLastOfType.class, ((Evaluator) result).getClass());
        assertEquals(2, ((Evaluator.IsNthLastOfType) result).getIndex());
    }

    @Test
    public void testParse_DescendantSelector() {
        String query = "div p";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertEquals(Evaluator.class, result.getClass());
        assertEquals(CombiningEvaluator.Descendant.class, ((Evaluator) result).getClass());
        assertEquals(2, ((CombiningEvaluator.Descendant) result).getDepth());

        Evaluator child1 = ((CombiningEvaluator.Descendant) result).getParent();
        assertEquals(Evaluator.class, child1.getClass());
        assertEquals(Evaluator.Tag.class, ((Evaluator) child1).getClass());
        assertEquals("div", ((Evaluator.Tag) child1).getValue());

        Evaluator child2 = ((CombiningEvaluator.Descendant) result).getChild();
        assertEquals(Evaluator.class, child2.getClass());
        assertEquals(Evaluator.Tag.class, ((Evaluator) child2).getClass());
        assertEquals("p", ((Evaluator.Tag) child2).getValue());
    }

    @Test
    public void testParse_ChildSelector() {
        String query = "div > p";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertEquals(Evaluator.class, result.getClass());
        assertEquals(CombiningEvaluator.Child.class, ((Evaluator) result).getClass());

        Evaluator parent = ((CombiningEvaluator.Child) result).getParent();
        assertEquals(Evaluator.class, parent.getClass());
        assertEquals(Evaluator.Tag.class, ((Evaluator) parent).getClass());
        assertEquals("div", ((Evaluator.Tag) parent).getValue());

        Evaluator child = ((CombiningEvaluator.Child) result).getChild();
        assertEquals(Evaluator.class, child.getClass());
        assertEquals(Evaluator.Tag.class, ((Evaluator) child).getClass());
        assertEquals("p", ((Evaluator.Tag) child).getValue());
    }

    @Test
    public void testParse_AdjacentSiblingSelector() {
        String query = "p + li";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertEquals(Evaluator.class, result.getClass());
        assertEquals(CombiningEvaluator.AdjacentSibling.class, ((Evaluator) result).getClass());

        Evaluator previous = ((CombiningEvaluator.AdjacentSibling) result).getPrevious();
        assertEquals(Evaluator.class, previous.getClass());
        assertEquals(Evaluator.Tag.class, ((Evaluator) previous).getClass());
        assertEquals("p", ((Evaluator.Tag) previous).getValue());

        Evaluator next = ((CombiningEvaluator.AdjacentSibling) result).getNext();
        assertEquals(Evaluator.class, next.getClass());
        assertEquals(Evaluator.Tag.class, ((Evaluator) next).getClass());
        assertEquals("li", ((Evaluator.Tag) next).getValue());
    }

    @Test
    public void testParse_GeneralSiblingSelector() {
        String query = "p ~ li";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertEquals(Evaluator.class, result.getClass());
        assertEquals(CombiningEvaluator.GeneralSibling.class, ((Evaluator) result).getClass());

        Evaluator previous = ((CombiningEvaluator.GeneralSibling) result).getPrevious();
        assertEquals(Evaluator.class, previous.getClass());
        assertEquals(Evaluator.Tag.class, ((Evaluator) previous).getClass());
        assertEquals("p", ((Evaluator.Tag) previous).getValue());

        Evaluator next = ((CombiningEvaluator.GeneralSibling) result).getNext();
        assertEquals(Evaluator.class, next.getClass());
        assertEquals(Evaluator.Tag.class, ((Evaluator) next).getClass());
        assertEquals("li", ((Evaluator.Tag) next).getValue());
    }

    @Test
    public void testParse_AttributeSelectorWithRegex() {
        String query = "[title~='regex']";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertEquals(Evaluator.class, result.getClass());
        assertEquals(Evaluator.Matches.class, ((Evaluator) result).getClass());

        Pattern pattern = ((Evaluator.Matches) result).getPattern();
        String regex = pattern.pattern();
        assertEquals("regex", regex);
    }
}