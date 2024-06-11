import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.codec.Caverphone;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEmptyString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("");
        String expected = "1111111111";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneSingleCharacter() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("a");
        String expected = "2";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneLowerCase() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("hello");
        String expected = "h2l2o2l2o";
        Assert.assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneNullString() {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(null);
    }
}