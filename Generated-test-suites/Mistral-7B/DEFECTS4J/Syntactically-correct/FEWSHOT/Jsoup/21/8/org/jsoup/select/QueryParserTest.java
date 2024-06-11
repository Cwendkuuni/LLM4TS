package org.jsoup.select;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.jsoup.helper.StringUtil;
import org.jsoup.parser.SelectorParser;
import org.jsoup.select.Elements;
import org.jsoup.select.QueryParser;
import org.junit.Test;

public class QueryParserTest {

 @Test
 public void testParseSimpleSelector() {
 String query = "div";
 Evaluator actual = QueryParser.parse(query);
 Evaluator expected = new Evaluator.Tag("div");
 assertEquals(expected, actual);
 }

 @Test
 public void testParseComplexSelector() {
 String query = "div.button:first-child";
 Evaluator actual = QueryParser.parse(query);

 Evaluator expected = new CombiningEvaluator.And(
 new StructuralEvaluator.Tag("div"),
 new Evaluator.Class("button")
 );
 StructuralEvaluator.FirstChild firstChild = new StructuralEvaluator.FirstChild();
 expected.add(firstChild);

 assertEquals(expected, actual);
 }

 @Test
 public void testParseSelectorWithAttribute() {
 String query = "input[type='text']";
 Evaluator actual = QueryParser.parse(query);

 Evaluator expected = new CombiningEvaluator.And(
 new Evaluator.Tag("input"),
 new Evaluator.Attribute("type", "text")
 );

 assertEquals(expected, actual);
 }

 @Test
 public void testParseSelectorWithId() {
 String query = "#myId";
 Evaluator actual = QueryParser.parse(query);

 Evaluator expected = new Evaluator.Id("myId");

 assertEquals(expected, actual);
 }

 @Test
 public void testParseSelectorWithClass() {
 String query = ".myClass";
 Evaluator actual = QueryParser.parse(query);

 Evaluator expected = new Evaluator.Class("myClass");

 assertEquals(expected, actual);
 }

 @Test
 public void testParseSelectorWithMultipleClasses() {
 String query = ".class1.class2";
 Evaluator actual = QueryParser.parse(query);

 Evaluator expected = new CombiningEvaluator.And(
 new Evaluator.Class("class1"),
 new Evaluator.Class("class2")
 );

 assertEquals(expected, actual);
 }

 @Test
 public void testParseSelectorWithIdAndClass() {
 String query = "#myId.myClass";
 Evaluator actual = QueryParser.parse(query);

 Evaluator expected = new CombiningEvaluator.And(
 new Evaluator.Id("myId"),
 new Evaluator.Class("myClass")
 );

 assertEquals(expected, actual);
 }

 @Test
 public void testParseSelectorWithPseudoElement() {
 String query = "div:first-child";
 Evaluator actual = QueryParser.parse(query);

 StructuralEvaluator.FirstChild firstChild = new StructuralEvaluator.FirstChild();
 Evaluator expected = new CombiningEvaluator.And(new StructuralEvaluator.Tag("div"), firstChild);

 assertEquals(expected, actual);
 }

 @Test
 public void testParseSelectorWithPseudoClass() {
 String query = "div.button";
 Evaluator actual = QueryParser.parse(query);

 Evaluator expected = new CombiningEvaluator.And(
 new StructuralEvaluator.Tag("div"),
 new Evaluator.Class("button")
 );

 assertEquals(expected, actual);
 }

 @Test
 public void testParseSelectorWithCombinatorGreaterThan() {
 String query = "p > ul";
 Evaluator actual = QueryParser.parse(query);

 Evaluator pTag = new Evaluator.Tag("p");
 Evaluator ulTag = new Evaluator.Tag("ul");
 CombiningEvaluator.And and = new CombiningEvaluator.And(pTag, new StructuralEvaluator.ImmediateParent(ulTag));

 assertEquals(and, actual);
 }

 @Test
 public void testParseSelectorWithCombinatorSpace() {
 String query = "p span";
 Evaluator actual = QueryParser.parse(query);

 Evaluator pTag = new Evaluator.Tag("p");
 Evaluator spanTag = new Evaluator.Tag("span");
 CombiningEvaluator.And and = new CombiningEvaluator.And(pTag, spanTag);

 assertEquals(and, actual);
 }

 @Test
 public void testParseSelectorWithCombinatorPlus() {
 String query = "p + span";
 Evaluator actual = QueryParser.parse(query);

 Evaluator pTag = new Evaluator.Tag("p");
 Evaluator spanTag = new Evaluator.Tag("span");
 CombiningEvaluator.And and = new CombiningEvaluator.And(pTag, new StructuralEvaluator.ImmediatePreviousSibling(spanTag));

 assertEquals(and, actual);
 }

 @Test
 public void testParseSelectorWithCombinatorTilde() {
 String query = "p ~ span";
 Evaluator actual = QueryParser.parse(query);

 Evaluator pTag = new Evaluator.Tag("p");
 Evaluator spanTag = new Evaluator.Tag("span");
 CombiningEvaluator.And and = new CombiningEvaluator.And(pTag, new StructuralEvaluator.PreviousSibling(spanTag));

 assertEquals(and, actual);
 }

 @Test
 public void testParseSelectorWithAttributeStarting() {
 String query = "input[src^='http']";
 Evaluator actual = QueryParser.parse(query);

 Evaluator inputTag = new Evaluator.Tag("input");
 Evaluator attributeStarting = new Evaluator.AttributeStarting("src", "http");
 CombiningEvaluator.And and = new CombiningEvaluator.And(inputTag, attributeStarting);

 assertEquals(and, actual);
 }

 @Test
 public void testParseSelectorWithAttributeEnding() {
 String query = "input[src$='.jpg']";
 Evaluator actual = QueryParser.parse(query);

 Evaluator inputTag = new Evaluator.Tag("input");
 Evaluator attributeEnding = new Evaluator.AttributeWithValueEnding("src", ".jpg");
 CombiningEvaluator.And and = new CombiningEvaluator.And(inputTag, attributeEnding);

 assertEquals(and, actual);
 }

 @Test
 public void testParseSelectorWithAttributeContaining() {
 String query = "input[name*='search']";
 Evaluator actual = QueryParser.parse(query);

 Evaluator inputTag = new Evaluator.Tag("input");
 Evaluator attributeContaining = new Evaluator.AttributeWithValueContaining("name", "search");
 CombiningEvaluator.And and = new CombiningEvaluator.And(inputTag, attributeContaining);

 assertEquals(and, actual);
 }

 @Test
 public void testParseSelectorWithAttributeExact() {
 String query = "input[type='text']";
 Evaluator actual = QueryParser.parse(query);

 Evaluator inputTag = new Evaluator.Tag("input");
 Evaluator attributeExact = new Evaluator.AttributeWithValue("type", "text");
 CombiningEvaluator.And and = new CombiningEvaluator.And(inputTag, attributeExact);

 assertEquals(and, actual);
 }

 @Test
 public void testParseSelectorWithAttributeNotExact() {
 String query = "input:not([type='text'])";
 Evaluator actual = QueryParser.parse(query);

 StructuralEvaluator.Not not = new StructuralEvaluator.Not(new Evaluator.Tag("input").and(new Evaluator.AttributeWithValue("type", "text")));

 assertEquals(not, actual);
 }

 @Test
 public void testParseSelectorWithAttributeContainsText() {
 String query = "p:contains(Java)";
 Evaluator actual = QueryParser.parse(query);

 Evaluator pTag = new Evaluator.Tag("p");
 Evaluator containsText = new Evaluator.ContainsText("Java");
 CombiningEvaluator.And and = new CombiningEvaluator.And(pTag, containsText);

 assertEquals(and, actual);
 }

 @Test
 public void testParseSelectorWithAttributeContainsTextOwn() {
 String query = "p:containsOwn(Java)";
 Evaluator actual = QueryParser.parse(query);

 Evaluator pTag = new Evaluator.Tag("p");
 Evaluator containsTextOwn = new Evaluator.ContainsOwnText("Java");
 CombiningEvaluator.And and = new CombiningEvaluator.And(pTag, containsTextOwn);

 assertEquals(and, actual);
 }

 @Test
 public void testParseSelectorWithAttributeMatches() {
 String query = "li:matches(^li.highlight$)";
 Evaluator actual = QueryParser.parse(query);

 Evaluator liTag = new Evaluator.Tag("li");
 Evaluator attributeMatches = new Evaluator.Matches(Pattern.compile("^li.highlight$"));
 CombiningEvaluator.And and = new CombiningEvaluator.And(liTag, attributeMatches);

 assertEquals(and, actual);
 }

 @Test
 public void testParseSelectorWithAttributeMatchesOwn() {
 String query = "li:matchesOwn(^li.highlight$)";
 Evaluator actual = QueryParser.parse(query);

 Evaluator liTag = new Evaluator.Tag("li");
 Evaluator attributeMatchesOwn = new Evaluator.MatchesOwn(Pattern.compile("^li.highlight$"));
 CombiningEvaluator.And and = new CombiningEvaluator.And(liTag, attributeMatchesOwn);

 assertEquals(and, actual);
 }

 @Test
 public void testParseSelectorWithIndexLessThan() {
 String query = "li:lt(3)";
 Evaluator actual = QueryParser.parse(query);

 Evaluator liTag = new Evaluator.Tag("li");
 Evaluator indexLessThan = new Evaluator.IndexLessThan(3);
 CombiningEvaluator.And and = new CombiningEvaluator.And(liTag, indexLessThan);

 assertEquals(and, actual);
 }

 @Test
 public void testParseSelectorWithIndexGreaterThan() {
 String query = "li:gt(3)";
 Evaluator actual = QueryParser.parse(query);

 Evaluator liTag = new Evaluator.Tag("li");
 Evaluator indexGreaterThan = new Evaluator.IndexGreaterThan(3);
 CombiningEvaluator.And and = new CombiningEvaluator.And(liTag, indexGreaterThan);

 assertEquals(and, actual);
 }

 @Test
 public void testParseSelectorWithIndexEquals() {
 String query = "li:eq(3)";
 Evaluator actual = QueryParser.parse(query);

 Evaluator liTag = new Evaluator.Tag("li");
 Evaluator indexEquals = new Evaluator.IndexEquals(3);
 CombiningEvaluator.And and = new CombiningEvaluator.And(liTag, indexEquals);

 assertEquals(and, actual);
 }

 @Test
 public void testParseSelectorWithPseudoClassNot() {
 String query = "p:not(:last-child)";
 Evaluator actual = QueryParser.parse(query);

 StructuralEvaluator.Not not = new StructuralEvaluator.Not(new StructuralEvaluator.LastChild());
 StructuralEvaluator.Tag pTag = new StructuralEvaluator.Tag("p");
 CombiningEvaluator.And and = new CombiningEvaluator.And(pTag, not);

 assertEquals(and, actual);
 }

 @Test
 public void testParseSelectorWithPseudoClassHas() {
 String query = "div:has(a:contains(Link))";
 Evaluator actual = QueryParser.parse(query);

 StructuralEvaluator.Has has = new StructuralEvaluator.Has(new Evaluator.Tag("a").and(new Evaluator.ContainsText("Link")));
 StructuralEvaluator.Tag divTag = new StructuralEvaluator.Tag("div");
 CombiningEvaluator.And and = new CombiningEvaluator.And(divTag, has);

 assertEquals(and, actual);
 }

 @Test
 public void testParseSelectorWithNegatedPseudoClassHas() {
 String query = "div:not(:has(a:contains(Link)))";
 Evaluator actual = QueryParser.parse(query);

 StructuralEvaluator.Not not = new StructuralEvaluator.Not(new StructuralEvaluator.Has(new Evaluator.Tag("a").and(new Evaluator.ContainsText("Link"))));
 StructuralEvaluator.Tag divTag = new StructuralEvaluator.Tag("div");
 CombiningEvaluator.And and = new CombiningEvaluator.And(divTag, not);

 assertEquals(and, actual);
 }
}