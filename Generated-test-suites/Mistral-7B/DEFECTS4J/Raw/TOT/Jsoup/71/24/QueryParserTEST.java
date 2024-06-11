import org.junit.Test;
import org.junit.Assert.*;
import static org.jsoup.parser.QueryParser.*;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        Evaluator evaluator = parse("");
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertTrue(evaluator.toString().isEmpty());
    }

    @Test
    public void testParseSimpleTagQuery() {
        Evaluator evaluator = parse("div");
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("div", evaluator.toString());
    }

    @Test
    public void testParseClassQuery() {
        Evaluator evaluator = parse(".class1");
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals("class1", evaluator.toString());
    }

    @Test
    public void testParseIDQuery() {
        Evaluator evaluator = parse("#id1");
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals("id1", evaluator.toString());
    }

    @Test
    public void testParseCombinedQuery() {
        Evaluator evaluator = parse("div.class1");
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertEquals(2, evaluator.children().size());
        Evaluator tagEvaluator = evaluator.children().get(0);
        assertTrue(tagEvaluator instanceof Evaluator.Tag);
        assertEquals("div", tagEvaluator.toString());
        Evaluator classEvaluator = evaluator.children().get(1);
        assertTrue(classEvaluator instanceof Evaluator.Class);
        assertEquals("class1", classEvaluator.toString());
    }

    @Test
    public void testParseAttributeQuery() {
        Evaluator evaluator = parse("input[type=text]");
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertEquals(2, evaluator.children().size());
        Evaluator tagEvaluator = evaluator.children().get(0);
        assertTrue(tagEvaluator instanceof Evaluator.Tag);
        assertEquals("input", tagEvaluator.toString());
        Evaluator attributeEvaluator = evaluator.children().get(1);
        assertTrue(attributeEvaluator instanceof Evaluator.Attribute);
        assertEquals("type", attributeEvaluator.toString());
        assertTrue(attributeEvaluator.getValue().equals("text"));
    }

    @Test
    public void testParsePseudoClassQuery() {
        Evaluator evaluator = parse("a:link");
        assertTrue(evaluator instanceof Evaluator.PseudoClass);
        assertEquals("a:link", evaluator.toString());
    }

    @Test
    public void testParsePseudoElementQuery() {
        Evaluator evaluator = parse("ul > li:first-child ::before");
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertEquals(3, evaluator.children().size());
        Evaluator structuralEvaluator = evaluator.children().get(0);
        assertTrue(structuralEvaluator instanceof StructuralEvaluator.ImmediateChild);
        Evaluator pseudoElementEvaluator = evaluator.children().get(1);
        assertTrue(pseudoElementEvaluator instanceof Evaluator.PseudoElement);
        assertEquals("::before", pseudoElementEvaluator.toString());
        Evaluator tagEvaluator = evaluator.children().get(2);
        assertTrue(tagEvaluator instanceof Evaluator.Tag);
        assertEquals("li", tagEvaluator.toString());
    }

    @Test
    public void testParseComplexQuery() {
        Evaluator evaluator = parse("input:not([type=radio]):checked + label");
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertEquals(3, evaluator.children().size());
        Evaluator notEvaluator = evaluator.children().get(0);
        assertTrue(notEvaluator instanceof StructuralEvaluator.Not);
        Evaluator attributeEvaluator = notEvaluator.children().get(0);
        assertTrue(attributeEvaluator instanceof Evaluator.Attribute);
        assertEquals("type", attributeEvaluator.toString());
        assertTrue(attributeEvaluator.getValue().equals("radio"));
        Evaluator checkedEvaluator = evaluator.children().get(1);
        assertTrue(checkedEvaluator instanceof Evaluator.PseudoClass);
        assertEquals(":checked", checkedEvaluator.toString());
        Evaluator structuralEvaluator = evaluator.children().get(2);
        assertTrue(structuralEvaluator instanceof StructuralEvaluator.AdjacentSibling);
        Evaluator tagEvaluator = structuralEvaluator.children().get(0);
        assertTrue(tagEvaluator instanceof Evaluator.Tag);
        assertEquals("label", tagEvaluator.toString());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        parse("div[type=text]"); // type should be tag name, not attribute name
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQueryWithInvalidToken() {
        parse("div#id[type=text]"); // type should be tag name, not attribute value
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQueryWithUnbalancedParentheses() {
        parse("(div#id[type=text])"); // unbalanced opening parenthesis
    }
}