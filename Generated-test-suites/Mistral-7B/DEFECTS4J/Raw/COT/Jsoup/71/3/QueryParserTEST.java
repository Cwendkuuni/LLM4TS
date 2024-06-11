import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.QueryParser;
import org.jsoup.parser.TokenQueue;
import org.jsoup.parser.Selector;
import org.jsoup.parser.StructuralEvaluator;
import org.jsoup.parser.CombiningEvaluator;
import org.jsoup.parser.Evaluator;
import org.jsoup.parser.Evaluator.Id;
import org.jsoup.parser.Evaluator.Class;
import org.jsoup.parser.Evaluator.Tag;
import org.jsoup.parser.Evaluator.Attribute;
import org.jsoup.parser.Evaluator.AttributeWithValue;
import org.jsoup.parser.Evaluator.AttributeWithValueContaining;
import org.jsoup.parser.Evaluator.AttributeWithValueStarting;
import org.jsoup.parser.Evaluator.AttributeWithValueEnding;
import org.jsoup.parser.Evaluator.AttributeWithValueMatching;
import org.jsoup.parser.Evaluator.AllElements;
import org.jsoup.parser.Evaluator.IndexLessThan;
import org.jsoup.parser.Evaluator.IndexGreaterThan;
import org.jsoup.parser.Evaluator.IndexEquals;
import org.jsoup.parser.Evaluator.IsNthChild;
import org.jsoup.parser.Evaluator.IsNthLastChild;
import org.jsoup.parser.Evaluator.IsNthOfType;
import org.jsoup.parser.Evaluator.IsNthLastOfType;
import org.jsoup.parser.Evaluator.IsEmpty;
import org.jsoup.parser.Evaluator.IsRoot;
import org.jsoup.parser.Evaluator.IsFirstChild;
import org.jsoup.parser.Evaluator.IsLastChild;
import org.jsoup.parser.Evaluator.IsFirstOfType;
import org.jsoup.parser.Evaluator.IsLastOfType;
import org.jsoup.parser.Evaluator.IsOnlyChild;
import org.jsoup.parser.Evaluator.IsOnlyOfType;
import org.jsoup.helper.StringUtil;
import java.util.ArrayList;
import java.util.List;

public class QueryParserTest {

 // Test for parse() method
 @Test
 public void testParse() {
 String query = "div.class1 > div:first-child > p:contains(text)";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();

 Assert.assertNotNull(evaluator);

 // Test the parsed evaluator against a selector
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select("body");
 Assert.assertEquals(1, elements.size());
 }

 // Test for empty query
 @Test(expected = Selector.SelectorParseException.class)
 public void testEmptyQuery() {
 String query = "";
 new QueryParser(query).parse();
 }

 // Test for combinator ' ' (descendant)
 @Test
 public void testCombinatorSpace() {
 String query = "div > p";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();

 Assert.assertNotNull(evaluator);
 }

 // Test for combinator > (child)
 @Test
 public void testCombinatorGreaterThan() {
 String query = "div > p";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();

 Assert.assertNotNull(evaluator);
 }

 // Test for combinator + (adjacent)
 @Test
 public void testCombinatorPlus() {
 String query = "div + p";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();

 Assert.assertNotNull(evaluator);
 }

 // Test for combinator ~ (prev sibling)
 @Test
 public void testCombinatorTilde() {
 String query = "div ~ p";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();

 Assert.assertNotNull(evaluator);
 }

 // Test for combinator , (group)
 @Test
 public void testCombinatorComma() {
 String query = "div, p";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();

 Assert.assertNotNull(evaluator);

 // Test for combinator OR (,)
 Evaluator orEvaluator = evaluator;
 while (orEvaluator instanceof CombiningEvaluator.Or) {
 orEvaluator = ((CombiningEvaluator.Or) orEvaluator).rightMostEvaluator();
 }
 List<Evaluator> orOperands = ((CombiningEvaluator.Or) orEvaluator).operands();
 Assert.assertEquals(2, orOperands.size());
 Assert.assertTrue(orOperands.get(0) instanceof Tag);
 Assert.assertTrue(orOperands.get(1) instanceof Tag);
 }

 // Test for tag name
 @Test
 public void testTagName() {
 String query = "div";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof Tag);
 }

 // Test for tag name with namespace
 @Test
 public void testTagNameWithNamespace() {
 String query = "html:html";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof Tag);
 Assert.assertEquals("html", evaluator.tagName());
 }

 // Test for element with id
 @Test
 public void testElementWithId() {
 String query = "#id1";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof Id);
 Assert.assertEquals("id1", evaluator.selectorText());
 }

 // Test for element with class
 @Test
 public void testElementWithClass() {
 String query = ".class1";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof Class);
 Assert.assertEquals("class1", evaluator.selectorText());
 }

 // Test for element with attribute
 @Test
 public void testElementWithAttribute() {
 String query = "[attribute='value']";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof Attribute);
 Assert.assertEquals("attribute", evaluator.selectorText());
 Assert.assertEquals("value", evaluator.selectorAttribute());
 }

 // Test for element with attribute and value starting with
 @Test
 public void testElementWithAttributeStarting() {
 String query = "[attribute^='value']";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof AttributeWithValueStarting);
 Assert.assertEquals("attribute", evaluator.selectorText());
 Assert.assertEquals("value", evaluator.selectorAttributeStartsWithValue());
 }

 // Test for element with attribute and value ending with
 @Test
 public void testElementWithAttributeEnding() {
 String query = "[attribute$='value']";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof AttributeWithValueEnding);
 Assert.assertEquals("attribute", evaluator.selectorText());
 Assert.assertEquals("value", evaluator.selectorAttributeEndsWithValue());
 }

 // Test for element with attribute and value containing
 @Test
 public void testElementWithAttributeContaining() {
 String query = "[attribute*='value']";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof AttributeWithValueContaining);
 Assert.assertEquals("attribute", evaluator.selectorText());
 Assert.assertEquals("value", evaluator.selectorAttributeContainingValue());
 }

 // Test for element with attribute and value matching using regex
 @Test
 public void testElementWithAttributeMatching() {
 String query = "[attribute~='value']";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof AttributeWithValueMatching);
 Assert.assertEquals("attribute", evaluator.selectorText());
 }

 // Test for all elements
 @Test
 public void testAllElements() {
 String query = "*";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof AllElements);
 }

 // Test for pseudo elements :first-child
 @Test
 public void testPseudoElementFirstChild() {
 String query = "div > :first-child";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof IsFirstChild);
 }

 // Test for pseudo elements :last-child
 @Test
 public void testPseudoElementLastChild() {
 String query = "div > :last-child";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof IsLastChild);
 }

 // Test for pseudo elements :nth-child
 @Test
 public void testPseudoElementNthChild() {
 String query = "div:nth-child(2)";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof IsNthChild);
 Assert.assertEquals(2, evaluator.selectorIndex());
 }

 // Test for pseudo elements :nth-last-child
 @Test
 public void testPseudoElementNthLastChild() {
 String query = "div:nth-last-child(2)";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof IsNthLastChild);
 Assert.assertEquals(2, evaluator.selectorIndex());
 }

 // Test for pseudo elements :nth-of-type
 @Test
 public void testPseudoElementNthOfType() {
 String query = "div:nth-of-type(2)";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof IsNthOfType);
 Assert.assertEquals(2, evaluator.selectorIndex());
 }

 // Test for pseudo elements :nth-last-of-type
 @Test
 public void testPseudoElementNthLastOfType() {
 String query = "div:nth-last-of-type(2)";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof IsNthLastOfType);
 Assert.assertEquals(2, evaluator.selectorIndex());
 }

 // Test for pseudo elements :empty
 @Test
 public void testPseudoElementEmpty() {
 String query = "div:empty";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof IsEmpty);
 }

 // Test for pseudo elements :root
 @Test
 public void testPseudoElementRoot() {
 String query = ":root";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof IsRoot);
 }

 // Test for pseudo elements :not
 @Test
 public void testPseudoElementNot() {
 String query = ":not(div)";
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof StructuralEvaluator.Not);
 Assert.assertTrue(evaluator.getChildEvaluator() instanceof Tag);
 Assert.assertEquals("div", evaluator.getChildEvaluator().tagName());
 }
}