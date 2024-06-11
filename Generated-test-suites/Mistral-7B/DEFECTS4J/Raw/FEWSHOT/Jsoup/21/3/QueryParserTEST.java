package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;
import org.jsoup.helper.StringUtil;
import org.jsoup.parser.TokenQueue;

/**
 * Test cases for the QueryParser class.
 */
public class QueryParserTest {

    @Test
    public void testParseSimpleSelectors() {
        String query = "div";
        Evaluator eval = QueryParser.parse(query);
        Evaluator expected = new Evaluator.Tag("div");
        assertEquals(expected, eval);

        query = "div#id";
        eval = QueryParser.parse(query);
        expected = new Evaluator.Id("id");
        Evaluator tag = new Evaluator.Tag("div");
        expected = new Evaluator.CombiningEvaluator.And(tag, expected);
        assertEquals(expected, eval);

        query = "div.class";
        eval = QueryParser.parse(query);
        expected = new Evaluator.Class("class");
        Evaluator tag2 = new Evaluator.Tag("div");
        expected = new Evaluator.CombiningEvaluator.And(tag2, expected);
        assertEquals(expected, eval);

        query = "div.class.class2";
        eval = QueryParser.parse(query);
        List<Evaluator> expectedList = new ArrayList<>();
        expectedList.add(new Evaluator.Tag("div"));
        expectedList.add(new Evaluator.Class("class"));
        expectedList.add(new Evaluator.Class("class2"));
        CombiningEvaluator and = new CombiningEvaluator.And(expectedList);
        assertEquals(and, eval);
    }

    @Test
    public void testParseDescendantSelectors() {
        String query = "div > p";
        Evaluator eval = QueryParser.parse(query);
        Evaluator parent = new Evaluator.Tag("div");
        Evaluator child = new Evaluator.Tag("p");
        Evaluator expected = new CombiningEvaluator.And(parent, new StructuralEvaluator.ImmediateChild(child));
        assertEquals(expected, eval);

        query = "div > p > span";
        eval = QueryParser.parse(query);
        expected = new CombiningEvaluator.And(parent, new StructuralEvaluator.Child(child), new Evaluator.Tag("span"));
        assertEquals(expected, eval);
    }

    @Test
    public void testParseAttributeSelectors() {
        String query = "div[class='class']";
        Evaluator eval = QueryParser.parse(query);
        Evaluator tag = new Evaluator.Tag("div");
        AttributeEvaluator attr = new AttributeEvaluator.Attribute("class", "class");
        CombiningEvaluator and = new CombiningEvaluator.And(tag, attr);
        assertEquals(and, eval);

        query = "div[class='class' i='1']";
        eval = QueryParser.parse(query);
        tag = new Evaluator.Tag("div");
        List<AttributeEvaluator> attrs = new ArrayList<>();
        attrs.add(new AttributeEvaluator.Attribute("class", "class"));
        attrs.add(new AttributeEvaluator.Attribute("i", "1"));
        CombiningEvaluator and2 = new CombiningEvaluator.And(tag, new Evaluator.CombiningEvaluator.And(attrs));
        assertEquals(and2, eval);

        query = "div[class='class'][i]";
        eval = QueryParser.parse(query);
        tag = new Evaluator.Tag("div");
        AttributeEvaluator attr2 = new AttributeEvaluator.Attribute("class", "class");
        CombiningEvaluator and3 = new CombiningEvaluator.And(tag, attr2);
        List<AttributeEvaluator> attrs2 = new ArrayList<>();
        attrs2.add(new AttributeEvaluator.AttributeWithName("i"));
        CombiningEvaluator or = new CombiningEvaluator.Or(attrs2);
        CombiningEvaluator and4 = new CombiningEvaluator.And(and3, or);
        assertEquals(and4, eval);
    }

    @Test
    public void testParsePseudoClassSelectors() {
        String query = "div:not(p)";
        Evaluator eval = QueryParser.parse(query);
        Evaluator tag = new Evaluator.Tag("div");
        Evaluator not = new StructuralEvaluator.Not(new Evaluator.Tag("p"));
        assertEquals(new CombiningEvaluator.And(tag, not), eval);

        query = "div:has(p)";
        eval = QueryParser.parse(query);
        Evaluator child = new Evaluator.Tag("p");
        Evaluator has = new StructuralEvaluator.Has(child);
        assertEquals(has, eval);

        query = "div:contains(text)";
        eval = QueryParser.parse(query);
        String text = "text";
        Evaluator contains = new Evaluator.ContainsText(text);
        assertEquals(contains, eval);

        query = "div:containsOwn(text)";
        eval = QueryParser.parse(query);
        Evaluator containsOwn = new Evaluator.ContainsOwnText(text);
        assertEquals(containsOwn, eval);

        query = "div:matches(regex)";
        eval = QueryParser.parse(query);
        String regex = "regex";
        Evaluator matches = new Evaluator.Matches(Pattern.compile(regex));
        assertEquals(matches, eval);

        query = "div:matchesOwn(regex)";
        eval = QueryParser.parse(query);
        Evaluator matchesOwn = new Evaluator.MatchesOwn(Pattern.compile(regex));
        assertEquals(matchesOwn, eval);
    }

    @Test
    public void testParseComplexSelectors() {
        String query = "div.class1 > div.class2 > div.class3:not(div.class4)";
        Evaluator eval = QueryParser.parse(query);
        Evaluator tag = new Evaluator.Tag("div");
        List<Evaluator> classList1 = new ArrayList<>();
        classList1.add(new Evaluator.Class("class1"));
        Evaluator tag2 = new Evaluator.Tag("div");
        classList1.add(tag2);
        List<Evaluator> classList2 = new ArrayList<>();
        classList2.add(new Evaluator.Class("class2"));
        Evaluator tag3 = new Evaluator.Tag("div");
        classList2.add(tag3);
        List<Evaluator> classList3 = new ArrayList<>();
        classList3.add(new Evaluator.Class("class3"));
        Evaluator tag4 = new Evaluator.Tag("div");
        classList3.add(tag4);
        List<Evaluator> classList4 = new ArrayList<>();
        classList4.add(new Evaluator.Class("class4"));
        Evaluator not = new StructuralEvaluator.Not(classList4);
        CombiningEvaluator and1 = new CombiningEvaluator.And(classList1);
        CombiningEvaluator and2 = new CombiningEvaluator.And(tag2, classList2);
        CombiningEvaluator and3 = new CombiningEvaluator.And(tag3, classList3);
        CombiningEvaluator and = new CombiningEvaluator.And(and1, and2);
        CombiningEvaluator and4 = new CombiningEvaluator.And(and, and3);
        StructuralEvaluator immediateChild = new StructuralEvaluator.ImmediateChild(tag);
        CombiningEvaluator and5 = new CombiningEvaluator.And(and4, immediateChild);
        assertEquals(and5, eval);
    }
}