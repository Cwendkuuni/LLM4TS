import static org.junit.Assert.*;
import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_ValidInput_ShouldReturnTranslatedValue() throws IOException {
        CharSequence[] lookup1 = { "cat", "dog" };
        CharSequence[] lookup2 = { "dog", "cat" };
        LookupTranslator translator = new LookupTranslator(lookup1, lookup2);
        StringBuilder result = new StringBuilder();
        translator.translate("I have a cat and a dog", 0, new Writer(){
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                result.append(new String(cbuf, off, len));
            }
            @Override
            public void flush() throws IOException {
            }
            @Override
            public void close() throws IOException {
            }
        });
        assertEquals("I have a dog and a cat", result.toString());
    }

    @Test
    public void testTranslate_InvalidInput_ShouldReturnZero() throws IOException {
        CharSequence[] lookup = { "cat", "dog" };
        LookupTranslator translator = new LookupTranslator(lookup);
        StringBuilder result = new StringBuilder();
        int actual = translator.translate("Invalid input", 0, new Writer(){
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                result.append(new String(cbuf, off, len));
            }
            @Override
            public void flush() throws IOException {
            }
            @Override
            public void close() throws IOException {
            }
        });
        assertEquals(0, actual);
        assertEquals("", result.toString());
    }

    @Test
    public void testTranslate_InputNotStartingFromZero_ShouldReturnTranslatedValue() throws IOException {
        CharSequence[] lookup = { "cat", "dog" };
        LookupTranslator translator = new LookupTranslator(lookup);
        StringBuilder result = new StringBuilder();
        translator.translate("I have a cat and a dog", 5, new Writer(){
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                result.append(new String(cbuf, off, len));
            }
            @Override
            public void flush() throws IOException {
            }
            @Override
            public void close() throws IOException {
            }
        });
        assertEquals("a cat and a dog", result.toString());
    }

    @Test
    public void testTranslate_LongLookups_ShouldReturnTranslatedValue() throws IOException {
        CharSequence[] lookup = { "abcdef", "1234" };
        LookupTranslator translator = new LookupTranslator(lookup);
        StringBuilder result = new StringBuilder();
        translator.translate("I have abcdef", 0, new Writer(){
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                result.append(new String(cbuf, off, len));
            }
            @Override
            public void flush() throws IOException {
            }
            @Override
            public void close() throws IOException {
            }
        });
        assertEquals("I have 1234", result.toString());
    }
}