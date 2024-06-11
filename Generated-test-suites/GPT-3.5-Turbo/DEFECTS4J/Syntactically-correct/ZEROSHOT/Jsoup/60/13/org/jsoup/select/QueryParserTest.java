package org.jsoup.select;

import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "";
        Evaluator expected = null;
        Evaluator result = QueryParser.parse(query);
        assert(result.equals(expected));

        query = "div";
        expected = new Evaluator.Tag("div");
        result = QueryParser.parse(query);
        assert(result.equals(expected));

        query = "#myId";
        expected = new Evaluator.Id("myId");
        result = QueryParser.parse(query);
        assert(result.equals(expected));

        query = ".myClass";
        expected = new Evaluator.Class("myClass");
        result = QueryParser.parse(query);
        assert(result.equals(expected));

        query = "[name=value]";
        expected = new Evaluator.AttributeWithValue("name", "value");
        result = QueryParser.parse(query);
        assert(result.equals(expected));

        // Add more test cases for all methods in QueryParser

    }
}