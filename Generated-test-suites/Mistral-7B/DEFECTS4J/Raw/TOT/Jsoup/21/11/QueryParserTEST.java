import org.jsoup.parser.TokenQueue;
import org.jsoup.parser.Parser;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class QueryParserTest {

 @Test
 public void testParseSimpleSelector() {
 String query = "div.class1";
 Evaluator evaluator = QueryParser.parse(query);
 assertEquals("class1", evaluator.getClassSelector());
 assertNotNull(evaluator.getTagNameSelector());
 }

 @Test
 public void testParseComplexSelector() {
 String query = "div.class1 > ul > li:first-child:has(a:containsOwn(link))";
 Evaluator evaluator = QueryParser.parse(query);
 assertEquals("div", evaluator.getTagNameSelector());
 assertEquals("class1", evaluator.getClassSelector());
 assertEquals(Evaluator.Type.CHILD, evaluator.getChildRelation());
 assertEquals(1, evaluator.getChildIndex());
 assertNotNull(evaluator.getDescendantEvaluator());
 assertEquals("ul", evaluator.getDescendantEvaluator().getTagNameSelector());
 assertEquals("li", evaluator.getDescendantEvaluator().getClassSelector());
 assertEquals(Evaluator.Type.FIRST_CHILD, evaluator.getDescendantEvaluator().getChildRelation());
 assertNotNull(evaluator.getDescendantEvaluator().getDescendantEvaluator());
 assertEquals("a", evaluator.getDescendantEvaluator().getDescendantEvaluator().getClassSelector());
 assertEquals("link", evaluator.getDescendantEvaluator().getDescendantEvaluator().getTextSelector());
 }

 @Test
 public void testParseInvalidSelector() {
 String query = "div#id:not:class1:not(div):not:div";
 try {
 QueryParser.parse(query);
 fail("Expected Selector.SelectorParseException");
 } catch (Selector.SelectorParseException e) {
 // expected
 }
 }

 @Test
 public void testParseEmptySelector() {
 String query = "";
 Evaluator evaluator = QueryParser.parse(query);
 assertNull(evaluator);
 }

 @Test
 public void testParseInvalidTokenisedQuery() {
 Parser parser = new Parser();
 TokenQueue tq = parser.parse("div#id:not:class1:not(div):not:div");
 QueryParser queryParser = new QueryParser(tq.toString());
 Evaluator evaluator = queryParser.parse();
 assertNull(evaluator);
 }
}