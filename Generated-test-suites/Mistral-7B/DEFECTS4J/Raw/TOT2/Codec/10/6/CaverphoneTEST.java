import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Caverphone;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEmptyString() {
        Caverphone caverphone = new Caverphone();
        String actual = caverphone.caverphone("");
        String expected = "1111111111";
        assertEquals(expected, actual);
    }

    @Test
    public void testCaverphoneLowerCaseLetters() {
        Caverphone caverphone = new Caverphone();
        String actual = caverphone.caverphone("hello");
        String expected = "h3l3o2";
        assertEquals(expected, actual);
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneNullString() {
        Caverphone caverphone = new Caverphone();
        caverphone.caverphone(null);
    }

    @Test
    public void testCaverphoneHandlesStartOptions() {
        Caverphone caverphone = new Caverphone();
        String actual = caverphone.caverphone("tough");
        String expected = "tou2f";
        assertEquals(expected, actual);
    }

    @Test
    public void testCaverphoneHandlesReplacements() {
        Caverphone caverphone = new Caverphone();
        String actual = caverphone.caverphone("cq");
        String expected = "2q";
        assertEquals(expected, actual);
    }

    @Test
    public void testCaverphoneHandlesRemovals() {
        Caverphone caverphone = new Caverphone();
        String actual = caverphone.caverphone("2");
        String expected = "";
        assertEquals(expected, actual);
    }

    @Test
    public void testCaverphoneHandlesCombinationOfRules() {
        Caverphone caverphone = new Caverphone();
        String actual = caverphone.caverphone("toughcqc");
        String expected = "tou2fqk";
        assertEquals(expected, actual);
    }

    @Test
    public void testCaverphoneHandlesEdgeCases() {
        Caverphone caverphone = new Caverphone();
        String actual = caverphone.caverphone("AaEiIoUaEiIoU");
        String expected = "A3A33";
        assertEquals(expected, actual);
    }

    @Test
    public void testCaverphoneHandlesEdgeCasesWithJ() {
        Caverphone caverphone = new Caverphone();
        String actual = caverphone.caverphone("CafeJingle");
        String expected = "C2fE2j3l2i3n2g3l2e";
        assertEquals(expected, actual);
    }

    // Add more test cases if necessary
}