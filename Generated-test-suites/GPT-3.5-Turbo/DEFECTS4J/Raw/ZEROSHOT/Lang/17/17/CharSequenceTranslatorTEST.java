import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateMethod() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        StringWriter writer = new StringWriter();
        try {
            int result = translator.translate("Hello World", 0, writer);
            assertEquals(0, result);
            assertEquals("", writer.toString());
        } catch (IOException e) {
            fail("Should not throw an IOException");
        }
    }

    @Test
    public void testTranslateHelper() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        String result = translator.translate("Hello World");
        assertEquals("", result);
        
        result = translator.translate(null);
        assertNull(result);
    }
    
    @Test
    public void testTranslateMethodWithSurrogatePair() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 2;
            }
        };
        
        StringWriter writer = new StringWriter();
        try {
            int result = translator.translate("\uD83D\uDC4D", 0, writer);
            assertEquals(2, result);
            assertEquals("", writer.toString());
        } catch (IOException e) {
            fail("Should not throw an IOException");
        }
    }
    
    @Test
    public void testTranslateMethodWithSurrogatePairAtEnd() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 2;
            }
        };
        
        StringWriter writer = new StringWriter();
        try {
            int result = translator.translate("Hello \uD83D\uDC4D", 6, writer);
            assertEquals(2, result);
            assertEquals("", writer.toString());
        } catch (IOException e) {
            fail("Should not throw an IOException");
        }
    }
    
    @Test
    public void testTranslateMethodWithSurrogatePairAtEndAndFallback() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 1;
            }
        };
        
        StringWriter writer = new StringWriter();
        try {
            int result = translator.translate("Hello \uD83D\uDC4D", 6, writer);
            assertEquals(1, result);
            assertEquals("\uD83D", writer.toString());
        } catch (IOException e) {
            fail("Should not throw an IOException");
        }
    }
    
    @Test
    public void testWithMethod() {
        CharSequenceTranslator translator1 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        CharSequenceTranslator translator3 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        CharSequenceTranslator resultTranslator = translator1.with(translator2, translator3);
        
        assertEquals(AggregateTranslator.class, resultTranslator.getClass());
        
        AggregateTranslator aggregateTranslator = (AggregateTranslator) resultTranslator;
        CharSequenceTranslator[] translators = aggregateTranslator.getTranslators();
        
        assertEquals(3, translators.length);
        assertSame(translator1, translators[0]);
        assertSame(translator2, translators[1]);
        assertSame(translator3, translators[2]);
    }
    
    @Test
    public void testHexMethod() {
        String result = CharSequenceTranslator.hex(65);
        assertEquals("41", result);
        
        result = CharSequenceTranslator.hex(123456);
        assertEquals("1E240", result);
        
        result = CharSequenceTranslator.hex(0);
        assertEquals("0", result);
    }

}