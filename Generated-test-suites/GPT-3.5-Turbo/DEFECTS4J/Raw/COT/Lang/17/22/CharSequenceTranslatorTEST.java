import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() throws Exception {
        // Test null input
        CharSequenceTranslator translator = new ConcreteTranslator();
        assertNull(translator.translate(null));
        
        // Test empty input
        assertEquals("", translator.translate(""));
        
        // Test basic translation
        assertEquals("Hello, world!", translator.translate("Hello, world!"));
        
        // Test translation with code point
        assertEquals("\\u0048\\u0065\\u006c\\u006c\\u006f, world!", translator.translate("Hello, world!"));
        
        // Test translation with code point that requires surrogate pair
        assertEquals("\\uD83D\\uDE0D", translator.translate("\uD83D\uDE0D"));
    }
    
    @Test
    public void testTranslate_withWriter() throws Exception {
        // Test null input
        CharSequenceTranslator translator = new ConcreteTranslator();
        StringWriter writer = new StringWriter();
        translator.translate(null, writer);
        assertEquals("", writer.toString());
        
        // Test empty input
        writer = new StringWriter();
        translator.translate("", writer);
        assertEquals("", writer.toString());
        
        // Test basic translation
        writer = new StringWriter();
        translator.translate("Hello, world!", writer);
        assertEquals("Hello, world!", writer.toString());
        
        // Test translation with code point
        writer = new StringWriter();
        translator.translate("Hello, world!", writer);
        assertEquals("\\u0048\\u0065\\u006c\\u006c\\u006f, world!", writer.toString());
        
        // Test translation with code point that requires surrogate pair
        writer = new StringWriter();
        translator.translate("\uD83D\uDE0D", writer);
        assertEquals("\\uD83D\\uDE0D", writer.toString());
    }
    
    @Test(expected = IOException.class)
    public void testTranslate_withIOException() throws Exception {
        // Test IOException
        CharSequenceTranslator translator = new ConcreteTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IOException();
            }
        };
        translator.translate("Hello, world!");
    }
    
    @Test
    public void testWith() throws Exception {
        // Test with method
        CharSequenceTranslator translator1 = new ConcreteTranslator();
        CharSequenceTranslator translator2 = new ConcreteTranslator();
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        String input = "Hello, world!";
        assertEquals(translator2.translate(translator1.translate(input)), mergedTranslator.translate(input));
    }
    
    private static class ConcreteTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null) {
                return 0;
            }
            if (index < 0 || index >= input.length()) {
                return 0;
            }
            out.write(input.charAt(index));
            return 1;
        }
    }
}