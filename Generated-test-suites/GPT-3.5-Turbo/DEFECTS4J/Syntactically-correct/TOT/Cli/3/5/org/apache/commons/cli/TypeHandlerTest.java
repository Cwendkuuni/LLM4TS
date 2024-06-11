import org.junit.Assert;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_WithStringValue_ReturnsString() {
        // Arrange
        String str = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;

        // Act
        Object result = TypeHandler.createValue(str, obj);

        // Assert
        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValue_String_WithNumberValue_ReturnsNull() {
        // Arrange
        String str = "test";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;

        // Act
        Object result = TypeHandler.createValue(str, obj);

        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void testCreateValue_String_WithObjectValue_ReturnsObject() {
        // Arrange
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;

        // Act
        Object result = TypeHandler.createValue(str, obj);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(str, result.getClass().getName());
    }

    // More test cases for other methods...

}