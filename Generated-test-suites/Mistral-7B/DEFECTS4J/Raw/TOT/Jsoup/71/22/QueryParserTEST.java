package org.jsoup.select.test;

import org.junit.Test;
import org.jsoup.helper.StringUtil;
import org.jsoup.parser.TokenQueue;
import org.jsoup.parser.QueryParser;
import org.jsoup.parser.Selector;
import org.jsoup.parser.SssSelector;
import org.jsoup.parser.Token;
import org.jsoup.parser.ParseError;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class QueryParserTest {

 private static final String TEST_QUERY_1 = "div.test-class > ul > li:nth-child(2) > input[type='text']";
 private static final String TEST_QUERY_2 = "input[type='text']";
 private static final String TEST_QUERY_3 = "div.test-class input[type='text']";
 private static final String TEST_QUERY_4 = "div#test-div > ul > li:nth-child(1) > input[type='text']";
 private static final String TEST_QUERY_5 = "input[type='text'][name='mytext']";
 private static final String TEST_QUERY_6 = "div.test-class input[type='text']:first-child";
 private static final String TEST_QUERY_7 = "input[type='text']:not(.test-class)";
 private static final String TEST_QUERY_8 = "input[type='text'][name='mytext']:not(.test-class)";
 private static final String TEST_QUERY_9 = "div input[type='text']"; // should fail
 private static final String TEST_QUERY_10 = "input[type='text'][name='mytext']:contains(foo)";
 private static final String TEST_QUERY_11 = "input[type='text'][name='mytext']:containsOwn(foo)";
 private static final String TEST_QUERY_12 = "input[type='text'][name='mytext']:matches(/^foo$/)";
 private static final String TEST_QUERY_13 = "input[type='text'][name='mytext']:matchesOwn(/^foo$/)";
 private static final String TEST_QUERY_14 = "input[type='text'][name='mytext']:has(.test-class)";

 @Test
 public void testValidQueries() {
 for (String query : new String[] { TEST_QUERY_1, TEST_QUERY_2, TEST_QUERY_3, TEST_QUERY_4, TEST_QUERY_5, TEST_QUERY_6, TEST_QUERY_7, TEST_QUERY_8, TEST_QUERY_9, TEST_QUERY_10, TEST_QUERY_11, TEST_QUERY_12, TEST_QUERY_13, TEST_QUERY_14 }) {
 QueryParser parser = new QueryParser(query);
 Selector selector = parser.parse();
 // Perform some checks on the parsed selector to ensure it's as expected
 // ...
 }
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testInvalidQueries() {
 String invalidQuery = "div:not:not:not input[type='text']"; // three negations are not valid
 QueryParser parser = new QueryParser(invalidQuery);
 parser.parse(); // This should throw an exception
 }

 @Test
 public void testEmptyQuery() {
 QueryParser parser = new QueryParser("");
 Selector selector = parser.parse();
 assertNull(selector);
 }

 @Test
 public void testNullQuery() {
 QueryParser parser = new QueryParser(null);
 Selector selector = parser.parse();
 assertNull(selector);
 }

 @Test
 public void testEmptyQueryString() {
 QueryParser parser = new QueryParser(" ");
 Selector selector = parser.parse();
 assertNull(selector);
 }
}