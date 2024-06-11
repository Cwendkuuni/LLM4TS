package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.StringUtil;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        Evaluator evaluator = QueryParser.parse("");
        assertNull(evaluator);
    }

    @Test
    public void testParseSingleElement() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.TAG, evaluator.getType());
        assertEquals(Evaluator.Tag.DIV, ((Evaluator.Tag) evaluator).getTag());
    }

    @Test
    public void testParseElementWithId() {
        String query = "#header";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ID, evaluator.getType());
        assertEquals("header", evaluator.getValue());
    }

    @Test
    public void testParseElementWithClass() {
        String query = ".button";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.CLASS, evaluator.getType());
        assertEquals("button", evaluator.getValue());
    }

    @Test
    public void testParseElementWithTagAndClass() {
        String query = "input.text";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.CLASS, evaluator.getType());
        assertEquals("text", evaluator.getValue());
        assertEquals(Evaluator.Tag.INPUT, ((Evaluator.Tag) evaluator.getNext()).getTag());
    }

    @Test
    public void testParseElementWithAttribute() {
        String query = "[href]";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ATTR, evaluator.getType());
        assertEquals("href", evaluator.getValue());
    }

    @Test
    public void testParseElementWithAttributeValue() {
        String query = "[href='http://example.com']";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ATTR_VALUE, evaluator.getType());
        assertEquals("http://example.com", evaluator.getValue());
    }

    @Test
    public void testParseElementWithMultipleSelectors() {
        String query = "div.button:first-child";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.AND, evaluator.getType());
        Evaluator tagEvaluator = evaluator.getFirstChild();
        assertEquals(Evaluator.Type.TAG, tagEvaluator.getType());
        assertEquals(Evaluator.Tag.DIV, tagEvaluator.getTag());
        Evaluator classEvaluator = tagEvaluator.getNext();
        assertEquals(Evaluator.Type.CLASS, classEvaluator.getType());
        assertEquals("button", classEvaluator.getValue());
        Evaluator combinatorEvaluator = classEvaluator.getNext();
        assertEquals(Evaluator.Type.COMBINATOR, combinatorEvaluator.getType());
        assertEquals(":first-child", combinatorEvaluator.getValue());
    }

    @Test
    public void testParseElementWithPseudoClass() {
        String query = "input:text";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.PSEUDO_CLASS, evaluator.getType());
        assertEquals("text", evaluator.getValue());
        assertEquals(Evaluator.Tag.INPUT, ((Evaluator.Tag) evaluator.getNext()).getTag());
    }

    @Test
    public void testParseElementWithPseudoClassAndAttribute() {
        String query = "input:disabled[type=text]";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.AND, evaluator.getType());
        Evaluator pseudoClassEvaluator = evaluator.getFirstChild();
        assertEquals(Evaluator.Type.PSEUDO_CLASS, pseudoClassEvaluator.getType());
        assertEquals("disabled", pseudoClassEvaluator.getValue());
        Evaluator combinatorEvaluator = pseudoClassEvaluator.getNext();
        assertEquals(Evaluator.Type.COMBINATOR, combinatorEvaluator.getType());
        assertEquals(" ", combinatorEvaluator.getValue());
        Evaluator attributeEvaluator = combinatorEvaluator.getNext();
        assertEquals(Evaluator.Type.ATTR, attributeEvaluator.getType());
        assertEquals("type", attributeEvaluator.getValue());
        Evaluator attributeValueEvaluator = attributeEvaluator.getNext();
        assertEquals(Evaluator.Type.ATTR_VALUE, attributeValueEvaluator.getType());
        assertEquals("text", attributeValueEvaluator.getValue());
    }

    @Test
    public void testParseElementWithPseudoElement() {
        String query = "ul > li:first-child::before";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.AND, evaluator.getType());
        Evaluator tagEvaluator = evaluator.getFirstChild();
        assertEquals(Evaluator.Type.TAG, tagEvaluator.getType());
        assertEquals(Evaluator.Tag.UL, tagEvaluator.getTag());
        Evaluator combinatorEvaluator = tagEvaluator.getNext();
        assertEquals(Evaluator.Type.COMBINATOR, combinatorEvaluator.getType());
        assertEquals(">", combinatorEvaluator.getValue());
        Evaluator childEvaluator = combinatorEvaluator.getNext();
        assertEquals(Evaluator.Type.ELEMENT_CHILD, childEvaluator.getType());
        Evaluator pseudoElementEvaluator = childEvaluator.getNext();
        assertEquals(Evaluator.Type.PSEUDO_ELEMENT, pseudoElementEvaluator.getType());
        assertEquals(":before", pseudoElementEvaluator.getValue());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        String query = "div=x";
        QueryParser.parse(query);
    }
}