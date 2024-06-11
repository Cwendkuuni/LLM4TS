import static org.junit.Assert.*;
import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateWithIndexAndWriter() {
        // Test normal functionality
        CharSequenceTranslator translator = new SampleTranslator();
        StringWriter out = new StringWriter();
        try {
            int consumed = translator.translate("hello", 0, out);
            assertEquals(5, consumed);
            assertEquals("hello", out.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
        
        // Test empty input
        out = new StringWriter();
        try {
            int consumed = translator.translate("", 0, out);
            assertEquals(0, consumed);
            assertEquals("", out.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
        
        // Test null input
        out = new StringWriter();
        try {
            int consumed = translator.translate(null, 0, out);
            assertEquals(0, consumed);
            assertEquals("", out.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
        
        // Test IOException from Writer
        CharSequenceTranslator errorTranslator = new ErrorTranslator();
        StringWriter errorOut = new StringWriter() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }
        };
        try {
            errorTranslator.translate("error", 0, errorOut);
            fail("IOException should be thrown");
        } catch (IOException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testTranslateWithStringOutput() {
        CharSequenceTranslator translator = new SampleTranslator();
        
        // Test normal functionality
        String translated = translator.translate("hello");
        assertEquals("hello", translated);
        
        // Test empty input
        translated = translator.translate("");
        assertEquals("", translated);
        
        // Test null input
        translated = translator.translate(null);
        assertNull(translated);
    }
    
    @Test
    public void testTranslateToWriter() {
        // Test normal functionality
        CharSequenceTranslator translator = new SampleTranslator();
        StringWriter out = new StringWriter();
        try {
            translator.translate("hello", out);
            assertEquals("hello", out.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
        
        // Test empty input
        out = new StringWriter();
        try {
            translator.translate("", out);
            assertEquals("", out.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
        
        // Test null input
        out = new StringWriter();
        try {
            translator.translate(null, out);
            assertEquals("", out.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
        
        // Test null Writer
        try {
            translator.translate("error", null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testWith() {
        // Test normal functionality
        CharSequenceTranslator translator = new SampleTranslator();
        CharSequenceTranslator withTranslator = translator.with(new SampleTranslator());
        
        assertNotNull(withTranslator);
        assertTrue(withTranslator instanceof AggregateTranslator);
    }

    @Test
    public void testHex() {
        // Test codepoint conversion
        assertEquals("41", CharSequenceTranslator.hex(65));
        
        // Test lowercase codepoint conversion
        assertEquals("7A", CharSequenceTranslator.hex(122));
    }
}