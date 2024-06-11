import org.junit.Test;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

public class NumericEntityUnescaperTest {

    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    @Test
    public void testTranslate_withValidHexadecimalEntity_shouldTranslateSuccessfully() {
        // Test input: "&#x41;"
        // Expected output: 'A'
        // Translation should successfully translate the hexadecimal entity to corresponding codepoint 'A'
        assertEquals("Translation failed", 'A', unescaper.translate("&#x41;", 0, null));
    }
    
    @Test
    public void testTranslate_withValidDecimalEntity_shouldTranslateSuccessfully() {
        // Test input: "&#65;"
        // Expected output: 'A'
        // Translation should successfully translate the decimal entity to corresponding codepoint 'A'
        assertEquals("Translation failed", 'A', unescaper.translate("&#65;", 0, null));
    }
    
    @Test
    public void testTranslate_withInvalidEntity_shouldReturnZero() {
        // Test input: "&#xG;"
        // Expected output: 0
        // Translation should return 0 as the hexadecimal entity has invalid character 'G'
        assertEquals("Translation failed", 0, unescaper.translate("&#xG;", 0, null));
    }
    
    @Test
    public void testTranslate_withInvalidEntityLength_shouldReturnZero() {
        // Test input: "&#x1;"
        // Expected output: 0
        // Translation should return 0 as the hexadecimal entity has only one digit instead of two
        assertEquals("Translation failed", 0, unescaper.translate("&#x1;", 0, null));
    }
    
    @Test
    public void testTranslate_withMissingSemicolon_shouldReturnZero() {
        // Test input: "&#x41"
        // Expected output: 0
        // Translation should return 0 as the hexadecimal entity is missing the closing semicolon
        assertEquals("Translation failed", 0, unescaper.translate("&#x41", 0, null));
    }
    
    @Test
    public void testTranslate_withInvalidStartingCharacter_shouldReturnZero() {
        // Test input: "&$#x41;"
        // Expected output: 0
        // Translation should return 0 as the starting character of the entity is not '&'
        assertEquals("Translation failed", 0, unescaper.translate("&$#x41;", 0, null));
    }
    
    @Test
    public void testTranslate_withInvalidStartingCharacter2_shouldReturnZero() {
        // Test input: "#&#x41;"
        // Expected output: 0
        // Translation should return 0 as the starting character of the entity is not '&'
        assertEquals("Translation failed", 0, unescaper.translate("#&#x41;", 0, null));
    }
}