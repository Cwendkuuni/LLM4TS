package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParseSimpleTag() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.TAG, evaluator.getType());
        assertEquals("div", evaluator.toString());
    }

    @Test
    public void testParseIdSelector() {
        String query = "#id1";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ID, evaluator.getType());
        assertEquals("#id1", evaluator.toString());
    }

    @Test
    public void testParseClassSelector() {
        String query = ".class1";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.CLASS, evaluator.getType());
        assertEquals(".class1", evaluator.toString());
    }

    @Test
    public void testParseTagWithAttribute() {
        String query = "input[type=text]";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        Evaluator attributeEvaluator = ((Evaluator.Elements) evaluator).get(0);
        assertEquals(Evaluator.Type.TAG, attributeEvaluator.getType());
        assertEquals("input", attributeEvaluator.toString());
        Evaluator attributeValueEvaluator = ((Evaluator.Attributes) attributeEvaluator).get(0);
        assertEquals(Evaluator.Type.ATTRIBUTE, attributeValueEvaluator.getType());
        assertEquals("type", attributeValueEvaluator.toString());
        assertEquals(AttributeEvaluator.Operator.EQUALS, attributeValueEvaluator.getOperator());
        assertEquals("text", attributeValueEvaluator.getValue());
    }

    @Test
    public void testParseNthChildSelector() {
        String query = "li:nth-child(2)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        Evaluator nthChildEvaluator = ((Evaluator.Elements) evaluator).get(0);
        assertEquals(Evaluator.Type.NTH_CHILD, nthChildEvaluator.getType());
        assertEquals("li", nthChildEvaluator.toString());
        assertEquals(2, ((Evaluator.NthChild) nthChildEvaluator).getIndex());
        assertEquals(false, ((Evaluator.NthChild) nthChildEvaluator).isOfType());
    }

    @Test
    public void testParseNthLastChildSelector() {
        String query = "li:nth-last-child(2)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        Evaluator nthLastChildEvaluator = ((Evaluator.Elements) evaluator).get(0);
        assertEquals(Evaluator.Type.NTH_LAST_CHILD, nthLastChildEvaluator.getType());
        assertEquals("li", nthLastChildEvaluator.toString());
        assertEquals(2, ((Evaluator.NthLastChild) nthLastChildEvaluator).getIndex());
        assertEquals(false, ((Evaluator.NthLastChild) nthLastChildEvaluator).isOfType());
    }

    @Test
    public void testParseNthOfTypeSelector() {
        String query = "li:nth-of-type(2)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        Evaluator nthOfTypeEvaluator = ((Evaluator.Elements) evaluator).get(0);
        assertEquals(Evaluator.Type.NTH_OF_TYPE, nthOfTypeEvaluator.getType());
        assertEquals("li", nthOfTypeEvaluator.toString());
        assertEquals(2, ((Evaluator.NthOfType) nthOfTypeEvaluator).getIndex());
        assertEquals(false, ((Evaluator.NthOfType) nthOfTypeEvaluator).isOfType());
    }

    @Test
    public void testParseNthLastOfTypeSelector() {
        String query = "li:nth-last-of-type(2)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        Evaluator nthLastOfTypeEvaluator = ((Evaluator.Elements) evaluator).get(0);
        assertEquals(Evaluator.Type.NTH_LAST_OF_TYPE, nthLastOfTypeEvaluator.getType());
        assertEquals("li", nthLastOfTypeEvaluator.toString());
        assertEquals(2, ((Evaluator.NthLastOfType) nthLastOfTypeEvaluator).getIndex());
        assertEquals(false, ((Evaluator.NthLastOfType) nthLastOfTypeEvaluator).isOfType());
    }

    @Test
    public void testParseCombinedSelectors() {
        String query = "li.class1:nth-child(2)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        Evaluator elementsEvaluator = ((Evaluator.Elements) evaluator).get(0);
        assertEquals(Evaluator.Type.ELEMENT, elementsEvaluator.getType());
        assertEquals("li", elementsEvaluator.toString());
        Evaluator classAttributeEvaluator = ((Evaluator.Attributes) elementsEvaluator).get(0);
        assertEquals(Evaluator.Type.CLASS, classAttributeEvaluator.getType());
        assertEquals(".class1", classAttributeEvaluator.toString());
        Evaluator nthChildEvaluator = ((Evaluator.Elements) ((Evaluator.Elements) evaluator).get(1)).get(0);
        assertEquals(Evaluator.Type.NTH_CHILD, nthChildEvaluator.getType());
        assertEquals("li", nthChildEvaluator.toString());
        assertEquals(2, ((Evaluator.NthChild) nthChildEvaluator).getIndex());
        assertEquals(false, ((Evaluator.NthChild) nthChildEvaluator).isOfType());
    }

    @Test
    public void testParseAttributeStartsWithSelector() {
        String query = "a[href^='http']";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        Evaluator elementsEvaluator = ((Evaluator.Elements) evaluator).get(0);
        assertEquals(Evaluator.Type.ELEMENT, elementsEvaluator.getType());
        assertEquals("a", elementsEvaluator.toString());
        Evaluator attributeStartsWithEvaluator = ((Evaluator.Attributes) elementsEvaluator).get(0);
        assertEquals(Evaluator.Type.ATTRIBUTE_STARTS_WITH, attributeStartsWithEvaluator.getType());
        assertEquals("href", attributeStartsWithEvaluator.toString());
        assertEquals(AttributeEvaluator.Operator.STARTS_WITH, attributeStartsWithEvaluator.getOperator());
        assertEquals("http", attributeStartsWithEvaluator.getValue());
    }

    @Test
    public void testParseAttributeEndsWithSelector() {
        String query = "a[href$='.pdf']";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        Evaluator elementsEvaluator = ((Evaluator.Elements) evaluator).get(0);
        assertEquals(Evaluator.Type.ELEMENT, elementsEvaluator.getType());
        assertEquals("a", elementsEvaluator.toString());
        Evaluator attributeEndsWithEvaluator = ((Evaluator.Attributes) elementsEvaluator).get(0);
        assertEquals(Evaluator.Type.ATTRIBUTE_ENDS_WITH, attributeEndsWithEvaluator.getType());
        assertEquals("href", attributeEndsWithEvaluator.toString());
        assertEquals(AttributeEvaluator.Operator.ENDS_WITH, attributeEndsWithEvaluator.getOperator());
        assertEquals(".pdf", attributeEndsWithEvaluator.getValue());
    }

    @Test
    public void testParseAttributeContainsSelector() {
        String query = "a[href*='google']";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        Evaluator elementsEvaluator = ((Evaluator.Elements) evaluator).get(0);
        assertEquals(Evaluator.Type.ELEMENT, elementsEvaluator.getType());
        assertEquals("a", elementsEvaluator.toString());
        Evaluator attributeContainsEvaluator = ((Evaluator.Attributes) elementsEvaluator).get(0);
        assertEquals(Evaluator.Type.ATTRIBUTE_CONTAINS, attributeContainsEvaluator.getType());
        assertEquals("href", attributeContainsEvaluator.toString());
        assertEquals(AttributeEvaluator.Operator.CONTAINS, attributeContainsEvaluator.getOperator());
        assertEquals("google", attributeContainsEvaluator.getValue());
    }

    @Test
    public void testParseAttributeMatchingRegexSelector() {
        String query = "input:matches(^[a-zA-Z]+$)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        Evaluator elementsEvaluator = ((Evaluator.Elements) evaluator).get(0);
        assertEquals(Evaluator.Type.ELEMENT, elementsEvaluator.getType());
        assertEquals("input", elementsEvaluator.toString());
        Evaluator attributeMatchingRegexEvaluator = ((Evaluator.Attributes) elementsEvaluator).get(0);
        assertEquals(Evaluator.Type.REGEX_MATCH, attributeMatchingRegexEvaluator.getType());
        assertEquals("^[a-zA-Z]+$", attributeMatchingRegexEvaluator.toString());
    }

    @Test
    public void testParseCombinedAttributeSelectors() {
        String query = "input[type='text'][name^='user']";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        Evaluator elementsEvaluator = ((Evaluator.Elements) evaluator).get(0);
        assertEquals(Evaluator.Type.ELEMENT, elementsEvaluator.getType());
        assertEquals("input", elementsEvaluator.toString());
        Evaluator typeAttributeEvaluator = ((Evaluator.Attributes) elementsEvaluator).get(0);
        assertEquals(Evaluator.Type.ATTRIBUTE, typeAttributeEvaluator.getType());
        assertEquals("type", typeAttributeEvaluator.toString());
        assertEquals(AttributeEvaluator.Operator.EQUALS, typeAttributeEvaluator.getOperator());
        assertEquals("text", typeAttributeEvaluator.getValue());
        Evaluator nameAttributeEvaluator = ((Evaluator.Attributes) ((Evaluator.Attributes) evaluator).get(1)).get(0);
        assertEquals(Evaluator.Type.ATTRIBUTE_STARTS_WITH, nameAttributeEvaluator.getType());
        assertEquals("name", nameAttributeEvaluator.toString());
        assertEquals(AttributeEvaluator.Operator.STARTS_WITH, nameAttributeEvaluator.getOperator());
        assertEquals("user", nameAttributeEvaluator.getValue());
    }

    @Test
    public void testParseNegatedSelector() {
        String query = "input:not([type='text'])";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.NEGATED, evaluator.getType());
        Evaluator negatedEvaluator = ((Evaluator.Negated) evaluator).getEvaluator();
        assertEquals(Evaluator.Type.ELEMENT, negatedEvaluator.getType());
        Evaluator elementsEvaluator = ((Evaluator.Elements) negatedEvaluator).get(0);
        assertEquals(Evaluator.Type.ELEMENT, elementsEvaluator.getType());
        assertEquals("input", elementsEvaluator.toString());
        Evaluator attributeEvaluator = ((Evaluator.Attributes) elementsEvaluator).get(0);
        assertEquals(Evaluator.Type.ATTRIBUTE, attributeEvaluator.getType());
        assertEquals("type", attributeEvaluator.toString());
        assertEquals(AttributeEvaluator.Operator.EQUALS, attributeEvaluator.getOperator());
        assertEquals("text", attributeEvaluator.getValue());
    }

    @Test
    public void testParseComplexQuery() {
        String query = "ul > li > a[href^='http']:not(a:containsOwn('signup'))";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.NEGATED, evaluator.getType());
        Evaluator negatedEvaluator = ((Evaluator.Negated) evaluator).getEvaluator();
        assertEquals(Evaluator.Type.ELEMENT, negatedEvaluator.getType());
        Evaluator elementsEvaluator = ((Evaluator.Elements) negatedEvaluator).get(0);
        assertEquals(Evaluator.Type.ELEMENT, elementsEvaluator.getType());
        assertEquals("a", elementsEvaluator.toString());
        Evaluator attributeContainsOwnEvaluator = ((Evaluator.Attributes) ((Evaluator.Attributes) ((Evaluator.Elements) ((Evaluator.Elements) evaluator).get(0))).get(0)).get(0);
        assertEquals(Evaluator.Type.ATTRIBUTE_CONTAINS_OWN, attributeContainsOwnEvaluator.getType());
        assertEquals("signup", attributeContainsOwnEvaluator.getValue());
        Evaluator attributeStartsWithEvaluator = ((Evaluator.Attributes) elementsEvaluator).get(0);
        assertEquals(Evaluator.Type.ATTRIBUTE_STARTS_WITH, attributeStartsWithEvaluator.getType());
        assertEquals("href", attributeStartsWithEvaluator.toString());
        assertEquals(AttributeEvaluator.Operator.STARTS_WITH, attributeStartsWithEvaluator.getOperator());
        assertEquals("http", attributeStartsWithEvaluator.getValue());
        Evaluator parentElementEvaluator = ((Evaluator.Elements) ((Evaluator.Elements) evaluator).get(1)).get(0);
        assertEquals(Evaluator.Type.ELEMENT, parentElementEvaluator.getType());
        assertEquals("li", parentElementEvaluator.toString());
        Evaluator parentParentElementEvaluator = ((Evaluator.Elements) ((Evaluator.Elements) evaluator).get(2)).get(0);
        assertEquals(Evaluator.Type.ELEMENT, parentParentElementEvaluator.getType());
        assertEquals("ul", parentParentElementEvaluator.toString());
    }
}