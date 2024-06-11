package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.parser.TokenQueue;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

public class QueryParserTest {
    private static QueryParser parser;

    @BeforeClass
    public static void setUp() {
        Document doc = mock(Document.class);
        parser = Mockito.spy(new QueryParser(doc));
    }

    @Test
    public void testParse() {
        Evaluator evaluator = parser.parse("body");
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWhenQueryIsEmpty() {
        QueryParser p = new QueryParser("");
        Evaluator evaluator = p.parse();
        assertNull(evaluator);
    }

    @Test
    public void testParseWhenQueryContainsCombinators() {
        Evaluator evaluator = parser.parse("div > p + span ~ a");
        assertNotNull(evaluator);
    }

    @Test
    public void testCombinator() {
        parser.evals.add(new Evaluator.Id("id"));
        parser.evals.add(new Evaluator.Tag("tag"));
        parser.evals.add(new Evaluator.Class("class"));
        parser.combinator('>');
        assertEquals(1, parser.evals.size());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testCombinatorWithUnknownCombinator() {
        parser.combinator('#');
    }

    @Test
    public void testConsumeSubQuery() {
        parser.tq = new TokenQueue("tag[attr~=value]");
        String subQuery = parser.consumeSubQuery();
        assertEquals("tag[attr~=value]", subQuery);
    }

    @Test
    public void testFindElements() {
        parser.tq = new TokenQueue("tag[attr~=value]");
        parser.findElements();
        assertEquals(1, parser.evals.size());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testFindElementsWithUnhandledToken() {
        parser.tq = new TokenQueue("tag[attr~=value");
        parser.findElements();
    }

    @Test
    public void testAllElements() {
        parser.allElements();
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testIndexLessThan() {
        parser.indexLessThan();
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testIndexGreaterThan() {
        parser.indexGreaterThan();
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testIndexEquals() {
        parser.indexEquals();
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testHas() {
        parser.tq = new TokenQueue("tag[attr~=value]");
        parser.has();
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testContains() {
        parser.tq = new TokenQueue("text");
        parser.contains(false);
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testContainsOwn() {
        parser.tq = new TokenQueue("text");
        parser.contains(true);
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testMatches() {
        parser.tq = new TokenQueue("regex");
        parser.matches(false);
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testMatchesOwn() {
        parser.tq = new TokenQueue("regex");
        parser.matches(true);
        assertEquals(1, parser.evals.size());
    }

    @Test
    public void testNot() {
        parser.tq = new TokenQueue("tag[attr~=value]");
        parser.not();
        assertEquals(1, parser.evals.size());
    }
}