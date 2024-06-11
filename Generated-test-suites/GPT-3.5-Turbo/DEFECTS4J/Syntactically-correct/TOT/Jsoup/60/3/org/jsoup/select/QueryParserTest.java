package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        // Test case 1
        String query1 = "div";
        Evaluator evaluator1 = QueryParser.parse(query1);
        assertNotNull(evaluator1);
        assertEquals(Evaluator.Tag.class, evaluator1.getClass());
        assertEquals("div", ((Evaluator.Tag)evaluator1).tagName());

        // Test case 2
        String query2 = "#id";
        Evaluator evaluator2 = QueryParser.parse(query2);
        assertNotNull(evaluator2);
        assertEquals(Evaluator.Id.class, evaluator2.getClass());
        assertEquals("id", ((Evaluator.Id)evaluator2).id());

        // Test case 3
        String query3 = ".class";
        Evaluator evaluator3 = QueryParser.parse(query3);
        assertNotNull(evaluator3);
        assertEquals(Evaluator.Class.class, evaluator3.getClass());
        assertEquals("class", ((Evaluator.Class)evaluator3).className());

        // Test case 4
        String query4 = "[attribute=value]";
        Evaluator evaluator4 = QueryParser.parse(query4);
        assertNotNull(evaluator4);
        assertEquals(Evaluator.AttributeWithValue.class, evaluator4.getClass());
        assertEquals("attribute", ((Evaluator.AttributeWithValue)evaluator4).key());
        assertEquals("value", ((Evaluator.AttributeWithValue)evaluator4).value());

        // Test case 5
        String query5 = "div p";
        Evaluator evaluator5 = QueryParser.parse(query5);
        assertNotNull(evaluator5);
        assertEquals(CombiningEvaluator.And.class, evaluator5.getClass());
        assertEquals(2, ((CombiningEvaluator.And)evaluator5).evaluatorList().size());
        Evaluator evaluator5_0 = ((CombiningEvaluator.And)evaluator5).evaluatorList().get(0);
        Evaluator evaluator5_1 = ((CombiningEvaluator.And)evaluator5).evaluatorList().get(1);
        assertNotNull(evaluator5_0);
        assertNotNull(evaluator5_1);
        assertEquals(Evaluator.Tag.class, evaluator5_0.getClass());
        assertEquals("div", ((Evaluator.Tag)evaluator5_0).tagName());
        assertEquals(Evaluator.Tag.class, evaluator5_1.getClass());
        assertEquals("p", ((Evaluator.Tag)evaluator5_1).tagName());
    }
}