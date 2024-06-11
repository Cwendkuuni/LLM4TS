import static org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.StructuralEvaluator;
import org.jsoup.select.Evaluator;
import org.jsoup.helper.StringUtil;
import org.junit.Test;

import java.util.List;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertEquals(StructuralEvaluator.Tag.DIV, evaluator.getClass());
        evaluator = ((StructuralEvaluator) evaluator).getLeft();
        assertEquals(StructuralEvaluator.Tag.P, evaluator.getClass());
    }

    @Test
    public void testParseWithCombinator() {
        String query = "div > p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertEquals(StructuralEvaluator.Tag.DIV, evaluator.getClass());
        evaluator = ((StructuralEvaluator) evaluator).getLeft();
        Evaluator leftChild = ((StructuralEvaluator) evaluator).getLeft();
        assertEquals(StructuralEvaluator.ImmediateParent.class, leftChild.getClass());
        assertEquals(StructuralEvaluator.Tag.P, leftChild.getRight().getClass());
    }

    @Test
    public void testParseWithAttribute() {
        String query = "input[type='text']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertEquals(Evaluator.Attribute.class, evaluator.getClass());
        assertEquals(Evaluator.AttributeWithValue.class, evaluator.getClass().getSuperclass());
        Evaluator attributeEvaluator = ((Evaluator.AttributeWithValue) evaluator).getValue();
        assertEquals("text", attributeEvaluator.getValue());
        assertEquals("type", ((Evaluator.Attribute) evaluator).getAttributeName());
    }

    @Test
    public void testParseWithComplexQuery() {
        String query = "div.class1 > p:containsOwn(text)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertEquals(StructuralEvaluator.Tag.DIV, evaluator.getClass());
        evaluator = ((StructuralEvaluator) evaluator).getLeft();
        assertEquals(StructuralEvaluator.Class.class, evaluator.getClass().getSuperclass());
        assertEquals("class1", ((StructuralEvaluator.Class) evaluator).getClassValue());

        evaluator = ((StructuralEvaluator) evaluator).getRight();
        assertEquals(StructuralEvaluator.Tag.P, evaluator.getClass());
        evaluator = ((StructuralEvaluator) evaluator).getLeft();
        assertEquals(Evaluator.ContainsOwnText.class, evaluator.getClass());
        assertEquals("text", evaluator.getRight().getValue());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseWithInvalidQuery() {
        String query = "div:not(p) > p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        fail("Expected Selector.SelectorParseException");
    }

    @Test
    public void testConsumeSubQuery() {
        String query = "(#id1) > p";
        QueryParser parser = new QueryParser(query);
        String subQuery = parser.consumeSubQuery();

        assertEquals("#id1", subQuery);
    }

    @Test
    public void testFindElements() {
        String query = "#id1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertEquals(Evaluator.Id.class, evaluator.getClass());
        assertEquals("id1", evaluator.getValue());
    }

    @Test
    public void testFindElementsWithClass() {
        String query = ".class1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertEquals(Evaluator.Class.class, evaluator.getClass());
        assertEquals("class1", evaluator.getValue());
    }

    @Test
    public void testFindElementsWithTag() {
        String query = "p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertEquals(Evaluator.Tag.class, evaluator.getClass());
        assertEquals("p", evaluator.getValue());
    }

    @Test
    public void testFindElementsWithAttribute() {
        String query = "input[type='text']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertEquals(Evaluator.Attribute.class, evaluator.getClass());
        assertEquals("type", ((Evaluator.Attribute) evaluator).getAttributeName());
        Evaluator attributeEvaluator = ((Evaluator.AttributeWithValue) evaluator).getValue();
        assertEquals("text", attributeEvaluator.getValue());
    }

    @Test
    public void testFindElementsWithAllElements() {
        String query = "*";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertEquals(Evaluator.AllElements.class, evaluator.getClass());
    }

    @Test
    public void testFindElementsWithIndexLessThan() {
        String query = "li:lt(3)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertEquals(Evaluator.IndexLessThan.class, evaluator.getClass());
        assertEquals(3, ((Evaluator.IndexLessThan) evaluator).getIndex());
    }

    @Test
    public void testFindElementsWithIndexGreaterThan() {
        String query = "li:gt(3)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertEquals(Evaluator.IndexGreaterThan.class, evaluator.getClass());
        assertEquals(3, ((Evaluator.IndexGreaterThan) evaluator).getIndex());
    }

    @Test
    public void testFindElementsWithIndexEquals() {
        String query = "li:eq(3)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertEquals(Evaluator.IndexEquals.class, evaluator.getClass());
        assertEquals(3, ((Evaluator.IndexEquals) evaluator).getIndex());
    }

    @Test
    public void testFindElementsWithHas() {
        String query = "li:has(a)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertEquals(StructuralEvaluator.Has.class, evaluator.getClass());
    }

    @Test
    public void testFindElementsWithContains() {
        String query = "li:contains(text)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertEquals(Evaluator.ContainsText.class, evaluator.getClass());
        assertEquals("text", evaluator.getRight().getValue());
    }

    @Test
    public void testFindElementsWithContainsOwn() {
        String query = "li:containsOwn(text)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertEquals(Evaluator.ContainsOwnText.class, evaluator.getClass());
        assertEquals("text", evaluator.getRight().getValue());
    }

    @Test
    public void testFindElementsWithMatches() {
        String query = "li:matches(regex)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertEquals(Evaluator.Matches.class, evaluator.getClass());
    }

    @Test
    public void testFindElementsWithMatchesOwn() {
        String query = "li:matchesOwn(regex)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertEquals(Evaluator.MatchesOwn.class, evaluator.getClass());
    }

    @Test
    public void testFindElementsWithNot() {
        String query = "li:not(a)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertEquals(StructuralEvaluator.Not.class, evaluator.getClass());
    }
}