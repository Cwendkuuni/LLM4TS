import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testEncode_NormalInput_ShouldReturnValidSoundexCode() {
        Soundex soundex = new Soundex();
        String input = "Robert";
        String expectedOutput = "R163";
        assertEquals(expectedOutput, soundex.encode(input));
    }

    @Test
    public void testEncode_EmptyInput_ShouldReturnEmptyString() {
        Soundex soundex = new Soundex();
        String input = "";
        String expectedOutput = "";
        assertEquals(expectedOutput, soundex.encode(input));
    }

    @Test
    public void testEncode_NullInput_ShouldReturnNull() {
        Soundex soundex = new Soundex();
        String input = null;
        assertNull(soundex.encode(input));
    }

    @Test
    public void testEncode_InputWithNonMappedCharacter_ShouldThrowIllegalArgumentException() {
        Soundex soundex = new Soundex();
        String input = "JohnDoe";
        assertThrows(IllegalArgumentException.class, () -> soundex.encode(input));
    }

    @Test
    public void testDifference_SimilarInputs_ShouldReturn4() throws EncoderException {
        Soundex soundex = new Soundex();
        String input1 = "Robert";
        String input2 = "Rupert";
        int expectedOutput = 4;
        assertEquals(expectedOutput, soundex.difference(input1, input2));
    }

    @Test
    public void testDifference_CompletelyDifferentInputs_ShouldReturn0() throws EncoderException {
        Soundex soundex = new Soundex();
        String input1 = "John";
        String input2 = "Doe";
        int expectedOutput = 0;
        assertEquals(expectedOutput, soundex.difference(input1, input2));
    }

    @Test
    public void testDifference_InputWithNonMappedCharacter_ShouldThrowEncoderException() throws EncoderException {
        Soundex soundex = new Soundex();
        String input1 = "JohnDoe";
        String input2 = "Robert";
        assertThrows(EncoderException.class, () -> soundex.difference(input1, input2));
    }

}