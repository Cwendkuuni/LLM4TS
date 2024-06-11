package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class TokenQueueTest {

    private TokenQueue tokenQueue;
    private String testData;
    private String expectedQueue;

    public TokenQueueTest(String testData, String expectedQueue) {
        this.testData = testData;
        this.expectedQueue = expectedQueue;
    }

    @Parameterized.Parameters(name = "{index}: testData={0}, expectedQueue={1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", "html<head><title>Test</title>"},
                {"<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", "<body><p>Hello World"},
                {"<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", "<p>Hello World"},
                {"", ""},
                {"<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", null},
                {"<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", ""},
                {"<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", "<html<head<titleTe"},
                {"<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", "<html<head<titleTe<body<pHe"},
                {"<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", "<html<head<titleTe<body<pHe<pHell"},
                {"<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", "<html<head<titleTe<body<pHe<pHell<pHelloWorl"}
        });
    }

    @Test
    public void testConstructor() {
        tokenQueue = new TokenQueue(testData);
        assertEquals(expectedQueue, tokenQueue.queue);
        assertEquals(expectedQueue.length(), tokenQueue.remainingLength());
    }

    @Test
    public void testIsEmpty() {
        tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());
        tokenQueue = new TokenQueue(testData);
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        tokenQueue = new TokenQueue(testData);
        if (expectedQueue.length() > 0) {
            assertEquals(expectedQueue.charAt(0), tokenQueue.peek());
        } else {
            assertNull(tokenQueue.peek());
        }
    }

    @Test
    public void testAddFirst() {
        tokenQueue = new TokenQueue(testData);
        String testString = "test";
        tokenQueue.addFirst(testString);
        assertEquals(testString + expectedQueue.substring(0, tokenQueue.pos), tokenQueue.queue);
    }

    @Test
    public void testMatches() {
        tokenQueue = new TokenQueue(testData);
        String testString = "test";
        assertTrue(tokenQueue.matches(testString));
        tokenQueue.consume(testString);
        assertFalse(tokenQueue.matches(testString));
    }

    @Test
    public void testMatchesCS() {
        tokenQueue = new TokenQueue(testData);
        String testString = "Test";
        assertTrue(tokenQueue.matchesCS(testString));
        tokenQueue.consume(testString);
        assertFalse(tokenQueue.matchesCS(testString));
    }

    @Test
    public void testMatchesAny() {
        tokenQueue = new TokenQueue(testData);
        String[] testStrings = {"test", "Test", "hello"};
        assertTrue(tokenQueue.matchesAny(testStrings));
        for (String testString : testStrings) {
            tokenQueue.consume(testString);
        }
        assertFalse(tokenQueue.matchesAny(testStrings));
    }

    @Test
    public void testMatchesAnyChar() {
        tokenQueue = new TokenQueue(testData);
        char[] testChars = {'t', 'e', 's', 'T', ' '};
        tokenQueue.advance();
        tokenQueue.advance();
        assertTrue(tokenQueue.matchesAny(testChars));
        for (char testChar : testChars) {
            tokenQueue.consume((Character.toString(testChar)));
        }
        assertFalse(tokenQueue.matchesAny(testChars));
    }

    @Test
    public void testMatchesStartTag() {
        tokenQueue = new TokenQueue(testData);
        assertTrue(tokenQueue.matchesStartTag());
        tokenQueue.consume("<");
        assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        tokenQueue = new TokenQueue(testData);
        String testString = "test";
        assertTrue(tokenQueue.matchChomp(testString));
        assertEquals(expectedQueue.substring(tokenQueue.pos), testString);
        tokenQueue.consume(testString);
        assertFalse(tokenQueue.matchChomp(testString));
    }

    @Test
    public void testMatchesWhitespace() {
        tokenQueue = new TokenQueue(testData);
        if (expectedQueue.length() > 0 && StringUtil.isWhitespace(expectedQueue.charAt(tokenQueue.pos))) {
            assertTrue(tokenQueue.matchesWhitespace());
        } else {
            assertFalse(tokenQueue.matchesWhitespace());
        }
        tokenQueue.advance();
        assertTrue(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        tokenQueue = new TokenQueue(testData);
        if (expectedQueue.length() > 0 && Character.isLetterOrDigit(expectedQueue.charAt(tokenQueue.pos))) {
            assertTrue(tokenQueue.matchesWord());
        } else {
            assertFalse(tokenQueue.matchesWord());
        }
        tokenQueue.advance();
        assertTrue(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        tokenQueue = new TokenQueue(testData);
        assertEquals(expectedQueue.charAt(tokenQueue.pos), tokenQueue.peek());
        tokenQueue.advance();
        assertEquals(expectedQueue.charAt(tokenQueue.pos), tokenQueue.peek());
        assertEquals(expectedQueue.substring(0, tokenQueue.pos + 1), tokenQueue.consume(1).toString());
        assertEquals(expectedQueue.substring(tokenQueue.pos + 1), tokenQueue.queue);
    }

    @Test
    public void testConsume() {
        tokenQueue = new TokenQueue(testData);
        String testString = "test";
        assertEquals(testString, tokenQueue.consume(testString.length()));
        assertEquals(expectedQueue.substring(tokenQueue.pos), tokenQueue.queue);
        assertEquals(expectedQueue.length() - testString.length(), tokenQueue.remainingLength());
    }

    @Test
    public void testConsumeTagName() {
        tokenQueue = new TokenQueue(testData);
        assertEquals(tokenQueue.consumeTagName(), tokenQueue.consumeToIgnoreCase("html"));
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeElementSelector() {
        tokenQueue = new TokenQueue(testData);
        assertEquals(tokenQueue.consumeElementSelector(), tokenQueue.consumeToIgnoreCase("html<head<title"));
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeCssIdentifier() {
        tokenQueue = new TokenQueue(testData);
        assertEquals(tokenQueue.consumeCssIdentifier(), tokenQueue.consumeToIgnoreCase("title"));
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeAttributeKey() {
        tokenQueue = new TokenQueue(testData + " class=\"class1\" id=\"id1\"");
        assertEquals(tokenQueue.consumeAttributeKey(), tokenQueue.consumeToIgnoreCase("class"));
        assertEquals(tokenQueue.consumeAttributeKey(), tokenQueue.consumeToIgnoreCase("id"));
        assertTrue(tokenQueue.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithNoMatch() {
        tokenQueue = new TokenQueue(testData);
        String testString = "test";
        tokenQueue.consume(testString);
        fail("Expected IllegalStateException when no match found");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeToWithNoMatch() {
        tokenQueue = new TokenQueue(testData);
        String testString = "test";
        tokenQueue.consumeTo(testString);
        fail("Expected IllegalStateException when no match found");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeToAnyWithNoMatch() {
        tokenQueue = new TokenQueue(testData);
        String[] testStrings = {"test1", "test2"};
        tokenQueue.consumeToAny(testStrings);
        fail("Expected IllegalStateException when no match found");
    }

    @Test
    public void testChompBalanced() {
        tokenQueue = new TokenQueue(testData + " (test (test) test)");
        assertEquals("test (test) test", tokenQueue.chompBalanced('(', ')'));
        assertEquals("test", tokenQueue.chompBalanced('(', ')').substring(0, tokenQueue.pos));
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testUnescape() {
        assertEquals(testData, TokenQueue.unescape(TokenQueue.escape(testData)));
    }

    @Test
    public void testConsumeWhitespace() {
        tokenQueue = new TokenQueue(testData);
        tokenQueue.consumeWhitespace();
        tokenQueue.consumeWhitespace();
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeWord() {
        tokenQueue = new TokenQueue(testData);
        assertEquals(tokenQueue.consumeWord(), tokenQueue.consumeToIgnoreCase("html"));
    }

    @Test
    public void testConsumeTagNameIgnoreCase() {
        tokenQueue = new TokenQueue(testData);
        assertEquals(tokenQueue.consumeTagNameIgnoreCase(), tokenQueue.tagName());
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeElementSelectorIgnoreCase() {
        tokenQueue = new TokenQueue(testData);
        assertEquals(tokenQueue.consumeElementSelectorIgnoreCase(), tokenQueue.elementSelector());
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeCssIdentifierIgnoreCase() {
        tokenQueue = new TokenQueue(testData);
        assertEquals(tokenQueue.consumeCssIdentifierIgnoreCase(), tokenQueue.cssIdentifier());
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeAttributeKeyIgnoreCase() {
        tokenQueue = new TokenQueue(testData + " class=\"class1\" id=\"id1\"");
        assertEquals(tokenQueue.consumeAttributeKeyIgnoreCase(), tokenQueue.attributeKey());
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testRemainder() {
        tokenQueue = new TokenQueue(testData);
        String testString = tokenQueue.consumeToAny(testStrings());
        assertEquals(testString, tokenQueue.remainder());
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testToString() {
        tokenQueue = new TokenQueue(testData);
        assertEquals(testData.substring(tokenQueue.pos), tokenQueue.toString());
    }

    private List<String> testStrings() {
        return Arrays.asList("test1", "test2");
    }

}