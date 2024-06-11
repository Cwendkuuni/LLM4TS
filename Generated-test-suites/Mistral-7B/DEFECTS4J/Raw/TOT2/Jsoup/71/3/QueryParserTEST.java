import org.jsoup.parser.QueryParser;
import org.jsoup.parser.Selector;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class QueryParserTest {

 private static final String BASIC_QUERY = "#id1 > .class1 > .class2";
 private static final String BASIC_QUERY_SELECTOR = "#id1 > .class1 > .class2";

 @Test
 public void testBasicQuery() {
 QueryParser parser = new QueryParser(BASIC_QUERY);
 Selector selector = parser.parse();

 assertEquals(BASIC_QUERY_SELECTOR, selector.toString());
 }

 private static final String COMBINATOR_TEST_QUERY = "div > p > ul > li > a";
 private static final String COMBINATOR_TEST_SELECTOR = "div > p > ul > li > a";

 @Test
 public void testCombinatorQuery() {
 QueryParser parser = new QueryParser(COMBINATOR_TEST_QUERY);
 Selector selector = parser.parse();

 assertEquals(COMBINATOR_TEST_SELECTOR, selector.toString());
 }

 private static final String ID_TEST_QUERY = "#id1";
 private static final String ID_TEST_SELECTOR = "#id1";

 @Test
 public void testIdQuery() {
 QueryParser parser = new QueryParser(ID_TEST_QUERY);
 Selector selector = parser.parse();

 assertEquals(ID_TEST_SELECTOR, selector.toString());
 }

 private static final String CLASS_TEST_QUERY = ".class1";
 private static final String CLASS_TEST_SELECTOR = "class:class1";

 @Test
 public void testClassQuery() {
 QueryParser parser = new QueryParser(CLASS_TEST_QUERY);
 Selector selector = parser.parse();

 assertEquals(CLASS_TEST_SELECTOR, selector.toString());
 }

 private static final String ATTRIBUTE_TEST_QUERY = "[title='Java']";
 private static final String ATTRIBUTE_TEST_SELECTOR = "attr[title='Java']";

 @Test
 public void testAttributeQuery() {
 QueryParser parser = new QueryParser(ATTRIBUTE_TEST_QUERY);
 Selector selector = parser.parse();

 assertEquals(ATTRIBUTE_TEST_SELECTOR, selector.toString());
 }

 private static final String ELEMENT_TEST_QUERY = "ul > li:nth-child(2) > a";
 private static final String ELEMENT_TEST_SELECTOR = "ul > li:nth-child(2) > a";

 @Test
 public void testElementQuery() {
 QueryParser parser = new QueryParser(ELEMENT_TEST_QUERY);
 Selector selector = parser.parse();

 assertEquals(ELEMENT_TEST_SELECTOR, selector.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidQuery() {
 QueryParser parser = new QueryParser("invalid query");

 // The test should throw an IllegalArgumentException
 }

 @Test
 public void testEmptyQuery() {
 QueryParser parser = new QueryParser("");
 Selector selector = parser.parse();

 assertNull(selector);
 }
}