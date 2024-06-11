import org.junit.Test;
import org.jsoup.select.QueryParser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class QueryParserTest {

 private static Evaluator parseQuery(String query) {
 return QueryParser.parse(query);
 }

 @Test
 public void testParseEmptyQuery() {
 QueryParser queryParser = new QueryParser("");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.class, evaluator.getClass());
 assertNotNull(evaluator);
 }

 @Test
 public void testParseTagQuery() {
 QueryParser queryParser = new QueryParser("div");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.Tag.class, evaluator.getClass());
 assertEquals("div", evaluator.toString());
 }

 @Test
 public void testParseClassQuery() {
 QueryParser queryParser = new QueryParser(".class");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.Class.class, evaluator.getClass());
 assertEquals("class", evaluator.toString());
 }

 @Test
 public void testParseIDQuery() {
 QueryParser queryParser = new QueryParser("#id");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.Id.class, evaluator.getClass());
 assertEquals("id", evaluator.toString());
 }

 @Test
 public void testParseAttributeQuery() {
 QueryParser queryParser = new QueryParser("[title]");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.Attribute.class, evaluator.getClass());
 assertEquals("[title]", evaluator.toString());
 }

 @Test
 public void testParseAttributeWithValueQuery() {
 QueryParser queryParser = new QueryParser("[title='title']");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.AttributeWithValue.class, evaluator.getClass());
 assertEquals("[title='title']", evaluator.toString());
 }

 @Test
 public void testParseAttributeWithValueNotQuery() {
 QueryParser queryParser = new QueryParser("[title!='title']");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.AttributeWithValueNot.class, evaluator.getClass());
 assertEquals("[title!='title']", evaluator.toString());
 }

 @Test
 public void testParseAttributeStartingQuery() {
 QueryParser queryParser = new QueryParser("[title^='title']");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.AttributeWithValueStarting.class, evaluator.getClass());
 assertEquals("[title^='title']", evaluator.toString());
 }

 @Test
 public void testParseAttributeEndingQuery() {
 QueryParser queryParser = new QueryParser("[title$='title']");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.AttributeWithValueEnding.class, evaluator.getClass());
 assertEquals("[title$='title']", evaluator.toString());
 }

 @Test
 public void testParseAttributeContainingQuery() {
 QueryParser queryParser = new QueryParser("[title*='title']");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.AttributeWithValueContaining.class, evaluator.getClass());
 assertEquals("[title*='title']", evaluator.toString());
 }

 @Test
 public void testParseAttributeMatchingQuery() {
 QueryParser queryParser = new QueryParser("[title~='title']");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.AttributeWithValueMatching.class, evaluator.getClass());
 assertEquals("[title~='title']", evaluator.toString());
 }

 @Test
 public void testParseCombinedQueries() {
 QueryParser queryParser = new QueryParser("div.class#id > ul > li:nth-child(2)");
 Evaluator evaluator = queryParser.parse();
 assertNotNull(evaluator);
 assertEquals("div > ul > li:nth-child(2)", evaluator.toString());
 }

 @Test (expected = Selector.SelectorParseException.class)
 public void testParseInvalidQuery() {
 QueryParser queryParser = new QueryParser("div#class"); // class should be in quotes
 Evaluator evaluator = queryParser.parse();
 fail("Expected Selector.SelectorParseException");
 }

}