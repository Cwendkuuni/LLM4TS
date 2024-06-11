import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div.class";
        Evaluator eval = QueryParser.parse(query);
        assertNotNull(eval);
    }

    @Test
    public void testConstructor() {
        String query = "div.class";
        QueryParser parser = new QueryParser(query);
        assertNotNull(parser);
    }

    @Test
    public void testParseEmpty() {
        String query = "";
        Evaluator eval = QueryParser.parse(query);
        assertNotNull(eval);
    }

    @Test
    public void testParseCombinator() {
        String query = "div.class > p";
        Evaluator eval = QueryParser.parse(query);
        assertNotNull(eval);
    }

    @Test
    public void testCombinator() {
        QueryParser parser = new QueryParser("div.class > p");
        parser.combinator('>');
        assertNotNull(parser);
    }

    @Test
    public void testConsumeSubQuery() {
        QueryParser parser = new QueryParser("div.class > p");
        String subQuery = parser.consumeSubQuery();
        assertNotNull(subQuery);
    }

    @Test
    public void testFindElements() {
        QueryParser parser = new QueryParser("div.class");
        parser.findElements();
        assertNotNull(parser);
    }

    @Test
    public void testById() {
        QueryParser parser = new QueryParser("div#id");
        parser.byId();
        assertNotNull(parser);
    }

    @Test
    public void testByClass() {
        QueryParser parser = new QueryParser("div.class");
        parser.byClass();
        assertNotNull(parser);
    }

    @Test
    public void testByTag() {
        QueryParser parser = new QueryParser("div");
        parser.byTag();
        assertNotNull(parser);
    }

    @Test
    public void testByAttribute() {
        QueryParser parser = new QueryParser("[name=value]");
        parser.byAttribute();
        assertNotNull(parser);
    }

    @Test
    public void testAllElements() {
        QueryParser parser = new QueryParser("*");
        parser.allElements();
        assertNotNull(parser);
    }

    @Test
    public void testIndexLessThan() {
        QueryParser parser = new QueryParser(":lt(1)");
        parser.indexLessThan();
        assertNotNull(parser);
    }

    @Test
    public void testIndexGreaterThan() {
        QueryParser parser = new QueryParser(":gt(1)");
        parser.indexGreaterThan();
        assertNotNull(parser);
    }

    @Test
    public void testIndexEquals() {
        QueryParser parser = new QueryParser(":eq(1)");
        parser.indexEquals();
        assertNotNull(parser);
    }

    @Test
    public void testHas() {
        QueryParser parser = new QueryParser(":has(div)");
        parser.has();
        assertNotNull(parser);
    }

    @Test
    public void testContains() {
        QueryParser parser = new QueryParser(":contains(text)");
        parser.contains(false);
        assertNotNull(parser);
    }

    @Test
    public void testContainsOwn() {
        QueryParser parser = new QueryParser(":containsOwn(text)");
        parser.contains(true);
        assertNotNull(parser);
    }

    @Test
    public void testMatches() {
        QueryParser parser = new QueryParser(":matches(regex)");
        parser.matches(false);
        assertNotNull(parser);
    }

    @Test
    public void testMatchesOwn() {
        QueryParser parser = new QueryParser(":matchesOwn(regex)");
        parser.matches(true);
        assertNotNull(parser);
    }

    @Test
    public void testNot() {
        QueryParser parser = new QueryParser(":not(div)");
        parser.not();
        assertNotNull(parser);
    }
}


