import org.junit.Test;
import org.junit.Assert;

public class SoundexTest {

    @Test
    public void testEncode_Object_StringInput_ReturnsSoundexCode() throws EncoderException {
        // Arrange
        Soundex soundex = new Soundex();
        String input = "Robert";
        String expected = "R163";

        // Act
        Object result = soundex.encode(input);

        // Assert
        Assert.assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testEncode_Object_NonStringInput_ThrowsEncoderException() throws EncoderException {
        // Arrange
        Soundex soundex = new Soundex();
        Integer input = 123;

        // Act
        soundex.encode(input);
    }

    @Test
    public void testEncode_StringInput_ReturnsSoundexCode() {
        // Arrange
        Soundex soundex = new Soundex();
        String input = "Robert";
        String expected = "R163";

        // Act
        String result = soundex.encode(input);

        // Assert
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDifference_TwoStrings_ReturnsDifferenceCode() throws EncoderException {
        // Arrange
        Soundex soundex = new Soundex();
        String s1 = "Robert";
        String s2 = "Rupert";
        int expected = 2;

        // Act
        int result = soundex.difference(s1, s2);

        // Assert
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testSoundex_StringInput_ReturnsSoundexCode() {
        // Arrange
        Soundex soundex = new Soundex();
        String input = "Robert";
        String expected = "R163";

        // Act
        String result = soundex.soundex(input);

        // Assert
        Assert.assertEquals(expected, result);
    }
}