package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class LookupTranslatorTest {

    private LookupTranslator translator;
    private Writer writer;

    @Before
    public void setUp() {
        writer = mock(Writer.class);
        translator = new LookupTranslator(new CharSequence[][] { { "foo", "bar" }, { "bar", "baz" } });
    }

    @Test
    public void testEmptyLookupMap() {
        assertNull(translator.translate(mock(CharSequence.class), 0, writer));
    }

    @Test
    public void testTranslationWithExactMatch() throws IOException {
        when(writer.write(mock(CharSequence.class).toString())).thenAnswer(invocation -> invocation.getArgument(0));

        translator.translate(mock(CharSequence.class).wrap("foo"), 0, writer);
        assertEquals("bar", translator.translate(mock(CharSequence.class).wrap("foo"), 0, writer));
        translator.translate(mock(CharSequence.class).wrap("FOO"), 0, writer);
        assertEquals("bar", translator.translate(mock(CharSequence.class).wrap("FOO"), 0, writer));

        translator.translate(mock(CharSequence.class).wrap("bar"), 0, writer);
        assertEquals("baz", translator.translate(mock(CharSequence.class).wrap("bar"), 0, writer));
        translator.translate(mock(CharSequence.class).wrap("BAR"), 0, writer);
        assertEquals("baz", translator.translate(mock(CharSequence.class).wrap("BAR"), 0, writer));
    }

    @Test
    public void testTranslationWithLongestMatch() throws IOException {
        when(writer.write(mock(CharSequence.class).toString())).thenAnswer(invocation -> invocation.getArgument(0));

        translator.translate(mock(CharSequence.class).wrap("foobar"), 0, writer);
        assertEquals("barbaz", translator.translate(mock(CharSequence.class).wrap("foobar"), 0, writer));

        translator.translate(mock(CharSequence.class).wrap("foofoo"), 0, writer);
        assertEquals("barbar", translator.translate(mock(CharSequence.class).wrap("foofoo"), 0, writer));

        translator.translate(mock(CharSequence.class).wrap("barbar"), 0, writer);
        assertEquals("bazbaz", translator.translate(mock(CharSequence.class).wrap("barbar"), 0, writer));
    }

    @Test
    public void testTranslationWithMultipleMatches() throws IOException {
        when(writer.write(mock(CharSequence.class).toString())).thenAnswer(invocation -> invocation.getArgument(0));

        translator.translate(mock(CharSequence.class).wrap("foobarfoo"), 0, writer);
        assertEquals("barbazbar", translator.translate(mock(CharSequence.class).wrap("foobarfoo"), 0, writer));

        translator.translate(mock(CharSequence.class).wrap("foobarfoofoo"), 0, writer);
        assertEquals("barbazbarbaz", translator.translate(mock(CharSequence.class).wrap("foobarfoofoo"), 0, writer));
    }

    @Test
    public void testTranslationWithNoMatch() throws IOException {
        when(writer.write(mock(CharSequence.class).toString())).thenAnswer(invocation -> invocation.getArgument(0));

        translator.translate(mock(CharSequence.class).wrap("qux"), 0, writer);
        assertEquals("", translator.translate(mock(CharSequence.class).wrap("qux"), 0, writer));
    }

    @Test
    public void testTranslationWithMismatchedLength() throws IOException {
        when(writer.write(mock(CharSequence.class).toString())).thenAnswer(invocation -> invocation.getArgument(0));

        translator.translate(mock(CharSequence.class).wrap("quxqux"), 0, writer);
        assertEquals("", translator.translate(mock(CharSequence.class).wrap("quxqux"), 0, writer));
    }
}

class ByteArrayOutputStreamWriter implements Writer {
    private final ByteArrayOutputStream outputStream;

    public ByteArrayOutputStreamWriter(ByteArrayOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void write(int c) throws IOException {
        outputStream.write(c);
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        outputStream.write(cbuf, off, len);
    }

    @Override
    public void write(CharSequence csq) throws IOException {
        outputStream.write(csq.toString().toCharArray(), 0, csq.length());
    }

    @Override
    public void flush() throws IOException {
        // No-op
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }
}