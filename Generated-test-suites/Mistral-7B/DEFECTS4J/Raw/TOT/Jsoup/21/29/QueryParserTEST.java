import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.StructuralEvaluator;
import org.jsoup.select.Evaluator;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        QueryParser parser = new QueryParser("");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AllElements);
    }

    @Test
    public void testParseSimpleTagQuery() {
        QueryParser parser = new QueryParser("div");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.Tag.class, evaluator.getClass());
        assertEquals("div", evaluator.toString());
    }

    @Test
    public void testParseClassQuery() {
        QueryParser parser = new QueryParser(".myClass");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.Class.class, evaluator.getClass());
        assertEquals(".myClass", evaluator.toString());
    }

    @Test
    public void testParseIdQuery() {
        QueryParser parser = new QueryParser("#myId");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.Id.class, evaluator.getClass());
        assertEquals("#myId", evaluator.toString());
    }

    @Test
    public void testParseCombinedQueries() {
        QueryParser parser = new QueryParser("div.myClass");
        Evaluator evaluator = parser.parse();
        Evaluator expected = new CombiningEvaluator.And(new Evaluator.Tag("div"), new Evaluator.Class("myClass"));
        assertEquals(expected.getClass(), evaluator.getClass());
        assertEquals("div.myClass", evaluator.toString());
    }

    @Test
    public void testParseTagAndClassWithSpace() {
        QueryParser parser = new QueryParser("div .myClass");
        Evaluator evaluator = parser.parse();
        Evaluator expected = new CombiningEvaluator.And(new Evaluator.Tag("div"), new Evaluator.Class("myClass"));
        assertEquals(expected.getClass(), evaluator.getClass());
        assertEquals("div .myClass", evaluator.toString());
    }

    @Test
    public void testParseChildQuery() {
        QueryParser parser = new QueryParser("div > p");
        Evaluator evaluator = parser.parse();
        Evaluator expected = new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("p"), new Evaluator.Tag("div"));
        assertEquals(expected.getClass(), evaluator.getClass());
        assertEquals("div > p", evaluator.toString());
    }

    @Test
    public void testParseDescendantQuery() {
        QueryParser parser = new QueryParser("div p");
        Evaluator evaluator = parser.parse();
        Evaluator expected = new StructuralEvaluator.Descendant(new Evaluator.Tag("p"), new Evaluator.Tag("div"));
        assertEquals(expected.getClass(), evaluator.getClass());
        assertEquals("div p", evaluator.toString());
    }

    @Test
    public void testParseAttributeQuery() {
        QueryParser parser = new QueryParser("[href]");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.Attribute.class, evaluator.getClass());
        assertEquals("[href]", evaluator.toString());
    }

    @Test
    public void testParseAttributeWithValueQuery() {
        QueryParser parser = new QueryParser("[href='http://example.com']");
        Evaluator evaluator = parser.parse();
        Evaluator expected = new Evaluator.AttributeWithValue(Evaluator.Attribute.AttributeType.HREF, "http://example.com");
        assertEquals(expected.getClass(), evaluator.getClass());
        assertEquals("[href='http://example.com']", evaluator.toString());
    }

    @Test
    public void testParseAttributeWithValueStartingQuery() {
        QueryParser parser = new QueryParser("[href^='http']");
        Evaluator evaluator = parser.parse();
        Evaluator expected = new Evaluator.AttributeWithValueStarting(Evaluator.Attribute.AttributeType.HREF, "http");
        assertEquals(expected.getClass(), evaluator.getClass());
        assertEquals("[href^='http']", evaluator.toString());
    }

    @Test
    public void testParseAttributeWithValueEndingQuery() {
        QueryParser parser = new QueryParser("[href$='.pdf']");
        Evaluator evaluator = parser.parse();
        Evaluator expected = new Evaluator.AttributeWithValueEnding(Evaluator.Attribute.AttributeType.HREF, ".pdf");
        assertEquals(expected.getClass(), evaluator.getClass());
        assertEquals("[href$='.pdf']", evaluator.toString());
    }

    @Test
    public void testParseAttributeWithValueContainingQuery() {
        QueryParser parser = new QueryParser("[href*='google']");
        Evaluator evaluator = parser.parse();
        Evaluator expected = new Evaluator.AttributeWithValueContaining(Evaluator.Attribute.AttributeType.HREF, "google");
        assertEquals(expected.getClass(), evaluator.getClass());
        assertEquals("[href*='google']", evaluator.toString());
    }

    @Test
    public void testParseAttributeWithValueMatchingQuery() {
        QueryParser parser = new QueryParser("[href~='google']");
        Evaluator evaluator = parser.parse();
        Evaluator expected = new Evaluator.AttributeWithValueMatching(Evaluator.Attribute.AttributeType.HREF, Pattern.compile("google"));
        assertEquals(expected.getClass(), evaluator.getClass());
        assertEquals("[href~='google']", evaluator.toString());
    }

    @Test
    public void testParseMultipleAttributeQueries() {
        QueryParser parser = new QueryParser("[href and title]");
        Evaluator evaluator = parser.parse();
        Evaluator expected = new CombiningEvaluator.And(new Evaluator.Attribute(Evaluator.Attribute.AttributeType.HREF), new Evaluator.Attribute(Evaluator.Attribute.AttributeType.TITLE));
        assertEquals(expected.getClass(), evaluator.getClass());
        assertEquals("[href and title]", evaluator.toString());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        QueryParser parser = new QueryParser("div>p^foo");
        parser.parse();
    }
}