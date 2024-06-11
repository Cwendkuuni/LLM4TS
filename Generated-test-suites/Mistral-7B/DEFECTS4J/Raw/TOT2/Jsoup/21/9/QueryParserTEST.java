
import org.jsoup.helper.StringUtil;
import org.jsoup.parser.Selector;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;
import org.jsoup.select.IdNameEvaluator;
import org.jsoup.select.StructuralEvaluator;
import org.jsoup.select.TagNameEvaluator;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        // Test empty query
        Evaluator evaluator = QueryParser.parse("").parse();
        assertEquals(Evaluator.AllElements.class, evaluator.getClass());

        // Test parse with valid query
        String cssQuery = "div > p.red > span.blue";
        Evaluator expectedEvaluator = new And(new StructuralEvaluator.ImmediateParent(new TagNameEvaluator("div")),
                new StructuralEvaluator.ImmediateChild(new TagNameEvaluator("p"), new ClassNameEvaluator("red")),
                new StructuralEvaluator.ImmediateChild(new TagNameEvaluator("span"), new ClassNameEvaluator("blue")));

        Evaluator evaluator2 = QueryParser.parse(cssQuery).parse();
        assertEquals(expectedEvaluator.getClass(), evaluator2.getClass());

        // Test parse with query containing unknown combinator
        String cssQueryError = "div > p.red > span.blue > _";
        assertThrows(Selector.SelectorParseException.class, () -> QueryParser.parse(cssQueryError).parse());

        // Test parse with query containing unmatched parentheses
        String cssQueryParentheses = "div > p.red > span.blue > (#test)";
        assertThrows(Selector.SelectorParseException.class, () -> QueryParser.parse(cssQueryParentheses).parse());

        String cssQueryParentheses2 = "div > p.red > span.blue > )#test";
        assertThrows(Selector.SelectorParseException.class, () -> QueryParser.parse(cssQueryParentheses2).parse());

        // Test parse with query containing valid id
        String cssQueryId = "div > p.red > span.blue > #test";
        Evaluator expectedEvaluatorId = new And(new StructuralEvaluator.ImmediateParent(new TagNameEvaluator("div")),
                new StructuralEvaluator.ImmediateChild(new TagNameEvaluator("p"), new ClassNameEvaluator("red")),
                new StructuralEvaluator.ImmediateChild(new TagNameEvaluator("span"), new ClassNameEvaluator("blue"),
                        new IdNameEvaluator("test")));

        Evaluator evaluatorId = QueryParser.parse(cssQueryId).parse();
        assertEquals(expectedEvaluatorId.getClass(), evaluatorId.getClass());
    }
}