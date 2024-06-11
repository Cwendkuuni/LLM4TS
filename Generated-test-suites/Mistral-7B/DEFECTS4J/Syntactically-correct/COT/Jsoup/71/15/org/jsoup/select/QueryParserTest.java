import org.jsoup.parser.QueryParser;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.List;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

public class QueryParserTest {

    @Test
    public void testParse_EmptyQuery() {
        QueryParser parser = new QueryParser("");
        Evaluator result = parser.parse();
        Assert.assertThat(result, is(nullValue()));
    }

    @Test
    public void testParse_SimpleSelector() {
        QueryParser parser = new QueryParser("div");
        Evaluator result = parser.parse();
        Assert.assertThat(result, instanceOf(Evaluator.Tag.class));
        Assert.assertThat(((Evaluator.Tag) result).tagName, is("div"));
    }

    @Test
    public void testParse_ClassSelector() {
        QueryParser parser = new QueryParser(".button");
        Evaluator result = parser.parse();
        Assert.assertThat(result, instanceOf(Evaluator.Class.class));
        Assert.assertThat(((Evaluator.Class) result).className, is("button"));
    }

    @Test
    public void testParse_IDSelector() {
        QueryParser parser = new QueryParser("#content");
        Evaluator result = parser.parse();
        Assert.assertThat(result, instanceOf(Evaluator.Id.class));
        Assert.assertThat(((Evaluator.Id) result).id, is("content"));
    }

    @Test
    public void testParse_CombinedSelectors() {
        QueryParser parser = new QueryParser("div.button");
        Evaluator result = parser.parse();
        Assert.assertThat(result, instanceOf(CombiningEvaluator.And.class));
        List<Evaluator> evals = ((CombiningEvaluator.And) result).getChildren();
        Assert.assertThat(evals.get(0), instanceOf(Evaluator.Tag.class));
        Assert.assertThat(((Evaluator.Tag) evals.get(0)).tagName, is("div"));
        Assert.assertThat(evals.get(1), instanceOf(Evaluator.Class.class));
        Assert.assertThat(((Evaluator.Class) evals.get(1)).className, is("button"));
    }

    @Test
    public void testParse_AttributeSelector() {
        QueryParser parser = new QueryParser("[href='http://example.com']");
        Evaluator result = parser.parse();
        Assert.assertThat(result, instanceOf(Evaluator.Attribute.class));
        Assert.assertThat(((Evaluator.Attribute) result).attributeName, is("href"));
        Assert.assertThat(((Evaluator.Attribute) result).attributeValue, is("http://example.com"));
    }

    @Test
    public void testParse_PseudoClassSelector() {
        QueryParser parser = new QueryParser(":hover");
        Evaluator result = parser.parse();
        Assert.assertThat(result, instanceOf(PseudoClassEvaluator.class));
        Assert.assertThat(((PseudoClassEvaluator) result).pseudoClass, is("hover"));
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_InvalidQuery() {
        QueryParser parser = new QueryParser("invalid:query");
        parser.parse(); // assert exception is thrown here
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_InvalidCombiner() {
        QueryParser parser = new QueryParser("div~invalid:query");
        parser.parse(); // assert exception is thrown here
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_MissingElement() {
        QueryParser parser = new QueryParser(":not(#element) .class");
        Evaluator rootEval = parser.parse();
        Evaluator notEval = ((StructuralEvaluator.Not) rootEval).getChild();
        Assert.assertThat(notEval, instanceOf(Evaluator.Id.class));
        Assert.assertThat(((Evaluator.Id) notEval).id, is("element"));
        parser = new QueryParser(".class");
        Evaluator classEval = parser.parse();
        List<Evaluator> classEvalChildren = ((CombiningEvaluator.And) classEval).getChildren();
        Assert.assertEquals(classEvalChildren.size(), 0);
        CombiningEvaluator.And and = new CombiningEvaluator.And();
        and.add(notEval);
        and.add(classEval);
        Evaluator result = parser.parse();
        Assert.assertThat(result, is(and));
    }
}