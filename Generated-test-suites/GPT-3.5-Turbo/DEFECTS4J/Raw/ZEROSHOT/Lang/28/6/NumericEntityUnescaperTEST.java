import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import org.apache.commons.lang3.text.translate.*;
import java.lang.reflect.Field;

public class NumericEntityUnescaperTest {
    private NumericEntityUnescaper numericEntityUnescaper;
    
    @Before
    public void setup() {
        numericEntityUnescaper = new NumericEntityUnescaper();
    }
    
    @Test
    public void testTranslate_NumericEntity() throws IOException {
        // Arrange
        String input = "&#65;";
        StringWriter writer = new StringWriter();
        
        // Act
        int result = numericEntityUnescaper.translate(input, 0, writer);
        
        // Assert
        assertEquals(5, result);
        assertEquals("A", writer.toString());
    }
    
    @Test
    public void testTranslate_HexNumericEntity() throws IOException {
        // Arrange
        String input = "&#x41;";
        StringWriter writer = new StringWriter();
        
        // Act
        int result = numericEntityUnescaper.translate(input, 0, writer);
        
        // Assert
        assertEquals(6, result);
        assertEquals("A", writer.toString());
    }
    
    @Test
    public void testTranslate_ZeroInput() throws IOException {
        // Arrange
        String input = "";
        StringWriter writer = new StringWriter();
        
        // Act
        int result = numericEntityUnescaper.translate(input, 0, writer);
        
        // Assert
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslate_NoEntity() throws IOException {
        // Arrange
        String input = "ABC";
        StringWriter writer = new StringWriter();
        
        // Act
        int result = numericEntityUnescaper.translate(input, 0, writer);
        
        // Assert
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslate_ExceptionThrown() throws IOException {
        // Arrange
        String input = "&#x41;;";
        StringWriter writer = new StringWriter();
        
        // Act
        int result = numericEntityUnescaper.translate(input, 0, writer);
        
        // Assert
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
}