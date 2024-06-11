package org.jsoup.select;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse("div");
        assertNotNull(evaluator);
    }

    @Test
    public void testParseEmptyQuery() {
        Evaluator evaluator = QueryParser.parse("");
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithCombinator() {
        Evaluator evaluator = QueryParser.parse("div > p");
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithMultipleCombinators() {
        Evaluator evaluator = QueryParser.parse("div > p ~ span");
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithAttribute() {
        Evaluator evaluator = QueryParser.parse("a[href]");
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithPseudoSelectors() {
        Evaluator evaluator = QueryParser.parse("div:first-child");
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithNthChild() {
        Evaluator evaluator = QueryParser.parse("div:nth-child(2n+1)");
        assertNotNull(evaluator);
    }

    @Test
    public void testParseWithNegatedSelector() {
        Evaluator evaluator = QueryParser.parse(":not(p)");
        assertNotNull(evaluator);
    }

}