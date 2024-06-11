import org.junit.Test;
import org.jsoup.select.QueryParser;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        QueryParser parser = new QueryParser("");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AllElements);
    }

    @Test
    public void testParseTagQuery() {
        QueryParser parser = new QueryParser("div");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("div", evaluator.getClass().getSimpleName());
    }

    @Test
    public void testParseIdQuery() {
        QueryParser parser = new QueryParser("#id");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals("id", evaluator.getId());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        QueryParser parser = new QueryParser(":invalid");
        parser.parse(); // this should throw an exception
    }

    @Test
    public void testParseComplexQuery() {
        QueryParser parser = new QueryParser("#id > div:contains(text)");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof CombiningEvaluator.And);

        CombiningEvaluator.And and = (CombiningEvaluator.And) evaluator;
        Evaluator left = and.getLeft();
        Evaluator right = and.getRight();

        assertTrue(left instanceof Evaluator.Id);
        assertEquals("id", left.getId());

        assertTrue(right instanceof StructuralEvaluator.Has);
        StructuralEvaluator.Has has = (StructuralEvaluator.Has) right;
        Evaluator subQuery = has.getSubQuery();
        assertTrue(subQuery instanceof Evaluator.Text);
        assertEquals("text", subQuery.getText());
    }
}