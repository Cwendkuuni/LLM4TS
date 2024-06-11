import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CaverphoneTest {
    private Caverphone caverphone;

    @Before
    public void setup() {
        caverphone = new Caverphone();
    }
    
    @Test
    public void testCaverphone() {
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("1111111111", caverphone.caverphone(" "));
        assertEquals("S11111111", caverphone.caverphone("test"));
        assertEquals("S1111112T2", caverphone.caverphone("testing"));
        assertEquals("FAQARQ3", caverphone.caverphone("framework"));
        assertEquals("2F2R", caverphone.caverphone("free"));
        assertEquals("2H2RFA", caverphone.caverphone("hovercraft"));
        assertEquals("AWAW424244", caverphone.caverphone("around"));
    }
    
    @Test(expected=EncoderException.class)
    public void testEncode() throws EncoderException {
        Object obj = new Object();
        caverphone.encode(obj);
    }
    
    @Test
    public void testEncodeString() {
        assertEquals("1111111111", caverphone.encode((String) null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("1111111111", caverphone.encode(" "));
        assertEquals("S11111111", caverphone.encode("test"));
        assertEquals("S1111112T2", caverphone.encode("testing"));
        assertEquals("FAQARQ3", caverphone.encode("framework"));
        assertEquals("2F2R", caverphone.encode("free"));
        assertEquals("2H2RFA", caverphone.encode("hovercraft"));
        assertEquals("AWAW424244", caverphone.encode("around"));
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        assertEquals(true, caverphone.isCaverphoneEqual(null, null));
        assertEquals(false, caverphone.isCaverphoneEqual("", null));
        assertEquals(true, caverphone.isCaverphoneEqual("test", "test"));
        assertEquals(false, caverphone.isCaverphoneEqual("testing", "test"));
        assertEquals(true, caverphone.isCaverphoneEqual("FREE", "fRAeE"));
        assertEquals(false, caverphone.isCaverphoneEqual("faqs", "frames"));
    }
}