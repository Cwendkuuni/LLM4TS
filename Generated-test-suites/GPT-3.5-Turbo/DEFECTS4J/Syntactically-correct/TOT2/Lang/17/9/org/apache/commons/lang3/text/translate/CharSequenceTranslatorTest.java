import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new ConcreteTranslator();
        String input = "hello";
        String expectedOutput = "HELLO";
        assertEquals(expectedOutput, translator.translate(input));
    }

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new ConcreteTranslator();
        assertNull(translator.translate(null));
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new ConcreteTranslator();
        CharSequenceTranslator translator2 = new ConcreteTranslator();
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        String input = "hello";
        String expectedOutput = "HELLO";
        assertEquals(expectedOutput, mergedTranslator.translate(input));
    }

    @Test
    public void testHex() {
        int codepoint = 65;
        String expectedHex = "41";
        assertEquals(expectedHex, CharSequenceTranslator.hex(codepoint));
    }

    @Test(expected = IOException.class)
    public void testTranslateWithIOException() throws IOException {
        CharSequenceTranslator translator = new TranslatorWithIOException();
        translator.translate("hello");
    }
}

class ConcreteTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        return out.write(input.toString().toUpperCase());
    }
}

class TranslatorWithIOException extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        throw new IOException();
    }
}