package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;

import java.util.Arrays;
import java.util.List;

public class QueryParserTest {

    @Test
    public void testParseSimpleSelector() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.TAG, evaluator.getType());
        assertEquals("div", evaluator.toString());
    }

    @Test
    public void testParseSimpleAttributeSelector() {
        String query = "div[class='foo']";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ATTRIBUTE, evaluator.getType());
        assertEquals("div[class='foo']", evaluator.toString());
    }

    @Test
    public void testParseClassSelector() {
        String query = ".foo";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.CLASS, evaluator.getType());
        assertEquals(".foo", evaluator.toString());
    }

    @Test
    public void testParseIdSelector() {
        String query = "#foo";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ID, evaluator.getType());
        assertEquals("#foo", evaluator.toString());
    }

    @Test
    public void testParseTagWithAttributeSelector() {
        String query = "div[class='foo' id='bar']";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.TAG, evaluator.getType());
        assertEquals("div[class='foo' id='bar']", evaluator.toString());
    }

    @Test
    public void testParseCombinedSelectors() {
        String query = "div.foo.bar";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.TAG, evaluator.getType());
        assertEquals("div.foo.bar", evaluator.toString());

        query = "div#foo.bar";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.TAG, evaluator.getType());
        assertEquals("div#foo.bar", evaluator.toString());

        query = "div.foo#bar";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.TAG, evaluator.getType());
        assertEquals("div.foo#bar", evaluator.toString());

        query = "div.foo#bar.baz";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.TAG, evaluator.getType());
        assertEquals("div.foo#bar.baz", evaluator.toString());
    }

    @Test
    public void testParseAttributeWithValueSelector() {
        String query = "div[class='foo']";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ATTRIBUTE, evaluator.getType());
        assertEquals("div[class='foo']", evaluator.toString());

        query = "div[class='foo'][id='bar']";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.AND, evaluator.getType());
        List<Evaluator> children = ((CombiningEvaluator.And) evaluator).getChildren();
        assertEquals(2, children.size());
        Evaluator firstChild = children.get(0);
        assertEquals(Evaluator.Type.ATTRIBUTE, firstChild.getType());
        assertEquals("div[class='foo']", firstChild.toString());
        Evaluator secondChild = children.get(1);
        assertEquals(Evaluator.Type.ID, secondChild.getType());
        assertEquals("div[class='foo'][id='bar']", evaluator.toString());
    }

    @Test
    public void testParseAttributeWithValueAndOperatorSelector() {
        String query = "div[class='foo'][class='bar']";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.AND, evaluator.getType());
        List<Evaluator> children = ((CombiningEvaluator.And) evaluator).getChildren();
        assertEquals(2, children.size());
        Evaluator firstChild = children.get(0);
        assertEquals(Evaluator.Type.ATTRIBUTE, firstChild.getType());
        assertEquals("div[class='foo']", firstChild.toString());
        Evaluator secondChild = children.get(1);
        assertEquals(Evaluator.Type.ATTRIBUTE, secondChild.getType());
        assertEquals("div[class='bar']", secondChild.toString());
        assertEquals("div[class='foo'][class='bar']", evaluator.toString());

        query = "div[class='foo'][class!='bar']";
        evaluator = QueryParser.parse(query);
        assertEquals("div[class='foo']:not([class='bar'])", evaluator.toString());
    }

    @Test
    public void testParseAttributeWithValueContainingSelector() {
        String query = "div[class*='foo']";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ATTRIBUTE, evaluator.getType());
        assertEquals("div[class*='foo']", evaluator.toString());
    }

    @Test
    public void testParseAttributeWithValueMatchingSelector() {
        String query = "div[class~='foo']";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ATTRIBUTE, evaluator.getType());
        assertEquals("div[class~='foo']", evaluator.toString());

        query = "div[class~='foo bar']";
        evaluator = QueryParser.parse(query);
        assertEquals("div.foo.bar", evaluator.toString());
    }

    @Test
    public void testParsePseudoElements() {
        String query = ":root";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.PSEUDO_ELEMENT, evaluator.getType());
        assertEquals(":root", evaluator.toString());

        query = ":first-child";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.PSEUDO_CLASS, evaluator.getType());
        assertEquals(":first-child", evaluator.toString());

        query = ":last-child";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.PSEUDO_CLASS, evaluator.getType());
        assertEquals(":last-child", evaluator.toString());

        query = ":only-child";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.PSEUDO_CLASS, evaluator.getType());
        assertEquals(":only-child", evaluator.toString());

        query = ":only-of-type";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.PSEUDO_CLASS, evaluator.getType());
        assertEquals(":only-of-type", evaluator.toString());

        query = ":empty";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.PSEUDO_CLASS, evaluator.getType());
        assertEquals(":empty", evaluator.toString());

        query = ":nth-child(1)";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.PSEUDO_CLASS, evaluator.getType());
        assertEquals(":nth-child(1)", evaluator.toString());

        query = ":nth-last-child(1)";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.PSEUDO_CLASS, evaluator.getType());
        assertEquals(":nth-last-child(1)", evaluator.toString());

        query = ":nth-of-type(1)";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.PSEUDO_CLASS, evaluator.getType());
        assertEquals(":nth-of-type(1)", evaluator.toString());

        query = ":nth-last-of-type(1)";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.PSEUDO_CLASS, evaluator.getType());
        assertEquals(":nth-last-of-type(1)", evaluator.toString());
    }

    @Test
    public void testParseAttributeWithValueContainingData() {
        String query = "div[data-foo*='bar']";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ATTRIBUTE, evaluator.getType());
        assertEquals("div[data-foo*='bar']", evaluator.toString());
    }

    @Test
    public void testParseAttributeWithValueMatchingData() {
        String query = "div[data-foo='bar']";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ATTRIBUTE, evaluator.getType());
        assertEquals("div[data-foo='bar']", evaluator.toString());
    }

    @Test
    public void testParseDescendantSelectors() {
        String query = "div ul li";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.DESCENDANT, evaluator.getType());
        assertEquals("div > ul > li", evaluator.toString());
    }

    @Test
    public void testParseChildSelectors() {
        String query = "div > ul > li";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.CHILD, evaluator.getType());
        assertEquals("div > ul > li", evaluator.toString());
    }

    @Test
    public void testParseAdjacentSiblingSelectors() {
        String query = "div > ul > li + p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.ADJACENT_SIBLING, evaluator.getType());
        assertEquals("div > ul > li, div > ul > li + p", evaluator.toString());
    }

    @Test
    public void testParseGeneralSiblingSelectors() {
        String query = "div > ul > li ~ p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.GENERAL_SIBLING, evaluator.getType());
        assertEquals("div > ul > li, div > ul > li ~ p", evaluator.toString());
    }

    @Test
    public void testParseNegativeSelectors() {
        String query = "div:not(ul) > li";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals(Evaluator.Type.NEGATION, evaluator.getType());
        assertEquals("div > li:not(ul)", evaluator.toString());
    }

    @Test
    public void testParseComplexSelectors() {
        String query = "div > ul > li#foo:not(.bar):first-child > a[href^='http://']";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertEquals("div > ul > li#foo:not(.bar):first-child > a[href^='http://']", evaluator.toString());

        query = "div > ul > li#foo:not(.bar):first-child > a[href^='http://'][href$='.pdf']";
        evaluator = QueryParser.parse(query);
        assertEquals("div > ul > li#foo:not(.bar):first-child > a[href^='http://'][href$='.pdf']", evaluator.toString());

        query = "div > ul > li#foo:not(.bar):first-child > a[href^='http://'][href$='.pdf'] ~ ul > li";
        evaluator = QueryParser.parse(query);
        assertEquals("div > ul > li#foo:not(.bar):first-child > a[href^='http://'][href$='.pdf'] ~ ul > li", evaluator.toString());
    }

    @Test
    public void testParseComplexSelectorsWithCombinators() {
        String query = "div > ul > li:first-child:not(.bar) > a[href^='http://'][href$='.pdf'] ~ ul > li:first-child";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals("div > ul > li:first-child:not(.bar) > a[href^='http://'][href$='.pdf'] ~ ul > li:first-child", evaluator.toString());

        query = "div > ul > li:first-child:not(.bar) + ul > li:first-child";
        evaluator = QueryParser.parse(query);
        assertEquals("div > ul > li:first-child:not(.bar) + ul > li:first-child", evaluator.toString());

        query = "div > ul > li:first-child:not(.bar) ~ ul > li:first-child + ul > li:first-child";
        evaluator = QueryParser.parse(query);
        assertEquals("div > ul > li:first-child:not(.bar) ~ ul > li:first-child + ul > li:first-child", evaluator.toString());

        query = "div > ul > li:first-child:not(.bar) + ul > li:first-child ~ ul > li:first-child";
        evaluator = QueryParser.parse(query);
        assertEquals("div > ul > li:first-child:not(.bar) + ul > li:first-child ~ ul > li:first-child", evaluator.toString());
    }
}