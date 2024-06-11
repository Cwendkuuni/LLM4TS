import org.junit.Assert;
import org.junit.Test;

public class Base64Test {
    
    @Test
    public void testAltBase64ToByteArray() {
        // Test input string with different lengths
        String input1 = "QmFzZTY0";
        byte[] expected1 = {66, 97, 115, 101, 54, 52};
        Assert.assertArrayEquals(expected1, Base64.altBase64ToByteArray(input1));
        
        String input2 = "SGVsbG8gd29ybGQ=";
        byte[] expected2 = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        Assert.assertArrayEquals(expected2, Base64.altBase64ToByteArray(input2));
        
        String input3 = "U3dvcmRz";
        byte[] expected3 = {83, 119, 111, 114, 100, 115};
        Assert.assertArrayEquals(expected3, Base64.altBase64ToByteArray(input3));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayInvalidLength() {
        // Test input string with invalid length
        String input = "QmFz";
        Base64.altBase64ToByteArray(input);
    }
    
    @Test
    public void testBase64ToByteArray() {
        // Test input string with different lengths
        String input1 = "QmFzZTY0";
        byte[] expected1 = {66, 97, 115, 101, 54, 52};
        Assert.assertArrayEquals(expected1, Base64.base64ToByteArray(input1));
        
        String input2 = "SGVsbG8gd29ybGQ=";
        byte[] expected2 = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        Assert.assertArrayEquals(expected2, Base64.base64ToByteArray(input2));
        
        String input3 = "U3dvcmRz";
        byte[] expected3 = {83, 119, 111, 114, 100, 115};
        Assert.assertArrayEquals(expected3, Base64.base64ToByteArray(input3));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayInvalidLength() {
        // Test input string with invalid length
        String input = "QmFz";
        Base64.base64ToByteArray(input);
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        // Test input byte array with different lengths
        byte[] input1 = {66, 97, 115, 101, 54, 52};
        String expected1 = "QmFzZTY0";
        Assert.assertEquals(expected1, Base64.byteArrayToAltBase64(input1));
        
        byte[] input2 = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        String expected2 = "SGVsbG8gd29ybGQ=";
        Assert.assertEquals(expected2, Base64.byteArrayToAltBase64(input2));
        
        byte[] input3 = {83, 119, 111, 114, 100, 115};
        String expected3 = "U3dvcmRz";
        Assert.assertEquals(expected3, Base64.byteArrayToAltBase64(input3));
    }
    
    @Test
    public void testByteArrayToBase64() {
        // Test input byte array with different lengths
        byte[] input1 = {66, 97, 115, 101, 54, 52};
        String expected1 = "QmFzZTY0";
        Assert.assertEquals(expected1, Base64.byteArrayToBase64(input1));
        
        byte[] input2 = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        String expected2 = "SGVsbG8gd29ybGQ=";
        Assert.assertEquals(expected2, Base64.byteArrayToBase64(input2));
        
        byte[] input3 = {83, 119, 111, 114, 100, 115};
        String expected3 = "U3dvcmRz";
        Assert.assertEquals(expected3, Base64.byteArrayToBase64(input3));
    }
    
    @Test
    public void testMain() {
        // Redirect standard output to capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        
        // Test main method with example string
        String[] args = {};
        Base64.main(args);
        String expectedOutput = "MDEyMzQ1Njc4OQ==\r\n0123456789\r\n";
        Assert.assertEquals(expectedOutput, outContent.toString());
        
        // Reset standard output
        System.setOut(originalOut);
    }
}