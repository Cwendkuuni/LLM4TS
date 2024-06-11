package org.jsoup.select;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.StringUtil;
import org.jsoup.parser.TokenQueue;

import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        QueryParser parser = new QueryParser("");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AllElements);
    }

    @Test
    public void testParseSingleTagQuery() {
        QueryParser parser = new QueryParser("div");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.class, evaluator.getClass());
        Evaluator.Tag tag = (Evaluator.Tag) evaluator;
        assertEquals("div", tag.selector());
    }

    @Test
    public void testParseSingleIdQuery() {
        QueryParser parser = new QueryParser("#myId");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.Id.class, evaluator.getClass());
        Evaluator.Id id = (Evaluator.Id) evaluator;
        assertEquals("myId", id.id());
    }

    @Test
    public void testParseSingleClassQuery() {
        QueryParser parser = new QueryParser(".myClass");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.Class.class, evaluator.getClass());
        Evaluator.Class classEvaluator = (Evaluator.Class) evaluator;
        assertEquals("myClass", classEvaluator.className());
    }

    @Test
    public void testParseCombinedQuery() {
        QueryParser parser = new QueryParser("div.myClass");
        Evaluator evaluator = parser.parse();
        Evaluator.Tag tag = (Evaluator.Tag) evaluator;
        assertEquals("div", tag.selector());
        Evaluator andEvaluator = (Evaluator.And) evaluator.getNext();
        Evaluator.Class classEvaluator = (Evaluator.Class) andEvaluator.getNext();
        assertEquals("myClass", classEvaluator.className());
    }

    @Test
    public void testParseAttributeQuery() {
        QueryParser parser = new QueryParser("[href='http://example.com']");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.Attribute.class, evaluator.getClass());
        Evaluator.Attribute attributeEvaluator = (Evaluator.Attribute) evaluator;
        assertEquals("href", attributeEvaluator.attribute());
        assertEquals("http://example.com", attributeEvaluator.value());
    }

    @Test
    public void testParseAttributeQueryWithOperator() {
        QueryParser parser = new QueryParser("[title!='Home']");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.AttributeWithValueNot.class, evaluator.getClass());
        Evaluator.AttributeWithValueNot notEvaluator = (Evaluator.AttributeWithValueNot) evaluator;
        assertEquals("title", notEvaluator.attribute());
        assertNull(notEvaluator.value());
    }

    @Test
    public void testParseComplexQuery() {
        QueryParser parser = new QueryParser("div.myClass:contains('text') > ul > li:nth-child(3)");
        Evaluator evaluator = parser.parse();

        Evaluator.Tag tag = (Evaluator.Tag) evaluator;
        assertEquals("div", tag.selector());

        Evaluator.And andEvaluator = (Evaluator.And) tag.getNext();
        Evaluator.Class classEvaluator = (Evaluator.Class) andEvaluator.getNext();
        assertEquals("myClass", classEvaluator.className());

        Evaluator.Combinator combinator = andEvaluator.getNext();
        assertEquals(">", combinator.operator());

        Evaluator childEvaluator = combinator.next();
        Evaluator.Tag childTag = (Evaluator.Tag) childEvaluator;
        assertEquals("ul", childTag.selector());

        Evaluator.Combinator combinator2 = childEvaluator.getNext();
        assertEquals(">", combinator2.operator());

        Evaluator liEvaluator = combinator2.next();
        Evaluator.Index indexEvaluator = (Evaluator.Index) liEvaluator;
        assertEquals(3, indexEvaluator.index());

        Evaluator lastEvaluator = liEvaluator.getNext();
        assertNull(lastEvaluator);
    }
}