import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray_typicalCase() {
        String s = "SGVsbG8gV29ybGQ=";
        byte[] expectedResult = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        byte[] result = Base64.altBase64ToByteArray(s);
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testAltBase64ToByteArray_emptyString() {
        String s = "";
        byte[] expectedResult = {};
        byte[] result = Base64.altBase64ToByteArray(s);
        assertArrayEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_invalidStringLength() {
        String s = "SGVsbG8gV29ybGQ";
        Base64.altBase64ToByteArray(s);
    }

    @Test
    public void testBase64ToByteArray_typicalCase() {
        String s = "SGVsbG8gV29ybGQ=";
        byte[] expectedResult = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        byte[] result = Base64.base64ToByteArray(s);
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testBase64ToByteArray_emptyString() {
        String s = "";
        byte[] expectedResult = {};
        byte[] result = Base64.base64ToByteArray(s);
        assertArrayEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_invalidStringLength() {
        String s = "SGVsbG8gV29ybGQ";
        Base64.base64ToByteArray(s);
    }

    @Test
    public void testByteArrayToAltBase64_typicalCase() {
        byte[] bb = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        String expectedResult = "SGVsbG8gV29ybGQ=";
        String result = Base64.byteArrayToAltBase64(bb);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testByteArrayToAltBase64_emptyArray() {
        byte[] bb = {};
        String expectedResult = "";
        String result = Base64.byteArrayToAltBase64(bb);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testByteArrayToBase64_typicalCase() {
        byte[] bb = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        String expectedResult = "SGVsbG8gV29ybGQ=";
        String result = Base64.byteArrayToBase64(bb);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testByteArrayToBase64_emptyArray() {
        byte[] bb = {};
        String expectedResult = "";
        String result = Base64.byteArrayToBase64(bb);
        assertEquals(expectedResult, result);
    }

}