package org.jsoup.select;

import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.parser.TokenQueue;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class QueryParserTest {
    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse("");
        assertNotNull(evaluator);

        evaluator = QueryParser.parse("tag");
        assertNotNull(evaluator);

        evaluator = QueryParser.parse(".class");
        assertNotNull(evaluator);

        evaluator = QueryParser.parse("#id");
        assertNotNull(evaluator);

        evaluator = QueryParser.parse("[attr]");
        assertNotNull(evaluator);

        evaluator = QueryParser.parse("tag[attr]");
        assertNotNull(evaluator);

        evaluator = QueryParser.parse(":has(tag)");
        assertNotNull(evaluator);

        evaluator = QueryParser.parse(":contains(text)");
        assertNotNull(evaluator);

        evaluator = QueryParser.parse(":matches(regex)");
        assertNotNull(evaluator);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        Evaluator evaluator = QueryParser.parse(":has(");
    }

    @Test
    public void testCombinator() {
        QueryParser parser = new QueryParser("");
        parser.combinator(',');
        parser.combinator('>');
        parser.combinator('+');
        parser.combinator('~');
        parser.combinator(' ');
    }

    @Test
    public void testConsumeSubQuery() {
        QueryParser parser = new QueryParser("");
        parser.consumeSubQuery();
    }

    @Test
    public void testFindElements() {
        QueryParser parser = new QueryParser("");
        parser.findElements();
    }

    @Test
    public void testAllElements() {
        QueryParser parser = new QueryParser("");
        parser.allElements();
    }

    @Test
    public void testIndexLessThan() {
        QueryParser parser = new QueryParser("");
        parser.indexLessThan();
    }

    @Test
    public void testIndexGreaterThan() {
        QueryParser parser = new QueryParser("");
        parser.indexGreaterThan();
    }

    @Test
    public void testIndexEquals() {
        QueryParser parser = new QueryParser("");
        parser.indexEquals();
    }

    @Test
    public void testHas() {
        QueryParser parser = new QueryParser("");
        parser.has();
    }

    @Test
    public void testContains() {
        QueryParser parser = new QueryParser("");
        parser.contains(false);
        parser.contains(true);
    }

    @Test
    public void testMatches() {
        QueryParser parser = new QueryParser("");
        parser.matches(false);
        parser.matches(true);
    }

    @Test
    public void testNot() {
        QueryParser parser = new QueryParser("");
        parser.not();
    }

    @Test
    public void testById() {
        QueryParser parser = new QueryParser("");
        parser.byId();
    }

    @Test
    public void testByClass() {
        QueryParser parser = new QueryParser("");
        parser.byClass();
    }

    @Test
    public void testByTag() {
        QueryParser parser = new QueryParser("");
        parser.byTag();
    }

    @Test
    public void testByAttribute() {
        QueryParser parser = new QueryParser("");
        parser.byAttribute();
    }
}