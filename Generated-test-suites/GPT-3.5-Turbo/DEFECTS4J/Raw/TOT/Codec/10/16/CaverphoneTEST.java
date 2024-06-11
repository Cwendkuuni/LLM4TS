import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();

        // Typical use case
        String input1 = "hello";
        String expectedOutput1 = "hla1111111";
        Assert.assertEquals(expectedOutput1, caverphone.caverphone(input1));

        // Edge case: empty string
        String input2 = "";
        String expectedOutput2 = "1111111111";
        Assert.assertEquals(expectedOutput2, caverphone.caverphone(input2));

        // Edge case: null string
        Assert.assertNull(caverphone.caverphone(null));
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();

        // Typical use case
        String input1 = "hello";
        String expectedOutput1 = "hla1111111";
        Assert.assertEquals(expectedOutput1, caverphone.encode(input1));

        // Edge case: empty string
        String input2 = "";
        String expectedOutput2 = "1111111111";
        Assert.assertEquals(expectedOutput2, caverphone.encode(input2));

        // Edge case: null string
        Assert.assertNull(caverphone.encode(null));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        // Typical use case: caverphones are equal
        String input1 = "hello";
        String input2 = "hello";
        Assert.assertTrue(caverphone.isCaverphoneEqual(input1, input2));

        // Typical use case: caverphones are not equal
        String input3 = "hello";
        String input4 = "world";
        Assert.assertFalse(caverphone.isCaverphoneEqual(input3, input4));

        // Edge case: empty strings
        String input5 = "";
        String input6 = "";
        Assert.assertTrue(caverphone.isCaverphoneEqual(input5, input6));

        // Edge case: null strings
        Assert.assertTrue(caverphone.isCaverphoneEqual(null, null));
        Assert.assertFalse(caverphone.isCaverphoneEqual(null, "hello"));
        Assert.assertFalse(caverphone.isCaverphoneEqual("hello", null));
    }

}