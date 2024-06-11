package org.jsoup.select;

import static org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.TokenQueueUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for QueryParser class
 */
public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        String query = "";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AllElements);
    }

    @Test
    public void testParseSimpleTagQuery() {
        String query = "p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals(evaluator.toString(), "tag(p)");
    }

    @Test
    public void testParseClassQuery() {
        String query = ".className";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals(evaluator.toString(), "class(className)");
    }

    @Test
    public void testParseIDQuery() {
        String query = "#id";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals(evaluator.toString(), "id(id)");
    }

    @Test
    public void testParseCombinedQueries() {
        String query = "p > .className > #id";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        Evaluator[] children = ((CombiningEvaluator.And) evaluator).getChildren();
        assertEquals(children[0].toString(), "tag(p)");
        assertEquals(children[1].toString(), "class(className)");
        assertEquals(children[2].toString(), "id(id)");
    }

    @Test
    public void testParseAttributeQuery() {
        String query = "[title='Test']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
        assertEquals(evaluator.toString(), "attribute(title, 'Test')");
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidAttributeQuery() {
        String query = "[title=Test";
        QueryParser parser = new QueryParser(query);
        parser.parse();
        fail("Expected Selector.SelectorParseException");
    }

    @Test
    public void testParseMultipleAttributeQueries() {
        String query = "[title='Test'][class='className']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        Evaluator[] children = ((CombiningEvaluator.And) evaluator).getChildren();
        assertEquals(children[0].toString(), "attribute(title, 'Test')");
        assertEquals(children[1].toString(), "class(className)");
    }

    @Test
    public void testParseNthChildQuery() {
        String query = "p:nth-child(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsNthChild);
        assertEquals(evaluator.toString(), "isNthChild(p, 2)");
    }

    @Test
    public void testParseNthLastChildQuery() {
        String query = "p:nth-last-child(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsNthLastChild);
        assertEquals(evaluator.toString(), "isNthLastChild(p, 2)");
    }

    @Test
    public void testParseNthOfTypeQuery() {
        String query = "p:nth-of-type(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsNthOfType);
        assertEquals(evaluator.toString(), "isNthOfType(p, 2)");
    }

    @Test
    public void testParseNthLastOfTypeQuery() {
        String query = "p:nth-last-of-type(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsNthLastOfType);
        assertEquals(evaluator.toString(), "isNthLastOfType(p, 2)");
    }

    @Test
    public void testParseHasQuery() {
        String query = "p:has(a)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof StructuralEvaluator.Has);
        assertEquals(evaluator.toString(), "has(tag(a))");
    }

    @Test
    public void testParseContainsTextQuery() {
        String query = "p:contains(Text)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.ContainsText);
        assertEquals(evaluator.toString(), "containsText(Text, p)");
    }

    @Test
    public void testParseContainsOwnTextQuery() {
        String query = "p:containsOwn(Text)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.ContainsOwnText);
        assertEquals(evaluator.toString(), "containsOwnText(Text, p)");
    }

    @Test
    public void testParseContainsDataQuery() {
        String query = "p:containsData(data)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.ContainsData);
        assertEquals(evaluator.toString(), "containsData(data, p)");
    }

    @Test
    public void testParseNotQuery() {
        String query = "p:not(a)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof StructuralEvaluator.Not);
        assertEquals(evaluator.toString(), "not(tag(a))");
    }

    @Test
    public void testParseComplexQuery() {
        String query = "div:not(.error):has(input:containsOwn(Search)):first-child";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        Evaluator[] children = ((CombiningEvaluator.And) evaluator).getChildren();
        Evaluator notEval = children[0];
        assertTrue(notEval instanceof StructuralEvaluator.Not);
        assertEquals(notEval.toString(), "not(class(error))");
        Evaluator hasEval = children[1];
        assertTrue(hasEval instanceof StructuralEvaluator.Has);
        Evaluator inputEval = ((StructuralEvaluator.Has) hasEval).getChild();
        assertTrue(inputEval instanceof Evaluator.ContainsOwnText);
        assertEquals(inputEval.toString(), "containsOwnText(Search, input)");
        Evaluator firstChildEval = children[2];
        assertTrue(firstChildEval instanceof Evaluator.IsFirstChild);
        assertEquals(firstChildEval.toString(), "isFirstChild(div)");
    }
}