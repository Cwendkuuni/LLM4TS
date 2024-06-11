import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.Caverphone;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEmptyString() {
        Caverphone caverphone = new Caverphone();
        String expected = "1111111111";
        String result = caverphone.caverphone("");
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneLowerCaseString() {
        Caverphone caverphone = new Caverphone();
        String input = "hello";
        String expected = "h3l2l2o3o";
        String result = caverphone.caverphone(input);
        assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneNullString() {
        Caverphone caverphone = new Caverphone();
        String input = null;
        caverphone.caverphone(input);
    }

    @Test
    public void testCaverphoneStartOptions() {
        Caverphone caverphone = new Caverphone();
        String input = "cough";
        String expected = "cou2f";
        String result = caverphone.caverphone(input);
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlingReplacements() {
        Caverphone caverphone = new Caverphone();
        String input = "cyanide";
        String expected = "s2i2an3d";
        String result = caverphone.caverphone(input);
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlingRemovals() {
        Caverphone caverphone = new Caverphone();
        String input = "two threes";
        String expected = "2";
        String result = caverphone.caverphone(input);
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlingEndOptions() {
        Caverphone caverphone = new Caverphone();
        String input = "rough";
        String expected = "rou2f";
        String result = caverphone.caverphone(input);
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlingFinalE() {
        Caverphone caverphone = new Caverphone();
        String input = "enough";
        String expected = "enou2f";
        String result = caverphone.caverphone(input);
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneEncodeObject() {
        Caverphone caverphone = new Caverphone();
        String input = "test";
        String expected = "t3st";
        String result = (String) caverphone.encode(input);
        assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneEncodeNonStringObject() {
        Caverphone caverphone = new Caverphone();
        Object input = new Object();
        caverphone.encode(input);
    }

    @Test
    public void testCaverphoneIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        String string1 = "hello";
        String string2 = "world";
        String caverphone1 = caverphone.caverphone(string1);
        String caverphone2 = caverphone.caverphone(string2);
        boolean result = caverphone.isCaverphoneEqual(string1, string2);
        assertFalse(result);
        result = caverphone.isCaverphoneEqual(string1, string1);
        assertTrue(result);
        result = caverphone.isCaverphoneEqual(caverphone1, caverphone1);
        assertTrue(result);
    }
}