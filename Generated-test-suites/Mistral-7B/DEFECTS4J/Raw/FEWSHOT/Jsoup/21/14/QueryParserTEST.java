package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParseSimpleSelector() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals("div", evaluator.toString());
    }

    @Test
    public void testParseSelectorWithId() {
        String query = "#testId";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals("#testId", evaluator.toString());
        assertTrue(evaluator instanceof Evaluator.Id);
    }

    @Test
    public void testParseSelectorWithClass() {
        String query = ".testClass";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(".testClass", evaluator.toString());
        assertTrue(evaluator instanceof Evaluator.Class);
    }

    @Test
    public void testParseSelectorWithTag() {
        String query = "p";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals("p", evaluator.toString());
        assertTrue(evaluator instanceof Evaluator.Tag);
    }

    @Test
    public void testParseSelectorWithAttribute() {
        String query = "[href]";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals("[href]", evaluator.toString());
        assertTrue(evaluator instanceof Evaluator.Attribute);
    }

    @Test
    public void testParseSelectorWithAttributeValue() {
        String query = "[href='http://example.com']";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals("[href='http://example.com']", evaluator.toString());
        assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
    }

    @Test
    public void testParseSelectorWithClassAndAttribute() {
        String query = ".testClass[href]";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(".testClass[href]", evaluator.toString());
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertTrue(((CombiningEvaluator.And) evaluator).getLeft() instanceof Evaluator.Class);
        assertTrue(((CombiningEvaluator.And) evaluator).getRight() instanceof Evaluator.Attribute);
    }

    @Test
    public void testParseSelectorWithCombinators() {
        String query = "div > p > a";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals("div > p > a", evaluator.toString());
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        CombiningEvaluator.And and = (CombiningEvaluator.And) evaluator;
        assertTrue(and.getLeft() instanceof StructuralEvaluator.ImmediateChild);
        assertTrue(and.getRight() instanceof CombiningEvaluator.And);
        CombiningEvaluator.And and2 = (CombiningEvaluator.And) and.getRight();
        assertTrue(and2.getLeft() instanceof Evaluator.Tag);
        assertTrue(and2.getRight() instanceof Evaluator.Tag);
    }

    @Test
    public void testParseSelectorWithPseudoClass() {
        String query = "p:first-child";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals("p:first-child", evaluator.toString());
        assertTrue(evaluator instanceof Evaluator.PseudoClass);
    }

    @Test
    public void testParseSelectorWithPseudoElement() {
        String query = "p:first-letter";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals("p:first-letter", evaluator.toString());
        assertTrue(evaluator instanceof Evaluator.PseudoElement);
    }

    @Test
    public void testParseSelectorWithMultiplePseudoElements() {
        String query = "p:first-letter + p:last-child";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals("p:first-letter + p:last-child", evaluator.toString());
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        CombiningEvaluator.And and = (CombiningEvaluator.And) evaluator;
        assertTrue(and.getLeft() instanceof Evaluator.PseudoElement);
        assertTrue(and.getRight() instanceof Evaluator.PseudoElement);
    }

    @Test
    public void testParseSelectorWithNegatedPseudoElement() {
        String query = "p:not(:first-letter)";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals("p:not(:first-letter)", evaluator.toString());
        assertTrue(evaluator instanceof StructuralEvaluator.Not);
        assertTrue(((StructuralEvaluator.Not) evaluator).getLeft() instanceof Evaluator.PseudoElement);
    }

    @Test
    public void testParseSelectorWithMultipleCombinators() {
        String query = "div > p + div > p";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals("div > p + div > p", evaluator.toString());
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        CombiningEvaluator.And and = (CombiningEvaluator.And) evaluator;
        assertTrue(and.getLeft() instanceof StructuralEvaluator.ImmediateChild);
        assertTrue(and.getRight() instanceof CombiningEvaluator.And);
        CombiningEvaluator.And and2 = (CombiningEvaluator.And) and.getRight();
        assertTrue(and2.getLeft() instanceof Evaluator.Tag);
        assertTrue(and2.getRight() instanceof StructuralEvaluator.AdjacentSibling);
    }

    @Test
    public void testParseSelectorWithAttributeContains() {
        String query = "[title~='home']";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals("[title~='home']", evaluator.toString());
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueMatching);
    }

    @Test
    public void testParseSelectorWithAttributeStartsWith() {
        String query = "[title^='home']";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals("[title^='home']", evaluator.toString());
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueStarting);
    }

    @Test
    public void testParseSelectorWithAttributeEndsWith() {
        String query = "[title$='home']";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals("[title$='home']", evaluator.toString());
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueEnding);
    }

    @Test
    public void testParseSelectorWithAttributeContainsRegex() {
        String query = "[title*='home']";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals("[title*='home']", evaluator.toString());
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueContaining);
    }

    @Test
    public void testParseSelectorWithDescendantSelector() {
        String query = "div > p > a > span";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals("div > p > a > span", evaluator.toString());
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        CombiningEvaluator.And and = (CombiningEvaluator.And) evaluator;
        assertTrue(and.getLeft() instanceof StructuralEvaluator.ImmediateChild);
        assertTrue(and.getRight() instanceof CombiningEvaluator.And);
        CombiningEvaluator.And and2 = (CombiningEvaluator.And) and.getRight();
        assertTrue(and2.getLeft() instanceof Evaluator.Tag);
        assertTrue(and2.getRight() instanceof CombiningEvaluator.And);
        CombiningEvaluator.And and3 = (CombiningEvaluator.And) and2.getRight();
        assertTrue(and3.getLeft() instanceof Evaluator.Tag);
        assertTrue(and3.getRight() instanceof Evaluator.Tag);
    }

    @Test
    public void testParseSelectorWithNegatedDescendantSelector() {
        String query = "div > p > a > :not(span)";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals("div > p > a > :not(span)", evaluator.toString());
        assertTrue(evaluator instanceof StructuralEvaluator.Not);
        assertTrue(((StructuralEvaluator.Not) evaluator).getLeft() instanceof CombiningEvaluator.And);
        CombiningEvaluator.And and = (CombiningEvaluator.And) (((StructuralEvaluator.Not) evaluator).getLeft());
        assertTrue(and.getLeft() instanceof Evaluator.Tag);
        assertTrue(and.getRight() instanceof Evaluator.Tag);
    }

    @Test
    public void testParseSelectorWithListSelector() {
        String query = "ul > li > a";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals("ul > li > a", evaluator.toString());
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        CombiningEvaluator.And and = (CombiningEvaluator.And) evaluator;
        assertTrue(and.getLeft() instanceof Evaluator.Tag);
        assertTrue(and.getRight() instanceof CombiningEvaluator.And);
        CombiningEvaluator.And and2 = (CombiningEvaluator.And) and.getRight();
        assertTrue(and2.getLeft() instanceof Evaluator.Tag);
        assertTrue(and2.getRight() instanceof Evaluator.Tag);

        String query2 = "ul > li:first-child > a";
        Evaluator evaluator2 = QueryParser.parse(query2);
        assertEquals("ul > li:first-child > a", evaluator2.toString());
        assertTrue(evaluator2 instanceof CombiningEvaluator.And);
        CombiningEvaluator.And and3 = (CombiningEvaluator.And) evaluator2;
        assertTrue(and3.getLeft() instanceof StructuralEvaluator.PseudoFirstChild);
        assertTrue(and3.getRight() instanceof Evaluator.Tag);
    }

    @Test
    public void testParseSelectorWithAttributeSelectorOperators() {
        String query = "[title~=home ^=about]";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals("[title~=home ^=about]", evaluator.toString());
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        CombiningEvaluator.And and = (CombiningEvaluator.And) evaluator;
        assertTrue(and.getLeft() instanceof Evaluator.AttributeWithValueMatching);
        assertTrue(and.getRight() instanceof Evaluator.AttributeWithValueStarting);
    }
}