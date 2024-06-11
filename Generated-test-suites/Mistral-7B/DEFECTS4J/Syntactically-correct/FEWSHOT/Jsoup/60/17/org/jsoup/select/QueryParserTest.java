package org.jsoup.select;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.helper.StringUtil;
import org.jsoup.parser.TokenQueue;
import org.junit.Test;

import org.jsoup.select.QueryParser;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;
import org.jsoup.select.StructuralEvaluator;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        QueryParser parser = new QueryParser("");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AllElements);
    }

    @Test
    public void testParseTagQuery() {
        QueryParser parser = new QueryParser("div");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("div", evaluator.toString());
    }

    @Test
    public void testParseTagWithNamespace() {
        QueryParser parser = new QueryParser("html:div");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("div", evaluator.toString());
        assertTrue(((Evaluator.Tag) evaluator).isNamespace());
    }

    @Test
    public void testParseClassQuery() {
        QueryParser parser = new QueryParser(".class");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals(".class", evaluator.toString());
    }

    @Test
    public void testParseIdQuery() {
        QueryParser parser = new QueryParser("#id");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals("#id", evaluator.toString());
    }

    @Test
    public void testParseAttributeQuery() {
        QueryParser parser = new QueryParser("[class]");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Attribute);
        assertEquals("[class]", evaluator.toString());
    }

    @Test
    public void testParseAttributeWithValueQuery() {
        QueryParser parser = new QueryParser("[class=value]");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
        assertEquals("[class=value]", evaluator.toString());
    }

    @Test
    public void testParseCombinedQuery() {
        QueryParser parser = new QueryParser("div.class");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof CombiningEvaluator.And);

        Evaluator tagEvaluator = ((CombiningEvaluator.And) evaluator).get(0);
        assertTrue(tagEvaluator instanceof Evaluator.Tag);
        assertEquals("div", tagEvaluator.toString());

        Evaluator classEvaluator = ((CombiningEvaluator.And) evaluator).get(1);
        assertTrue(classEvaluator instanceof Evaluator.Class);
        assertEquals(".class", classEvaluator.toString());
    }

    @Test
    public void testParseCombinedQueryWithAttributeValue() {
        QueryParser parser = new QueryParser("div[class=value]");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof CombiningEvaluator.And);

        Evaluator tagEvaluator = ((CombiningEvaluator.And) evaluator).get(0);
        assertTrue(tagEvaluator instanceof Evaluator.Tag);
        assertEquals("div", tagEvaluator.toString());

        Evaluator attributeEvaluator = ((CombiningEvaluator.And) evaluator).get(1);
        assertTrue(attributeEvaluator instanceof Evaluator.AttributeWithValue);
        assertEquals("[class=value]", attributeEvaluator.toString());
    }

    @Test
    public void testParseCombinedQueryWithMultipleSelectors() {
        QueryParser parser = new QueryParser("div.class1, div.class2");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof CombiningEvaluator.Or);

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Tag("div"));
        evaluators.add(new Evaluator.Class("class1"));
        evaluators.add(new Evaluator.Class("class2"));

        CombiningEvaluator.Or orEvaluator = (CombiningEvaluator.Or) evaluator;
        List<Evaluator> orEvaluators = orEvaluator.getEvaluators();
        assertEquals(evaluators.size(), orEvaluators.size());

        for (int i = 0; i < evaluators.size(); i++) {
            Evaluator evaluator1 = evaluators.get(i);
            Evaluator evaluator2 = orEvaluators.get(i);
            assertEquals(evaluator1, evaluator2);
        }
    }

    @Test
    public void testParsePseudoClassQuery() {
        QueryParser parser = new QueryParser(":first-child");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsFirstChild);
    }

    @Test
    public void testParsePseudoClassQueryWithAttribute() {
        QueryParser parser = new QueryParser(":has(p:containsOwn(text))");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof StructuralEvaluator.Has);
    }

    @Test
    public void testParseComplexQuery() {
        QueryParser parser = new QueryParser("div.class1:not(:has(p:containsOwn(text)))");
        Evaluator evaluator = parser.parse();

        assertTrue(evaluator instanceof CombiningEvaluator.And);

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Tag("div"));
        evaluators.add(new Evaluator.Class("class1"));
        evaluators.add(new StructuralEvaluator.Not(new StructuralEvaluator.Has(new QueryParser("p:containsOwn(text)").parse())));

        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) evaluator;
        List<Evaluator> andEvaluators = andEvaluator.getEvaluators();
        assertEquals(evaluators.size(), andEvaluators.size());

        for (int i = 0; i < evaluators.size(); i++) {
            Evaluator evaluator1 = evaluators.get(i);
            Evaluator evaluator2 = andEvaluators.get(i);
            assertEquals(evaluator1, evaluator2);
        }
    }

    @Test
    public void testParseComplexQueryWithNthChild() {
        QueryParser parser = new QueryParser("li:nth-child(2)");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsNthChild);
        assertEquals("li:nth-child(2)", evaluator.toString());
    }

    @Test
    public void testParseComplexQueryWithNthOfType() {
        QueryParser parser = new QueryParser("li:nth-of-type(2)");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsNthOfType);
        assertEquals("li:nth-of-type(2)", evaluator.toString());
    }

    @Test
    public void testParseComplexQueryWithNthLastChild() {
        QueryParser parser = new QueryParser("li:nth-last-child(2)");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsNthLastChild);
        assertEquals("li:nth-last-child(2)", evaluator.toString());
    }

    @Test
    public void testParseComplexQueryWithNthLastOfType() {
        QueryParser parser = new QueryParser("li:nth-last-of-type(2)");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsNthLastOfType);
        assertEquals("li:nth-last-of-type(2)", evaluator.toString());
    }
}