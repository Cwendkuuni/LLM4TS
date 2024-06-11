import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.jsoup.helper.StringUtil;
import org.jsoup.parser.TokenQueue;
import org.junit.Test;

import org.jsoup.select.QueryParser;

public class QueryParserTest {

 private static final String[] COMBINATORS = QueryParser.combinators;

 @Test
 public void testParseEmptyQuery() {
 QueryParser parser = new QueryParser("");
 Evaluator evaluator = parser.parse();
 assertNull(evaluator);
 }

 @Test
 public void testParseSimpleTagQuery() {
 String query = "div";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertInstanceOf(Evaluator.Tag.class, evaluator);
 assertEquals("div", ((Evaluator.Tag) evaluator).getTag());
 }

 @Test
 public void testParseIdQuery() {
 String query = "#myId";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertInstanceOf(Evaluator.Id.class, evaluator);
 assertEquals("myId", ((Evaluator.Id) evaluator).getId());
 }

 @Test
 public void testParseClassQuery() {
 String query = ".myClass";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertInstanceOf(Evaluator.Class.class, evaluator);
 assertEquals("myClass", ((Evaluator.Class) evaluator).getClassName());
 }

 @Test
 public void testParseCombinedQueries() {
 String query = "div#myId.myClass";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertInstanceOf(CombiningEvaluator.And.class, evaluator);

 Evaluator firstEvaluator = ((CombiningEvaluator.And) evaluator).getEvaluators().get(0);
 assertInstanceOf(Evaluator.Id.class, firstEvaluator);
 assertEquals("myId", ((Evaluator.Id) firstEvaluator).getId());

 Evaluator secondEvaluator = ((CombiningEvaluator.And) evaluator).getEvaluators().get(1);
 assertInstanceOf(Evaluator.Class.class, secondEvaluator);
 assertEquals("myClass", ((Evaluator.Class) secondEvaluator).getClassName());
 }

 @Test
 public void testParseAttributeQuery() {
 String query = "[data-test='myTest']";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertInstanceOf(Evaluator.Attribute.class, evaluator);
 assertEquals("data-test", ((Evaluator.Attribute) evaluator).getKey());
 assertEquals("myTest", ((Evaluator.Attribute) evaluator).getValue());
 }

 @Test
 public void testParseComplexQuery() {
 String query = "div > .myClass:not(.otherClass) > a:contains(linkText)";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);

 Evaluator rootElementEvaluator = evaluator;
 while (rootElementEvaluator instanceof CombiningEvaluator.And) {
 rootElementEvaluator = ((CombiningEvaluator.And) rootElementEvaluator).getEvaluators().get(0);
 }
 assertInstanceOf(Evaluator.Tag.class, rootElementEvaluator);
 assertEquals("div", ((Evaluator.Tag) rootElementEvaluator).getTag());

 Evaluator childElementEvaluator = null;
 Evaluator classEvaluator = null;
 Evaluator linkTextEvaluator = null;

 for (Evaluator evaluator : ((CombiningEvaluator.And) evaluator).getEvaluators()) {
 if (evaluator instanceof CombiningEvaluator.And) {
 if (childElementEvaluator == null) {
 childElementEvaluator = ((CombiningEvaluator.And) evaluator).getEvaluators().get(0);
 assertInstanceOf(Evaluator.Tag.class, childElementEvaluator);
 assertEquals(".", ((Evaluator.Tag) childElementEvaluator).getTag());
 }
 if (classEvaluator == null) {
 classEvaluator = ((CombiningEvaluator.And) evaluator).getEvaluators().get(1);
 assertInstanceOf(Evaluator.Class.class, classEvaluator);
 assertEquals("myClass", ((Evaluator.Class) classEvaluator).getClassName());
 }
 } else if (evaluator instanceof Evaluator.ContainsText) {
 linkTextEvaluator = evaluator;
 assertEquals("linkText", ((Evaluator.ContainsText) evaluator).getText());
 } else {
 throw new AssertionError("Unexpected evaluator: " + evaluator);
 }
 }

 assertNull(childElementEvaluator.getNextSibling());
 assertNull(classEvaluator.getNextSibling());
 assertNull(linkTextEvaluator.getNextSibling());
 }

 @Test
 public void testParseInvalidQuery() {
 String query = "*:not:not:not(div)"; // this query has an extra "not"
 QueryParser parser = new QueryParser(query);
 assertThrows(Selector.SelectorParseException.class, parser::parse);
 }

 @Test
 public void testParseQueryWithInvalidCombination() {
 String query = "div*class"; // this query has an invalid combination of combinator and tag
 QueryParser parser = new QueryParser(query);
 assertThrows(Selector.SelectorParseException.class, parser::parse);
 }

 @Test
 public void testParseQueryWithInvalidSelectorType() {
 String query = "div:not(#myId)"; // this query has an invalid selector type for the combinator "not"
 QueryParser parser = new QueryParser(query);
 assertThrows(Selector.SelectorParseException.class, parser::parse);
 }

 @Test
 public void testParseQueryWithMissingElementName() {
 String query = "> .myClass"; // this query is missing the element name for the combinator ">"
 QueryParser parser = new QueryParser(query);
 assertThrows(Selector.SelectorParseException.class, parser::parse);
 }

 @Test
 public void testParseQueryWithMissingAttributeName() {
 String query = "[=]"; // this query is missing the attribute name
 QueryParser parser = new QueryParser(query);
 assertThrows(Selector.SelectorParseException.class, parser::parse);
 }

 @Test
 public void testParseQueryWithMissingAttributeValue() {
 String query = "[data-test]"; // this query is missing the attribute value
 QueryParser parser = new QueryParser(query);
 assertThrows(Selector.SelectorParseException.class, parser::parse);
 }

 @Test
 public void testParseQueryWithMissingCombinator() {
 String query = "div.myClass"; // this query is missing the combinator for the class selector
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertInstanceOf(CombiningEvaluator.And.class, evaluator);

 Evaluator classEvaluator = ((CombiningEvaluator.And) evaluator).getEvaluators().get(1);
 assertInstanceOf(Evaluator.Class.class, classEvaluator);
 assertEquals("myClass", ((Evaluator.Class) classEvaluator).getClassName());
 }

 @Test
 public void testParseQueryWithMissingOpeningParenthesis() {
 String query = ":containslinkText"; // this query is missing the opening parenthesis for the pseudo-class
 QueryParser parser = new QueryParser(query);
 assertThrows(Selector.SelectorParseException.class, parser::parse);
 }

 @Test
 public void testParseQueryWithMissingClosingParenthesis() {
 String query = ":contains(linkText)"; // this query is missing the closing parenthesis for the pseudo-class
 QueryParser parser = new QueryParser(query);
 assertThrows(Selector.SelectorParseException.class, parser::parse);
 }

 @Test
 public void testParseQueryWithMissingClosingBracket() {
 String query = "[data-test=myTest, data-test=myOtherTest]"; // this query is missing the closing bracket for the attribute selector
 QueryParser parser = new QueryParser(query);
 assertThrows(Selector.SelectorParseException.class, parser::parse);
 }

 @Test
 public void testParseQueryWithEmptyCombinedQueries() {
 String query = ":not(.emptyClass)"; // this query has empty combined queries
 QueryParser parser = new QueryParser(query);
 assertNotNull(parser.parse());
 }

 @Test
 public void testParseQueryWithSingleElementAndCombinedQueries() {
 String query = "div:not(.emptyClass) > a:contains(linkText)";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);

 Evaluator divEvaluator = null;
 Evaluator notEvaluator = null;
 Evaluator aEvaluator = null;
 Evaluator containsTextEvaluator = null;

 for (Evaluator evaluatorToCheck : ((CombiningEvaluator.And) evaluator).getEvaluators()) {
 if (evaluatorToCheck instanceof CombiningEvaluator.And) {
 if (divEvaluator == null) {
 divEvaluator = ((CombiningEvaluator.And) evaluatorToCheck).getEvaluators().get(0);
 assertInstanceOf(Evaluator.Tag.class, divEvaluator);
 assertEquals("div", ((Evaluator.Tag) divEvaluator).getTag());
 }
 if (notEvaluator == null) {
 notEvaluator = ((CombiningEvaluator.And) evaluatorToCheck).getEvaluators().get(1);
 assertInstanceOf(StructuralEvaluator.Not.class, notEvaluator);
 }
 } else if (evaluatorToCheck instanceof Evaluator.Tag) {
 aEvaluator = evaluatorToCheck;
 assertEquals("a", ((Evaluator.Tag) aEvaluator).getTag());
 } else if (evaluatorToCheck instanceof Evaluator.ContainsText) {
 containsTextEvaluator = evaluatorToCheck;
 assertEquals("linkText", ((Evaluator.ContainsText) containsTextEvaluator).getText());
 } else {
 throw new AssertionError("Unexpected evaluator: " + evaluatorToCheck);
 }
 }

 assertNull(divEvaluator.getNextSibling());
 assertNull(notEvaluator.getNextSibling());
 assertNull(aEvaluator.getNextSibling());
 assertNull(containsTextEvaluator.getNextSibling());
 }

}