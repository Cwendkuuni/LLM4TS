import static org.junit.Assert.*;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneBasic() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("test");
        assertEquals("test case for caverphone method", "1111111111", result);
    }

    @Test
    public void testCaverphoneEdgeCases() {
        Caverphone caverphone = new Caverphone();
        String emptyResult = caverphone.caverphone("");
        String nullResult = caverphone.caverphone(null);

        assertEquals("test case for empty input", "1111111111", emptyResult);
        assertEquals("test case for null input", "1111111111", nullResult);
    }

    @Test
    public void testEncodeObject() {
        Caverphone caverphone = new Caverphone();
        
        try {
            Object result = caverphone.encode("test");
            assertEquals("test case for encode method with String input", "1111111111", result);
        } catch (EncoderException e) {
            fail("EncoderException should not be thrown for String input");
        }

        try {
            Object result = caverphone.encode(new Object());
            fail("EncoderException should be thrown for non-String input");
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        boolean equalResult = caverphone.isCaverphoneEqual("test", "test");
        boolean notEqualResult = caverphone.isCaverphoneEqual("test", "hello");

        assertTrue("test case for equal caverphones", equalResult);
        assertFalse("test case for not equal caverphones", notEqualResult);
    }
}