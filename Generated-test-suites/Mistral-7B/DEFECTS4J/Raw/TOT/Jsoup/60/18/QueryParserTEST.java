import org.jsoup.parser.TokenQueue;
import org.jsoup.select.Evaluator;
import org.jsoup.select.QueryParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParseSimpleSelector() {
        String query = "div.class1";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals("class1", evaluator.getCssProperty());
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
    }

    @Test
    public void testParseSimpleIdSelector() {
        String query = "#id1";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals("id1", evaluator.getCssProperty());
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
    }

    @Test
    public void testParseCombinedSelectors() {
        String query = "div.class1 > p.class2";
        Evaluator evaluator = QueryParser.parse(query);

        assertTrue(evaluator instanceof CombiningEvaluator.And);
        Evaluator left = ((CombiningEvaluator.And) evaluator).getLeftEvaluator();
        assertTrue(left instanceof Evaluator.Class);
        assertEquals("class1", left.getCssProperty());
        assertEquals(Evaluator.Type.ELEMENT, left.getType());

        Evaluator right = ((CombiningEvaluator.And) evaluator).getRightEvaluator();
        assertTrue(right instanceof StructuralEvaluator.ImmediateChild);
        assertEquals(Evaluator.Type.ELEMENT, right.getType());

        Evaluator grandChild = right.getChild();
        assertTrue(grandChild instanceof Evaluator.Class);
        assertEquals("class2", grandChild.getCssProperty());
        assertEquals(Evaluator.Type.ELEMENT, grandChild.getType());
    }

    @Test
    public void testParseAttributeSelectors() {
        String query = "input[type=text]";
        Evaluator evaluator = QueryParser.parse(query);

        assertTrue(evaluator instanceof Evaluator.Attribute);
        assertEquals("type", evaluator.getCssProperty());
        assertEquals("text", evaluator.getValue());
        assertEquals(Evaluator.Type.ATTRIBUTE, evaluator.getType());
    }

    @Test
    public void testParseComplexSelectors() {
        String query = "ul.list > li:first-child:has(> a[href^='http']):not(> a[href^='http://google.com'])";
        Evaluator evaluator = QueryParser.parse(query);

        assertTrue(evaluator instanceof CombiningEvaluator.And);
        Evaluator left = ((CombiningEvaluator.And) evaluator).getLeftEvaluator();
        assertTrue(left instanceof Evaluator.Class);
        assertEquals("list", left.getCssProperty());
        assertEquals(Evaluator.Type.ELEMENT, left.getType());

        Evaluator right = ((CombiningEvaluator.And) evaluator).getRightEvaluator();
        assertTrue(right instanceof StructuralEvaluator.Has);
        Evaluator hasEvaluator = ((StructuralEvaluator.Has) right).getChildEvaluator();
        assertTrue(hasEvaluator instanceof CombiningEvaluator.And);

        Evaluator notEvaluator = ((CombiningEvaluator.And) hasEvaluator).getRightEvaluator();
        assertTrue(notEvaluator instanceof Evaluator.AttributeWithValueNot);
        assertEquals("http://google.com", notEvaluator.getValue());
        assertEquals("href", notEvaluator.getCssProperty());
        assertEquals(Evaluator.Type.ATTRIBUTE, notEvaluator.getType());

        Evaluator andLeft = ((CombiningEvaluator.And) hasEvaluator).getLeftEvaluator();
        assertTrue(andLeft instanceof Evaluator.IsFirstChild);
        assertEquals(Evaluator.Type.FIRST_CHILD, andLeft.getType());

        Evaluator aEvaluator = andLeft.getChild();
        assertTrue(aEvaluator instanceof Evaluator.Attribute);
        assertEquals("href", aEvaluator.getCssProperty());
        assertTrue(aEvaluator.getValue().startsWith("http"));
        assertEquals(Evaluator.Type.ATTRIBUTE, aEvaluator.getType());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidSelector() {
        String query = "div.class1 > p.class2$";
        QueryParser.parse(query);
    }
}