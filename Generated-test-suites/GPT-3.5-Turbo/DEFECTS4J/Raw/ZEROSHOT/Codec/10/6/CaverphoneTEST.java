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
    public void testCaverphone() {
        String input = "test";
        String expected = "TST1111111";
        String result = caverphone.caverphone(input);
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneWithEmptyString() {
        String input = "";
        String expected = "1111111111";
        String result = caverphone.caverphone(input);
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneWithNullString() {
        String input = null;
        String expected = "1111111111";
        String result = caverphone.caverphone(input);
        assertEquals(expected, result);
    }

    @Test
    public void testEncodeWithValidString() {
        String input = "test";
        String expected = "TST1111111";
        Object result = caverphone.encode(input);
        assertEquals(expected, result);
    }

    @Test(expected=EncoderException.class)
    public void testEncodeWithInvalidObject() throws EncoderException {
        Object input = new Object();
        caverphone.encode(input);
    }

    @Test
    public void testEncodeWithValidString_ReturnsString() {
        String input = "test";
        String expected = "TST1111111";
        String result = caverphone.encode(input);
        assertEquals(expected, result);
    }

    @Test
    public void testIsCaverphoneEqual() {
        String input1 = "test";
        String input2 = "tst";
        boolean expected = true;
        boolean result = caverphone.isCaverphoneEqual(input1, input2);
        assertEquals(expected, result);
    }
}