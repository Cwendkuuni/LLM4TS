package org.jsoup.parser.test;

import static org.junit.Assert.*;

import org.jsoup.parser.TokenQueue;

import org.junit.Test;

public class TokenQueueTest {

 private String testData = "<?xml version=\"1.0\"?>\r\n<test>\r\n<tag attr=\"value\">Content\r\n <subtag/>\r\n</tag>\r\n</test>";
 private TokenQueue tokenQueue;

 @Before
 public void setUp() {
 tokenQueue = new TokenQueue(testData);
 }

 // Test isEmpty() method
 @Test
 public void testIsEmpty() {
 assertTrue(tokenQueue.isEmpty());
 tokenQueue.addFirst("a");
 assertFalse(tokenQueue.isEmpty());
 }

 // Test peek() method
 @Test
 public void testPeek() {
 assertEquals('<?', tokenQueue.peek());
 tokenQueue.advance();
 assertEquals('x', tokenQueue.peek());
 }

 // Test addFirst() method
 @Test
 public void testAddFirst() {
 tokenQueue.addFirst("test");
 assertEquals("test<?xml version=\"1.0\"?>\r\n<test>\r\n<tag attr=\"value\">Content\r\n <subtag/>\r\n</tag>\r\n</test>", tokenQueue.toString());
 }

 // Test matches() method
 @Test
 public void testMatches() {
 tokenQueue.advance();
 tokenQueue.advance();
 assertTrue(tokenQueue.matches("ml"));
 tokenQueue.advance();
 assertFalse(tokenQueue.matches("ml"));
 }

 // Test matchesCS() method
 @Test
 public void testMatchesCS() {
 tokenQueue.addFirst("XML");
 assertTrue(tokenQueue.matchesCS("XML"));
 }

 // Test matchesAny() method
 @Test
 public void testMatchesAny() {
 tokenQueue.addFirst("test");
 tokenQueue.addFirst("xml");
 assertTrue(tokenQueue.matchesAny("test", "xml"));
 }

 // Test matchesAny(char[]) method
 @Test
 public void testMatchesAnyChars() {
 tokenQueue.addFirst("testxml");
 assertTrue(tokenQueue.matchesAny(new char[]{'t', 'e', 's'}));
 }

 // Test matchesStartTag() method
 @Test
 public void testMatchesStartTag() {
 tokenQueue.addFirst("<tag attr=\"value\">");
 assertTrue(tokenQueue.matchesStartTag());
 }

 // Test matchChomp() method
 @Test
 public void testMatchChomp() {
 tokenQueue.addFirst("<tag attr=\"value\">Content");
 tokenQueue.matchChomp("tag");
 assertEquals("Content", tokenQueue.consume().toString());
 }

 // Test matchesWhitespace() method
 @Test
 public void testMatchesWhitespace() {
 tokenQueue.addFirst(" \r\n\t<test>\r\n");
 assertTrue(tokenQueue.matchesWhitespace());
 }

 // Test matchesWord() method
 @Test
 public void testMatchesWord() {
 tokenQueue.addFirst("testxml");
 assertTrue(tokenQueue.matchesWord());
 }

 // Test advance() method
 @Test
 public void testAdvance() {
 tokenQueue.addFirst("testxml");
 tokenQueue.advance();
 assertEquals("testxml<?xml version=\"1.0\"?>\r\n<test>\r\n", tokenQueue.toString());
 }

 // Test consume() method
 @Test
 public void testConsume() {
 tokenQueue.addFirst("<?xml version=\"1.0\"?>\r\n<test>\r\n<tag attr=\"value\">Content\r\n <subtag/>\r\n</tag>\r\n</test>");
 assertEquals('<?', tokenQueue.consume());
 }

 // Test consume(String) method
 @Test(expected = IllegalStateException.class)
 public void testConsumeStringInvalid() {
 tokenQueue.consume("invalid");
 }

 // Test consumeTo(String) method
 @Test
 public void testConsumeTo() {
 tokenQueue.addFirst("<?xml version=\"1.0\"?>\r\n<test>\r\n<tag attr=\"value\">Content\r\n <subtag/>\r\n</tag>\r\n</test>");
 assertEquals("<?xml version=\"1.0\"?>\r\n<test>\r\n<tag attr=\"value\"", tokenQueue.consumeTo("tag attr=\"").toString());
 }

 // Test consumeToIgnoreCase(String) method
 @Test
 public void testConsumeToIgnoreCase() {
 tokenQueue.addFirst("<?xml version=\"1.0\"?>\r\n<test>\r\n<tag attr=\"value\">Content\r\n <subtag/>\r\n</tag>\r\n</test>");
 assertEquals("<?xml version=\"1.0\"?>\r\n<test>\r\n<tag attr=\"value\"", tokenQueue.consumeToIgnoreCase("tag attr=").toString());
 }

 // Test consumeToAny(String[]) method
 @Test
 public void testConsumeToAny() {
 tokenQueue.addFirst("<?xml version=\"1.0\"?>\r\n<test>\r\n<tag attr=\"value\">Content\r\n <subtag/>\r\n</tag>\r\n</test>");
 assertEquals("<?xml version=\"1.0\"?>\r\n<test>\r\n<tag attr=\"value\"", tokenQueue.consumeToAny(new String[]{"tag", "value"}).toString());
 }

 // Test chompBalanced(char, char) method
 @Test
 public void testChompBalanced() {
 tokenQueue.addFirst("(one (two) three) four");
 assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')').toString());
 }

 // Test unescape(String) method
 @Test
 public void testUnescape() {
 assertEquals("\\r\\n<test>\r\n<tag attr=\"value\">Content\r\n <subtag/>\r\n</tag>\r\n</test>", TokenQueue.unescape(testData));
 }

 // Test consumeWhitespace() method
 @Test
 public void testConsumeWhitespace() {
 tokenQueue.addFirst(" \r\n\t<test>\r\n");
 assertTrue(tokenQueue.consumeWhitespace());
 }

 // Test consumeWord() method
 @Test
 public void testConsumeWord() {
 tokenQueue.addFirst("testxml");
 assertEquals("test", tokenQueue.consumeWord());
 }

 // Test consumeTagName() method
 @Test
 public void testConsumeTagName() {
 tokenQueue.addFirst("<tag attr=\"value\">");
 assertEquals("tag", tokenQueue.consumeTagName());
 }

 // Test consumeElementSelector() method
 @Test
 public void testConsumeElementSelector() {
 tokenQueue.addFirst("tag:class");
 assertEquals("tag", tokenQueue.consumeElementSelector());
 }

 // Test consumeCssIdentifier() method
 @Test
 public void testConsumeCssIdentifier() {
 tokenQueue.addFirst("class:value");
 assertEquals("class", tokenQueue.consumeCssIdentifier());
 }

 // Test consumeAttributeKey() method
 @Test
 public void testConsumeAttributeKey() {
 tokenQueue.addFirst("attr=\"value\"");
 assertEquals("attr", tokenQueue.consumeAttributeKey());
 }

 // Test remainder() method
 @Test
 public void testRemainder() {
 tokenQueue.addFirst("<?xml version=\"1.0\"?>\r\n<test>\r\n<tag attr=\"value\">Content\r\n <subtag/>\r\n</tag>\r\n</test>");
 assertEquals("</test>", tokenQueue.remainder());
 }

 // Test toString() method
 @Test
 public void testToString() {
 assertEquals(testData, tokenQueue.toString());
 }
}