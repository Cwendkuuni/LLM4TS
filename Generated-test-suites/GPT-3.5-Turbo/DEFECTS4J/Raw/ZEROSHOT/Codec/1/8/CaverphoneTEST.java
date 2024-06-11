import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();

        // Test empty string
        Assert.assertEquals("1111111111", caverphone.caverphone(""));

        // Test lowercase string
        Assert.assertEquals("kpmf", caverphone.caverphone("kpmf"));

        // Test string with numbers and special characters
        Assert.assertEquals("kpmf", caverphone.caverphone("kpmf123#@!$"));

        // Test different start options
        Assert.assertEquals("cou2f", caverphone.caverphone("cough"));
        Assert.assertEquals("rou2f", caverphone.caverphone("rough"));
        Assert.assertEquals("tou2f", caverphone.caverphone("tough"));
        Assert.assertEquals("enou2f", caverphone.caverphone("enough"));
        Assert.assertEquals("trou2f", caverphone.caverphone("trough"));
        Assert.assertEquals("2n", caverphone.caverphone("gn"));
        Assert.assertEquals("m2", caverphone.caverphone("mb"));

        // Test replacements
        Assert.assertEquals("2q", caverphone.caverphone("cq"));
        Assert.assertEquals("si", caverphone.caverphone("ci"));
        Assert.assertEquals("se", caverphone.caverphone("ce"));
        Assert.assertEquals("sy", caverphone.caverphone("cy"));
        Assert.assertEquals("2ch", caverphone.caverphone("tch"));
        Assert.assertEquals("k", caverphone.caverphone("c"));
        Assert.assertEquals("k", caverphone.caverphone("q"));
        Assert.assertEquals("k", caverphone.caverphone("x"));
        Assert.assertEquals("f", caverphone.caverphone("v"));
        Assert.assertEquals("2g", caverphone.caverphone("dg"));
        Assert.assertEquals("sio", caverphone.caverphone("tio"));
        Assert.assertEquals("sia", caverphone.caverphone("tia"));
        Assert.assertEquals("t", caverphone.caverphone("d"));
        Assert.assertEquals("fh", caverphone.caverphone("ph"));
        Assert.assertEquals("p", caverphone.caverphone("b"));
        Assert.assertEquals("s2", caverphone.caverphone("sh"));
        Assert.assertEquals("s", caverphone.caverphone("z"));
        Assert.assertEquals("A", caverphone.caverphone("a"));
        Assert.assertEquals("3", caverphone.caverphone("e"));
        Assert.assertEquals("y", caverphone.caverphone("j"));
        Assert.assertEquals("Y3", caverphone.caverphone("y3"));
        Assert.assertEquals("A", caverphone.caverphone("y"));
        Assert.assertEquals("3", caverphone.caverphone("y"));
        Assert.assertEquals("3kh3", caverphone.caverphone("3gh3"));
        Assert.assertEquals("22", caverphone.caverphone("gh"));
        Assert.assertEquals("k", caverphone.caverphone("g"));
        Assert.assertEquals("S", caverphone.caverphone("ssss"));
        Assert.assertEquals("T", caverphone.caverphone("tttt"));
        Assert.assertEquals("P", caverphone.caverphone("ppp"));
        Assert.assertEquals("K", caverphone.caverphone("kkkk"));
        Assert.assertEquals("F", caverphone.caverphone("ffff"));
        Assert.assertEquals("M", caverphone.caverphone("mmmmm"));
        Assert.assertEquals("N", caverphone.caverphone("nnnn"));
        Assert.assertEquals("W3", caverphone.caverphone("w3"));
        Assert.assertEquals("Wh3", caverphone.caverphone("wh3"));
        Assert.assertEquals("3", caverphone.caverphone("w"));
        Assert.assertEquals("2", caverphone.caverphone("w"));
        Assert.assertEquals("A", caverphone.caverphone("h"));
        Assert.assertEquals("2", caverphone.caverphone("h"));
        Assert.assertEquals("R3", caverphone.caverphone("r3"));
        Assert.assertEquals("3", caverphone.caverphone("r"));
        Assert.assertEquals("2", caverphone.caverphone("r"));
        Assert.assertEquals("L3", caverphone.caverphone("l3"));
        Assert.assertEquals("3", caverphone.caverphone("l"));
        Assert.assertEquals("2", caverphone.caverphone("l"));

        // Test removals
        Assert.assertEquals("", caverphone.caverphone("2"));
        Assert.assertEquals("A", caverphone.caverphone("3$"));
        Assert.assertEquals("", caverphone.caverphone("3"));

        // Test length of output
        Assert.assertEquals(10, caverphone.caverphone("kpmf").length());
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();

        // Test empty string
        Assert.assertEquals("1111111111", caverphone.encode(""));

        // Test lowercase string
        Assert.assertEquals("kpmf", caverphone.encode("kpmf"));

        // Test string with numbers and special characters
        Assert.assertEquals("kpmf", caverphone.encode("kpmf123#@!$"));

        // Test encoding object
        Assert.assertEquals("kpmf", caverphone.encode((Object) "kpmf"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        // Test same strings
        Assert.assertTrue(caverphone.isCaverphoneEqual("kpmf", "kpmf"));

        // Test different strings
        Assert.assertFalse(caverphone.isCaverphoneEqual("kpmf", "abcd"));
    }
}