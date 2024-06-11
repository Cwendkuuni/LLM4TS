import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        String input = "HelloWorld";
        String expectedOutput = "H2h2l2o2l2w2r2d2";
        String actualOutput = caverphone.caverphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        String input = "HelloWorld";
        String expectedOutput = "H2h2l2o2l2w2r2d2";
        String actualOutput = caverphone.encode(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        String str1 = "HelloWorld";
        String str2 = "HelloWorld";
        String str3 = "HelloWorld1";
        boolean expectedOutput = true;
        boolean actualOutput = caverphone.isCaverphoneEqual(str1, str2);
        assertTrue(expectedOutput == actualOutput);

        actualOutput = caverphone.isCaverphoneEqual(str1, str3);
        assertFalse(expectedOutput == actualOutput);
    }

    // Edge cases and exception handling scenarios to be added here
}