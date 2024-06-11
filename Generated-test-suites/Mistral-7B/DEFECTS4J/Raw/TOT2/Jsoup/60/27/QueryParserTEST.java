import org.jsoup.parser.QueryParser;
import org.jsoup.parser.Selector;
import org.jsoup.parser.TokenQueue;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class QueryParserTest {

    @Test
    public void testParse_EmptyQuery() {
        String query = "";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(null, parser.parse());
    }

    @Test
    public void testParse_TagQuery() {
        String query = "div";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new Evaluator.Tag("div"), parser.parse());
    }

    @Test
    public void testParse_TagQueryWithClass() {
        String query = "div.className";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new CombiningEvaluator.And(new Evaluator.Tag("div"), new Evaluator.Class("className")), parser.parse());
    }

    @Test
    public void testParse_IdQuery() {
        String query = "#id";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new Evaluator.Id("id"), parser.parse());
    }

    @Test
    public void testParse_AttributeQuery() {
        String query = "[attribute='value']";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new Evaluator.AttributeWithValue("attribute", "value"), parser.parse());
    }

    @Test
    public void testParse_MultipleSelectors_Comma() {
        String query = "div, p";
        QueryParser parser = new QueryParser(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new Evaluator.Tag("p"));
        Assert.assertEquals(new CombiningEvaluator.Or(expectedEvaluators.toArray(new Evaluator[0])), parser.parse());
    }

    @Test
    public void testParse_MultipleSelectors_Space() {
        String query = "div p";
        QueryParser parser = new QueryParser(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new Evaluator.Tag("p"));
        Assert.assertEquals(new CombiningEvaluator.And(expectedEvaluators.toArray(new Evaluator[0])), parser.parse());
    }

    @Test
    public void testParse_DescendantSelectors() {
        String query = "div p";
        QueryParser parser = new QueryParser(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new StructuralEvaluator.HasDescendant(new Evaluator.Tag("p")));
        expectedEvaluators.add(new Evaluator.Tag("div"));
        Assert.assertEquals(new CombiningEvaluator.And(expectedEvaluators.toArray(new Evaluator[0])), parser.parse());
    }

    @Test
    public void testParse_PreviousSiblingSelectors() {
        String query = "p ~ div";
        QueryParser parser = new QueryParser(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new StructuralEvaluator.PreviousSibling(new Evaluator.Tag("div")));
        expectedEvaluators.add(new Evaluator.Tag("p"));
        Assert.assertEquals(new CombiningEvaluator.And(expectedEvaluators.toArray(new Evaluator[0])), parser.parse());
    }

    @Test
    public void testParse_ChildSelectors() {
        String query = "div > p";
        QueryParser parser = new QueryParser(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("p")));
        expectedEvaluators.add(new Evaluator.Tag("div"));
        Assert.assertEquals(new CombiningEvaluator.And(expectedEvaluators.toArray(new Evaluator[0])), parser.parse());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_InvalidQuery() {
        String query = "div a[b=c]"; // This query is not valid according to the given class structure
        new QueryParser(query).parse();
    }
}