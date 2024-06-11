import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import org.jsoup.select.Evaluator;
import org.jsoup.select.QueryParser;
import org.junit.Test;

public class QueryParserTest {
    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse("div");
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) evaluator).tagName());

        evaluator = QueryParser.parse("#id");
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals("id", ((Evaluator.Id) evaluator).id());

        evaluator = QueryParser.parse(".class");
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals("class", ((Evaluator.Class) evaluator).className());

        evaluator = QueryParser.parse("[attr]");
        assertTrue(evaluator instanceof Evaluator.Attribute);
        assertEquals("attr", ((Evaluator.Attribute) evaluator).key());

        evaluator = QueryParser.parse("[attr=value]");
        assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
        assertEquals("attr", ((Evaluator.AttributeWithValue) evaluator).key());
        assertEquals("value", ((Evaluator.AttributeWithValue) evaluator).value());

        evaluator = QueryParser.parse(":containsOwn(text)");
        assertTrue(evaluator instanceof Evaluator.ContainsOwnText);
        assertEquals("text", ((Evaluator.ContainsOwnText) evaluator).getText());

        evaluator = QueryParser.parse(":matches(regex)");
        assertTrue(evaluator instanceof Evaluator.Matches);
        assertEquals(Pattern.compile("regex"), ((Evaluator.Matches) evaluator).pattern());

        evaluator = QueryParser.parse(":not(selector)");
        assertTrue(evaluator instanceof StructuralEvaluator.Not);
        assertEquals(QueryParser.parse("selector"), ((StructuralEvaluator.Not) evaluator).evaluator());
    }
}