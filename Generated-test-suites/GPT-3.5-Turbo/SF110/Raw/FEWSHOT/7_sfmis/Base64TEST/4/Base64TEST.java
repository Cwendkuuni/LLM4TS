import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        byte[] expected = new byte[] {1, 2, 3, 4, 5, 6};
        byte[] result = Base64.altBase64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, result);

        expected = new byte[] {115, 101, 99, 114, 101, 116};
        result = Base64.altBase64ToByteArray("c2VyZXQ=");
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBase64ToByteArray() {
        byte[] expected = new byte[] {1, 2, 3, 4, 5, 6};
        byte[] result = Base64.base64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, result);

        expected = new byte[] {115, 101, 99, 114, 101, 116};
        result = Base64.base64ToByteArray("c2VyZXQ=");
        assertArrayEquals(expected, result);
    }

    @Test
    public void test_$23183() {
        byte[] bb = new byte[] {1, 2, 3, 4, 5, 6};
        byte[] expected = new byte[] {-1, -1, -1, -1, -1, -1};
        for (int i = 0; i < bb.length; i++) {
            assertEquals(expected[i], Base64._$23183(bb[i], Base64._$23169));
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] bb = new byte[] {1, 2, 3, 4, 5, 6};
        String expected = "AQIDBAUG";
        String result = Base64.byteArrayToAltBase64(bb);
        assertEquals(expected, result);

        bb = new byte[] {115, 101, 99, 114, 101, 116};
        expected = "c2VyZXQ=";
        result = Base64.byteArrayToAltBase64(bb);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] bb = new byte[] {1, 2, 3, 4, 5, 6};
        String expected = "AQIDBAUG";
        String result = Base64.byteArrayToBase64(bb);
        assertEquals(expected, result);

        bb = new byte[] {115, 101, 99, 114, 101, 116};
        expected = "c2VyZXQ=";
        result = Base64.byteArrayToBase64(bb);
        assertEquals(expected, result);
    }
}