import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.QueryParser;
import org.jsoup.parser.TokenQueue;
import org.jsoup.parser.Selector;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class QueryParserTest {

 private QueryParser queryParser;
 private String query;

 @Before
 public void setUp() {
 queryParser = new QueryParser("");
 query = "";
 }

 @After
 public void tearDown() {
 queryParser = null;
 query = null;
 }

 @Test
 public void testParseEmptyQuery() {
 Evaluator evaluator = queryParser.parse(query);
 assertNull(evaluator);
 }

 @Test
 public void testParseRootQuery() {
 query = "*";
 Evaluator evaluator = queryParser.parse(query);
 assertEquals(Evaluator.class, evaluator.getClass());
 }

 @Test
 public void testParseElementSelector() {
 query = "div";
 Evaluator evaluator = queryParser.parse(query);
 assertEquals(Evaluator.Tag.class, evaluator.getClass());
 }

 @Test
 public void testParseClassSelector() {
 query = ".class";
 Evaluator evaluator = queryParser.parse(query);
 assertEquals(Evaluator.Class.class, evaluator.getClass());
 }

 @Test
 public void testParseIDSelector() {
 query = "#id";
 Evaluator evaluator = queryParser.parse(query);
 assertEquals(Evaluator.Id.class, evaluator.getClass());
 }

 @Test
 public void testParseAttributeSelector() {
 query = "[attribute]";
 Evaluator evaluator = queryParser.parse(query);
 assertEquals(Evaluator.Attribute.class, evaluator.getClass());
 }

 @Test
 public void testParseAttributeValueSelector() {
 query = "[attribute=value]";
 Evaluator evaluator = queryParser.parse(query);
 assertEquals(Evaluator.AttributeWithValue.class, evaluator.getClass());
 }

 @Test
 public void testParseAttributeValueStartingSelector() {
 query = "[attribute^='value']";
 Evaluator evaluator = queryParser.parse(query);
 assertEquals(Evaluator.AttributeWithValueStarting.class, evaluator.getClass());
 }

 @Test
 public void testParseAttributeValueEndingSelector() {
 query = "[attribute$='value']";
 Evaluator evaluator = queryParser.parse(query);
 assertEquals(Evaluator.AttributeWithValueEnding.class, evaluator.getClass());
 }

 @Test
 public void testParseAttributeValueContainingSelector() {
 query = "[attribute*='value']";
 Evaluator evaluator = queryParser.parse(query);
 assertEquals(Evaluator.AttributeWithValueContaining.class, evaluator.getClass());
 }

 @Test
 public void testParseAttributeValueMatchingSelector() {
 query = "[attribute~='value']";
 Evaluator evaluator = queryParser.parse(query);
 assertEquals(Evaluator.AttributeWithValueMatching.class, evaluator.getClass());
 }

 @Test
 public void testParseDescendantSelector() {
 query = "div > p";
 Evaluator evaluator = queryParser.parse(query);
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 assertEquals(Evaluator.Tag.class, ((CombiningEvaluator.And) evaluator).getLeft().getClass());
 assertEquals(Evaluator.Tag.class, ((CombiningEvaluator.And) evaluator).getRight().getClass());
 }

 @Test
 public void testParseDescendantSelectorWithClass() {
 query = "div > p.class";
 Evaluator evaluator = queryParser.parse(query);
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 assertEquals(Evaluator.Tag.class, ((CombiningEvaluator.And) evaluator).getLeft().getClass());
 assertEquals(Evaluator.Class.class, ((CombiningEvaluator.And) evaluator).getRight().getClass());
 }

 @Test
 public void testParseDescendantSelectorWithAttribute() {
 query = "div > p[attribute]";
 Evaluator evaluator = queryParser.parse(query);
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 assertEquals(Evaluator.Tag.class, ((CombiningEvaluator.And) evaluator).getLeft().getClass());
 assertEquals(Evaluator.Attribute.class, ((CombiningEvaluator.And) evaluator).getRight().getClass());
 }

 @Test
 public void testParseDescendantSelectorWithID() {
 query = "div > p#id";
 Evaluator evaluator = queryParser.parse(query);
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 assertEquals(Evaluator.Tag.class, ((CombiningEvaluator.And) evaluator).getLeft().getClass());
 assertEquals(Evaluator.Id.class, ((CombiningEvaluator.And) evaluator).getRight().getClass());
 }

 @Test
 public void testParseSiblingSelector() {
 query = "div > p + p";
 Evaluator evaluator = queryParser.parse(query);
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 assertEquals(Evaluator.Tag.class, ((CombiningEvaluator.And) evaluator).getLeft().getClass());
 assertEquals(StructuralEvaluator.ImmediatePreviousSibling.class, ((CombiningEvaluator.And) evaluator).getRight().getClass());
 }

 @Test
 public void testParseSiblingSelectorWithClass() {
 query = "div > p + p.class";
 Evaluator evaluator = queryParser.parse(query);
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 assertEquals(Evaluator.Tag.class, ((CombiningEvaluator.And) evaluator).getLeft().getClass());
 assertEquals(StructuralEvaluator.ImmediatePreviousSibling.class, ((CombiningEvaluator.And) evaluator).getRight().getClass());
 assertEquals(Evaluator.Class.class, ((StructuralEvaluator.ImmediatePreviousSibling) ((CombiningEvaluator.And) evaluator).getRight()).getRight().getClass());
 }

 @Test
 public void testParseChildSelector() {
 query = "div > p > span";
 Evaluator evaluator = queryParser.parse(query);
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 assertEquals(Evaluator.Tag.class, ((CombiningEvaluator.And) evaluator).getLeft().getClass());
 assertEquals(Evaluator.Tag.class, ((CombiningEvaluator.And) evaluator).getRight().getClass());
 }

 @Test
 public void testParseChildSelectorWithAttribute() {
 query = "div > p > span[attribute]";
 Evaluator evaluator = queryParser.parse(query);
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 assertEquals(Evaluator.Tag.class, ((CombiningEvaluator.And) evaluator).getLeft().getClass());
 assertEquals(Evaluator.Tag.class, ((CombiningEvaluator.And) evaluator).getRight().getClass());
 assertEquals(Evaluator.Attribute.class, ((CombiningEvaluator.And) evaluator).getRight().getLeft().getClass());
 }

 @Test
 public void testParseChildSelectorWithID() {
 query = "div > p > span#id";
 Evaluator evaluator = queryParser.parse(query);
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 assertEquals(Evaluator.Tag.class, ((CombiningEvaluator.And) evaluator).getLeft().getClass());
 assertEquals(Evaluator.Tag.class, ((CombiningEvaluator.And) evaluator).getRight().getClass());
 assertEquals(Evaluator.Id.class, ((CombiningEvaluator.And) evaluator).getRight().getLeft().getClass());
 }

 @Test
 public void testParseAttributeWithValueSelector() {
 query = "[attribute='value']";
 Evaluator evaluator = queryParser.parse(query);
 assertEquals(Evaluator.AttributeWithValue.class, evaluator.getClass());
 }

 @Test
 public void testParseAttributeWithValueStartingSelector() {
 query = "[attribute^='value']";
 Evaluator evaluator = queryParser.parse(query);
 assertEquals(Evaluator.AttributeWithValueStarting.class, evaluator.getClass());
 }

 @Test
 public void testParseAttributeWithValueEndingSelector() {
 query = "[attribute$='value']";
 Evaluator evaluator = queryParser.parse(query);
 assertEquals(Evaluator.AttributeWithValueEnding.class, evaluator.getClass());
 }

 @Test
 public void testParseAttributeWithValueContainingSelector() {
 query = "[attribute*='value']";
 Evaluator evaluator = queryParser.parse(query);
 assertEquals(Evaluator.AttributeWithValueContaining.class, evaluator.getClass());
 }

 @Test
 public void testParseAttributeWithValueMatchingSelector() {
 query = "[attribute~='value']";
 Evaluator evaluator = queryParser.parse(query);
 assertEquals(Evaluator.AttributeWithValueMatching.class, evaluator.getClass());
 }

 @Test
 public void testParseNegatedAttributeSelector() {
 query = "[not(attribute)]";
 Evaluator evaluator = queryParser.parse(query);
 assertEquals(StructuralEvaluator.Not.class, evaluator.getClass());
 assertEquals(Evaluator.Attribute.class, ((StructuralEvaluator.Not) evaluator).getAttributeSelector().getClass());
 }

 @Test
 public void testParseNegatedElementSelector() {
 query = ":not(div)";
 Evaluator evaluator = queryParser.parse(query);
 assertEquals(StructuralEvaluator.Not.class, evaluator.getClass());
 assertEquals(Evaluator.Tag.class, ((StructuralEvaluator.Not) evaluator).getTagSelector().getClass());
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidQuery() {
 query = "invalid";
 assertThrows(Selector.SelectorParseException.class, () -> queryParser.parse(query));
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidQueryWithUnknownToken() {
 query = "unknownToken";
 assertThrows(Selector.SelectorParseException.class, () -> queryParser.parse(query));
 }
}