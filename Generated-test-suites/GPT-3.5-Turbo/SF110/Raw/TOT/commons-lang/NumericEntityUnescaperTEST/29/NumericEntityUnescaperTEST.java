import org.junit.*;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper unescaper;

    @Before
    public void setUp() {
        // Initialize NumericEntityUnescaper before each test
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
    }

    @Test
    public void testTranslate_WithValidNumericEntity_ReturnsCorrectTranslation() throws IOException {
        // Arrange
        CharSequence input = "&#65;";
        StringWriter out = new StringWriter();
        
        // Act
        int translated = unescaper.translate(input, 0, out);
        
        // Assert
        assertEquals("A", out.toString());
        assertEquals(4, translated);
    }

    @Test
    public void testTranslate_WithHexNumericEntity_ReturnsCorrectTranslation() throws IOException {
        // Arrange
        CharSequence input = "&#x41;";
        StringWriter out = new StringWriter();
        
        // Act
        int translated = unescaper.translate(input, 0, out);
        
        // Assert
        assertEquals("A", out.toString());
        assertEquals(6, translated);
    }

    @Test
    public void testTranslate_WithInvalidNumericEntity_ReturnsZeroTranslation() throws IOException {
        // Arrange
        CharSequence input = "&invalid;";
        StringWriter out = new StringWriter();
        
        // Act
        int translated = unescaper.translate(input, 0, out);
        
        // Assert
        assertEquals("", out.toString());
        assertEquals(0, translated);
    }

    @Test
    public void testTranslate_WithSemiColonOptionalAndSemiColonPresent_ReturnsCorrectTranslation() throws IOException {
        // Arrange
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        CharSequence input = "&#65;";
        StringWriter out = new StringWriter();
        
        // Act
        int translated = unescaper.translate(input, 0, out);
        
        // Assert
        assertEquals("A", out.toString());
        assertEquals(4, translated);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testTranslate_WithSemiColonRequiredAndSemiColonMissing_ThrowsIllegalArgumentException() throws IOException {
        // Arrange
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        CharSequence input = "&#65";
        StringWriter out = new StringWriter();
        
        // Act
        unescaper.translate(input, 0, out);
    }

    @Test
    public void testTranslate_WithErrorIfNoSemiColonAndSemiColonMissing_ReturnsZeroTranslation() throws IOException {
        // Arrange
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        CharSequence input = "&#65";
        StringWriter out = new StringWriter();
        
        // Act
        int translated = unescaper.translate(input, 0, out);
        
        // Assert
        assertEquals("", out.toString());
        assertEquals(0, translated);
    }
}