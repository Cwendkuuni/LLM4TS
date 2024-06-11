package org.jsoup.select;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class QueryParserTest {
    @Test
    public void testParse() {
        // Test case 1
        String query1 = "div";
        Evaluator eval1 = QueryParser.parse(query1);
        assertEquals(Evaluator.Tag.class, eval1.getClass());
        assertEquals("div", ((Evaluator.Tag) eval1).tagName());

        // Test case 2
        String query2 = "#id";
        Evaluator eval2 = QueryParser.parse(query2);
        assertEquals(Evaluator.Id.class, eval2.getClass());
        assertEquals("id", ((Evaluator.Id) eval2).id());

        // Test case 3
        String query3 = ".class";
        Evaluator eval3 = QueryParser.parse(query3);
        assertEquals(Evaluator.Class.class, eval3.getClass());
        assertEquals("class", ((Evaluator.Class) eval3).className());

        // Test case 4
        String query4 = "[attr]";
        Evaluator eval4 = QueryParser.parse(query4);
        assertEquals(Evaluator.Attribute.class, eval4.getClass());
        assertEquals("attr", ((Evaluator.Attribute) eval4).key());

        // Test case 5
        String query5 = "[attr=value]";
        Evaluator eval5 = QueryParser.parse(query5);
        assertEquals(Evaluator.AttributeWithValue.class, eval5.getClass());
        assertEquals("attr", ((Evaluator.AttributeWithValue) eval5).key());
        assertEquals("value", ((Evaluator.AttributeWithValue) eval5).value());

        // Test case 6
        String query6 = ":lt(5)";
        Evaluator eval6 = QueryParser.parse(query6);
        assertEquals(Evaluator.IndexLessThan.class, eval6.getClass());
        assertEquals(5, ((Evaluator.IndexLessThan) eval6).index());

        // Test case 7
        String query7 = ":gt(5)";
        Evaluator eval7 = QueryParser.parse(query7);
        assertEquals(Evaluator.IndexGreaterThan.class, eval7.getClass());
        assertEquals(5, ((Evaluator.IndexGreaterThan) eval7).index());

        // Test case 8
        String query8 = ":eq(5)";
        Evaluator eval8 = QueryParser.parse(query8);
        assertEquals(Evaluator.IndexEquals.class, eval8.getClass());
        assertEquals(5, ((Evaluator.IndexEquals) eval8).index());

        // Test case 9
        String query9 = ":has(p)";
        Evaluator eval9 = QueryParser.parse(query9);
        assertEquals(StructuralEvaluator.Has.class, eval9.getClass());
        Evaluator subEval9 = ((StructuralEvaluator.Has) eval9).hasEvaluator();
        assertEquals(Evaluator.Tag.class, subEval9.getClass());
        assertEquals("p", ((Evaluator.Tag) subEval9).tagName());

        // Test case 10
        String query10 = ":contains(text)";
        Evaluator eval10 = QueryParser.parse(query10);
        assertEquals(Evaluator.ContainsText.class, eval10.getClass());
        assertEquals("text", ((Evaluator.ContainsText) eval10).searchText());

        // Test case 11
        String query11 = ":not(div)";
        Evaluator eval11 = QueryParser.parse(query11);
        assertEquals(StructuralEvaluator.Not.class, eval11.getClass());
        Evaluator subEval11 = ((StructuralEvaluator.Not) eval11).hasEvaluator();
        assertEquals(Evaluator.Tag.class, subEval11.getClass());
        assertEquals("div", ((Evaluator.Tag) subEval11).tagName());
    }
}