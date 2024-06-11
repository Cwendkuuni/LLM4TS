import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.StructuralEvaluator;
import org.jsoup.select.Evaluator;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator.Id;
import org.jsoup.select.Evaluator.Class;
import org.jsoup.select.Evaluator.Tag;
import org.jsoup.select.Evaluator.Attribute;
import org.jsoup.select.Evaluator.AttributeStarting;
import org.jsoup.select.Evaluator.AttributeWithValue;
import org.jsoup.select.Evaluator.AttributeWithValueEnding;
import org.jsoup.select.Evaluator.AttributeWithValueContaining;
import org.jsoup.select.Evaluator.AttributeWithValueMatching;
import org.jsoup.select.Evaluator.AllElements;
import org.jsoup.select.Evaluator.IndexLessThan;
import org.jsoup.select.Evaluator.IndexGreaterThan;
import org.jsoup.select.Evaluator.IndexEquals;
import org.jsoup.select.StructuralEvaluator.Root;
import org.jsoup.select.StructuralEvaluator.Has;
import org.jsoup.select.StructuralEvaluator.ImmediateParent;
import org.jsoup.select.StructuralEvaluator.Parent;
import org.jsoup.select.StructuralEvaluator.ImmediatePreviousSibling;
import org.jsoup.select.StructuralEvaluator.PreviousSibling;
import org.jsoup.select.StructuralEvaluator.Not;

public class QueryParserTest {

 @Test
 public void testParseEmptyQuery() {
 String query = "";
 Evaluator evaluator = QueryParser.parse(query);
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 Assert.assertEquals(0, ((CombiningEvaluator.And) evaluator).size());
 }

 @Test
 public void testParseRootQuery() {
 String query = "*";
 Evaluator evaluator = QueryParser.parse(query);
 assertEquals(new Root(), evaluator);
 }

 @Test
 public void testParseSimpleClassQuery() {
 String query = ".class1";
 Evaluator evaluator = QueryParser.parse(query);
 assertEquals(new Class("class1"), evaluator);
 }

 @Test
 public void testParseSimpleIdQuery() {
 String query = "#id1";
 Evaluator evaluator = QueryParser.parse(query);
 assertEquals(new Id("id1"), evaluator);
 }

 @Test
 public void testParseTagQuery() {
 String query = "tag1";
 Evaluator evaluator = QueryParser.parse(query);
 assertEquals(new Tag("tag1"), evaluator);
 }

 @Test
 public void testParseCombinedQuery() {
 String query = "div.class1 > p#id1";
 Evaluator evaluator = QueryParser.parse(query);

 StructuralEvaluator.ImmediateParent immediateParent = new StructuralEvaluator.ImmediateParent(new Tag("div"));
 StructuralEvaluator.Tag tag = new StructuralEvaluator.Tag("p");
 StructuralEvaluator.Id id = new Id("id1");

 CombiningEvaluator.And and = new CombiningEvaluator.And();
 and.add(immediateParent);
 and.add(tag);
 and.add(id);

 assertEquals(and, evaluator);
 }

 @Test
 public void testParseMultiCombinatorQuery() {
 String query = "div > p > div > span > input:containsOwn(text)";
 Evaluator evaluator = QueryParser.parse(query);

 StructuralEvaluator.ImmediateParent immediateParent1 = new StructuralEvaluator.ImmediateParent(new Tag("div"));
 StructuralEvaluator.Tag tag1 = new StructuralEvaluator.Tag("p");
 StructuralEvaluator.ImmediateParent immediateParent2 = new StructuralEvaluator.ImmediateParent(new Tag("div"));
 StructuralEvaluator.Tag tag2 = new StructuralEvaluator.Tag("span");
 StructuralEvaluator.Tag tag3 = new Tag("input");
 Evaluator containsOwnText = new Evaluator.ContainsOwnText("text");

 StructuralEvaluator.And and1 = new StructuralEvaluator.And();
 and1.add(immediateParent1);
 and1.add(tag1);
 StructuralEvaluator.And and2 = new StructuralEvaluator.And();
 and2.add(immediateParent2);
 and2.add(tag2);
 StructuralEvaluator.And and3 = new StructuralEvaluator.And();
 and3.add(and1);
 and3.add(tag3);
 StructuralEvaluator.And and4 = new StructuralEvaluator.And();
 and4.add(and2);
 and4.add(containsOwnText);

 assertEquals(and4, evaluator);
 }

 @Test
 public void testParseAttributeQuery() {
 String query = "input[type=text][name=username]";
 Evaluator evaluator = QueryParser.parse(query);

 Attribute attribute1 = new Attribute("type", "text");
 Attribute attribute2 = new Attribute("name", "username");

 StructuralEvaluator.And and1 = new StructuralEvaluator.And();
 StructuralEvaluator.Tag tag = new Tag("input");
 and1.add(tag);
 and1.add(attribute1);
 StructuralEvaluator.And and2 = new StructuralEvaluator.And();
 and2.add(tag);
 and2.add(attribute2);

 CombiningEvaluator.And and = new CombiningEvaluator.And();
 and.add(and1);
 and.add(and2);

 assertEquals(and, evaluator);
 }

 @Test
 public void testParsePseudoClassQuery() {
 String query = "a:link";
 Evaluator evaluator = QueryParser.parse(query);

 StructuralEvaluator.Tag tag = new Tag("a");
 StructuralEvaluator.PseudoClass pseudoClass = new StructuralEvaluator.PseudoClass("link");

 StructuralEvaluator.And and = new StructuralEvaluator.And();
 and.add(tag);
 and.add(pseudoClass);

 assertEquals(and, evaluator);
 }

 @Test
 public void testParsePseudoElementQuery() {
 String query = "ul > li:last-child > a:first-child::before";
 Evaluator evaluator = QueryParser.parse(query);

 StructuralEvaluator.ImmediateParent immediateParent1 = new StructuralEvaluator.ImmediateParent(new Tag("ul"));
 StructuralEvaluator.Tag tag1 = new StructuralEvaluator.Tag("li");
 StructuralEvaluator.Tag tag2 = new Tag("a");
 StructuralEvaluator.PseudoElement pseudoElement = new StructuralEvaluator.PseudoElement("before");

 StructuralEvaluator.And and1 = new StructuralEvaluator.And();
 and1.add(immediateParent1);
 and1.add(tag1);
 StructuralEvaluator.And and2 = new StructuralEvaluator.And();
 and2.add(tag1);
 and2.add(tag2);
 StructuralEvaluator.And and3 = new StructuralEvaluator.And();
 and3.add(and2);
 and3.add(pseudoElement);

 StructuralEvaluator.StructuralPseudoClass pseudoClass = new StructuralEvaluator.StructuralPseudoClass("last-child");
 StructuralEvaluator.StructuralPseudoClass pseudoClass1 = new StructuralEvaluator.StructuralPseudoClass("first-child");

 StructuralEvaluator.ImmediatePreviousSibling immediatePreviousSibling = new StructuralEvaluator.ImmediatePreviousSibling(tag1);
 StructuralEvaluator.ImmediatePreviousSibling immediatePreviousSibling1 = new StructuralEvaluator.ImmediatePreviousSibling(tag2);

 CombiningEvaluator.And and = new CombiningEvaluator.And();
 and.add(immediateParent1);
 and.add(and3);
 and.add(new StructuralEvaluator.And(new StructuralEvaluator.And(new StructuralEvaluator.And(new StructuralEvaluator.StructuralPseudoClass("last-child"), tag1), immediatePreviousSibling), new StructuralEvaluator.And(new StructuralEvaluator.StructuralPseudoClass("first-child"), tag2), immediatePreviousSibling1));
 and.add(pseudoClass);
 and.add(pseudoClass1);

 assertEquals(and, evaluator);
 }

 @Test
 public void testParseNegatedQuery() {
 String query = "div:not(.class1) > p";
 Evaluator evaluator = QueryParser.parse(query);

 StructuralEvaluator.Tag tag1 = new Tag("div");
 CombiningEvaluator.Not not = new CombiningEvaluator.Not(new Class("class1"));
 StructuralEvaluator.Tag tag2 = new Tag("p");

 StructuralEvaluator.And and = new StructuralEvaluator.And();
 and.add(new StructuralEvaluator.And(new StructuralEvaluator.Not(tag1), not));
 and.add(tag2);

 assertEquals(and, evaluator);
 }

 @Test
 public void testParseQueryWithInvalidCombinator() {
 String query = "div @> p";
 try {
 QueryParser.parse(query);
 fail("Expected SelectorParseException");
 } catch (Selector.SelectorParseException e) {
 // expected
 }
 }
}