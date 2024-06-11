package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import org.jsoup.select.QueryParser.CombiningEvaluator;
import org.jsoup.select.QueryParser.Evaluator;
import org.jsoup.select.QueryParser.StructuralEvaluator;

public class QueryParserTest {

    @Test
    public void testParseSimpleQuery() {
        Evaluator parserResult = QueryParser.parse("div");
        Evaluator expectedResult = new Evaluator.Tag("div");
        assertEquals(expectedResult, parserResult);
    }

    @Test
    public void testParseAttributeQuery() {
        Evaluator parserResult = QueryParser.parse("input[type=text]");
        Evaluator expectedResult = new Evaluator.Attribute(new String[]{"type"}, "text");
        assertEquals(expectedResult, parserResult);
    }

    @Test
    public void testParseClassQuery() {
        Evaluator parserResult = QueryParser.parse(".myClass");
        Evaluator expectedResult = new Evaluator.Class("myClass");
        assertEquals(expectedResult, parserResult);
    }

    @Test
    public void testParseIdQuery() {
        Evaluator parserResult = QueryParser.parse("#myId");
        Evaluator expectedResult = new Evaluator.Id("myId");
        assertEquals(expectedResult, parserResult);
    }

    @Test
    public void testParseTagAndClassQuery() {
        Evaluator parserResult = QueryParser.parse("div.myClass");
        Evaluator expectedResult = new CombiningEvaluator.And(new Evaluator.Tag("div"), new Evaluator.Class("myClass"));
        assertEquals(expectedResult, parserResult);
    }

    @Test
    public void testParseTagAndIdQuery() {
        Evaluator parserResult = QueryParser.parse("div#myId");
        Evaluator expectedResult = new StructuralEvaluator.HasChildWithId("div", "myId");
        assertEquals(expectedResult, parserResult);
    }

    @Test
    public void testParseTagAndAttributeQuery() {
        Evaluator parserResult = QueryParser.parse("input[type=text][value=foo]");
        Evaluator expectedResult = new CombiningEvaluator.And(new Evaluator.Attribute(new String[]{"type"}, "text"), new Evaluator.AttributeWithValue("value", "foo"));
        assertEquals(expectedResult, parserResult);
    }

    @Test
    public void testParseComplexQuery() {
        Evaluator parserResult = QueryParser.parse("div.myClass:not(div.anotherClass):contains(text)");

        Evaluator expectedResult = new CombiningEvaluator.And(
                new CombiningEvaluator.And(new Evaluator.Tag("div"), new Evaluator.Class("myClass")),
                new StructuralEvaluator.Not(new CombiningEvaluator.And(new Evaluator.Tag("div"), new Evaluator.Class("anotherClass")))
        );

        Evaluator containsTextEvaluator = new Evaluator.ContainsText("text");
        Evaluator expectedContainsTextChild = new StructuralEvaluator.HasDescendant(containsTextEvaluator);

        Evaluator expectedResultWithContainsText = new CombiningEvaluator.And(expectedResult, expectedContainsTextChild);

        assertEquals(expectedResultWithContainsText, parserResult);
    }
}