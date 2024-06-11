package org.jsoup.select;

import static org.junit.Assert.*;

import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for QueryParser
 */
public class QueryParserTest {

    @org.junit.Test
    public void testParse_EmptyQuery() {
        String query = "";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        assertEquals(expected, evaluator.getSubExpressions());
    }

    @org.junit.Test
    public void testParse_SimpleTag() {
        String query = "p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("p"));
        assertEquals(expected, evaluator.getSubExpressions());
    }

    @org.junit.Test
    public void testParse_ClassSelector() {
        String query = ".class-name";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Class("class-name"));
        assertEquals(expected, evaluator.getSubExpressions());
    }

    @org.junit.Test
    public void testParse_IdSelector() {
        String query = "#id";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Id("id"));
        assertEquals(expected, evaluator.getSubExpressions());
    }

    @org.junit.Test
    public void testParse_ElementWithAttribute() {
        String query = "div[data-test='test']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("div"));
        Evaluator attributeEval = new Evaluator.AttributeWithValue("data-test", "test");
        expected.add(attributeEval);
        assertEquals(expected, evaluator.getSubExpressions());
    }

    @org.junit.Test
    public void testParse_MultipleSelectors() {
        String query = "p.class-name#id";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("p"));
        expected.add(new Evaluator.Class("class-name"));
        expected.add(new Evaluator.Id("id"));
        assertEquals(expected, evaluator.getSubExpressions());
    }

    @org.junit.Test
    public void testParse_CombinedSelectors() {
        String query = "p > div.class-name > ul.list";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("p"));
        expected.add(new StructuralEvaluator.ImmediateChild());
        expected.add(new Evaluator.Tag("div"));
        expected.add(new Evaluator.Class("class-name"));
        expected.add(new StructuralEvaluator.ImmediateChild());
        expected.add(new Evaluator.Tag("ul"));
        expected.add(new Evaluator.Class("list"));
        assertEquals(expected, evaluator.getSubExpressions());
    }

    @org.junit.Test(expected = Selector.SelectorParseException.class)
    public void testParse_InvalidQuery() {
        String query = "p>div:not[data-test='test']>#id"; // This query is invalid because of the order of combinators and selectors
        QueryParser parser = new QueryParser(query);
        parser.parse(); // The test should fail with Selector.SelectorParseException
    }

    @org.junit.Test(expected = Selector.SelectorParseException.class)
    public void testParse_InvalidCombinator() {
        String query = "p~=div"; // The '~=' combinator is not valid
        QueryParser parser = new QueryParser(query);
        parser.parse(); // The test should fail with Selector.SelectorParseException
    }

    @org.junit.Test(expected = Selector.SelectorParseException.class)
    public void testParse_InvalidAttributeSelector() {
        String query = "p[data-test]"; // The query should fail as it is missing the attribute value
        QueryParser parser = new QueryParser(query);
        parser.parse(); // The test should fail with Selector.SelectorParseException
    }

    @org.junit.Test
    public void testParse_PseudoClassSelectors() {
        String query = ":empty";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.IsEmpty());
        assertEquals(expected, evaluator.getSubExpressions());
    }

    @org.junit.Test
    public void testParse_NthChild() {
        String query = "li:nth-child(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.IsNthChild(1, 2));
        assertEquals(expected, evaluator.getSubExpressions());
    }

    @org.junit.Test
    public void testParse_NthLastChild() {
        String query = "li:nth-last-child(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.IsNthLastChild(1, 2));
        assertEquals(expected, evaluator.getSubExpressions());
    }

    @org.junit.Test
    public void testParse_NthOfType() {
        String query = "li:nth-of-type(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.IsNthOfType(1, 2));
        assertEquals(expected, evaluator.getSubExpressions());
    }

    @org.junit.Test
    public void testParse_NthLastOfType() {
        String query = "li:nth-last-of-type(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.IsNthLastOfType(1, 2));
        assertEquals(expected, evaluator.getSubExpressions());
    }

    @org.junit.Test
    public void testParse_PseudoElementSelectors() {
        String query = "li:first-child";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.IsFirstChild());
        assertEquals(expected, evaluator.getSubExpressions());
    }
}