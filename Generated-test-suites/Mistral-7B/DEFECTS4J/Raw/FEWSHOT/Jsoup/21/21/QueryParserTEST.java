package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.helper.StringUtil;
import org.jsoup.parser.TokenQueue;

/**
 * Test class for QueryParser.
 */
@RunWith(Parameterized.class)
public class QueryParserTest {
    private QueryParser parser;
    private String query;
    private Evaluator expectedEvaluator;

    public QueryParserTest(String query, Evaluator expectedEvaluator) {
        this.query = query;
        this.expectedEvaluator = expectedEvaluator;
    }

    @Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {
                        ".foo",
                        new Evaluator.Class("foo")
                },
                {
                        "div.foo",
                        new CombiningEvaluator.And(
                                new Evaluator.Tag("div"),
                                new Evaluator.Class("foo")
                        )
                },
                {
                        "div > p",
                        new CombiningEvaluator.And(
                                new Evaluator.Tag("div"),
                                new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("p"))
                        )
                },
                {
                        "div > p > span",
                        new CombiningEvaluator.And(
                                new Evaluator.Tag("div"),
                                new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("p")),
                                new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("span"))
                        )
                },
                {
                        "div > p > span:first-child",
                        new CombiningEvaluator.And(
                                new Evaluator.Tag("div"),
                                new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("p")),
                                new StructuralEvaluator.FirstChild(new Evaluator.Tag("span"))
                        )
                },
                {
                        "input[type=text]",
                        new Evaluator.Attribute(new AttributeSpecification("type", "text"))
                },
                {
                        "#foo",
                        new Evaluator.Id("foo")
                },
                {
                        "input#foo[type=text]",
                        new CombiningEvaluator.And(
                                new Evaluator.Id("foo"),
                                new Evaluator.Attribute(new AttributeSpecification("type", "text")))
                },
                {
                        "a[href^='https']",
                        new Evaluator.AttributeWithValueStarting(new AttributeSpecification("href"), "https")
                },
                {
                        "input:checked",
                        new Evaluator.PseudoClass("checked")
                },
                {
                        "input:not(:checked)",
                        new StructuralEvaluator.Not(new Evaluator.PseudoClass("checked"))
                },
                {
                        "div:has(p)",
                        new StructuralEvaluator.Has(new Evaluator.Tag("p"))
                },
                {
                        "div:containsOwn('test')",
                        new Evaluator.ContainsOwnText("test")
                },
                {
                        "div:contains('test')",
                        new Evaluator.ContainsText("test")
                },
                {
                        "div:matchesOwn(p:first-child)",
                        new Evaluator.MatchesOwn(Pattern.compile("p:first-child"))
                },
                {
                        "div:matches(p:first-child)",
                        new Evaluator.Matches(Pattern.compile("p:first-child"))
                }
        });
    }

    @Before
    public void setUp() {
        parser = new QueryParser(query);
    }

    @Test
    public void testParse() {
        Evaluator evaluator = parser.parse();
        assertEquals(expectedEvaluator, evaluator);
    }
}