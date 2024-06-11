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
    public void testCaverphone_emptyString() {
        String result = caverphone.caverphone("");
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphone_lowercase() {
        String result = caverphone.caverphone("AbC");
        assertEquals("A3", result);
    }

    @Test
    public void testCaverphone_removeNonAlpha() {
        String result = caverphone.caverphone("a1b2c3");
        assertEquals("A3", result);
    }

    @Test
    public void testCaverphone_removeFinalE() {
        String result = caverphone.caverphone("cave");
        assertEquals("C3V", result);
    }

    @Test
    public void testCaverphone_handleStartOptions() {
        String result1 = caverphone.caverphone("coughabc");
        String result2 = caverphone.caverphone("roughabc");
        String result3 = caverphone.caverphone("toughabc");
        String result4 = caverphone.caverphone("enoughabc");
        String result5 = caverphone.caverphone("troughabc");
        assertEquals("C2FABC", result1);
        assertEquals("R2FABC", result2);
        assertEquals("T2FABC", result3);
        assertEquals("ENOU2FABC", result4);
        assertEquals("TROU2FABC", result5);
    }

    @Test
    public void testCaverphone_handleReplacements() {
        String result1 = caverphone.caverphone("cqabc");
        String result2 = caverphone.caverphone("ciabc");
        String result3 = caverphone.caverphone("ceabc");
        String result4 = caverphone.caverphone("cyabc");
        String result5 = caverphone.caverphone("tchabc");
        String result6 = caverphone.caverphone("cabc");
        String result7 = caverphone.caverphone("qabc");
        String result8 = caverphone.caverphone("xabc");
        String result9 = caverphone.caverphone("vabc");
        String result10 = caverphone.caverphone("dgabc");
        String result11 = caverphone.caverphone("tioabc");
        String result12 = caverphone.caverphone("tiaabc");
        String result13 = caverphone.caverphone("dabc");
        String result14 = caverphone.caverphone("phabc");
        String result15 = caverphone.caverphone("babc");
        String result16 = caverphone.caverphone("shabc");
        String result17 = caverphone.caverphone("zabc");
        String result18 = caverphone.caverphone("aabc");
        String result19 = caverphone.caverphone("eabc");
        String result20 = caverphone.caverphone("iabc");
        String result21 = caverphone.caverphone("oabc");
        String result22 = caverphone.caverphone("uabc");
        String result23 = caverphone.caverphone("jabc");
        String result24 = caverphone.caverphone("y3abc");
        String result25 = caverphone.caverphone("yabc");
        String result26 = caverphone.caverphone("3gh3abc");
        String result27 = caverphone.caverphone("ghabc");
        String result28 = caverphone.caverphone("gabc");
        String result29 = caverphone.caverphone("sabc");
        String result30 = caverphone.caverphone("tabc");
        String result31 = caverphone.caverphone("pabc");
        String result32 = caverphone.caverphone("kabc");
        String result33 = caverphone.caverphone("fabc");
        String result34 = caverphone.caverphone("mabc");
        String result35 = caverphone.caverphone("nabc");
        String result36 = caverphone.caverphone("w3abc");
        String result37 = caverphone.caverphone("wh3abc");
        String result38 = caverphone.caverphone("wabc");
        String result39 = caverphone.caverphone("habc");
        String result40 = caverphone.caverphone("r3abc");
        String result41 = caverphone.caverphone("rabc");
        String result42 = caverphone.caverphone("l3abc");
        String result43 = caverphone.caverphone("labc");

        assertEquals("2QABC", result1);
        assertEquals("SIABC", result2);
        assertEquals("SEABC", result3);
        assertEquals("SYABC", result4);
        assertEquals("2CHABC", result5);
        assertEquals("KABC", result6);
        assertEquals("KABC", result7);
        assertEquals("KABC", result8);
        assertEquals("FABC", result9);
        assertEquals("2GABC", result10);
        assertEquals("SIOABC", result11);
        assertEquals("SIAABC", result12);
        assertEquals("TABC", result13);
        assertEquals("FHABC", result14);
        assertEquals("PABC", result15);
        assertEquals("S2ABC", result16);
        assertEquals("SABC", result17);
        assertEquals("AABC", result18);
        assertEquals("3ABC", result19);
        assertEquals("IABC", result20);
        assertEquals("OABC", result21);
        assertEquals("UABC", result22);
        assertEquals("YABC", result23);
        assertEquals("Y3ABC", result24);
        assertEquals("AABC", result25);
        assertEquals("3KH3ABC", result26);
        assertEquals("22ABC", result27);
        assertEquals("KABC", result28);
        assertEquals("SABC", result29);
        assertEquals("TABC", result30);
        assertEquals("PABC", result31);
        assertEquals("KABC", result32);
        assertEquals("FABC", result33);
        assertEquals("MABC", result34);
        assertEquals("NABC", result35);
        assertEquals("W3ABC", result36);
        assertEquals("WH3ABC", result37);
        assertEquals("2ABC", result38);
        assertEquals("AABC", result39);
        assertEquals("R3ABC", result40);
        assertEquals("2ABC", result41);
        assertEquals("L3ABC", result42);
        assertEquals("2ABC", result43);
    }

    @Test
    public void testCaverphone_handleRemovals() {
        String result1 = caverphone.caverphone("2");
        String result2 = caverphone.caverphone("3");
        String result3 = caverphone.caverphone("ABC3");
        assertEquals("", result1);
        assertEquals("", result2);
        assertEquals("A", result3);
    }

    @Test
    public void testEncode_objectType() throws EncoderException {
        Object obj = "abc";
        Object result = caverphone.encode(obj);
        assertEquals("A3", result);
    }

    @Test(expected = EncoderException.class)
    public void testEncode_invalidType() throws EncoderException {
        Object obj = 123;
        caverphone.encode(obj);
    }

    @Test
    public void testEncode_stringType() {
        String result = caverphone.encode("abc");
        assertEquals("A3", result);
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

    @Test
    public void testIsCaverphoneEqual_sameCaverphones() {
        boolean result = caverphone.isCaverphoneEqual("abc", "a2c");
        assertTrue(result);
    }

    @Test
    public void testIsCaverphoneEqual_differentCaverphones() {
        boolean result = caverphone.isCaverphoneEqual("abc", "def");
        assertFalse(result);
    }

}