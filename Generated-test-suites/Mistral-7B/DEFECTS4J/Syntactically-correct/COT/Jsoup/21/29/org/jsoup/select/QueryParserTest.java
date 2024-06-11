import static org.junit.Assert.*;
import org.jsoup.parser.QueryParser;
import org.jsoup.parser.Selector;
import org.junit.Test;

public class QueryParserTest {

 private static final String BASIC_SELECTOR = ".testClass";
 private static final String TEST_CASE_QUERY = "body > .testClass:has(.testChild)";

 @Test
 public void testParseBasicSelector() {
 QueryParser queryParser = new QueryParser(BASIC_SELECTOR);
 Selector selector = queryParser.parse();
 assertEquals(".testClass", selector.toString());
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseUnknownCombinator() {
 QueryParser queryParser = new QueryParser("body > .testClass%");
 queryParser.parse(); // this line should throw an exception
 }

 @Test
 public void testParseCombinator() {
 QueryParser queryParser = new QueryParser(TEST_CASE_QUERY);
 Selector selector = queryParser.parse();
 Selector testClass = selector.first();
 Selector testChild = selector.getElementsByClass("testChild").get(0);
 assertEquals("body > .testClass:has(.testChild)", selector.toString());
 assertEquals("body", testClass.parent().tagName());
 assertTrue(testChild.containsOwnText("testChildText"));
 }

 @Test
 public void testParseAttribute() {
 String attributeQuery = "[title='Test']";
 QueryParser queryParser = new QueryParser(attributeQuery);
 Selector selector = queryParser.parse();
 assertEquals("[title='Test']", selector.toString());
 Selector titleAttribute = selector.first();
 assertEquals("title", titleAttribute.attr("title"));
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidAttribute() {
 String invalidAttributeQuery = "[class='Test']=1";
 QueryParser queryParser = new QueryParser(invalidAttributeQuery);
 queryParser.parse(); // this line should throw an exception
 }

 @Test
 public void testParseAttributeWithValueContaining() {
 String attributeContainingQuery = "[title*='Test']";
 QueryParser queryParser = new QueryParser(attributeContainingQuery);
 Selector selector = queryParser.parse();
 Selector titleAttribute = selector.first();
 assertTrue(titleAttribute.attr("title").contains("Test"));
 }

 @Test
 public void testParseAttributeWithValueStarting() {
 String attributeStartingQuery = "[title^='Test']";
 QueryParser queryParser = new QueryParser(attributeStartingQuery);
 Selector selector = queryParser.parse();
 Selector titleAttribute = selector.first();
 assertTrue(titleAttribute.attr("title").startsWith("Test"));
 }

 @Test
 public void testParseAttributeWithValueEnding() {
 String attributeEndingQuery = "[title$='Test']";
 QueryParser queryParser = new QueryParser(attributeEndingQuery);
 Selector selector = queryParser.parse();
 Selector titleAttribute = selector.first();
 assertTrue(titleAttribute.attr("title").endsWith("Test"));
 }

 @Test
 public void testParseAttributeWithValueMatching() {
 String attributeMatchingQuery = "[title~='Test']";
 QueryParser queryParser = new QueryParser(attributeMatchingQuery);
 Selector selector = queryParser.parse();
 Selector titleAttribute = selector.first();
 String title = titleAttribute.attr("title");
 assertTrue(Pattern.compile(".*Test.*").matcher(title).matches());
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidAttributeWithValueMatching() {
 String attributeMatchingInvalidQuery = "[title~=123]";
 QueryParser queryParser = new QueryParser(attributeMatchingInvalidQuery);
 queryParser.parse(); // this line should throw an exception
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidQuery() {
 String invalidQuery = "body > .testClass:has(123)";
 QueryParser queryParser = new QueryParser(invalidQuery);
 queryParser.parse(); // this line should throw an exception
 }

 @Test
 public void testParseNegatedSelector() {
 String negatedSelectorQuery = ":not(.testClass)";
 QueryParser queryParser = new QueryParser(negatedSelectorQuery);
 Selector selector = queryParser.parse();
 List<Selector> selectors = selector.getElementsByClass("testClass");
 assertTrue(selectors.isEmpty());
 }

 @Test
 public void testParseIndexSelector() {
 String indexSelectorQuery = "li:eq(1)";
 QueryParser queryParser = new QueryParser(indexSelectorQuery);
 Selector selector = queryParser.parse();
 Selector firstLi = selector.getElementsByTagName("li").get(0);
 assertEquals("li:eq(1)", selector.toString());
 assertEquals("li", firstLi.tagName());
 }

 @Test
 public void testParseIndexLessThanSelector() {
 String indexLessThanSelectorQuery = "li:lt(2)";
 QueryParser queryParser = new QueryParser(indexLessThanSelectorQuery);
 Selector selector = queryParser.parse();
 List<Selector> selectors = selector.getElementsByTagName("li");
 assertEquals(2, selectors.size());
 Selector firstLi = selectors.get(0);
 assertEquals("li:lt(2)", selector.toString());
 assertEquals("li", firstLi.tagName());
 }

 @Test
 public void testParseIndexGreaterThanSelector() {
 String indexGreaterThanSelectorQuery = "li:gt(1)";
 QueryParser queryParser = new QueryParser(indexGreaterThanSelectorQuery);
 Selector selector = queryParser.parse();
 List<Selector> selectors = selector.getElementsByTagName("li");
 assertEquals(2, selectors.size());
 Selector secondLi = selectors.get(1);
 assertEquals("li:gt(1)", selector.toString());
 assertEquals("li", secondLi.tagName());
 }

 @Test
 public void testParsePseudoClassSelector() {
 String pseudoClassSelectorQuery = "a:link";
 QueryParser queryParser = new QueryParser(pseudoClassSelectorQuery);
 Selector selector = queryParser.parse();
 List<Selector> selectors = selector.getElementsByTagName("a");
 assertEquals(2, selectors.size());
 Selector firstA = selectors.get(0);
 assertEquals("a:link", selector.toString());
 assertEquals("a", firstA.tagName());
 }

 @Test
 public void testParsePseudoElementSelector() {
 String pseudoElementSelectorQuery = "input:focus";
 QueryParser queryParser = new QueryParser(pseudoElementSelectorQuery);
 Selector selector = queryParser.parse();
 List<Selector> selectors = selector.getElementsByTagName("input");
 assertEquals(1, selectors.size());
 Selector firstInput = selectors.get(0);
 assertEquals("input:focus", selector.toString());
 assertEquals("input", firstInput.tagName());
 }

 @Test
 public void testParseComplexSelector() {
 String complexSelectorQuery = ".testClass1 > .testClass2 + .testClass3";
 QueryParser queryParser = new QueryParser(complexSelectorQuery);
 Selector selector = queryParser.parse();
 List<Selector> selectors = selector.getElementsByTagName("*");
 assertEquals(3, selectors.size());
 Selector testClass1 = selectors.get(0);
 Selector testClass2 = selectors.get(1);
 Selector testClass3 = selectors.get(2);
 assertEquals(".testClass1", testClass1.toString());
 assertEquals(">", testClass1.nextSibling());
 assertEquals(".testClass2", testClass2.toString());
 assertEquals("+", testClass2.nextSibling());
 assertEquals(".testClass3", testClass3.toString());
 }

 @Test
 public void testParseComplexSelectorWithAttributes() {
 String complexSelectorQueryWithAttributes = ".testClass1 > .testClass2[title='Test']:has(.testClass3)";
 QueryParser queryParser = new QueryParser(complexSelectorQueryWithAttributes);
 Selector selector = queryParser.parse();
 List<Selector> selectors = selector.getElementsByTagName("*");
 assertEquals(4, selectors.size());
 Selector testClass1 = selectors.get(0);
 Selector testClass2 = selectors.get(1);
 Selector titleAttribute = selectors.get(2);
 Selector testClass3 = selectors.get(3);
 assertEquals(".testClass1", testClass1.toString());
 assertEquals(">", testClass1.nextSibling());
 assertEquals(".testClass2", testClass2.toString());
 assertEquals("[title='Test']", titleAttribute.toString());
 assertEquals(":has(.testClass3)", titleAttribute.nextSibling());
 assertEquals(".testClass3", testClass3.toString());
 }
}