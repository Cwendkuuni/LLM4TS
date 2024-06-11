import org. jsoup.parser.QueryParser;
import org.jsoup.select.Evaluator;
import org.jsoup.select.StructuralEvaluator;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator.Id;
import org.jsoup.select.Evaluator.Class;
import org.jsoup.select.Evaluator.Tag;
import org.jsoup.select.Evaluator.TagEndsWith;
import org.jsoup.select.Evaluator.Attribute;
import org.jsoup.select.Evaluator.AttributeStarting;
import org.jsoup.select.Evaluator.AttributeWithValue;
import org.jsoup.select.Evaluator.AttributeWithValueContaining;
import org.jsoup.select.Evaluator.AttributeWithValueEnding;
import org.jsoup.select.Evaluator.AttributeWithValueMatching;
import org.jsoup.select.Evaluator.AllElements;
import org.jsoup.select.Evaluator.IndexLessThan;
import org.jsoup.select.Evaluator.IndexGreaterThan;
import org.jsoup.select.Evaluator.IndexEquals;
import org.jsoup.select.Evaluator.IsFirstChild;
import org.jsoup.select.Evaluator.IsLastChild;
import org.jsoup.select.Evaluator.IsFirstOfType;
import org.jsoup.select.Evaluator.IsLastOfType;
import org.jsoup.select.Evaluator.IsEmpty;
import org.jsoup.select.Evaluator.IsRoot;
import org.jsoup.select.Evaluator.IsOnlyChild;
import org.jsoup.select.Evaluator.IsOnlyOfType;
import org.jsoup.select.Evaluator.IsNthOfType;
import org.jsoup.select.Evaluator.IsNthChild;
import org.jsoup.select.Evaluator.IsNthLastOfType;
import org.jsoup.select.Evaluator.IsNthLastChild;
import org.jsoup.select.Evaluator.Has;
import org.jsoup.select.Evaluator.ContainsText;
import org.jsoup.select.Evaluator.ContainsOwnText;
import org.jsoup.select.Evaluator.ContainsData;
import org.jsoup.select.Evaluator.Matches;
import org.jsoup.select.Evaluator.MatchesOwn;
import org.jsoup.select.Evaluator.Not;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class QueryParserTest {

 @org.junit.Test
 public void testParse() {
 QueryParser parser = new QueryParser("div.class");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertEquals(Evaluator.class, evaluator.getClass());
 }

 @org.junit.Test
 public void testParseCombinator() {
 QueryParser parser = new QueryParser("div > p");
 Evaluator rootEval = parser.parse();
 Evaluator currentEval = null;
 Evaluator newEval = null;

 if (rootEval instanceof CombiningEvaluator.And) {
 currentEval = rootEval;
 rootEval = ((CombiningEvaluator.And) rootEval).rightMostEvaluator();
 }

 parser = new QueryParser("p");
 newEval = parser.parse();

 if (currentEval instanceof StructuralEvaluator.ImmediateParent) {
 StructuralEvaluator.ImmediateParent immediateParent = (StructuralEvaluator.ImmediateParent) currentEval;
 assertEquals("div", immediateParent.getParentTag());
 }

 if (rootEval instanceof CombiningEvaluator.And) {
 ((CombiningEvaluator.And) rootEval).add(newEval);
 } else {
 ((CombiningEvaluator.Or) rootEval).add(newEval);
 }

 Evaluator finalEval = parser.parse();
 assertEquals(CombiningEvaluator.class, finalEval.getClass());
 }

 @org.junit.Test
 public void testParseAttribute() {
 QueryParser parser = new QueryParser("[href='http://example.com']");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
 Evaluator.AttributeWithValue attributeWithValue = (Evaluator.AttributeWithValue) evaluator;
 assertEquals("href", attributeWithValue.getAttributeName());
 assertEquals("http://example.com", attributeWithValue.getValue());
 }

 @org.junit.Test
 public void testParseClass() {
 QueryParser parser = new QueryParser(".class");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.Class);
 Evaluator.Class classEvaluator = (Evaluator.Class) evaluator;
 assertEquals("class", classEvaluator.getClass().getSimpleName());
 assertEquals("class", classEvaluator.getValue());
 }

 @org.junit.Test
 public void testParseId() {
 QueryParser parser = new QueryParser("#id");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.Id);
 Evaluator.Id idEvaluator = (Evaluator.Id) evaluator;
 assertEquals("id", idEvaluator.getId());
 }

 @org.junit.Test
 public void testParseTag() {
 QueryParser parser = new QueryParser("div");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.Tag);
 Evaluator.Tag tagEvaluator = (Evaluator.Tag) evaluator;
 assertEquals("div", tagEvaluator.getTagName());
 }

 @org.junit.Test
 public void testParseTagWithNamespace() {
 QueryParser parser = new QueryParser("html:body");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.TagEndsWith);
 Evaluator.TagEndsWith tagEndsWithEvaluator = (Evaluator.TagEndsWith) evaluator;
 assertEquals("body", tagEndsWithEvaluator.getTagName());
 assertEquals("html", tagEndsWithEvaluator.getNamespace());
 }

 @org.junit.Test
 public void testParseAttributeStarting() {
 QueryParser parser = new QueryParser("[title^='Test']");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.AttributeStarting);
 Evaluator.AttributeStarting attributeStartingEvaluator = (Evaluator.AttributeStarting) evaluator;
 assertEquals("title", attributeStartingEvaluator.getAttributeName());
 assertEquals("Test", attributeStartingEvaluator.getValuePrefix());
 }

 @org.junit.Test
 public void testParseAttributeEnding() {
 QueryParser parser = new QueryParser("[title$='Test']");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.AttributeWithValueEnding);
 Evaluator.AttributeWithValueEnding attributeEndingEvaluator = (Evaluator.AttributeWithValueEnding) evaluator;
 assertEquals("title", attributeEndingEvaluator.getAttributeName());
 assertEquals("Test", attributeEndingEvaluator.getValuePrefix());
 }

 @org.junit.Test
 public void testParseAttributeContaining() {
 QueryParser parser = new QueryParser("[title*='Test']");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.AttributeWithValueContaining);
 Evaluator.AttributeWithValueContaining attributeContainingEvaluator = (Evaluator.AttributeWithValueContaining) evaluator;
 assertEquals("title", attributeContainingEvaluator.getAttributeName());
 assertEquals("Test", attributeContainingEvaluator.getValuePrefix());
 }

 @org.junit.Test
 public void testParseAttributeMatching() {
 QueryParser parser = new QueryParser("[title~='Test']");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.AttributeWithValueMatching);
 Evaluator.AttributeWithValueMatching attributeMatchingEvaluator = (Evaluator.AttributeWithValueMatching) evaluator;
 assertEquals("title", attributeMatchingEvaluator.getAttributeName());
 Pattern pattern = attributeMatchingEvaluator.getValuePattern();
 assertTrue(pattern.matcher("TestTitle").matches());
 assertTrue(pattern.matcher("AnotherTestTitle").matches());
 }

 @org.junit.Test
 public void testParsePseudoClassIsEmpty() {
 QueryParser parser = new QueryParser(":empty");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.IsEmpty);
 }

 @org.junit.Test
 public void testParsePseudoClassIsRoot() {
 QueryParser parser = new QueryParser(":root");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.IsRoot);
 }

 @org.junit.Test
 public void testParsePseudoClassIsFirstChild() {
 QueryParser parser = new QueryParser("div:first-child");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.IsFirstChild);
 }

 @org.junit.Test
 public void testParsePseudoClassIsLastChild() {
 QueryParser parser = new QueryParser("div:last-child");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.IsLastChild);
 }

 @org.junit.Test
 public void testParsePseudoClassIsFirstOfType() {
 QueryParser parser = new QueryParser("div:first-of-type");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.IsFirstOfType);
 }

 @org.junit.Test
 public void testParsePseudoClassIsLastOfType() {
 QueryParser parser = new QueryParser("div:last-of-type");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.IsLastOfType);
 }

 @org.junit.Test
 public void testParsePseudoClassIsOnlyChild() {
 QueryParser parser = new QueryParser("div:only-child");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.IsOnlyChild);
 }

 @org.junit.Test
 public void testParsePseudoClassIsOnlyOfType() {
 QueryParser parser = new QueryParser("div:only-of-type");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.IsOnlyOfType);
 }

 @org.junit.Test
 public void testParsePseudoClassHas() {
 QueryParser parser = new QueryParser("div:has(p)");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.Has);
 }

 @org.junit.Test
 public void testParsePseudoClassContainsText() {
 QueryParser parser = new QueryParser("div:contains(Text)");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.ContainsText);
 }

 @org.junit.Test
 public void testParsePseudoClassContainsOwnText() {
 QueryParser parser = new QueryParser("div:containsOwn(Text)");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.ContainsOwnText);
 }

 @org.junit.Test
 public void testParsePseudoClassContainsData() {
 QueryParser parser = new QueryParser("div:containsData(data)");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.ContainsData);
 }

 @org.junit.Test
 public void testParsePseudoClassMatches() {
 QueryParser parser = new QueryParser("div:matches(data)");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.Matches);
 }

 @org.junit.Test
 public void testParsePseudoClassMatchesOwn() {
 QueryParser parser = new QueryParser("div:matchesOwn(data)");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.MatchesOwn);
 }

 @org.junit.Test
 public void testParsePseudoClassNot() {
 QueryParser parser = new QueryParser("div:not(p)");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.Not);
 }

 @org.junit.Test
 public void testParseNthChild() {
 QueryParser parser = new QueryParser("div:nth-child(2)");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.IsNthChild);
 Evaluator.IsNthChild isNthChildEvaluator = (Evaluator.IsNthChild) evaluator;
 assertEquals(2, isNthChildEvaluator.getNthChildIndex());
 }

 @org.junit.Test
 public void testParseNthLastChild() {
 QueryParser parser = new QueryParser("div:nth-last-child(2)");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.IsNthLastChild);
 Evaluator.IsNthLastChild isNthLastChildEvaluator = (Evaluator.IsNthLastChild) evaluator;
 assertEquals(2, isNthLastChildEvaluator.getNthLastChildIndex());
 }

 @org.junit.Test
 public void testParseNthOfType() {
 QueryParser parser = new QueryParser("div:nth-of-type(2)");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.IsNthOfType);
 Evaluator.IsNthOfType isNthOfTypeEvaluator = (Evaluator.IsNthOfType) evaluator;
 assertEquals(2, isNthOfTypeEvaluator.getNthOfTypeIndex());
 }

 @org.junit.Test
 public void testParseNthLastOfType() {
 QueryParser parser = new QueryParser("div:nth-last-of-type(2)");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Evaluator.IsNthLastOfType);
 Evaluator.IsNthLastOfType isNthLastOfTypeEvaluator = (Evaluator.IsNthLastOfType) evaluator;
 assertEquals(2, isNthLastOfTypeEvaluator.getNthLastOfTypeIndex());
 }
}