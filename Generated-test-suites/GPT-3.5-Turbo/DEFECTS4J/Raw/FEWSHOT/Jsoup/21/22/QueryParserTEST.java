package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.junit.Test;

public class QueryParserTest {
    @Test
    public void testParse() {
        String query = "div.container";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) evaluator).tagName());

        query = ".popular";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals("popular", ((Evaluator.Class) evaluator).className());

        query = "[src^=image]";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueStarting);
        assertEquals("src", ((Evaluator.AttributeWithValueStarting) evaluator).key());
        assertEquals("image", ((Evaluator.AttributeWithValueStarting) evaluator).value());

        query = ":has(a)";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof StructuralEvaluator.Has);
        assertEquals("a", ((StructuralEvaluator.Has) evaluator).getBy());
        
        query = ":containsOwn(text)";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.ContainsOwnText);
        assertEquals("text", ((Evaluator.ContainsOwnText)evaluator).text());
        
        query = ":matches(regex)";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Matches);
        assertEquals("regex", ((Evaluator.Matches)evaluator).pattern().pattern());
        
        query = ":not(selector)";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof StructuralEvaluator.Not);
        Evaluator nestedEvaluator = ((StructuralEvaluator.Not)evaluator).getNestedEvaluator();
        assertNotNull(nestedEvaluator);
        assertTrue(nestedEvaluator instanceof Evaluator.Tag);
        assertEquals("selector", ((Evaluator.Tag)nestedEvaluator).tagName());
    }
}