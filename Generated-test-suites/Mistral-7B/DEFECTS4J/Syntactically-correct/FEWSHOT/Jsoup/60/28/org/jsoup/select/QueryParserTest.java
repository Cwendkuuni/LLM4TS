package org.jsoup.select;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.jsoup.select.QueryParser;

public class QueryParserTest {

    @Test
    public void testParse_EmptyQuery() {
        QueryParser parser = new QueryParser("");
        Evaluator evaluator = parser.parse();
        assertNull(evaluator);
    }

    @Test
    public void testParse_TagSelector() {
        QueryParser parser = new QueryParser("p");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.Tag.class, evaluator.getClass());
        assertEquals("p", evaluator.getText());
    }

    @Test
    public void testParse_ClassSelector() {
        QueryParser parser = new QueryParser(".class");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.Class.class, evaluator.getClass());
        assertEquals("class", evaluator.getText());
    }

    @Test
    public void testParse_IdSelector() {
        QueryParser parser = new QueryParser("#id");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.Id.class, evaluator.getClass());
        assertEquals("id", evaluator.getText());
    }

    @Test
    public void testParse_AttributeSelector() {
        QueryParser parser = new QueryParser("[attribute]");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.Attribute.class, evaluator.getClass());
        assertEquals("attribute", evaluator.getText());
    }

    @Test
    public void testParse_AttributeValueSelector() {
        QueryParser parser = new QueryParser("[attribute=value]");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.AttributeWithValue.class, evaluator.getClass());
        assertEquals("attribute", evaluator.getText());
        assertEquals("value", evaluator.getValue());
    }

    @Test
    public void testParse_AttributeValueWithPrefixSelector() {
        QueryParser parser = new QueryParser("[attribute^='value']");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.AttributeWithValueStarting.class, evaluator.getClass());
        assertEquals("attribute", evaluator.getText());
        assertEquals("value", evaluator.getValue());
    }

    @Test
    public void testParse_AttributeValueWithPrefixAndSuffixSelector() {
        QueryParser parser = new QueryParser("[attribute^='value' and attribute$='other']");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.AttributeWithValueStartingAndEnding.class, evaluator.getClass());
        assertEquals("attribute", evaluator.getText());
        assertEquals("value", evaluator.getValueStart());
        assertEquals("other", evaluator.getValueEnd());
    }

    @Test
    public void testParse_ChildSelector() {
        QueryParser parser = new QueryParser("p > div");
        Evaluator evaluator = parser.parse();
        assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
        Evaluator leftOperand = ((CombiningEvaluator.And) evaluator).getLeftOperand();
        assertEquals(Evaluator.Tag.class, leftOperand.getClass());
        assertEquals("p", leftOperand.getText());

        Evaluator rightOperand = ((CombiningEvaluator.And) evaluator).getRightOperand();
        assertEquals(StructuralEvaluator.ImmediateChild.class, rightOperand.getClass());
        assertEquals("div", rightOperand.getText());
    }

    @Test
    public void testParse_DescendantSelector() {
        QueryParser parser = new QueryParser("p div");
        Evaluator evaluator = parser.parse();
        assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
        Evaluator leftOperand = ((CombiningEvaluator.And) evaluator).getLeftOperand();
        assertEquals(Evaluator.Tag.class, leftOperand.getClass());
        assertEquals("p", leftOperand.getText());

        Evaluator rightOperand = ((CombiningEvaluator.And) evaluator).getRightOperand();
        assertEquals(Evaluator.Tag.class, rightOperand.getClass());
        assertEquals("div", rightOperand.getText());
    }

    @Test
    public void testParse_SiblingSelector() {
        QueryParser parser = new QueryParser("p ~ div");
        Evaluator evaluator = parser.parse();
        assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
        Evaluator leftOperand = ((CombiningEvaluator.And) evaluator).getLeftOperand();
        assertEquals(Evaluator.Tag.class, leftOperand.getClass());
        assertEquals("p", leftOperand.getText());

        Evaluator rightOperand = ((CombiningEvaluator.And) evaluator).getRightOperand();
        assertEquals(StructuralEvaluator.PreviousSibling.class, rightOperand.getClass());
        assertEquals("div", rightOperand.getText());
    }

    @Test
    public void testParse_AdjacentSiblingSelector() {
        QueryParser parser = new QueryParser("p + div");
        Evaluator evaluator = parser.parse();
        assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
        Evaluator leftOperand = ((CombiningEvaluator.And) evaluator).getLeftOperand();
        assertEquals(Evaluator.Tag.class, leftOperand.getClass());
        assertEquals("p", leftOperand.getText());

        Evaluator rightOperand = ((CombiningEvaluator.And) evaluator).getRightOperand();
        assertEquals(StructuralEvaluator.ImmediatePreviousSibling.class, rightOperand.getClass());
        assertEquals("div", rightOperand.getText());
    }

    @Test
    public void testParse_CombinatorAndSelector() {
        QueryParser parser = new QueryParser("p, div");
        Evaluator evaluator = parser.parse();
        assertEquals(CombiningEvaluator.Or.class, evaluator.getClass());
        Evaluator leftOperand = ((CombiningEvaluator.Or) evaluator).getLeftOperand();
        assertEquals(Evaluator.Tag.class, leftOperand.getClass());
        assertEquals("p", leftOperand.getText());

        Evaluator rightOperand = ((CombiningEvaluator.Or) evaluator).getRightOperand();
        assertEquals(Evaluator.Tag.class, rightOperand.getClass());
        assertEquals("div", rightOperand.getText());
    }

    @Test
    public void testParse_TagSelectorWithAttribute() {
        QueryParser parser = new QueryParser("p.class");
        Evaluator evaluator = parser.parse();
        assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
        Evaluator leftOperand = ((CombiningEvaluator.And) evaluator).getLeftOperand();
        assertEquals(Evaluator.Tag.class, leftOperand.getClass());
        assertEquals("p", leftOperand.getText());

        Evaluator rightOperand = ((CombiningEvaluator.And) evaluator).getRightOperand();
        assertEquals(Evaluator.Class.class, rightOperand.getClass());
        assertEquals("class", rightOperand.getText());
    }

    @Test
    public void testParse_TagSelectorWithId() {
        QueryParser parser = new QueryParser("p#id");
        Evaluator evaluator = parser.parse();
        assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
        Evaluator leftOperand = ((CombiningEvaluator.And) evaluator).getLeftOperand();
        assertEquals(Evaluator.Tag.class, leftOperand.getClass());
        assertEquals("p", leftOperand.getText());

        Evaluator rightOperand = ((CombiningEvaluator.And) evaluator).getRightOperand();
        assertEquals(Evaluator.Id.class, rightOperand.getClass());
        assertEquals("id", rightOperand.getText());
    }

    @Test
    public void testParse_CombinedSelectors() {
        QueryParser parser = new QueryParser("p.class > div.subclass");
        Evaluator evaluator = parser.parse();
        assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
        Evaluator leftOperand = ((CombiningEvaluator.And) evaluator).getLeftOperand();
        assertEquals(CombiningEvaluator.And.class, leftOperand.getClass());
        Evaluator leftOperandLeft = ((CombiningEvaluator.And) leftOperand).getLeftOperand();
        assertEquals(Evaluator.Tag.class, leftOperandLeft.getClass());
        assertEquals("p", leftOperandLeft.getText());

        Evaluator leftOperandRight = ((CombiningEvaluator.And) leftOperand).getRightOperand();
        assertEquals(Evaluator.Class.class, leftOperandRight.getClass());
        assertEquals("class", leftOperandRight.getText());

        Evaluator rightOperand = ((CombiningEvaluator.And) evaluator).getRightOperand();
        assertEquals(CombiningEvaluator.And.class, rightOperand.getClass());
        Evaluator rightOperandLeft = ((CombiningEvaluator.And) rightOperand).getLeftOperand();
        assertEquals(Evaluator.Tag.class, rightOperandLeft.getClass());
        assertEquals("div", rightOperandLeft.getText());

        Evaluator rightOperandRight = ((CombiningEvaluator.And) rightOperand).getRightOperand();
        assertEquals(Evaluator.Class.class, rightOperandRight.getClass());
        assertEquals("subclass", rightOperandRight.getText());
    }

    @Test
    public void testParse_NegatedSelector() {
        QueryParser parser = new QueryParser(":not(p)");
        Evaluator evaluator = parser.parse();
        assertEquals(StructuralEvaluator.Not.class, evaluator.getClass());
        assertEquals(Evaluator.Tag.class, evaluator.getSubExpression().getClass());
        assertEquals("p", evaluator.getSubExpression().getText());
    }

    @Test
    public void testParse_PseudoClassSelector() {
        QueryParser parser = new QueryParser(":first-child");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.IsFirstChild.class, evaluator.getClass());
    }

    @Test
    public void testParse_PseudoClassSelectorWithAttribute() {
        QueryParser parser = new QueryParser(":has(p) > div.selected");
        Evaluator evaluator = parser.parse();
        assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
        Evaluator leftOperand = ((CombiningEvaluator.And) evaluator).getLeftOperand();
        assertEquals(StructuralEvaluator.Has.class, leftOperand.getClass());
        assertEquals(Evaluator.Tag.class, ((StructuralEvaluator.Has) leftOperand).getSubExpression().getClass());
        assertEquals("p", ((StructuralEvaluator.Has) leftOperand).getSubExpression().getText());

        Evaluator rightOperand = ((CombiningEvaluator.And) evaluator).getRightOperand();
        assertEquals(CombiningEvaluator.And.class, rightOperand.getClass());
        Evaluator rightOperandLeft = ((CombiningEvaluator.And) rightOperand).getLeftOperand();
        assertEquals(Evaluator.Tag.class, rightOperandLeft.getClass());
        assertEquals("div", rightOperandLeft.getText());

        Evaluator rightOperandRight = ((CombiningEvaluator.And) rightOperand).getRightOperand();
        assertEquals(Evaluator.Class.class, rightOperandRight.getClass());
        assertEquals("selected", rightOperandRight.getText());
    }
}