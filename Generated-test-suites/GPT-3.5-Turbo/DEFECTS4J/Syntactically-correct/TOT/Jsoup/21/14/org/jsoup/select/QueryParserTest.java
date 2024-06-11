import static org.junit.Assert.*;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse_CombinatorComma() {
        String query = "div, span";
        Evaluator expected = new CombiningEvaluator.Or(new Evaluator.Tag("div"), new Evaluator.Tag("span"));
        assertEquals(expected, QueryParser.parse(query));
    }

    @Test
    public void testParse_CombinatorGreaterThan() {
        String query = "div > span";
        Evaluator expected = new CombiningEvaluator.And(new Evaluator.Tag("div"), new StructuralEvaluator.ImmediateParent(new Evaluator.Tag("span")));
        assertEquals(expected, QueryParser.parse(query));
    }

    @Test
    public void testParse_CombinatorSpace() {
        String query = "div span";
        Evaluator expected = new CombiningEvaluator.And(new Evaluator.Tag("div"), new StructuralEvaluator.Parent(new Evaluator.Tag("span")));
        assertEquals(expected, QueryParser.parse(query));
    }

    @Test
    public void testParse_CombinatorPlus() {
        String query = "div + span";
        Evaluator expected = new CombiningEvaluator.And(new Evaluator.Tag("div"), new StructuralEvaluator.ImmediatePreviousSibling(new Evaluator.Tag("span")));
        assertEquals(expected, QueryParser.parse(query));
    }

    @Test
    public void testParse_CombinatorTilde() {
        String query = "div ~ span";
        Evaluator expected = new CombiningEvaluator.And(new Evaluator.Tag("div"), new StructuralEvaluator.PreviousSibling(new Evaluator.Tag("span")));
        assertEquals(expected, QueryParser.parse(query));
    }

    @Test
    public void testParse_MultipleCombinators() {
        String query = "div > span + p ~ a";
        Evaluator expected = new CombiningEvaluator.And(
            new Evaluator.Tag("div"),
            new StructuralEvaluator.ImmediateParent(
                new CombiningEvaluator.And(
                    new Evaluator.Tag("span"),
                    new StructuralEvaluator.ImmediatePreviousSibling(
                        new CombiningEvaluator.And(
                            new Evaluator.Tag("p"),
                            new StructuralEvaluator.PreviousSibling(
                                new Evaluator.Tag("a"))
                            )
                        )
                    )
                )
            );
        assertEquals(expected, QueryParser.parse(query));
    }

    @Test
    public void testParse_OrCombinator() {
        String query = "div, span, p";
        Evaluator expected = new CombiningEvaluator.Or(new Evaluator.Tag("div"), new Evaluator.Tag("span"), new Evaluator.Tag("p"));
        assertEquals(expected, QueryParser.parse(query));
    }

    @Test
    public void testParse_HierarchyAndExtras() {
        String query = "div ~ span + p[class=\"test\"], .example";
        Evaluator expected = new CombiningEvaluator.And(
            new Evaluator.Tag("div"),
            new StructuralEvaluator.PreviousSibling(
                new Evaluator.Tag("span")
            ),
            new StructuralEvaluator.ImmediatePreviousSibling(
                new Evaluator.Tag("p").setAttribute("class", "\"test\"")
            ),
            new Evaluator.Class("example")
        );
        assertEquals(expected, QueryParser.parse(query));
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_UnknownCombinator() {
        String query = "div $ span"; // Unknown combinator $
        QueryParser.parse(query);
    }

    @Test
    public void testParse_NoCombination() {
        String query = "div.class[attr=\"value\"]";
        Evaluator expected = new CombiningEvaluator.And(
            new Evaluator.Tag("div"),
            new Evaluator.Class("class"),
            new Evaluator.AttributeWithValue("attr", "\"value\"")
        );
        assertEquals(expected, QueryParser.parse(query));
    }

    @Test
    public void testParse_Id() {
        String query = "#id";
        Evaluator expected = new Evaluator.Id("id");
        assertEquals(expected, QueryParser.parse(query));
    }

    @Test
    public void testParse_Class() {
        String query = ".class";
        Evaluator expected = new Evaluator.Class("class");
        assertEquals(expected, QueryParser.parse(query));
    }

    @Test
    public void testParse_Tag() {
        String query = "div";
        Evaluator expected = new Evaluator.Tag("div");
        assertEquals(expected, QueryParser.parse(query));
    }

    @Test
    public void testParse_Attribute() {
        String query = "[attr=\"value\"]";
        Evaluator expected = new Evaluator.AttributeWithValue("attr", "\"value\"");
        assertEquals(expected, QueryParser.parse(query));
    }

    @Test
    public void testParse_MultipleAttributes() {
        String query = "[attr1=\"value1\"][attr2=\"value2\"]";
        Evaluator expected = new CombiningEvaluator.And(
            new Evaluator.AttributeWithValue("attr1", "\"value1\""),
            new Evaluator.AttributeWithValue("attr2", "\"value2\"")
        );
        assertEquals(expected, QueryParser.parse(query));
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_EmptyAttribute() {
        String query = "[]";
        QueryParser.parse(query);
    }

    @Test
    public void testParse_AllElements() {
        String query = "*";
        Evaluator expected = new Evaluator.AllElements();
        assertEquals(expected, QueryParser.parse(query));
    }

    @Test
    public void testParse_PseudoSelector_IndexLessThan() {
        String query = ":lt(5)";
        Evaluator expected = new Evaluator.IndexLessThan(5);
        assertEquals(expected, QueryParser.parse(query));
    }

    @Test
    public void testParse_PseudoSelector_IndexGreaterThan() {
        String query = ":gt(5)";
        Evaluator expected = new Evaluator.IndexGreaterThan(5);
        assertEquals(expected, QueryParser.parse(query));
    }

    @Test
    public void testParse_PseudoSelector_IndexEquals() {
        String query = ":eq(5)";
        Evaluator expected = new Evaluator.IndexEquals(5);
        assertEquals(expected, QueryParser.parse(query));
    }

    @Test
    public void testParse_PseudoSelector_Has() {
        String query = ":has(div)";
        Evaluator expected = new StructuralEvaluator.Has(new Evaluator.Tag("div"));
        assertEquals(expected, QueryParser.parse(query));
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_PseudoSelector_Has_EmptySubQuery() {
        String query = ":has()";
        QueryParser.parse(query);
    }

    @Test
    public void testParse_PseudoSelector_Contains() {
        String query = ":contains(test)";
        Evaluator expected = new Evaluator.ContainsText("test");
        assertEquals(expected, QueryParser.parse(query));
    }

    @Test
    public void testParse_PseudoSelector_ContainsOwn() {
        String query = ":containsOwn(test)";
        Evaluator expected = new Evaluator.ContainsOwnText("test");
        assertEquals(expected, QueryParser.parse(query));
    }

    @Test
    public void testParse_PseudoSelector_Matches() {
        String query = ":matches(\\d+)";
        Evaluator expected = new Evaluator.Matches(Pattern.compile("\\d+"));
        assertEquals(expected, QueryParser.parse(query));
    }

    @Test
    public void testParse_PseudoSelector_MatchesOwn() {
        String query = ":matchesOwn(\\d+)";
        Evaluator expected = new Evaluator.MatchesOwn(Pattern.compile("\\d+"));
        assertEquals(expected, QueryParser.parse(query));
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_PseudoSelector_Matches_EmptyRegex() {
        String query = ":matches()";
        QueryParser.parse(query);
    }

    @Test
    public void testParse_PseudoSelector_Not() {
        String query = ":not(div)";
        Evaluator expected = new StructuralEvaluator.Not(new Evaluator.Tag("div"));
        assertEquals(expected, QueryParser.parse(query));
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_PseudoSelector_Not_EmptySubQuery() {
        String query = ":not()";
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_UnhandledSelector() {
        String query = ":unknown";
        QueryParser.parse(query);
    }

    // Additional test cases can be added for edge cases and error scenarios

}