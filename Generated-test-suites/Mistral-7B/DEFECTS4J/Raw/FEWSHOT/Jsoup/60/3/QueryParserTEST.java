package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import org.jsoup.parser.TokenQueue;

public class QueryParserTest {

    @Test
    public void testParseSimpleSelector() {
        String query = "div";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertInstanceOf(Evaluator.Tag.class, evaluator);
        Evaluator.Tag tagEvaluator = (Evaluator.Tag) evaluator;
        assertEquals("div", tagEvaluator.tagName);
    }

    @Test
    public void testParseSelectorWithClass() {
        String query = "div.className";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertInstanceOf(Evaluator.Class.class, evaluator);
        Evaluator.Class classEvaluator = (Evaluator.Class) evaluator;
        assertEquals("className", classEvaluator.className);
    }

    @Test
    public void testParseSelectorWithId() {
        String query = "#myId";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertInstanceOf(Evaluator.Id.class, evaluator);
        Evaluator.Id idEvaluator = (Evaluator.Id) evaluator;
        assertEquals("myId", idEvaluator.id);
    }

    @Test
    public void testParseSelectorWithAttribute() {
        String query = "input[type='text']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertInstanceOf(Evaluator.Attribute.class, evaluator);
        Evaluator.Attribute attributeEvaluator = (Evaluator.Attribute) evaluator;
        assertEquals("type", attributeEvaluator.attributeName);
        assertEquals("text", attributeEvaluator.expectedAttributeValue);
    }

    @Test
    public void testParseSelectorWithCombinator() {
        String query = "div > p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertInstanceOf(CombiningEvaluator.And.class, evaluator);
        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) evaluator;
        Evaluator leftChild = andEvaluator.getChild(0);
        assertInstanceOf(Evaluator.Tag.class, leftChild);
        Evaluator.Tag tagLeftChild = (Evaluator.Tag) leftChild;
        assertEquals("div", tagLeftChild.tagName);
        Evaluator rightChild = andEvaluator.getChild(1);
        assertInstanceOf(StructuralEvaluator.ImmediateChild.class, rightChild);
        StructuralEvaluator.ImmediateChild immediateChild = (StructuralEvaluator.ImmediateChild) rightChild;
        assertEquals(Evaluator.Tag.class, immediateChild.getElementType());
        assertEquals("p", immediateChild.expectedElementName);
    }

    @Test
    public void testParseSelectorWithCombinators() {
        String query = "div > p > ul > li";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertInstanceOf(CombiningEvaluator.And.class, evaluator);
        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) evaluator;
        Evaluator leftChild = andEvaluator.getChild(0);
        assertInstanceOf(Evaluator.Tag.class, leftChild);
        Evaluator.Tag tagLeftChild = (Evaluator.Tag) leftChild;
        assertEquals("div", tagLeftChild.tagName);
        Evaluator rightChild = andEvaluator.getChild(1);
        assertInstanceOf(StructuralEvaluator.ImmediateChild.class, rightChild);
        StructuralEvaluator.ImmediateChild immediateChild = (StructuralEvaluator.ImmediateChild) rightChild;
        assertEquals(Evaluator.Tag.class, immediateChild.getElementType());
        assertEquals("p", immediateChild.expectedElementName);
        Evaluator grandChild = andEvaluator.getChild(2);
        assertInstanceOf(StructuralEvaluator.Child.class, grandChild);
        StructuralEvaluator.Child child = (StructuralEvaluator.Child) grandChild;
        assertEquals(Evaluator.Tag.class, child.getElementType());
        assertEquals("ul", child.expectedElementName);
        Evaluator greatGrandChild = andEvaluator.getChild(3);
        assertInstanceOf(StructuralEvaluator.ImmediateChild.class, greatGrandChild);
        StructuralEvaluator.ImmediateChild greatImmediateChild = (StructuralEvaluator.ImmediateChild) greatGrandChild;
        assertEquals(Evaluator.Tag.class, greatImmediateChild.getElementType());
        assertEquals("li", greatImmediateChild.expectedElementName);
    }

    @Test
    public void testParseComplexSelector() {
        String query = "div.class1:not(.class2) > ul.class3 > li:nth-child(2) > input[type='text']:contains('search')";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertInstanceOf(CombiningEvaluator.And.class, evaluator);
        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) evaluator;
        Evaluator leftChild = andEvaluator.getChild(0);
        assertInstanceOf(Evaluator.Tag.class, leftChild);
        Evaluator.Tag tagLeftChild = (Evaluator.Tag) leftChild;
        assertEquals("div", tagLeftChild.tagName);
        Evaluator classChild = andEvaluator.getChild(1);
        assertInstanceOf(Evaluator.Class.class, classChild);
        Evaluator.Class classEvaluator = (Evaluator.Class) classChild;
        assertEquals("class1", classEvaluator.className);
        Evaluator notChild = andEvaluator.getChild(2);
        assertInstanceOf(StructuralEvaluator.Not.class, notChild);
        StructuralEvaluator.Not notEvaluator = (StructuralEvaluator.Not) notChild;
        Evaluator notElement = notEvaluator.getElement();
        assertInstanceOf(Evaluator.Class.class, notElement);
        Evaluator.Class classNotElement = (Evaluator.Class) notElement;
        assertEquals("class2", classNotElement.className);
        Evaluator rightChild = andEvaluator.getChild(3);
        assertInstanceOf(Evaluator.Tag.class, rightChild);
        Evaluator.Tag tagRightChild = (Evaluator.Tag) rightChild;
        assertEquals("ul", tagRightChild.tagName);
        Evaluator classRightChild = andEvaluator.getChild(4);
        assertInstanceOf(Evaluator.Class.class, classRightChild);
        Evaluator.Class classRightEvaluator = (Evaluator.Class) classRightChild;
        assertEquals("class3", classRightEvaluator.className);
        Evaluator liChild = andEvaluator.getChild(5);
        assertInstanceOf(StructuralEvaluator.ImmediateChild.class, liChild);
        StructuralEvaluator.ImmediateChild liImmediateChild = (StructuralEvaluator.ImmediateChild) liChild;
        assertEquals(Evaluator.Tag.class, liImmediateChild.getElementType());
        assertEquals("li", liImmediateChild.expectedElementName);
        Evaluator inputChild = andEvaluator.getChild(6);
        assertInstanceOf(StructuralEvaluator.ImmediateChild.class, inputChild);
        StructuralEvaluator.ImmediateChild inputImmediateChild = (StructuralEvaluator.ImmediateChild) inputChild;
        assertEquals(Evaluator.Tag.class, inputImmediateChild.getElementType());
        assertEquals("input", inputImmediateChild.expectedElementName);
        Evaluator attributeChild = inputChild; // inputChild also has attribute evaluator
        assertInstanceOf(Evaluator.Attribute.class, attributeChild);
        Evaluator.Attribute attributeEvaluator = (Evaluator.Attribute) attributeChild;
        assertEquals("type", attributeEvaluator.attributeName);
        assertEquals("text", attributeEvaluator.expectedAttributeValue);
        Evaluator textChild = andEvaluator.getChild(7);
        assertInstanceOf(Evaluator.ContainsText.class, textChild);
        Evaluator.ContainsText containsTextEvaluator = (Evaluator.ContainsText) textChild;
        assertEquals("search", containsTextEvaluator.expectedText);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidSelector() {
        String query = "div.class1 > p > ul > li:nth-child(-1) > input[type='text']:contains(search)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
    }
}