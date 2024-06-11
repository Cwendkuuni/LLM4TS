import org.junit.Assert;
import org.junit.Test;

public class SoundexTest {

    @Test
    public void encode_ValidString_ReturnsSoundexCode(){
        // Arrange
        Soundex soundex = new Soundex();

        // Act
        String result = soundex.encode("Robert");

        // Assert
        Assert.assertEquals("R163", result);
    }

    @Test
    public void encode_StringWithNonMappedCharacters_IgnoresNonMappedCharacters(){
        // Arrange
        Soundex soundex = new Soundex();

        // Act
        String result = soundex.encode("John#");

        // Assert
        Assert.assertEquals("J500", result);
    }

    @Test
    public void encode_StringWithSameMappedConsonants_SkipsDuplicates(){
        // Arrange
        Soundex soundex = new Soundex();

        // Act
        String result = soundex.encode("Lloyd");

        // Assert
        Assert.assertEquals("L300", result);
    }

    @Test
    public void encode_ShortString_ReturnsPaddedCode(){
        // Arrange
        Soundex soundex = new Soundex();

        // Act
        String result = soundex.encode("A");

        // Assert
        Assert.assertEquals("A000", result);
    }

    @Test(expected = EncoderException.class)
    public void encode_NullString_ThrowsEncoderException(){
        // Arrange
        Soundex soundex = new Soundex();

        // Act
        soundex.encode(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void encode_StringWithUnmappedCharacters_ThrowsIllegalArgumentException(){
        // Arrange
        Soundex soundex = new Soundex();

        // Act
        soundex.encode("Hello!");
    }

    @Test(expected = EncoderException.class)
    public void encode_ObjectNotOfTypeString_ThrowsEncoderException(){
        // Arrange
        Soundex soundex = new Soundex();

        // Act
        soundex.encode(123);
    }

    @Test
    public void difference_SimilarStrings_ReturnsSimilarityScore(){
        // Arrange
        Soundex soundex = new Soundex();
        String s1 = "Robert";
        String s2 = "Rupert";

        // Act
        int result = soundex.difference(s1, s2);

        // Assert
        Assert.assertEquals(3, result);
    }

    @Test
    public void difference_DifferentStrings_ReturnsDifferenceScore(){
        // Arrange
        Soundex soundex = new Soundex();
        String s1 = "John";
        String s2 = "Smith";

        // Act
        int result = soundex.difference(s1, s2);

        // Assert
        Assert.assertEquals(0, result);
    }

    @Test(expected = EncoderException.class)
    public void difference_EncodingError_ThrowsEncoderException(){
        // Arrange
        Soundex soundex = new Soundex();
        String s1 = "Robert";
        String s2 = null;

        // Act
        soundex.difference(s1, s2);
    }

    @Test
    public void difference_LongestPossibleDifference_ReturnsDifferenceOfZero(){
        // Arrange
        Soundex soundex = new Soundex();
        String s1 = "AAAA";
        String s2 = "BBBB";

        // Act
        int result = soundex.difference(s1, s2);

        // Assert
        Assert.assertEquals(0, result);
    }

    // Add more test cases as needed

}