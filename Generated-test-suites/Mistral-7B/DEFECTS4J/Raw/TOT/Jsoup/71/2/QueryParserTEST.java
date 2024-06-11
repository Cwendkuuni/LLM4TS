package org.jsoup.select.test;

import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class QueryParserTest {

    @Test
    public void testParseSimpleSelector() {
        String query = "div.class1";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> evaluatorTree = evaluator.toTree();

        Assert.assertEquals(2, evaluatorTree.size());
        Assert.assertTrue(evaluatorTree.get(0) instanceof Evaluator.Class);
        Assert.assertTrue(evaluatorTree.get(1) instanceof Evaluator.Tag);
        Assert.assertEquals("div", ((Evaluator.Tag) evaluatorTree.get(1)).getTagName());
    }

    @Test
    public void testParseComplexSelectorWithDescendant() {
        String query = "ul > li.class1 > a[href='#link']";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> evaluatorTree = evaluator.toTree();

        Assert.assertEquals(6, evaluatorTree.size());
        Assert.assertTrue(evaluatorTree.get(0) instanceof Evaluator.Tag);
        Assert.assertEquals("ul", ((Evaluator.Tag) evaluatorTree.get(0)).getTagName());
        Assert.assertTrue(evaluatorTree.get(1) instanceof Evaluator.StructuralEvaluator.ImmediateChild);
        Assert.assertTrue(evaluatorTree.get(2) instanceof Evaluator.Class);
        Assert.assertTrue(evaluatorTree.get(3) instanceof Evaluator.Tag);
        Assert.assertEquals("li", ((Evaluator.Tag) evaluatorTree.get(3)).getTagName());
        Assert.assertTrue(evaluatorTree.get(4) instanceof Evaluator.Attribute);
        Assert.assertEquals("href", ((Evaluator.Attribute) evaluatorTree.get(4)).getAttributeName());
        Assert.assertEquals("#link", ((Evaluator.Attribute) evaluatorTree.get(4)).getValue());
        Assert.assertTrue(evaluatorTree.get(5) instanceof Evaluator.Tag);
        Assert.assertEquals("a", ((Evaluator.Tag) evaluatorTree.get(5)).getTagName());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidSelector() {
        String query = "div*=class1";
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidCombinationOfSelectors() {
        String query = "div.class1.class2:has(a[href='#link']), ul > li.class3";
        QueryParser.parse(query);
    }

    @Test
    public void testParseComplexSelectorWithAttribute() {
        String query = "input[type='text'][name='username']";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> evaluatorTree = evaluator.toTree();

        Assert.assertEquals(3, evaluatorTree.size());
        Assert.assertTrue(evaluatorTree.get(0) instanceof Evaluator.Attribute);
        Assert.assertEquals("type", ((Evaluator.Attribute) evaluatorTree.get(0)).getAttributeName());
        Assert.assertEquals("text", ((Evaluator.Attribute) evaluatorTree.get(0)).getValue());
        Assert.assertTrue(evaluatorTree.get(1) instanceof Evaluator.Attribute);
        Assert.assertEquals("name", ((Evaluator.Attribute) evaluatorTree.get(1)).getAttributeName());
        Assert.assertEquals("username", ((Evaluator.Attribute) evaluatorTree.get(1)).getValue());
        Assert.assertTrue(evaluatorTree.get(2) instanceof Evaluator.Tag);
        Assert.assertEquals("input", ((Evaluator.Tag) evaluatorTree.get(2)).getTagName());
    }

    @Test
    public void testParseComplexSelectorWithNegation() {
        String query = "div:not(.class1)";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> evaluatorTree = evaluator.toTree();

        Assert.assertEquals(2, evaluatorTree.size());
        Assert.assertTrue(evaluatorTree.get(0) instanceof Evaluator.Class);
        Assert.assertEquals("class1", ((Evaluator.Class) evaluatorTree.get(0)).getClassName());
        Assert.assertTrue(evaluatorTree.get(1) instanceof Evaluator.StructuralEvaluator.Not);
        Assert.assertEquals(1, evaluatorTree.get(1).size());
        Assert.assertTrue(evaluatorTree.get(1).get(0) instanceof Evaluator.Class);
        Assert.assertEquals("class1", ((Evaluator.Class) evaluatorTree.get(1).get(0)).getClassName());
    }

    @Test
    public void testParseComplexSelectorWithNthChild() {
        String query = "li:nth-child(3)";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> evaluatorTree = evaluator.toTree();

        Assert.assertEquals(2, evaluatorTree.size());
        Assert.assertTrue(evaluatorTree.get(0) instanceof Evaluator.Tag);
        Assert.assertEquals("li", ((Evaluator.Tag) evaluatorTree.get(0)).getTagName());
        Assert.assertTrue(evaluatorTree.get(1) instanceof Evaluator.NthChild);
        Assert.assertEquals(1, ((Evaluator.NthChild) evaluatorTree.get(1)).getNth());
    }

    @Test
    public void testParseComplexSelectorWithNthOfType() {
        String query = "p:nth-of-type(3)";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> evaluatorTree = evaluator.toTree();

        Assert.assertEquals(2, evaluatorTree.size());
        Assert.assertTrue(evaluatorTree.get(0) instanceof Evaluator.Tag);
        Assert.assertEquals("p", ((Evaluator.Tag) evaluatorTree.get(0)).getTagName());
        Assert.assertTrue(evaluatorTree.get(1) instanceof Evaluator.NthOfType);
        Assert.assertEquals(1, ((Evaluator.NthOfType) evaluatorTree.get(1)).getNth());
    }

    @Test
    public void testParseComplexSelectorWithNthLastChild() {
        String query = "li:nth-last-child(2)";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> evaluatorTree = evaluator.toTree();

        Assert.assertEquals(2, evaluatorTree.size());
        Assert.assertTrue(evaluatorTree.get(0) instanceof Evaluator.Tag);
        Assert.assertEquals("li", ((Evaluator.Tag) evaluatorTree.get(0)).getTagName());
        Assert.assertTrue(evaluatorTree.get(1) instanceof Evaluator.NthLastChild);
        Assert.assertEquals(1, ((Evaluator.NthLastChild) evaluatorTree.get(1)).getNth());
    }

    @Test
    public void testParseComplexSelectorWithNthLastOfType() {
        String query = "p:nth-last-of-type(2)";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> evaluatorTree = evaluator.toTree();

        Assert.assertEquals(2, evaluatorTree.size());
        Assert.assertTrue(evaluatorTree.get(0) instanceof Evaluator.Tag);
        Assert.assertEquals("p", ((Evaluator.Tag) evaluatorTree.get(0)).getTagName());
        Assert.assertTrue(evaluatorTree.get(1) instanceof Evaluator.NthLastOfType);
        Assert.assertEquals(1, ((Evaluator.NthLastOfType) evaluatorTree.get(1)).getNth());
    }

    @Test
    public void testParseComplexSelectorWithNthLastOfTypeAndNthOfType() {
        String query = "li:nth-last-of-type(1):nth-child(3)";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> evaluatorTree = evaluator.toTree();

        Assert.assertEquals(3, evaluatorTree.size());
        Assert.assertTrue(evaluatorTree.get(0) instanceof Evaluator.Tag);
        Assert.assertEquals("li", ((Evaluator.Tag) evaluatorTree.get(0)).getTagName());
        Assert.assertTrue(evaluatorTree.get(1) instanceof Evaluator.NthLastOfType);
        Assert.assertEquals(1, ((Evaluator.NthLastOfType) evaluatorTree.get(1)).getNth());
        Assert.assertTrue(evaluatorTree.get(2) instanceof Evaluator.NthChild);
        Assert.assertEquals(1, ((Evaluator.NthChild) evaluatorTree.get(2)).getNth());
    }
}