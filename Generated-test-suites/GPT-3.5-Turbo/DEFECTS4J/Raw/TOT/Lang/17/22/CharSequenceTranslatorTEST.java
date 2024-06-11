import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    private CharSequenceTranslator translator;
    
    @Test
    public void testTranslate_withNullInput_returnsNull() {
        // Arrange
        translator = new TestTranslator();
        
        // Act
        String result = translator.translate(null);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testTranslate_withEmptyInput_returnsEmptyString() {
        // Arrange
        translator = new TestTranslator();
        
        // Act
        String result = translator.translate("");
        
        // Assert
        assertEquals("", result);
    }
    
    @Test
    public void testTranslate_withNormalInput_returnsTranslatedString() {
        // Arrange
        translator = new TestTranslator();
        
        // Act
        String result = translator.translate("Hello World");
        
        // Assert
        assertEquals("Translated: Hello World", result);
    }
    
    @Test
    public void testTranslate_withSpecialCharacters_returnsTranslatedString() {
        // Arrange
        translator = new TestTranslator();
        
        // Act
        String result = translator.translate("@#$%^&*()");
        
        // Assert
        assertEquals("Translated: @#$%^&*()", result);
    }
    
    @Test
    public void testTranslate_withSurrogatePairs_returnsTranslatedString() {
        // Arrange
        translator = new TestTranslator();
        
        // Act
        String result = translator.translate("😀😃😄");
        
        // Assert
        assertEquals("Translated: 😀😃😄", result);
    }
    
    @Test
    public void testTranslate_withLargeInput_returnsTranslatedString() {
        // Arrange
        translator = new TestTranslator();
        String input = generateLargeInput();
        
        // Act
        long startTime = System.currentTimeMillis();
        String result = translator.translate(input);
        long endTime = System.currentTimeMillis();
        
        // Assert
        System.out.println("Time for translation: " + (endTime - startTime) + "ms");
        assertEquals("Translated: " + input, result);
    }
    
    private String generateLargeInput() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000000; i++) {
            sb.append("a");
        }
        return sb.toString();
    }
    
    private class TestTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null || out == null) {
                throw new IllegalArgumentException();
            }
            out.write("Translated: " + input);
            return input.length();
        }
    }
}