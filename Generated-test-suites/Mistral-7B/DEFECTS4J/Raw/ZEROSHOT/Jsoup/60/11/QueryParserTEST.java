package org.jsoup.select;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.helper.StringUtil;
import org.jsoup.parser.TokenQueue;

import org.junit.Test;

/**
 * Tests for QueryParser class
 */
public class QueryParserTest {

    @Test
    public void testParseBasicSelectors() {
        String query = "#id1 .class1";
        Evaluator eval = QueryParser.parse(query);
        List<Evaluator> expected = new ArrayList<Evaluator>() {{
            add(new Evaluator.Id("id1"));
            add(new Evaluator.Class("class1"));
        }};
        assertEquals(expected, eval.getChildren());
    }

    @Test
    public void testParseDescendantSelectors() {
        String query = "div p";
        Evaluator eval = QueryParser.parse(query);
        List<Evaluator> expected = new ArrayList<Evaluator>() {{
            add(new Evaluator.Tag("div"));
            add(new Evaluator.Descendant());
            add(new Evaluator.Tag("p"));
        }};
        assertEquals(expected, eval.getChildren());
    }

    @Test
    public void testParseAttributeSelectors() {
        String query = "[href]";
        Evaluator eval = QueryParser.parse(query);
        List<Evaluator> expected = new ArrayList<Evaluator>() {{
            add(new Evaluator.Attribute("href"));
        }};
        assertEquals(expected, eval.getChildren());

        String query2 = "[href='http://example.com']";
        Evaluator eval2 = QueryParser.parse(query2);
        List<Evaluator> expected2 = new ArrayList<Evaluator>() {{
            add(new Evaluator.AttributeWithValue("href", "http://example.com"));
        }};
        assertEquals(expected2, eval2.getChildren());
    }

    @Test
    public void testParseCombinatorSelectors() {
        String query = "div > p";
        Evaluator eval = QueryParser.parse(query);
        List<Evaluator> expected = new ArrayList<Evaluator>() {{
            add(new Evaluator.Tag("div"));
            add(new Evaluator.ImmediateChild());
            add(new Evaluator.Tag("p"));
        }};
        assertEquals(expected, eval.getChildren());

        String query2 = "div + p";
        Evaluator eval2 = QueryParser.parse(query2);
        List<Evaluator> expected2 = new ArrayList<Evaluator>() {{
            add(new Evaluator.Tag("div"));
            add(new Evaluator.PreviousSibling());
            add(new Evaluator.Tag("p"));
        }};
        assertEquals(expected2, eval2.getChildren());
    }

    @Test
    public void testParsePseudoClasses() {
        String query = "div:first-child";
        Evaluator eval = QueryParser.parse(query);
        List<Evaluator> expected = new ArrayList<Evaluator>() {{
            add(new Evaluator.IsFirstChild());
            add(new Evaluator.Tag("div"));
        }};
        assertEquals(expected, eval.getChildren());

        String query2 = "div:last-child";
        Evaluator eval2 = QueryParser.parse(query2);
        List<Evaluator> expected2 = new ArrayList<Evaluator>() {{
            add(new Evaluator.IsLastChild());
            add(new Evaluator.Tag("div"));
        }};
        assertEquals(expected2, eval2.getChildren());

        String query3 = "div:empty";
        Evaluator eval3 = QueryParser.parse(query3);
        List<Evaluator> expected3 = new ArrayList<Evaluator>() {{
            add(new Evaluator.IsEmpty());
            add(new Evaluator.Tag("div"));
        }};
        assertEquals(expected3, eval3.getChildren());
    }

    @Test
    public void testParseNthChildSelectors() {
        String query = "div:nth-child(2)";
        Evaluator eval = QueryParser.parse(query);
        List<Evaluator> expected = new ArrayList<Evaluator>() {{
            add(new Evaluator.IsNthChild(2, 0));
            add(new Evaluator.Tag("div"));
        }};
        assertEquals(expected, eval.getChildren());

        String query2 = "div:nth-last-child(2)";
        Evaluator eval2 = QueryParser.parse(query2);
        List<Evaluator> expected2 = new ArrayList<Evaluator>() {{
            add(new Evaluator.IsNthLastChild(2, 0));
            add(new Evaluator.Tag("div"));
        }};
        assertEquals(expected2, eval2.getChildren());

        String query3 = "div:nth-of-type(2)";
        Evaluator eval3 = QueryParser.parse(query3);
        List<Evaluator> expected3 = new ArrayList<Evaluator>() {{
            add(new Evaluator.IsNthOfType(2, 0));
            add(new Evaluator.Tag("div"));
        }};
        assertEquals(expected3, eval3.getChildren());

        String query4 = "div:nth-last-of-type(2)";
        Evaluator eval4 = QueryParser.parse(query4);
        List<Evaluator> expected4 = new ArrayList<Evaluator>() {{
            add(new Evaluator.IsNthLastOfType(2, 0));
            add(new Evaluator.Tag("div"));
        }};
        assertEquals(expected4, eval4.getChildren());

        String query5 = "div:nth-child(odd)";
        Evaluator eval5 = QueryParser.parse(query5);
        List<Evaluator> expected5 = new ArrayList<Evaluator>() {{
            add(new Evaluator.IsNthChild(0, 2));
            add(new Evaluator.Tag("div"));
        }};
        assertEquals(expected5, eval5.getChildren());

        String query6 = "div:nth-child(even)";
        Evaluator eval6 = QueryParser.parse(query6);
        List<Evaluator> expected6 = new ArrayList<Evaluator>() {{
            add(new Evaluator.IsNthChild(0, 1));
            add(new Evaluator.Tag("div"));
        }};
        assertEquals(expected6, eval6.getChildren());
    }

    @Test
    public void testParsePseudoElements() {
        String query = "div > ul > li:first-child:before";
        Evaluator eval = QueryParser.parse(query);
        List<Evaluator> expected = new ArrayList<Evaluator>() {{
            add(new Evaluator.Tag("div"));
            add(new Evaluator.ImmediateChild());
            add(new Evaluator.Tag("ul"));
            add(new Evaluator.ImmediateChild());
            add(new Evaluator.Tag("li"));
            add(new Evaluator.IsFirstChild());
            add(new Evaluator.PseudoElement("before"));
        }};
        assertEquals(expected, eval.getChildren());

        String query2 = "div > ul > li:last-child:after";
        Evaluator eval2 = QueryParser.parse(query2);
        List<Evaluator> expected2 = new ArrayList<Evaluator>() {{
            add(new Evaluator.Tag("div"));
            add(new Evaluator.ImmediateChild());
            add(new Evaluator.Tag("ul"));
            add(new Evaluator.ImmediateChild());
            add(new Evaluator.Tag("li"));
            add(new Evaluator.IsLastChild());
            add(new Evaluator.PseudoElement("after"));
        }};
        assertEquals(expected2, eval2.getChildren());
    }

    @Test
    public void testParseAttributeOperators() {
        String query = "[href^='http']";
        Evaluator eval = QueryParser.parse(query);
        List<Evaluator> expected = new ArrayList<Evaluator>() {{
            add(new Evaluator.AttributeStarting("href", "http"));
            add(new Evaluator.Tag("*"));
        }};
        assertEquals(expected, eval.getChildren());

        String query2 = "[href$='.html]";
        Evaluator eval2 = QueryParser.parse(query2);
        List<Evaluator> expected2 = new ArrayList<Evaluator>() {{
            add(new Evaluator.AttributeEnding("href", ".html"));
            add(new Evaluator.Tag("*"));
        }};
        assertEquals(expected2, eval2.getChildren());

        String query3 = "[href*='jsoup']";
        Evaluator eval3 = QueryParser.parse(query3);
        List<Evaluator> expected3 = new ArrayList<Evaluator>() {{
            add(new Evaluator.AttributeContaining("href", "jsoup"));
            add(new Evaluator.Tag("*"));
        }};
        assertEquals(expected3, eval3.getChildren());

        String query4 = "[href~='class1 class2']";
        Evaluator eval4 = QueryParser.parse(query4);
        List<Evaluator> expected4 = new ArrayList<Evaluator>() {{
            add(new Evaluator.AttributeMatching("href", Pattern.compile("(class1|class2)")));
            add(new Evaluator.Tag("*"));
        }};
        assertEquals(expected4, eval4.getChildren());
    }

    @Test
    public void testParseCombiningSelectors() {
        String query = "div > p, div > ul > li";
        Evaluator eval = QueryParser.parse(query);
        List<Evaluator> expected = new ArrayList<Evaluator>() {{
            add(new Evaluator.Tag("div"));
            add(new Evaluator.ImmediateChild());
            add(new Evaluator.Tag("p"));
            add(new CombiningEvaluator.Or());
            add(new Evaluator.Tag("div"));
            add(new Evaluator.ImmediateChild());
            add(new Evaluator.Tag("ul"));
            add(new Evaluator.ImmediateChild());
            add(new Evaluator.Tag("li"));
        }};
        assertEquals(expected, eval.getChildren());
    }
}