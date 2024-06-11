import org.jsoup.parser.QueryParser;
import org.jsoup.parser.TokenQueue;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class QueryParserTest {

 @Test
 public void testParseEmptyQuery() {
 QueryParser queryParser = new QueryParser("");
 Evaluator evaluator = queryParser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseTagSelector() {
 QueryParser queryParser = new QueryParser("div");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.Tag.class, evaluator.getClass());
 assertEquals("div", evaluator.toString());
 }

 @Test
 public void testParseClassSelector() {
 QueryParser queryParser = new QueryParser(".class");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.Class.class, evaluator.getClass());
 assertEquals(".class", evaluator.toString());
 }

 @Test
 public void testParseIdSelector() {
 QueryParser queryParser = new QueryParser("#id");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.Id.class, evaluator.getClass());
 assertEquals("#id", evaluator.toString());
 }

 @Test
 public void testParseElementWithAttribute() {
 QueryParser queryParser = new QueryParser("input[type=text]");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.CombiningEvaluator.class, evaluator.getClass());
 assertNotNull(evaluator.getLeft());
 assertNotNull(evaluator.getRight());
 assertEquals(Evaluator.Tag.class, evaluator.getLeft().getClass());
 assertEquals("input", evaluator.getLeft().toString());
 assertEquals(Evaluator.Attribute.class, evaluator.getRight().getClass());
 assertEquals("type=text", evaluator.getRight().toString());
 }

 @Test
 public void testParseElementWithAttributeValue() {
 QueryParser queryParser = new QueryParser("input[type='text']");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.CombiningEvaluator.class, evaluator.getClass());
 assertNotNull(evaluator.getLeft());
 assertNotNull(evaluator.getRight());
 assertEquals(Evaluator.Tag.class, evaluator.getLeft().getClass());
 assertEquals("input", evaluator.getLeft().toString());
 assertEquals(Evaluator.AttributeWithValue.class, evaluator.getRight().getClass());
 assertEquals("type='text'", evaluator.getRight().toString());
 }

 @Test
 public void testParseElementWithAttributeValueStarting() {
 QueryParser queryParser = new QueryParser("input[type^='text']");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.CombiningEvaluator.class, evaluator.getClass());
 assertNotNull(evaluator.getLeft());
 assertNotNull(evaluator.getRight());
 assertEquals(Evaluator.AttributeWithValueStarting.class, evaluator.getRight().getClass());
 assertEquals("type^='text'", evaluator.getRight().toString());
 }

 @Test
 public void testParseElementWithAttributeValueEnding() {
 QueryParser queryParser = new QueryParser("input[type$='text']");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.CombiningEvaluator.class, evaluator.getClass());
 assertNotNull(evaluator.getLeft());
 assertNotNull(evaluator.getRight());
 assertEquals(Evaluator.AttributeWithValueEnding.class, evaluator.getRight().getClass());
 assertEquals("type$='text'", evaluator.getRight().toString());
 }

 @Test
 public void testParseElementWithAttributeValueContaining() {
 QueryParser queryParser = new QueryParser("input[type*='text']");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.CombiningEvaluator.class, evaluator.getClass());
 assertNotNull(evaluator.getLeft());
 assertNotNull(evaluator.getRight());
 assertEquals(Evaluator.AttributeWithValueContaining.class, evaluator.getRight().getClass());
 assertEquals("type*='text'", evaluator.getRight().toString());
 }

 @Test
 public void testParseElementWithAttributeValueMatching() {
 QueryParser queryParser = new QueryParser("input[type~='text']");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.CombiningEvaluator.class, evaluator.getClass());
 assertNotNull(evaluator.getLeft());
 assertNotNull(evaluator.getRight());
 assertEquals(Evaluator.AttributeWithValueMatching.class, evaluator.getRight().getClass());
 assertEquals("type~='text'", evaluator.getRight().toString());
 }

 @Test
 public void testParseDescendantSelector() {
 QueryParser queryParser = new QueryParser("div.class p");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.CombiningEvaluator.class, evaluator.getClass());
 assertNotNull(evaluator.getLeft());
 assertNotNull(evaluator.getRight());
 assertEquals(Evaluator.Tag.class, evaluator.getLeft().getClass());
 assertEquals("div", evaluator.getLeft().toString());
 assertEquals(Evaluator.CombiningEvaluator.class, evaluator.getRight().getClass());
 assertNotNull(evaluator.getRight().getLeft());
 assertNotNull(evaluator.getRight().getRight());
 assertEquals(Evaluator.Tag.class, evaluator.getRight().getLeft().getClass());
 assertEquals("p", evaluator.getRight().getLeft().toString());
 assertEquals(Evaluator.Class.class, evaluator.getRight().getRight().getClass());
 assertEquals(".class", evaluator.getRight().getRight().toString());
 }

 @Test
 public void testParseCombinatorSelector() {
 QueryParser queryParser = new QueryParser("input > form input");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.CombiningEvaluator.class, evaluator.getClass());
 assertNotNull(evaluator.getLeft());
 assertNotNull(evaluator.getRight());
 assertEquals(Evaluator.Tag.class, evaluator.getLeft().getClass());
 assertEquals("input", evaluator.getLeft().toString());
 assertEquals(Evaluator.CombiningEvaluator.class, evaluator.getRight().getClass());
 assertNotNull(evaluator.getRight().getLeft());
 assertNotNull(evaluator.getRight().getRight());
 assertEquals(Evaluator.Tag.class, evaluator.getRight().getLeft().getClass());
 assertEquals("form", evaluator.getRight().getLeft().toString());
 assertEquals(Evaluator.Tag.class, evaluator.getRight().getRight().getClass());
 assertEquals("input", evaluator.getRight().getRight().toString());
 }

 @Test
 public void testParseAttributeSelectorWithSingleQuotes() {
 QueryParser queryParser = new QueryParser("input[title='Text']");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.CombiningEvaluator.class, evaluator.getClass());
 assertNotNull(evaluator.getLeft());
 assertNotNull(evaluator.getRight());
 assertEquals(Evaluator.Tag.class, evaluator.getLeft().getClass());
 assertEquals("input", evaluator.getLeft().toString());
 assertEquals(Evaluator.Attribute.class, evaluator.getRight().getClass());
 assertEquals("title='Text'", evaluator.getRight().toString());
 }

 @Test
 public void testParseAttributeSelectorWithDoubleQuotes() {
 QueryParser queryParser = new QueryParser("input[title=\"Text\"]");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.CombiningEvaluator.class, evaluator.getClass());
 assertNotNull(evaluator.getLeft());
 assertNotNull(evaluator.getRight());
 assertEquals(Evaluator.Tag.class, evaluator.getLeft().getClass());
 assertEquals("input", evaluator.getLeft().toString());
 assertEquals(Evaluator.Attribute.class, evaluator.getRight().getClass());
 assertEquals("title=\"Text\"", evaluator.getRight().toString());
 }

 @Test
 public void testParseAttributeSelectorWithNoValue() {
 QueryParser queryParser = new QueryParser("input[title]");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.CombiningEvaluator.class, evaluator.getClass());
 assertNotNull(evaluator.getLeft());
 assertNotNull(evaluator.getRight());
 assertEquals(Evaluator.Tag.class, evaluator.getLeft().getClass());
 assertEquals("input", evaluator.getLeft().toString());
 assertEquals(Evaluator.Attribute.class, evaluator.getRight().getClass());
 assertEquals("title", evaluator.getRight().toString());
 }

 @Test
 public void testParseAttributeSelectorWithMultipleAttributes() {
 QueryParser queryParser = new QueryParser("input[title='Text' class='class']");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.CombiningEvaluator.class, evaluator.getClass());
 assertNotNull(evaluator.getLeft());
 assertNotNull(evaluator.getRight());
 assertEquals(Evaluator.Tag.class, evaluator.getLeft().getClass());
 assertEquals("input", evaluator.getLeft().toString());
 assertEquals(Evaluator.CombiningEvaluator.class, evaluator.getRight().getClass());
 assertNotNull(evaluator.getRight().getLeft());
 assertNotNull(evaluator.getRight().getRight());
 assertEquals(Evaluator.AttributeWithValue.class, evaluator.getRight().getLeft().getClass());
 assertEquals("title='Text'", evaluator.getRight().getLeft().toString());
 assertEquals(Evaluator.Class.class, evaluator.getRight().getRight().getClass());
 assertEquals(".class", evaluator.getRight().getRight().toString());
 }

 @Test
 public void testParseNegatedAttributeSelector() {
 QueryParser queryParser = new QueryParser("input:not([title])");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.StructuralEvaluator.class, evaluator.getClass());
 assertEquals(StructuralEvaluator.Not.class, evaluator.getClass());
 assertNotNull(evaluator.getLeft());
 assertEquals(Evaluator.Tag.class, evaluator.getLeft().getClass());
 assertEquals("input", evaluator.getLeft().toString());
 }

 @Test
 public void testParsePseudoClassSelector() {
 QueryParser queryParser = new QueryParser("input:first-child");
 Evaluator evaluator = queryParser.parse();
 assertEquals(StructuralEvaluator.class, evaluator.getClass());
 assertEquals(StructuralEvaluator.FirstChild.class, evaluator.getClass());
 }

 @Test
 public void testParsePseudoClassSelectorWithArguments() {
 QueryParser queryParser = new QueryParser("input:checked:enabled");
 Evaluator evaluator = queryParser.parse();
 assertEquals(StructuralEvaluator.class, evaluator.getClass());
 assertEquals(StructuralEvaluator.CombiningEvaluator.class, evaluator.getClass());
 assertNotNull(evaluator.getLeft());
 assertNotNull(evaluator.getRight());
 assertEquals(StructuralEvaluator.Checked.class, evaluator.getLeft().getClass());
 assertEquals(StructuralEvaluator.Enabled.class, evaluator.getRight().getClass());
 }

 @Test
 public void testParseCombinatorWithMoreThanTwoSelectors() {
 QueryParser queryParser = new QueryParser("input > form > div.class");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.CombiningEvaluator.class, evaluator.getClass());
 assertNotNull(evaluator.getLeft());
 assertNotNull(evaluator.getRight());
 assertEquals(Evaluator.Tag.class, evaluator.getLeft().getClass());
 assertEquals("input", evaluator.getLeft().toString());
 assertEquals(Evaluator.CombiningEvaluator.class, evaluator.getRight().getClass());
 assertNotNull(evaluator.getRight().getLeft());
 assertNotNull(evaluator.getRight().getRight());
 assertEquals(Evaluator.Tag.class, evaluator.getRight().getLeft().getClass());
 assertEquals("form", evaluator.getRight().getLeft().toString());
 assertEquals(Evaluator.Class.class, evaluator.getRight().getRight().getClass());
 assertEquals(".class", evaluator.getRight().getRight().toString());
 }

 @Test
 public void testParseComplexSelector() {
 QueryParser queryParser = new QueryParser("input:checked > form > div.class:not([title='This title']) input:enabled");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.CombiningEvaluator.class, evaluator.getClass());
 assertNotNull(evaluator.getLeft());
 assertEquals(Evaluator.CombiningEvaluator.class, evaluator.getLeft().getClass());
 assertNotNull(evaluator.getLeft().getLeft());
 assertNotNull(evaluator.getLeft().getRight());
 assertEquals(StructuralEvaluator.Checked.class, evaluator.getLeft().getLeft().getClass());
 assertEquals(StructuralEvaluator.Enabled.class, evaluator.getLeft().getRight().getClass());
 assertNotNull(evaluator.getRight());
 assertEquals(Evaluator.Tag.class, evaluator.getRight().getClass());
 assertEquals("input", evaluator.getRight().toString());
 }

 @Test
 public void testParseComplexSelectorWithSubSelectors() {
 QueryParser queryParser = new QueryParser("input:checked > form > div.class > input:enabled");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.CombiningEvaluator.class, evaluator.getClass());
 assertNotNull(evaluator.getLeft());
 assertEquals(Evaluator.CombiningEvaluator.class, evaluator.getLeft().getClass());
 assertNotNull(evaluator.getLeft().getLeft());
 assertEquals(StructuralEvaluator.Checked.class, evaluator.getLeft().getLeft().getClass());
 assertNotNull(evaluator.getLeft().getRight());
 assertEquals(Evaluator.Tag.class, evaluator.getLeft().getRight().getClass());
 assertEquals("form", evaluator.getLeft().getRight().toString());
 assertNotNull(evaluator.getRight());
 assertEquals(Evaluator.Tag.class, evaluator.getRight().getClass());
 assertEquals("input", evaluator.getRight().toString());
 }
}