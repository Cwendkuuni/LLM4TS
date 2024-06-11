import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CaverphoneTest {

    private Caverphone caverphone;

    @Before
    public void setUp() {
        caverphone = new Caverphone();
    }

    @Test
    public void testCaverphone_basic() {
        String result = caverphone.caverphone("hello");
        assertEquals("Expected caverphone code not generated", "HL11111111", result);
    }

    @Test
    public void testCaverphone_edgeCases() {
        String result = caverphone.caverphone("1234");//edge case: input with numbers
        assertEquals("Expected caverphone code not generated", "1111111111", result);
    }

    @Test
    public void testEncode_success() {
        String result = (String) caverphone.encode("world");
        assertEquals("Expected encoded value not returned", "WDL1111111", result);
    }

    @Test(expected = EncoderException.class)
    public void testEncode_failure() {
        caverphone.encode(123);//edge case: non-string parameter
    }

    @Test
    public void testIsCaverphoneEqual_true() {
        boolean result = caverphone.isCaverphoneEqual("hello", "hallow");
        assertTrue("Expected caverphone codes to be equal", result);
    }

    @Test
    public void testIsCaverphoneEqual_false() {
        boolean result = caverphone.isCaverphoneEqual("hello", "world");
        assertFalse("Expected caverphone codes to be different", result);
    }

}