import org.junit.Test;
import org.apache.commons.codec.EncoderException;

import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone_emptyString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(""));
    }

    @Test
    public void testCaverphone_lowercase() {
        Caverphone caverphone = new Caverphone();
        assertEquals("gtf", caverphone.caverphone("GTF"));
    }

    @Test
    public void testCaverphone_specialCharacters() {
        Caverphone caverphone = new Caverphone();
        assertEquals("ndvd", caverphone.caverphone("nd!@#$%^&*()vd"));
    }

    @Test
    public void testCaverphone_startOptions() {
        Caverphone caverphone = new Caverphone();
        assertEquals("cou2f", caverphone.caverphone("coughabc"));
        assertEquals("rou2f", caverphone.caverphone("roughabc"));
        assertEquals("tou2f", caverphone.caverphone("toughabc"));
        assertEquals("enou2f", caverphone.caverphone("enoughabc"));
        assertEquals("trou2f", caverphone.caverphone("troughabc"));
        assertEquals("2nabc", caverphone.caverphone("gnabc"));
        assertEquals("m2abc", caverphone.caverphone("mbabc"));
    }

    @Test
    public void testCaverphone_replacements() {
        Caverphone caverphone = new Caverphone();
        assertEquals("2qabc", caverphone.caverphone("cqabc"));
        assertEquals("siabc", caverphone.caverphone("ciabc"));
        assertEquals("seabc", caverphone.caverphone("ceabc"));
        assertEquals("syabc", caverphone.caverphone("cyabc"));
        assertEquals("2chabc", caverphone.caverphone("tchabc"));
        assertEquals("kabc", caverphone.caverphone("cabc"));
        assertEquals("kabc", caverphone.caverphone("qabc"));
        assertEquals("kabc", caverphone.caverphone("xabc"));
        assertEquals("fabc", caverphone.caverphone("vabc"));
        assertEquals("2gabc", caverphone.caverphone("dgabc"));
        assertEquals("sioabc", caverphone.caverphone("tioabc"));
        assertEquals("siaabc", caverphone.caverphone("tiaabc"));
        assertEquals("tabc", caverphone.caverphone("dabc"));
        assertEquals("fhabc", caverphone.caverphone("phabc"));
        assertEquals("pabc", caverphone.caverphone("babc"));
        assertEquals("s2abc", caverphone.caverphone("shabc"));
        assertEquals("sabc", caverphone.caverphone("zabc"));
        assertEquals("Aabc", caverphone.caverphone("aabc"));
        assertEquals("3bcd", caverphone.caverphone("ebcd"));
        assertEquals("y", caverphone.caverphone("j"));                       // 2.0 only
        assertEquals("A", caverphone.caverphone("y3"));                      // 2.0 only
        assertEquals("A", caverphone.caverphone("y"));                       // 2.0 only
        assertEquals("3", caverphone.caverphone("y"));                       // 2.0 only
        assertEquals("3kh3", caverphone.caverphone("3gh3"));                 // 2.0 only
        assertEquals("22th2", caverphone.caverphone("ghth"));                // 2.0 only
        assertEquals("k", caverphone.caverphone("g"));                       // 2.0 only
        assertEquals("S", caverphone.caverphone("ssss"));                    // 2.0 only
        assertEquals("T", caverphone.caverphone("tttt"));                    // 2.0 only
        assertEquals("P", caverphone.caverphone("pppp"));                    // 2.0 only
        assertEquals("K", caverphone.caverphone("kkkk"));                    // 2.0 only
        assertEquals("F", caverphone.caverphone("ffff"));                    // 2.0 only
        assertEquals("M", caverphone.caverphone("mmmm"));                    // 2.0 only
        assertEquals("N", caverphone.caverphone("nnnn"));                    // 2.0 only
        assertEquals("W3", caverphone.caverphone("w3"));                     // 2.0 only
        assertEquals("3", caverphone.caverphone("w"));                       // 2.0 only
        assertEquals("2", caverphone.caverphone("w"));                       // 2.0 only
        assertEquals("A", caverphone.caverphone("h"));                       // 2.0 only
        assertEquals("2", caverphone.caverphone("h"));                       // 2.0 only
        assertEquals("R3", caverphone.caverphone("r3"));                     // 2.0 only
        assertEquals("3", caverphone.caverphone("r"));                       // 2.0 only
        assertEquals("2", caverphone.caverphone("r"));                       // 2.0 only
        assertEquals("L3", caverphone.caverphone("l3"));                     // 2.0 only
        assertEquals("3", caverphone.caverphone("l"));                       // 2.0 only
        assertEquals("2", caverphone.caverphone("l"));                       // 2.0 only
    }

    @Test
    public void testCaverphone_removals() {
        Caverphone caverphone = new Caverphone();
        assertEquals("", caverphone.caverphone("2"));
        assertEquals("A", caverphone.caverphone("3"));
        assertEquals("A", caverphone.caverphone("3a"));
    }

    @Test
    public void testEncode_Object_notString() {
        Caverphone caverphone = new Caverphone();
        try {
            caverphone.encode(123);
            fail("EncoderException should be thrown");
        } catch (EncoderException e) {
            assertEquals("Parameter supplied to Caverphone encode is not of type java.lang.String", e.getMessage());
        }
    }

    @Test
    public void testEncode_String() {
        Caverphone caverphone = new Caverphone();
        assertEquals("ndvd", caverphone.encode("nd!@#$%^&*()vd"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertTrue(caverphone.isCaverphoneEqual("abc", "abc"));
        assertFalse(caverphone.isCaverphoneEqual("abc", "def"));
    }

}