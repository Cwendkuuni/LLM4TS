package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        Evaluator result = QueryParser.parse("");
        assertNull(result);
    }

    @Test
    public void testParseSingleElementSelector() {
        String query = "div";
        Evaluator result = QueryParser.parse(query);
        assertInstanceOf(Evaluator.Tag.class, result);
        Evaluator.Tag tag = (Evaluator.Tag) result;
        assertEquals("div", tag.getName());
    }

    @Test
    public void testParseClassSelector() {
        String query = ".button";
        Evaluator result = QueryParser.parse(query);
        assertInstanceOf(Evaluator.Class.class, result);
        Evaluator.Class classSelector = (Evaluator.Class) result;
        assertEquals("button", classSelector.getCssClass());
    }

    @Test
    public void testParseIdSelector() {
        String query = "#header";
        Evaluator result = QueryParser.parse(query);
        assertInstanceOf(Evaluator.Id.class, result);
        Evaluator.Id idSelector = (Evaluator.Id) result;
        assertEquals("header", idSelector.getId());
    }

    @Test
    public void testParseCombinedSelectors() {
        String query = "div.button";
        Evaluator result = QueryParser.parse(query);
        Evaluator.And and = (Evaluator.And) result;
        List<Evaluator> children = and.getChildren();
        assertEquals(2, children.size());

        Evaluator tagSelector = children.get(0);
        assertInstanceOf(Evaluator.Tag.class, tagSelector);
        Evaluator.Tag tag = (Evaluator.Tag) tagSelector;
        assertEquals("div", tag.getName());

        Evaluator classSelector = children.get(1);
        assertInstanceOf(Evaluator.Class.class, classSelector);
        Evaluator.Class classSel = (Evaluator.Class) classSelector;
        assertEquals("button", classSel.getCssClass());
    }

    @Test
    public void testParseCombinedSelectorsWithAttribute() {
        String query = "input.error";
        Evaluator result = QueryParser.parse(query);
        Evaluator.And and = (Evaluator.And) result;
        List<Evaluator> children = and.getChildren();
        assertEquals(2, children.size());

        Evaluator tagSelector = children.get(0);
        assertInstanceOf(Evaluator.Tag.class, tagSelector);
        Evaluator.Tag tag = (Evaluator.Tag) tagSelector;
        assertEquals("input", tag.getName());

        Evaluator classSelector = children.get(1);
        assertInstanceOf(Evaluator.Class.class, classSelector);
        Evaluator.Class classSel = (Evaluator.Class) classSelector;
        assertEquals("error", classSel.getCssClass());
    }

    @Test
    public void testParseComplexSelector() {
        String query = "#header .button > input";
        Evaluator result = QueryParser.parse(query);
        Evaluator.And and = (Evaluator.And) result;
        List<Evaluator> children = and.getChildren();
        assertEquals(3, children.size());

        Evaluator idSelector = children.get(0);
        assertInstanceOf(Evaluator.Id.class, idSelector);
        Evaluator.Id id = (Evaluator.Id) idSelector;
        assertEquals("header", id.getId());

        Evaluator classSelector = children.get(1);
        assertInstanceOf(Evaluator.Class.class, classSelector);
        Evaluator.Class classSel = (Evaluator.Class) classSelector;
        assertEquals("button", classSel.getCssClass());

        Evaluator tagSelector = children.get(2);
        assertInstanceOf(Evaluator.Tag.class, tagSelector);
        Evaluator.Tag tag = (Evaluator.Tag) tagSelector;
        assertEquals("input", tag.getName());

        Evaluator parentSelector = ((Evaluator.ImmediateParent) tagSelector).getParent();
        assertSame(children.get(1), parentSelector);
    }

    @Test
    public void testParseComplexSelectorWithPseudoClass() {
        String query = "#header > .error:not(.selected)";
        Evaluator result = QueryParser.parse(query);
        Evaluator.And and = (Evaluator.And) result;
        List<Evaluator> children = and.getChildren();
        assertEquals(3, children.size());

        Evaluator idSelector = children.get(0);
        assertInstanceOf(Evaluator.Id.class, idSelector);
        Evaluator.Id id = (Evaluator.Id) idSelector;
        assertEquals("header", id.getId());

        Evaluator classSelector = children.get(1);
        assertInstanceOf(Evaluator.Class.class, classSelector);
        Evaluator.Class classSel = (Evaluator.Class) classSelector;
        assertEquals("error", classSel.getCssClass());

        Evaluator notSelector = children.get(2);
        assertInstanceOf(Evaluator.Not.class, notSelector);
        Evaluator not = (Evaluator.Not) notSelector;
        Evaluator.And notChildren = (Evaluator.And) not.getChild();
        List<Evaluator> notChildrenList = notChildren.getChildren();
        assertEquals(1, notChildrenList.size());

        Evaluator classNotSelector = notChildrenList.get(0);
        assertInstanceOf(Evaluator.Class.class, classNotSelector);
        Evaluator.Class classNot = (Evaluator.Class) classNotSelector;
        assertEquals("selected", classNot.getCssClass());
    }

    @Test
    public void testParseComplexSelectorWithAttributeSelector() {
        String query = "a[href^='https']";
        Evaluator result = QueryParser.parse(query);

        Evaluator.And and = (Evaluator.And) result;
        List<Evaluator> children = and.getChildren();
        assertEquals(1, children.size());

        Evaluator attributeSelector = children.get(0);
        assertInstanceOf(Evaluator.Attribute.class, attributeSelector);
        Evaluator.Attribute attribute = (Evaluator.Attribute) attributeSelector;
        assertEquals("href", attribute.getName());
        assertTrue(attribute.getValuePattern().matcher("https").matches());
    }

    @Test
    public void testParseComplexSelectorWithAttributeValueContaining() {
        String query = "li:contains(apple)";
        Evaluator result = QueryParser.parse(query);

        Evaluator.And and = (Evaluator.And) result;
        List<Evaluator> children = and.getChildren();
        assertEquals(1, children.size());

        Evaluator containsSelector = children.get(0);
        assertInstanceOf(Evaluator.ContainsText.class, containsSelector);
        Evaluator.ContainsText contains = (Evaluator.ContainsText) containsSelector;
        assertEquals("apple", contains.getText());
    }

    @Test
    public void testParseComplexSelectorWithAttributeValueMatching() {
        String query = "li:matches(^[A-Z].*)";
        Evaluator result = QueryParser.parse(query);

        Evaluator.And and = (Evaluator.And) result;
        List<Evaluator> children = and.getChildren();
        assertEquals(1, children.size());

        Evaluator matchesSelector = children.get(0);
        assertInstanceOf(Evaluator.Matches.class, matchesSelector);
        Evaluator.Matches matches = (Evaluator.Matches) matchesSelector;
        Pattern regex = matches.getPattern();
        assertTrue(regex.matcher("Apple").matches());
        assertTrue(regex.matcher("Banana").doesNotMatch());
    }

    @Test
    public void testParseComplexSelectorWithAttributeValueStarting() {
        String query = "li:matches(^Apple)";
        Evaluator result = QueryParser.parse(query);

        Evaluator.And and = (Evaluator.And) result;
        List<Evaluator> children = and.getChildren();
        assertEquals(1, children.size());

        Evaluator matchesSelector = children.get(0);
        assertInstanceOf(Evaluator.MatchesOwn.class, matchesSelector);
        Evaluator.MatchesOwn matches = (Evaluator.MatchesOwn) matchesSelector;
        Pattern regex = matches.getPattern();
        assertTrue(regex.matcher("Apple").matches());
        assertTrue(regex.matcher("Banana").doesNotMatch());
    }

    @Test
    public void testParseComplexSelectorWithAttributeValueEnding() {
        String query = "li:matches(.*Apple)";
        Evaluator result = QueryParser.parse(query);

        Evaluator.And and = (Evaluator.And) result;
        List<Evaluator> children = and.getChildren();
        assertEquals(1, children.size());

        Evaluator hasTextSelector = children.get(0);
        assertInstanceOf(Evaluator.Matches.class, hasTextSelector);
        Evaluator.Matches matches = (Evaluator.Matches) hasTextSelector;
        Pattern regex = matches.getPattern();
        assertTrue(regex.matcher("Apple").matches());
        assertTrue(regex.matcher("BananaApple").matches());
        assertFalse(regex.matcher("AppleBanana").matches());
    }

    @Test
    public void testParseComplexSelectorWithPseudoElement() {
        String query = "ul > li:first-child { color: red; }";
        Evaluator result = QueryParser.parse(query);

        Evaluator.And and = (Evaluator.And) result;
        List<Evaluator> children = and.getChildren();
        assertEquals(2, children.size());

        Evaluator tagSelector = children.get(0);
        assertInstanceOf(Evaluator.Tag.class, tagSelector);
        Evaluator.Tag tag = (Evaluator.Tag) tagSelector;
        assertEquals("li", tag.getName());

        Evaluator pseudoElementSelector = children.get(1);
        assertInstanceOf(Evaluator.PseudoElement.class, pseudoElementSelector);
        Evaluator.PseudoElement pseudoElement = (Evaluator.PseudoElement) pseudoElementSelector;
        assertEquals("first-child", pseudoElement.getName());
    }

    @Test
    public void testParseComplexSelectorWithDescendantSelector() {
        String query = "ul > li > a";
        Evaluator result = QueryParser.parse(query);

        Evaluator.And and = (Evaluator.And) result;
        List<Evaluator> children = and.getChildren();
        assertEquals(3, children.size());

        Evaluator tagSelector1 = children.get(0);
        assertInstanceOf(Evaluator.Tag.class, tagSelector1);
        Evaluator.Tag tag1 = (Evaluator.Tag) tagSelector1;
        assertEquals("ul", tag1.getName());

        Evaluator tagSelector2 = children.get(1);
        assertInstanceOf(Evaluator.Tag.class, tagSelector2);
        Evaluator.Tag tag2 = (Evaluator.Tag) tagSelector2;
        assertEquals("li", tag2.getName());

        Evaluator tagSelector3 = children.get(2);
        assertInstanceOf(Evaluator.Tag.class, tagSelector3);
        Evaluator.Tag tag3 = (Evaluator.Tag) tagSelector3;
        assertEquals("a", tag3.getName());

        Evaluator.ImmediateAncestor immediateAncestor = ((Evaluator.ImmediateAncestor) tagSelector1).getAncestor();
        assertSame(tagSelector2, immediateAncestor);
    }

    @Test
    public void testParseComplexSelectorWithCombinator() {
        String query = "h1 > ul > li:first-child > a";
        Evaluator result = QueryParser.parse(query);

        Evaluator.And and = (Evaluator.And) result;
        List<Evaluator> children = and.getChildren();
        assertEquals(4, children.size());

        Evaluator tagSelector1 = children.get(0);
        assertInstanceOf(Evaluator.Tag.class, tagSelector1);
        Evaluator.Tag tag1 = (Evaluator.Tag) tagSelector1;
        assertEquals("h1", tag1.getName());

        Evaluator tagSelector2 = children.get(1);
        assertInstanceOf(Evaluator.Tag.class, tagSelector2);
        Evaluator.Tag tag2 = (Evaluator.Tag) tagSelector2;
        assertEquals("ul", tag2.getName());

        Evaluator pseudoElementSelector1 = children.get(2);
        assertInstanceOf(Evaluator.PseudoElement.class, pseudoElementSelector1);
        Evaluator.PseudoElement pseudoElement1 = (Evaluator.PseudoElement) pseudoElementSelector1;
        assertEquals("first-child", pseudoElement1.getName());

        Evaluator tagSelector3 = children.get(3);
        assertInstanceOf(Evaluator.Tag.class, tagSelector3);
        Evaluator.Tag tag3 = (Evaluator.Tag) tagSelector3;
        assertEquals("a", tag3.getName());

        Evaluator.ImmediateAncestor immediateAncestor1 = ((Evaluator.ImmediateAncestor) tagSelector1).getAncestor();
        assertSame(tagSelector2, immediateAncestor1);

        Evaluator.ImmediateAncestor immediateAncestor2 = ((Evaluator.ImmediateAncestor) tagSelector2).getAncestor();
        assertSame(pseudoElementSelector1, immediateAncestor2);
    }

    @Test
    public void testParseComplexSelectorWithAttributeSelectorAndCombinator() {
        String query = "li[data-testid='test-item'] > a";
        Evaluator result = QueryParser.parse(query);

        Evaluator.And and = (Evaluator.And) result;
        List<Evaluator> children = and.getChildren();
        assertEquals(2, children.size());

        Evaluator tagSelector1 = children.get(0);
        assertInstanceOf(Evaluator.Tag.class, tagSelector1);
        Evaluator.Tag tag1 = (Evaluator.Tag) tagSelector1;
        assertEquals("li", tag1.getName());

        Evaluator attributeSelector = children.get(1);
        assertInstanceOf(Evaluator.Attribute.class, attributeSelector);
        Evaluator.Attribute attribute = (Evaluator.Attribute) attributeSelector;
        assertEquals("data-testid", attribute.getName());
        assertEquals("test-item", attribute.getValue());

        Evaluator tagSelector2 = ((Evaluator.ImmediateChild) tagSelector1).getChild();
        assertInstanceOf(Evaluator.Tag.class, tagSelector2);
        Evaluator.Tag tag2 = (Evaluator.Tag) tagSelector2;
        assertEquals("a", tag2.getName());
    }
}