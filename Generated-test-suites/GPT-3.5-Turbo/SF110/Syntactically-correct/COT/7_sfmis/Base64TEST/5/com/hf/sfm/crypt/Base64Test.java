import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        byte[] expected = {(byte) 1, (byte) 2, (byte) 3};
        byte[] actual = Base64.altBase64ToByteArray("AQID");
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testBase64ToByteArray() {
        byte[] expected = {(byte) 1, (byte) 2, (byte) 3};
        byte[] actual = Base64.base64ToByteArray("AQID");
        assertArrayEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayWithInvalidLength() {
        Base64.altBase64ToByteArray("AQ==");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayWithInvalidLength() {
        Base64.base64ToByteArray("AQ==");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayWithInvalidCharacter() {
        Base64.altBase64ToByteArray("A!ID");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayWithInvalidCharacter() {
        Base64.base64ToByteArray("A!ID");
    }

    @Test
    public void testByteArrayToAltBase64() {
        String expected = "AQID";
        String actual = Base64.byteArrayToAltBase64(new byte[]{(byte) 1, (byte) 2, (byte) 3});
        assertEquals(expected, actual);
    }

    @Test
    public void testByteArrayToBase64() {
        String expected = "AQID";
        String actual = Base64.byteArrayToBase64(new byte[]{(byte) 1, (byte) 2, (byte) 3});
        assertEquals(expected, actual);
    }

    @Test
    public void testMain() {
        String s = "0123456789";
        byte[] b = s.getBytes();
        s = Base64.byteArrayToBase64(b);
        assertEquals("MDEyMzQ1Njc4OQ==", s);
        b = Base64.base64ToByteArray(s);
        assertEquals("0123456789", new String(b));
    }

}