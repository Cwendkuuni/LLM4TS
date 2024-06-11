package org.jsoup.select;

import static org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;

import java.util.ArrayList;
import java.util.List;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div p#id1.class1 > span.class2:contains(text)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new Evaluator.Tag("p"));
        expectedEvaluators.add(new Evaluator.Id("id1"));
        expectedEvaluators.add(new Evaluator.Class("class1"));
        expectedEvaluators.add(new StructuralEvaluator.Descendant(new Evaluator.Tag("span")));
        expectedEvaluators.add(new Evaluator.Class("class2"));
        expectedEvaluators.add(new Evaluator.ContainsText("text"));

        assertEquals(expectedEvaluators, evaluator.getSubTree());
    }

    @Test
    public void testParseWithNthChild() {
        String query = "li:nth-child(3)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.IsNthChild(1, 3));

        assertEquals(expectedEvaluators, evaluator.getSubTree());
    }

    @Test
    public void testParseWithNthLastChild() {
        String query = "li:nth-last-child(3)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.IsNthLastChild(1, 3));

        assertEquals(expectedEvaluators, evaluator.getSubTree());
    }

    @Test
    public void testParseWithNthOfType() {
        String query = "li:nth-of-type(3)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.IsNthOfType(1, 3));

        assertEquals(expectedEvaluators, evaluator.getSubTree());
    }

    @Test
    public void testParseWithNthLastOfType() {
        String query = "li:nth-last-of-type(3)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.IsNthLastOfType(1, 3));

        assertEquals(expectedEvaluators, evaluator.getSubTree());
    }

    @Test
    public void testParseWithCombiningCombinators() {
        String query = "div > p + span.class1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new StructuralEvaluator.Descendant(new Evaluator.Tag("p")));
        expectedEvaluators.add(new StructuralEvaluator.AdjacentSibling(new Evaluator.Class("class1")));

        assertEquals(expectedEvaluators, evaluator.getSubTree());
    }

    @Test
    public void testParseWithAttributeSelectors() {
        String query = "input[type='text']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("input"));
        expectedEvaluators.add(new Evaluator.AttributeWithValue("type", "text"));

        assertEquals(expectedEvaluators, evaluator.getSubTree());
    }

    @Test
    public void testParseWithPseudoClasses() {
        String query = "a:hover";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("a"));
        expectedEvaluators.add(new Evaluator.PseudoClass("hover"));

        assertEquals(expectedEvaluators, evaluator.getSubTree());
    }

    @Test
    public void testParseWithNegation() {
        String query = "div:not(.class1)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new StructuralEvaluator.Not(new Evaluator.Class("class1")));

        assertEquals(expectedEvaluators, evaluator.getSubTree());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseWithInvalidQuery() {
        String query = "div:not(.class1:asdf)";
        QueryParser parser = new QueryParser(query);
        parser.parse();
    }
}