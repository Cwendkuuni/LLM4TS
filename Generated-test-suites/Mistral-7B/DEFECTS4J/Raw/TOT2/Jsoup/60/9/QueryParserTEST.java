import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.select.QueryParser;
import org.jsoup.helper.StringUtil;

public class QueryParserTest {

    @Test
    public void testParseSimpleSelector() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals(evaluator.getClass().getSimpleName(), "Tag");
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "div[class='class1']";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Attribute);
        assertEquals(evaluator.getClass().getSimpleName(), "Attribute");
    }

    @Test
    public void testParseClassSelector() {
        String query = ".class1";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals(evaluator.getClass().getSimpleName(), "Class");
    }

    @Test
    public void testParseIdSelector() {
        String query = "#id1";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals(evaluator.getClass().getSimpleName(), "Id");
    }

    @Test
    public void testParseCombinedSelector() {
        String query = "div > p";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        Evaluator rootEval = ((CombiningEvaluator.And) evaluator).getChildren().get(0);
        assertTrue(rootEval instanceof Evaluator.Tag);
        assertEquals(rootEval.getClass().getSimpleName(), "Tag");
        Evaluator childEval = ((CombiningEvaluator.And) evaluator).getChildren().get(1);
        assertTrue(childEval instanceof Evaluator.Tag);
        assertEquals(childEval.getClass().getSimpleName(), "Tag");
    }
}