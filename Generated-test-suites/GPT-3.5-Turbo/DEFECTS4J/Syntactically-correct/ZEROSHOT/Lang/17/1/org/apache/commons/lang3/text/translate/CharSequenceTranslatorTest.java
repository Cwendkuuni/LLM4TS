import static org.junit.Assert.*;
import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new TestTranslator();
        
        // Test null input
        assertNull(translator.translate(null));
        
        // Test empty input
        assertEquals("", translator.translate(""));
        
        // Test normal input
        assertEquals("ABC", translator.translate("ABC"));
        
        // Test input with special characters
        assertEquals("&lt;&gt;", translator.translate("<>"));
    }

    @Test
    public void testTranslateWithIndexAndWriter() {
        CharSequenceTranslator translator = new TestTranslator();
        StringWriter writer = new StringWriter();
        
        try {
            // Test null input
            assertEquals(0, translator.translate(null, 0, writer));
            assertEquals("", writer.toString());
            
            // Test empty input
            assertEquals(0, translator.translate("", 0, writer));
            assertEquals("", writer.toString());
            
            // Test normal input
            assertEquals(3, translator.translate("ABC", 0, writer));
            assertEquals("ABC", writer.toString());
            
            // Test input with special characters
            assertEquals(2, translator.translate("<>", 0, writer));
            assertEquals("&lt;&gt;", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new TestTranslator();
        CharSequenceTranslator translator2 = new TestTranslator();
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        
        assertTrue(mergedTranslator instanceof AggregateTranslator);
        AggregateTranslator aggregateTranslator = (AggregateTranslator) mergedTranslator;
        assertEquals(2, aggregateTranslator.getTranslators().length);
        assertEquals(translator1, aggregateTranslator.getTranslators()[0]);
        assertEquals(translator2, aggregateTranslator.getTranslators()[1]);
    }

    public static class TestTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null) {
                return 0;
            }
            
            if (index < input.length()) {
                char ch = input.charAt(index);
                
                if (ch == '<') {
                    out.write("&lt;");
                    return 1;
                } else if (ch == '>') {
                    out.write("&gt;");
                    return 1;
                }
            }
            
            return 0;
        }
    }
}