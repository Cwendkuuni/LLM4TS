import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("3khk", caverphone.caverphone("cough"));
        assertEquals("tghk", caverphone.caverphone("tough"));
        assertEquals("34zn", caverphone.caverphone("gnat"));
        assertEquals("smrklhn", caverphone.caverphone("smirklin"));
    }

    @Test
    public void testEncode() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("3khk", caverphone.encode("cough"));
        assertEquals("tghk", caverphone.encode("tough"));
        assertEquals("34zn", caverphone.encode("gnat"));
        assertEquals("smrklhn", caverphone.encode("smirklin"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertTrue(caverphone.isCaverphoneEqual("cough", "kof"));
        assertFalse(caverphone.isCaverphoneEqual("tough", "couf"));
        assertTrue(caverphone.isCaverphoneEqual("gnat", "gnit"));
        assertFalse(caverphone.isCaverphoneEqual("smirklin", "smirkling"));
    }

}