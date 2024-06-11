package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        String query = "";
        Evaluator evaluator = QueryParser.parse(query);
        assertNull(evaluator);
    }

    @Test
    public void testParseSingleTag() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) evaluator).getName());
    }

    @Test
    public void testParseSingleClass() {
        String query = ".myClass";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals("myClass", ((Evaluator.Class) evaluator).getCssName());
    }

    @Test
    public void testParseSingleId() {
        String query = "#myId";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals("myId", ((Evaluator.Id) evaluator).getId());
    }

    @Test
    public void testParseCombinedSelectors() {
        String query = "div.myClass";
        List<Evaluator> evaluators = QueryParser.parse(query).getChildren();
        assertEquals(2, evaluators.size());
        assertTrue(evaluators.get(0) instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) evaluators.get(0)).getName());
        assertTrue(evaluators.get(1) instanceof Evaluator.Class);
        assertEquals("myClass", ((Evaluator.Class) evaluators.get(1)).getCssName());
    }

    @Test
    public void testParseDescendantSelectors() {
        String query = "div > p";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        List<Evaluator> children = ((CombiningEvaluator.And) evaluator).getChildren();
        assertEquals(2, children.size());
        assertTrue(children.get(0) instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) children.get(0)).getName());
        assertTrue(children.get(1) instanceof StructuralEvaluator.ImmediateChild);
        assertEquals("p", ((StructuralEvaluator.ImmediateChild) children.get(1)).getChildName());
    }

    @Test
    public void testParseAttributeSelectors() {
        String query = "div[class='myClass']";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Attribute);
        assertEquals("class", ((Evaluator.Attribute) evaluator).getAttributeName());
        assertEquals("myClass", ((Evaluator.Attribute) evaluator).getValue());
    }

    @Test
    public void testParsePseudoSelectors() {
        String query = "div:has(p)";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof StructuralEvaluator.Has);
        assertEquals("p", ((StructuralEvaluator.Has) evaluator).getChildSelectors().get(0));
    }

    @Test
    public void testParseNegatedPseudoSelectors() {
        String query = "div:not(.myClass)";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof StructuralEvaluator.Not);
        assertEquals("div", ((StructuralEvaluator.Not) evaluator).getChild());
        assertTrue(((StructuralEvaluator.Not) evaluator).getChild() instanceof Evaluator.Class);
        assertEquals("myClass", ((Evaluator.Class) ((StructuralEvaluator.Not) evaluator).getChild()).getCssName());
    }

    @Test
    public void testParseCombinators() { {
        String query = "div > p + ul";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        List<Evaluator> children = ((CombiningEvaluator.And) evaluator).getChildren();
        assertEquals(3, children.size());
        assertTrue(children.get(0) instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) children.get(0)).getName());
        assertTrue(children.get(1) instanceof StructuralEvaluator.ImmediateChild);
        assertEquals("p", ((StructuralEvaluator.ImmediateChild) children.get(1)).getChildName());
        assertTrue(children.get(2) instanceof StructuralEvaluator.ImmediatePreviousSibling);
        assertEquals("ul", ((StructuralEvaluator.ImmediatePreviousSibling) children.get(2)).getSiblingName());
    }
    }

    @Test
    public void testParseInvalidQuery() {
        String query = "div #myId > p + ul:not(div)";
        assertThrows(Selector.SelectorParseException.class, () -> QueryParser.parse(query));
    }
}