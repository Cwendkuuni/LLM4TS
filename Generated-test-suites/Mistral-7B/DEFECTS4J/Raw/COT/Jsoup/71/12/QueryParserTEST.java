package org.jsoup.select;

import static org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;

import java.util.ArrayList;
import java.util.List;

public class QueryParserTest {

    @org.junit.Test
    public void testParse_emptyQuery() {
        QueryParser parser = new QueryParser("");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.class, evaluator.getClass());
        assertTrue(evaluator.isEmpty());
    }

    @org.junit.Test
    public void testParse_tagSelector() {
        String query = "div";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.Tag.class, evaluator.getClass());
        assertEquals("div", ((Evaluator.Tag) evaluator).tagName());
    }

    @org.junit.Test
    public void testParse_classSelector() {
        String query = ".class";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.Class.class, evaluator.getClass());
        assertEquals("class", ((Evaluator.Class) evaluator).className());
    }

    @org.junit.Test
    public void testParse_idSelector() {
        String query = "#id";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.Id.class, evaluator.getClass());
        assertEquals("id", ((Evaluator.Id) evaluator).id());
    }

    @org.junit.Test
    public void testParse_attributeSelector() {
        String query = "[attribute]";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.Attribute.class, evaluator.getClass());
        assertEquals("attribute", ((Evaluator.Attribute) evaluator).attributeName());
    }

    @org.junit.Test
    public void testParse_attributeWithValueSelector() {
        String query = "[attribute=value]";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.AttributeWithValue.class, evaluator.getClass());
        assertEquals("attribute", ((Evaluator.AttributeWithValue) evaluator).attributeName());
        assertEquals("value", ((Evaluator.AttributeWithValue) evaluator).attributeValue());
    }

    @org.junit.Test
    public void testParse_descendantSelector() {
        String query = "div p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertEquals(2, ((CombiningEvaluator.And) evaluator).getChildren().size());
        Evaluator firstChild = ((CombiningEvaluator.And) evaluator).getChildren().get(0);
        assertEquals(Evaluator.Tag.class, firstChild.getClass());
        assertEquals("div", ((Evaluator.Tag) firstChild).tagName());
        Evaluator secondChild = ((CombiningEvaluator.And) evaluator).getChildren().get(1);
        assertEquals(Evaluator.Tag.class, secondChild.getClass());
        assertEquals("p", ((Evaluator.Tag) secondChild).tagName());
    }

    @org.junit.Test
    public void testParse_childSelector() {
        String query = "div > p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof StructuralEvaluator.ImmediateChild);
        assertEquals(Evaluator.Tag.class, ((StructuralEvaluator.ImmediateChild) evaluator).getChildTag().getClass());
        assertEquals("p", ((StructuralEvaluator.ImmediateChild) evaluator).getChildTag().tagName());
    }

    @org.junit.Test
    public void testParse_adjacentSiblingSelector() {
        String query = "div + p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof StructuralEvaluator.AdjacentSibling);
        assertEquals(Evaluator.Tag.class, ((StructuralEvaluator.AdjacentSibling) evaluator).getPreviousTag().getClass());
        assertEquals("p", ((StructuralEvaluator.AdjacentSibling) evaluator).getPreviousTag().tagName());
    }

    @org.junit.Test
    public void testParse_generalSiblingSelector() {
        String query = "div ~ p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof StructuralEvaluator.PreviousSibling);
        assertEquals(Evaluator.Tag.class, ((StructuralEvaluator.PreviousSibling) evaluator).getPreviousTag().getClass());
        assertEquals("p", ((StructuralEvaluator.PreviousSibling) evaluator).getPreviousTag().tagName());
    }

    @org.junit.Test(expected = org.jsoup.select.Selector.SelectorParseException.class)
    public void testParse_invalidSelector() {
        String query = "div[attrib=value]~p"; // combining a general sibling combinator with an attribute selector
        QueryParser parser = new QueryParser(query);
        parser.parse();
    }

    @org.junit.Test
    public void testParse_attributeStartsWithSelector() {
        String query = "[attribute^='value']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.AttributeWithValueStarting.class, evaluator.getClass());
        assertEquals("attribute", ((Evaluator.AttributeWithValueStarting) evaluator).attributeName());
        assertEquals("value", ((Evaluator.AttributeWithValueStarting) evaluator).attributeValuePrefix());
    }

    @org.junit.Test
    public void testParse_attributeEndsWithSelector() {
        String query = "[attribute$='value']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.AttributeWithValueEnding.class, evaluator.getClass());
        assertEquals("attribute", ((Evaluator.AttributeWithValueEnding) evaluator).attributeName());
        assertEquals("value", ((Evaluator.AttributeWithValueEnding) evaluator).attributeValueSuffix());
    }

    @org.junit.Test
    public void testParse_attributeContainsSelector() {
        String query = "[attribute*='value']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.AttributeWithValueContaining.class, evaluator.getClass());
        assertEquals("attribute", ((Evaluator.AttributeWithValueContaining) evaluator).attributeName());
        assertEquals("value", ((Evaluator.AttributeWithValueContaining) evaluator).attributeValue());
    }

    @org.junit.Test
    public void testParse_attributeMatchingSelector() {
        String query = "[attribute~='value']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.AttributeWithValueMatching.class, evaluator.getClass());
        assertEquals("attribute", ((Evaluator.AttributeWithValueMatching) evaluator).attributeName());
        assertEquals(Pattern.compile("value"), ((Evaluator.AttributeWithValueMatching) evaluator).attributeValuePattern());
    }

    @org.junit.Test
    public void testParse_multipleAttributeSelectors() {
        String query = "[attribute1=value1][attribute2=value2]";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        List<Evaluator> children = ((CombiningEvaluator.And) evaluator).getChildren();
        assertEquals(2, children.size());
        Evaluator firstChild = children.get(0);
        assertEquals(Evaluator.AttributeWithValue.class, firstChild.getClass());
        assertEquals("attribute1", ((Evaluator.AttributeWithValue) firstChild).attributeName());
        assertEquals("value1", ((Evaluator.AttributeWithValue) firstChild).attributeValue());
        Evaluator secondChild = children.get(1);
        assertEquals(Evaluator.AttributeWithValue.class, secondChild.getClass());
        assertEquals("attribute2", ((Evaluator.AttributeWithValue) secondChild).attributeName());
        assertEquals("value2", ((Evaluator.AttributeWithValue) secondChild).attributeValue());
    }

    @org.junit.Test
    public void testParse_elementWithAttributeSelector() {
        String query = "div[attribute]";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof StructuralEvaluator.ElementWithAttribute);
        assertEquals(Evaluator.Tag.class, ((StructuralEvaluator.ElementWithAttribute) evaluator).getTag().getClass());
        assertEquals("div", ((StructuralEvaluator.ElementWithAttribute) evaluator).getTag().tagName());
        assertEquals("attribute", ((StructuralEvaluator.ElementWithAttribute) evaluator).getAttributeName());
    }

    @org.junit.Test
    public void testParse_pseudoClassSelector() {
        String query = "div:empty";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.IsEmpty.class, evaluator.getClass());
    }

    @org.junit.Test
    public void testParse_pseudoElementSelector() {
        String query = "div:first-child";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.IsFirstChild.class, evaluator.getClass());
    }

    @org.junit.Test
    public void testParse_elementWithPseudoClassSelector() {
        String query = "div:hover div";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        List<Evaluator> children = ((CombiningEvaluator.And) evaluator).getChildren();
        assertEquals(2, children.size());
        Evaluator firstChild = children.get(0);
        assertTrue(firstChild instanceof StructuralEvaluator.ElementWithPseudoClass);
        assertEquals("div", ((StructuralEvaluator.ElementWithPseudoClass) firstChild).getTag().tagName());
        assertEquals(Evaluator.PseudoClass.HOVER, ((StructuralEvaluator.ElementWithPseudoClass) firstChild).getPseudoClass());
        Evaluator secondChild = children.get(1);
        assertEquals(Evaluator.Tag.class, secondChild.getClass());
        assertEquals("div", ((Evaluator.Tag) secondChild).tagName());
    }

    @org.junit.Test
    public void testParse_nthChildSelector() {
        String query = "div:nth-child(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.IsNthChild.class, evaluator.getClass());
        assertEquals(2, ((Evaluator.IsNthChild) evaluator).n());
        assertEquals(0, ((Evaluator.IsNthChild) evaluator).skip());
    }

    @org.junit.Test
    public void testParse_nthLastChildSelector() {
        String query = "div:nth-last-child(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.IsNthLastChild.class, evaluator.getClass());
        assertEquals(2, ((Evaluator.IsNthLastChild) evaluator).n());
        assertEquals(0, ((Evaluator.IsNthLastChild) evaluator).skip());
    }

    @org.junit.Test
    public void testParse_nthOfTypeSelector() {
        String query = "div:nth-of-type(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.IsNthOfType.class, evaluator.getClass());
        assertEquals(2, ((Evaluator.IsNthOfType) evaluator).n());
        assertEquals(0, ((Evaluator.IsNthOfType) evaluator).skip());
    }

    @org.junit.Test
    public void testParse_nthLastOfTypeSelector() {
        String query = "div:nth-last-of-type(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.IsNthLastOfType.class, evaluator.getClass());
        assertEquals(2, ((Evaluator.IsNthLastOfType) evaluator).n());
        assertEquals(0, ((Evaluator.IsNthLastOfType) evaluator).skip());
    }

    @org.junit.Test
    public void testParse_hasSelector() {
        String query = "div:has(p)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof StructuralEvaluator.Has);
        assertEquals(Evaluator.Tag.class, ((StructuralEvaluator.Has) evaluator).getChildEvaluator().getClass());
        assertEquals("p", ((StructuralEvaluator.Has) evaluator).getChildEvaluator().tagName());
    }

    @org.junit.Test
    public void testParse_containsTextSelector() {
        String query = "div:contains(text)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.ContainsText);
        assertEquals("text", ((Evaluator.ContainsText) evaluator).getText());
    }

    @org.junit.Test
    public void testParse_containsOwnTextSelector() {
        String query = "div:containsOwn(text)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.ContainsOwnText);
        assertEquals("text", ((Evaluator.ContainsOwnText) evaluator).getText());
    }

    @org.junit.Test
    public void testParse_containsDataSelector() {
        String query = "div:containsData(data)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.ContainsData);
        assertEquals("data", ((Evaluator.ContainsData) evaluator).getData());
    }

    @org.junit.Test
    public void testParse_matchesSelector() {
        String query = "div:matches(regex)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Matches);
        assertEquals(Pattern.compile("regex"), ((Evaluator.Matches) evaluator).getPattern());
    }

    @org.junit.Test
    public void testParse_matchesOwnSelector() {
        String query = "div:matchesOwn(regex)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.MatchesOwn);
        assertEquals(Pattern.compile("regex"), ((Evaluator.MatchesOwn) evaluator).getPattern());
    }

    @org.junit.Test
    public void testParse_notSelector() {
        String query = "div:not(p)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof StructuralEvaluator.Not);
        assertEquals(Evaluator.Tag.class, ((StructuralEvaluator.Not) evaluator).getChildEvaluator().getClass());
        assertEquals("p", ((StructuralEvaluator.Not) evaluator).getChildEvaluator().tagName());
    }
}