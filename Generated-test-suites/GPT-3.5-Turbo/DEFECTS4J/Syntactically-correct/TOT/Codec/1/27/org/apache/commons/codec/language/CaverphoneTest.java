import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CaverphoneTest {
    private Caverphone caverphone;

    @Before
    public void setup() {
        caverphone = new Caverphone();
    }

    // Test caverphone method

    @Test
    public void testCaverphone_typicalCase() {
        String input = "example";
        String expectedOutput = "1MF1PL1";
        
        String output = caverphone.caverphone(input);
        
        Assert.assertEquals(expectedOutput, output);
    }
    
    @Test
    public void testCaverphone_emptyInput() {
        String input = "";
        String expectedOutput = "1111111111";
        
        String output = caverphone.caverphone(input);
        
        Assert.assertEquals(expectedOutput, output);
    }
    
    @Test
    public void testCaverphone_nullInput() {
        String input = null;
        String expectedOutput = "1111111111";
        
        String output = caverphone.caverphone(input);
        
        Assert.assertEquals(expectedOutput, output);
    }
    
    // Test isCaverphoneEqual method
    
    @Test
    public void testIsCaverphoneEqual_identicalStrings() {
        String str1 = "example";
        String str2 = "example";
        
        boolean result = caverphone.isCaverphoneEqual(str1, str2);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testIsCaverphoneEqual_differentStrings() {
        String str1 = "example";
        String str2 = "exampel";
        
        boolean result = caverphone.isCaverphoneEqual(str1, str2);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testIsCaverphoneEqual_emptyStrings() {
        String str1 = "";
        String str2 = "";
        
        boolean result = caverphone.isCaverphoneEqual(str1, str2);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testIsCaverphoneEqual_nullStrings() {
        String str1 = null;
        String str2 = null;
        
        boolean result = caverphone.isCaverphoneEqual(str1, str2);
        
        Assert.assertTrue(result);
    }
    
    // Test encode method
    
    @Test
    public void testEncode_typicalCase() {
        String input = "example";
        String expectedOutput = "1MF1PL1";
        
        String output = caverphone.encode(input);
        
        Assert.assertEquals(expectedOutput, output);
    }
    
    @Test
    public void testEncode_emptyInput() {
        String input = "";
        String expectedOutput = "1111111111";
        
        String output = caverphone.encode(input);
        
        Assert.assertEquals(expectedOutput, output);
    }
    
    @Test
    public void testEncode_nullInput() {
        String input = null;
        String expectedOutput = "1111111111";
        
        String output = caverphone.encode(input);
        
        Assert.assertEquals(expectedOutput, output);
    }
    
    // Test encode method with non-string parameter
    
    @Test(expected = EncoderException.class)
    public void testEncode_nonStringParameter() {
        Object input = new Object();
        
        caverphone.encode(input);
    }
}