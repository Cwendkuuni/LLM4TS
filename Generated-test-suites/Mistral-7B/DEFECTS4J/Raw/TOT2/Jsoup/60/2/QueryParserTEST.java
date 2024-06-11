import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.QueryParser;
import org.jsoup.parser.TokenQueue;
import org.jsoup.parser.Selector;
import org.jsoup.parser.StructuralEvaluator;
import org.jsoup.parser.CombiningEvaluator;
import org.jsoup.parser.Evaluator;
import org.jsoup.parser.ElementSelector;
import org.jsoup.helper.StringUtil;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        String query = "";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertTrue(evaluator.size() == 0);
    }

    @Test
    public void testParseSimpleTagSelector() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("div", evaluator.toString());
    }

    @Test
    public void testParseIdSelector() {
        String query = "#myId";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals("myId", evaluator.toString());
    }

    @Test
    public void testParseClassSelector() {
        String query = ".myClass";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals("myClass", evaluator.toString());
    }

    @Test
    public void testParseMultipleSelectors() {
        String query = "div#myId.myClass";
        Evaluator evaluator = QueryParser.parse(query);
        Evaluator idEvaluator = null;
        Evaluator classEvaluator = null;

        for (Evaluator e : evaluator) {
            if (e instanceof Evaluator.Id) {
                idEvaluator = e;
            } else if (e instanceof Evaluator.Class) {
                classEvaluator = e;
            }
        }

        assertTrue(idEvaluator instanceof Evaluator.Id);
        assertEquals("myId", idEvaluator.toString());
        assertTrue(classEvaluator instanceof Evaluator.Class);
        assertEquals("myClass", classEvaluator.toString());
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "[title]";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Attribute);
        assertEquals("title", evaluator.toString());
    }

    @Test
    public void testParseAttributeSelectorWithValue() {
        String query = "[title='hello']";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
        assertEquals("title", evaluator.toString());
        assertEquals("hello", ((Evaluator.AttributeWithValue) evaluator).getValue());
    }

    @Test
    public void testParseAttributeSelectorWithOperator() {
        String query = "[title^='hello']";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueStarting);
        assertEquals("title", evaluator.toString());
        assertEquals("hello", ((Evaluator.AttributeWithValueStarting) evaluator).getValue());
    }

    @Test
    public void testParseAttributeSelectorWithMultipleOperators() {
        String query = "[title^='hello'|= 'world']";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof CombiningEvaluator.Or);
        Evaluator orEvaluator = ((CombiningEvaluator.Or) evaluator);

        Evaluator left = orEvaluator.getLeft();
        assertTrue(left instanceof Evaluator.AttributeWithValueStarting);
        assertEquals("title", left.toString());
        assertEquals("hello", ((Evaluator.AttributeWithValueStarting) left).getValue());

        Evaluator right = orEvaluator.getRight();
        assertTrue(right instanceof Evaluator.AttributeWithValue);
        assertEquals("title", right.toString());
        assertEquals("world", ((Evaluator.AttributeWithValue) right).getValue());
    }

    @Test
    public void testParseSimpleDescendantSelector() {
        String query = "div p";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        Evaluator left = ((CombiningEvaluator.And) evaluator).getLeft();
        assertTrue(left instanceof StructuralEvaluator.ImmediateParent);
        assertEquals("div", left.toString());
        Evaluator right = ((CombiningEvaluator.And) evaluator).getRight();
        assertTrue(right instanceof Evaluator.Tag);
        assertEquals("p", right.toString());
    }

    @Test
    public void testParseComplexSelector() {
        String query = "div#myId.myClass > p#subId.subClass:has(ul.list)";
        Evaluator evaluator = QueryParser.parse(query);
        Evaluator idEvaluator = null;
        Evaluator classEvaluator = null;
        Evaluator hasEvaluator = null;

        for (Evaluator e : evaluator) {
            if (e instanceof Evaluator.Id) {
                idEvaluator = e;
            } else if (e instanceof Evaluator.Class) {
                classEvaluator = e;
            } else if (e instanceof StructuralEvaluator.Has) {
                hasEvaluator = e;
            }
        }

        assertTrue(idEvaluator instanceof Evaluator.Id);
        assertEquals("myId", idEvaluator.toString());
        assertTrue(classEvaluator instanceof Evaluator.Class);
        assertEquals("myClass", classEvaluator.toString());
        assertTrue(hasEvaluator instanceof StructuralEvaluator.Has);

        Selector subQuery = ((StructuralEvaluator.Has) hasEvaluator).getSelector();
        Evaluator subEvaluator = QueryParser.parse(subQuery.toString());
        assertTrue(subEvaluator instanceof Evaluator.Tag);
        assertEquals("ul", subEvaluator.toString());
    }

    @Test
    public void testParseComplexSelectorWithException() {
        String query = "div#myId.myClass > p#subId.subClass:has(ul[list='abc']";
        try {
            Evaluator evaluator = QueryParser.parse(query);
            fail("Expected SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            // expected
        }
    }
}