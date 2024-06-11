import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    private Caverphone caverphone;

    @Before
    public void setUp() {
        caverphone = new Caverphone();
    }

    @Test
    public void testCaverphone_typicalCase() {
        String input = "hello";
        String expectedOutput = "H440000000";
        String actualOutput = caverphone.caverphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCaverphone_emptyString() {
        String input = "";
        String expectedOutput = "1111111111";
        String actualOutput = caverphone.caverphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCaverphone_nullString() {
        String input = null;
        String expectedOutput = "1111111111";
        String actualOutput = caverphone.caverphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCaverphone_edgeCase() {
        String input = "aAbBcCdDeE";
        String expectedOutput = "A132332332";
        String actualOutput = caverphone.caverphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testEncode_typicalCase() {
        String input = "hello";
        String expectedOutput = "H440000000";
        String actualOutput = caverphone.encode(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected=EncoderException.class)
    public void testEncode_nonStringInput() {
        Object input = new Object();
        caverphone.encode(input);
    }

    @Test
    public void testIsCaverphoneEqual_identicalStrings() {
        String str1 = "hello";
        String str2 = "HELLO";
        assertTrue(caverphone.isCaverphoneEqual(str1, str2));
    }

    @Test
    public void testIsCaverphoneEqual_differentStrings() {
        String str1 = "hello";
        String str2 = "hi";
        assertFalse(caverphone.isCaverphoneEqual(str1, str2));
    }

}