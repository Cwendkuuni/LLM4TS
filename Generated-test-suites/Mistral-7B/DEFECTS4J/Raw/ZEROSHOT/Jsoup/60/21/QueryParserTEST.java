import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.QueryParser;
import org.jsoup.parser.TokenQueue;
import org.jsoup.helper.StringUtil;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        String query = "";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AllElements);
    }

    @Test
    public void testParseTagQuery() {
        String query = "div";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("div", evaluator.toString());
    }

    @Test
    public void testParseTagWithNamespaceQuery() {
        String query = "html:html";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.TagEndsWith);
        assertEquals("html", evaluator.toString());
    }

    @Test
    public void testParseClassQuery() {
        String query = ".class";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals(".class", evaluator.toString());
    }

    @Test
    public void testParseIdQuery() {
        String query = "#id";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals("#id", evaluator.toString());
    }

    @Test
    public void testParseAttributeQuery() {
        String query = "[attribute]";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Attribute);
        assertEquals("[attribute]", evaluator.toString());
    }

    @Test
    public void testParseAttributeWithValueQuery() {
        String query = "[attribute='value']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
        assertEquals("[attribute='value']", evaluator.toString());
    }

    @Test
    public void testParseAttributeStartingQuery() {
        String query = "[attribute^='value']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueStarting);
        assertEquals("[attribute^='value']", evaluator.toString());
    }

    @Test
    public void testParseAttributeEndingQuery() {
        String query = "[attribute$='value']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueEnding);
        assertEquals("[attribute$='value']", evaluator.toString());
    }

    @Test
    public void testParseAttributeContainingQuery() {
        String query = "[attribute*='value']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueContaining);
        assertEquals("[attribute*='value']", evaluator.toString());
    }

    @Test
    public void testParseAttributeMatchingQuery() {
        String query = "[attribute~='value']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueMatching);
        assertEquals("[attribute~='value']", evaluator.toString());
    }

    @Test
    public void testParseAttributeNotQuery() {
        String query = "[attribute!='value']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueNot);
        assertEquals("[attribute!='value']", evaluator.toString());
    }

    @Test
    public void testParseIndexLessThanQuery() {
        String query = ":lt(1)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IndexLessThan);
        assertEquals(":lt(1)", evaluator.toString());
    }

    @Test
    public void testParseIndexGreaterThanQuery() {
        String query = ":gt(1)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IndexGreaterThan);
        assertEquals(":gt(1)", evaluator.toString());
    }

    @Test
    public void testParseIndexEqualsQuery() {
        String query = ":eq(1)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IndexEquals);
        assertEquals(":eq(1)", evaluator.toString());
    }

    @Test
    public void testParseNthChildQuery() {
        String query = ":nth-child(1)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsNthChild);
        assertEquals(":nth-child(1)", evaluator.toString());
    }

    @Test
    public void testParseNthLastChildQuery() {
        String query = ":nth-last-child(1)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsNthLastChild);
        assertEquals(":nth-last-child(1)", evaluator.toString());
    }

    @Test
    public void testParseNthOfTypeQuery() {
        String query = ":nth-of-type(1)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsNthOfType);
        assertEquals(":nth-of-type(1)", evaluator.toString());
    }

    @Test
    public void testParseNthLastOfTypeQuery() {
        String query = ":nth-last-of-type(1)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsNthLastOfType);
        assertEquals(":nth-last-of-type(1)", evaluator.toString());
    }

    @Test
    public void testParseFirstChildQuery() {
        String query = ":first-child";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsFirstChild);
        assertEquals(":first-child", evaluator.toString());
    }

    @Test
    public void testParseLastChildQuery() {
        String query = ":last-child";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsLastChild);
        assertEquals(":last-child", evaluator.toString());
    }

    @Test
    public void testParseFirstOfTypeQuery() {
        String query = ":first-of-type";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsFirstOfType);
        assertEquals(":first-of-type", evaluator.toString());
    }

    @Test
    public void testParseLastOfTypeQuery() {
        String query = ":last-of-type";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsLastOfType);
        assertEquals(":last-of-type", evaluator.toString());
    }

    @Test
    public void testParseOnlyChildQuery() {
        String query = ":only-child";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsOnlyChild);
        assertEquals(":only-child", evaluator.toString());
    }

    @Test
    public void testParseOnlyOfTypeQuery() {
        String query = ":only-of-type";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsOnlyOfType);
        assertEquals(":only-of-type", evaluator.toString());
    }

    @Test
    public void testParseEmptyQueryWithDescendantsSelector() {
        String query = ":empty > *";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof StructuralEvaluator.Has);
    }

    @Test
    public void testParseHasSelector() {
        String query = ":has(#id)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof StructuralEvaluator.Has);
        assertEquals(":has(#id)", evaluator.toString());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        String query = ":not";
        QueryParser parser = new QueryParser(query);
        parser.parse();
        fail("Expected SelectorParseException for invalid query :not");
    }

    @Test
    public void testParseComplexQuery() {
        String query = "div#id > .class:not(.otherclass)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        CombiningEvaluator and = (CombiningEvaluator) evaluator;
        CombiningEvaluator or = (CombiningEvaluator) and.getLeft();
        Evaluator tag = or.getLeft();
        assertTrue(tag instanceof Evaluator.Tag);
        assertEquals("div", tag.toString());
        Evaluator id = or.getRight();
        assertTrue(id instanceof Evaluator.Id);
        assertEquals("#id", id.toString());
        Evaluator not = and.getRight();
        assertTrue(not instanceof StructuralEvaluator.Not);
        Evaluator classEvaluator = (Evaluator.Class) not.getChild();
        assertEquals(".class", classEvaluator.toString());
        Evaluator otherClassEvaluator = classEvaluator.getNextSibling();
        assertEquals(".otherclass", otherClassEvaluator.toString());
    }
}