package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        // Test case 1
        String query1 = ", > + ~ ";
        Evaluator evaluator1 = QueryParser.parse(query1);
        assertNotNull(evaluator1);

        // Test case 2
        String query2 = "div#id.class[attr]";
        Evaluator evaluator2 = QueryParser.parse(query2);
        assertNotNull(evaluator2);

        // Test case 3
        String query3 = "div";
        Evaluator evaluator3 = QueryParser.parse(query3);
        assertNotNull(evaluator3);
    }

    @Test
    public void testCombinator() {
        QueryParser parser = new QueryParser("div > p + span ~ a");
        parser.combinator(',');
        assertNotNull(parser.evals);

        parser.combinator('>');
        assertNotNull(parser.evals);

        parser.combinator('+');
        assertNotNull(parser.evals);

        parser.combinator('~');
        assertNotNull(parser.evals);
    }

    @Test
    public void testConsumeSubQuery() {
        QueryParser parser = new QueryParser("div > p#id.class[attr]");
        String subQuery = parser.consumeSubQuery();
        assertNotNull(subQuery);

        QueryParser parser2 = new QueryParser("div[attr]");
        String subQuery2 = parser2.consumeSubQuery();
        assertNotNull(subQuery2);

        QueryParser parser3 = new QueryParser("div");
        String subQuery3 = parser3.consumeSubQuery();
        assertNotNull(subQuery3);
    }

    @Test
    public void testFindElements() {
        QueryParser parser = new QueryParser("div#id.class[attr]");
        parser.findElements();
        assertNotNull(parser.evals);

        QueryParser parser2 = new QueryParser("div");
        parser2.findElements();
        assertNotNull(parser2.evals);
    }

    @Test
    public void testById() {
        QueryParser parser = new QueryParser("div#id");
        parser.byId();
        assertNotNull(parser.evals);
    }

    @Test
    public void testByClass() {
        QueryParser parser = new QueryParser("div.class");
        parser.byClass();
        assertNotNull(parser.evals);
    }

    @Test
    public void testByTag() {
        QueryParser parser = new QueryParser("div");
        parser.byTag();
        assertNotNull(parser.evals);
    }

    @Test
    public void testByAttribute() {
        QueryParser parser = new QueryParser("div[attr=value]");
        parser.byAttribute();
        assertNotNull(parser.evals);
    }

    @Test
    public void testAllElements() {
        QueryParser parser = new QueryParser("*");
        parser.allElements();
        assertNotNull(parser.evals);
    }

    @Test
    public void testIndexLessThan() {
        QueryParser parser = new QueryParser(":lt(1)");
        parser.indexLessThan();
        assertNotNull(parser.evals);
    }

    @Test
    public void testIndexGreaterThan() {
        QueryParser parser = new QueryParser(":gt(1)");
        parser.indexGreaterThan();
        assertNotNull(parser.evals);
    }

    @Test
    public void testIndexEquals() {
        QueryParser parser = new QueryParser(":eq(1)");
        parser.indexEquals();
        assertNotNull(parser.evals);
    }

    @Test
    public void testHas() {
        QueryParser parser = new QueryParser(":has(div)");
        parser.has();
        assertNotNull(parser.evals);
    }

    @Test
    public void testContains() {
        QueryParser parser = new QueryParser(":contains(text)");
        parser.contains(false);
        assertNotNull(parser.evals);

        QueryParser parser2 = new QueryParser(":containsOwn(text)");
        parser2.contains(true);
        assertNotNull(parser2.evals);
    }

    @Test
    public void testMatches() {
        QueryParser parser = new QueryParser(":matches(regex)");
        parser.matches(false);
        assertNotNull(parser.evals);

        QueryParser parser2 = new QueryParser(":matchesOwn(regex)");
        parser2.matches(true);
        assertNotNull(parser2.evals);
    }

    @Test
    public void testNot() {
        QueryParser parser = new QueryParser(":not(div)");
        parser.not();
        assertNotNull(parser.evals);
    }
}