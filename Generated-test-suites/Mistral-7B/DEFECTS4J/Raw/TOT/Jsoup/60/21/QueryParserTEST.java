import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParseSimpleSelector() {
        String query = "div.class1";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Class("class1"));
        expected.add(new Evaluator.Tag("div"));
        assertEquals(evaluator, new CombiningEvaluator.And(expected));
    }

    @Test
    public void testParseComplexSelectorWithCombiner() {
        String query = "div.class1 > ul > li";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("div"));
        expected.add(new Evaluator.Class("class1"));
        expected.add(new CombiningEvaluator.And(
                new Evaluator.Tag("ul"),
                new StructuralEvaluator.ImmediateChild(expected.get(0))
        ));
        expected.add(new Evaluator.Tag("li"));
        assertEquals(evaluator, new CombiningEvaluator.And(expected));
    }

    @Test
    public void testParseSelectorWithAttribute() {
        String query = "input[type='text']";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("input"));
        expected.add(new Evaluator.AttributeWithValue("type", "text"));
        assertEquals(evaluator, new CombiningEvaluator.And(expected));
    }

    @Test
    public void testParseSelectorWithMultipleAttributes() {
        String query = "input[type='text'][name='username']";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("input"));
        expected.add(new CombiningEvaluator.And(
                new Evaluator.AttributeWithValue("type", "text"),
                new Evaluator.AttributeWithValue("name", "username")
        ));
        assertEquals(evaluator, new CombiningEvaluator.And(expected));
    }

    @Test
    public void testParseSelectorWithPseudoClass() {
        String query = "a:link";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("a"));
        expected.add(new Evaluator.PseudoClass("link"));
        assertEquals(evaluator, new CombiningEvaluator.And(expected));
    }

    @Test
    public void testParseSelectorWithNegation() {
        String query = "div:not(.class1)";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("div"));
        expected.add(new Evaluator.PseudoClass("not"));
        expected.add(new Evaluator.Class("class1"));
        expected.add(new StructuralEvaluator.Not(expected.get(0)));
        assertEquals(evaluator, new CombiningEvaluator.And(expected));
    }

    @Test
    public void testParseSelectorWithMultipleCombiners() {
        String query = "div.class1 > ul.class2 > li.class3";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("div"));
        expected.add(new Evaluator.Class("class1"));
        expected.add(new CombiningEvaluator.And(
                new Evaluator.Tag("ul"),
                new StructuralEvaluator.ImmediateChild(expected.get(0))
        ));
        expected.add(new Evaluator.Class("class2"));
        expected.add(new CombiningEvaluator.And(
                new Evaluator.Tag("li"),
                new StructuralEvaluator.ImmediateChild(expected.get(1))
        ));
        expected.add(new Evaluator.Class("class3"));
        assertEquals(evaluator, new CombiningEvaluator.And(expected));
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testInvalidSelector() {
        String query = "div[type='text']"; // type is not a valid attribute for div tag
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testInvalidSelectorWithUnescapedParenthesis() {
        String query = "div(class1)";
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testInvalidSelectorWithUnmatchedParenthesis() {
        String query = "div(class1)(";
        QueryParser.parse(query);
    }
}