import org.junit.Test;
import org.jsoup.parser.TokenQueue;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.select.QueryParser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class QueryParserTest {

 private static String[] testCases = {
 "div > p > ul > li > a[href]",
 "div.class1 > div.class2 > ul > li:first-child > a[href^='https']",
 "div#id1 > div.class1 > ul > li:last-child > a[href$='.pdf']",
 "div[class~='class1 class2'] > p > a[contains(text, 'test')]",
 "div:has(p[contains(text, 'test')]) > a[contains(text, 'link')]",
 "div:containsOwn('test') > a[contains(text, 'link')]",
 "div:containsOwn('test') > a:matches(^[A-Z].+$)"
 };

 @Test
 public void testParse() {
 for (String query : testCases) {
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidQuery() {
 String invalidQuery = "div > p > ul > li > a[href ^='https']";
 QueryParser parser = new QueryParser(invalidQuery);
 parser.parse(); // should throw an exception
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseEmptyQuery() {
 QueryParser parser = new QueryParser("");
 parser.parse(); // should throw an exception
 }

 @Test
 public void testParseComplexQuery() {
 String complexQuery = "div > p > ul > li > a[href^='https'][title~='test'][class~='class1 class2']";
 QueryParser parser = new QueryParser(complexQuery);
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }
}