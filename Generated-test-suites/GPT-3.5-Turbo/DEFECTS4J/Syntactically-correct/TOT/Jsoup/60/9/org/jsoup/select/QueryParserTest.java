import org.junit.Test;
import org.junit.Before;
import org.jsoup.select.QueryParser;

import static org.junit.Assert.*;

public class QueryParserTest {

    private QueryParser parser;

    @Before
    public void setUp() {
        parser = new QueryParser("");
    }

    @Test
    public void testParse_emptyQuery_returnsAllElementsEvaluator() {
        String query = "";
        Evaluator expected = new Evaluator.AllElements();
        
        Evaluator result = QueryParser.parse(query);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testParse_singleElementQuery_returnsTagEvaluator() {
        String query = "div";
        Evaluator expected = new Evaluator.Tag(query);
        
        Evaluator result = QueryParser.parse(query);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testParse_multipleElementQuery_returnsCombiningEvaluator() {
        String query = "div > span";
        Evaluator expected = new CombiningEvaluator.And(new Evaluator.Tag("div"), new StructuralEvaluator.ImmediateParent(new Evaluator.Tag("span")));
        
        Evaluator result = QueryParser.parse(query);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testParse_complexQuery_returnsCombiningEvaluator() {
        String query = "div + span[class=\"test\"]:containsOwn(text)";
        Evaluator expected = new CombiningEvaluator.And(
            new Evaluator.Tag("div"), 
            new StructuralEvaluator.ImmediatePreviousSibling(
                new CombiningEvaluator.And(
                    new Evaluator.Tag("span"), 
                    new Evaluator.AttributeWithValue("class", "test"),
                    new Evaluator.ContainsOwnText("text")
                )
            )
        );
        
        Evaluator result = QueryParser.parse(query);
        
        assertEquals(expected, result);
    }
    
    // Add more test cases to cover all methods in QueryParser class
    
}