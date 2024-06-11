package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        Evaluator evaluator = QueryParser.parse("");
        assertNull(evaluator);
    }

    @Test
    public void testParseSimpleTag() {
        String query = "p";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.TAG, evaluator.getType());
        assertEquals(Evaluator.Tag.class, evaluator.getClass());
        assertEquals("p", ((Evaluator.Tag) evaluator).getValue());
    }

    @Test
    public void testParseIdSelector() {
        String query = "#id1";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.ID, evaluator.getType());
        assertEquals(Evaluator.Id.class, evaluator.getClass());
        assertEquals("id1", ((Evaluator.Id) evaluator).getId());
    }

    @Test
    public void testParseClassSelector() {
        String query = ".class1";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.CLASS, evaluator.getType());
        assertEquals(Evaluator.Class.class, evaluator.getClass());
        assertEquals("class1", ((Evaluator.Class) evaluator).getClassValue());
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "[title='Test']";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.ATTRIBUTE, evaluator.getType());
        assertEquals(Evaluator.Attribute.class, evaluator.getClass());
        assertEquals("title", ((Evaluator.Attribute) evaluator).getAttributeName());
        assertEquals("Test", ((Evaluator.Attribute) evaluator).getAttributeValue());
    }

    @Test
    public void testParseDescendantSelector() {
        String query = "p > div";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.STRUCTURAL, evaluator.getType());
        assertEquals(StructuralEvaluator.class, evaluator.getClass());
        StructuralEvaluator structuralEvaluator = (StructuralEvaluator) evaluator;
        assertEquals(StructuralEvaluator.Type.DESCENDANT, structuralEvaluator.getType());
        assertEquals("p", ((StructuralEvaluator) evaluator).getParentSelector());
        assertEquals("div", structuralEvaluator.getChildSelector());
    }

    @Test
    public void testParseAdjacentSiblingSelector() {
        String query = "p + div";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.STRUCTURAL, evaluator.getType());
        assertEquals(StructuralEvaluator.class, evaluator.getClass());
        StructuralEvaluator structuralEvaluator = (StructuralEvaluator) evaluator;
        assertEquals(StructuralEvaluator.Type.ADJACENT_SIBLING, structuralEvaluator.getType());
        assertEquals("p", structuralEvaluator.getParentSelector());
        assertEquals("div", structuralEvaluator.getSiblingSelector());
    }

    @Test
    public void testParseGeneralSiblingSelector() {
        String query = "p ~ div";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.STRUCTURAL, evaluator.getType());
        assertEquals(StructuralEvaluator.class, evaluator.getClass());
        StructuralEvaluator structuralEvaluator = (StructuralEvaluator) evaluator;
        assertEquals(StructuralEvaluator.Type.GENERAL_SIBLING, structuralEvaluator.getType());
        assertEquals("p", structuralEvaluator.getParentSelector());
        assertEquals("div", structuralEvaluator.getSiblingSelector());
    }

    @Test
    public void testParsePseudoClassSelector() {
        String query = "p:first-child";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.STRUCTURAL, evaluator.getType());
        assertEquals(StructuralEvaluator.class, evaluator.getClass());
        StructuralEvaluator structuralEvaluator = (StructuralEvaluator) evaluator;
        assertEquals(StructuralEvaluator.Type.PSEUDO_CLASS, structuralEvaluator.getType());
        assertEquals(StructuralEvaluator.PseudoClass.FIRST_CHILD, structuralEvaluator.getPseudoClass());
        assertEquals("p", structuralEvaluator.getParentSelector());
    }

    @Test
    public void testParseCombinedSelector() {
        String query = "p.class1, div.class2";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.COMBINED, evaluator.getType());
        assertEquals(CombiningEvaluator.class, evaluator.getClass());
        CombiningEvaluator combiningEvaluator = (CombiningEvaluator) evaluator;
        List<Evaluator> childSelectors = combiningEvaluator.getChildSelectors();

        assertEquals(2, childSelectors.size());

        Evaluator firstSelector = childSelectors.get(0);
        assertEquals(Evaluator.Type.CLASS, firstSelector.getType());
        assertEquals(Evaluator.Class.class, firstSelector.getClass());
        assertEquals("p", ((Evaluator.Class) firstSelector).getParentSelector());
        assertEquals("class1", ((Evaluator.Class) firstSelector).getClassValue());

        Evaluator secondSelector = childSelectors.get(1);
        assertEquals(Evaluator.Type.CLASS, secondSelector.getType());
        assertEquals(Evaluator.Class.class, secondSelector.getClass());
        assertEquals("div", ((Evaluator.Class) secondSelector).getParentSelector());
        assertEquals("class2", ((Evaluator.Class) secondSelector).getClassValue());
    }

    @Test
    public void testParseNegatedSelector() {
        String query = "p:not(.class1)";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.NEGATED, evaluator.getType());
        assertEquals(Evaluator.class, evaluator.getClass());
        Evaluator innerEvaluator = evaluator.getInnerEvaluator();
        assertEquals(Evaluator.Type.CLASS, innerEvaluator.getType());
        assertEquals(Evaluator.Class.class, evaluator.getClass());
        assertEquals("p", ((Evaluator.Class) innerEvaluator).getParentSelector());
        assertEquals("class1", ((Evaluator.Class) innerEvaluator).getClassValue());
    }

    @Test
    public void testParseComplexSelector() {
        String query = "div#id1 > ul > li:first-child > a[href^='http']";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.COMBINED, evaluator.getType());
        assertEquals(CombiningEvaluator.class, evaluator.getClass());
        List<Evaluator> childSelectors = ((CombiningEvaluator) evaluator).getChildSelectors();

        Evaluator parentSelector = childSelectors.get(0);
        assertEquals(Evaluator.Type.ID, parentSelector.getType());
        assertEquals(Evaluator.Id.class, parentSelector.getClass());
        assertEquals("div", ((Evaluator.Id) parentSelector).getParentSelector());
        assertEquals("id1", ((Evaluator.Id) parentSelector).getId());

        Evaluator child1 = childSelectors.get(1);
        assertEquals(Evaluator.Type.TAG, child1.getType());
        assertEquals(Evaluator.Tag.class, child1.getClass());
        assertEquals("ul", ((Evaluator.Tag) child1).getValue());
        assertEquals("div", ((Evaluator.Tag) child1).getParentSelector());

        Evaluator child2 = childSelectors.get(2);
        assertEquals(Evaluator.Type.STRUCTURAL, child2.getType());
        assertEquals(StructuralEvaluator.class, child2.getClass());
        StructuralEvaluator structuralEvaluator = (StructuralEvaluator) child2;
        assertEquals(StructuralEvaluator.Type.FIRST_CHILD, structuralEvaluator.getType());
        assertEquals("li", structuralEvaluator.getChildSelector());
        assertEquals("ul", structuralEvaluator.getParentSelector());

        Evaluator child3 = childSelectors.get(3);
        assertEquals(Evaluator.Type.STRUCTURAL, child3.getType());
        assertEquals(StructuralEvaluator.class, child3.getClass());
        structuralEvaluator = (StructuralEvaluator) child3;
        assertEquals(StructuralEvaluator.Type.TAG, structuralEvaluator.getType());
        assertEquals("a", structuralEvaluator.getChildSelector());
        assertEquals("li", structuralEvaluator.getParentSelector());

        Evaluator attributeEvaluator = ((StructuralEvaluator) child3).getInnerEvaluator();
        assertEquals(Evaluator.Type.ATTRIBUTE, attributeEvaluator.getType());
        assertEquals(Evaluator.Attribute.class, attributeEvaluator.getClass());
        assertEquals("a", attributeEvaluator.getParentSelector());
        assertEquals("href", ((Evaluator.Attribute) attributeEvaluator).getAttributeName());
        assertEquals("^='http", ((Evaluator.Attribute) attributeEvaluator).getAttributeValuePattern());
    }
}