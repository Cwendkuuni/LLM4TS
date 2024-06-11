package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.helper.StringUtil;
import org.jsoup.parser.TokenQueue;

import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div#main > ul > li.active:contains(Apples)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        String query = "div#main > ul > li.active:contains(Apples):not(ul)";
        QueryParser.parse(query);
    }

    @Test
    public void testCombineElements() {
        String query1 = "div#main > ul > li";
        Evaluator element1 = QueryParser.parse(query1);

        String query2 = "li.active";
        Evaluator element2 = QueryParser.parse(query2);

        Evaluator andEvaluator = new CombiningEvaluator.And(element1, element2);
        Evaluator parsedQuery = QueryParser.parse(query1 + " " + query2);

        assertEquals(andEvaluator, parsedQuery);
    }

    @Test
    public void testCombineElementsWithCombinator() {
        String query1 = "div#main > ul > li";
        Evaluator element1 = QueryParser.parse(query1);

        String query2 = "li.active";
        Evaluator element2 = QueryParser.parse(query2);

        Evaluator andEvaluator = new CombiningEvaluator.And(element1, new StructuralEvaluator.ImmediateChild(new TagNameEvaluator("ul"), element1));
        Evaluator greaterThanEvaluator = new StructuralEvaluator.GreaterThan(new TagNameEvaluator("li"), element1);
        Evaluator andWithCombinatorEvaluator = new CombiningEvaluator.And(new StructuralEvaluator.Descendant(new TagNameEvaluator("li"), element1), greaterThanEvaluator);
        Evaluator classEvaluator = new ClassNameEvaluator("active");
        Evaluator orEvaluator = new CombiningEvaluator.Or(element2, classEvaluator);
        Evaluator finalEvaluator = new CombiningEvaluator.And(andWithCombinatorEvaluator, orEvaluator);

        String query = query1 + " > " + query2 + " " + ".active";
        Evaluator parsedQuery = QueryParser.parse(query);

        assertEquals(finalEvaluator, parsedQuery);
    }

    @Test
    public void testParseSubSelect() {
        String query = "div#main > ul > li.active:has(> a[href^='http://example.com'])";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testFindElements() {
        String query = "div#main > ul > li:eq(2)";
        Evaluator evaluator = QueryParser.parse(query);

        int index = 2;
        assertEquals(new Evaluator.IndexEquals(index), evaluator);
    }

    @Test
    public void testFindElementsById() {
        String query = "div#main";
        Evaluator evaluator = QueryParser.parse(query);

        String id = "main";
        assertEquals(new Evaluator.Id(id), evaluator);
    }

    @Test
    public void testFindElementsByClass() {
        String query = "div.main";
        Evaluator evaluator = QueryParser.parse(query);

        String className = "main";
        assertEquals(new Evaluator.Class(className), evaluator);
    }

    @Test
    public void testFindElementsByTag() {
        String query = "div.main > p";
        Evaluator evaluator = QueryParser.parse(query);

        String tagName = "p";
        assertEquals(new Evaluator.Tag(tagName), evaluator);
    }

    @Test
    public void testFindElementsByAttribute() {
        String query = "div[class='main']";
        Evaluator evaluator = QueryParser.parse(query);

        String attributeName = "class";
        assertEquals(new Evaluator.Attribute(attributeName), evaluator);
    }

    @Test
    public void testFindAllElements() {
        String query = "*";
        Evaluator evaluator = QueryParser.parse(query);

        assertEquals(new Evaluator.AllElements(), evaluator);
    }

    @Test
    public void testFindElementsByIndex() {
        String query = "div:eq(1)";
        Evaluator evaluator = QueryParser.parse(query);

        int index = 1;
        assertEquals(new Evaluator.IndexEquals(index), evaluator);
    }

    @Test
    public void testFindElementsByAttributeValue() {
        String query = "div[class='main']";
        Evaluator evaluator = QueryParser.parse(query);

        String attributeName = "class";
        String attributeValue = "main";
        assertEquals(new Evaluator.AttributeWithValue(attributeName, attributeValue), evaluator);
    }

    @Test
    public void testFindElementsByAttributeValueStarting() {
        String query = "div[class^='main']";
        Evaluator evaluator = QueryParser.parse(query);

        String attributeName = "class";
        String attributeValuePrefix = "main";
        assertEquals(new Evaluator.AttributeWithValueStarting(attributeName, attributeValuePrefix), evaluator);
    }

    @Test
    public void testFindElementsByAttributeValueEnding() {
        String query = "div[class$='main']";
        Evaluator evaluator = QueryParser.parse(query);

        String attributeName = "class";
        String attributeValueSuffix = "main";
        assertEquals(new Evaluator.AttributeWithValueEnding(attributeName, attributeValueSuffix), evaluator);
    }

    @Test
    public void testFindElementsByAttributeValueContaining() {
        String query = "div[class*='main']";
        Evaluator evaluator = QueryParser.parse(query);

        String attributeName = "class";
        String attributeValue = "main";
        assertEquals(new Evaluator.AttributeWithValueContaining(attributeName, attributeValue), evaluator);
    }

    @Test
    public void testFindElementsByAttributeValueMatching() {
        String query = "div[class~='main']";
        Evaluator evaluator = QueryParser.parse(query);

        String attributeName = "class";
        String attributeValue = "main";
        assertEquals(new Evaluator.AttributeWithValueMatching(attributeName, Pattern.compile("main")), evaluator);
    }
}