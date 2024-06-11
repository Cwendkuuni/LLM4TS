package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

import java.io.ByteArrayReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * Test file for CharacterReader class.
 */
public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        String input = "<html><body><h1>Hello World</h1></body></html>";
        reader = new CharacterReader(new StringReader(input));
    }

    @Test
    public void testNewInstance() {
        CharacterReader reader1 = new CharacterReader(new StringReader(""));
        CharacterReader reader2 = new CharacterReader(new ByteArrayReader(new byte[0]));
        CharacterReader reader3 = new CharacterReader(null);

        assertThat(reader1.isEmpty(), is(true));
        assertThat(reader2.isEmpty(), is(true));
        assertThat(reader3, is(nullValue()));
    }

    @Test
    public void testNewInstanceWithStringInput() {
        CharacterReader reader = new CharacterReader("Hello");
        assertThat(reader.current(), is('H'));
        assertThat(reader.consume(), is('e'));
        assertThat(reader.isEmpty(), is(false));
        assertThat(reader.pos(), is(2));
        assertThat(reader.nextIndexOf('l'), is(1));
        assertThat(reader.nextIndexOf("ll"), is(-1));
        reader.rewindToMark();
        reader.consumeLetterSequence();
        assertThat(reader.consumeLetter(), is('o'));
        reader.rewindToMark();
        reader.consumeTo('l');
        assertThat(reader.consume(), is('o'));
        reader.rewindToMark();
        String consumed = reader.consumeTo("ll");
        assertThat(consumed, is("ll"));
        reader.consumeToEnd();
        assertThat(reader.consume(), is((char) -1));
        reader.rewindToMark();
        reader.consumeLetterThenDigitSequence();
        assertThat(reader.consumeDigit(), is('0'));
        reader.rewindToMark();
        reader.consumeToAny(new char[]{'l', 'o'} );
        assertThat(reader.consume(), is('l'));
        reader.rewindToMark();
        reader.consumeLetter();
        reader.matches('l');
        reader.matches("ll");
        reader.matchesAny('l');
        reader.matchesAny(new char[]{'l', 'o'});
        reader.matchesLetter();
        reader.matchesDigit();
        reader.mark();
        reader.advance();
        reader.consume();
        reader.reedwToMark();
        reader.advance();
        reader.rewindToMark();
        reader.rewindToMark();
        reader.consumeToEnd();
        reader.rewindToMark();
        reader.consumeTo("</title>");
        reader.consumeToAnySorted(new char[]{'t', 'i', 't', 'l', 'e'} );
        reader.consumeTo("</style>");
        reader.consumeToAnySorted(new char[]{'s', 't', 'y', 'l', 'e'} );
        reader.toString();
    }

    @Test(expected = UncheckedIOException.class)
    public void testExceptionThrown() throws IOException {
        reader.bufferUp();
        reader.reader.skip(10000);
    }

    @After
    public void tearDown() throws IOException {
        reader.reader.close();
    }
}