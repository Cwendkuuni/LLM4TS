package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import org.jsoup.helper.Validate;
import org.jsoup.parser.TokenQueue;
import org.junit.Test;

public class QueryParserTest {
    @Test
    public void testParse() {
        String query = "div > p + a";
        Evaluator evaluator = QueryParser.parse(query);

        assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) evaluator;

        assertEquals(3, andEvaluator.evals.size());

        assertEquals(CombiningEvaluator.ImmediateParent.class, andEvaluator.evals.get(0).getClass());
        assertEquals(Evaluator.Tag.class, andEvaluator.evals.get(1).getClass());
        assertEquals(CombiningEvaluator.ImmediatePreviousSibling.class, andEvaluator.evals.get(2).getClass());

        CombiningEvaluator.ImmediateParent immediateParent = (CombiningEvaluator.ImmediateParent) andEvaluator.evals.get(0);
        Evaluator.Tag tagEvaluator = (Evaluator.Tag) andEvaluator.evals.get(1);
        CombiningEvaluator.ImmediatePreviousSibling immediatePreviousSibling = (CombiningEvaluator.ImmediatePreviousSibling) andEvaluator.evals.get(2);

        assertEquals(CombiningEvaluator.And.class, immediateParent.e.getClass());
        assertEquals(StructuralEvaluator.Parent.class, ((CombiningEvaluator) immediateParent.e).evals.get(0).getClass());

        assertEquals("div", tagEvaluator.tagName);

        assertEquals(CombiningEvaluator.And.class, immediatePreviousSibling.e.getClass());
        assertEquals(StructuralEvaluator.ImmediatePreviousSibling.class, ((CombiningEvaluator) immediatePreviousSibling.e).evals.get(0).getClass());

        query = "div, p, a";
        evaluator = QueryParser.parse(query);

        assertEquals(CombiningEvaluator.Or.class, evaluator.getClass());
        CombiningEvaluator.Or orEvaluator = (CombiningEvaluator.Or) evaluator;

        assertEquals(3, orEvaluator.evals.size());

        assertEquals(Evaluator.Tag.class, orEvaluator.evals.get(0).getClass());
        assertEquals(Evaluator.Tag.class, orEvaluator.evals.get(1).getClass());
        assertEquals(Evaluator.Tag.class, orEvaluator.evals.get(2).getClass());

        Evaluator.Tag firstTagEvaluator = (Evaluator.Tag) orEvaluator.evals.get(0);
        Evaluator.Tag secondTagEvaluator = (Evaluator.Tag) orEvaluator.evals.get(1);
        Evaluator.Tag thirdTagEvaluator = (Evaluator.Tag) orEvaluator.evals.get(2);

        assertEquals("div", firstTagEvaluator.tagName);
        assertEquals("p", secondTagEvaluator.tagName);
        assertEquals("a", thirdTagEvaluator.tagName);
    }

    @Test
    public void testCombinator() {
        QueryParser queryParser = new QueryParser("div > p + a");
        
        queryParser.evals.add(new StructuralEvaluator.Parent(new Evaluator.Tag("div")));
        queryParser.evals.add(new Evaluator.Tag("p"));
        queryParser.evals.add(new StructuralEvaluator.ImmediatePreviousSibling(new Evaluator.Tag("a")));

        queryParser.combinator('>');

        assertEquals(1, queryParser.evals.size());
        assertEquals(CombiningEvaluator.And.class, queryParser.evals.get(0).getClass());

        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) queryParser.evals.get(0);
        assertEquals(2, andEvaluator.evals.size());

        assertEquals(StructuralEvaluator.Parent.class, andEvaluator.evals.get(0).getClass());
        assertEquals(Evaluator.Tag.class, andEvaluator.evals.get(1).getClass());

        assertEquals("div", ((Evaluator.Tag) andEvaluator.evals.get(0)).tagName);
        assertEquals("p", ((Evaluator.Tag) andEvaluator.evals.get(1)).tagName);
    }

    @Test
    public void testConsumeSubQuery() {
        QueryParser queryParser = new QueryParser("div > p");
        queryParser.tq = new TokenQueue(" > p");

        assertEquals("div", queryParser.consumeSubQuery());
        assertTrue(queryParser.tq.isEmpty());
    }

    @Test
    public void testFindElements() {
        QueryParser queryParser = new QueryParser("div");
        
        queryParser.findElements();

        assertEquals(1, queryParser.evals.size());
        assertEquals(Evaluator.Tag.class, queryParser.evals.get(0).getClass());

        Evaluator.Tag tagEvaluator = (Evaluator.Tag) queryParser.evals.get(0);
        assertEquals("div", tagEvaluator.tagName);

        queryParser = new QueryParser("#id");
        
        queryParser.findElements();

        assertEquals(1, queryParser.evals.size());
        assertEquals(Evaluator.Id.class, queryParser.evals.get(0).getClass());

        Evaluator.Id idEvaluator = (Evaluator.Id) queryParser.evals.get(0);
        assertEquals("id", idEvaluator.id);

        queryParser = new QueryParser(".class");
        
        queryParser.findElements();

        assertEquals(1, queryParser.evals.size());
        assertEquals(Evaluator.Class.class, queryParser.evals.get(0).getClass());

        Evaluator.Class classEvaluator = (Evaluator.Class) queryParser.evals.get(0);
        assertEquals("class", classEvaluator.className);

        queryParser = new QueryParser("[attribute]");
        
        queryParser.findElements();

        assertEquals(1, queryParser.evals.size());
        assertEquals(Evaluator.Attribute.class, queryParser.evals.get(0).getClass());

        Evaluator.Attribute attributeEvaluator = (Evaluator.Attribute) queryParser.evals.get(0);
        assertEquals("attribute", attributeEvaluator.key);

        queryParser = new QueryParser("[attribute=value]");
        
        queryParser.findElements();

        assertEquals(1, queryParser.evals.size());
        assertEquals(Evaluator.AttributeWithValue.class, queryParser.evals.get(0).getClass());

        Evaluator.AttributeWithValue attributeWithValueEvaluator = (Evaluator.AttributeWithValue) queryParser.evals.get(0);
        assertEquals("attribute", attributeWithValueEvaluator.key);
        assertEquals("value", attributeWithValueEvaluator.value);

        queryParser = new QueryParser(":has(selector)");
        
        queryParser.findElements();

        assertEquals(1, queryParser.evals.size());
        assertEquals(StructuralEvaluator.Has.class, queryParser.evals.get(0).getClass());

        StructuralEvaluator.Has hasEvaluator = (StructuralEvaluator.Has) queryParser.evals.get(0);
        assertEquals(CombiningEvaluator.And.class, hasEvaluator.e.getClass());

        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) hasEvaluator.e;
        assertEquals(1, andEvaluator.evals.size());
        assertEquals(Evaluator.Tag.class, andEvaluator.evals.get(0).getClass());

        Evaluator.Tag tagEvaluator = (Evaluator.Tag) andEvaluator.evals.get(0);
        assertEquals("selector", tagEvaluator.tagName);
    }
}