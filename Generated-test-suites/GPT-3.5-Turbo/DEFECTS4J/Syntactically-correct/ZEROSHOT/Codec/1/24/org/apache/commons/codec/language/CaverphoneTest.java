import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();

        // Test 1
        String input1 = "hello";
        String expectedOutput1 = "hA11111111";
        assertEquals(expectedOutput1, caverphone.caverphone(input1));

        // Test 2
        String input2 = "world";
        String expectedOutput2 = "wAr11111111";
        assertEquals(expectedOutput2, caverphone.caverphone(input2));

        // Test 3
        String input3 = "";
        String expectedOutput3 = "1111111111";
        assertEquals(expectedOutput3, caverphone.caverphone(input3));

        // Test 4
        String input4 = "123";
        String expectedOutput4 = "1111111111";
        assertEquals(expectedOutput4, caverphone.caverphone(input4));
    }

    @Test
    public void testEncode() throws EncoderException {
        Caverphone caverphone = new Caverphone();

        // Test 1
        String input1 = "hello";
        String expectedOutput1 = "hA11111111";
        assertEquals(expectedOutput1, caverphone.encode(input1));

        // Test 2
        String input2 = "world";
        String expectedOutput2 = "wAr11111111";
        assertEquals(expectedOutput2, caverphone.encode(input2));

        // Test 3
        String input3 = "";
        String expectedOutput3 = "1111111111";
        assertEquals(expectedOutput3, caverphone.encode(input3));

        // Test 4
        String input4 = "123";
        String expectedOutput4 = "1111111111";
        assertEquals(expectedOutput4, caverphone.encode(input4));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        // Test 1
        String str1 = "hello";
        String str2 = "hA11111111";
        assertTrue(caverphone.isCaverphoneEqual(str1, str2));

        // Test 2
        String str3 = "world";
        String str4 = "wAr11111111";
        assertTrue(caverphone.isCaverphoneEqual(str3, str4));

        // Test 3
        String str5 = "hello";
        String str6 = "wAr11111111";
        assertFalse(caverphone.isCaverphoneEqual(str5, str6));

        // Test 4
        String str7 = "";
        String str8 = "1111111111";
        assertTrue(caverphone.isCaverphoneEqual(str7, str8));
    }
}