import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class QueryParserTest {

    @Test
    public void testParse_CombinatorWithRootElements_Success() {
        String query = ",[id=test] > .class + tag ~ [attr=value] ";
        Evaluator expected = new CombiningEvaluator.And(
                new StructuralEvaluator.Root(),
                new Evaluator.Attribute("id", "test"),
                new CombiningEvaluator.And(
                        new Evaluator.Class("class"),
                        new StructuralEvaluator.ImmediateParent(),
                        new CombiningEvaluator.And(
                                new Evaluator.Tag("tag"),
                                new StructuralEvaluator.ImmediatePreviousSibling(),
                                new CombiningEvaluator.And(
                                        new Evaluator.Attribute("attr", "value")
                                )
                        )
                )
        );

        Evaluator actual = QueryParser.parse(query);

        assertEquals(expected, actual);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_InvalidCombinator_ThrowException() {
        String query = "#test $ .class";
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_UnbalancedBrackets_ThrowException() {
        String query = "#test[attribute=[value";
        QueryParser.parse(query);
    }

    @Test
    public void testParse_MultipleCommaSeparatorsWithOr_Success() {
        String query = "#id1, #id2, #id3";
        Evaluator expected = new CombiningEvaluator.Or(
                new Evaluator.Id("id1"),
                new CombiningEvaluator.Or(
                        new Evaluator.Id("id2"),
                        new Evaluator.Id("id3")
                )
        );

        Evaluator actual = QueryParser.parse(query);

        assertEquals(expected, actual);
    }

    @Test
    public void testParse_CommaSeparatorWithComplexExpression_Success() {
        String query = "#id1 .class, [attr=value], tag";
        Evaluator expected = new CombiningEvaluator.Or(
                new CombiningEvaluator.And(
                        new Evaluator.Id("id1"),
                        new Evaluator.Class("class")
                ),
                new Evaluator.Attribute("attr", "value"),
                new Evaluator.Tag("tag")
        );

        Evaluator actual = QueryParser.parse(query);

        assertEquals(expected, actual);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_InvalidAttributeQuery_ThrowException() {
        String query = "[attr!=value!==]";
        QueryParser.parse(query);
    }

    @Test
    public void testParse_CombinatorWithEmptySubQuery_Success() {
        String query = " > ";
        Evaluator expected = new StructuralEvaluator.ImmediateParent();

        Evaluator actual = QueryParser.parse(query);

        assertEquals(expected, actual);
    }

    @Test
    public void testParse_EmptyQuery_ReturnAllElementsEvaluator() {
        String query = "";
        Evaluator expected = new Evaluator.AllElements();

        Evaluator actual = QueryParser.parse(query);

        assertEquals(expected, actual);
    }

    @Test
    public void testParse_ComplexQueryWithAllSelectors_Success() {
        String query = ":lt(5):gt(2):eq(3):has(#id):contains(text):containsOwn(text)";

        Evaluator expected = new CombiningEvaluator.And(
                new Evaluator.IndexLessThan(5),
                new CombiningEvaluator.And(
                        new Evaluator.IndexGreaterThan(2),
                        new CombiningEvaluator.And(
                                new Evaluator.IndexEquals(3),
                                new CombiningEvaluator.And(
                                        new StructuralEvaluator.Has(
                                                new Evaluator.Id("id")
                                        ),
                                        new CombiningEvaluator.And(
                                                new Evaluator.ContainsText("text"),
                                                new Evaluator.ContainsOwnText("text")
                                        )
                                )
                        )
                )
        );

        Evaluator actual = QueryParser.parse(query);

        assertEquals(expected, actual);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_NotSelectorWithEmptySubQuery_ThrowException() {
        String query = ":not()";
        QueryParser.parse(query);
    }

    @Test
    public void testParse_NotSelectorWithValidSubQuery_Success() {
        String query = ":not(.class)";
        Evaluator expected = new StructuralEvaluator.Not(
                new Evaluator.Class("class")
        );

        Evaluator actual = QueryParser.parse(query);

        assertEquals(expected, actual);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_InvalidIndex_ThrowException() {
        String query = ":lt(abc)";
        QueryParser.parse(query);
    }

    @Test
    public void testParse_MatchesRegexSelector_Success() {
        String query = ":matchesOwn(^abc.*)";
        Evaluator expected = new Evaluator.MatchesOwn(
                Pattern.compile("^abc.*")
        );

        Evaluator actual = QueryParser.parse(query);

        assertEquals(expected, actual);
    }

}