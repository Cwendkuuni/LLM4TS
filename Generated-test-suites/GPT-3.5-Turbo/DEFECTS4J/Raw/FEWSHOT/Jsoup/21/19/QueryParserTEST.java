package org.jsoup.select;

import static org.junit.Assert.assertEquals;

import org.jsoup.helper.Validate;
import org.jsoup.parser.TokenQueue;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "body";
        Evaluator expected = new Evaluator.Tag("body");
        Evaluator result = QueryParser.parse(query);
        assertEquals(expected, result);

        query = ".class";
        expected = new Evaluator.Class("class");
        result = QueryParser.parse(query);
        assertEquals(expected, result);

        query = "#id";
        expected = new Evaluator.Id("id");
        result = QueryParser.parse(query);
        assertEquals(expected, result);

        query = ":contains(text)";
        expected = new Evaluator.ContainsText("text");
        result = QueryParser.parse(query);
        assertEquals(expected, result);
    }

    @Test
    public void testCombiningEvaluator() {
        String query = "div p";
        TokenQueue tq = new TokenQueue(query);
        tq.consumeWhitespace();
        CombiningEvaluator.And expected = new CombiningEvaluator.And(new Evaluator.Tag("div"), new Evaluator.Tag("p"));
        QueryParser qp = new QueryParser(query);
        qp.evals.add(new Evaluator.Tag("div"));
        qp.combinator(' ');
        assertEquals(expected, qp.evals.get(0));

        query = "div+p";
        tq = new TokenQueue(query);
        tq.consumeWhitespace();
        expected = new CombiningEvaluator.And(new Evaluator.Tag("div"), new StructuralEvaluator.ImmediatePreviousSibling(new Evaluator.Tag("p")));
        qp = new QueryParser(query);
        qp.evals.add(new Evaluator.Tag("div"));
        qp.combinator('+');
        assertEquals(expected, qp.evals.get(0));
    }

    @Test
    public void testConsumeSubQuery() {
        String query = "h1 span";
        QueryParser qp = new QueryParser(query);
        String expected = "h1";
        String result = qp.consumeSubQuery();
        assertEquals(expected, result);

        query = "div+p";
        qp = new QueryParser(query);
        expected = "div";
        result = qp.consumeSubQuery();
        assertEquals(expected, result);
    }

    @Test
    public void testFindElements() {
        String query = "div";
        QueryParser qp = new QueryParser(query);
        qp.findElements();
        assertEquals(1, qp.evals.size());
        assertEquals(new Evaluator.Tag("div"), qp.evals.get(0));

        query = "#id";
        qp = new QueryParser(query);
        qp.findElements();
        assertEquals(1, qp.evals.size());
        assertEquals(new Evaluator.Id("id"), qp.evals.get(0));
        
        query = ".class";
        qp = new QueryParser(query);
        qp.findElements();
        assertEquals(1, qp.evals.size());
        assertEquals(new Evaluator.Class("class"), qp.evals.get(0));

        query = "[name=value]";
        qp = new QueryParser(query);
        qp.findElements();
        assertEquals(1, qp.evals.size());
        assertEquals(new Evaluator.AttributeWithValue("name", "value"), qp.evals.get(0));

        query = "*";
        qp = new QueryParser(query);
        qp.findElements();
        assertEquals(1, qp.evals.size());
        assertEquals(new Evaluator.AllElements(), qp.evals.get(0));

        query = ":lt(5)";
        qp = new QueryParser(query);
        qp.findElements();
        assertEquals(1, qp.evals.size());
        assertEquals(new Evaluator.IndexLessThan(5), qp.evals.get(0));

        query = ":gt(5)";
        qp = new QueryParser(query);
        qp.findElements();
        assertEquals(1, qp.evals.size());
        assertEquals(new Evaluator.IndexGreaterThan(5), qp.evals.get(0));

        query = ":eq(5)";
        qp = new QueryParser(query);
        qp.findElements();
        assertEquals(1, qp.evals.size());
        assertEquals(new Evaluator.IndexEquals(5), qp.evals.get(0));

        query = ":has(div)";
        qp = new QueryParser(query);
        qp.findElements();
        assertEquals(1, qp.evals.size());
        assertEquals(new StructuralEvaluator.Has(new Evaluator.Tag("div")), qp.evals.get(0));

        query = ":contains(text)";
        qp = new QueryParser(query);
        qp.findElements();
        assertEquals(1, qp.evals.size());
        assertEquals(new Evaluator.ContainsText("text"), qp.evals.get(0));

        query = ":containsOwn(text)";
        qp = new QueryParser(query);
        qp.findElements();
        assertEquals(1, qp.evals.size());
        assertEquals(new Evaluator.ContainsOwnText("text"), qp.evals.get(0));

        query = ":matches(regex)";
        qp = new QueryParser(query);
        qp.findElements();
        assertEquals(1, qp.evals.size());
        assertEquals(new Evaluator.Matches(TokenQueue.unescape("regex")), qp.evals.get(0));

        query = ":matchesOwn(regex)";
        qp = new QueryParser(query);
        qp.findElements();
        assertEquals(1, qp.evals.size());
        assertEquals(new Evaluator.MatchesOwn(TokenQueue.unescape("regex")), qp.evals.get(0));

        query = ":not(div)";
        qp = new QueryParser(query);
        qp.findElements();
        assertEquals(1, qp.evals.size());
        assertEquals(new StructuralEvaluator.Not(new Evaluator.Tag("div")), qp.evals.get(0));

        query = ":not(#id)";
        qp = new QueryParser(query);
        qp.findElements();
        assertEquals(1, qp.evals.size());
        assertEquals(new StructuralEvaluator.Not(new Evaluator.Id("id")), qp.evals.get(0));
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testFindElementsWithInvalidQuery() {
        String query = ":not()";
        QueryParser qp = new QueryParser(query);
        qp.findElements();
    }
}