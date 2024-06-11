import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.QueryParser;
import org.jsoup.parser.TokenQueue;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.select.Evaluator;

public class QueryParserTest {

    @Test
    public void testParseSimpleSelector() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        Evaluator expectedEvaluator = new Evaluator.Tag("div");
        assertEquals(expectedEvaluator, evaluator);
    }

    @Test
    public void testParseComplexSelector() {
        String query = "div.class1.class2 > ul > li:nth-child(2)";
        Evaluator evaluator = QueryParser.parse(query);

        Evaluator expectedEvaluator = new CombiningEvaluator.And(
                new Evaluator.Tag("div"),
                new CombiningEvaluator.And(
                        new Evaluator.Class("class1"),
                        new Evaluator.Class("class2")
                ),
                new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("ul")),
                new Evaluator.IsNthChild(2, 0)
        );

        assertEquals(expectedEvaluator, evaluator);
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "input[type=text]";
        Evaluator evaluator = QueryParser.parse(query);

        Evaluator expectedEvaluator = new Evaluator.Attribute(
                "type",
                new Evaluator.AttributeValue("text")
        );

        assertEquals(expectedEvaluator, evaluator);
    }

    @Test
    public void testParseIdSelector() {
        String query = "#myId";
        Evaluator evaluator = QueryParser.parse(query);

        Evaluator expectedEvaluator = new Evaluator.Id("myId");
        assertEquals(expectedEvaluator, evaluator);
    }

    @Test
    public void testParseClassSelector() {
        String query = ".class1.class2";
        Evaluator evaluator = QueryParser.parse(query);

        Evaluator expectedEvaluator = new CombiningEvaluator.And(
                new Evaluator.Class("class1"),
                new Evaluator.Class("class2")
        );

        assertEquals(expectedEvaluator, evaluator);
    }

    @Test
    public void testParseCombinedSelectors() {
        String query = "div > ul > li, div > ul > ol > li";
        Evaluator evaluator = QueryParser.parse(query);

        Evaluator expectedEvaluator = new CombiningEvaluator.Or(
                new CombiningEvaluator.And(
                        new Evaluator.Tag("div"),
                        new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("ul")),
                        new Evaluator.Tag("li")
                ),
                new CombiningEvaluator.And(
                        new Evaluator.Tag("div"),
                        new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("ul")),
                        new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("ol")),
                        new Evaluator.Tag("li")
                )
        );

        assertEquals(expectedEvaluator, evaluator);
    }

    @Test
    public void testParsePsedoClassSelectors() {
        String query = "ul:empty";
        Evaluator evaluator = QueryParser.parse(query);

        Evaluator expectedEvaluator = new Evaluator.IsEmpty();
        assertEquals(expectedEvaluator, evaluator);
    }

    @Test
    public void testParseNthChildSelectors() {
        String query = "li:nth-child(2)";
        Evaluator evaluator = QueryParser.parse(query);

        Evaluator expectedEvaluator = new Evaluator.IsNthChild(1, 1);
        assertEquals(expectedEvaluator, evaluator);

        query = "li:nth-child(2n+1)";
        evaluator = QueryParser.parse(query);

        expectedEvaluator = new Evaluator.IsNthChild(0, 2);
        assertEquals(expectedEvaluator, evaluator);

        query = "li:nth-child(-n+3)";
        evaluator = QueryParser.parse(query);

        expectedEvaluator = new Evaluator.IsNthChild(2, 0);
        assertEquals(expectedEvaluator, evaluator);
    }

    @Test
    public void testParseNthOfTypeSelectors() {
        String query = "li:nth-of-type(2)";
        Evaluator evaluator = QueryParser.parse(query);

        Evaluator expectedEvaluator = new Evaluator.IsNthOfType(1, 1);
        assertEquals(expectedEvaluator, evaluator);

        query = "li:nth-of-type(2n+1)";
        evaluator = QueryParser.parse(query);

        expectedEvaluator = new Evaluator.IsNthOfType(0, 2);
        assertEquals(expectedEvaluator, evaluator);

        query = "li:nth-of-type(-n+3)";
        evaluator = QueryParser.parse(query);

        expectedEvaluator = new Evaluator.IsNthOfType(2, 0);
        assertEquals(expectedEvaluator, evaluator);
    }

    @Test
    public void testParseHasSelector() {
        String query = "div:has(img[src='image.png'])";
        Evaluator evaluator = QueryParser.parse(query);

        Evaluator expectedEvaluator = new StructuralEvaluator.Has(
                new Evaluator.Attribute(
                        "src",
                        new Evaluator.AttributeValue("image.png")
                )
        );

        assertEquals(expectedEvaluator, evaluator);
    }

    @Test
    public void testParseNotSelector() {
        String query = "div:not(.class1)";
        Evaluator evaluator = QueryParser.parse(query);

        Evaluator expectedEvaluator = new StructuralEvaluator.Not(
                new Evaluator.Class("class1")
        );

        assertEquals(expectedEvaluator, evaluator);
    }
}