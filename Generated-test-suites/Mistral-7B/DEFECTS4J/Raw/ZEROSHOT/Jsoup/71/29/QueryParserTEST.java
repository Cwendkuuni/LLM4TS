package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import java.util.List;

public class QueryParserTest {

    @Test
    public void testParseSimpleSelector() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.class, evaluator.getClass());
        assertEquals(Evaluator.Tag.class, evaluator.getClass().getSuperclass());
        assertEquals("div", evaluator.toString());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidSelector() {
        String query = "div^";
        QueryParser.parse(query);
    }

    @Test
    public void testParseCombinedSelectors() {
        String query = "div > p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
        assertEquals(Evaluator.class, evaluator.getClass().getComponentType().getClass());
        assertEquals(StructuralEvaluator.ImmediateParent.class, evaluator.getClass().getComponentType().getClass().getSuperclass());
        assertEquals("div > p", evaluator.toString());
    }

    @Test
    public void testParseSelectorWithClass() {
        String query = "div.className";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
        assertEquals(Evaluator.class, evaluator.getClass().getComponentType().getClass());
        assertEquals(Evaluator.Class.class, evaluator.getClass().getComponentType().getClass().getComponentType().getClass());
        assertEquals("div.className", evaluator.toString());
    }

    @Test
    public void testParseSelectorWithId() {
        String query = "div#id";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
        assertEquals(Evaluator.class, evaluator.getClass().getComponentType().getClass());
        assertEquals(Evaluator.Id.class, evaluator.getClass().getComponentType().getClass().getComponentType().getClass());
        assertEquals("div#id", evaluator.toString());
    }

    @Test
    public void testParseSelectorWithAttribute() {
        String query = "div[class='class']";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
        assertEquals(Evaluator.class, evaluator.getClass().getComponentType().getClass());
        assertEquals(Evaluator.Attribute.class, evaluator.getClass().getComponentType().getClass().getComponentType().getClass());
        assertEquals("div[class='class']", evaluator.toString());
    }

    @Test
    public void testParseSelectorWithAttributeAndValue() {
        String query = "div[class='class' and title='title']";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
        assertEquals(Evaluator.class, evaluator.getClass().getComponentType().getClass());
        Evaluator attributeEvaluator = evaluator.getClass().getComponentType().getClass().getComponentType();
        assertEquals(AttributeEvaluator.class, attributeEvaluator.getClass());
        assertEquals("class", attributeEvaluator.getAttributeName());
        assertEquals("title", evaluator.getClass().getComponentType().getClass().getComponentType().getClass().getComponentType().toString());
        assertEquals("div[class='class' and title='title']", evaluator.toString());
    }

    @Test
    public void testParseSelectorWithPseudoClass() {
        String query = "div.active";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.class, evaluator.getClass().getComponentType().getClass().getComponentType().getClass());
        assertEquals("active", evaluator.toString());
    }

    @Test
    public void testParseSelectorWithCombinatorAnd() {
        String query = "div.class1, div.class2";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(CombiningEvaluator.Or.class, evaluator.getClass());
        List<Evaluator> evaluators = (List<Evaluator>) evaluator;
        assertEquals(2, evaluators.size());
        Evaluator firstEvaluator = evaluators.get(0);
        assertEquals(CombiningEvaluator.And.class, firstEvaluator.getClass());
        Evaluator firstAndEvaluator = firstEvaluator.getClass().getComponentType().getClass();
        assertEquals(Evaluator.class, firstAndEvaluator.getClass().getComponentType().getClass());
        assertEquals(Evaluator.Class.class, firstAndEvaluator.getClass().getComponentType().getClass().getComponentType().getClass());
        assertEquals("div.class1", firstAndEvaluator.toString());
        Evaluator secondEvaluator = evaluators.get(1);
        assertEquals(CombiningEvaluator.And.class, secondEvaluator.getClass());
        Evaluator secondAndEvaluator = secondEvaluator.getClass().getComponentType().getClass();
        assertEquals("div.class2", secondAndEvaluator.toString());
    }

    @Test
    public void testParseSelectorWithCombinatorOr() {
        String query = "div.class1 or div.class2";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(CombiningEvaluator.Or.class, evaluator.getClass());
        List<Evaluator> evaluators = (List<Evaluator>) evaluator;
        assertEquals(2, evaluators.size());
        Evaluator firstEvaluator = evaluators.get(0);
        assertEquals(CombiningEvaluator.And.class, firstEvaluator.getClass());
        Evaluator firstAndEvaluator = firstEvaluator.getClass().getComponentType().getClass();
        assertEquals("div.class1", firstAndEvaluator.toString());
        Evaluator secondEvaluator = evaluators.get(1);
        assertEquals(CombiningEvaluator.And.class, secondEvaluator.getClass());
        Evaluator secondAndEvaluator = secondEvaluator.getClass().getComponentType().getClass();
        assertEquals("div.class2", secondAndEvaluator.toString());
    }

    @Test
    public void testParseSelectorWithCombinatorDescendant() {
        String query = "div > p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
        Evaluator firstEvaluator = evaluator.getClass().getComponentType();
        assertEquals(StructuralEvaluator.ImmediateParent.class, firstEvaluator.getClass());
        assertEquals("div", firstEvaluator.toString());
        Evaluator secondEvaluator = evaluator.getClass().getComponentType().getClass();
        assertEquals(Evaluator.Tag.class, secondEvaluator.getClass());
        assertEquals("p", secondEvaluator.toString());
    }

    @Test
    public void testParseSelectorWithCombinatorChild() {
        String query = "div > p > span";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
        Evaluator parentEvaluator = evaluator.getClass().getComponentType();
        assertEquals(StructuralEvaluator.ImmediateParent.class, parentEvaluator.getClass());
        assertEquals("div", parentEvaluator.toString());
        Evaluator childEvaluator = parentEvaluator.getClass().getComponentType().getClass();
        assertEquals(CombiningEvaluator.And.class, childEvaluator.getClass());
        Evaluator lastEvaluator = childEvaluator.getClass().getComponentType();
        assertEquals(Evaluator.Tag.class, lastEvaluator.getClass());
        assertEquals("span", lastEvaluator.toString());
    }

    @Test
    public void testParseSelectorWithAttributeValueAndSubSelector() {
        String query = "div[class='class' and title='title'] > p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
        Evaluator attributeEvaluator = evaluator.getClass().getComponentType();
        assertEquals(CombiningEvaluator.And.class, attributeEvaluator.getClass());
        Evaluator attributeFirstEvaluator = attributeEvaluator.getClass().getComponentType();
        assertEquals(Evaluator.class, attributeFirstEvaluator.getClass().getComponentType().getClass().getComponentType().getClass());
        assertEquals("class", attributeFirstEvaluator.toString());
        Evaluator attributeSecondEvaluator = attributeEvaluator.getClass().getComponentType().getClass().getComponentType();
        assertEquals("title", attributeSecondEvaluator.toString());
        Evaluator childEvaluator = evaluator.getClass().getComponentType().getClass();
        assertEquals(StructuralEvaluator.ImmediateChild.class, childEvaluator.getClass());
        assertEquals("p", childEvaluator.toString());
    }

    @Test
    public void testParseSelectorWithNthChild() {
        String query = "li:nth-child(2)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.IsNthChild.class, evaluator.getClass());
        assertEquals("li", evaluator.toString());
        assertEquals(2, ((Evaluator.IsNthChild) evaluator).getIndex());
    }

    @Test
    public void testParseSelectorWithNthLastChild() {
        String query = "li:nth-last-child(2)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.IsNthLastChild.class, evaluator.getClass());
        assertEquals("li", evaluator.toString());
        assertEquals(2, ((Evaluator.IsNthLastChild) evaluator).getIndex());
    }

    @Test
    public void testParseSelectorWithNthOfType() {
        String query = "li:nth-of-type(2)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.IsNthOfType.class, evaluator.getClass());
        assertEquals("li", evaluator.toString());
        assertEquals(2, ((Evaluator.IsNthOfType) evaluator).getIndex());
    }

    @Test
    public void testParseSelectorWithNthLastOfType() {
        String query = "li:nth-last-of-type(2)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.IsNthLastOfType.class, evaluator.getClass());
        assertEquals("li", evaluator.toString());
        assertEquals(2, ((Evaluator.IsNthLastOfType) evaluator).getIndex());
    }

    @Test
    public void testParseSelectorWithPseudoElement() {
        String query = "li:first-child::before";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(StructuralEvaluator.PseudoElementFirstChild.class, evaluator.getClass());
        assertEquals("li", evaluator.toString());
    }

    @Test
    public void testParseSelectorWithPseudoElementLastChild() {
        String query = "li:last-child::after";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(StructuralEvaluator.PseudoElementLastChild.class, evaluator.getClass());
        assertEquals("li", evaluator.toString());
    }

    @Test
    public void testParseSelectorWithPseudoElementOnlyChild() {
        String query = "li:only-child";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.IsOnlyChild.class, evaluator.getClass());
        assertEquals("li", evaluator.toString());
    }

    @Test
    public void testParseSelectorWithPseudoElementOnlyOfType() {
        String query = "li:only-of-type";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.IsOnlyOfType.class, evaluator.getClass());
        assertEquals("li", evaluator.toString());
    }

    @Test
    public void testParseSelectorWithPseudoElementEmpty() {
        String query = "li:empty";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.IsEmpty.class, evaluator.getClass());
        assertEquals("li", evaluator.toString());
    }

    @Test
    public void testParseSelectorWithPseudoElementRoot() {
        String query = ":root";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.IsRoot.class, evaluator.getClass());
    }
}