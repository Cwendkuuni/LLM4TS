package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.jsoup.parser.TokenQueue;

public class QueryParserTest {

    @Test
    public void testParse_EmptyQuery() {
        QueryParser queryParser = new QueryParser("");
        Evaluator evaluator = queryParser.parse();
        assertNull(evaluator);
    }

    @Test
    public void testParse_TagSelector() {
        String query = "div";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertSame(evaluator, new Evaluator.Tag("div"));
    }

    @Test
    public void testParse_ClassSelector() {
        String query = ".button";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        assertTrue(evaluator instanceof Evaluator.Class);
        assertSame(evaluator, new Evaluator.Class("button"));
    }

    @Test
    public void testParse_IdSelector() {
        String query = "#id1";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        assertTrue(evaluator instanceof Evaluator.Id);
        assertSame(evaluator, new Evaluator.Id("id1"));
    }

    @Test
    public void testParse_SimpleAttributeSelector() {
        String query = "[href]";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        assertTrue(evaluator instanceof Evaluator.Attribute);
        assertSame(evaluator, new Evaluator.Attribute("href"));
    }

    @Test
    public void testParse_AttributeWithValueSelector() {
        String query = "[href='https://www.example.com']";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
        assertSame(evaluator, new Evaluator.AttributeWithValue("href", "https://www.example.com"));
    }

    @Test
    public void testParse_AttributeWithValueMatchingSelector() {
        String query = "[title~='example']";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueMatching);
        assertEquals(evaluator.getClass(), Evaluator.AttributeWithValueMatching.class);
        assertSame(evaluator, new Evaluator.AttributeWithValueMatching("[title~='example']", Pattern.compile("example")));
    }

    @Test
    public void testParse_CombinatorSelectors() {
        String query = "div > p";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertSame(evaluator, new CombiningEvaluator.And(Arrays.asList(new Evaluator.Tag("div"), new StructuralEvaluator.ImmediateChild())));

        query = "div ~ p";
        queryParser = new QueryParser(query);
        evaluator = queryParser.parse();
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertSame(evaluator, new CombiningEvaluator.And(Arrays.asList(new Evaluator.Tag("div"), new StructuralEvaluator.PreviousSibling())));

        query = "div + p";
        queryParser = new QueryParser(query);
        evaluator = queryParser.parse();
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertSame(evaluator, new CombiningEvaluator.And(Arrays.asList(new Evaluator.Tag("div"), new StructuralEvaluator.ImmediatePreviousSibling())));
    }

    @Test
    public void testParse_PseudoClassSelectors() {
        String query = ":first-child";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        assertTrue(evaluator instanceof Evaluator.PseudoClass);
        assertSame(evaluator, new Evaluator.PseudoClass(":first-child"));

        query = ":has(p)";
        queryParser = new QueryParser(query);
        evaluator = queryParser.parse();
        assertTrue(evaluator instanceof StructuralEvaluator.Has);
        assertSame(evaluator, new StructuralEvaluator.Has(new Evaluator.Tag("p")));
    }

    @Test
    public void testParse_ComplexQuery() {
        String query = "#header > nav > ul > li > a[href^='/news/']";
        QueryParser queryParser = new QueryParser(query);
        Evaluator evaluator = queryParser.parse();
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        List<Evaluator> list = ((CombiningEvaluator.And) evaluator).getChildren();
        assertEquals(list.size(), 5);
        assertTrue(list.get(0) instanceof Evaluator.Id);
        assertTrue(list.get(1) instanceof StructuralEvaluator.ImmediateChild);
        assertTrue(list.get(2) instanceof Evaluator.Tag);
        assertTrue(list.get(3) instanceof Evaluator.Tag);
        assertTrue(list.get(4) instanceof Evaluator.AttributeWithValueStarting);
    }
}