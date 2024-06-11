
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {
    
    @Test
    public void testParse() {
        // Test case for parse method
        
        // Test an empty query
        Evaluator result1 = QueryParser.parse("");
        assertTrue(result1 instanceof Evaluator.AllElements);
        
        // Test a query with only a tag name
        Evaluator result2 = QueryParser.parse("div");
        assertTrue(result2 instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) result2).tagName());
        
        // Test a query with a class name
        Evaluator result3 = QueryParser.parse(".className");
        assertTrue(result3 instanceof Evaluator.Class);
        assertEquals("className", ((Evaluator.Class) result3).className());
        
        // Test a query with an ID
        Evaluator result4 = QueryParser.parse("#id");
        assertTrue(result4 instanceof Evaluator.Id);
        assertEquals("id", ((Evaluator.Id) result4).id());
        
        // Test a query with an attribute
        Evaluator result5 = QueryParser.parse("[attr=value]");
        assertTrue(result5 instanceof Evaluator.AttributeWithValue);
        assertEquals("attr", ((Evaluator.AttributeWithValue) result5).key());
        assertEquals("value", ((Evaluator.AttributeWithValue) result5).value());
        
        // Test a query with combined selectors
        Evaluator result6 = QueryParser.parse("div.className[attr=value]");
        assertTrue(result6 instanceof CombiningEvaluator.And);
        
        Evaluator evaluator1 = ((CombiningEvaluator.And) result6).first();
        assertTrue(evaluator1 instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) evaluator1).tagName());
        
        Evaluator evaluator2 = ((CombiningEvaluator.And) result6).second();
        assertTrue(evaluator2 instanceof CombiningEvaluator.And);
        
        Evaluator evaluator3 = ((CombiningEvaluator.And) evaluator2).first();
        assertTrue(evaluator3 instanceof Evaluator.Class);
        assertEquals("className", ((Evaluator.Class) evaluator3).className());
        
        Evaluator evaluator4 = ((CombiningEvaluator.And) evaluator2).second();
        assertTrue(evaluator4 instanceof Evaluator.AttributeWithValue);
        assertEquals("attr", ((Evaluator.AttributeWithValue) evaluator4).key());
        assertEquals("value", ((Evaluator.AttributeWithValue) evaluator4).value());
        
        // Test a query with a combination of selectors and combinators
        Evaluator result7 = QueryParser.parse("div > .className + #id");
        assertTrue(result7 instanceof CombiningEvaluator.And);
        
        Evaluator evaluator5 = ((CombiningEvaluator.And) result7).first();
        assertTrue(evaluator5 instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) evaluator5).tagName());
        
        Evaluator evaluator6 = ((CombiningEvaluator.And) result7).second();
        assertTrue(evaluator6 instanceof CombiningEvaluator.And);
        
        Evaluator evaluator7 = ((CombiningEvaluator.And) evaluator6).first();
        assertTrue(evaluator7 instanceof CombiningEvaluator.And);
        
        Evaluator evaluator8 = ((CombiningEvaluator.And) evaluator6).second();
        assertTrue(evaluator8 instanceof Evaluator.Id);
        assertEquals("id", ((Evaluator.Id) evaluator8).id());
    }
    
    @Test
    public void testParse_exceptionHandling() {
        // Test cases for exception handling in parse method
        
        // Test a query with an unknown combinator
        try {
            Evaluator result1 = QueryParser.parse("div@className");
            fail("Selector.ParseException expected");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Unknown combinator: @", e.getMessage());
        }
        
        // Test a query with an unexpected token
        try {
            Evaluator result2 = QueryParser.parse("div!");
            fail("Selector.ParseException expected");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Could not parse query 'div!': unexpected token at '!'", e.getMessage());
        }
        
        // Test a query with an empty :has() subselect
        try {
            Evaluator result3 = QueryParser.parse("div:has()");
            fail("Selector.ParseException expected");
        } catch (Selector.SelectorParseException e) {
            assertEquals(":has(el) subselect must not be empty", e.getMessage());
        }
        
        // Test a query with an empty :contains() subquery
        try {
            Evaluator result4 = QueryParser.parse("div:contains()");
            fail("Selector.ParseException expected");
        } catch (Selector.SelectorParseException e) {
            assertEquals(":contains(text) query must not be empty", e.getMessage());
        }
        
        // Test a query with an empty :containsOwn() subquery
        try {
            Evaluator result5 = QueryParser.parse("div:containsOwn()");
            fail("Selector.ParseException expected");
        } catch (Selector.SelectorParseException e) {
            assertEquals(":containsOwn(text) query must not be empty", e.getMessage());
        }
        
        // Test a query with an empty :matches() regex
        try {
            Evaluator result6 = QueryParser.parse("div:matches()");
            fail("Selector.ParseException expected");
        } catch (Selector.SelectorParseException e) {
            assertEquals(":matches(regex) query must not be empty", e.getMessage());
        }
        
        // Test a query with an empty :matchesOwn() regex
        try {
            Evaluator result7 = QueryParser.parse("div:matchesOwn()");
            fail("Selector.ParseException expected");
        } catch (Selector.SelectorParseException e) {
            assertEquals(":matchesOwn(regex) query must not be empty", e.getMessage());
        }
        
        // Test a query with an empty :not() subselect
        try {
            Evaluator result8 = QueryParser.parse("div:not()");
            fail("Selector.ParseException expected");
        } catch (Selector.SelectorParseException e) {
            assertEquals(":not(selector) subselect must not be empty", e.getMessage());
        }
    }
}
