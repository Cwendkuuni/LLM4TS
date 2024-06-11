
import org.junit.Test;
import org.junit.Assert;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_withDecimalEntityValue_shouldUnescapeCharacter() throws IOException {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        // Act
        int result = unescaper.translate("&#65;", 0, writer);

        // Assert
        Assert.assertEquals(5, result);
        Assert.assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_withHexadecimalEntityValue_shouldUnescapeCharacter() throws IOException {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        // Act
        int result = unescaper.translate("&#x41;", 0, writer);

        // Assert
        Assert.assertEquals(6, result);
        Assert.assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_withDecimalEntityValueGreaterThan65535_shouldUnescapeCharacter() throws IOException {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        // Act
        int result = unescaper.translate("&#65536;", 0, writer);

        // Assert
        Assert.assertEquals(9, result);
        Assert.assertEquals("\uD800\uDC00", writer.toString());
    }

    @Test
    public void testTranslate_withDecimalEntityValueLessThan65536_shouldUnescapeCharacter() throws IOException {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        // Act
        int result = unescaper.translate("&#64;", 0, writer);

        // Assert
        Assert.assertEquals(5, result);
        Assert.assertEquals("@", writer.toString());
    }

}
