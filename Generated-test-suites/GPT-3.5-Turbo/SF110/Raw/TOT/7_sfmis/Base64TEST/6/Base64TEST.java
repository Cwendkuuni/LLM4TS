import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {
    
    // Test case for altBase64ToByteArray method
    @Test
    public void testAltBase64ToByteArray() {
        String base64String = "SGVsbG8gd29ybGQ=";
        byte[] expectedBytes = new byte[] { 72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100 };
        byte[] resultBytes = Base64.altBase64ToByteArray(base64String);
        assertArrayEquals(expectedBytes, resultBytes);
    }
    
    // Test case for base64ToByteArray method
    @Test
    public void testBase64ToByteArray() {
        String base64String = "SGVsbG8gd29ybGQ=";
        byte[] expectedBytes = new byte[] { 72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100 };
        byte[] resultBytes = Base64.base64ToByteArray(base64String);
        assertArrayEquals(expectedBytes, resultBytes);
    }
    
    // Test case for byteArrayToAltBase64 method
    @Test
    public void testByteArrayToAltBase64() {
        byte[] bytes = new byte[] { 72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100 };
        String expectedBase64 = "SGVsbG8gd29ybGQ=";
        String resultBase64 = Base64.byteArrayToAltBase64(bytes);
        assertEquals(expectedBase64, resultBase64);
    }
    
    // Test case for byteArrayToBase64 method
    @Test
    public void testByteArrayToBase64() {
        byte[] bytes = new byte[] { 72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100 };
        String expectedBase64 = "SGVsbG8gd29ybGQ=";
        String resultBase64 = Base64.byteArrayToBase64(bytes);
        assertEquals(expectedBase64, resultBase64);
    }
    
    // Test case for _$23180 method (positive case - string length multiple of four)
    @Test
    public void test_$23180_Positive() {
        String base64String = "SGVsbG8gd29ybGQ=";
        byte[] expectedBytes = new byte[] { 72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100 };
        byte[] resultBytes = Base64._$23180(base64String, true);
        assertArrayEquals(expectedBytes, resultBytes);
    }
    
    // Test case for _$23180 method (negative case - string length not multiple of four)
    @Test(expected = IllegalArgumentException.class)
    public void test_$23180_Negative() {
        String base64String = "SGVsbG8gd29yb";
        Base64._$23180(base64String, true);
    }
    
    // Test case for _$23183 method (positive case - valid character)
    @Test
    public void test_$23183_Positive() {
        char c = 'a';
        byte[] bb = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, -1, 62, 9, 10, 11, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 12, 13, 14, -1, 15, 63, 16, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 17, -1, 18, 19, 21, 20, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 22, 23, 24, 25 };
        int expectedValue = 52;
        int resultValue = Base64._$23183(c, bb);
        assertEquals(expectedValue, resultValue);
    }
    
    // Test case for _$23183 method (negative case - invalid character)
    @Test(expected = IllegalArgumentException.class)
    public void test_$23183_Negative() {
        char c = '?';
        byte[] bb = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };
        Base64._$23183(c, bb);
    }
  
}