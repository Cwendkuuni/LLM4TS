import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberInputTest {

    @Before
    public void setUp() throws Exception {
        // Set up any necessary objects or configurations before each test
    }

    @After
    public void tearDown() throws Exception {
        // Clean up any objects or configurations after each test
    }

    @Test
    public void testParseInt_withValidString_shouldReturnIntegerValue() {
        // Arrange
        String str = "123";

        // Act
        int result = NumberInput.parseInt(str);

        // Assert
        assertEquals(123, result);
    }

    @Test
    public void testParseInt_withInvalidString_shouldThrowNumberFormatException() {
        // Arrange
        String str = "abc";

        // Act & Assert
        assertThrows(NumberFormatException.class, () -> {
            NumberInput.parseInt(str);
        });
    }

    @Test
    public void testParseLong_withValidString_shouldReturnLongValue() {
        // Arrange
        String str = "1234567890";

        // Act
        long result = NumberInput.parseLong(str);

        // Assert
        assertEquals(1234567890L, result);
    }

    @Test
    public void testParseLong_withInvalidString_shouldThrowNumberFormatException() {
        // Arrange
        String str = "abc";

        // Act & Assert
        assertThrows(NumberFormatException.class, () -> {
            NumberInput.parseLong(str);
        });
    }

    @Test
    public void testInLongRange_withValidNumberInLongRange_shouldReturnTrue() {
        // Arrange
        char[] digitChars = "1234567890".toCharArray();
        int offset = 0;
        int len = digitChars.length;
        boolean negative = false;

        // Act
        boolean result = NumberInput.inLongRange(digitChars, offset, len, negative);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testInLongRange_withInvalidNumberOutsideLongRange_shouldReturnFalse() {
        // Arrange
        char[] digitChars = "9223372036854775808".toCharArray();
        int offset = 0;
        int len = digitChars.length;
        boolean negative = false;

        // Act
        boolean result = NumberInput.inLongRange(digitChars, offset, len, negative);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testParseAsInt_withValidNumberString_shouldReturnIntegerValue() {
        // Arrange
        String input = "123";

        // Act
        int result = NumberInput.parseAsInt(input, 0);

        // Assert
        assertEquals(123, result);
    }

    @Test
    public void testParseAsInt_withInvalidNumberString_shouldReturnDefaultValue() {
        // Arrange
        String input = "abc";

        // Act
        int result = NumberInput.parseAsInt(input, 0);

        // Assert
        assertEquals(0, result);
    }

    // More test cases for other methods...

}