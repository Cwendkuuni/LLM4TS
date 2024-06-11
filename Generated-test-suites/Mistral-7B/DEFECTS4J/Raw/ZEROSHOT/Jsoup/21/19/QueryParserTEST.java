import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.QueryParser;
import org.jsoup.helper.StringUtil;
import java.util.ArrayList;
import java.util.List;

public class QueryParserTest {

    @Test
    public void testParseSimpleSelector() {
        String query = "body";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        Evaluator tagEvaluator = new Evaluator.Tag("body");
        expectedEvaluators.add(tagEvaluator);
        Assert.assertEquals(expectedEvaluators.get(0).getClass(), evaluator.getClass());
    }

    @Test
    public void testParseCombinatorSelector() {
        String query = "body > div";
        Evaluator evaluator = QueryParser.parse(query);

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        Evaluator tagEvaluator1 = new Evaluator.Tag("body");
        StructuralEvaluator.ImmediateParent parentEvaluator = new StructuralEvaluator.ImmediateParent(tagEvaluator1);
        Evaluator tagEvaluator2 = new Evaluator.Tag("div");
        expectedEvaluators.add(tagEvaluator1);
        expectedEvaluators.add(parentEvaluator);
        expectedEvaluators.add(tagEvaluator2);

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(expectedEvaluators);
        Assert.assertEquals(andEvaluator.getClass(), evaluator.getClass());
        Assert.assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "input[type=text]";
        Evaluator evaluator = QueryParser.parse(query);

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        Evaluator tagEvaluator = new Evaluator.Tag("input");
        Evaluator attributeEvaluator = new Evaluator.AttributeWithValue(new Evaluator.Attribute("type"), "text");
        expectedEvaluators.add(tagEvaluator);
        expectedEvaluators.add(attributeEvaluator);

        Assert.assertEquals(expectedEvaluators.get(0).getClass(), evaluator.getClass());
        Assert.assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParsePseudoClassSelector() {
        String query = ":active";
        Evaluator evaluator = QueryParser.parse(query);

        Evaluator pseudoClassEvaluator = new Evaluator.PseudoClass("active");
        Assert.assertEquals(pseudoClassEvaluator.getClass(), evaluator.getClass());
    }

    @Test
    public void testParseComplexSelector() {
        String query = "input[type=text]:has(label:containsOwn(Textbox))";
        Evaluator evaluator = QueryParser.parse(query);

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        Evaluator tagEvaluator1 = new Evaluator.Tag("input");
        Evaluator attributeEvaluator1 = new Evaluator.AttributeWithValue(new Evaluator.Attribute("type"), "text");
        Evaluator pseudoClassHasEvaluator = new StructuralEvaluator.Has(new Evaluator.Tag("label"));
        Evaluator pseudoClassContainsEvaluator = new Evaluator.ContainsOwnText("Textbox");

        expectedEvaluators.add(tagEvaluator1);
        expectedEvaluators.add(attributeEvaluator1);
        expectedEvaluators.add(pseudoClassHasEvaluator);
        expectedEvaluators.add(pseudoClassContainsEvaluator);

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(expectedEvaluators);
        Assert.assertEquals(andEvaluator.getClass(), evaluator.getClass());
        Assert.assertEquals(expectedEvaluators, evaluator.getChildren());
    }
}