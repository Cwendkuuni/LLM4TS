import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_ValidDecimalEntity_ReturnsTranslatedCharacter() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#48;");
        
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate(input, 0, new StringWriter(output));
        
        assertEquals(5, result);
        assertEquals("0", output.toString());
    }
    
    @Test
    public void testTranslate_ValidHexEntity_ReturnsTranslatedCharacter() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#x2A;");
        
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate(input, 0, new StringWriter(output));
        
        assertEquals(6, result);
        assertEquals("*", output.toString());
    }
    
    @Test
    public void testTranslate_ValidHexEntityLowerCase_ReturnsTranslatedCharacter() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#x2a;");
        
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate(input, 0, new StringWriter(output));
        
        assertEquals(6, result);
        assertEquals("*", output.toString());
    }
    
    @Test
    public void testTranslate_SemiColonRequiredOptionOff_ReturnsTranslatedCharacter() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder input = new StringBuilder("&#48");
        
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate(input, 0, new StringWriter(output));
        
        assertEquals(2, result);
        assertEquals("&#", output.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_ErrorIfNoSemiColonOptionOn_ThrowsIllegalArgumentException() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringBuilder input = new StringBuilder("&#48");
        
        StringBuilder output = new StringBuilder();
        unescaper.translate(input, 0, new StringWriter(output));
    }
    
    @Test
    public void testTranslate_InvalidDecimalEntity_ReturnsZero() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#invalid;");
        
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate(input, 0, new StringWriter(output));
        
        assertEquals(0, result);
        assertEquals("", output.toString());
    }
    
    @Test
    public void testTranslate_InvalidHexEntity_ReturnsZero() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#xinvalid;");
        
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate(input, 0, new StringWriter(output));
        
        assertEquals(0, result);
        assertEquals("", output.toString());
    }
    
    @Test
    public void testTranslate_EntityValueGreaterThan65535_ReturnsTranslatedCharacters() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#65536;");
        
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate(input, 0, new StringWriter(output));
        
        assertEquals(10, result);
        assertEquals("\uD800\uDC00", output.toString()); // surrogate pair encoding for U+10000
    }
    
    @Test
    public void testTranslate_EntityValueLessThan65535_ReturnsTranslatedCharacter() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#49;");
        
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate(input, 0, new StringWriter(output));
        
        assertEquals(5, result);
        assertEquals("1", output.toString());
    }
}