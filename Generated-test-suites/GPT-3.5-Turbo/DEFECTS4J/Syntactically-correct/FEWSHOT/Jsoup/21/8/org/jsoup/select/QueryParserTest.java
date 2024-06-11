package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.parser.TokenQueue;
import org.junit.Test;

public class QueryParserTest {
    
    @Test
    public void testParse() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testCombinator() {
        QueryParser parser = new QueryParser("");
        List<Evaluator> evals = new ArrayList<Evaluator>();
        evals.add(new Evaluator.Tag("div"));
        parser.setEvals(evals);
        
        parser.combinator('>');
        assertEquals(1, parser.getEvals().size());
        
        parser.combinator(' ');
        assertEquals(1, parser.getEvals().size());
        
        parser.combinator('+');
        assertEquals(1, parser.getEvals().size());
        
        parser.combinator('~');
        assertEquals(1, parser.getEvals().size());
        
        parser.combinator('?');
        assertEquals(1, parser.getEvals().size());
    }
    
    @Test
    public void testConsumeSubQuery() {
        QueryParser parser = new QueryParser("[attr=value]");
        String subQuery = parser.consumeSubQuery();
        assertEquals("[attr=value]", subQuery);
    }
    
    @Test
    public void testFindElements() {
        QueryParser parser = new QueryParser("#id");
        
        parser.findElements();
        assertEquals(1, parser.getEvals().size());
        
        parser = new QueryParser(".class");
        parser.findElements();
        assertEquals(1, parser.getEvals().size());
        
        parser = new QueryParser("div");
        parser.findElements();
        assertEquals(1, parser.getEvals().size());
        
        parser = new QueryParser("[attr=value]");
        parser.findElements();
        assertEquals(1, parser.getEvals().size());
        
        parser = new QueryParser("*");
        parser.findElements();
        assertEquals(1, parser.getEvals().size());
        
        parser = new QueryParser(":lt(2)");
        parser.findElements();
        assertEquals(1, parser.getEvals().size());
        
        parser = new QueryParser(":gt(2)");
        parser.findElements();
        assertEquals(1, parser.getEvals().size());
        
        parser = new QueryParser(":eq(2)");
        parser.findElements();
        assertEquals(1, parser.getEvals().size());
        
        parser = new QueryParser(":has(a)");
        parser.findElements();
        assertEquals(1, parser.getEvals().size());
        
        parser = new QueryParser(":contains(text)");
        parser.findElements();
        assertEquals(1, parser.getEvals().size());
        
        parser = new QueryParser(":matches(regex)");
        parser.findElements();
        assertEquals(1, parser.getEvals().size());
        
        parser = new QueryParser(":not(a)");
        parser.findElements();
        assertEquals(1, parser.getEvals().size());
        
        parser = new QueryParser(":unhandled");
        try {
            parser.findElements();
        } catch (Exception e) {
            assertEquals(Selector.SelectorParseException.class, e.getClass());
        }
    }
    
    @Test
    public void testById() {
        QueryParser parser = new QueryParser("#id");
        parser.byId();
        assertEquals(1, parser.getEvals().size());
    }
    
    @Test
    public void testByClass() {
        QueryParser parser = new QueryParser(".class");
        parser.byClass();
        assertEquals(1, parser.getEvals().size());
    }
    
    @Test
    public void testByTag() {
        QueryParser parser = new QueryParser("div");
        parser.byTag();
        assertEquals(1, parser.getEvals().size());
        
        parser = new QueryParser("abc:def");
        parser.byTag();
        assertEquals(1, parser.getEvals().size());
    }
    
    @Test
    public void testByAttribute() {
        QueryParser parser = new QueryParser("[attr=value]");
        parser.byAttribute();
        assertEquals(1, parser.getEvals().size());
        
        parser = new QueryParser("[^attr=value]");
        parser.byAttribute();
        assertEquals(1, parser.getEvals().size());
        
        parser = new QueryParser("[attr]");
        parser.byAttribute();
        assertEquals(1, parser.getEvals().size());
        
        parser = new QueryParser("[attr=value]");
        parser.byAttribute();
        assertEquals(1, parser.getEvals().size());
        
        parser = new QueryParser("[attr=value]");
        parser.byAttribute();
        assertEquals(1, parser.getEvals().size());
        
        parser = new QueryParser("[attr~=value]");
        parser.byAttribute();
        assertEquals(1, parser.getEvals().size());
        
        parser = new QueryParser("[attr!=value]");
        parser.byAttribute();
        assertEquals(1, parser.getEvals().size());
        
        parser = new QueryParser("[attr$=value]");
        parser.byAttribute();
        assertEquals(1, parser.getEvals().size());
        
        parser = new QueryParser("[attr*=value]");
        parser.byAttribute();
        assertEquals(1, parser.getEvals().size());
        
        parser = new QueryParser("[attr^=value]");
        parser.byAttribute();
        assertEquals(1, parser.getEvals().size());
    }
    
    @Test
    public void testAllElements() {
        QueryParser parser = new QueryParser("*");
        parser.allElements();
        assertEquals(1, parser.getEvals().size());
    }
    
    @Test
    public void testIndexLessThan() {
        QueryParser parser = new QueryParser(":lt(2)");
        parser.indexLessThan();
        assertEquals(1, parser.getEvals().size());
    }
    
    @Test
    public void testIndexGreaterThan() {
        QueryParser parser = new QueryParser(":gt(2)");
        parser.indexGreaterThan();
        assertEquals(1, parser.getEvals().size());
    }
    
    @Test
    public void testIndexEquals() {
        QueryParser parser = new QueryParser(":eq(2)");
        parser.indexEquals();
        assertEquals(1, parser.getEvals().size());
    }
    
    @Test
    public void testHas() {
        QueryParser parser = new QueryParser(":has(a)");
        parser.has();
        assertEquals(1, parser.getEvals().size());
    }
    
    @Test
    public void testContains() {
        QueryParser parser = new QueryParser(":contains(text)");
        parser.contains(false);
        assertEquals(1, parser.getEvals().size());
        
        parser = new QueryParser(":containsOwn(text)");
        parser.contains(true);
        assertEquals(1, parser.getEvals().size());
    }
    
    @Test
    public void testMatches() {
        QueryParser parser = new QueryParser(":matches(regex)");
        parser.matches(false);
        assertEquals(1, parser.getEvals().size());
        
        parser = new QueryParser(":matchesOwn(regex)");
        parser.matches(true);
        assertEquals(1, parser.getEvals().size());
    }
    
    @Test
    public void testNot() {
        QueryParser parser = new QueryParser(":not(a)");
        parser.not();
        assertEquals(1, parser.getEvals().size());
    }
}