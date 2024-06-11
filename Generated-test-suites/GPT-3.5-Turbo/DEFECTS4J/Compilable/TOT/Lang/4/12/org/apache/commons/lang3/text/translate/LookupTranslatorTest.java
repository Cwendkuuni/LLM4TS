package org.apache.commons.lang3.text.translate;
import org.junit.Test;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

import static org.junit.Assert.*;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_typicalUseCase() throws IOException {
        // Arrange
        CharSequence[] values = { "hello", "world" };
        LookupTranslator translator = new LookupTranslator(values);
        StringBuilder sb = new StringBuilder();

        // Act
        int result = translator.translate("hello world", 0, new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                sb.append(cbuf, off, len);
            }

            @Override
            public void flush() throws IOException {}

            @Override
            public void close() throws IOException {}
        });

        // Assert
        assertEquals(11, result);
        assertEquals("world world", sb.toString());
    }

    @Test
    public void testTranslate_emptyInputString() throws IOException {
        // Arrange
        CharSequence[] values = { "hello", "world" };
        LookupTranslator translator = new LookupTranslator(values);
        StringBuilder sb = new StringBuilder();

        // Act
        int result = translator.translate("", 0, new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                sb.append(cbuf, off, len);
            }

            @Override
            public void flush() throws IOException {}

            @Override
            public void close() throws IOException {}
        });

        // Assert
        assertEquals(0, result);
        assertEquals("", sb.toString());
    }

    @Test
    public void testTranslate_singleMatch() throws IOException {
        // Arrange
        CharSequence[] values = { "hello", "world" };
        LookupTranslator translator = new LookupTranslator(values);
        StringBuilder sb = new StringBuilder();

        // Act
        int result = translator.translate("hello", 0, new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                sb.append(cbuf, off, len);
            }

            @Override
            public void flush() throws IOException {}

            @Override
            public void close() throws IOException {}
        });

        // Assert
        assertEquals(5, result);
        assertEquals("world", sb.toString());
    }

    @Test
    public void testTranslate_multipleMatches() throws IOException {
        // Arrange
        CharSequence[] values = { "hello", "world", "hello", "universe" };
        LookupTranslator translator = new LookupTranslator(values);
        StringBuilder sb = new StringBuilder();

        // Act
        int result = translator.translate("hello world hello", 0, new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                sb.append(cbuf, off, len);
            }

            @Override
            public void flush() throws IOException {}

            @Override
            public void close() throws IOException {}
        });

        // Assert
        assertEquals(17, result);
        assertEquals("world world universe", sb.toString());
    }

    @Test
    public void testTranslate_noMatch() throws IOException {
        // Arrange
        CharSequence[] values = { "hello", "world" };
        LookupTranslator translator = new LookupTranslator(values);
        StringBuilder sb = new StringBuilder();

        // Act
        int result = translator.translate("test", 0, new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                sb.append(cbuf, off, len);
            }

            @Override
            public void flush() throws IOException {}

            @Override
            public void close() throws IOException {}
        });

        // Assert
        assertEquals(0, result);
        assertEquals("test", sb.toString());
    }

    @Test
    public void testTranslate_indexOutOfBounds() throws IOException {
        // Arrange
        CharSequence[] values = { "hello", "world" };
        LookupTranslator translator = new LookupTranslator(values);
        StringBuilder sb = new StringBuilder();

        // Act
        int result = translator.translate("test", 10, new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                sb.append(cbuf, off, len);
            }

            @Override
            public void flush() throws IOException {}

            @Override
            public void close() throws IOException {}
        });

        // Assert
        assertEquals(0, result);
        assertEquals("", sb.toString());
    }
}