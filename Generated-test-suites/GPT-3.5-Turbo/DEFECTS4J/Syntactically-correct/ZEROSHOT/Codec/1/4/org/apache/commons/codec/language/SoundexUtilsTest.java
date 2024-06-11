import org.junit.Assert;
import org.junit.Test;

public class SoundexUtilsTest {
    
    @Test
    public void testClean() {
        String str = "aBcDeF";
        String expected = "ABCDEF";
        String actual = SoundexUtils.clean(str);
        Assert.assertEquals(expected, actual);
        
        str = "123!@#$";
        expected = "";
        actual = SoundexUtils.clean(str);
        Assert.assertEquals(expected, actual);
        
        str = null;
        expected = null;
        actual = SoundexUtils.clean(str);
        Assert.assertEquals(expected, actual);
        
        str = "";
        expected = "";
        actual = SoundexUtils.clean(str);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "hello";
        String s2 = "hallo";
        int expected = 3;
        int actual = SoundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(expected, actual);
        
        s1 = "";
        s2 = "world";
        expected = 0;
        actual = SoundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(expected, actual);
        
        s1 = null;
        s2 = "hello";
        expected = 0;
        actual = SoundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testDifferenceEncoded() {
        String es1 = "H400";
        String es2 = "H100";
        int expected = 3;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(expected, actual);
        
        es1 = "";
        es2 = "W200";
        expected = 0;
        actual = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(expected, actual);
        
        es1 = null;
        es2 = "H400";
        expected = 0;
        actual = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(expected, actual);
    }
}