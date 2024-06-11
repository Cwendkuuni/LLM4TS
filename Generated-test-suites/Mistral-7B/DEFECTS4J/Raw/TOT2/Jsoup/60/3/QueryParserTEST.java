import org.jsoup.parser.QueryParser;
import org.jsoup.parser.Selector;
import org.jsoup.parser.TokenQueue;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

 private static QueryParser parseQuery(String query) {
 return QueryParser.parse(query);
 }

 @Test
 public void testParseEmptyQuery() {
 QueryParser queryParser = new QueryParser("");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof Evaluator.AllElements);
 }

 @Test
 public void testParseElementSelector() {
 QueryParser queryParser = new QueryParser("div");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.Tag.class, evaluator.getClass());
 assertEquals("div", evaluator.asTag().getName());
 }

 @Test
 public void testParseClassSelector() {
 QueryParser queryParser = new QueryParser(".class");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.Class.class, evaluator.getClass());
 assertEquals("class", evaluator.asClass().getName());
 }

 @Test
 public void testParseIdSelector() {
 QueryParser queryParser = new QueryParser("#id");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.Id.class, evaluator.getClass());
 assertEquals("id", evaluator.asId().getId());
 }

 @Test
 public void testParseAttributeSelector() {
 QueryParser queryParser = new QueryParser("[attribute]");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.Attribute.class, evaluator.getClass());
 assertEquals("attribute", evaluator.asAttribute().getKey());
 }

 @Test
 public void testParseAttributeWithValueSelector() {
 QueryParser queryParser = new QueryParser("[attribute=value]");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.AttributeWithValue.class, evaluator.getClass());
 assertEquals("attribute", evaluator.asAttribute().getKey());
 assertEquals("value", evaluator.asAttribute().getValue());
 }

 @Test
 public void testParseAttributeWithValueStartingSelector() {
 QueryParser queryParser = new QueryParser("[attribute^=value]");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.AttributeWithValueStarting.class, evaluator.getClass());
 assertEquals("attribute", evaluator.asAttribute().getKey());
 assertEquals("value", evaluator.asAttribute().getValue());
 }

 @Test
 public void testParseAttributeWithValueEndingSelector() {
 QueryParser queryParser = new QueryParser("[attribute$=value]");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.AttributeWithValueEnding.class, evaluator.getClass());
 assertEquals("attribute", evaluator.asAttribute().getKey());
 assertEquals("value", evaluator.asAttribute().getValue());
 }

 @Test
 public void testParseAttributeWithValueContainingSelector() {
 QueryParser queryParser = new QueryParser("[attribute*=value]");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.AttributeWithValueContaining.class, evaluator.getClass());
 assertEquals("attribute", evaluator.asAttribute().getKey());
 assertEquals("value", evaluator.asAttribute().getValue());
 }

 @Test
 public void testParseAttributeWithValueMatchingSelector() {
 QueryParser queryParser = new QueryParser("[attribute~=value]");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.AttributeWithValueMatching.class, evaluator.getClass());
 assertEquals("attribute", evaluator.asAttribute().getKey());
 assertEquals(Pattern.compile(value), evaluator.asAttribute().getPattern());
 }

 @Test
 public void testParseCombinatorSelectors() {
 String query = "div > p";
 QueryParser queryParser = new QueryParser(query);
 Evaluator evaluator = queryParser.parse();
 assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
 Evaluator[] elements = evaluator.asCombining().getElements();
 assertEquals(2, elements.length);
 assertEquals(Evaluator.Tag.class, elements[0].getClass());
 assertEquals("div", elements[0].asTag().getName());
 assertEquals(Evaluator.Tag.class, elements[1].getClass());
 assertEquals("p", elements[1].asTag().getName());
 }

 @Test
 public void testParseCombinatorSelectorsWithEmptyElement() {
 String query = "div > ";
 QueryParser queryParser = new QueryParser(query);
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.AllElements.class, evaluator.getClass());
 }

 @Test (expected = Selector.SelectorParseException.class)
 public void testParseUnknownCombinatorSelector() { {
 String query = "div >~ p";
 QueryParser queryParser = new QueryParser(query);queryParser.parse();
 }
 }

 @Test
 public void testParseNthChildSelectors() {
 String query = "div:nth-child(2)";
 QueryParser queryParser = new QueryParser(query);
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.IsNthChild.class, evaluator.getClass());
 assertEquals(2, evaluator.asNthChild().getIndex());
 }

 @Test
 public void testParseNthChildSelectorsWithOddKeyword() {
 String query = "div:nth-child(odd)";
 QueryParser queryParser = new QueryParser(query);
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.IsNthChild.class, evaluator.getClass());
 assertEquals(1, evaluator.asNthChild().getIndex() % 2);
 }

 @Test
 public void testParseNthChildSelectorsWithEvenKeyword() {
 String query = "div:nth-child(even)";
 QueryParser queryParser = new QueryParser(query);
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.IsNthChild.class, evaluator.getClass());
 assertEquals(0, evaluator.asNthChild().getIndex() % 2);
 }

 @Test
 public void testParseNthLastChildSelectors() {
 String query = "div:nth-last-child(2)";
 QueryParser queryParser = new QueryParser(query);
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.IsNthLastChild.class, evaluator.getClass());
 assertEquals(2, evaluator.asNthLastChild().getIndex());
 }

 @Test
 public void testParseNthLastChildSelectorsWithOddKeyword() {
 String query = "div:nth-last-child(odd)";
 QueryParser queryParser = new QueryParser(query);
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.IsNthLastChild.class, evaluator.getClass());
 assertEquals(1, evaluator.asNthLastChild().getIndex() % 2);
 }

 @Test
 public void testParseNthLastChildSelectorsWithEvenKeyword() {
 String query = "div:nth-last-child(even)";
 QueryParser queryParser = new QueryParser(query);
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.IsNthLastChild.class, evaluator.getClass());
 assertEquals(0, evaluator.asNthLastChild().getIndex() % 2);
 }

 @Test
 public void testParseNthOfTypeSelectors() {
 String query = "div:nth-of-type(2)";
 QueryParser queryParser = new QueryParser(query);
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.IsNthOfType.class, evaluator.getClass());
 assertEquals(2, evaluator.asNthOfType().getTypeIndex());
 }

 @Test
 public void testParseNthLastOfTypeSelectors() {
 String query = "div:nth-last-of-type(2)";
 QueryParser queryParser = new QueryParser(query);
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.IsNthLastOfType.class, evaluator.getClass());
 assertEquals(2, evaluator.asNthLastOfType().getTypeIndex());
 }

 @Test
 public void testParseHasSelector() {
 String query = "div:has(p)";
 QueryParser queryParser = new QueryParser(query);
 Evaluator evaluator = queryParser.parse();
 assertEquals(StructuralEvaluator.Has.class, evaluator.getClass());
 assertEquals(evaluator.asHas().getSelector(), new QueryParser("p").parse());
 }

 @Test
 public void testParseContainsSelector() {
 String query = "div:contains(text)";
 QueryParser queryParser = new QueryParser(query);
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.ContainsText.class, evaluator.getClass());
 assertEquals("text", evaluator.asContainsText().getText());
 }

 @Test
 public void testParseContainsOwnSelector() {
 String query = "div:containsOwn(text)";
 QueryParser queryParser = new QueryParser(query);
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.ContainsOwnText.class, evaluator.getClass());
 assertEquals("text", evaluator.asContainsOwnText().getText());
 }

 @Test
 public void testParseContainsDataSelector() {
 String query = "div:containsData(data)";
 QueryParser queryParser = new QueryParser(query);
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.ContainsData.class, evaluator.getClass());
 assertEquals("data", evaluator.asContainsData().getData());
 }

 @Test
 public void testParseNotSelector() {
 String query = "div:not(p)";
 QueryParser queryParser = new QueryParser(query);
 Evaluator evaluator = queryParser.parse();
 assertEquals(StructuralEvaluator.Not.class, evaluator.getClass());
 assertEquals(evaluator.asNot().getSelector(), new QueryParser("p").parse());
 }

 @Test (expected = Selector.SelectorParseException.class)
 public void testParseInvalidSelector() {
 String query = "div:doesnotexist";
 QueryParser queryParser = new QueryParser(query);queryParser.parse();
 }

}