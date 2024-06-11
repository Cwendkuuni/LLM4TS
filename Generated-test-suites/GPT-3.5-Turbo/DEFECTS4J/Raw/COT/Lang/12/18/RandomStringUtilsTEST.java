import org.junit.Assert;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void random_shouldReturnStringWithCorrectLength() {
        // Arrange
        int count = 10;
        
        // Act
        String result = RandomStringUtils.random(count);
        
        // Assert
        Assert.assertEquals(count, result.length());
    }

    @Test
    public void randomAscii_shouldReturnStringWithCorrectLength() {
        // Arrange
        int count = 10;
        
        // Act
        String result = RandomStringUtils.randomAscii(count);
        
        // Assert
        Assert.assertEquals(count, result.length());
    }

    @Test
    public void randomAlphabetic_shouldReturnStringWithCorrectLength() {
        // Arrange
        int count = 10;
        
        // Act
        String result = RandomStringUtils.randomAlphabetic(count);
        
        // Assert
        Assert.assertEquals(count, result.length());
    }

    @Test
    public void randomAlphanumeric_shouldReturnStringWithCorrectLength() {
        // Arrange
        int count = 10;
        
        // Act
        String result = RandomStringUtils.randomAlphanumeric(count);
        
        // Assert
        Assert.assertEquals(count, result.length());
    }

    @Test
    public void randomNumeric_shouldReturnStringWithCorrectLength() {
        // Arrange
        int count = 10;
        
        // Act
        String result = RandomStringUtils.randomNumeric(count);
        
        // Assert
        Assert.assertEquals(count, result.length());
    }

    @Test
    public void random_shouldReturnStringWithCorrectCharacterTypes() {
        // Arrange
        int count = 10;
        boolean letters = true;
        boolean numbers = true;
        
        // Act
        String result = RandomStringUtils.random(count, letters, numbers);
        
        // Assert
        Assert.assertTrue(result.matches("^[a-zA-Z0-9]*$"));
    }

    @Test
    public void random_shouldThrowExceptionWhenCountIsNegative() {
        // Arrange
        int count = -1;
        
        // Act & Assert
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            RandomStringUtils.random(count);
        });
    }

    @Test
    public void random_shouldReturnEmptyStringWhenCountIsZero() {
        // Arrange
        int count = 0;
        
        // Act
        String result = RandomStringUtils.random(count);
        
        // Assert
        Assert.assertEquals("", result);
    }

    @Test
    public void random_shouldReturnStringWithCorrectCharacterSet() {
        // Arrange
        int count = 10;
        String chars = "abc";
        
        // Act
        String result = RandomStringUtils.random(count, chars);
        
        // Assert
        Assert.assertTrue(result.matches("^[abc]*$"));
    }

    @Test
    public void random_shouldThrowExceptionWhenCharsIsEmpty() {
        // Arrange
        int count = 10;
        String chars = "";
        
        // Act & Assert
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            RandomStringUtils.random(count, chars);
        });
    }

    @Test
    public void random_shouldThrowExceptionWhenCharsIsNull() {
        // Arrange
        int count = 10;
        String chars = null;
        
        // Act & Assert
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            RandomStringUtils.random(count, chars);
        });
    }

    @Test
    public void random_shouldReturnStringWithCorrectCharacterTypesAndCharacterSet() {
        // Arrange
        int count = 10;
        boolean letters = true;
        boolean numbers = true;
        char[] chars = {'a', 'b', '1'};
        
        // Act
        String result = RandomStringUtils.random(count, chars);
        
        // Assert
        Assert.assertTrue(result.matches("^[a-zA-Z1]*$"));
    }
}