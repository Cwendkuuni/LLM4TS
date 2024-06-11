import org.jsoup.parser.QueryParser;
import org.jsoup.parser.TokenQueue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QueryParserTest {
    @Test
    public void testParseEmptyQuery() {
        String query = "";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.class, evaluator.getClass());
    }

    @Test
    public void testParseTagQuery() {
        String query = "div";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.Tag.class, evaluator.getClass());
        assertEquals("div", evaluator.toString());
    }

    @Test
    public void testParseIdQuery() {
        String query = "#id";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.Id.class, evaluator.getClass());
        assertEquals("#id", evaluator.toString());
    }

    @Test
    public void testParseClassQuery() {
        String query = ".class";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.Class.class, evaluator.getClass());
        assertEquals(".class", evaluator.toString());
    }

    @Test
    public void testParseAttributeQuery() {
        String query = "[title=Test]";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.Attribute.class, evaluator.getClass());
        assertEquals("[title=Test]", evaluator.toString());
    }

    @Test
    public void testParseCombinedQuery() {
        String query = "div#id > .class";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
        assertEquals("div#id > .class", evaluator.toString());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testInvalidQuery() {
        String query = "div#id > .class#invalid";
        QueryParser parser = new QueryParser(query);
        parser.parse();
    }
}