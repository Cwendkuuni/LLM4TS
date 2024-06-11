import org.junit.Assert;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        String result = metaphone.metaphone("hello");
        Assert.assertEquals("HL", result);
        
        result = metaphone.metaphone("world");
        Assert.assertEquals("WRLT", result);
        
        result = metaphone.metaphone("Java");
        Assert.assertEquals("JF", result);
        
        result = metaphone.metaphone("JUnit");
        Assert.assertEquals("JNT", result);
    }
    
    @Test
    public void testEncode() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        
        Object result = metaphone.encode("hello");
        Assert.assertEquals("HL", result);
        
        result = metaphone.encode("world");
        Assert.assertEquals("WRLT", result);
        
        result = metaphone.encode("Java");
        Assert.assertEquals("JF", result);
        
        result = metaphone.encode("JUnit");
        Assert.assertEquals("JNT", result);
    }
    
    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        
        boolean result = metaphone.isMetaphoneEqual("hello", "hlo");
        Assert.assertTrue(result);
        
        result = metaphone.isMetaphoneEqual("world", "WLDT");
        Assert.assertTrue(result);
        
        result = metaphone.isMetaphoneEqual("Java", "JF");
        Assert.assertTrue(result);
        
        result = metaphone.isMetaphoneEqual("JUnit", "JNT");
        Assert.assertTrue(result);
    }
    
    @Test
    public void testGetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        
        int result = metaphone.getMaxCodeLen();
        Assert.assertEquals(4, result);
    }
    
    @Test
    public void testSetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        
        metaphone.setMaxCodeLen(3);
        int result = metaphone.getMaxCodeLen();
        Assert.assertEquals(3, result);
    }

}