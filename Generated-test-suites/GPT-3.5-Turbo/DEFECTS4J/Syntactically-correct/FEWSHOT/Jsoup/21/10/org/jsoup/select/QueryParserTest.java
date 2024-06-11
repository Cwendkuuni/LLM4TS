package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.parser.TokenQueue;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div.class";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Class);
        Evaluator.Class classEvaluator = (Evaluator.Class) evaluator;
        assertEquals("class", classEvaluator.className());

        query = "div#id";
        evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Id);
        Evaluator.Id idEvaluator = (Evaluator.Id) evaluator;
        assertEquals("id", idEvaluator.id());

        query = "div[attr=value]";
        evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
        Evaluator.AttributeWithValue attributeWithValueEvaluator = (Evaluator.AttributeWithValue) evaluator;
        assertEquals("attr", attributeWithValueEvaluator.key());
        assertEquals("value", attributeWithValueEvaluator.value());

        query = "div :has(span)";
        evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof StructuralEvaluator.Has);

        query = "div :contains(text)";
        evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.ContainsText);

        query = "div:matches(regex)";
        evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Matches);

        query = "div:not(span)";
        evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof StructuralEvaluator.Not);
    }

    @Test
    public void testConsumeSubQuery() {
        QueryParser queryParser = new QueryParser("div.class");
        TokenQueue tq = new TokenQueue("div .class");
        queryParser.parse();

        String subQuery = queryParser.consumeSubQuery();
        assertEquals("div", subQuery);
    }

    @Test
    public void testById() {
        QueryParser queryParser = new QueryParser("div#id");
        TokenQueue tq = new TokenQueue("div#id");
        queryParser.parse();

        queryParser.byId();
        assertTrue(queryParser.evals.get(0) instanceof Evaluator.Id);
        Evaluator.Id idEvaluator = (Evaluator.Id) queryParser.evals.get(0);
        assertEquals("id", idEvaluator.id());
    }

    @Test
    public void testByClass() {
        QueryParser queryParser = new QueryParser("div.class");
        TokenQueue tq = new TokenQueue("div.class");
        queryParser.parse();

        queryParser.byClass();
        assertTrue(queryParser.evals.get(0) instanceof Evaluator.Class);
        Evaluator.Class classEvaluator = (Evaluator.Class) queryParser.evals.get(0);
        assertEquals("class", classEvaluator.className());
    }

    @Test
    public void testByTag() {
        QueryParser queryParser = new QueryParser("div");
        TokenQueue tq = new TokenQueue("div");
        queryParser.parse();

        queryParser.byTag();
        assertTrue(queryParser.evals.get(0) instanceof Evaluator.Tag);
        Evaluator.Tag tagEvaluator = (Evaluator.Tag) queryParser.evals.get(0);
        assertEquals("div", tagEvaluator.tagName());
    }

    @Test
    public void testByAttribute() {
        QueryParser queryParser = new QueryParser("div[attr=value]");
        TokenQueue tq = new TokenQueue("div[attr=value]");
        queryParser.parse();

        queryParser.byAttribute();
        assertTrue(queryParser.evals.get(0) instanceof Evaluator.AttributeWithValue);
        Evaluator.AttributeWithValue attributeWithValueEvaluator = (Evaluator.AttributeWithValue) queryParser.evals.get(0);
        assertEquals("attr", attributeWithValueEvaluator.key());
        assertEquals("value", attributeWithValueEvaluator.value());
    }

    @Test
    public void testAllElements() {
        QueryParser queryParser = new QueryParser("*");
        TokenQueue tq = new TokenQueue("*");
        queryParser.parse();

        queryParser.allElements();
        assertTrue(queryParser.evals.get(0) instanceof Evaluator.AllElements);
    }

    @Test
    public void testIndexLessThan() {
        QueryParser queryParser = new QueryParser("div:lt(3)");
        TokenQueue tq = new TokenQueue("div:lt(3)");
        queryParser.parse();

        queryParser.indexLessThan();
        assertTrue(queryParser.evals.get(0) instanceof Evaluator.IndexLessThan);
        Evaluator.IndexLessThan indexLessThanEvaluator = (Evaluator.IndexLessThan) queryParser.evals.get(0);
        assertEquals(3, indexLessThanEvaluator.index());
    }

    @Test
    public void testIndexGreaterThan() {
        QueryParser queryParser = new QueryParser("div:gt(3)");
        TokenQueue tq = new TokenQueue("div:gt(3)");
        queryParser.parse();

        queryParser.indexGreaterThan();
        assertTrue(queryParser.evals.get(0) instanceof Evaluator.IndexGreaterThan);
        Evaluator.IndexGreaterThan indexGreaterThanEvaluator = (Evaluator.IndexGreaterThan) queryParser.evals.get(0);
        assertEquals(3, indexGreaterThanEvaluator.index());
    }

    @Test
    public void testIndexEquals() {
        QueryParser queryParser = new QueryParser("div:eq(3)");
        TokenQueue tq = new TokenQueue("div:eq(3)");
        queryParser.parse();

        queryParser.indexEquals();
        assertTrue(queryParser.evals.get(0) instanceof Evaluator.IndexEquals);
        Evaluator.IndexEquals indexEqualsEvaluator = (Evaluator.IndexEquals) queryParser.evals.get(0);
        assertEquals(3, indexEqualsEvaluator.index());
    }

    @Test
    public void testHas() {
        QueryParser queryParser = new QueryParser("div:has(span)");
        TokenQueue tq = new TokenQueue("div:has(span)");
        queryParser.parse();

        queryParser.has();
        assertTrue(queryParser.evals.get(0) instanceof StructuralEvaluator.Has);
        StructuralEvaluator.Has hasEvaluator = (StructuralEvaluator.Has) queryParser.evals.get(0);
        assertTrue(hasEvaluator.hasEvaluator() instanceof Evaluator.Tag);
    }

    @Test
    public void testContains() {
        QueryParser queryParser = new QueryParser("div:contains(text)");
        TokenQueue tq = new TokenQueue("div:contains(text)");
        queryParser.parse();

        queryParser.contains(false);
        assertTrue(queryParser.evals.get(0) instanceof Evaluator.ContainsText);
        Evaluator.ContainsText containsTextEvaluator = (Evaluator.ContainsText) queryParser.evals.get(0);
        assertEquals("text", containsTextEvaluator.text());
    }

    @Test
    public void testMatches() {
        QueryParser queryParser = new QueryParser("div:matches(regex)");
        TokenQueue tq = new TokenQueue("div:matches(regex)");
        queryParser.parse();

        queryParser.matches(false);
        assertTrue(queryParser.evals.get(0) instanceof Evaluator.Matches);
        Evaluator.Matches matchesEvaluator = (Evaluator.Matches) queryParser.evals.get(0);
        assertEquals("regex", matchesEvaluator.getPattern().pattern());
    }

    @Test
    public void testNot() {
        QueryParser queryParser = new QueryParser("div:not(span)");
        TokenQueue tq = new TokenQueue("div:not(span)");
        queryParser.parse();

        queryParser.not();
        assertTrue(queryParser.evals.get(0) instanceof StructuralEvaluator.Not);
        StructuralEvaluator.Not notEvaluator = (StructuralEvaluator.Not) queryParser.evals.get(0);
        assertTrue(notEvaluator.innerEvaluator() instanceof Evaluator.Tag);
    }
}