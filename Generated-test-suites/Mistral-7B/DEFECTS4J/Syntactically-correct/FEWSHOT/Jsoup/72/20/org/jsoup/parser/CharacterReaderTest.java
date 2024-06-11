package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Locale;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        Reader reader = new StringReader("abcdefghijklmnopqrstuvwxyz");
        CharacterReader cr = new CharacterReader(reader);

        assertEquals(0, cr.pos());
        assertEquals(true, cr.isEmpty());
        assertEquals(Character.MIN_VALUE, cr.current());
        assertEquals(Character.MIN_VALUE, cr.consume());

        cr.advance();
        assertEquals(1, cr.pos());
        assertEquals(false, cr.isEmpty());
        assertEquals('a', cr.current());
        assertEquals('a', cr.consume());

        cr.mark();
        cr.advance();
        cr.rewindToMark();
        assertEquals('a', cr.current());

        assertEquals(2, cr.nextIndexOf('b'));
        assertEquals(-1, cr.nextIndexOf('z'));

        assertEquals("a", cr.consumeTo('a'));
        assertEquals("bcdefghijklmnopqrstuvwxyz", cr.consumeToEnd());
        assertEquals("abc", cr.consumeTo("bc"));

        cr.consumeAny('a');
        cr.consumeAny('b');
        cr.consumeAnySorted('z', 'a');
        cr.consumeData();
        cr.consumeTagName();
    }

    @Test
    public void testCharacterReaderWithStringInput() {
        CharacterReader cr = new CharacterReader("abcdefghijklmnopqrstuvwxyz");

        assertEquals(0, cr.pos());
        assertEquals(true, cr.isEmpty());
        assertEquals(Character.MIN_VALUE, cr.current());
        assertEquals(Character.MIN_VALUE, cr.consume());

        cr.advance();
        assertEquals(1, cr.pos());
        assertEquals(false, cr.isEmpty());
        assertEquals('a', cr.current());
        assertEquals('a', cr.consume());

        cr.mark();
        cr.advance();
        cr.rewindToMark();
        assertEquals('a', cr.current());

        assertEquals(2, cr.nextIndexOf('b'));
        assertEquals(-1, cr.nextIndexOf('z'));

        assertEquals("a", cr.consumeTo('a'));
        assertEquals("bcdefghijklmnopqrstuvwxyz", cr.consumeToEnd());
        assertEquals("abc", cr.consumeTo("bc"));

        cr.consumeAny('a');
        cr.consumeAny('b');
        cr.consumeAnySorted('z', 'a');
        cr.consumeData();
        cr.consumeTagName();
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderWithIOException() throws IOException {
        Reader reader = new StringReader("abcdefghijklmnopqrstuvwxyz");
        CharacterReader cr = new CharacterReader(reader) {
            @Override
            public void bufferUp() throws IOException {
                throw new IOException("Test exception");
            }
        };

        cr.consume();
    }

    @Test
    public void testStringCache() {
        CharacterReader cr = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));

        String str1 = cr.consumeTo('a');
        String str2 = cr.consumeTo('a');
        String str3 = cr.consumeTo('a');
        String str4 = cr.consumeTo('a');
        String str5 = cr.consumeTo('a');

        assertArrayEquals(new String[]{"a", "a", "a", "a", "a"}, new String[]{str1, str2, str3, str4, str5});

        String str6 = cr.consumeToEnd();
        String str7 = cr.consumeToEnd();

        assertFalse(str6.equals(str7));
    }
}