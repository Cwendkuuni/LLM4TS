import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphone() {
        String input = "test";
        String expectedOutput = "1111111111";
        String actualOutput = caverphone.caverphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testEncode() {
        String input = "test";
        String expectedOutput = "1111111111";
        String actualOutput = (String) caverphone.encode(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeNonString() throws EncoderException {
        Integer input = 123;
        caverphone.encode(input);
    }

    @Test
    public void testIsCaverphoneEqual() {
        String str1 = "test";
        String str2 = "Test";
        boolean expectedOutput = true;
        boolean actualOutput = caverphone.isCaverphoneEqual(str1, str2);
        assertEquals(expectedOutput, actualOutput);
    }

}