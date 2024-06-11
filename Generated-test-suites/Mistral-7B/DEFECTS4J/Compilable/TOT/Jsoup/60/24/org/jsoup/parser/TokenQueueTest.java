package org.jsoup.parser;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;
import static org.junit.Assert.*;

public class TokenQueueTest {

 @Test
 public void testEmptyQueue() {
 TokenQueue queue = new TokenQueue("");
 assertTrue(queue.isEmpty());
 }

 @Test
 public void testPeekAndAddFirst() {
 TokenQueue queue = new TokenQueue("abcdef");
 assertEquals('a', queue.peek());
 queue.addFirst('X');
 assertEquals('X', queue.peek());
 }

 @Test
 public void testMatchAndConsume() {
 TokenQueue queue = new TokenQueue("abcdefg<tag>hello</tag>");
 assertTrue(queue.matches("tag"));
 queue.consume("tag");
 assertFalse(queue.matches("tag"));
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeNonMatchingSequence() {
 TokenQueue queue = new TokenQueue("abcdefg<tag>hello</tag>");
 queue.consume("tag2");
 }

 @Test
 public void testMatchChomp() {
 TokenQueue queue = new TokenQueue("abcdefg<tag>hello</tag>");
 assertTrue(queue.matchChomp("tag"));
 assertFalse(queue.matches("tag"));
 }

 @Test
 public void testMatchesWhitespaceAndWord() {
 TokenQueue queue = new TokenQueue(" abc def g <tag>hello</tag>");
 assertTrue(queue.matchesWhitespace());
 assertTrue(queue.matchesWord());
 queue.advance();
 assertFalse(queue.matchesWhitespace());
 assertFalse(queue.matchesWord());
 }

 @Test
 public void testAddFirstStringWithLongerLength() {
 TokenQueue queue = new TokenQueue("abcdef");
 queue.addFirst("xyz");
 assertEquals("xyzabcdef", queue.toString());
 }

 @Test
 public void testMatchesAny() {
 TokenQueue queue = new TokenQueue("abcdefg<tag>hello</tag>");
 assertTrue(queue.matchesAny("tag", "hello"));
 }

 @Test
 public void testMatchesStartTag() {
 TokenQueue queue = new TokenQueue("<tag>hello</tag>");
 assertTrue(queue.matchesStartTag());
 }

 @Test
 public void testConsumeTo() {
 TokenQueue queue = new TokenQueue("abcdefg<tag>hello</tag>");
 assertEquals("tag", queue.consumeTo("tag").toString());
 assertEquals("hello", queue.consumeTo("hello").toString());
 assertEquals("", queue.consumeTo("xyz").toString());
 }

 @Test
 public void testChompBalanced() {
 TokenQueue queue = new TokenQueue("(abc)(def)(ghi)jkl");
 assertEquals("abcdefghi", queue.chompBalanced('(', ')').toString());
 }
}