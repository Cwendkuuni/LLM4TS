import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.select.QueryParser;
import org.jsoup.Select;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div p";
        Evaluator eval = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.Type.ELEMENT, eval.getType());
        Assert.assertTrue(eval instanceof CombiningEvaluator.And);
        CombiningEvaluator.And and = (CombiningEvaluator.And) eval;
        Assert.assertEquals(2, and.getEvaluators().size());
        Assert.assertTrue(and.getEvaluators().get(0) instanceof Evaluator.Tag);
        Assert.assertEquals("div", ((Evaluator.Tag) and.getEvaluators().get(0)).getName());
        Assert.assertTrue(and.getEvaluators().get(1) instanceof Evaluator.Tag);
        Assert.assertEquals("p", ((Evaluator.Tag) and.getEvaluators().get(1)).getName());

        query = "a[href='#']";
        eval = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.Type.ELEMENT, eval.getType());
        Assert.assertTrue(eval instanceof Evaluator.Attribute);
        Assert.assertEquals("a", ((Evaluator.Attribute) eval).getElementName());
        Assert.assertEquals("href", ((Evaluator.Attribute) eval).getAttributeName());
        Assert.assertEquals("#", ((Evaluator.Attribute) eval).getAttributeValue());

        query = "p#id1";
        eval = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.Type.ELEMENT, eval.getType());
        Assert.assertTrue(eval instanceof Evaluator.Id);
        Assert.assertEquals("p", ((Evaluator.Id) eval).getId());

        query = "input[type='text']";
        eval = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.Type.ELEMENT, eval.getType());
        Assert.assertTrue(eval instanceof Evaluator.Attribute);
        Assert.assertEquals("input", ((Evaluator.Attribute) eval).getElementName());
        Assert.assertEquals("type", ((Evaluator.Attribute) eval).getAttributeName());
        Assert.assertEquals("text", ((Evaluator.Attribute) eval).getAttributeValue());

        query = "div > p";
        eval = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.Type.ELEMENT, eval.getType());
        Assert.assertTrue(eval instanceof CombiningEvaluator.And);
        and = (CombiningEvaluator.And) eval;
        Assert.assertEquals(2, and.getEvaluators().size());
        Assert.assertTrue(and.getEvaluators().get(0) instanceof Evaluator.Tag);
        Assert.assertEquals("div", ((Evaluator.Tag) and.getEvaluators().get(0)).getName());
        Assert.assertTrue(and.getEvaluators().get(1) instanceof Evaluator.Structural);
        Assert.assertTrue(and.getEvaluators().get(1) instanceof StructuralEvaluator.ImmediateChild);
        Assert.assertEquals("p", ((StructuralEvaluator.ImmediateChild) and.getEvaluators().get(1)).getChildElementName());

        query = "p~ul";
        eval = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.Type.ELEMENT, eval.getType());
        Assert.assertTrue(eval instanceof CombiningEvaluator.And);
        and = (CombiningEvaluator.And) eval;
        Assert.assertEquals(2, and.getEvaluators().size());
        Assert.assertTrue(and.getEvaluators().get(0) instanceof Evaluator.Tag);
        Assert.assertEquals("p", ((Evaluator.Tag) and.getEvaluators().get(0)).getName());
        Assert.assertTrue(and.getEvaluators().get(1) instanceof Evaluator.Structural);
        Assert.assertTrue(and.getEvaluators().get(1) instanceof StructuralEvaluator.PreviousSibling);
        Assert.assertEquals("ul", ((StructuralEvaluator.PreviousSibling) and.getEvaluators().get(1)).getPreviousElementName());

        query = "div:has(p:contains(test))";
        eval = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.Type.ELEMENT, eval.getType());
        Assert.assertTrue(eval instanceof StructuralEvaluator.Has);
        StructuralEvaluator.Has has = (StructuralEvaluator.Has) eval;
        Assert.assertEquals(Evaluator.Type.ELEMENT, has.getChildEvaluator().getType());
        Assert.assertTrue(has.getChildEvaluator() instanceof Evaluator.Text);
        Assert.assertEquals("test", ((Evaluator.Text) has.getChildEvaluator()).getText());

        query = "div:not(p)";
        eval = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.Type.ELEMENT, eval.getType());
        Assert.assertTrue(eval instanceof StructuralEvaluator.Not);
        StructuralEvaluator.Not not = (StructuralEvaluator.Not) eval;
        Assert.assertEquals(Evaluator.Type.ELEMENT, not.getChildEvaluator().getType());
        Assert.assertTrue(not.getChildEvaluator() instanceof Evaluator.Tag);
        Assert.assertEquals("p", ((Evaluator.Tag) not.getChildEvaluator()).getName());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        String query = "div p#";
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery2() {
        String query = "div > p#id1";
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery3() {
        String query = "div > p#id1:not(p)";
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery4() {
        String query = "div > p#id1:not(p):has(p:contains(test))";
    }
}