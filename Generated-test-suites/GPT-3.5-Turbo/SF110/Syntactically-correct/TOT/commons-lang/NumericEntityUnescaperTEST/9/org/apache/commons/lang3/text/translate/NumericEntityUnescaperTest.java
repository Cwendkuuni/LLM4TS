import org.junit.*;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_NormalCharSequence_ReturnsZero() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("abc");
        try {
            int result = translator.translate(stringBuilder, 0, null);
            assertEquals(0, result);
        } catch (Exception ex) {
            fail("An exception was thrown");
        }
    }

    @Test
    public void testTranslate_NumericEntity_ReturnsTranslatedEntity() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("&#65;");
        try {
            StringWriter writer = new StringWriter();
            int result = translator.translate(stringBuilder, 0, writer);
            assertEquals(6, result);
            assertEquals("A", writer.toString());
        } catch (Exception ex) {
            fail("An exception was thrown");
        }
    }

    @Test
    public void testTranslate_HexNumericEntity_ReturnsTranslatedEntity() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("&#x43;");
        try {
            StringWriter writer = new StringWriter();
            int result = translator.translate(stringBuilder, 0, writer);
            assertEquals(7, result);
            assertEquals("C", writer.toString());
        } catch (Exception ex) {
            fail("An exception was thrown");
        }
    }

    @Test
    public void testTranslate_SemicolonNotRequired_OptionSet_ReturnsZero() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("&#65");
        try {
            int result = translator.translate(stringBuilder, 0, null);
            assertEquals(0, result);
        } catch (Exception ex) {
            fail("An exception was thrown");
        }
    }

    @Test
    public void testTranslate_SemicolonRequired_OptionSet_ReturnsZero() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("&#65");
        try {
            int result = translator.translate(stringBuilder, 0, null);
            assertEquals(0, result);
        } catch (Exception ex) {
            fail("An exception was thrown");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_ErrorIfNoSemicolon_OptionSet_ThrowsIllegalArgumentException() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("&#65");
        try {
            translator.translate(stringBuilder, 0, null);
            fail("IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Semi-colon required at end of numeric entity", ex.getMessage());
        }
    }

    @Test
    public void testTranslate_NotANumber_ReturnsZero() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("&#XYZ;");
        try {
            int result = translator.translate(stringBuilder, 0, null);
            assertEquals(0, result);
        } catch (Exception ex) {
            fail("An exception was thrown");
        }
    }

    @Test
    public void testTranslate_EntityValueAbove65535_ReturnsTranslatedEntity() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("&#65536;");
        try {
            StringWriter writer = new StringWriter();
            int result = translator.translate(stringBuilder, 0, writer);
            assertEquals(10, result);
            assertEquals(Character.toChars(65536)[0], writer.toString().charAt(0));
            assertEquals(Character.toChars(65536)[1], writer.toString().charAt(1));
        } catch (Exception ex) {
            fail("An exception was thrown");
        }
    }
}