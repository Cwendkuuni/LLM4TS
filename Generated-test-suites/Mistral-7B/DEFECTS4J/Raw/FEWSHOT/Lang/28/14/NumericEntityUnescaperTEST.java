package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        CharSequence input = "&amp;&#x61;&#x62;&#x63;&#x64;";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        };

        for (int i = 0; i < input.length(); i++) {
            int length = translator.translate(input, i, writer);
            writer.flush();
            assertEquals(new String(outputStream.toByteArray(), 0, length), Character.toString((char) (i + 1)));
            outputStream.reset();
        }

        input = "&amp;&#032;&#x7f;&#061;&#160;&#x1f468;&#x1f384;&#x1f385;&#x2642;&#x2640;&#x2665;";
        writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        };

        for (int i = 0; i < input.length(); i++) {
            int length = translator.translate(input, i, writer);
            writer.flush();
            assertEquals(new String(outputStream.toByteArray(), 0, length), Character.toString((char) (i + 1)));
            outputStream.reset();
        }

        input = "&amp;";
        writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        };

        int length = translator.translate(input, 0, writer);
        writer.flush();
        assertEquals("", Character.toString((char) length));

        input = "&amp;";
        writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        };

        length = translator.translate(input, -1, writer);
        writer.flush();
        assertNull(Character.toString((char) length));

        input = "&";
        writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        };

        length = translator.translate(input, 0, writer);
        writer.flush();
        assertNull(Character.toString((char) length));

        input = "&";
        writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        };

        length = translator.translate(input, -1, writer);
        writer.flush();
        assertNull(Character.toString((char) length));
    }
}