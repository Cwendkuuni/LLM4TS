package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse_EmptyQuery() {
        QueryParser parser = new QueryParser("");
        Evaluator evaluator = parser.parse();
        assertNull(evaluator);
    }

    @Test
    public void testParse_SimpleSelector() {
        String query = "div";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertEquals(EvaluatorType.TAG, evaluator.getType());
        assertEquals(query, evaluator.toString());
    }

    @Test
    public void testParse_ClassSelector() {
        String query = ".button";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertEquals(EvaluatorType.CLASS, evaluator.getType());
        assertEquals(query, evaluator.toString());
    }

    @Test
    public void testParse_IDSelector() {
        String query = "#header";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertEquals(EvaluatorType.ID, evaluator.getType());
        assertEquals(query, evaluator.toString());
    }

    @Test
    public void testParse_AttributeSelector() {
        String query = "[title~='search']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertEquals(EvaluatorType.ATTRIBUTE, evaluator.getType());
        assertEquals(query, evaluator.toString());
    }

    @Test
    public void testParse_CombinedSelectors() {
        String query = "div.button#header";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);

        CombiningEvaluator and = (CombiningEvaluator) evaluator;
        assertEquals(EvaluatorType.AND, and.getType());

        List<Evaluator> children = and.getChildren();
        assertEquals(3, children.size());

        Evaluator tag = children.get(0);
        assertEquals(EvaluatorType.TAG, tag.getType());
        assertEquals("div", tag.toString());

        Evaluator classSelector = children.get(1);
        assertEquals(EvaluatorType.CLASS, classSelector.getType());
        assertEquals("button", classSelector.toString());

        Evaluator idSelector = children.get(2);
        assertEquals(EvaluatorType.ID, idSelector.getType());
        assertEquals("header", idSelector.toString());
    }

    @Test
    public void testParse_DescendantSelector() {
        String query = "div ul li.item";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);

        StructuralEvaluator descendant = (StructuralEvaluator) evaluator;
        assertEquals(EvaluatorType.DESCENDANT, descendant.getType());
        assertEquals("div > ul > li.item", descendant.toString());
    }

    @Test
    public void testParse_AdjacentSiblingSelector() {
        String query = "li~p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);

        StructuralEvaluator adjacentSibling = (StructuralEvaluator) evaluator;
        assertEquals(EvaluatorType.ADJACENT_SIBLING, adjacentSibling.getType());
        assertEquals("li ~ p", adjacentSibling.toString());
    }

    @Test
    public void testParse_GeneralSiblingSelector() {
        String query = "li > p ~ p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);

        StructuralEvaluator generalSibling = (StructuralEvaluator) evaluator;
        assertEquals(EvaluatorType.GENERAL_SIBLING, generalSibling.getType());
        assertEquals("li > p ~ p", generalSibling.toString());
    }

    @Test
    public void testParse_NegatedSelector() {
        String query = "div:not(.button)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);

        StructuralEvaluator not = (StructuralEvaluator) evaluator;
        assertEquals(EvaluatorType.NOT, not.getType());

        Evaluator child = not.getChild();
        assertEquals(EvaluatorType.CLASS, child.getType());
        assertEquals(".button", child.toString());
    }

    @Test
    public void testParse_PseudoClasses() {
        String query = "a:hover, input:focus";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);

        CombiningEvaluator or = (CombiningEvaluator) evaluator;
        assertEquals(EvaluatorType.OR, or.getType());

        List<Evaluator> children = or.getChildren();
        assertEquals(2, children.size());

        Evaluator hover = children.get(0);
        assertEquals(EvaluatorType.PSEUDO_CLASS, hover.getType());
        assertEquals("a:hover", hover.toString());

        Evaluator focus = children.get(1);
        assertEquals(EvaluatorType.PSEUDO_CLASS, focus.getType());
        assertEquals("input:focus", focus.toString());
    }

    @Test
    public void testParse_NthChildSelectors() {
        String query = "li:nth-child(2), li:nth-last-child(-n+3)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);

        CombiningEvaluator and = (CombiningEvaluator) evaluator;
        assertEquals(EvaluatorType.AND, and.getType());

        List<Evaluator> children = and.getChildren();
        assertEquals(2, children.size());

        Evaluator secondChild = children.get(0);
        assertEquals(EvaluatorType.NTH_CHILD, secondChild.getType());
        assertEquals("li:nth-child(2)", secondChild.toString());

        Evaluator lastThreeChildren = children.get(1);
        assertEquals(EvaluatorType.NTH_LAST_CHILD, lastThreeChildren.getType());
        assertEquals("li:nth-last-child(-n+3)", lastThreeChildren.toString());
    }

    @Test
    public void testParse_NthOfTypeSelectors() {
        String query = "li:nth-of-type(2), li:nth-last-of-type(-n+3)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);

        CombiningEvaluator and = (CombiningEvaluator) evaluator;
        assertEquals(EvaluatorType.AND, and.getType());

        List<Evaluator> children = and.getChildren();
        assertEquals(2, children.size());

        Evaluator secondChild = children.get(0);
        assertEquals(EvaluatorType.NTH_OF_TYPE, secondChild.getType());
        assertEquals("li:nth-of-type(2)", secondChild.toString());

        Evaluator lastThreeChildren = children.get(1);
        assertEquals(EvaluatorType.NTH_LAST_OF_TYPE, lastThreeChildren.getType());
        assertEquals("li:nth-last-of-type(-n+3)", lastThreeChildren.toString());
    }

    @Test
    public void testParse_AttributeValueSelectors() {
        String query = "input[type='text'], input[placeholder]";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);

        CombiningEvaluator or = (CombiningEvaluator) evaluator;
        assertEquals(EvaluatorType.OR, or.getType());

        List<Evaluator> children = or.getChildren();
        assertEquals(2, children.size());

        Evaluator textInput = children.get(0);
        assertEquals(EvaluatorType.ATTRIBUTE_VALUE, textInput.getType());
        assertEquals("input[type='text']", textInput.toString());

        Evaluator placeholderInput = children.get(1);
        assertEquals(EvaluatorType.ATTRIBUTE_VALUE, placeholderInput.getType());
        assertEquals("input[placeholder]", placeholderInput.toString());
    }
}