import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class QueryParserTest {
    @Test
    public void testParse() {
        // Given
        String query = "div.class1 > p.class2";
        QueryParser parser = new QueryParser(query);

        // When
        Evaluator evaluator = parser.parse();

        // Then
        Evaluator expectedEvaluator = new CombiningEvaluator.And(
                new StructuralEvaluator.Tag("div"),
                new CombiningEvaluator.And(
                        new StructuralEvaluator.Class("class1"),
                        new StructuralEvaluator.ImmediateChild(
                                new CombiningEvaluator.And(
                                        new StructuralEvaluator.Tag("p"),
                                        new StructuralEvaluator.Class("class2")))
                )
        );

        assertEquals(expectedEvaluator, evaluator);
    }
}