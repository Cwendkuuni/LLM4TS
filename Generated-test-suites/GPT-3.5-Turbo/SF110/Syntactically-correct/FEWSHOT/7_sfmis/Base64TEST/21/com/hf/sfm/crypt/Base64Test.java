import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class Base64Test {
  
    @Test
    public void testAltBase64ToByteArray() {
        String base64String = "fGZvZWJhcg==";
        byte[] expectedByteArray = {102, 111, 111, 98, 97, 114};
        byte[] actualByteArray = Base64.altBase64ToByteArray(base64String);
        assertArrayEquals(expectedByteArray, actualByteArray);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testAltBase64ToByteArrayThrowsExceptionWhenInvalidStringLength() {
        String base64String = "fGZvZWJh";
        Base64.altBase64ToByteArray(base64String);
    }
    
    @Test
    public void testBase64ToByteArray() {
        String base64String = "MTIzNDU2Nzg5";
        byte[] expectedByteArray = {49, 50, 51, 52, 53, 54, 55, 56, 57};
        byte[] actualByteArray = Base64.base64ToByteArray(base64String);
        assertArrayEquals(expectedByteArray, actualByteArray);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testBase64ToByteArrayThrowsExceptionWhenInvalidStringLength() {
        String base64String = "MTIzNDU2Nzg5Nzg5";
        Base64.base64ToByteArray(base64String);
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        byte[] byteArray = {102, 111, 111, 98, 97, 114};
        String expectedBase64String = "fGZvZWJhcg==";
        String actualBase64String = Base64.byteArrayToAltBase64(byteArray);
        assertEquals(expectedBase64String, actualBase64String);
    }
    
    @Test
    public void testByteArrayToAltBase64WithPadding() {
        byte[] byteArray = {49, 50, 51, 52, 53, 54, 55, 56, 57};
        String expectedBase64String = "MTIzNDU2Nzg5";
        String actualBase64String = Base64.byteArrayToAltBase64(byteArray);
        assertEquals(expectedBase64String, actualBase64String);
    }
    
    @Test
    public void testByteArrayToAltBase64WithoutPadding() {
        byte[] byteArray = {49, 50, 51, 52, 53, 54, 55, 56};
        String expectedBase64String = "MTIzNDU2Nzg4";
        String actualBase64String = Base64.byteArrayToAltBase64(byteArray);
        assertEquals(expectedBase64String, actualBase64String);
    }
    
    @Test
    public void testByteArrayToBase64() {
        byte[] byteArray = {102, 111, 111, 98, 97, 114};
        byte[] encodedByteArray = {49, 50, 51, 52, 53, 54, 55, 56, 57};
        String encodedBase64String = Base64.byteArrayToAltBase64(encodedByteArray);
        assertEquals(encodedBase64String, Base64.byteArrayToBase64(byteArray));
    }
    
}