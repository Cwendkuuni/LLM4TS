import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class CharSequenceTranslatorTest {

    private CharSequenceTranslator translator;

    @Before
    public void setUp() {
        translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
    }

    @Test
    public void testTranslate() {
        String input = "Hello World!";
        String expectedOutput = "Hello World!";
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testTranslate_NullInput() {
        String input = null;
        String output = translator.translate(input);
        assertNull(output);
    }

    @Test
    public void testTranslate_IOException() throws IOException {
        String input = "Hello World!";
        Writer writer = mock(Writer.class);
        doThrow(IOException.class).when(writer).write(any(char[].class));

        String output = translator.translate(input);
        assertNull(output);
    }

    @Test
    public void testTranslate_IndexBased() throws IOException {
        StringWriter writer = new StringWriter();
        CharSequence input = "Hello World!";
        int index = 0;

        int consumed = translator.translate(input, index, writer);
        assertEquals(0, consumed);

        String expectedOutput = "Hello World!";
        String output = writer.toString();
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testTranslate_IndexBased_IOException() throws IOException {
        Writer writer = mock(Writer.class);
        doThrow(IOException.class).when(writer).write(any(char[].class));

        CharSequence input = "Hello World!";
        int index = 0;
        int consumed = translator.translate(input, index, writer);
        assertEquals(0, consumed);
    }

    @Test
    public void testTranslate_IndexBased_MultiConsumable() throws IOException {
        StringWriter writer = new StringWriter();
        CharSequence input = "Hello World!";
        int index = 0;

        CharSequenceTranslator multiConsumable = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 2; // Consumes 2 codepoints
            }
        };

        int consumed = multiConsumable.translate(input, index, writer);
        assertEquals(2, consumed);

        String expectedOutput = "Hello World!";
        String output = writer.toString();
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testTranslate_IndexBased_MultiConsumable_EdgeCase() throws IOException {
        StringWriter writer = new StringWriter();
        CharSequence input = "HelloWorld!";
        int index = 0;

        CharSequenceTranslator multiConsumable = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (index == 5) {
                    return 3; // Consumes 3 codepoints
                } else {
                    return 0;
                }
            }
        };

        int consumed = multiConsumable.translate(input, index, writer);
        assertEquals(3, consumed);

        String expectedOutput = "Hello";
        String output = writer.toString();
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_NullWriter() {
        Writer writer = null;
        CharSequence input = "Hello World!";
        translator.translate(input, writer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_IndexBased_NullWriter() throws IOException {
        Writer writer = null;
        CharSequence input = "Hello World!";
        int index = 0;
        translator.translate(input, index, writer);
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = mock(CharSequenceTranslator.class);
        CharSequenceTranslator translator2 = mock(CharSequenceTranslator.class);
        CharSequenceTranslator mergedTranslator = translator.with(translator1, translator2);
        assertTrue(mergedTranslator instanceof AggregateTranslator);
    }

    @Test
    public void testHex() {
        int codepoint = 65;
        String expectedHex = "41";
        String hex = CharSequenceTranslator.hex(codepoint);
        assertEquals(expectedHex, hex);
    }
}