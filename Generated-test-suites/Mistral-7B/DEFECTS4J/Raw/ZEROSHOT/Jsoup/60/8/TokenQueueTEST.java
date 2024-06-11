package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TokenQueueTest {

 private TokenQueue tokenQueue;

 @Before
 public void setUp() {
 String data = "abc<def>xyz<p class=\"foo\">123<h1 id=\"test\">Hello World</h1>";
 tokenQueue = new TokenQueue(data);
 }

 @After
 public void tearDown() {
 tokenQueue = null;
 }

 @Test
 public void testIsEmpty() {
 assertTrue(tokenQueue.isEmpty());
 tokenQueue.addFirst("a");
 assertFalse(tokenQueue.isEmpty());
 tokenQueue.consume();
 assertFalse(tokenQueue.isEmpty());
 tokenQueue.consume("bc");
 assertTrue(tokenQueue.isEmpty());
 }

 @Test
 public void testPeek() {
 assertEquals('a', tokenQueue.peek());
 tokenQueue.addFirst("1");
 assertEquals('1', tokenQueue.peek());
 tokenQueue.consume();
 assertEquals('a', tokenQueue.peek());
 }

 @Test
 public void testAddFirst() {
 tokenQueue.addFirst("x");
 assertEquals("x", tokenQueue.remainder());
 tokenQueue.addFirst("y");
 assertEquals("yx", tokenQueue.remainder());
 tokenQueue.consume();
 assertEquals("xy", tokenQueue.remainder());
 }

 @Test
 public void testMatches() {
 assertTrue(tokenQueue.matches("a"));
 assertTrue(tokenQueue.matches("ab"));
 assertFalse(tokenQueue.matches("ef"));
 }

 @Test
 public void testMatchesCS() {
 assertFalse(tokenQueue.matchesCS("ef"));
 assertTrue(tokenQueue.matchesCS("h1"));
 }

 @Test
 public void testMatchesAny() {
 tokenQueue.addFirst("x");
 tokenQueue.addFirst("y");
 assertTrue(tokenQueue.matchesAny("x", "y"));
 assertFalse(tokenQueue.matchesAny("z"));
 }

 @Test
 public void testMatchesAnyChars() {
 tokenQueue.addFirst("x");
 tokenQueue.addFirst("y");
 assertTrue(tokenQueue.matchesAny('x', 'y'));
 assertFalse(tokenQueue.matchesAny('z'));
 }

 @Test
 public void testMatchesStartTag() {
 assertTrue(tokenQueue.matchesStartTag());
 tokenQueue.consume();
 assertFalse(tokenQueue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 tokenQueue.addFirst("abc<def>xyz<p class=\"foo\">123<h1 id=\"test\">Hello World</h1>");
 assertTrue(tokenQueue.matchChomp("<def>"));
 assertEquals("xyz<p class=\"foo\">123<h1 id=\"test\">Hello World</h1>", tokenQueue.remainder());
 }

 @Test
 public void testMatchesWhitespace() {
 assertTrue(tokenQueue.matchesWhitespace());
 tokenQueue.consumeWord();
 assertFalse(tokenQueue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 assertTrue(tokenQueue.matchesWord());
 tokenQueue.consumeWord();
 assertFalse(tokenQueue.matchesWord());
 }

 @Test
 public void testConsume() {
 assertEquals('a', tokenQueue.consume());
 assertEquals('b', tokenQueue.consume());
 assertEquals('c', tokenQueue.consume());
 assertTrue(tokenQueue.isEmpty());
 tokenQueue.addFirst("1");
 assertEquals('1', tokenQueue.consume());
 assertFalse(tokenQueue.isEmpty());
 }

 @Test
 public void testConsumeString() {
 tokenQueue.addFirst("abc<def>xyz<p class=\"foo\">123<h1 id=\"test\">Hello World</h1>");
 tokenQueue.consume("def");
 assertEquals("xyz<p class=\"foo\">123<h1 id=\"test\">Hello World</h1>", tokenQueue.remainder());
 }

 @Test
 public void testConsumeTo() {
 assertEquals("abc<def>", tokenQueue.consumeTo("def"));
 assertEquals("xyz<p class=\"foo\">123<h1 id=\"test\">Hello World</h1>", tokenQueue.remainder());
 }

 @Test
 public void testConsumeToIgnoreCase() {
 assertEquals("abc<def>", tokenQueue.consumeToIgnoreCase("Def"));
 assertEquals("xyz<p class=\"foo\">123<h1 id=\"test\">Hello World</h1>", tokenQueue.remainder());
 }

 @Test
 public void testConsumeToAny() {
 assertEquals("abc<def>", tokenQueue.consumeToAny("def"));
 assertEquals("xyz<p class=\"foo\">123<h1 id=\"test\">Hello World</h1>", tokenQueue.remainder());
 }

 @Test
 public void testChompBalanced() {
 assertEquals("abc<def>", tokenQueue.chompBalanced('(', ')'));
 assertEquals("xyz<p class=\"foo\">123<h1 id=\"test\">Hello World</h1>", tokenQueue.remainder());
 }
}