import static org.junit.Assert.*;
import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        assertArrayEquals(null, Base64.altBase64ToByteArray(null));
        assertArrayEquals(new byte[] {}, Base64.altBase64ToByteArray(""));
        assertArrayEquals(new byte[] {0, 1, 2}, Base64.altBase64ToByteArray("AAE="));
        assertArrayEquals(new byte[] {0, 1, 2, 3}, Base64.altBase64ToByteArray("AAEC"));
        assertArrayEquals(new byte[] {0, 1, 2, 3, 4}, Base64.altBase64ToByteArray("AAEDBQ=="));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testAltBase64ToByteArrayInvalidLength() {
        Base64.altBase64ToByteArray("A");
    }

    @Test
    public void testBase64ToByteArray() {
        assertArrayEquals(null, Base64.base64ToByteArray(null));
        assertArrayEquals(new byte[] {}, Base64.base64ToByteArray(""));
        assertArrayEquals(new byte[] {0, 1, 2}, Base64.base64ToByteArray("AAE="));
        assertArrayEquals(new byte[] {0, 1, 2, 3}, Base64.base64ToByteArray("AAEC"));
        assertArrayEquals(new byte[] {0, 1, 2, 3, 4}, Base64.base64ToByteArray("AAEDBQ=="));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testBase64ToByteArrayInvalidLength() {
        Base64.base64ToByteArray("A");
    }

    @Test
    public void testBytearrayToAltBase64() {
        assertEquals("", Base64.byteArrayToAltBase64(new byte[] {}));
        assertEquals("AAE=", Base64.byteArrayToAltBase64(new byte[] {0, 1, 2}));
        assertEquals("AAEC", Base64.byteArrayToAltBase64(new byte[] {0, 1, 2, 3}));
        assertEquals("AAEDBQ==", Base64.byteArrayToAltBase64(new byte[] {0, 1, 2, 3, 4}));
    }

    @Test
    public void testBytearrayToBase64() {
        assertEquals("", Base64.byteArrayToBase64(new byte[] {}));
        assertEquals("AAE=", Base64.byteArrayToBase64(new byte[] {0, 1, 2}));
        assertEquals("AAEC", Base64.byteArrayToBase64(new byte[] {0, 1, 2, 3}));
        assertEquals("AAEDBQ==", Base64.byteArrayToBase64(new byte[] {0, 1, 2, 3, 4}));
    }

    @Test
    public void testBytearrayToAltBase64AndBase64() {
        byte[] byteArray = {};
        String encodedAltBase64 = Base64.byteArrayToAltBase64(byteArray);
        byte[] decodedAltBase64 = Base64.altBase64ToByteArray(encodedAltBase64);
        assertArrayEquals(byteArray, decodedAltBase64);

        byteArray = new byte[] {0, 1, 2, 3, 4};
        encodedAltBase64 = Base64.byteArrayToAltBase64(byteArray);
        decodedAltBase64 = Base64.altBase64ToByteArray(encodedAltBase64);
        assertArrayEquals(byteArray, decodedAltBase64);
    }
}