package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals("foo", reader.consumeToEnd());
        assertEquals('f', reader.consume());
        reader.unconsume();
        assertEquals('f', reader.current());
        reader.advance();
        assertEquals('o', reader.current());
        reader.mark();
        reader.rewindToMark();
        assertEquals('f', reader.current());
        reader.consume();
        reader.consume();
        reader.consume();
        assertEquals("ooo", reader.consumeTo("oo"));
        reader.consume();
        assertNull(reader.nextIndexOf('z'));
        reader.consumeTo('o');
        assertEquals("oo", reader.consumeTo("oo"));
        reader.bufferUp();
        reader.bufferUp();
        reader.consumeLetterSequence();
        reader.consumeLetterThenDigitSequence();
        reader.consumeHexSequence();
        reader.consumeDigitSequence();
        reader.mark();
        reader.rewindToMark();
        reader.consumeData();
        reader.consumeTagName();
        reader.matchConsume("foo");
        reader.matchConsumeIgnoreCase("FOO");
        reader.matchAny('o');
        reader.matchAnySorted(new char[]{'o', 'n'});
        reader.matches('o');
        reader.matches("foo");
        reader.matchesIgnoreCase("foo");
        reader.containsIgnoreCase("FOO");
        reader.isEmpty();
        reader.isEmptyNoBufferUp();
        reader.pos();
    }
}