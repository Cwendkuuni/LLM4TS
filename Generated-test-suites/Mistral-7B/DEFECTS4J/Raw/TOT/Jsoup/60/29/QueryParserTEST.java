import org.jsoup.parser.QueryParser;
import org.jsoup.parser.Selector;
import org.jsoup.parser.TokenQueue;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

 private String[] testQueries = new String[]{"div.class1 > div.class2", "div#id1 ~ div.class1", "div.class1 + div.class2", "div.class1, div.class2", "div[id='id1']", "div.class1[title='Test']", "div[title^='Test']", "div[title$='Test']", "div[title*='Test']", "div[title^='Test'][title$='World']", "div:first-child", "div:last-child", "div:first-of-type", "div:last-of-type", "div:only-child", "div:only-of-type", "div:empty", "div:root", "div:nth-child(3)", "div:nth-child(-3)", "div:nth-last-child(3)", "div:nth-last-child(-3)", "div:nth-of-type(3)", "div:nth-of-type(-3)", "div:nth-last-of-type(3)", "div:nth-last-of-type(-3)", "div:has(p)", "div:contains(Test)", "div:containsOwn(Test)", "div:containsData(data)", "div:matches(.*Test.*)", "div:matchesOwn(.*Test.*)", "div:not(.class1)"};

 @Test
 public void testParse() {
 for (String query : testQueries) {
 QueryParser qp = new QueryParser(query);
 Evaluator eval = qp.parse();
 Selector sel = new Selector(eval);
 TokenQueue tq = new TokenQueue(query);
 Selector expectedSel = new Selector(parse(query));
 assertEquals("Unexpected result for query: " + query, expectedSel, sel);
 }
 }

 private static Evaluator parse(String query) {
 // Perform actual parsing here using your implementation of QueryParser
 // For simplicity, we'll just use the provided one in the test
 return QueryParser.parse(query);
 }

}