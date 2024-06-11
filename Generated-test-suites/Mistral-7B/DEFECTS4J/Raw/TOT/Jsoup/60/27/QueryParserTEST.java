import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.helper.StringUtil;
import org.jsoup.parser.QueryParser;
import org.jsoup.parser.Selector;
import org.jsoup.parser.Token;
import org.jsoup.parser.SelectorParser;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.ArrayList;

public class QueryParserTest {

    @Test
    public void testParse_EmptyQuery() {
        QueryParser parser = new QueryParser("");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        // An empty query returns an empty list of evaluators
        assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_SimpleTagSelector() {
        QueryParser parser = new QueryParser("p");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("p"));
        assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_ClassSelector() {
        QueryParser parser = new QueryParser(".className");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Class("className"));
        assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_IdSelector() {
        QueryParser parser = new QueryParser("#id");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Id("id"));
        assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_CombinedSelectors() {
        QueryParser parser = new QueryParser("div p.className");
        Evaluator evaluator = parser.parse();

        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("div"));
        expected.add(new CombiningEvaluator.And(new Evaluator.Tag("p"), new Evaluator.Class("className")));
        assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_AttributeSelector() {
        QueryParser parser = new QueryParser("[href]");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Attribute("href"));
        assertEquals(expected, evaluator.getChildren());

        parser = new QueryParser("[href='test.html']");
        evaluator = parser.parse();
        expected = new ArrayList<>();
        expected.add(new Evaluator.AttributeWithValue("href", "test.html"));
        assertEquals(expected, evaluator.getChildren());

        parser = new QueryParser("[title~='test']");
        evaluator = parser.parse();
        expected = new ArrayList<>();
        expected.add(new Evaluator.AttributeWithValueMatching("title", Pattern.compile("test")));
        assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_NegatedAttributeSelector() {
        QueryParser parser = new QueryParser("[!href]");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new StructuralEvaluator.Not(new Evaluator.Attribute("href")));
        assertEquals(expected, evaluator.getChildren());

        parser = new QueryParser("[!title~='test']");
        evaluator = parser.parse();
        expected = new ArrayList<>();
        expected.add(new StructuralEvaluator.Not(new Evaluator.AttributeWithValueMatching("title", Pattern.compile("test"))));
        assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_CombinatorSelectors() {
        QueryParser parser = new QueryParser("div > p.className");
        Evaluator evaluator = parser.parse();

        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("div"));
        expected.add(new StructuralEvaluator.ImmediateChildOf(new Evaluator.Tag("p")));
        expected.add(new Evaluator.Class("className"));
        assertEquals(expected, evaluator.getChildren());

        parser = new QueryParser("div ~ p.className");
        evaluator = parser.parse();
        expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("div"));
        expected.add(new StructuralEvaluator.PreviousSiblingOrSame(new Evaluator.Tag("p")));
        expected.add(new Evaluator.Class("className"));
        assertEquals(expected, evaluator.getChildren());
    }

    @Test
    public void testParse_PseudoSelectors() {
        QueryParser parser = new QueryParser(":has(a)");
        Evaluator evaluator = parser.parse();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new StructuralEvaluator.Has(new Evaluator.Tag("a")));
        assertEquals(expected, evaluator.getChildren());

        parser = new QueryParser(":contains(text)");
        evaluator = parser.parse();
        expected = new ArrayList<>();
        expected.add(new Evaluator.ContainsText("text"));
        assertEquals(expected, evaluator.getChildren());

        parser = new QueryParser(":containsOwn(text)");
        evaluator = parser.parse();
        expected = new ArrayList<>();
        expected.add(new Evaluator.ContainsOwnText("text"));
        assertEquals(expected, evaluator.getChildren());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_InvalidSelector() {
        QueryParser parser = new QueryParser(":invalidSelector");
        parser.parse(); // should throw Selector.SelectorParseException
    }

    @Test
    public void testParse_ComplexSelector() {
        QueryParser parser = new QueryParser("div.class1.class2 > p.class3 > ul > li#id > a[href^='http']");
        Evaluator evaluator = parser.parse();

        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("div"));
        expected.add(new Evaluator.Class("class1"));
        expected.add(new Evaluator.Class("class2"));
        expected.add(new StructuralEvaluator.ImmediateChildOf(new Evaluator.Tag("p")));
        expected.add(new Evaluator.Class("class3"));
        expected.add(new StructuralEvaluator.ImmediateChildOf(new Evaluator.Tag("ul")));
        expected.add(new Evaluator.Tag("li"));
        expected.add(new Evaluator.Id("id"));
        expected.add(new Evaluator.AttributeWithValueStarting("href", "http"));
        assertEquals(expected, evaluator.getChildren());
    }
}