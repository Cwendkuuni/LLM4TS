import org.jsoup.parser.QueryParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class QueryParserTest {

    @Test
    public void testParseQuery() {
        // Test parsing a simple query with no combinators
        String query = "div.class1";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = List.of(
                new Evaluator.Class("class1")
        );
        assertEvaluatorsEqual(evaluator, expectedEvaluators);

        // Test parsing a query with descendant combinator
        query = "div > p";
        evaluator = QueryParser.parse(query);
        expectedEvaluators = List.of(
                new Evaluator.Tag("div"),
                new CombiningEvaluator.And(
                        new StructuralEvaluator.ImmediateChild(),
                        new Evaluator.Tag("p")
                )
        );
        assertEvaluatorsEqual(evaluator, expectedEvaluators);

        // Test parsing a query with adjacent sibling combinator
        query = "div > p ~ ul";
        evaluator = QueryParser.parse(query);
        expectedEvaluators = List.of(
                new Evaluator.Tag("div"),
                new StructuralEvaluator.ImmediatePreviousSibling(),
                new Evaluator.Tag("ul")
        );
        assertEvaluatorsEqual(evaluator, expectedEvaluators);

        // Test parsing a query with attribute selector
        query = "div[title='Test']";
        evaluator = QueryParser.parse(query);
        expectedEvaluators = List.of(
                new Evaluator.AttributeWithValue("title", "Test")
        );
        assertEvaluatorsEqual(evaluator, expectedEvaluators);

        // Test parsing a query with multiple selectors and combinators
        query = "div.class1 > p.class2:contains('Text') ~ ul[data-test='test']";
        evaluator = QueryParser.parse(query);
        expectedEvaluators = List.of(
                new Evaluator.Tag("div"),
                new CombiningEvaluator.And(
                        new Evaluator.Class("class1"),
                        new StructuralEvaluator.ImmediateChild()
                ),
                new CombiningEvaluator.And(
                        new Evaluator.Tag("p"),
                        new CombiningEvaluator.And(
                                new Evaluator.Class("class2"),
                                new Evaluator.ContainsText("Text")
                        )
                ),
                new StructuralEvaluator.PreviousSibling(),
                new Evaluator.AttributeWithValue("data-test", "test")
        );
        assertEvaluatorsEqual(evaluator, expectedEvaluators);
    }

    // Helper method to compare the evaluators
    private void assertEvaluatorsEqual(Evaluator evaluator, List<Evaluator> expectedEvaluators) {
        CombiningEvaluator actualCombiningEvaluator = (CombiningEvaluator) evaluator;
        CombiningEvaluator expectedCombiningEvaluator = new CombiningEvaluator.And(expectedEvaluators.toArray(new Evaluator[0]));
        Evaluator actualEvaluator = actualCombiningEvaluator.rightMostEvaluator();
        Evaluator expectedEvaluator = expectedCombiningEvaluator.rightMostEvaluator();

        if (actualEvaluator.getClass() != expectedEvaluator.getClass()) {
            Assert.fail("Unexpected evaluator types: " + actualEvaluator.getClass().getName() + " vs " + expectedEvaluator.getClass().getName());
        }

        Assert.assertEquals("Unexpected evaluator values", expectedEvaluator.toString(), actualEvaluator.toString());
    }
}