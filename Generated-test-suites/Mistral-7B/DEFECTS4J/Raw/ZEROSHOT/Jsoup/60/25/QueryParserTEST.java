package org.jsoup.select.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.jsoup.parser.TokenQueue;

import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;

/**
 * Tests for QueryParser class.
 */
public class QueryParserTest {

    @Test
    public void testParseSimpleSelector() {
        String query = "div.class1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.type);
        assertEquals("div", evaluator.tagName);
        assertEquals("class1", evaluator.className);
    }

    @Test
    public void testParseCompoundSelector() {
        String query = "div.class1 > p.class2";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.type);
        assertEquals("div", evaluator.tagName);
        assertEquals("class1", evaluator.className);
        assertNotNull(evaluator.firstChild);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.firstChild.type);
        assertEquals("p", evaluator.firstChild.tagName);
        assertEquals("class2", evaluator.firstChild.className);
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "div[id='test']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ATTRIBUTE, evaluator.type);
        assertEquals("id", evaluator.attributeName);
        assertEquals("test", evaluator.attributeValue);
    }

    @Test
    public void testParseNegatedSelector() {
        String query = "div:not(.class1)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.NEGATED, evaluator.type);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.negated.type);
        assertEquals("div", evaluator.negated.tagName);
        assertEquals("class1", evaluator.negated.className);
    }

    @Test
    public void testParsePseudoClassSelector() {
        String query = "div:first-child";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.PSEUDO_CLASS, evaluator.type);
        assertEquals("first-child", evaluator.pseudoClass);
    }

    @Test
    public void testParseComplexQuery() {
        String query = "div.class1 > p:first-child:has(a[href='https://www.example.com'])";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.type);
        assertEquals("div", evaluator.tagName);
        assertNotNull(evaluator.firstChild);
        assertEquals(Evaluator.Type.PSEUDO_CLASS, evaluator.firstChild.type);
        assertEquals("first-child", evaluator.firstChild.pseudoClass);
        assertNotNull(evaluator.firstChild.firstChild);
        assertEquals(Evaluator.Type.ATTRIBUTE, evaluator.firstChild.firstChild.type);
        assertEquals("href", evaluator.firstChild.firstChild.attributeName);
        assertEquals("https://www.example.com", evaluator.firstChild.firstChild.attributeValue);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        String query = "div.class1 = p";
        QueryParser parser = new QueryParser(query);
        parser.parse();
    }

    @Test
    public void testParseCustomQuery() {
        String query = "div[data-custom-attribute='test']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ATTRIBUTE, evaluator.type);
        assertEquals("data-custom-attribute", evaluator.attributeName);
        assertEquals("test", evaluator.attributeValue);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidAttributeQuery() {
        String query = "div[data-custom-attribute='test' = 'test2']";
        QueryParser parser = new QueryParser(query);
        parser.parse();
    }

    @Test
    public void testParseComplexAttributeQuery() {
        String query = "div[data-custom-attribute='test'][data-custom-attribute2='test2']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ATTRIBUTE, evaluator.type);
        List<Evaluator> attributes = (List<Evaluator>) evaluator;
        assertEquals(2, attributes.size());
        Evaluator attribute1 = attributes.get(0);
        assertEquals("data-custom-attribute", attribute1.attributeName);
        assertEquals("test", attribute1.attributeValue);
        Evaluator attribute2 = attributes.get(1);
        assertEquals("data-custom-attribute2", attribute2.attributeName);
        assertEquals("test2", attribute2.attributeValue);
    }
}