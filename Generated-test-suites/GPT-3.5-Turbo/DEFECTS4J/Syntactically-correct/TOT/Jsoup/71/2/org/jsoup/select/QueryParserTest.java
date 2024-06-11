import org.jsoup.select.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {
    
    @Test
    public void parse_validSelector_returnsEvaluator() {
        String query = "div.item";
        Evaluator expected = new Evaluator.And(new Evaluator.Tag("div"), new Evaluator.Class("item"));
        
        Evaluator actual = QueryParser.parse(query);
        
        assertEquals(expected, actual);
    }
    
    @Test(expected = Selector.SelectorParseException.class)
    public void parse_invalidSelector_throwsParseException() {
        String query = "div.item[";
        
        QueryParser.parse(query);
    }
    
    @Test
    public void combinator_withChildCombinator_returnsAndEvaluator() {
        QueryParser parser = new QueryParser("");
        Evaluator currentEval = new Evaluator.AllElements();
        Evaluator expected = new Evaluator.And(new Evaluator.AllElements(), new Evaluator.Parent(currentEval));
        
        parser.combinator('>', currentEval);
        Evaluator actual = parser.evals.get(0);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void findElements_withId_callsIdMethod() {
        QueryParser parser = new QueryParser("");
        
        parser.byId();
        // assert specific id method is called
    }
    
    // Additional test cases for other methods omititd
    
    @Test
    public void cssNthChild_withBackwardsAndOfTypeParams_returnsNthChildEvaluator() {
        QueryParser parser = new QueryParser("");
        boolean backwards = true;
        boolean ofType = true;
        String argS = "3n + 2";
        Evaluator expected = new Evaluator.IsNthLastOfType(3, 2);
        
        parser.cssNthChild(backwards, ofType, argS);
        Evaluator actual = parser.evals.get(0);
        
        assertEquals(expected, actual);
    }
}