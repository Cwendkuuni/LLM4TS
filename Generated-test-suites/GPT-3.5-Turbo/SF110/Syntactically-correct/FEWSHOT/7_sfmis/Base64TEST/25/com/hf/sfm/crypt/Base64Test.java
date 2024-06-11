import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, Base64.altBase64ToByteArray("AAECAwQFBgcICQ=="));
        assertArrayEquals(new byte[]{17, 18, 19, 20, 21, 22, 23, 24, 25, 26}, Base64.altBase64ToByteArray("EzcTFhogJZinPCRE"));
        assertArrayEquals(new byte[]{25, 24, 23, 22, 21, 20, 19, 18, 17}, Base64.altBase64ToByteArray("JYgZFAwTFfdcQRZX"));
        assertArrayEquals(new byte[]{33, 34, 35, 36, 37, 38, 39, 40, 41, 42}, Base64.altBase64ToByteArray("RZQZGBohJSmjLyYm"));
    }

    @Test
    public void testBase64ToByteArray() {
        assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, Base64.base64ToByteArray("AAECAwQFBgcICQ=="));
        assertArrayEquals(new byte[]{17, 18, 19, 20, 21, 22, 23, 24, 25, 26}, Base64.base64ToByteArray("EzcTFhogJZinPCRE"));
        assertArrayEquals(new byte[]{25, 24, 23, 22, 21, 20, 19, 18, 17}, Base64.base64ToByteArray("JYgZFAwTFfdcQRZX"));
        assertArrayEquals(new byte[]{33, 34, 35, 36, 37, 38, 39, 40, 41, 42}, Base64.base64ToByteArray("RZQZGBohJSmjLyYm"));
    }

    @Test
    public void testByteArrayToAltBase64() {
        assertEquals("AAECAwQFBgcICQ==", Base64.byteArrayToAltBase64(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}));
        assertEquals("EzcTFhogJZinPCRE", Base64.byteArrayToAltBase64(new byte[]{17, 18, 19, 20, 21, 22, 23, 24, 25, 26}));
        assertEquals("JYgZFAwTFfdcQRZX", Base64.byteArrayToAltBase64(new byte[]{25, 24, 23, 22, 21, 20, 19, 18, 17}));
        assertEquals("RZQZGBohJSmjLyYm", Base64.byteArrayToAltBase64(new byte[]{33, 34, 35, 36, 37, 38, 39, 40, 41, 42}));
    }

    @Test
    public void testByteArrayToBase64() {
        assertEquals("AAECAwQFBgcICQ==", Base64.byteArrayToBase64(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}));
        assertEquals("EzcTFhogJZinPCRE", Base64.byteArrayToBase64(new byte[]{17, 18, 19, 20, 21, 22, 23, 24, 25, 26}));
        assertEquals("JYgZFAwTFfdcQRZX", Base64.byteArrayToBase64(new byte[]{25, 24, 23, 22, 21, 20, 19, 18, 17}));
        assertEquals("RZQZGBohJSmjLyYm", Base64.byteArrayToBase64(new byte[]{33, 34, 35, 36, 37, 38, 39, 40, 41, 42}));
    }
}