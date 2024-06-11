package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        Evaluator evaluator = QueryParser.parse("");
        assertEquals(Evaluator.class, evaluator.getClass());
        assertTrue(evaluator.isEmpty());
    }

    @Test
    public void testParseSingleTagSelector() {
        Evaluator evaluator = QueryParser.parse("p");
        assertTrue(evaluator.isTag("p"));
    }

    @Test
    public void testParseTagSelectorWithNamespace() {
        Evaluator evaluator = QueryParser.parse("html:body");
        assertTrue(evaluator.isTag("body"));
        assertTrue(evaluator.isTagNamespace("html"));
    }

    @Test
    public void testParseClassSelector() {
        Evaluator evaluator = QueryParser.parse(".class");
        assertTrue(evaluator.isClass("class"));
    }

    @Test
    public void testParseIdSelector() {
        Evaluator evaluator = QueryParser.parse("#id");
        assertTrue(evaluator.isId("id"));
    }

    @Test
    public void testParseAttributeSelector() {
        Evaluator evaluator = QueryParser.parse("[attr]");
        assertTrue(evaluator instanceof Evaluator.Attribute);
    }

    @Test
    public void testParseAttributeSelectorWithValue() {
        Evaluator evaluator = QueryParser.parse("[attr=value]");
        assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
        assertEquals("value", evaluator.getAttributeValue());
    }

    @Test
    public void testParseAttributeSelectorWithValueAndOperator() {
        Evaluator evaluator = QueryParser.parse("[attr~=value]");
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueMatching);
        assertEquals("value", evaluator.getAttributeValue());
        assertEquals(Pattern.compile("value"), evaluator.getMatcher());
    }

    @Test
    public void testParseDescendantSelector() {
        Evaluator parentEvaluator = new Evaluator.Tag("div");
        Evaluator childEvaluator = new Evaluator.Tag("p");

        Evaluator evaluator = new QueryParser("div > p").parse()[0];
        assertEquals(Evaluator.class, evaluator.getClass());
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertEquals(2, ((CombiningEvaluator.And) evaluator).getChildren().size());
        assertEquals(parentEvaluator, ((CombiningEvaluator.And) evaluator).getChildren().get(0));
        assertEquals(childEvaluator, ((CombiningEvaluator.And) evaluator).getChildren().get(1));
    }

    @Test
    public void testParseAdjacentSiblingSelector() {
        Evaluator parentEvaluator = new Evaluator.Tag("div");
        Evaluator siblingEvaluator = new Evaluator.Tag("p");

        Evaluator evaluator = new QueryParser("div + p").parse()[0];
        assertEquals(Evaluator.class, evaluator.getClass());
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertEquals(2, ((CombiningEvaluator.And) evaluator).getChildren().size());
        assertEquals(parentEvaluator, ((CombiningEvaluator.And) evaluator).getChildren().get(0));
        assertEquals(siblingEvaluator, ((CombiningEvaluator.And) evaluator).getChildren().get(1));
    }

    @Test
    public void testParseGeneralSiblingSelector() {
        Evaluator parentEvaluator = new Evaluator.Tag("div");
        Evaluator siblingEvaluator = new Evaluator.Tag("p");

        Evaluator evaluator = new QueryParser("div ~ p").parse()[0];
        assertEquals(Evaluator.class, evaluator.getClass());
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertEquals(2, ((CombiningEvaluator.And) evaluator).getChildren().size());
        assertEquals(parentEvaluator, ((CombiningEvaluator.And) evaluator).getChildren().get(0));
        assertEquals(siblingEvaluator, ((CombiningEvaluator.And) evaluator).getChildren().get(1));
    }

    @Test
    public void testParseChildSelectorWithIndex() {
        Evaluator parentEvaluator = new Evaluator.Tag("div");
        Evaluator childEvaluator = new Evaluator.IsNthChild(1, 1);

        Evaluator evaluator = new QueryParser("div:nth-child(1) > p").parse()[0];
        assertEquals(Evaluator.class, evaluator.getClass());
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertEquals(2, ((CombiningEvaluator.And) evaluator).getChildren().size());
        assertEquals(parentEvaluator, ((CombiningEvaluator.And) evaluator).getChildren().get(0));
        assertEquals(childEvaluator, ((CombiningEvaluator.And) evaluator).getChildren().get(1));
    }

    @Test
    public void testParsePseudoClassSelector() {
        Evaluator evaluator = new QueryParser(":empty").parse()[0];
        assertTrue(evaluator instanceof Evaluator.IsEmpty);
    }

    @Test
    public void testParsePseudoClassSelectorWithArgument() {
        Evaluator evaluator = new QueryParser(":has(p)").parse()[0];
        assertTrue(evaluator instanceof StructuralEvaluator.Has);
        assertEquals(Evaluator.class, evaluator.getChild().getClass());
        assertEquals(Evaluator.Tag.class, evaluator.getChild().getClass());
        assertEquals("p", ((Evaluator.Tag) evaluator.getChild()).getTagName());
    }

    @Test
    public void testParseComplexQuery() {
        Evaluator evaluator = new QueryParser("div > p:first-child.class1:nth-child(2):not(.class2)").parse()[0];

        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertEquals(3, ((CombiningEvaluator.And) evaluator).getChildren().size());

        Evaluator firstChildEvaluator = ((CombiningEvaluator.And) evaluator).getChildren().get(0);
        assertEquals(Evaluator.class, firstChildEvaluator.getClass());
        assertTrue(firstChildEvaluator instanceof StructuralEvaluator.ImmediateChild);
        assertEquals("div", ((StructuralEvaluator.ImmediateChild) firstChildEvaluator).getParentTagName());

        Evaluator secondChildEvaluator = ((CombiningEvaluator.And) evaluator).getChildren().get(1);
        assertEquals(Evaluator.class, secondChildEvaluator.getClass());
        assertTrue(secondChildEvaluator instanceof Evaluator.IsNthChild);
        assertEquals(2, ((Evaluator.IsNthChild) secondChildEvaluator).getIndex());
        assertEquals(1, ((Evaluator.IsNthChild) secondChildEvaluator).getPreviousSiblingIndex());

        Evaluator notClassEvaluator = ((CombiningEvaluator.And) evaluator).getChildren().get(2);
        assertEquals(Evaluator.class, notClassEvaluator.getClass());
        assertTrue(notClassEvaluator instanceof StructuralEvaluator.Not);
        assertEquals(Evaluator.class, notClassEvaluator.getChild().getClass());
        assertTrue(notClassEvaluator.getChild().isClass("class2"));
    }
}