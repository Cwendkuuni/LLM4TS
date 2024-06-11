import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;

public class CaverphoneTest {

    private static Caverphone caverphone;

    @BeforeClass
    public static void setUp() {
        caverphone = new Caverphone();
    }

    @Test
    public void testCaverphone() {
        String txt = "Caverphone";
        String expected = "KFS1111111";
        String result = caverphone.caverphone(txt);
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneNullString() {
        String txt = null;
        String expected = "1111111111";
        String result = caverphone.caverphone(txt);
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneEmptyString() {
        String txt = "";
        String expected = "1111111111";
        String result = caverphone.caverphone(txt);
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneLowerCase() {
        String txt = "loWercaSe";
        String expected = "LWKS111111";
        String result = caverphone.caverphone(txt);
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneRemoveNonAZ() {
        String txt = "remo!ve@nonAz";
        String expected = "RMFNNS1111";
        String result = caverphone.caverphone(txt);
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneRemoveFinalE() {
        String txt = "removee";
        String expected = "RMV1111111";
        String result = caverphone.caverphone(txt);
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneStartOptions() {
        String txt = "tough";
        String expected = "T2F1111111";
        String result = caverphone.caverphone(txt);
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneReplacements() {
        String txt = "replace";
        String expected = "RPLSS111111";
        String result = caverphone.caverphone(txt);
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneRemovals() {
        String txt = "removal";
        String expected = "RML11111111";
        String result = caverphone.caverphone(txt);
        assertEquals(expected, result);
    }

    @Test
    public void testEncode() {
        String txt = "encode";
        String expected = "ANKD1111111";
        String result = caverphone.encode(txt);
        assertEquals(expected, result);
    }

    @Test
    public void testEncodeNullString() throws EncoderException {
        Object obj = null;
        String expected = "1111111111";
        String result = (String) caverphone.encode(obj);
        assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeNonStringObject() throws EncoderException {
        Object obj = new Object();
        caverphone.encode(obj);
    }

    @Test
    public void testIsCaverphoneEqual() {
        String str1 = "equal";
        String str2 = "eql";
        boolean expected = false;
        boolean result = caverphone.isCaverphoneEqual(str1, str2);
        assertEquals(expected, result);
    }

}