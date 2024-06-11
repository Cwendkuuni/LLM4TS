import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone_basic() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("hello");
        assertEquals("Expected caverphone code", "HL11111111", result);
    }

    @Test
    public void testEncode_ObjectException() {
        Caverphone caverphone = new Caverphone();
        try {
            caverphone.encode(new Object());
            fail("Expected EncoderException to be thrown");
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testEncode_String() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.encode("world");
        assertEquals("Expected encoded string", "WDL1111111", result);
    }

    @Test
    public void testIsCaverphoneEqual_equalStrings() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("hello", "hello");
        assertTrue("Expected caverphones to be equal", result);
    }

    @Test
    public void testIsCaverphoneEqual_differentStrings() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("hello", "world");
        assertFalse("Expected caverphones to be different", result);
    }

    @Test
    public void testCaverphone_nullInput() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone(null);
        assertEquals("Expected caverphone code for null input", "1111111111", result);
    }

    @Test
    public void testCaverphone_emptyInput() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("");
        assertEquals("Expected caverphone code for empty input", "1111111111", result);
    }

    @Test
    public void testCaverphone_edgeCases() {
        Caverphone caverphone = new Caverphone();
        String result1 = caverphone.caverphone("cough");
        assertEquals("Expected caverphone code for 'cough'", "KA21111111", result1);

        String result2 = caverphone.caverphone("xenophobe");
        assertEquals("Expected caverphone code for 'xenophobe'", "SNAFB31111", result2);
    }

    @Test
    public void testCaverphone_exceptionHandling() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("exception");
        assertEquals("Expected caverphone code for 'exception'", "EKSN311111", result);
    }

}