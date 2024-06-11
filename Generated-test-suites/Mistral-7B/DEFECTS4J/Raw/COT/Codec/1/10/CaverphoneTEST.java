package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit test cases for the Caverphone class
 */
public class CaverphoneTest {

    /**
     * Test instance creation
     */
    @Test
    public void testInstanceCreation() {
        Caverphone caverphone = new Caverphone();
        assertNotNull(caverphone);
    }

    /**
     * Test caverphone method with empty String
     */
    @Test
    public void testCaverphoneEmptyString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("");
        assertEquals("1111111111", result);
    }

    /**
     * Test caverphone method with null String
     */
    @Test(expected = EncoderException.class)
    public void testCaverphoneNullString() {
        Caverphone caverphone = new Caverphone();
        caverphone.caverphone(null);
    }

    /**
     * Test caverphone method with lowercase string
     */
    @Test
    public void testCaverphoneLowerCaseString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("hello");
        assertEquals("H2h2l2o2l2o", result);
    }

    /**
     * Test caverphone method with string containing only letters
     */
    @Test
    public void testCaverphoneOnlyLettersString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("TestString");
        assertEquals("T2t2s2t2s2t2r", result);
    }

    /**
     * Test caverphone method with string containing initial 'cough', 'rough' or 'tough'
     */
    @Test
    public void testCaverphoneInitialString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("CoughTest");
        assertEquals("Cou2fT2t2s2t2r", result);
        result = caverphone.caverphone("RoughTest");
        assertEquals("Rou2fT2t2s2t2r", result);
        result = caverphone.caverphone("ToughTest");
        assertEquals("Tou2fT2t2s2t2r", result);
    }

    /**
     * Test caverphone method with string containing initial 'enough'
     */
    @Test
    public void testCaverphoneInitialEnoughString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("EnoughTest");
        assertEquals("Enou2fT2t2s2t2r", result);
    }

    /**
     * Test caverphone method with string containing 'gn' and 'mb'
     */
    @Test
    public void testCaverphoneGnMbString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("StringWithGnMb");
        assertEquals("S2nM2", result);
    }

    /**
     * Test caverphone method with string containing 'cq' and 'ci'
     */
    @Test
    public void testCaverphoneCqCiString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("CqCiTest");
        assertEquals("2q2s2i", result);
    }

    /**
     * Test caverphone method with string containing 'ce' and 'cy'
     */
    @Test
    public void testCaverphoneCeCyString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("CeCyTest");
        assertEquals("SeSyTest", result);
    }

    /**
     * Test caverphone method with string containing 'tch'
     */
    @Test
    public void testCaverphoneTchString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("TchTest");
        assertEquals("2chTest", result);
    }

    /**
     * Test caverphone method with string containing 'c' and 'q'
     */
    @Test
    public void testCaverphoneCAndQString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("CqTest");
        assertEquals("kTest", result);
    }

    /**
     * Test caverphone method with string containing 'x'
     */
    @Test
    public void testCaverphoneXString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("TestX");
        assertEquals("kTest", result);
    }

    /**
     * Test caverphone method with string containing 'v'
     */
    @Test
    public void testCaverphoneVString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("TestV");
        assertEquals("fTest", result);
    }

    /**
     * Test caverphone method with string containing 'dg'
     */
    @Test
    public void testCaverphoneDgString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("TestDg");
        assertEquals("2gTest", result);
    }

    /**
     * Test caverphone method with string containing 'tio' and 'tia'
     */
    @Test
    public void testCaverphoneTioTiaString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("TioTiaTest");
        assertEquals("sio3", result);
    }

    /**
     * Test caverphone method with string containing 'd'
     */
    @Test
    public void testCaverphoneDString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("TestD");
        assertEquals("tTest", result);
    }

    /**
     * Test caverphone method with string containing 'ph'
     */
    @Test
    public void testCaverphonePhString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("TestPh");
        assertEquals("fhTest", result);
    }

    /**
     * Test caverphone method with string containing 'b'
     */
    @Test
    public void testCaverphoneBString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("TestB");
        assertEquals("pTest", result);
    }

    /**
     * Test caverphone method with string containing 'sh'
     */
    @Test
    public void testCaverphoneShString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("TestSh");
        assertEquals("s2Test", result);
    }

    /**
     * Test caverphone method with string containing 'z'
     */
    @Test
    public void testCaverphoneZString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("TestZ");
        assertEquals("sTest", result);
    }

    /**
     * Test caverphone method with string containing initial vowels
     */
    @Test
    public void testCaverphoneInitialVowelsString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("AeIoUTest");
        assertEquals("A1111111111", result);
    }

    /**
     * Test caverphone method with string containing only vowels
     */
    @Test
    public void testCaverphoneOnlyVowelsString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("TestAeIou");
        assertEquals("A1111111111", result);
    }

    /**
     * Test caverphone method with string containing 'y' and 'Y'
     */
    @Test
    public void testCaverphoneYAndYString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("TestYy");
        assertEquals("A1111111111", result);
    }

    /**
     * Test caverphone method with string containing '3gh3' and 'gh'
     */
    @Test
    public void testCaverphone3gh3AndGhString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("Test3gh3AndGh");
        assertEquals("3kh3", result);
    }

    /**
     * Test caverphone method with string containing 'g'
     */
    @Test
    public void testCaverphoneGString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("TestG");
        assertEquals("kTest", result);
    }

    /**
     * Test caverphone method with string containing 's+'
     */
    @Test
    public void testCaverphoneSString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("TestS");
        assertEquals("S", result);
    }

    /**
     * Test caverphone method with string containing 't+'
     */
    @Test
    public void testCaverphoneTString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("TestT");
        assertEquals("T", result);
    }

    /**
     * Test caverphone method with string containing 'p+'
     */
    @Test
    public void testCaverphonePString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("TestP");
        assertEquals("P", result);
    }

    /**
     * Test caverphone method with string containing 'k+'
     */
    @Test
    public void testCaverphoneKString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("TestK");
        assertEquals("K", result);
    }

    /**
     * Test caverphone method with string containing 'f+'
     */
    @Test
    public void testCaverphoneFString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("TestF");
        assertEquals("F", result);
    }

    /**
     * Test caverphone method with string containing 'm+'
     */
    @Test
    public void testCaverphoneMString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("TestM");
        assertEquals("M", result);
    }

    /**
     * Test caverphone method with string containing 'n+'
     */
    @Test
    public void testCaverphoneNString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("TestN");
        assertEquals("N", result);
    }

    /**
     * Test caverphone method with string containing 'W3'
     */
    @Test
    public void testCaverphoneW3String() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("TestW3");
        assertEquals("W3", result);
    }

    /**
     * Test caverphone method with string containing 'wh3'
     */
    @Test
    public void testCaverphoneWh3String() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("TestWh3");
        assertEquals("Wh3", result);
    }

    /**
     * Test caverphone method with string containing 'w$'
     */
    @Test(expected = EncoderException.class)
    public void testCaverphoneWSpeicalCharacterString() {
        Caverphone caverphone = new Caverphone();
        caverphone.caverphone("TestW$");
    }

    /**
     * Test caverphone method with string containing 'why'
     */
    @Test(expected = EncoderException.class)
    public void testCaverphoneWhyString() {
        Caverphone caverphone = new Caverphone();
        caverphone.caverphone("TestWhy");
    }

    /**
     * Test caverphone method with string containing 'wy'
     */
    @Test(expected = EncoderException.class)
    public void testCaverphoneWyString() {
        Caverphone caverphone = new Caverphone();
        caverphone.caverphone("TestWy");
    }
}