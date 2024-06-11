package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParseSimpleSelectors() {
        String query = "tag";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Tag.class, evaluator.getClass());
        assertEquals("tag", evaluator.toString());

        query = "tag.class";
        evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals("tag.class", evaluator.toString());

        query = "tag#id";
        evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals("tag#id", evaluator.toString());

        query = "tag.class:first-child";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.IsFirstChild.class, evaluator.getClass());
        assertEquals("tag.class:first-child", evaluator.toString());

        query = "tag:last-child";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.IsLastChild.class, evaluator.getClass());
        assertEquals("tag:last-child", evaluator.toString());
    }

    @Test
    public void testParseAttributeSelectors() {
        String query = "tag[attr]";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Attribute);
        assertEquals("tag[attr]", evaluator.toString());

        query = "tag[attr=value]";
        evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
        assertEquals("tag[attr=value]", evaluator.toString());

        query = "tag[attr!='value']";
        evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueNot);
        assertEquals("tag[attr!='value']", evaluator.toString());

        query = "tag[attr^='value']";
        evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueStarting);
        assertEquals("tag[attr^='value']", evaluator.toString());

        query = "tag[attr$='value']";
        evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueEnding);
        assertEquals("tag[attr$='value']", evaluator.toString());

        query = "tag[attr*='value']";
        evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueContaining);
        assertEquals("tag[attr*='value']", evaluator.toString());

        query = "tag[attr~='value']";
        evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueMatching);
        assertEquals("tag[attr~='value']", evaluator.toString());
    }

    @Test
    public void testParseCombinedSelectors() {
        String query = "tag > div";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.class, evaluator.getClass());
        assertEquals("tag > div", evaluator.toString());

        evaluator = ((CombiningEvaluator) evaluator).getLeft();
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("tag", evaluator.toString());

        evaluator = ((StructuralEvaluator) evaluator).getParent();
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals("div", evaluator.toString());

        query = "tag + div";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.class, evaluator.getClass());
        assertEquals("tag + div", evaluator.toString());

        evaluator = ((CombiningEvaluator) evaluator).getLeft();
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("tag", evaluator.toString());

        evaluator = ((StructuralEvaluator) evaluator).getPreviousSibling();
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals("div", evaluator.toString());

        query = "tag ~ div";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.class, evaluator.getClass());
        assertEquals("tag ~ div", evaluator.toString());

        evaluator = ((CombiningEvaluator) evaluator).getLeft();
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("tag", evaluator.toString());

        List<Evaluator> siblings = ((StructuralEvaluator) evaluator).getPreviousSiblings();
        assertEquals(1, siblings.size());
        assertTrue(siblings.get(0) instanceof Evaluator.Class);
        assertEquals("div", siblings.get(0).toString());
    }

    @Test
    public void testParsePseudoClasses() {
        String query = "tag:first-child";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.IsFirstChild.class, evaluator.getClass());
        assertEquals("tag:first-child", evaluator.toString());

        query = "tag:last-child";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.IsLastChild.class, evaluator.getClass());
        assertEquals("tag:last-child", evaluator.toString());

        query = "tag:only-child";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.IsOnlyChild.class, evaluator.getClass());
        assertEquals("tag:only-child", evaluator.toString());

        query = "tag:only-of-type";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.IsOnlyOfType.class, evaluator.getClass());
        assertEquals("tag:only-of-type", evaluator.toString());

        query = "tag:empty";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.IsEmpty.class, evaluator.getClass());
        assertEquals("tag:empty", evaluator.toString());

        query = "tag:root";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.IsRoot.class, evaluator.getClass());
        assertEquals("tag:root", evaluator.toString());
    }

    @Test
    public void testParseCombinedPseudoClasses() {
        String query = "tag:not(.class)";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.class, evaluator.getClass());
        assertEquals("tag:not(.class)", evaluator.toString());

        evaluator = ((StructuralEvaluator) evaluator).getLeft();
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("tag", evaluator.toString());

        evaluator = ((StructuralEvaluator.Not) evaluator).getRight();
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals(".class", evaluator.toString());

        query = "tag:has(#id)";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.class, evaluator.getClass());
        assertEquals("tag:has(#id)", evaluator.toString());

        evaluator = ((StructuralEvaluator) evaluator).getLeft();
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("tag", evaluator.toString());

        evaluator = ((StructuralEvaluator.Has) evaluator).getRight();
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals("#id", evaluator.toString());
    }

    @Test
    public void testParseComplexSelectors() {
        String query = "div > ul > li > a[href] > input[type='submit']";
        Evaluator evaluator = QueryParser.parse(query);

        assertEquals(Evaluator.class, evaluator.getClass());
        assertEquals("div > ul > li > a[href] > input[type='submit']", evaluator.toString());

        evaluator = ((CombiningEvaluator) evaluator).getLeft();
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("div", evaluator.toString());

        evaluator = ((StructuralEvaluator) evaluator).getChildren();
        assertEquals(1, evaluator.size());
        evaluator = evaluator.get(0);
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("ul", evaluator.toString());

        evaluator = ((StructuralEvaluator) evaluator).getChildren();
        assertEquals(1, evaluator.size());
        evaluator = evaluator.get(0);
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("li", evaluator.toString());

        evaluator = ((StructuralEvaluator) evaluator).getChildren();
        assertEquals(1, evaluator.size());
        evaluator = evaluator.get(0);
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("a", evaluator.toString());

        evaluator = ((StructuralEvaluator) evaluator).getChildren();
        assertEquals(1, evaluator.size());
        evaluator = evaluator.get(0);
        assertTrue(evaluator instanceof Evaluator.Attribute);
        assertEquals("href", evaluator.toString());

        evaluator = ((StructuralEvaluator) evaluator).getChildren();
        assertEquals(1, evaluator.size());
        evaluator = evaluator.get(0);
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("input", evaluator.toString());

        evaluator = ((StructuralEvaluator) evaluator).getAttributes();
        assertEquals(1, evaluator.size());
        evaluator = evaluator.get(0);
        assertTrue(evaluator instanceof Evaluator.Attribute);
        assertEquals("type", evaluator.toString());
        assertEquals("submit", evaluator.getValues().get(0).toString());
    }

    @Test
    public void testParseInvalidSelectors() {
        String query = "tag:invalid";
        try {
            QueryParser.parse(query);
            fail("Expected SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Unknown pseudoclass: invalid", e.getMessage());
        }

        query = "tag[invalid]";
        try {
            QueryParser.parse(query);
            fail("Expected SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Unknown attribute: invalid", e.getMessage());
        }

        query = "tag[attr=invalid]";
        try {
            QueryParser.parse(query);
            fail("Expected SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Invalid value for attribute 'attr': invalid", e.getMessage());
        }

        query = "tag[attr=value] > invalid";
        try {
            QueryParser.parse(query);
            fail("Expected SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Unknown selector: invalid", e.getMessage());
        }

        query = "tag > invalid > ul";
        try {
            QueryParser.parse(query);
            fail("Expected SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Unknown selector: invalid", e.getMessage());
        }

        query = "tag > ul > li > input[type=invalid]";
        try {
            QueryParser.parse(query);
            fail("Expected SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Invalid value for attribute 'type': invalid", e.getMessage());
        }
    }
}