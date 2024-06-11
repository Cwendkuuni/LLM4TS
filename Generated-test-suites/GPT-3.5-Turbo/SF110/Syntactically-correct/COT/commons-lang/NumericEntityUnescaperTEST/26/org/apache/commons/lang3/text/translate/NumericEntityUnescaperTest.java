
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate_BasicDec() throws IOException {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&#65;";
        
        // Act
        int result = unescaper.translate(input, 0, writer);
        
        // Assert
        assertEquals(4, result);
        assertEquals("A", writer.toString());
    }
    
    @Test
    public void testTranslate_BasicHex() throws IOException {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&#x41;";
        
        // Act
        int result = unescaper.translate(input, 0, writer);
        
        // Assert
        assertEquals(5, result);
        assertEquals("A", writer.toString());
    }
    
    @Test
    public void testTranslate_NoSemiColon() throws IOException {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        StringWriter writer = new StringWriter();
        CharSequence input = "&#65";
        
        // Act
        int result = unescaper.translate(input, 0, writer);
        
        // Assert
        assertEquals(4, result);
        assertEquals("A", writer.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_ErrorIfNoSemiColon() throws IOException {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringWriter writer = new StringWriter();
        CharSequence input = "&#65";
        
        // Act
        int result = unescaper.translate(input, 0, writer);
        
        // Assert
        // Exception expected
    }
    
    @Test
    public void testTranslate_HighUnicode() throws IOException {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&#119808;";
        
        // Act
        int result = unescaper.translate(input, 0, writer);
        
        // Assert
        assertEquals(10, result);
        assertEquals("\ud835\udc00", writer.toString());
    }
}
