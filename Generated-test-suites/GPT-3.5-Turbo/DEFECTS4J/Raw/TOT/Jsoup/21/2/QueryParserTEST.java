import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {

    @Before
    public void setup() {
        // Initialize any necessary variables or objects for testing
    }

    @Test
    public void testParse_CombinatorStartsWithRoot() {
        String query = "div > p";
        Evaluator expected = new CombiningEvaluator.And(
            new Evaluator.Tag("div"),
            new StructuralEvaluator.ImmediateParent(new Evaluator.Tag("p"))
        );
        Evaluator actual = QueryParser.parse(query);
        assertEquals(expected, actual);
    }

    @Test
    public void testParse_CombinatorCommaSeparated() {
        String query = "div, p";
        Evaluator expected = new CombiningEvaluator.Or(
            new Evaluator.Tag("div"),
            new Evaluator.Tag("p")
        );
        Evaluator actual = QueryParser.parse(query);
        assertEquals(expected, actual);
    }

    @Test
    public void testParse_CombinatorCombination() {
        String query = "div + p ~ span";
        Evaluator expected = new CombiningEvaluator.And(
            new Evaluator.Tag("div"),
            new StructuralEvaluator.ImmediatePreviousSibling(new Evaluator.Tag("p")),
            new StructuralEvaluator.PreviousSibling(new Evaluator.Tag("span"))
        );
        Evaluator actual = QueryParser.parse(query);
        assertEquals(expected, actual);
    }

    @Test
    public void testParse_SingleIdSelector() {
        String query = "#myId";
        Evaluator expected = new Evaluator.Id("myId");
        Evaluator actual = QueryParser.parse(query);
        assertEquals(expected, actual);
    }

    @Test
    public void testParse_MultipleIdSelectors() {
        String query = "#id1, #id2";
        Evaluator expected = new CombiningEvaluator.Or(
            new Evaluator.Id("id1"),
            new Evaluator.Id("id2")
        );
        Evaluator actual = QueryParser.parse(query);
        assertEquals(expected, actual);
    }

    @Test
    public void testParse_SingleClassSelector() {
        String query = ".myClass";
        Evaluator expected = new Evaluator.Class("myClass");
        Evaluator actual = QueryParser.parse(query);
        assertEquals(expected, actual);
    }

    @Test
    public void testParse_MultipleClassSelectors() {
        String query = ".class1, .class2";
        Evaluator expected = new CombiningEvaluator.Or(
            new Evaluator.Class("class1"),
            new Evaluator.Class("class2")
        );
        Evaluator actual = QueryParser.parse(query);
        assertEquals(expected, actual);
    }

    @Test
    public void testParse_TagSelector() {
        String query = "div";
        Evaluator expected = new Evaluator.Tag("div");
        Evaluator actual = QueryParser.parse(query);
        assertEquals(expected, actual);
    }

    @Test
    public void testParse_AttributeSelector() {
        String query = "[name=value]";
        Evaluator expected = new Evaluator.AttributeWithValue("name", "value");
        Evaluator actual = QueryParser.parse(query);
        assertEquals(expected, actual);
    }

    @Test
    public void testParse_HasSelector() {
        String query = ":has(div)";
        Evaluator expected = new StructuralEvaluator.Has(new Evaluator.Tag("div"));
        Evaluator actual = QueryParser.parse(query);
        assertEquals(expected, actual);
    }

    @Test
    public void testParse_ContainsSelector() {
        String query = ":contains(text)";
        Evaluator expected = new Evaluator.ContainsText("text");
        Evaluator actual = QueryParser.parse(query);
        assertEquals(expected, actual);
    }

    @Test
    public void testParse_NotSelector() {
        String query = ":not(div)";
        Evaluator expected = new StructuralEvaluator.Not(new Evaluator.Tag("div"));
        Evaluator actual = QueryParser.parse(query);
        assertEquals(expected, actual);
    }

    @Test
    public void testParse_InvalidQuery() {
        String query = "[name=]";
        try {
            QueryParser.parse(query);
            fail("Expected SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            // Test passed
        }
    }

}