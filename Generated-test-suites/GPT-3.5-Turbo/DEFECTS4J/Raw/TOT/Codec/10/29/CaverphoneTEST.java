import org.junit.Test;
import org.junit.Assert;

public class CaverphoneTest {

    @Test
    public void testCaverphone_emptyString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("");
        Assert.assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphone_lowercase() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("TeStInG");
        Assert.assertEquals("TSTN111111", result);
    }

    @Test
    public void testCaverphone_specialCharacters() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("T@e!#s$t%^i&n*g");
        Assert.assertEquals("TSN1111111", result);
    }

    @Test
    public void testCaverphone_startOptions() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("coughTesting");
        Assert.assertEquals("2FTESTN111", result);
    }

    @Test
    public void testCaverphone_replacements() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("replacements");
        Assert.assertEquals("RPLCMENTS1", result);
    }

    @Test
    public void testEncode_String() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.encode("Testing");
        Assert.assertEquals("TSTN111111", result);
    }

    @Test
    public void testEncode_Object() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        Object object = "Testing";
        String result = (String) caverphone.encode(object);
        Assert.assertEquals("TSTN111111", result);
    }

    @Test
    public void testIsCaverphoneEqual_equalStrings() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("Testing", "Testing");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsCaverphoneEqual_notEqualStrings() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("Testing", "Java");
        Assert.assertFalse(result);
    }

    @Test
    public void testIsCaverphoneEqual_emptyStrings() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("", "");
        Assert.assertTrue(result);
    }

}