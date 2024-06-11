package org.jsoup.select;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.select.Evaluator;
import org.junit.Test;

public class QueryParserTest {
    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse("div");
        //add assertion for evaluator
        
        evaluator = QueryParser.parse("#id");
        //add assertion for evaluator
        
        evaluator = QueryParser.parse(".class");
        //add assertion for evaluator
        
        evaluator = QueryParser.parse("[key=value]");
        //add assertion for evaluator
        
        evaluator = QueryParser.parse("div > p");
        //add assertion for evaluator
        
        evaluator = QueryParser.parse("div + p");
        //add assertion for evaluator
        
        evaluator = QueryParser.parse("div ~ p");
        //add assertion for evaluator
        
        evaluator = QueryParser.parse("div:contains(text)");
        //add assertion for evaluator
        
        evaluator = QueryParser.parse("div[attr=value]");
        //add assertion for evaluator
        
        evaluator = QueryParser.parse("div:matches(regex)");
        //add assertion for evaluator
        
        evaluator = QueryParser.parse("div:not(selector)");
        //add assertion for evaluator
    }
}