import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse_ValidQuery_ReturnsEvaluator() {
        String query = "div#id.class[attr^=value]";
        Evaluator expected = new Evaluator.And(
            new Evaluator.Tag("div"),
            new Evaluator.Id("id"),
            new Evaluator.Class("class"),
            new Evaluator.AttributeStarting("attr", "value")
        );
        
        Evaluator result = QueryParser.parse(query);
        
        assertEquals(expected, result);
    }

    @Test(expected = SelectorParseException.class)
    public void testParse_InvalidQuery_ThrowsException() {
        String query = "div#id.[attr^=value]";
        
        QueryParser.parse(query);
    }

    @Test
    public void testParse_EmptyQuery_ReturnsAllElements() {
        String query = "";
        Evaluator expected = new Evaluator.AllElements();
        
        Evaluator result = QueryParser.parse(query);
        
        assertEquals(expected, result);
    }

    @Test
    public void testParse_QueryWithCombinators_ReturnsCombinatorEvaluator() {
        String query = "div > span + a";
        Evaluator expected = new Evaluator.And(
            new Evaluator.Tag("div"),
            new Evaluator.ImmediateParent(new Evaluator.Tag("span")),
            new Evaluator.ImmediatePreviousSibling(new Evaluator.Tag("a"))
        );
        
        Evaluator result = QueryParser.parse(query);
        
        assertEquals(expected, result);
    }

    @Test
    public void testParse_QueryWithWhitespace_ReturnsWhitespaceCombinatorEvaluator() {
        String query = "div span";
        Evaluator expected = new Evaluator.And(
            new Evaluator.Tag("div"),
            new Evaluator.Parent(new Evaluator.Tag("span"))
        );
        
        Evaluator result = QueryParser.parse(query);
        
        assertEquals(expected, result);
    }
}