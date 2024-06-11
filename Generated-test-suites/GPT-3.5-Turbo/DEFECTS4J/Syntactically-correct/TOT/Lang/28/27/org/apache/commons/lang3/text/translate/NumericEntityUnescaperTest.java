import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;
import org.junit.Test;

public class NumericEntityUnescaperTest {
    
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    
    @Test
    public void testTranslate_ValidNumericEntity_LowerCaseHexadecimal() throws IOException {
        // Arrange
        String input = "&#x61;";
        StringWriter out = new StringWriter();
        
        // Act
        int result = unescaper.translate(input, 0, out);
        
        // Assert
        assertEquals(7, out.toString().length());
        assertEquals('a', out.toString().charAt(0));
        assertEquals(7, result);
    }
    
    @Test
    public void testTranslate_ValidNumericEntity_UpperCaseHexadecimal() throws IOException {
        // Arrange
        String input = "&#X41;";
        StringWriter out = new StringWriter();
        
        // Act
        int result = unescaper.translate(input, 0, out);
        
        // Assert
        assertEquals(7, out.toString().length());
        assertEquals('A', out.toString().charAt(0));
        assertEquals(7, result);
    }
    
    @Test
    public void testTranslate_ValidNumericEntity_Decimal() throws IOException {
        // Arrange
        String input = "&#97;";
        StringWriter out = new StringWriter();
        
        // Act
        int result = unescaper.translate(input, 0, out);
        
        // Assert
        assertEquals(7, out.toString().length());
        assertEquals('a', out.toString().charAt(0));
        assertEquals(7, result);
    }
    
    @Test
    public void testTranslate_ValidNumericEntity_WithLeadingZeroes() throws IOException {
        // Arrange
        String input = "&#00000097;";
        StringWriter out = new StringWriter();
        
        // Act
        int result = unescaper.translate(input, 0, out);
        
        // Assert
        assertEquals(10, out.toString().length());
        assertEquals('a', out.toString().charAt(0));
        assertEquals(10, result);
    }
    
    @Test
    public void testTranslate_InvalidNumericEntity_NonNumericValue() throws IOException {
        // Arrange
        String input = "&#a;";
        StringWriter out = new StringWriter();
        
        // Act
        int result = unescaper.translate(input, 0, out);
        
        // Assert
        assertEquals(0, out.toString().length());
        assertEquals(0, result);
    }
    
    @Test
    public void testTranslate_InvalidNumericEntity_NoSemicolon() throws IOException {
        // Arrange
        String input = "&#97";
        StringWriter out = new StringWriter();
        
        // Act
        int result = unescaper.translate(input, 0, out);
        
        // Assert
        assertEquals(0, out.toString().length());
        assertEquals(0, result);
    }
    
    @Test
    public void testTranslate_InvalidNumericEntity_NoNumericValue() throws IOException {
        // Arrange
        String input = "&#;";
        StringWriter out = new StringWriter();
        
        // Act
        int result = unescaper.translate(input, 0, out);
        
        // Assert
        assertEquals(0, out.toString().length());
        assertEquals(0, result);
    }
    
}