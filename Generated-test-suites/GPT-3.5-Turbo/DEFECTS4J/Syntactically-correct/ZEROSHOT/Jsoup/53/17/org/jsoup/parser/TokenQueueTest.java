package org.jsoup.parser;

import org.testng.annotations.Test;
import org.testng.Assert;

public class TokenQueueTest {

	@Test
	public void testIsEmpty() {
		TokenQueue tokenQueue = new TokenQueue("");
		Assert.assertTrue(tokenQueue.isEmpty());
	}

	@Test
	public void testPeek() {
		TokenQueue tokenQueue = new TokenQueue("Hello");
		Assert.assertEquals(tokenQueue.peek(), 'H');
	}

	@Test
	public void testAddFirst() {
		TokenQueue tokenQueue = new TokenQueue("Hello");
		tokenQueue.addFirst('X');
		Assert.assertEquals(tokenQueue.toString(), "XHello");
	}

	@Test
	public void testMatches() {
		TokenQueue tokenQueue = new TokenQueue("Hello");
		Assert.assertTrue(tokenQueue.matches("Hello"));
	}

	@Test
	public void testMatchesCS() {
		TokenQueue tokenQueue = new TokenQueue("Hello");
		Assert.assertTrue(tokenQueue.matchesCS("Hello"));
	}

	@Test
	public void testMatchesAny() {
		TokenQueue tokenQueue = new TokenQueue("Hello");
		Assert.assertTrue(tokenQueue.matchesAny('H', 'X'));
	}

	@Test
	public void testMatchesStartTag() {
		TokenQueue tokenQueue = new TokenQueue("<Html>");
		Assert.assertTrue(tokenQueue.matchesStartTag());
	}

	@Test
	public void testMatchesWhitespace() {
		TokenQueue tokenQueue = new TokenQueue(" ");
		Assert.assertTrue(tokenQueue.matchesWhitespace());
	}

	@Test
	public void testMatchesWord() {
		TokenQueue tokenQueue = new TokenQueue("Hello");
		Assert.assertTrue(tokenQueue.matchesWord());
	}

	@Test
	public void testAdvance() {
		TokenQueue tokenQueue = new TokenQueue("Hello");
		tokenQueue.advance();
		Assert.assertEquals(tokenQueue.toString(), "ello");
	}

	@Test
	public void testConsume() {
		TokenQueue tokenQueue = new TokenQueue("Hello");
		Assert.assertEquals(tokenQueue.consume(), 'H');
	}

	@Test
	public void testConsumeString() {
		TokenQueue tokenQueue = new TokenQueue("Hello");
		tokenQueue.consume("Hello");
		Assert.assertEquals(tokenQueue.toString(), "");
	}

	@Test
	public void testConsumeTo() {
		TokenQueue tokenQueue = new TokenQueue("Hello");
		Assert.assertEquals(tokenQueue.consumeTo("l"), "He");
	}

	@Test
	public void testConsumeToIgnoreCase() {
		TokenQueue tokenQueue = new TokenQueue("Hello");
		Assert.assertEquals(tokenQueue.consumeToIgnoreCase("L"), "He");
	}

	@Test
	public void testConsumeToAny() {
		TokenQueue tokenQueue = new TokenQueue("Hello");
		Assert.assertEquals(tokenQueue.consumeToAny("l", "e"), "H");
	}

	@Test
	public void testChompTo() {
		TokenQueue tokenQueue = new TokenQueue("Hello");
		Assert.assertEquals(tokenQueue.chompTo("l"), "He");
	}

	@Test
	public void testChompToIgnoreCase() {
		TokenQueue tokenQueue = new TokenQueue("Hello");
		Assert.assertEquals(tokenQueue.chompToIgnoreCase("L"), "He");
	}

	@Test
	public void testChompBalanced() {
		TokenQueue tokenQueue = new TokenQueue("(Hello)");
		Assert.assertEquals(tokenQueue.chompBalanced('(', ')'), "Hello");
	}

	@Test
	public void testUnescape() {
		String test = "\\Hello";
		Assert.assertEquals(TokenQueue.unescape(test), test);
	}

	@Test
	public void testConsumeWhitespace() {
		TokenQueue tokenQueue = new TokenQueue(" ");
		Assert.assertTrue(tokenQueue.consumeWhitespace());
	}

	@Test
	public void testConsumeWord() {
		TokenQueue tokenQueue = new TokenQueue("Hello");
		Assert.assertEquals(tokenQueue.consumeWord(), "Hello");
	}

	@Test
	public void testConsumeTagName() {
		TokenQueue tokenQueue = new TokenQueue("Hello");
		Assert.assertEquals(tokenQueue.consumeTagName(), "Hello");
	}

	@Test
	public void testConsumeElementSelector() {
		TokenQueue tokenQueue = new TokenQueue("Hello");
		Assert.assertEquals(tokenQueue.consumeElementSelector(), "Hello");
	}

	@Test
	public void testConsumeCssIdentifier() {
		TokenQueue tokenQueue = new TokenQueue("Hello");
		Assert.assertEquals(tokenQueue.consumeCssIdentifier(), "Hello");
	}

	@Test
	public void testConsumeAttributeKey() {
		TokenQueue tokenQueue = new TokenQueue("Hello");
		Assert.assertEquals(tokenQueue.consumeAttributeKey(), "Hello");
	}

	@Test
	public void testRemainder() {
		TokenQueue tokenQueue = new TokenQueue("Hello");
		Assert.assertEquals(tokenQueue.remainder(), "Hello");
	}

}