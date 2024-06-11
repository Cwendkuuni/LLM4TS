package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.StringUtil;
import org.jsoup.parser.TokenQueue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * Unit tests for the {@link TokenQueue} class.
 */
public class TokenQueueTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        TokenQueue queue = new TokenQueue("test");
        assertNotNull(queue);
    }

    /**
     * Test isEmpty method.
     */
    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
        
        queue = new TokenQueue("test");
        assertFalse(queue.isEmpty());
    }

    /**
     * Test remainingLength method.
     */
    @Test
    public void testRemainingLength() {
        TokenQueue queue = new TokenQueue("test");
        assertEquals(4, queue.remainingLength());
    }

    /**
     * Test peek method.
     */
    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("test");
        assertEquals('t', queue.peek());
    }

    /**
     * Test addFirst method.
     */
    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("test");
        queue.addFirst('x');
        assertEquals(5, queue.remainingLength());
        assertEquals('x', queue.peek());
    }

    /**
     * Test matches method (case-insensitive).
     */
    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("test");
        assertTrue(queue.matches("te"));
        assertTrue(queue.matches("test"));
        assertFalse(queue.matches("tes"));
        assertFalse(queue.matches("tester"));
    }

    /**
     * Test matchesCS method.
     */
    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("test");
        assertTrue(queue.matchesCS("te"));
        assertTrue(queue.matchesCS("test"));
        assertFalse(queue.matchesCS("tes"));
        assertFalse(queue.matchesCS("tester"));
    }

    /**
     * Test matchesAny method.
     */
    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("test");
        assertTrue(queue.matchesAny("te", "es"));
        assertTrue(queue.matchesAny("test"));
        assertFalse(queue.matchesAny("tester"));
    }

    /**
     * Test matchesAny method with multiple arguments.
     */
    @Test
    public void testMatchesAnyMultiple() {
        TokenQueue queue = new TokenQueue("test");
        assertTrue(queue.matchesAny("te", "es", "st"));
        assertTrue(queue.matchesAny("test"));
        assertFalse(queue.matchesAny("tester"));
    }

    /**
     * Test matchesStartTag method.
     */
    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<test>");
        assertTrue(queue.matchesStartTag());
        assertTrue(queue.matchesStartTag()); // consume '<'
        assertFalse(queue.matchesStartTag()); // consume 't'
    }

    /**
     * Test matchChomp method.
     */
    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("testte");
        assertTrue(queue.matchChomp("te"));
        assertEquals(2, queue.remainingLength());
        assertTrue(queue.matches("st"));
    }

    /**
     * Test matchesWhitespace method.
     */
    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" test ");
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.matchesWhitespace()); // consume space
        assertFalse(queue.matchesWhitespace()); // consume 't'
    }

    /**
     * Test matchesWord method.
     */
    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("test");
        assertTrue(queue.matchesWord());
        assertTrue(queue.matchesWord()); // consume 't'
        assertFalse(queue.matchesWord()); // consume 'e'
    }

    /**
     * Test consume method with a single character.
     */
    @Test
    public void testConsumeSingle() {
        TokenQueue queue = new TokenQueue("test");
        assertEquals('t', queue.consume());
        assertEquals('e', queue.peek());
    }

    /**
     * Test consume method with a multi-character sequence.
     */
    @Test
    public void testConsumeSequence() {
        TokenQueue queue = new TokenQueue("test");
        assertEquals("est", queue.consume(2));
        assertEquals("st", queue.remainder());
    }

    /**
     * Test consumeTo method with a single character.
     */
    @Test
    public void testConsumeToSingle() {
        TokenQueue queue = new TokenQueue("test");
        assertEquals("tes", queue.consumeTo('s'));
        assertEquals("", queue.remainder());
    }

    /**
     * Test consumTo method with a multi-character sequence.
     */
    @Test
    public void testConsumeToSequence() {
        TokenQueue queue = new TokenQueue("testest");
        assertEquals("test", queue.consumeTo("est"));
        assertEquals("", queue.remainder());
    }

    /**
     * Test consumTo method with a case-insensitive sequence.
     */
    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("TestESTesT");
        assertEquals("TestEST", queue.consumeToIgnoreCase("TestEST"));
        assertEquals("esT", queue.remainder());
    }

    /**
     * Test consumToAny method with a single character.
     */
    @Test
    public void testConsumeToAnySingle() {
        TokenQueue queue = new TokenQueue("test");
        assertEquals("test", queue.consumeToAny("t"));
        assertEquals("", queue.remainder());
    }

    /**
     * Test consumToAny method with multiple characters.
     */
    @Test
    public void testConsumeToAnyMultiple() {
        TokenQueue queue = new TokenQueue("testtest");
        assertEquals("testtest", queue.consumeToAny("es", "t"));
        assertEquals("", queue.remainder());
    }

    /**
     * Test chompBalanced method with single quotes.
     */
    @Test
    public void testChompBalancedSingleQuotes() {
        TokenQueue queue = new TokenQueue("'test'test");
        assertEquals("test", queue.chompBalanced('\'', '\''));
        assertEquals("test", queue.remainder());
    }

    /**
     * Test chompBalanced method with double quotes.
     */
    @Test
    public void testChompBalancedDoubleQuotes() {
        TokenQueue queue = new TokenQueue("\"test\"test");
        assertEquals("test", queue.chompBalanced('\"', '\"'));
        assertEquals("test", queue.remainder());
    }

    /**
     * Test chompBalanced method with unescaped escape character.
     */
    @Test
    public void testChompBalancedEscape() {
        TokenQueue queue = new TokenQueue("\\'test\\'test");
        assertEquals("test", queue.chompBalanced('\'', '\''));
        assertEquals("test", queue.remainder());
    }

    /**
     * Test chompBalanced method with escaped escape character.
     */
    @Test
    public void testChompBalancedEscapedEscape() {
        TokenQueue queue = new TokenQueue("\\\\'test\\\\'test");
        assertEquals("test", queue.chompBalanced('\\', '\''));
        assertEquals("test", queue.remainder());
    }

    /**
     * Test unescape method.
     */
    @Test
    public void testUnescape() {
        String input = "\\\\test\\\\";
        String output = TokenQueue.unescape(input);
        assertEquals("test", output);
    }

    /**
     * Test consumeWhitespace method.
     */
    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" test ");
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
        assertEquals("t", queue.remainder());
    }

    /**
     * Test consumeWord method.
     */
    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("testtest");
        assertEquals("test", queue.consumeWord());
        assertEquals("test", queue.remainder());
    }

    /**
     * Test consumeTagName method.
     */
    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("testtagtag");
        assertEquals("testtag", queue.consumeTagName());
        assertEquals("tag", queue.remainder());
    }

    /**
     * Test consumeElementSelector method.
     */
    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("test*|tagname:class affix");
        assertEquals("test*|tagname", queue.consumeElementSelector());
        assertEquals(":class affix", queue.remainder());
    }

    /**
     * Test consumeCssIdentifier method.
     */
    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("classid classID class_ID");
        assertEquals("classid", queue.consumeCssIdentifier());
        assertEquals("classID class_ID", queue.remainder());
    }

    /**
     * Test consumeAttributeKey method.
     */
    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class=class attribute=value attribute-name");
        assertEquals("class", queue.consumeAttributeKey());
        assertEquals("attribute=value attribute-name", queue.remainder());
    }

    /**
     * Test remainder method.
     */
    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("test");
        assertEquals("", queue.remainder());
        queue = new TokenQueue("testremainder");
        List<String> expected = Arrays.asList("test", "remainder");
        assertEquals(expected.toString(), queue.toString());
    }
}