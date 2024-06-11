import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.codec.language.Caverphone;

public class CaverphoneTest {

    private Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphone_emptyString() {
        String result = caverphone.caverphone("");
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphone_nullString() {
        String result = caverphone.caverphone(null);
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphone_lowercaseConversion() {
        String result = caverphone.caverphone("AbcDef");
        assertEquals("3bcdf11111", result);
    }

    @Test
    public void testCaverphone_removeNonAlphaCharacters() {
        String result = caverphone.caverphone("#Ab#cDe5$%^");
        assertEquals("abcdef11111", result);
    }

    @Test
    public void testCaverphone_removeFinalE() {
        String result = caverphone.caverphone("Abcde");
        assertEquals("3bcd111111", result);
    }

    @Test
    public void testCaverphone_handleStartOptions() {
        String result1 = caverphone.caverphone("coughing");
        assertEquals("cou2f3ng11", result1);

        String result2 = caverphone.caverphone("roughly");
        assertEquals("rou2f3y111", result1);

        String result3 = caverphone.caverphone("toughenough");
        assertEquals("tou2f3nough", result3);
    }

    @Test
    public void testCaverphone_handleReplacements() {
        String result = caverphone.caverphone("cq");
        assertEquals("2q11111111", result);
    }

    @Test
    public void testCaverphone_handleRemovals() {
        String result = caverphone.caverphone("2");
        assertEquals("1111111111", result);
    }

    @Test
    public void testEncode_ObjectOfString() throws EncoderException {
        Object result = caverphone.encode((Object) "abc123");
        assertEquals("a3c3", result);
    }

    @Test
    public void testEncode_StringInput() {
        String result = caverphone.encode("abc123");
        assertEquals("a3c3", result);
    }

    @Test
    public void testIsCaverphoneEqual_identicalStrings() {
        boolean result = caverphone.isCaverphoneEqual("abc", "abc");
        assertTrue(result);
    }

    @Test
    public void testIsCaverphoneEqual_differentStrings() {
        boolean result = caverphone.isCaverphoneEqual("abc", "def");
        assertFalse(result);
    }

}