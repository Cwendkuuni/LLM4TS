import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.jsoup.helper.StringUtil;

public class QueryParserTest {

    @Test
    public void testParse_EmptyQuery() {
        String query = "";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertEquals(0, evaluator.size());
    }

    @Test
    public void testParse_SingleElement() {
        String query = "div";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertEquals(1, evaluator.size());
        Assert.assertTrue(evaluator.get(0) instanceof Evaluator.Tag);
        Assert.assertEquals("div", ((Evaluator.Tag) evaluator.get(0)).getName());
    }

    @Test
    public void testParse_SingleClass() {
        String query = ".button";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertEquals(1, evaluator.size());
        Assert.assertTrue(evaluator.get(0) instanceof Evaluator.Class);
        Assert.assertEquals(".button", evaluator.get(0).toString());
    }

    @Test
    public void testParse_SingleId() {
        String query = "#id1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertEquals(1, evaluator.size());
        Assert.assertTrue(evaluator.get(0) instanceof Evaluator.Id);
        Assert.assertEquals("id1", evaluator.get(0).toString());
    }

    @Test
    public void testParse_CombinedSelectors() {
        String query = "div > p.error";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertEquals(2, evaluator.size());
        Assert.assertTrue(evaluator.get(0) instanceof Evaluator.Tag);
        Assert.assertEquals("div", ((Evaluator.Tag) evaluator.get(0)).getName());
        Assert.assertTrue(evaluator.get(1) instanceof Evaluator.Class);
        Assert.assertEquals(".error", evaluator.get(1).toString());
        Assert.assertTrue(evaluator.get(0) instanceof Evaluator.StructuralEvaluator.ImmediateParent);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_InvalidCombinator() {
        String query = "div % p";
        new QueryParser(query).parse();
    }

    @Test
    public void testParse_ElementWithAttribute() {
        String query = "input[type='text']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertEquals(1, evaluator.size());
        Assert.assertTrue(evaluator.get(0) instanceof Evaluator.Attribute);
        Assert.assertEquals("input[type='text']", evaluator.get(0).toString());
    }

    @Test
    public void testParse_ElementWithIdAndClass() {
        String query = "#id1.class1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertEquals(2, evaluator.size());
        Assert.assertTrue(evaluator.get(0) instanceof Evaluator.Id);
        Assert.assertEquals("#id1", evaluator.get(0).toString());
        Assert.assertTrue(evaluator.get(1) instanceof Evaluator.Class);
        Assert.assertEquals(".class1", evaluator.get(1).toString());
    }

    @Test
    public void testParse_ElementWithMultipleClasses() {
        String query = ".class1.class2";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertEquals(1, evaluator.size());
        Assert.assertTrue(evaluator.get(0) instanceof Evaluator.Class);
        Assert.assertEquals(".class1.class2", evaluator.get(0).toString());
    }

    @Test
    public void testParse_PseudoSelectors_Has() {
        String query = "div:has(p)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        StructuralEvaluator.Has hasEvaluator = (StructuralEvaluator.Has) evaluator.get(0);
        Assert.assertEquals(1, hasEvaluator.getSubSelector().size());
        Assert.assertTrue(hasEvaluator.getSubSelector().get(0) instanceof Evaluator.Tag);
        Assert.assertEquals("p", ((Evaluator.Tag) hasEvaluator.getSubSelector().get(0)).getName());
    }

    @Test
    public void testParse_PseudoSelectors_ContainsText() {
        String query = "div:contains(text)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertEquals(1, evaluator.size());
        Assert.assertTrue(evaluator.get(0) instanceof Evaluator.ContainsText);
        Assert.assertEquals("div:contains(text)", evaluator.get(0).toString());
    }

    @Test
    public void testParse_PseudoSelectors_ContainsOwnText() {
        String query = "div:containsOwn(text)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertEquals(1, evaluator.size());
        Assert.assertTrue(evaluator.get(0) instanceof Evaluator.ContainsOwnText);
        Assert.assertEquals("div:containsOwn(text)", evaluator.get(0).toString());
    }

    @Test
    public void testParse_PseudoSelectors_Matches() {
        String query = "div:matches(.*button.*)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertEquals(1, evaluator.size());
        Assert.assertTrue(evaluator.get(0) instanceof Evaluator.Matches);
        Assert.assertEquals("div:matches(.*button.*)", evaluator.get(0).toString());
    }

    @Test
    public void testParse_PseudoSelectors_MatchesOwn() {
        String query = "div:matchesOwn(.*button.*)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertEquals(1, evaluator.size());
        Assert.assertTrue(evaluator.get(0) instanceof Evaluator.MatchesOwn);
        Assert.assertEquals("div:matchesOwn(.*button.*)", evaluator.get(0).toString());
    }

    @Test
    public void testParse_PseudoSelectors_Not() {
        String query = ":not(.button)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        StructuralEvaluator.Not notEvaluator = (StructuralEvaluator.Not) evaluator.get(0);
        Assert.assertEquals(1, notEvaluator.getSubSelector().size());
        Assert.assertTrue(notEvaluator.getSubSelector().get(0) instanceof Evaluator.Class);
        Assert.assertEquals(".button", notEvaluator.getSubSelector().get(0).toString());
    }
}