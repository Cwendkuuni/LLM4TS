package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import org.jsoup.select.QueryParser;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        QueryParser parser = new QueryParser("");
        Evaluator evaluator = parser.parse();
        assertNull(evaluator);
    }

    @Test
    public void testParseTagQuery() {
        String query = "div";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertEquals(Evaluator.Tag.class, evaluator.getClass());
        assertEquals("div", evaluator.toString());
    }

    @Test
    public void testParseClassQuery() {
        String query = ".myClass";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertEquals(Evaluator.Class.class, evaluator.getClass());
        assertEquals(".myClass", evaluator.toString());
    }

    @Test
    public void testParseIdQuery() {
        String query = "#myId";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertEquals(Evaluator.Id.class, evaluator.getClass());
        assertEquals("#myId", evaluator.toString());
    }

    @Test
    public void testParseCombinedQuery() {
        String query = "div.myClass";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertEquals(CombiningEvaluator.class, evaluator.getClass());
        Evaluator tagEval = ((CombiningEvaluator) evaluator).getLeftEvaluator();
        assertEquals(Evaluator.Tag.class, tagEval.getClass());
        assertEquals("div", tagEval.toString());
        Evaluator classEval = ((CombiningEvaluator) evaluator).getRightEvaluator();
        assertEquals(Evaluator.Class.class, classEval.getClass());
        assertEquals(".myClass", classEval.toString());
    }

    @Test
    public void testParseNthChildQuery() {
        String query = "div:nth-child(3)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertEquals(Evaluator.IsNthChild.class, evaluator.getClass());
        assertEquals("div:nth-child(3)", evaluator.toString());
    }

    @Test
    public void testParseNthOfTypeQuery() {
        String query = "p:nth-of-type(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertEquals(Evaluator.IsNthOfType.class, evaluator.getClass());
        assertEquals("p:nth-of-type(2)", evaluator.toString());
    }

    @Test
    public void testParseNthLastChildQuery() {
        String query = "div:nth-last-child(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertEquals(Evaluator.IsNthLastChild.class, evaluator.getClass());
        assertEquals("div:nth-last-child(2)", evaluator.toString());
    }

    @Test
    public void testParseNthLastOfTypeQuery() {
        String query = "p:nth-last-of-type(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertEquals(Evaluator.IsNthLastOfType.class, evaluator.getClass());
        assertEquals("p:nth-last-of-type(2)", evaluator.toString());
    }

    @Test
    public void testParseAttributeQuery() {
        String query = "input[type=text]";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertEquals(Evaluator.Attribute.class, evaluator.getClass());
        assertEquals("input[type=text]", evaluator.toString());
    }

    @Test
    public void testParseAttributeWithValueQuery() {
        String query = "input[type=text][value=abc]";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertEquals(CombiningEvaluator.class, evaluator.getClass());
        Evaluator attributeEval = ((CombiningEvaluator) evaluator).getLeftEvaluator();
        assertEquals(Evaluator.Attribute.class, attributeEval.getClass());
        assertEquals("input[type=text]", attributeEval.toString());
        Evaluator valueEval = ((CombiningEvaluator) evaluator).getRightEvaluator();
        assertEquals(Evaluator.Text.class, valueEval.getClass());
        assertEquals("abc", valueEval.toString());
    }

    @Test
    public void testParseAttributeWithValueContainingQuery() {
        String query = "input[type=text][value*=abc]";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertEquals(CombiningEvaluator.class, evaluator.getClass());
        Evaluator attributeEval = ((CombiningEvaluator) evaluator).getLeftEvaluator();
        assertEquals(Evaluator.Attribute.class, attributeEval.getClass());
        assertEquals("input[type=text]", attributeEval.toString());
        Evaluator valueEval = ((CombiningEvaluator) evaluator).getRightEvaluator();
        assertEquals(Evaluator.TextContaining.class, valueEval.getClass());
        assertEquals("abc", valueEval.toString());
    }

    @Test
    public void testParseAttributeWithValueStartingQuery() {
        String query = "input[type=text][value^=abc]";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertEquals(CombiningEvaluator.class, evaluator.getClass());
        Evaluator attributeEval = ((CombiningEvaluator) evaluator).getLeftEvaluator();
        assertEquals(Evaluator.Attribute.class, attributeEval.getClass());
        assertEquals("input[type=text]", attributeEval.toString());
        Evaluator valueEval = ((CombiningEvaluator) evaluator).getRightEvaluator();
        assertEquals(Evaluator.TextStartingWith.class, valueEval.getClass());
        assertEquals("abc", valueEval.toString());
    }

    @Test
    public void testParseAttributeWithValueEndingQuery() {
        String query = "input[type=text][value$=abc]";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertEquals(CombiningEvaluator.class, evaluator.getClass());
        Evaluator attributeEval = ((CombiningEvaluator) evaluator).getLeftEvaluator();
        assertEquals(Evaluator.Attribute.class, attributeEval.getClass());
        assertEquals("input[type=text]", attributeEval.toString());
        Evaluator valueEval = ((CombiningEvaluator) evaluator).getRightEvaluator();
        assertEquals(Evaluator.TextEndingWith.class, valueEval.getClass());
        assertEquals("abc", valueEval.toString());
    }

    @Test
    public void testParseAttributeWithValueMatchingQuery() {
        String query = "input[type=text]:matches(^abc$)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertEquals(Evaluator.Matches.class, evaluator.getClass());
        assertEquals("input[type=text]:matches(^abc$)", evaluator.toString());
    }

    @Test
    public void testParseAttributeWithValueMatchingOwnQuery() {
        String query = "input:containsOwn(abc)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertEquals(Evaluator.ContainsOwnText.class, evaluator.getClass());
        assertEquals("input:containsOwn(abc)", evaluator.toString());
    }

    @Test
    public void testParseAttributeWithValueContainingDataQuery() {
        String query = "input[data-myattr*=abc]";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertEquals(Evaluator.AttributeWithDataValueContaining.class, evaluator.getClass());
        assertEquals("input[data-myattr*=abc]", evaluator.toString());
    }

    @Test
    public void testParsePseudoClassQuery() {
        String query = ":empty";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertEquals(Evaluator.IsEmpty.class, evaluator.getClass());
        assertEquals(":empty", evaluator.toString());
    }

    @Test
    public void testParsePseudoClassOwnQuery() {
        String query = ":hasOwn(p)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertEquals(Evaluator.HasOwnElements.class, evaluator.getClass());
        assertEquals(":hasOwn(p)", evaluator.toString());
    }

    @Test
    public void testParsePseudoClassContainsTextQuery() {
        String query = ":contains(abc)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertEquals(Evaluator.ContainsText.class, evaluator.getClass());
        assertEquals(":contains(abc)", evaluator.toString());
    }

    @Test
    public void testParsePseudoClassContainsTextOwnQuery() {
        String query = ":containsOwn(abc)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertEquals(Evaluator.ContainsOwnText.class, evaluator.getClass());
        assertEquals(":containsOwn(abc)", evaluator.toString());
    }

    @Test
    public void testParsePseudoClassContainsDataQuery() {
        String query = ":containsData(abc)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertEquals(Evaluator.ContainsData.class, evaluator.getClass());
        assertEquals(":containsData(abc)", evaluator.toString());
    }

    @Test
    public void testParsePseudoClassMatchesQuery() {
        String query = ":matches(abc)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertEquals(Evaluator.Matches.class, evaluator.getClass());
        assertEquals(":matches(abc)", evaluator.toString());
    }

    @Test
    public void testParsePseudoClassMatchesOwnQuery() {
        String query = ":matchesOwn(abc)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertEquals(Evaluator.MatchesOwn.class, evaluator.getClass());
        assertEquals(":matchesOwn(abc)", evaluator.toString());
    }

    @Test
    public void testParsePseudoClassNotQuery() {
        String query = ":not(p)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNotNull(evaluator);
        assertEquals(Evaluator.Not.class, evaluator.getClass());
        assertEquals(":not(p)", evaluator.toString());
    }
}