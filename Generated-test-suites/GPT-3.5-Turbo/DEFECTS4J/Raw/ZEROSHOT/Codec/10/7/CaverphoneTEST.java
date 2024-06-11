import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Test 1: Empty input
        Assert.assertEquals("1111111111", caverphone.caverphone(""));
        
        // Test 2: Only lowercase letters
        Assert.assertEquals("12345aaaaa", caverphone.caverphone("abcde"));
        
        // Test 3: Ignore non-alphabetic characters
        Assert.assertEquals("2345aaaaa", caverphone.caverphone("a!b#c$d%e^"));
        
        // Test 4: Convert to lowercase
        Assert.assertEquals("aaaaabbbbb", caverphone.caverphone("AAAAbbbbb"));
        
        // Test 5: Remove final 'e'
        Assert.assertEquals("aaaaabbbb", caverphone.caverphone("AAAAbbbbe"));
        
        // Test 6: Handle start options
        Assert.assertEquals("cou2fabcde", caverphone.caverphone("coughabcde"));
        Assert.assertEquals("rou2fabcde", caverphone.caverphone("roughabcde"));
        Assert.assertEquals("tou2fabcde", caverphone.caverphone("toughabcde"));
        Assert.assertEquals("enou2fabcde", caverphone.caverphone("enoughabcde"));
        Assert.assertEquals("trou2fabcde", caverphone.caverphone("troughabcde"));
        Assert.assertEquals("2nabcde", caverphone.caverphone("gnabcde"));
        
        // Test 7: Handle end options
        Assert.assertEquals("m2abcde", caverphone.caverphone("mbabcde"));
        
        // Test 8: Handle replacements
        Assert.assertEquals("2qabcde", caverphone.caverphone("cqabcde"));
        Assert.assertEquals("siabcde", caverphone.caverphone("ciabcde"));
        Assert.assertEquals("seabcde", caverphone.caverphone("ceabcde"));
        Assert.assertEquals("syabcde", caverphone.caverphone("cyabcde"));
        Assert.assertEquals("2chabcde", caverphone.caverphone("tchabcde"));
        Assert.assertEquals("kabcde", caverphone.caverphone("cabcde"));
        Assert.assertEquals("kabcde", caverphone.caverphone("qabcde"));
        Assert.assertEquals("kabcde", caverphone.caverphone("xabcde"));
        Assert.assertEquals("fabcde", caverphone.caverphone("vabcde"));
        Assert.assertEquals("2gabcde", caverphone.caverphone("dgabcde"));
        Assert.assertEquals("sioabcde", caverphone.caverphone("tioabcde"));
        Assert.assertEquals("siaabcde", caverphone.caverphone("tiaabcde"));
        Assert.assertEquals("tabcde", caverphone.caverphone("dabcde"));
        Assert.assertEquals("fhabcde", caverphone.caverphone("phabcde"));
        Assert.assertEquals("pabcde", caverphone.caverphone("babcde"));
        Assert.assertEquals("s2abcde", caverphone.caverphone("shabcde"));
        Assert.assertEquals("sabcde", caverphone.caverphone("zabcde"));
        Assert.assertEquals("Aabcde", caverphone.caverphone("aabcde"));
        Assert.assertEquals("3abcde", caverphone.caverphone("eabcde"));
        Assert.assertEquals("3xbcde", caverphone.caverphone("exbcde"));
        Assert.assertEquals("3gh3abcdef", caverphone.caverphone("3gh3abcdef"));
        Assert.assertEquals("22h2ijklmnopq2rstu2vwxyz", caverphone.caverphone("ghijklmnopqrstuvwxyz"));
        Assert.assertEquals("2a9bcdefeabcde", caverphone.caverphone("ghabbbcdefeabcde"));
        Assert.assertEquals("2abcdfghijk3mnpqrst", caverphone.caverphone("gabcdfghijklmnopqrst"));
        Assert.assertEquals("R3abcde", caverphone.caverphone("rabcde"));
        Assert.assertEquals("L3abcde", caverphone.caverphone("labcde"));
        Assert.assertEquals("A3abcde", caverphone.caverphone("yabcde"));
        
        // Test 9: Handle removals
        Assert.assertEquals("abcde", caverphone.caverphone("a2bc3de"));
        
        // Test 10: Code length
        Assert.assertEquals(10, caverphone.caverphone("abcdefghijklmnopqrstuvwxyz").length());
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        // Test 1: Empty input
        Assert.assertEquals("1111111111", caverphone.encode(""));
        
        // Test 2: Only lowercase letters
        Assert.assertEquals("12345aaaaa", caverphone.encode("abcde"));
        
        // Test 3: Ignore non-alphabetic characters
        Assert.assertEquals("2345aaaaa", caverphone.encode("a!b#c$d%e^"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Test 1: Same caverphone code
        Assert.assertTrue(caverphone.isCaverphoneEqual("abcde", "abcdef"));
        
        // Test 2: Different caverphone code
        Assert.assertFalse(caverphone.isCaverphoneEqual("abcde", "12345"));
    }
}