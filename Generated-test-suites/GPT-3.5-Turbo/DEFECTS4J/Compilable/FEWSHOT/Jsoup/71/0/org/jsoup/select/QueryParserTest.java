package org.jsoup.select;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.junit.Test;

public class QueryParserTest {
    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse("div");
        assertEquals("org.jsoup.select.Evaluator$Tag", evaluator.getClass().getName());
        
        evaluator = QueryParser.parse("#myId");
        assertEquals("org.jsoup.select.Evaluator$Id", evaluator.getClass().getName());
        
        evaluator = QueryParser.parse(".myClass");
        assertEquals("org.jsoup.select.Evaluator$Class", evaluator.getClass().getName());
        
        evaluator = QueryParser.parse("[attribute]");
        assertEquals("org.jsoup.select.Evaluator$Attribute", evaluator.getClass().getName());
        
        evaluator = QueryParser.parse("div.class[attr=value]");
        assertEquals("org.jsoup.select.CombiningEvaluator$And", evaluator.getClass().getName());
        
        evaluator = QueryParser.parse("div > .class + [attr!=value]");
        assertEquals("org.jsoup.select.CombiningEvaluator$And", evaluator.getClass().getName());
        
    }

    @Test
    public void testParseInvalid() {
        try {
            QueryParser.parse("div[attr~=value]");
        } catch (Selector.SelectorParseException e) {
            assertEquals("org.jsoup.select.Selector$SelectorParseException", e.getClass().getName());
        }
    }

    @Test
    public void testParseEmpty() {
        Evaluator evaluator = QueryParser.parse("");
        assertEquals("org.jsoup.select.Evaluator$AllElements", evaluator.getClass().getName());
    }
}