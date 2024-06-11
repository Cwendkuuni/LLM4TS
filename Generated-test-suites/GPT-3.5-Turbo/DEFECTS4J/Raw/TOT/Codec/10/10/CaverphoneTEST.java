import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    private Caverphone caverphone;

    @Before
    public void setUp() {
        caverphone = new Caverphone();
    }

    @Test
    public void testCaverphone_emptyString_return1111111111() {
        String result = caverphone.caverphone("");
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphone_lowercaseString_returnCaverphoneCode() {
        String result = caverphone.caverphone("apache");
        assertEquals("APK1111111", result);
    }

    @Test
    public void testCaverphone_specialCharacters_returnCaverphoneCodeWithoutSpecialCharacters() {
        String result = caverphone.caverphone("#apache#$");
        assertEquals("APK1111111", result);
    }

    @Test
    public void testCaverphone_startOptions_returnCaverphoneCodeWithReplacements() {
        String result = caverphone.caverphone("cough");
        assertEquals("KO23", result);
    }

    @Test
    public void testCaverphone_finalE_removal() {
        String result = caverphone.caverphone("rocke");
        assertEquals("R23K", result);
    }

    @Test
    public void testCaverphone_replacements_returnCaverphoneCodeWithReplacements() {
        String result = caverphone.caverphone("cqci");
        assertEquals("CSK", result);
    }

    @Test
    public void testEncode_objectNotString_throwEncoderException() {
        try {
            caverphone.encode(123);
            fail("Expected EncoderException but no exception was thrown");
        } catch (EncoderException e) {
            assertEquals("Parameter supplied to Caverphone encode is not of type java.lang.String", e.getMessage());
        }
    }

    @Test
    public void testEncode_validString_returnCaverphoneCode() {
        String result = caverphone.encode("apache");
        assertEquals("APK1111111", result);
    }

    @Test
    public void testIsCaverphoneEqual_equalStrings_returnTrue() {
        boolean result = caverphone.isCaverphoneEqual("apache", "APk1111111");
        assertTrue(result);
    }

    @Test
    public void testIsCaverphoneEqual_notEqualStrings_returnFalse() {
        boolean result = caverphone.isCaverphoneEqual("apache1", "APk1111111");
        assertFalse(result);
    }
  
}