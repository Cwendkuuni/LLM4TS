import org.junit.Test;
import org.jsoup.parser.QueryParser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class QueryParserTest {

 @Test
 public void testParseSimpleSelector() {
 String query = "div.class1";
 Evaluator evaluator = QueryParser.parse(query);

 assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
 assertEquals(Evaluator.Class.class, evaluator.getClassType());
 assertEquals("class1", evaluator.getAttributeValue("class"));
 }

 @Test
 public void testParseComplexSelector() {
 String query = "div > span:first-child.class2 > ul:last-child > li:containsOwn(text)";
 Evaluator evaluator = QueryParser.parse(query);

 assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
 Evaluator parent = evaluator;
 while (parent.getNextSibling() != null) {
 parent = parent.getNextSibling();
 }
 Evaluator li = parent;

 assertEquals("li", li.getTagName());
 assertEquals("text", li.getContainsOwnText().getValue());
 }

 @Test
 public void testParseCombinedSelectors() {
 String query = "div.class1 > div > span.class2";
 Evaluator evaluator = QueryParser.parse(query);

 assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
 Evaluator parent1 = evaluator;
 while (parent1.getNextSibling() != null) {
 parent1 = parent1.getNextSibling();
 }
 Evaluator parent2 = parent1;
 while (parent2.getPreviousSibling() != null) {
 parent2 = parent2.getPreviousSibling();
 }
 Evaluator span = parent2;

 assertEquals("span", span.getTagName());
 assertEquals("class2", span.getClassValue());
 }

 @Test
 public void testParseAttributeSelectors() {
 String query = "input[type='text']";
 Evaluator evaluator = QueryParser.parse(query);

 assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
 Evaluator input = evaluator;

 assertEquals("input", input.getTagName());
 assertEquals("text", input.getAttributeValue("type"));
 }

 @Test
 public void testParsePseudoClassSelectors() {
 String query = "div.class1:first-child";
 Evaluator evaluator = QueryParser.parse(query);

 assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
 Evaluator firstChild = evaluator;
 Evaluator parent = firstChild.getParent();

 assertEquals("div", parent.getTagName());
 assertEquals("first-child", firstChild.getPseudoClass());
 }

 @Test
 public void testParseNegativePseudoClassSelectors() {
 String query = "div:not(.class1)";
 Evaluator evaluator = QueryParser.parse(query);

 assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
 Evaluator notClass1 = evaluator;
 Evaluator parent = notClass1.getParent();

 assertEquals("div", parent.getTagName());
 assertEquals("not", notClass1.getPseudoClass());
 assertEquals("class1", notClass1.getNegatedClassValue());
 }

 @Test
 public void testParseSubselectors() {
 String query = "div > ul > li:contains(apple) > span:first-child";
 Evaluator evaluator = QueryParser.parse(query);

 assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
 Evaluator span = evaluator;
 Evaluator parent = span.getParent();
 Evaluator li = parent;
 Evaluator ul = li.getParent();
 Evaluator div = ul.getParent();

 assertEquals("span", span.getTagName());
 assertEquals("first-child", span.getPseudoClass());
 assertEquals("apple", li.getContainsText().getValue());
 assertEquals("li", li.getTagName());
 assertEquals("ul", ul.getTagName());
 assertEquals("div", div.getTagName());
 }

 @Test
 public void testParseMultipleSubselectors() {
 String query = "div > ul > li:contains(apple) > span:first-child > a:contains(orange)";
 Evaluator evaluator = QueryParser.parse(query);

 assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
 Evaluator a = evaluator;
 Evaluator span = a.getParent();
 Evaluator li = span.getParent();
 Evaluator ul = li.getParent();
 Evaluator div = ul.getParent();

 assertEquals("a", a.getTagName());
 assertEquals("contains", a.getContainsText().getOperation());
 assertEquals("orange", a.getContainsText().getValue());
 assertEquals("span", span.getTagName());
 assertEquals("first-child", span.getPseudoClass());
 assertEquals("apple", li.getContainsText().getValue());
 assertEquals("li", li.getTagName());
 assertEquals("ul", ul.getTagName());
 assertEquals("div", div.getTagName());
 }

 @Test
 public void testParseMultiplePseudoClassSelectors() {
 String query = "div.class1:not(.class2):hover";
 Evaluator evaluator = QueryParser.parse(query);

 assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
 Evaluator hover = evaluator;
 Evaluator notClass2 = hover.getParent();
 Evaluator class1 = notClass2.getParent();

 assertEquals("div", class1.getTagName());
 assertEquals("class1", class1.getClassValue());
 assertEquals("not", notClass2.getPseudoClass());
 assertEquals("class2", notClass2.getNegatedClassValue());
 assertEquals("hover", hover.getPseudoClass());
 }
}