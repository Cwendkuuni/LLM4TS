package org.jsoup.select;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParseEmpty() {
        Evaluator result = QueryParser.parse("");
        assertNull(result);
    }

    @Test
    public void testParseSingleTag() {
        String query = "p";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof Evaluator.Tag);
        assertEquals("p", ((Evaluator.Tag) result).tagName());
    }

    @Test
    public void testParseSingleTagWithNamespace() {
        String query = "html:body";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof Evaluator.Tag);
        assertEquals("body", ((Evaluator.Tag) result).tagName());
        assertEquals("html", ((Evaluator.Tag) result).namespace());
    }

    @Test
    public void testParseClass() {
        String query = ".class1";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof Evaluator.Class);
        assertEquals("class1", ((Evaluator.Class) result).className());
    }

    @Test
    public void testParseId() {
        String query = "#id1";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof Evaluator.Id);
        assertEquals("id1", ((Evaluator.Id) result).id());
    }

    @Test
    public void testParseAttribute() {
        String query = "[title]";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof Evaluator.Attribute);
        assertEquals("title", ((Evaluator.Attribute) result).attributeName());
    }

    @Test
    public void testParseAttributeWithValue() {
        String query = "[title='foo']";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof Evaluator.AttributeWithValue);
        assertEquals("title", ((Evaluator.AttributeWithValue) result).attributeName());
        assertEquals("foo", ((Evaluator.AttributeWithValue) result).attributeValue());
    }

    @Test
    public void testParseAttributeWithValueStarting() {
        String query = "[title^='foo']";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof Evaluator.AttributeWithValueStarting);
        assertEquals("title", ((Evaluator.AttributeWithValueStarting) result).attributeName());
        assertEquals("foo", ((Evaluator.AttributeWithValueStarting) result).prefix());
    }

    @Test
    public void testParseAttributeWithValueEnding() {
        String query = "[title$='foo']";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof Evaluator.AttributeWithValueEnding);
        assertEquals("title", ((Evaluator.AttributeWithValueEnding) result).attributeName());
        assertEquals("foo", ((Evaluator.AttributeWithValueEnding) result).suffix());
    }

    @Test
    public void testParseAttributeWithValueContaining() {
        String query = "[title*='foo']";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof Evaluator.AttributeWithValueContaining);
        assertEquals("title", ((Evaluator.AttributeWithValueContaining) result).attributeName());
        assertEquals("foo", ((Evaluator.AttributeWithValueContaining) result).substring());
    }

    @Test
    public void testParseAttributeWithValueMatching() {
        String query = "[title~='foo']";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof Evaluator.AttributeWithValueMatching);
        assertEquals("title", ((Evaluator.AttributeWithValueMatching) result).attributeName());
        assertEquals(Pattern.compile("foo"), ((Evaluator.AttributeWithValueMatching) result).regex());
    }

    @Test
    public void testParseMultipleSelectors() {
        String query = "p.class1";
        Evaluator result = QueryParser.parse(query);
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("p"));
        expected.add(new Evaluator.Class("class1"));
        assertEquals(expected, result.children());
    }

    @Test
    public void testParseMultipleSelectorsWithNamespaces() {
        String query = "html p.class1";
        Evaluator result = QueryParser.parse(query);
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("p"));
        expected.add(new Evaluator.Namespace("html"));
        expected.add(new Evaluator.Class("class1"));
        assertEquals(expected, result.children());
    }

    @Test
    public void testParseCombinedSelectors() {
        String query = "p > ul > li";
        Evaluator result = QueryParser.parse(query);
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("p"));
        expected.add(new CombiningEvaluator.Descendant(new Evaluator.Tag("ul"), new Evaluator.Tag("li")));
        assertEquals(expected, result.children());
    }

    @Test
    public void testParseCombinedSelectorsWithAttributes() {
        String query = "p > ul > li[class='class1']";
        Evaluator result = QueryParser.parse(query);
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("p"));
        expected.add(new CombiningEvaluator.Descendant(new Evaluator.Tag("ul"), new CombiningEvaluator.And(
                new Evaluator.Tag("li"), new Evaluator.Class("class1"))));
        assertEquals(expected, result.children());
    }

    @Test
    public void testParsePseudoClasses() {
        String query = "p:first-child";
        Evaluator result = QueryParser.parse(query);
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.IsFirstChild());
        expected.add(new Evaluator.Tag("p"));
        assertEquals(expected, result.children());
    }

    @Test
    public void testParsePseudoClassesWithNamespaces() {
        String query = "html p:first-child";
        Evaluator result = QueryParser.parse(query);
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Namespace("html"));
        expected.add(new Evaluator.IsFirstChild());
        expected.add(new Evaluator.Tag("p"));
        assertEquals(expected, result.children());
    }

    @Test
    public void testParsePseudoClassesWithCombinedSelectors() {
        String query = "p:first-child > ul > li";
        Evaluator result = QueryParser.parse(query);
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Namespace("html")); // assuming the document is an HTML document
        expected.add(new Evaluator.Tag("p"));
        expected.add(new CombiningEvaluator.Descendant(new Evaluator.Tag("ul"), new Evaluator.Tag("li")));
        assertEquals(expected, result.children());
    }

    @Test
    public void testParseNegatedSelectors() {
        String query = "p:not(.class1)";
        Evaluator result = QueryParser.parse(query);
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("p"));
        expected.add(new StructuralEvaluator.Not(new Evaluator.Class("class1")));
        assertEquals(expected, result.children());
    }

    @Test
    public void testParseComplexQuery() {
        String query = "#id1 > .class1:not(.class2) > ul > li.class3:first-child";
        Evaluator result = QueryParser.parse(query);
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Id("id1"));
        expected.add(new Evaluator.Tag("p")); // assuming "p" is the parent of "#id1"
        expected.add(new CombiningEvaluator.And(
                new Evaluator.Class("class1"),
                new StructuralEvaluator.Not(new Evaluator.Class("class2"))));
        expected.add(new CombiningEvaluator.Descendant(new Evaluator.Tag("ul"), new Evaluator.Class("class3")));
        expected.add(new Evaluator.IsFirstChild());
        assertEquals(expected, result.children());
    }

    @Test
    public void testParseInvalidQuery() {
        try {
            QueryParser.parse("p:not:class");
            fail("Expected SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            // pass
        }
    }

    @Test
    public void testParseInvalidQueryWithMissingClosingParenthesis() {
        try {
            QueryParser.parse("p:has(li[class=foo]");
            fail("Expected SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            // pass
        }
    }

    @Test
    public void testParseInvalidQueryWithMissingOpeningParenthesis() {
        try {
            QueryParser.parse(":has(p:first-child)");
            fail("Expected SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            // pass
        }
    }

    @Test
    public void testParseInvalidQueryWithInvalidCombinator() {
        try {
            QueryParser.parse("p:apple:first-child");
            fail("Expected SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            // pass
        }
    }

    @Test
    public void testParseInvalidQueryWithMissingAttributeValue() {
        try {
            QueryParser.parse("[class]");
            fail("Expected SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            // pass
        }
    }

    @Test
    public void testParseInvalidQueryWithMissingAttributeName() {
        try {
            QueryParser.parse("[=value]");
            fail("Expected SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            // pass
        }
    }
}