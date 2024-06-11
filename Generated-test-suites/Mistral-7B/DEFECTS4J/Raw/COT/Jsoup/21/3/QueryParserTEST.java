import org.junit.Assert;
import org.junit.Test;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.jsoup.helper.StringUtil;

public class QueryParserTest {

 private String query;
 private QueryParser parser;
 private Evaluator result;

 @Test
 public void testParse_EmptyQuery() {
 query = "";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertNull(result);
 }

 @Test
 public void testParse_SimpleTag() {
 query = "div";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertEquals(Evaluator.Tag.class, result.getClass());
 Assert.assertEquals("div", ((Evaluator.Tag) result).getName());
 }

 @Test
 public void testParse_ClassSelector() {
 query = ".class";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertEquals(Evaluator.Class.class, result.getClass());
 Assert.assertEquals("class", ((Evaluator.Class) result).getCss());
 }

 @Test
 public void testParse_IDSelector() {
 query = "#id";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertEquals(Evaluator.Id.class, result.getClass());
 Assert.assertEquals("id", ((Evaluator.Id) result).getId());
 }

 @Test
 public void testParse_AttributeSelector() {
 query = "[attribute]";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertEquals(Evaluator.Attribute.class, result.getClass());
 Assert.assertEquals("attribute", result.toString());
 }

 @Test
 public void testParse_AttributeWithValueSelector() {
 query = "[attribute=value]";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertEquals(Evaluator.AttributeWithValue.class, result.getClass());
 Assert.assertEquals("attribute", result.getAttributeName());
 Assert.assertEquals("value", result.getValue());
 }

 @Test
 public void testParse_AttributeStartingSelector() {
 query = "[attribute^=value]";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertEquals(Evaluator.AttributeWithValueStarting.class, result.getClass());
 Assert.assertEquals("attribute", result.getAttributeName());
 Assert.assertEquals("value", result.getValue());
 }

 @Test
 public void testParse_AttributeEndingSelector() {
 query = "[attribute$=value]";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertEquals(Evaluator.AttributeWithValueEnding.class, result.getClass());
 Assert.assertEquals("attribute", result.getAttributeName());
 Assert.assertEquals("value", result.getValue());
 }

 @Test
 public void testParse_AttributeContainingSelector() {
 query = "[attribute*=value]";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertEquals(Evaluator.AttributeWithValueContaining.class, result.getClass());
 Assert.assertEquals("attribute", result.getAttributeName());
 Assert.assertEquals("value", result.getValue());
 }

 @Test
 public void testParse_AttributeMatchingSelector() {
 query = "[attribute~=value]";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertEquals(Evaluator.AttributeWithValueMatching.class, result.getClass());
 Assert.assertEquals("attribute", result.getAttributeName());
 Assert.assertEquals(Pattern.class, result.getValue().getClass());
 }

 @Test
 public void testParse_CombinedSelectors() {
 query = "div.class";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertEquals(2, result.size());
 Assert.assertTrue(result.get(0) instanceof Evaluator.Tag);
 Assert.assertTrue(result.get(1) instanceof Evaluator.Class);
 }

 @Test
 public void testParse_SubSelectors() {
 query = "div > p.class";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertEquals(3, result.size());
 Assert.assertTrue(result.get(0) instanceof Evaluator.Tag);
 Assert.assertTrue(result.get(1) instanceof Evaluator.Tag);
 Assert.assertTrue(result.get(2) instanceof Evaluator.Class);
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParse_InvalidCombinator() {
 query = "div % p";
 parser = new QueryParser(query);
 parser.parse();
 }

 @Test
 public void testParse_NegatedSelector() {
 query = "div:not(p)";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertEquals(1, result.size());
 Assert.assertTrue(result.getClass() instanceof StructuralEvaluator.Not);
 }

 @Test
 public void testParse_PseudoClasses() {
 query = "div:lt(2)";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertEquals(1, result.size());
 Assert.assertTrue(result.getClass() instanceof Evaluator.IndexLessThan);
 Assert.assertEquals(2, ((Evaluator.IndexLessThan) result).getIndex());
 }

 @Test
 public void testParse_PseudoElements() {
 query = "div:first-child";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertEquals(1, result.size());
 Assert.assertTrue(result.getClass() instanceof StructuralEvaluator.FirstChild);
 }

 @Test
 public void testParse_ComplexQuery() {
 query = "div.class1.class2 > ul > li > a[href^='https']:not([href^='https://example.com'])";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertEquals(6, result.size());
 }
}