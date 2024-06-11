import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {
    
    private QueryParser queryParser;
    
    @Before
    public void setUp() {
        queryParser = new QueryParser("");
    }
    
    @Test
    public void testParse() {
        // Test case 1: Query contains only tag
        queryParser = new QueryParser("div");
        Evaluator evaluator = queryParser.parse();
        assertTrue(evaluator instanceof Evaluator.Tag);
        
        // Test case 2: Query contains class selector
        queryParser = new QueryParser(".classname");
        evaluator = queryParser.parse();
        assertTrue(evaluator instanceof Evaluator.Class);
        
        // Test case 3: Query contains id selector
        queryParser = new QueryParser("#idname");
        evaluator = queryParser.parse();
        assertTrue(evaluator instanceof Evaluator.Id);
        
        // Test case 4: Query contains attribute selector
        queryParser = new QueryParser("[attr=value]");
        evaluator = queryParser.parse();
        assertTrue(evaluator instanceof Evaluator.Attribute);
        
        // Test case 5: Query contains attribute value selector
        queryParser = new QueryParser("[attr~=value]");
        evaluator = queryParser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueMatching);
        
        // Test case 6: Query contains multiple selectors
        queryParser = new QueryParser("div.classname[attr=value]");
        evaluator = queryParser.parse();
        assertTrue(evaluator instanceof CombiningEvaluator.And);
    }
    
    @Test
    public void testCombinator() {
        // Test case 1: Combinator >
        queryParser = new QueryParser("div > span");
        queryParser.parse();
        // Verify that the combinator is correctly added to the evaluator list
        assertEquals(2, queryParser.evals.size());
        assertTrue(queryParser.evals.get(1) instanceof StructuralEvaluator.ImmediateParent);
        
        // Test case 2: Combinator +
        queryParser = new QueryParser("div + span");
        queryParser.parse();
        // Verify that the combinator is correctly added to the evaluator list
        assertEquals(2, queryParser.evals.size());
        assertTrue(queryParser.evals.get(1) instanceof StructuralEvaluator.ImmediatePreviousSibling);
        
        // Test case 3: Combinator ~
        queryParser = new QueryParser("div ~ span");
        queryParser.parse();
        // Verify that the combinator is correctly added to the evaluator list
        assertEquals(2, queryParser.evals.size());
        assertTrue(queryParser.evals.get(1) instanceof StructuralEvaluator.PreviousSibling);
        
        // Test case 4: Combinator space
        queryParser = new QueryParser("div span");
        queryParser.parse();
        // Verify that the combinator is correctly added to the evaluator list
        assertEquals(2, queryParser.evals.size());
        assertTrue(queryParser.evals.get(1) instanceof StructuralEvaluator.Parent);
    }
    
    @Test
    public void testIndexSelectors() {
        // Test case 1: :lt(index)
        queryParser = new QueryParser(":lt(5)");
        queryParser.parse();
        assertTrue(queryParser.evals.get(0) instanceof Evaluator.IndexLessThan);
        
        // Test case 2: :gt(index)
        queryParser = new QueryParser(":gt(5)");
        queryParser.parse();
        assertTrue(queryParser.evals.get(0) instanceof Evaluator.IndexGreaterThan);
        
        // Test case 3: :eq(index)
        queryParser = new QueryParser(":eq(5)");
        queryParser.parse();
        assertTrue(queryParser.evals.get(0) instanceof Evaluator.IndexEquals);
    }
    
    @Test
    public void testPseudoSelectors() {
        // Test case 1: :has(selector)
        queryParser = new QueryParser(":has(div)");
        queryParser.parse();
        assertTrue(queryParser.evals.get(0) instanceof StructuralEvaluator.Has);
        
        // Test case 2: :contains(text)
        queryParser = new QueryParser(":contains(text)");
        queryParser.parse();
        assertTrue(queryParser.evals.get(0) instanceof Evaluator.ContainsText);
        
        // Test case 3: :containsOwn(text)
        queryParser = new QueryParser(":containsOwn(text)");
        queryParser.parse();
        assertTrue(queryParser.evals.get(0) instanceof Evaluator.ContainsOwnText);
        
        // Test case 4: :matches(regex)
        queryParser = new QueryParser(":matches(regex)");
        queryParser.parse();
        assertTrue(queryParser.evals.get(0) instanceof Evaluator.Matches);
        
        // Test case 5: :matchesOwn(regex)
        queryParser = new QueryParser(":matchesOwn(regex)");
        queryParser.parse();
        assertTrue(queryParser.evals.get(0) instanceof Evaluator.MatchesOwn);
        
        // Test case 6: :not(selector)
        queryParser = new QueryParser(":not(div)");
        queryParser.parse();
        assertTrue(queryParser.evals.get(0) instanceof StructuralEvaluator.Not);
    }
    
    @Test(expected = Selector.SelectorParseException.class)
    public void testInvalidQuery() {
        queryParser = new QueryParser(":invalid");
        queryParser.parse();
    }
}