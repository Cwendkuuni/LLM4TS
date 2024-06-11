import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        Assert.assertEquals("1111111111", caverphone.caverphone(null));
        Assert.assertEquals("1111111111", caverphone.caverphone(""));
        Assert.assertEquals("caverphone", caverphone.caverphone("Caverphone"));
        Assert.assertEquals("kt12n", caverphone.caverphone("kitten"));
        Assert.assertEquals("dt2d3n", caverphone.caverphone("Cattedon"));
        Assert.assertEquals("sm3213l", caverphone.caverphone("Sumol"));
        Assert.assertEquals("ph1nd", caverphone.caverphone("Fined"));
        Assert.assertEquals("kb23rdg323ss33", caverphone.caverphone("caberdgssdgdfgdfgdfgdfgdfgdfgdfgdfgdfgsfgse"));
    }

    @Test
    public void testEncodeObject() {
        Caverphone caverphone = new Caverphone();
        Assert.assertEquals("kt12n", caverphone.encode("kitten"));
        Assert.assertThrows(EncoderException.class, () -> {
            caverphone.encode(new Object());
        });
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();
        Assert.assertEquals("kt12n", caverphone.encode("kitten"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        Assert.assertTrue(caverphone.isCaverphoneEqual("kitten", "kitt3n"));
        Assert.assertFalse(caverphone.isCaverphoneEqual("kitten", "puppy"));
    }
}