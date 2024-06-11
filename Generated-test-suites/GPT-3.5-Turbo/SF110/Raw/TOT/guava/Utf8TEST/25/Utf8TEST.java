import org.junit.Assert;
import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength_typicalCase() {
        CharSequence sequence = "hello";
        int result = Utf8.encodedLength(sequence);
        Assert.assertEquals(5, result);
    }
    
    @Test
    public void testEncodedLength_edgeCase() {
        CharSequence sequence = "";
        int result = Utf8.encodedLength(sequence);
        Assert.assertEquals(0, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLength_errorCase() {
        CharSequence sequence = "hello\u2603";
        Utf8.encodedLength(sequence);
    }
    
    @Test
    public void testIsWellFormed_typicalCase() {
        byte[] bytes = {72, 101, 108, 108, 111};
        boolean result = Utf8.isWellFormed(bytes);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testIsWellFormed_edgeCase() {
        byte[] bytes = {};
        boolean result = Utf8.isWellFormed(bytes);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testIsWellFormed_errorCase() {
        byte[] bytes = {72, -50, 108};
        boolean result = Utf8.isWellFormed(bytes);
        Assert.assertFalse(result);
    }
    
    @Test
    public void testIsWellFormedSlowPath_typicalCase() {
        byte[] bytes = {72, -95, 108};
        boolean result = Utf8.isWellFormedSlowPath(bytes, 1, 3);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testIsWellFormedSlowPath_edgeCase() {
        byte[] bytes = {72};
        boolean result = Utf8.isWellFormedSlowPath(bytes, 0, 1);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testIsWellFormedSlowPath_errorCase1() {
        byte[] bytes = {-10, -50};
        boolean result = Utf8.isWellFormedSlowPath(bytes, 0, 2);
        Assert.assertFalse(result);
    }
    
    @Test
    public void testIsWellFormedSlowPath_errorCase2() {
        byte[] bytes = {-95, 108};
        boolean result = Utf8.isWellFormedSlowPath(bytes, 0, 2);
        Assert.assertFalse(result);
    }
    
    @Test
    public void testIsWellFormedSlowPath_errorCase3() {
        byte[] bytes = {-95, -95, 72};
        boolean result = Utf8.isWellFormedSlowPath(bytes, 0, 3);
        Assert.assertFalse(result);
    }
}