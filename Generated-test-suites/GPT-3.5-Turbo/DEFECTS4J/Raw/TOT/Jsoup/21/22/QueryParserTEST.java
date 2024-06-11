import org.jsoup.select.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = ".classname";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue("Test Failed - ClassSelector not parsed correctly", evaluator instanceof Evaluator.Class);
        
        query = "#idname";
        evaluator = QueryParser.parse(query);
        assertTrue("Test Failed - IdSelector not parsed correctly", evaluator instanceof Evaluator.Id);
        
        query = "tagname";
        evaluator = QueryParser.parse(query);
        assertTrue("Test Failed - TagSelector not parsed correctly", evaluator instanceof Evaluator.Tag);
        
        query = "[attribute]";
        evaluator = QueryParser.parse(query);
        assertTrue("Test Failed - AttributeSelector not parsed correctly", evaluator instanceof Evaluator.Attribute);
        
        query = "[attribute=value]";
        evaluator = QueryParser.parse(query);
        assertTrue("Test Failed - AttributeWithValueSelector not parsed correctly", evaluator instanceof Evaluator.AttributeWithValue);

        query = "[attribute!=value]";
        evaluator = QueryParser.parse(query);
        assertTrue("Test Failed - AttributeWithValueNotSelector not parsed correctly", evaluator instanceof Evaluator.AttributeWithValueNot);
        
        query = "[attribute^=value]";
        evaluator = QueryParser.parse(query);
        assertTrue("Test Failed - AttributeWithValueStartingSelector not parsed correctly", evaluator instanceof Evaluator.AttributeWithValueStarting);
        
        query = "[attribute$=value]";
        evaluator = QueryParser.parse(query);
        assertTrue("Test Failed - AttributeWithValueEndingSelector not parsed correctly", evaluator instanceof Evaluator.AttributeWithValueEnding);
        
        query = "[attribute*=value]";
        evaluator = QueryParser.parse(query);
        assertTrue("Test Failed - AttributeWithValueContainingSelector not parsed correctly", evaluator instanceof Evaluator.AttributeWithValueContaining);
        
        query = "[attribute~=value]";
        evaluator = QueryParser.parse(query);
        assertTrue("Test Failed - AttributeWithValueMatchingSelector not parsed correctly", evaluator instanceof Evaluator.AttributeWithValueMatching);
        
        query = "*";
        evaluator = QueryParser.parse(query);
        assertTrue("Test Failed - AllElementsSelector not parsed correctly", evaluator instanceof Evaluator.AllElements);
        
        query = ":lt(5)";
        evaluator = QueryParser.parse(query);
        assertTrue("Test Failed - IndexLessThanSelector not parsed correctly", evaluator instanceof Evaluator.IndexLessThan);
        
        query = ":gt(5)";
        evaluator = QueryParser.parse(query);
        assertTrue("Test Failed - IndexGreaterThanSelector not parsed correctly", evaluator instanceof Evaluator.IndexGreaterThan);
        
        query = ":eq(5)";
        evaluator = QueryParser.parse(query);
        assertTrue("Test Failed - IndexEqualsSelector not parsed correctly", evaluator instanceof Evaluator.IndexEquals);
        
        query = ":has(tagname)";
        evaluator = QueryParser.parse(query);
        assertTrue("Test Failed - HasSelector not parsed correctly", evaluator instanceof StructuralEvaluator.Has);
        
        query = ":contains(text)";
        evaluator = QueryParser.parse(query);
        assertTrue("Test Failed - ContainsSelector not parsed correctly", evaluator instanceof Evaluator.ContainsText);
        
        query = ":containsOwn(text)";
        evaluator = QueryParser.parse(query);
        assertTrue("Test Failed - ContainsOwnSelector not parsed correctly", evaluator instanceof Evaluator.ContainsOwnText);
        
        query = ":matches(regex)";
        evaluator = QueryParser.parse(query);
        assertTrue("Test Failed - MatchesSelector not parsed correctly", evaluator instanceof Evaluator.Matches);
        
        query = ":matchesOwn(regex)";
        evaluator = QueryParser.parse(query);
        assertTrue("Test Failed - MatchesOwnSelector not parsed correctly", evaluator instanceof Evaluator.MatchesOwn);
        
        query = ":not(tagname)";
        evaluator = QueryParser.parse(query);
        assertTrue("Test Failed - NotSelector not parsed correctly", evaluator instanceof StructuralEvaluator.Not);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseException() {
        String query = ":unknown";
        QueryParser.parse(query);
        fail("Test Failed - ParseException not thrown for unknown selector");
    }
}