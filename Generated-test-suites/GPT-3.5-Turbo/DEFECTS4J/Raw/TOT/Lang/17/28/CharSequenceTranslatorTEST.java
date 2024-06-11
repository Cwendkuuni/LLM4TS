import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class CharSequenceTranslatorTest {
    
    private CharSequenceTranslator translator;
    
    @Before
    public void setUp() {
        translator = new TestTranslator();
    }
    
    @Test
    public void testTranslate_StringWriter() throws IOException {
        CharSequence input = "Hello World";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        assertEquals("TestTranslator", writer.toString());
    }
    
    @Test
    public void testTranslate_String() {
        CharSequence input = "Hello World";
        String output = translator.translate(input);
        assertEquals("TestTranslator", output);
    }
    
    @Test
    public void testWith() {
        CharSequenceTranslator anotherTranslator = new TestTranslator();
        CharSequenceTranslator mergedTranslator = translator.with(anotherTranslator);
        assertNotNull(mergedTranslator);
    }
    
    @Test
    public void testHex() {
        int codepoint = 65;
        String hexString = "41";
        assertEquals(hexString, CharSequenceTranslator.hex(codepoint));
    }
    
    private static class TestTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write("TestTranslator");
            return input.length();
        }
        
    }

}