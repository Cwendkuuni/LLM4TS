package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for TokenQueue.
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
        assertTrue(queue.remainingLength() == 4);

        queue.advance();
        assertTrue(queue.remainingLength() == 3);

        queue.consume('t');
        assertTrue(queue.remainingLength() == 2);

        queue.consume('e');
        assertTrue(queue.remainingLength() == 1);

        queue.consume('s');
        assertFalse(queue.remainingLength() > 0);
    }

    /**
     * Test peek method.
     */
    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("test");
        assertTrue(queue.peek() == 't');

        queue.advance();
        assertTrue(queue.peek() == 'e');

        queue.consume('t');
        assertTrue(queue.peek() == 'e');

        queue.consume('e');
        assertFalse(queue.peek() > 0);
    }

    /**
     * Test addFirst method.
     */
    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("test");
        queue.addFirst('a');
        assertTrue(queue.toString().equals("atest"));
    }

    /**
     * Test matches method.
     */
    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("test");
        assertTrue(queue.matches("es"));
        assertFalse(queue.matches("est"));
        assertFalse(queue.matches("as"));

        queue.advance();
        assertTrue(queue.matches("es"));

        queue.consume('t');
        assertFalse(queue.matches("es"));
    }

    /**
     * Test matchesCS method.
     */
    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("Test");
        assertTrue(queue.matchesCS("Test"));
        assertFalse(queue.matchesCS("test"));
    }

    /**
     * Test matchesAny method.
     */
    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("test");
        assertTrue(queue.matchesAny("es", "st"));
        assertTrue(queue.matchesAny("est"));
        assertFalse(queue.matchesAny("as"));
    }

    /**
     * Test matchesAny with char array.
     */
    @Test
    public void testMatchesAnyChars() {
        TokenQueue queue = new TokenQueue("test");
        assertTrue(queue.matchesAny(new char[]{'s', 't'}));
        assertTrue(queue.matchesAny(new char[]{'e', 's'}));
        assertFalse(queue.matchesAny(new char[]{'a'}));
    }

    /**
     * Test matchesStartTag method.
     */
    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<test>");
        assertTrue(queue.matchesStartTag());

        queue.consume('<');
        assertFalse(queue.matchesStartTag());
    }

    /**
     * Test matchChomp method.
     */
    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("test<es>");
        assertTrue(queue.matchChomp("es"));

        assertTrue(queue.remainingLength() == 0);
        assertFalse(queue.matchChomp("es"));
    }

    /**
     * Test matchesWhitespace method.
     */
    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" test ");
        assertTrue(queue.matchesWhitespace());

        queue.consume(' ');
        assertFalse(queue.matchesWhitespace());
    }

    /**
     * Test matchesWord method.
     */
    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("test");
        assertTrue(queue.matchesWord());

        queue.consume('t');
        assertTrue(queue.matchesWord());

        queue.consume('e');
        assertFalse(queue.matchesWord());
    }

    /**
     * Test advance method.
     */
    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("test");
        queue.advance();
        assertTrue(queue.peek() == 'e');

        queue.consume('e');
        queue.advance();
        assertFalse(queue.remainingLength() > 0);
    }

    /**
     * Test consume method.
     */
    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("test");
        queue.consume();
        assertTrue(queue.peek() == 'e');

        queue.consume('e');
        assertTrue(queue.peek() == 's');

        queue.consume('s');
        assertFalse(queue.remainingLength() > 0);
    }

    /**
     * Test consume method with String arg.
     */
    @Test
    public void testConsumeString() {
        TokenQueue queue = new TokenQueue("test");
        queue.consume("st");
        assertTrue(queue.remainingLength() == 1);
        assertTrue(queue.peek() == 't');

        queue.consume("es");
        assertTrue(queue.remainingLength() == 0);
        assertFalse(queue.peek() > 0);
    }

    /**
     * Test consumeTo method.
     */
    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("test<es>");
        String consumed = queue.consumeTo("es");
        assertTrue(queue.remainingLength() == 0);
        assertTrue(consumed.equals("test<"));
    }

    /**
     * Test consumeToIgnoreCase method.
     */
    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("Test<Es>");
        String consumed = queue.consumeToIgnoreCase("es");
        assertTrue(queue.remainingLength() == 0);
        assertTrue(consumed.equals("Test<"));
    }

    /**
     * Test consumeToAny method.
     */
    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("test<es>");
        String consumed = queue.consumeToAny("st", "es");
        assertTrue(queue.remainingLength() == 0);
        assertTrue(consumed.equals("test<"));

        queue = new TokenQueue("<test>");
        consumed = queue.consumeToAny("st", "es", "e");
        assertTrue(queue.remainingLength() == 0);
        assertTrue(consumed.equals("<test>"));
    }

    /**
     * Test chompBalanced method.
     */
    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(test) (test) (test) test");
        String consumed = queue.chompBalanced('(', ')');
        assertTrue(queue.remainingLength() == 4);
        assertTrue(consumed.equals("testtest"));
    }

    /**
     * Test unescape method.
     */
    @Test
    public void testUnescape() {
        String escaped = "\\\\test\\\\\\\\test\\\\\\";
        String unescaped = TokenQueue.unescape(escaped);
        assertTrue(unescaped.equals("testtest"));
    }

    /**
     * Test consumeWhitespace method.
     */
    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" test ");
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.matchesWhitespace());

        queue.consume(' ');
        assertFalse(queue.matchesWhitespace());
    }

    /**
     * Test consumeWord method.
     */
    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("test");
        String word = queue.consumeWord();
        assertTrue(queue.remainingLength() == 0);
        assertTrue(word.equals("test"));

        queue = new TokenQueue("test123 test");
        word = queue.consumeWord();
        assertTrue(queue.remainingLength() == 4);
        assertTrue(word.equals("test123"));
    }

    /**
     * Test consumeTagName method.
     */
    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("test<tag>");
        String tagName = queue.consumeTagName();
        assertTrue(queue.remainingLength() == 6);
        assertTrue(tagName.equals("tag"));

        queue.consume('t');
        tagName = queue.consumeTagName();
        assertTrue(queue.remainingLength() == 5);
        assertTrue(tagName.equals("ag"));

        queue.consume('<');
        queue.consume('t');
        tagName = queue.consumeTagName();
        assertTrue(queue.remainingLength() == 4);
        assertTrue(tagName.equals("ag"));

        queue.consume('>');
        assertFalse(queue.remainingLength() > 0);
    }

    /**
     * Test consumeElementSelector method.
     */
    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("test .class:tag[attr='value']");
        String elementSelector = queue.consumeElementSelector();
        assertTrue(queue.remainingLength() == 32);
        assertTrue(elementSelector.equals(".class:tag[attr='value']"));

        queue.consume('t');
        queue.consume('.');
        queue.consume('c');
        queue.consume('l');
        queue.consume('a');
        queue.consume('s');
        queue.consume('s');
        queue.consume(':');
        queue.consume('t');
        queue.consume('a');
        queue.consume('g');
        queue.consume('[');
        queue.consume('a');
        queue.consume('t');
        queue.consume('t');
        queue.consume('=');
        queue.consume('v');
        queue.consume('a');
        queue.consume('l');
        queue.consume('u');
        queue.consume('e');
        queue.consume(']');
        queue.consume(' ');

        assertFalse(queue.remainingLength() > 0);
    }

    /**
     * Test consumeCssIdentifier method.
     */
    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("test test-class class-test id-test idClass test-id");
        String cssIdentifier = queue.consumeCssIdentifier();
        assertTrue(queue.remainingLength() == 29);
        assertTrue(cssIdentifier.equals("test-class"));

        queue.consume(' ');
        queue.consume('t');
        queue.consume('e');
        queue.consume('s');
        queue.consume('t');
        queue.consume('-');
        queue.consume('c');
        queue.consume('l');
        queue.consume('a');
        queue.consume('s');
        queue.consume('s');
        queue.consume(' ');
        queue.consume('i');
        queue.consume('d');
        queue.consume('C');
        queue.consume('l');
        queue.consume('a');
        queue.consume('s');
        queue.consume('s');
        queue.consume(' ');
        queue.consume('t');
        queue.consume('e');
        queue.consume('s');
        queue.consume('t');
        queue.consume('-');
        queue.consume('i');
        queue.consume('d');

        assertFalse(queue.remainingLength() > 0);
    }

    /**
     * Test consumeAttributeKey method.
     */
    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue(" test for='for' id='id' class='class' style='style'");
        String attributeKey = queue.consumeAttributeKey();
        assertTrue(queue.remainingLength() == 25);
        assertTrue(attributeKey.equals("for"));

        queue.consume(' ');
        queue.consume('t');
        queue.consume('h');
        queue.consume('e');
        queue.consume(' ');
        queue.consume('i');
        queue.consume('d');
        queue.consume(' ');
        queue.consume('c');
        queue.consume('l');
        queue.consume('a');
        queue.consume('s');
        queue.consume('s');
        queue.consume(' ');
        queue.consume('s');
        queue.consume('t');
        queue.consume('y');
        queue.consume('l');
        queue.consume('e');

        assertFalse(queue.remainingLength() > 0);
    }

    /**
     * Test remainder method.
     */
    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("test");
        String remainder = queue.remainder();
        assertTrue(queue.remainingLength() == 0);
        assertTrue(remainder.equals(""));

        queue = new TokenQueue("testremainder");
        remainder = queue.remainder();
        assertTrue(queue.remainingLength() == 0);
        assertTrue(remainder.equals("remainder"));
    }
}