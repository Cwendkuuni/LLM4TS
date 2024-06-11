import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class Base64Test {
    
    @Test
    public void testAltBase64ToByteArray() {
        byte[] expected = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        byte[] result = Base64.altBase64ToByteArray("AAECAwQFBgcICQ==");
        assertArrayEquals(expected, result);
        
        expected = new byte[] {10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24};
        result = Base64.altBase64ToByteArray("CgsMDQ4PEBESExQVFhcYGRobHB0eHyAhIiMkJSYnKCkqKywtLi8wMTIzNDU2Nzg5");
        assertArrayEquals(expected, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayInvalidInput() {
        Base64.altBase64ToByteArray("A===");
    }
    
    @Test
    public void testBase64ToByteArray() {
        byte[] expected = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        byte[] result = Base64.base64ToByteArray("AAECAwQFBgcICQ==");
        assertArrayEquals(expected, result);
        
        expected = new byte[] {10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24};
        result = Base64.base64ToByteArray("CgsMDQ4PEBESExQVFhcYGRobHB0eHyAhIiMkJSYnKCkqKywtLi8wMTIzNDU2Nzg5");
        assertArrayEquals(expected, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayInvalidInput() {
        Base64.base64ToByteArray("A===");
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        byte[] bb = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        assertEquals("AAECAwQFBgcICQ==", Base64.byteArrayToAltBase64(bb));
        
        bb = new byte[] {10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24};
        assertEquals("CgsMDQ4PEBESExQVFhcYGRobHB0eHyAhIiMkJSYnKCkqKywtLi8wMTIzNDU2Nzg5", Base64.byteArrayToAltBase64(bb));
    }
    
    @Test
    public void testByteArrayToBase64() {
        byte[] bb = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        assertEquals("AAECAwQFBgcICQ==", Base64.byteArrayToBase64(bb));
        
        bb = new byte[] {10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24};
        assertEquals("CgsMDQ4PEBESExQVFhcYGRobHB0eHyAhIiMkJSYnKCkqKywtLi8wMTIzNDU2Nzg5", Base64.byteArrayToBase64(bb));
    }
}