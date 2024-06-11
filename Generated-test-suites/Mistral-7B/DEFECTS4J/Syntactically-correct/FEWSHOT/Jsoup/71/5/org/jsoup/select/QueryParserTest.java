package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.parser.TokenQueue;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Unit tests for QueryParser class.
 */
public class QueryParserTest {

    @Test
    public void testParseSimpleQueries() {
        String query = "p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("p"));
        assertEquals(expectedEvaluators, evaluator.toList());

        query = "div p";
        evaluator = QueryParser.parse(query);
        assertEquals(expectedEvaluators, evaluator.toList());

        query = "p#id";
        evaluator = QueryParser.parse(query);
        expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Id("id"));
        expectedEvaluators.add(new Evaluator.Tag("p"));
        assertEquals(expectedEvaluators, evaluator.toList());

        query = "p.class";
        evaluator = QueryParser.parse(query);
        expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Class("class"));
        expectedEvaluators.add(new Evaluator.Tag("p"));
        assertEquals(expectedEvaluators, evaluator.toList());

        query = "p[title]";
        evaluator = QueryParser.parse(query);
        expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Attribute("title"));
        expectedEvaluators.add(new Evaluator.Tag("p"));
        assertEquals(expectedEvaluators, evaluator.toList());

        query = "p:first-child";
        evaluator = QueryParser.parse(query);
        expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.IsFirstChild());
        expectedEvaluators.add(new Evaluator.Tag("p"));
        assertEquals(expectedEvaluators, evaluator.toList());
    }

    @Test
    public void testParseComplexQueries() {
        String query = "div > ul > li:first-child";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.IsFirstChild());
        expectedEvaluators.add(new Evaluator.Tag("li"));
        expectedEvaluators.add(new Evaluator.Tag("ul"));
        expectedEvaluators.add(new Evaluator.Tag("div"));
        assertEquals(expectedEvaluators, evaluator.toList());

        query = "div > ul > li:last-child";
        evaluator = QueryParser.parse(query);
        expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.IsLastChild());
        expectedEvaluators.add(new Evaluator.Tag("li"));
        expectedEvaluators.add(new Evaluator.Tag("ul"));
        expectedEvaluators.add(new Evaluator.Tag("div"));
        assertEquals(expectedEvaluators, evaluator.toList());

        query = "div > ul > li:only-child";
        evaluator = QueryParser.parse(query);
        expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.IsOnlyChild());
        expectedEvaluators.add(new Evaluator.Tag("li"));
        expectedEvaluators.add(new Evaluator.Tag("ul"));
        expectedEvaluators.add(new Evaluator.Tag("div"));
        assertEquals(expectedEvaluators, evaluator.toList());

        query = "div > ul > li:only-of-type";
        evaluator = QueryParser.parse(query);
        expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.IsOnlyOfType());
        expectedEvaluators.add(new Evaluator.Tag("li"));
        expectedEvaluators.add(new Evaluator.Tag("ul"));
        expectedEvaluators.add(new Evaluator.Tag("div"));
        assertEquals(expectedEvaluators, evaluator.toList());

        query = "div > ul > li:nth-child(2)";
        evaluator = QueryParser.parse(query);
        expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.IsNthChild(2, 0));
        expectedEvaluators.add(new Evaluator.Tag("li"));
        expectedEvaluators.add(new Evaluator.Tag("ul"));
        expectedEvaluators.add(new Evaluator.Tag("div"));
        assertEquals(expectedEvaluators, evaluator.toList());

        query = "div > ul > li:nth-last-child(2)";
        evaluator = QueryParser.parse(query);
        expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.IsNthLastChild(2, 0));
        expectedEvaluators.add(new Evaluator.Tag("li"));
        expectedEvaluators.add(new Evaluator.Tag("ul"));
        expectedEvaluators.add(new Evaluator.Tag("div"));
        assertEquals(expectedEvaluators, evaluator.toList());

        query = "div > ul > li:nth-of-type(2)";
        evaluator = QueryParser.parse(query);
        expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.IsNthOfType(2, 0));
        expectedEvaluators.add(new Evaluator.Tag("li"));
        expectedEvaluators.add(new Evaluator.Tag("ul"));
        expectedEvaluators.add(new Evaluator.Tag("div"));
        assertEquals(expectedEvaluators, evaluator.toList());

        query = "div > ul > li:nth-last-of-type(2)";
        evaluator = QueryParser.parse(query);
        expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.IsNthLastOfType(2, 0));
        expectedEvaluators.add(new Evaluator.Tag("li"));
        expectedEvaluators.add(new Evaluator.Tag("ul"));
        expectedEvaluators.add(new Evaluator.Tag("div"));
        assertEquals(expectedEvaluators, evaluator.toList());

        query = "div > ul > li:not(:first-child)";
        evaluator = QueryParser.parse(query);
        expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new StructuralEvaluator.Not(new Evaluator.IsFirstChild()));
        expectedEvaluators.add(new Evaluator.Tag("li"));
        expectedEvaluators.add(new Evaluator.Tag("ul"));
        expectedEvaluators.add(new Evaluator.Tag("div"));
        assertEquals(expectedEvaluators, evaluator.toList());

        query = "div > ul > li:contains(text)";
        evaluator = QueryParser.parse(query);
        expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.ContainsText("text"));
        expectedEvaluators.add(new Evaluator.Tag("li"));
        expectedEvaluators.add(new Evaluator.Tag("ul"));
        expectedEvaluators.add(new Evaluator.Tag("div"));
        assertEquals(expectedEvaluators, evaluator.toList());

        query = "div > ul > li:containsOwn(text)";
        evaluator = QueryParser.parse(query);
        expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.ContainsOwnText("text"));
        expectedEvaluators.add(new Evaluator.Tag("li"));
        expectedEvaluators.add(new Evaluator.Tag("ul"));
        expectedEvaluators.add(new Evaluator.Tag("div"));
        assertEquals(expectedEvaluators, evaluator.toList());

        query = "div > ul > li:matches(regex)";
        evaluator = QueryParser.parse(query);
        expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Matches(Pattern.compile("regex")));
        expectedEvaluators.add(new Evaluator.Tag("li"));
        expectedEvaluators.add(new Evaluator.Tag("ul"));
        expectedEvaluators.add(new Evaluator.Tag("div"));
        assertEquals(expectedEvaluators, evaluator.toList());

        query = "div > ul > li:matchesOwn(regex)";
        evaluator = QueryParser.parse(query);
        expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.MatchesOwn(Pattern.compile("regex")));
        expectedEvaluators.add(new Evaluator.Tag("li"));
        expectedEvaluators.add(new Evaluator.Tag("ul"));
        expectedEvaluators.add(new Evaluator.Tag("div"));
        assertEquals(expectedEvaluators, evaluator.toList());
    }

    @Test
    public void testParseInvalidQueries() {
        String query = "div > ul > li:not:first-child";
        assertNull(QueryParser.parse(query));

        query = "div > ul > li:nth-child(2) > ul > li";
        assertNull(QueryParser.parse(query));

        query = "div > ul > li.class:first-child";
        assertNull(QueryParser.parse(query));

        query = "div > ul > li:nth-of-type(2):first-child";
        assertNull(QueryParser.parse(query));

        query = "div > ul > li:nth-child(2):nth-last-child(1)";
        assertNull(QueryParser.parse(query));

        query = "div > ul > li > div";
        assertNull(QueryParser.parse(query));

        query = "div > ul > li:not(li)";
        assertNull(QueryParser.parse(query));

        query = "div > ul > li:nth-child(2):not(li)";
        assertNull(QueryParser.parse(query));

        query = "div > ul > li:nth-last-child(1):not(li)";
        assertNull(QueryParser.parse(query));

        query = "div > ul > li:nth-child(-1)";
        assertNull(QueryParser.parse(query));

        query = "div > ul > li:nth-child(0:first-of-type)";
        assertNull(QueryParser.parse(query));

        query = "div > ul > li:nth-child(0):nth-last-child(1)";
        assertNull(QueryParser.parse(query));

        query = "div > ul > li:nth-child(0):nth-of-type(2)";
        assertNull(QueryParser.parse(query));

        query = "div > ul > li:nth-last-child(-1)";
        assertNull(QueryParser.parse(query));

        query = "div > ul > li:nth-last-child(0:last-of-type)";
        assertNull(QueryParser.parse(query));

        query = "div > ul > li:nth-last-child(0):nth-of-type(2)";
        assertNull(QueryParser.parse(query));

        query = "div > ul > li:nth-last-child(0):not(li)";
        assertNull(QueryParser.parse(query));

        query = "div > ul > li:nth-last-child(0):not(li):first-child";
        assertNull(QueryParser.parse(query));

        query = "div > ul > li:nth-last-child(0):not(li):last-child";
        assertNull(QueryParser.parse(query));

        query = "div > ul > li:nth-last-child(0):only-child";
        assertNull(QueryParser.parse(query));

        query = "div > ul > li:nth-last-child(0):only-of-type";
        assertNull(QueryParser.parse(query));
    }
}