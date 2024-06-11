import org.junit.Test;
import org.junit.Assert;

public class CaverphoneTest {
    
    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        String input = "";
        String expectedOutput = "1111111111";
        String actualOutput = caverphone.caverphone(input);
        Assert.assertEquals(expectedOutput, actualOutput);
        
        input = null;
        expectedOutput = "1111111111";
        actualOutput = caverphone.caverphone(input);
        Assert.assertEquals(expectedOutput, actualOutput);
        
        input = "Test String";
        expectedOutput = "TS1114131";
        actualOutput = caverphone.caverphone(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testEncode() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        
        Object input = null;
        String expectedOutput = "1111111111";
        Object actualOutput = caverphone.encode(input);
        Assert.assertEquals(expectedOutput, actualOutput);
        
        input = "Test String";
        expectedOutput = "TS1114131";
        actualOutput = caverphone.encode(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        String str1 = "Test";
        String str2 = "Test";
        boolean expectedOutput = true;
        boolean actualOutput = caverphone.isCaverphoneEqual(str1, str2);
        Assert.assertEquals(expectedOutput, actualOutput);
        
        str1 = "Test";
        str2 = "TestString";
        expectedOutput = false;
        actualOutput = caverphone.isCaverphoneEqual(str1, str2);
        Assert.assertEquals(expectedOutput, actualOutput);
    }
}