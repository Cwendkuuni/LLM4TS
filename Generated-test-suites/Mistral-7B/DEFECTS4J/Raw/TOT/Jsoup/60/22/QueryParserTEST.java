import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.jsoup.helper.StringUtil;

public class QueryParserTest {

    @Test
    public void testParseSimpleSelector() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("div", evaluator.toString());
    }

    @Test
    public void testParseComplexSelectorWithId() {
        String query = "div#id";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals("div", evaluator.getClass().getSimpleName());
        assertEquals("id", evaluator.getValue());
    }

    @Test
    public void testParseComplexSelectorWithClass() {
        String query = "div.class";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals("div", evaluator.getClass().getSimpleName());
        assertEquals("class", evaluator.getValue());
    }

    @Test
    public void testParseComplexSelectorWithTagAndClass() {
        String query = "div.class1.class2";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof CombiningEvaluator.And);

        Evaluator tagEvaluator = ((CombiningEvaluator.And) evaluator).getChild(0);
        assertTrue(tagEvaluator instanceof Evaluator.Tag);
        assertEquals("div", tagEvaluator.getClass().getSimpleName());

        Evaluator classEvaluator = ((CombiningEvaluator.And) evaluator).getChild(1);
        assertTrue(classEvaluator instanceof CombiningEvaluator.Or);

        Evaluator class1Evaluator = ((CombiningEvaluator.Or) classEvaluator).getChild(0);
        assertTrue(class1Evaluator instanceof Evaluator.Class);
        assertEquals("class1", class1Evaluator.getValue());

        Evaluator class2Evaluator = ((CombiningEvaluator.Or) classEvaluator).getChild(1);
        assertTrue(class2Evaluator instanceof Evaluator.Class);
        assertEquals("class2", class2Evaluator.getValue());
    }

    @Test
    public void testParseComplexSelectorWithAttribute() {
        String query = "div[data-test]";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Attribute);
        assertEquals("div", evaluator.getClass().getSimpleName());
        assertEquals("data-test", evaluator.getValue());
    }

    @Test
    public void testParseComplexSelectorWithAttributeValue() {
        String query = "div[data-test='test']";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
        assertEquals("div", evaluator.getClass().getSimpleName());
        assertEquals("data-test", evaluator.getValue());
        assertEquals("test", evaluator.getValueAsString());
    }

    @Test
    public void testParseComplexSelectorWithCombinatorGreaterThan() {
        String query = "div > p";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof CombiningEvaluator.And);

        Evaluator tagDivEvaluator = ((CombiningEvaluator.And) evaluator).getChild(0);
        assertTrue(tagDivEvaluator instanceof Evaluator.Tag);
        assertEquals("div", tagDivEvaluator.getClass().getSimpleName());

        Evaluator combinatorEvaluator = ((CombiningEvaluator.And) evaluator).getChild(1);
        assertTrue(combinatorEvaluator instanceof StructuralEvaluator.ImmediateChild);
        assertEquals("div", complinatorEvaluator.getLeftChild().getClass().getSimpleName());
        assertEquals("p", complinatorEvaluator.getRightChild().getClass().getSimpleName());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidSelector() {
        String query = "divfoo";
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidSelectorWithEmptyAttribute() {
        String query = "div[ ]";
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidSelectorWithInvalidCombinator() {
        String query = "div!!p";
        QueryParser.parse(query);
    }
}