import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        String input = "test";
        String expectedOutput = "test";
        assertEquals(expectedOutput, translator.translate(input));
    }
    
    @Test
    public void testTranslate_nullInput() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        assertNull(translator.translate(null));
    }
    
    @Test
    public void testTranslateWriter_nullWriter() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        String input = "test";
        try {
            translator.translate(input, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected exception
        } catch (IOException e) {
            fail("Expected IllegalArgumentException to be thrown");
        }
    }
    
    @Test
    public void testTranslateWriter() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(input.toString().toUpperCase());
                return input.length();
            }
        };
        String input = "test";
        StringWriter writer = new StringWriter();
        try {
            translator.translate(input, writer);
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }
        String expectedOutput = "TEST";
        assertEquals(expectedOutput, writer.toString());
    }
    
    @Test
    public void testTranslateIndex_outOfBounds() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        String input = "test";
        try {
            translator.translate(input, 5, new StringWriter());
            fail("Expected ArrayIndexOutOfBoundsException to be thrown");
        } catch (ArrayIndexOutOfBoundsException e) {
            // expected exception
        } catch (IOException e) {
            fail("Expected ArrayIndexOutOfBoundsException to be thrown");
        }
    }
    
    @Test
    public void testTranslateIndex() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(String.valueOf(input.charAt(index)));
                return 1;
            }
        };
        String input = "test";
        StringWriter writer = new StringWriter();
        try {
            translator.translate(input, 0, writer);
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }
        String expectedOutput = "t";
        assertEquals(expectedOutput, writer.toString());
    }
    
    @Test
    public void testWith() {
        final CharSequenceTranslator translator1 = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(input.toString().toUpperCase());
                return input.length();
            }
        };
        final CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(input.toString().toLowerCase());
                return input.length();
            }
        };
        CharSequenceTranslator translator = translator1.with(translator2);
        String input = "test";
        String expectedOutput = "test";
        assertEquals(expectedOutput, translator.translate(input));
    }
    
    @Test
    public void testHex() {
        for (int i = 0; i < 128; i++) {
            String expectedOutput = Integer.toHexString(i).toUpperCase();
            assertEquals(expectedOutput, CharSequenceTranslator.hex(i));
        }
    }
}