
import org.junit.Assert;
import org.junit.Test;

public class CharSequenceTranslatorTest {
    
    @Test
    public void testTranslate_withNonNullInputAndZeroIndexAndNonNullWriter() throws IOException {
        // Arrange
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0; // test translator that consumes 0 codepoints
            }
        };
        
        // Act
        StringWriter writer = new StringWriter();
        int result = translator.translate("input", 0, writer);
        String output = writer.toString();
        
        // Assert
        Assert.assertEquals(0, result);
        Assert.assertEquals("", output);
    }
    
    @Test
    public void testTranslate_withNonNullInputAndNullWriter() throws IOException {
        // Arrange
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 1; // test translator that consumes 1 codepoint
            }
        };
        
        // Act & Assert
        try {
            translator.translate("input", null);
            Assert.fail("Expected IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException ex) {
            // Expected
        }
    }
    
    @Test
    public void testTranslate_withNullInput() throws IOException {
        // Arrange
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 1; // test translator that consumes 1 codepoint
            }
        };
        
        // Act
        String result = translator.translate(null);
        
        // Assert
        Assert.assertNull(result);
    }
    
    @Test
    public void testTranslate_withNonNullInputAndNonNullWriter() throws IOException {
        // Arrange
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(input.toString()); // test translator that echoes the input
                return input.length();
            }
        };
        
        // Act
        StringWriter writer = new StringWriter();
        translator.translate("input", writer);
        String output = writer.toString();
        
        // Assert
        Assert.assertEquals("input", output);
    }
    
    @Test
    public void testWith_withNonNullTranslators() {
        // Arrange
        CharSequenceTranslator translator1 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0; // test translator that consumes 0 codepoints
            }
        };
        CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 1; // test translator that consumes 1 codepoint
            }
        };
        CharSequenceTranslator translator3 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(input.toString()); // test translator that echoes the input
                return input.length();
            }
        };
        CharSequenceTranslator translator = translator1.with(translator2, translator3);
        
        // Act
        StringWriter writer = new StringWriter();
        translator.translate("input", writer);
        String output = writer.toString();
        
        // Assert
        Assert.assertEquals("input", output);
    }
    
    @Test
    public void testHex_withChar() {
        // Arrange
        int codepoint = 'A';
        
        // Act
        String result = CharSequenceTranslator.hex(codepoint);
        
        // Assert
        Assert.assertEquals("41", result);
    }
    
    @Test
    public void testHex_withNonAsciiChar() {
        // Arrange
        int codepoint = 8364;
        
        // Act
        String result = CharSequenceTranslator.hex(codepoint);
        
        // Assert
        Assert.assertEquals("20AC", result);
    }
}
