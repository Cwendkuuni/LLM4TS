package org.jsoup.select.test;

import static org.junit.Assert.*;

import org.jsoup.select.QueryParser;

import java.util.List;

import org.junit.Test;

/**
 * Test class for QueryParser
 */
public class QueryParserTest {

    /**
     * Test parse method with empty query
     */
    @Test
    public void testParseEmptyQuery() {
        QueryParser parser = new QueryParser("");
        Evaluator evaluator = parser.parse();
        assertNull(evaluator);
    }

    /**
     * Test parse method with simple query
     */
    @Test
    public void testParseSimpleQuery() {
        String query = "div";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) evaluator).getName());
    }

    /**
     * Test parse method with class query
     */
    @Test
    public void testParseClassQuery() {
        String query = ".myClass";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals("myClass", ((Evaluator.Class) evaluator).getClassname());
    }

    /**
     * Test parse method with id query
     */
    @Test
    public void testParseIdQuery() {
        String query = "#myId";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals("myId", ((Evaluator.Id) evaluator).getId());
    }

    /**
     * Test parse method with tag and class query
     */
    @Test
    public void testParseTagAndClassQuery() {
        String query = "div.myClass";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.And);
        List<Evaluator> andList = ((Evaluator.And) evaluator).getChildren();
        assertTrue(andList.get(0) instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) andList.get(0)).getName());
        assertTrue(andList.get(1) instanceof Evaluator.Class);
        assertEquals("myClass", ((Evaluator.Class) andList.get(1)).getClassname());
    }

    /**
     * Test parse method with tag and id query
     */
    @Test
    public void testParseTagAndIdQuery() {
        String query = "div#myId";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.And);
        List<Evaluator> andList = ((Evaluator.And) evaluator).getChildren();
        assertTrue(andList.get(0) instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) andList.get(0)).getName());
        assertTrue(andList.get(1) instanceof Evaluator.Id);
        assertEquals("myId", ((Evaluator.Id) andList.get(1)).getId());
    }

    /**
     * Test parse method with combinator query
     */
    @Test
    public void testParseCombinatorQuery() {
        String query = "div > ul";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.And);
        List<Evaluator> andList = ((Evaluator.And) evaluator).getChildren();
        assertTrue(andList.get(0) instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) andList.get(0)).getName());
        assertTrue(andList.get(1) instanceof Evaluator.ImmediateChild);
        assertTrue(andList.get(1) instanceof Evaluator.Tag);
        assertEquals("ul", ((Evaluator.Tag) andList.get(1)).getName());
    }

    /**
     * Test parse method with multiple combinators query
     */
    @Test
    public void testParseMultipleCombinatorsQuery() {
        String query = "div > ul > li";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.And);
        List<Evaluator> andList = ((Evaluator.And) evaluator).getChildren();
        assertTrue(andList.get(0) instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) andList.get(0)).getName());
        assertTrue(andList.get(1) instanceof Evaluator.ImmediateChild);
        assertTrue(andList.get(1) instanceof Evaluator.Tag);
        assertEquals("ul", ((Evaluator.Tag) andList.get(1)).getName());
        assertTrue(andList.get(2) instanceof Evaluator.ImmediateChild);
        assertTrue(andList.get(2) instanceof Evaluator.Tag);
        assertEquals("li", ((Evaluator.Tag) andList.get(2)).getName());
    }

    /**
     * Test parse method with attribute query
     */
    @Test
    public void testParseAttributeQuery() {
        String query = "li[class='active']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.And);
        List<Evaluator> andList = ((Evaluator.And) evaluator).getChildren();
        assertTrue(andList.get(0) instanceof Evaluator.Tag);
        assertEquals("li", ((Evaluator.Tag) andList.get(0)).getName());
        assertTrue(andList.get(1) instanceof Evaluator.Attribute);
        assertEquals("class", ((Evaluator.Attribute) andList.get(1)).getAttributeName());
        assertEquals("active", ((Evaluator.Attribute) andList.get(1)).getValue());
    }

    /**
     * Test parse method with multiple attributes query
     */
    @Test
    public void testParseMultipleAttributesQuery() {
        String query = "input[type='text'][name='username']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.And);
        List<Evaluator> andList = ((Evaluator.And) evaluator).getChildren();
        assertTrue(andList.get(0) instanceof Evaluator.Tag);
        assertEquals("input", ((Evaluator.Tag) andList.get(0)).getName());
        List<Evaluator> attributeList = ((Evaluator.And) andList.get(1)).getChildren();
        assertTrue(attributeList.get(0) instanceof Evaluator.Attribute);
        assertEquals("type", ((Evaluator.Attribute) attributeList.get(0)).getAttributeName());
        assertEquals("text", ((Evaluator.Attribute) attributeList.get(0)).getValue());
        assertTrue(attributeList.get(1) instanceof Evaluator.Attribute);
        assertEquals("name", ((Evaluator.Attribute) attributeList.get(1)).getAttributeName());
        assertEquals("username", ((Evaluator.Attribute) attributeList.get(1)).getValue());
    }

    /**
     * Test parse method with negation query
     */
    @Test
    public void testParseNegationQuery() {
        String query = "li:not([class='active'])";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Not);
        Evaluator notEvaluator = ((Evaluator.Not) evaluator).getChild();
        assertTrue(notEvaluator instanceof Evaluator.And);
        List<Evaluator> andList = ((Evaluator.And) notEvaluator).getChildren();
        assertTrue(andList.get(0) instanceof Evaluator.Tag);
        assertEquals("li", ((Evaluator.Tag) andList.get(0)).getName());
        assertTrue(andList.get(1) instanceof Evaluator.Attribute);
        assertEquals("class", ((Evaluator.Attribute) andList.get(1)).getAttributeName());
        assertEquals("active", ((Evaluator.Attribute) andList.get(1)).getValue());
    }

    /**
     * Test parse method with pseudo-class query
     */
    @Test
    public void testParsePseudoClassQuery() {
        String query = "a:hover";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.PseudoClass);
        assertEquals("hover", ((Evaluator.PseudoClass) evaluator).getPseudoClass());
        assertTrue(evaluator.accept(null) == true);
    }

    /**
     * Test parse method with multiple pseudo-classes query
     */
    @Test
    public void testParseMultiplePseudoClassesQuery() {
        String query = "li:first-child:hover";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.And);
        List<Evaluator> andList = ((Evaluator.And) evaluator).getChildren();
        assertTrue(andList.get(0) instanceof Evaluator.PseudoClass);
        assertEquals("first-child", ((Evaluator.PseudoClass) andList.get(0)).getPseudoClass());
        assertTrue(andList.get(1) instanceof Evaluator.PseudoClass);
        assertEquals("hover", ((Evaluator.PseudoClass) andList.get(1)).getPseudoClass());
    }

    /**
     * Test parse method with pseudo-element query
     */
    @Test
    public void testParsePseudoElementQuery() {
        String query = "ul > li:first-child::before";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Combinator);
        Evaluator combinatorEvaluator = ((Evaluator.Combinator) evaluator).getLeftChild();
        assertTrue(combinatorEvaluator instanceof Evaluator.Tag);
        assertEquals("ul", ((Evaluator.Tag) combinatorEvaluator).getName());
        assertTrue(combinatorEvaluator.getNextSibling() instanceof Evaluator.PseudoElement);
        assertEquals("before", ((Evaluator.PseudoElement) combinatorEvaluator.getNextSibling()).getPseudoElement());
        assertTrue(combinatorEvaluator.getNextSibling().getNextSibling() instanceof Evaluator.PseudoClass);
        assertEquals("first-child", ((Evaluator.PseudoClass) combinatorEvaluator.getNextSibling().getNextSibling()).getPseudoClass());
        assertTrue(combinatorEvaluator.getNextSibling().getNextSibling().getNextSibling() instanceof Evaluator.Tag);
        assertEquals("li", ((Evaluator.Tag) combinatorEvaluator.getNextSibling().getNextSibling().getNextSibling()).getName());
    }
}