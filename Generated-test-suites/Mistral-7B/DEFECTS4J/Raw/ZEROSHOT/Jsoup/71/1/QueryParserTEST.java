package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.helper.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tests for QueryParser class.
 */
public class QueryParserTest {

    @Test
    public void testParseBasic() {
        String query = "div.class1";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("div"));
        expected.add(new Evaluator.Class("class1"));
        assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseCombined() {
        String query = "div > p.class1";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("div"));
        expected.add(new StructuralEvaluator.ImmediateChild());
        expected.add(new Evaluator.Tag("p"));
        expected.add(new Evaluator.Class("class1"));
        assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseAttribute() {
        String query = "input[type='text']";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("input"));
        expected.add(new Evaluator.AttributeWithValue("type", "text"));
        assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParsePseudoClass() {
        String query = "p:first-child";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("p"));
        expected.add(new Evaluator.IsFirstChild());
        assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParseMultipleSelectors() {
        String query = "div#id1 > p.class1:first-child";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("div"));
        expected.add(new Evaluator.Id("id1"));
        expected.add(new StructuralEvaluator.ImmediateChild());
        expected.add(new Evaluator.Tag("p"));
        expected.add(new Evaluator.Class("class1"));
        expected.add(new Evaluator.IsFirstChild());
        assertEquals(expected, evaluator.getChildren());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        String query = "div gt p"; // 'gt' is not a valid combinator
        QueryParser parser = new QueryParser(query);
        parser.parse();
        fail("Expected Selector.SelectorParseException");
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseMissingElement() {
        String query = ":first-child"; // missing the element tag
        QueryParser parser = new QueryParser(query);
        parser.parse();
        fail("Expected Selector.SelectorParseException");
    }
}