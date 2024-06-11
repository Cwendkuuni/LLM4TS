import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CaverphoneTest {

    private Caverphone caverphone;

    @Before
    public void setUp() {
        caverphone = new Caverphone();
    }

    @Test
    public void testCaverphone() {
        String txt1 = "cough";
        String txt2 = "rough";
        String txt3 = "tough";
        String txt4 = "enough";
        String txt5 = "trough";
        String txt6 = "gn";

        Assert.assertEquals("cou2f111111", caverphone.caverphone(txt1));
        Assert.assertEquals("rou2f111111", caverphone.caverphone(txt2));
        Assert.assertEquals("tou2f111111", caverphone.caverphone(txt3));
        Assert.assertEquals("enou2f111111", caverphone.caverphone(txt4));
        Assert.assertEquals("trou2f111111", caverphone.caverphone(txt5));
        Assert.assertEquals("2n11111111", caverphone.caverphone(txt6));
    }

    @Test
    public void testEncode() {
        String txt = "test";
        Assert.assertEquals("1111111111", caverphone.encode(txt));
    }

    @Test
    public void testIsCaverphoneEqual() {
        String txt1 = "hello";
        String txt2 = "world";

        Assert.assertFalse(caverphone.isCaverphoneEqual(txt1, txt2));
    }
}