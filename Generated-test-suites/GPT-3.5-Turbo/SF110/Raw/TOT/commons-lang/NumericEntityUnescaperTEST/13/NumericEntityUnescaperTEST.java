import java.io.StringWriter;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper unescaper;
    private StringWriter writer;

    @Before
    public void setUp() {
        unescaper = new NumericEntityUnescaper();
        writer = new StringWriter();
    }

    @Test
    public void testTranslate_NormalDecimalEntity_ReturnsLengthOfTranslatedEntity() {
        String input = "&#65;";
        int index = 0;
        try {
            int result = unescaper.translate(input, index, writer);
            assertEquals(5, result);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }

        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_NormalHexadecimalEntity_ReturnsLengthOfTranslatedEntity() {
        String input = "&#x41;";
        int index = 0;
        try {
            int result = unescaper.translate(input, index, writer);
            assertEquals(6, result);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }

        assertEquals("A", writer.toString());
    }
    
    @Test
    public void testTranslate_EntityWithoutSemicolonSemiColonRequired_ThrowsException() {
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired); 
        String input = "&#65";
        int index = 0;
        try {
            int result = unescaper.translate(input, index, writer);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
        } catch (Exception e) {
            fail("Incorrect exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testTranslate_EntityWithoutSemicolonSemicolonOptional_ReturnsLengthOfTranslatedEntity() {
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional); 
        String input = "&#65";
        int index = 0;
        try {
            int result = unescaper.translate(input, index, writer);
            assertEquals(4, result);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }

        assertEquals("A", writer.toString());
    }
    
    @Test
    public void testTranslate_EntityWithoutSemicolonErrorIfNoSemicolon_ReturnsLengthOfTranslatedEntity() {
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon); 
        String input = "&#65";
        int index = 0;
        try {
            int result = unescaper.translate(input, index, writer);
            assertEquals(4, result);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }

        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_EntityWithInvalidHexDigit_ReturnsLengthOfTranslatedEntity() {
        String input = "&#xG";
        int index = 0;
        try {
            int result = unescaper.translate(input, index, writer);
            assertEquals(3, result);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }

        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_EntityWithInvalidDecimalDigit_ReturnsLengthOfTranslatedEntity() {
        String input = "&#A";
        int index = 0;
        try {
            int result = unescaper.translate(input, index, writer);
            assertEquals(2, result);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }

        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_EntityValueGreaterThan65535_TranslatesToSurrogatePair() {
        String input = "&#65536;";
        int index = 0;
        try {
            int result = unescaper.translate(input, index, writer);
            assertEquals(10, result);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }

        assertEquals("\uD800\uDC00", writer.toString()); // Surrogate pair representing U+10000
    }

    @Test
    public void testTranslate_InvalidEntity_ReturnsZero() {
        String input = "&#;";
        int index = 0;
        try {
            int result = unescaper.translate(input, index, writer);
            assertEquals(0, result);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }

        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_EntityWithInvalidInput_ReturnsZero() {
        String input = "<>&#65;";
        int index = 3;
        try {
            int result = unescaper.translate(input, index, writer);
            assertEquals(0, result);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }

        assertEquals("", writer.toString());
    }
}