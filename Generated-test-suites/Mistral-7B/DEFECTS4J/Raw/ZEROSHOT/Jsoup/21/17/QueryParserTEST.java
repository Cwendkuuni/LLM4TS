package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.jsoup.helper.StringUtil;
import org.jsoup.parser.TokenQueue;
import org.junit.Test;

/**
 * Tests for QueryParser class
 */
public class QueryParserTest {

    @Test
    public void testParseSimpleSelector() {
        String query = "div.class1";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        assertEquals(Evaluator.Class.class, evaluator.getClassSpec());
        assertEquals("div", evaluator.getTagName());
        assertEquals("class1", evaluator.getAttributeValue("class"));
    }

    @Test
    public void testParseIdSelector() {
        String query = "#id1";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ID, evaluator.getType());
        assertEquals("id1", evaluator.getId());
    }

    @Test
    public void testParseMultipleClassSelector() {
        String query = "div.class1.class2";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        List<Evaluator.Class> classes = evaluator.getClassSpecList();
        assertEquals(2, classes.size());
        assertEquals("class1", classes.get(0).getValue());
        assertEquals("class2", classes.get(1).getValue());
    }

    @Test
    public void testParseElementSelector() {
        String query = "p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        assertEquals("p", evaluator.getTagName());
    }

    @Test
    public void testParseCombinedSelectors() {
        String query = "div > p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        assertEquals("div", evaluator.getParentSelector().getTagName());
        assertEquals("p", evaluator.getChildElementSelector().getTagName());

        query = "div > p~ ul";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        assertEquals("div", evaluator.getParentSelector().getTagName());
        assertEquals("p", evaluator.getFirstChildSelector().getTagName());
        assertEquals("ul", evaluator.getNextSiblingSelector().getTagName());

        query = "div > p + ul";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        assertEquals("div", evaluator.getParentSelector().getTagName());
        assertEquals("p", evaluator.getFirstChildSelector().getTagName());
        assertEquals("ul", evaluator.getNextSiblingSelector().getTagName());
    }

    @Test
    public void testParseAttributeSelectors() {
        String query = "input[type='text']";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ATTRIBUTE, evaluator.getType());
        assertEquals("type", evaluator.getAttributeName());
        assertEquals("text", evaluator.getAttributeValue());

        query = "input[type='text'][name='username']";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.ATTRIBUTE, evaluator.getType());
        assertEquals("type", evaluator.getAttributeName());
        assertEquals("text", evaluator.getAttributeValue());
        assertEquals("name", evaluator.getSecondAttributeName());
        assertEquals("username", evaluator.getSecondAttributeValue());
    }

    @Test
    public void testParsePseudoClasses() {
        String query = "a:link";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        assertEquals("a", evaluator.getTagName());
        assertEquals(Evaluator.PseudoClass.LINK, evaluator.getPseudoClass());

        query = "a:visited";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        assertEquals("a", evaluator.getTagName());
        assertEquals(Evaluator.PseudoClass.VISITED, evaluator.getPseudoClass());

        query = "input:checked";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        assertEquals("input", evaluator.getTagName());
        assertEquals(Evaluator.PseudoClass.CHECKED, evaluator.getPseudoClass());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        String query = "div.invalid-selector";
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidCombinator() {
        String query = "div ^ p";
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidAttributeSelector() {
        String query = "input[type=text]a";
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidPseudoClass() {
        String query = "a:invalid-pseudo-class";
        QueryParser.parse(query);
    }
}