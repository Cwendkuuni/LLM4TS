package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.StringUtil;

import java.util.Arrays;

import org.junit.Test;

/**
 * Tests for the TokenQueue class
 *
 * @author John Doe
 */
public class TokenQueueTest {

	@Test
	public void testTokenQueueInstance() {
		String data = "abcdef";
		TokenQueue queue = new TokenQueue(data);
		assertEquals(data, queue.queue);
		assertEquals(0, queue.pos);
		assertFalse(queue.isEmpty());
	}

	@Test
	public void testIsEmpty() {
		String data = "";
		TokenQueue queue = new TokenQueue(data);
		assertTrue(queue.isEmpty());

		data = "abc";
		queue = new TokenQueue(data);
		assertFalse(queue.isEmpty());
	}

	@Test
	public void testPeek() {
		String data = "abcdef";
		TokenQueue queue = new TokenQueue(data);
		assertEquals('a', queue.peek());
		assertEquals('a', queue.consume());
		assertEquals('b', queue.peek());
	}

	@Test(expected = IllegalStateException.class)
	public void testPeekWhenEmpty() {
		String data = "";
		TokenQueue queue = new TokenQueue(data);
		queue.peek();
	}

	@Test
	public void testAddFirst() {
		String data = "abcd";
		TokenQueue queue = new TokenQueue(data);
		queue.addFirst('e');
		assertEquals('e', queue.consume());
		assertEquals('a', queue.consume());
		// check queue length
		assertEquals(5, queue.remainingLength());
	}

	@Test
	public void testMatches() {
		String data = "abcdef";
		TokenQueue queue = new TokenQueue(data);
		// true tests
		assertTrue(queue.matches("a"));
		assertTrue(queue.matches("ab"));
		assertTrue(queue.matches("def"));
		// false tests
		assertFalse(queue.matches("efg"));
		assertFalse(queue.matches(""));
		assertFalse(queue.matches("x"));
	}

	@Test
	public void testMatchesCS() {
		String data = "ABCdef";
		TokenQueue queue = new TokenQueue(data);
		assertTrue(queue.matchesCS("ABC"));
		assertFalse(queue.matchesCS("efg"));
		assertFalse(queue.matchesCS(""));
	}

	@Test
	public void testMatchesAny() {
		String data = "abcd";
		TokenQueue queue = new TokenQueue(data);
		assertTrue(queue.matchesAny("a", "b"));
		assertTrue(queue.matchesAny("a", "c", "d"));
		assertFalse(queue.matchesAny("efg"));
		assertFalse(queue.matchesAny(""));
	}

	@Test
	public void testMatchesAnyChars() {
		String data = "abcd";
		TokenQueue queue = new TokenQueue(data);
		assertTrue(queue.matchesAny(new char[]{'a', 'b'}));
		assertTrue(queue.matchesAny(new char[]{'a', 'c', 'd'}));
		assertFalse(queue.matchesAny(new char[]{'e', 'f', 'g'}));
		assertFalse(queue.matchesAny(new char[]{}));
	}

	@Test
	public void testMatchesStartTag() {
		String data = "<abc><def>";
		TokenQueue queue = new TokenQueue(data);
		assertTrue(queue.matchesStartTag());
		assertEquals('<', queue.consume());
		assertEquals('a', queue.consume());
		assertEquals('b', queue.consume());
		assertEquals('<', queue.consume());
		assertEquals('d', queue.consume());
		assertEquals('e', queue.consume());
		assertEquals('f', queue.consume());
		assertFalse(queue.matchesStartTag());
		// test with empty queue
		queue = new TokenQueue("");
		assertFalse(queue.matchesStartTag());
	}

	@Test
	public void testMatchChomp() {
		String data = "<abc><def>";
		TokenQueue queue = new TokenQueue(data);
		assertTrue(queue.matchChomp("<ab"));
		assertEquals("c", queue.consume().toString());
		assertTrue(queue.matchChomp("def>"));
		assertEquals("", queue.toString());
		// test with empty queue
		queue = new TokenQueue("");
		assertFalse(queue.matchChomp(""));
	}

	@Test
	public void testMatchesWhitespace() {		
		String data = " \tabcd\n";
		TokenQueue queue = new TokenQueue(data);
		assertTrue(queue.matchesWhitespace());		
		assertEquals(' ', queue.consume());
		assertTrue(queue.matchesWhitespace());
		assertEquals('\t', queue.consume());
		assertEquals('\n', queue.consume());
		assertFalse(queue.matchesWhitespace());
	}

	@Test
	public void testMatchesWord() {
		String data = "AwordBwordCword";
		TokenQueue queue = new TokenQueue(data);
		assertTrue(queue.matchesWord());
		assertEquals('A', queue.consume());
		assertTrue(queue.matchesWord());
		assertEquals('w', queue.consume());
		assertTrue(queue.matchesWord());
		assertEquals('o', queue.consume());
		assertTrue(queue.matchesWord());
		assertEquals('r', queue.consume());
		assertTrue(queue.matchesWord());
		assertEquals('d', queue.consume());
		assertFalse(queue.matchesWord());
	}

	@Test
	public void testAdvance() {
		String data = "abcdef";
		TokenQueue queue = new TokenQueue(data);
		queue.advance();
		assertEquals('b', queue.consume());
		queue.advance();
		assertEquals('c', queue.consume());
		queue.advance();
		assertEquals('d', queue.consume());
	}

	@Test
	public void testConsume() {
		String data = "abcdef";
		TokenQueue queue = new TokenQueue(data);
		assertEquals('a', queue.consume());
		assertEquals('b', queue.consume());
		assertEquals('c', queue.consume());
		assertEquals('d', queue.consume());
		assertEquals('e', queue.consume());
		assertEquals('f', queue.consume());
		assertFalse(queue.isEmpty());
	}

	@Test(expected = IllegalStateException.class)
	public void testConsumeEmptyQueue() {
		String data = "";
		TokenQueue queue = new TokenQueue(data);
		queue.consume();
	}

	@Test
	public void testConsumeSequence() {
		String data = "abcdabcd";
		TokenQueue queue = new TokenQueue(data);
		assertEquals("abcd", queue.consumeTo("abcd"));
		assertEquals("", queue.toString());
		assertFalse(queue.isEmpty());
	}

	@Test
	public void testConsumeSequenceIgnoreCase() {
		String data = "AbCdAbCd";
		TokenQueue queue = new TokenQueue(data);
		assertEquals("AbCd", queue.consumeToIgnoreCase("AbCd"));
		assertEquals("", queue.toString());
		assertFalse(queue.isEmpty());
	}

	@Test
	public void testConsumeToAny() {
		String data = "123456";
		TokenQueue queue = new TokenQueue(data);
		assertEquals("12", queue.consumeToAny("1", "2"));
		assertEquals("3", queue.consume());
		assertEquals("4", queue.consume());
		assertEquals("5", queue.consume());
		assertEquals("6", queue.consume());
		assertEquals("", queue.toString());
		assertFalse(queue.isEmpty());
	}

	@Test
	public void testConsumeToAnyChars() {
		String data = "123456";
		TokenQueue queue = new TokenQueue(data);
		assertEquals("12", queue.consumeToAny(new char[]{'1', '2'}));
		assertEquals("3", queue.consume());
		assertEquals("4", queue.consume());
		assertEquals("5", queue.consume());
		assertEquals("6", queue.consume());
		assertEquals("", queue.toString());
		assertFalse(queue.isEmpty());
	}

	@Test
	public void testChompBalanced() {
		String data = "((())(()()))";
		TokenQueue queue = new TokenQueue(data);
		assertEquals("((())", queue.chompBalanced('(', ')'));
		assertEquals("()()))", queue.toString());
		assertFalse(queue.isEmpty());
	}

	@Test
	public void testChompBalancedWithQuotes() {
		String data = "(\'(\'(\'(\'(\'(\'(\'(\'(\'(\'(\'(\'(\'(\'(\'(\'(\'(\'";
		TokenQueue queue = new TokenQueue(data);
		assertEquals("(\'(\'(\'(\'(\'(\'(\'(\'(\'(\'(\'(\'(\'(\'(\'(\'(\'(\'", queue.chompBalanced('(', ')'));
		assertEquals("", queue.toString());
		assertFalse(queue.isEmpty());
	}

	@Test
	public void testUnescape() {
		String data = "\\abc\\def\\\\";
		assertEquals("abcdef\\", TokenQueue.unescape(data));
	}

	@Test
	public void testConsumeWhitespace() {
		String data = "\t abc\n def\r\t ghi";
		TokenQueue queue = new TokenQueue(data);
		assertTrue(queue.consumeWhitespace());
		assertTrue(queue.consumeWhitespace());
		assertTrue(queue.consumeWhitespace());
		assertEquals('a', queue.consume());
		assertEquals('b', queue.consume());
		assertEquals('c', queue.consume());
		assertEquals('d', queue.consume());
		assertEquals('e', queue.consume());
		assertEquals('g', queue.consume());
		assertEquals('h', queue.consume());
		assertEquals('i', queue.consume());
		assertFalse(queue.isEmpty());
	}

	@Test
	public void testConsumeWord() {
		String data = "A word B word C word";
		TokenQueue queue = new TokenQueue(data);
		assertEquals("A word", queue.consumeWord());
		assertEquals("B word", queue.consumeWord());
		assertEquals("C word", queue.consumeWord());
		assertEquals("", queue.toString());
		assertFalse(queue.isEmpty());
	}

	@Test
	public void testConsumeTagName() {
		String data = "<table><tr><td><table><tr><td></td></tr></table></td></tr></table>";
		TokenQueue queue = new TokenQueue(data);
		assertEquals("table", queue.consumeTagName());
		assertEquals("tr", queue.consumeTagName());
		assertEquals("td", queue.consumeTagName());
		assertEquals("table", queue.consumeTagName());
		assertEquals("tr", queue.consumeTagName());
		assertEquals("td", queue.consumeTagName());
		assertEquals("", queue.toString());
		assertFalse(queue.isEmpty());
	}

	@Test
	public void testConsumeElementSelector() {
		String data = "div#id.class:hover p#id2.class:hover a[href]:hover";
		TokenQueue queue = new TokenQueue(data);
		assertEquals("div", queue.consumeElementSelector());
		assertEquals("#id", queue.consume());
		assertEquals(".class", queue.consume());
		assertEquals(":hover", queue.consume());
		assertEquals("p", queue.consumeElementSelector());
		assertEquals("#id2", queue.consume());
		assertEquals(".class", queue.consume());
		assertEquals(":hover", queue.consume());
		assertEquals("a", queue.consumeElementSelector());
		assertEquals("[href]", queue.consume());
		assertEquals(":hover", queue.consume());
		assertEquals("", queue.toString());
		assertFalse(queue.isEmpty());
	}

	@Test
	public void testConsumeCssIdentifier() {
		String data = "color:red; background-color:#ff0000;";
		TokenQueue queue = new TokenQueue(data);
		assertEquals("color", queue.consumeCssIdentifier());
		assertEquals("red", queue.consume());
		assertEquals("background-color", queue.consumeCssIdentifier());
		assertEquals("#ff0000", queue.consume());
		assertEquals("", queue.toString());
		assertFalse(queue.isEmpty());
	}

	@Test
	public void testConsumeAttributeKey() {
		String data = "class='class1 class2' id='id1'";
		TokenQueue queue = new TokenQueue(data);
		assertEquals("class", queue.consumeAttributeKey());
		assertEquals("class1 class2", queue.consume());
		assertEquals("id", queue.consumeAttributeKey());
		assertEquals("id1", queue.consume());
		assertEquals("", queue.toString());
		assertFalse(queue.isEmpty());
	}

	@Test
	public void testRemainder() {
		String data = "abcdef";
		TokenQueue queue = new TokenQueue(data);
		assertEquals("def", queue.remainder());
		assertEquals("", queue.toString());
		assertFalse(queue.isEmpty());
	}

	@Test
	public void testToString() {
		String data = "abcdef";
		TokenQueue queue = new TokenQueue(data);
		assertEquals("def", queue.toString());
		assertEquals("", queue.remainder());
		assertFalse(queue.isEmpty());
	}
}