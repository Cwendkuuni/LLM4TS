import org.junit.Test;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

    @Test
    public void testTranslate() throws Exception {
        LookupTranslator translator = new LookupTranslator();
        StringBuilder input = new StringBuilder("hello");
        StringBuilder output = new StringBuilder();
        int index = 0;
        int result = translator.translate(input, index, output);
        assertEquals(0, result);
        assertEquals("", output.toString());
        
        translator = new LookupTranslator(new CharSequence[][]{{"hello", "world"}});
        input = new StringBuilder("hello");
        output = new StringBuilder();
        index = 0;
        result = translator.translate(input, index, output);
        assertEquals(5, result);
        assertEquals("world", output.toString());

        translator = new LookupTranslator(new CharSequence[][]{{"hello", "world"}});
        input = new StringBuilder("hello world");
        output = new StringBuilder();
        index = 6;
        result = translator.translate(input, index, output);
        assertEquals(5, result);
        assertEquals("world", output.toString());

        translator = new LookupTranslator(new CharSequence[][]{{"hello", "world"}, {"world", "hello"}});
        input = new StringBuilder("hello world");
        output = new StringBuilder();
        index = 0;
        result = translator.translate(input, index, output);
        assertEquals(5, result);
        assertEquals("world", output.toString());

        translator = new LookupTranslator(new CharSequence[][]{{"hello", "world"}, {"world", "hello"}});
        input = new StringBuilder("hello world");
        output = new StringBuilder();
        index = 6;
        result = translator.translate(input, index, output);
        assertEquals(5, result);
        assertEquals("hello", output.toString());

        translator = new LookupTranslator(new CharSequence[][]{{"hello", "world"}, {"world", "hello"}});
        input = new StringBuilder("hello world");
        output = new StringBuilder();
        index = 0;
        result = translator.translate(input, index, output);
        assertEquals(5, result);
        assertEquals("world", output.toString());

        translator = new LookupTranslator(new CharSequence[][]{{"hello", "world"}, {"world", "hello"}});
        input = new StringBuilder("hello world");
        output = new StringBuilder();
        index = 6;
        result = translator.translate(input, index, output);
        assertEquals(5, result);
        assertEquals("hello", output.toString());
    }
}