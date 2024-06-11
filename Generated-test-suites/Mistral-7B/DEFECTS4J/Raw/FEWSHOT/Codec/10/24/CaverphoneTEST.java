package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testEmptyString() {
        assertEquals("1111111111", new Caverphone().caverphone(""));
    }

    @Test
    public void testNullString() {
        assertNull(new Caverphone().caverphone(null));
    }

    @Test
    public void testLowerCase() {
        Caverphone caverphone = new Caverphone();
        assertEquals("oufoufoufoufoufoufoufoufou", caverphone.caverphone("fooBar"));
        assertEquals("FOUFOUFOUFOUFOUFOUFOUFOU", caverphone.caverphone("FOO BAR"));
    }

    @Test
    public void testRemoveNonAlphabets() {
        Caverphone caverphone = new Caverphone();
        assertEquals("oufoufoufoufoufoufoufoufou", caverphone.caverphone("fooBar123"));
        assertEquals("FOUFOUFOUFOUFOUFOUFOUFOU", caverphone.caverphone("FOO BAR 123"));
    }

    @Test
    public void testRemoveFinalE() {
        Caverphone caverphone = new Caverphone();
        assertEquals("oufoufoufoufoufoufoufoufo", caverphone.caverphone("fooBar"));
        assertEquals("FOUFOUFOUFOUFOUFOUFOUFO", caverphone.caverphone("FOO BAR"));
        assertEquals("oufooufoufoufoufoufoufou", caverphone.caverphone("fooBare"));
    }

    @Test
    public void testHandleStartOptions() {
        Caverphone caverphone = new Caverphone();
        assertEquals("cou2foufoufoufoufoufoufou", caverphone.caverphone("coughBar"));
        assertEquals("cou2frou2foufoufoufoufoufou", caverphone.caverphone("roughBar"));
        assertEquals("tou2foufoufoufoufoufoufou", caverphone.caverphone("toughBar"));
        assertEquals("enou2foufoufoufoufoufoufou", caverphone.caverphone("enoughBar"));
        assertEquals("trou2foufoufoufoufoufoufou", caverphone.caverphone("troughBar"));
        assertEquals("mn2noufoufoufoufoufoufou", caverphone.caverphone("gnBar"));
    }

    @Test
    public void testHandleReplacements() {
        Caverphone caverphone = new Caverphone();
        assertEquals("2qoufoufoufoufoufoufoufo", caverphone.caverphone("cqBar"));
        assertEquals("sisoufoufoufoufoufoufoufo", caverphone.caverphone("ciBar"));
        assertEquals("sesoufoufoufoufoufoufoufo", caverphone.caverphone("ceBar"));
        assertEquals("sysoufoufoufoufoufoufoufo", caverphone.caverphone("cyBar"));
        assertEquals("2choufoufoufoufoufoufoufo", caverphone.caverphone("tchBar"));
        assertEquals("kkoufoufoufoufoufoufoufo", caverphone.caverphone("cBar"));
        assertEquals("kkoufoufoufoufoufoufoufo", caverphone.caverphone("qBar"));
        assertEquals("kkoufoufoufoufoufoufoufo", caverphone.caverphone("xBar"));
        assertEquals("ffoufoufoufoufoufoufoufo", caverphone.caverphone("vBar"));
        assertEquals("2g2oufoufoufoufoufoufoufo", caverphone.caverphone("dgBar"));
        assertEquals("siosoufoufoufoufoufoufoufo", caverphone.caverphone("tioBar"));
        assertEquals("siasoufoufoufoufoufoufoufo", caverphone.caverphone("tiaBar"));
        assertEquals("ttoufoufoufoufoufoufoufo", caverphone.caverphone("dBar"));
        assertEquals("fhousoufoufoufoufoufoufoufo", caverphone.caverphone("phBar"));
        assertEquals("ppoufoufoufoufoufoufoufo", caverphone.caverphone("bBar"));
        assertEquals("s2oufoufoufoufoufoufoufo", caverphone.caverphone("shBar"));
        assertEquals("ssoufoufoufoufoufoufoufo", caverphone.caverphone("zBar"));
        assertEquals("AAAoufoufoufoufoufoufoufo", caverphone.caverphone("fooBar"));
        assertEquals("AAA3oufoufoufoufoufoufoufo", caverphone.caverphone("fooBar3"));
        assertEquals("3kh3oufoufoufoufoufoufoufo", caverphone.caverphone("3gh3Bar"));
    }

    @Test
    public void testHandleRemovals() {
        Caverphone caverphone = new Caverphone();
        assertEquals("oufoufoufoufoufoufoufoufo", caverphone.caverphone("2oufoufoufoufoufoufoufoufo"));
        assertEquals("oufoufoufoufoufoufoufo", caverphone.caverphone("3oufoufoufoufoufoufoufo"));
    }

    @Test
    public void testHandleRemovalsWithFinalThree() {
        Caverphone caverphone = new Caverphone();
        assertEquals("oufoufoufoufoufoufoufoA", caverphone.caverphone("3oufoufoufoufoufoufoufo3"));
    }

    @Test
    public void testHandleLeadingAndTrailingZeros() {
        Caverphone caverphone = new Caverphone();
        assertEquals("oufoufoufoufoufoufoufou1111111111", caverphone.caverphone("fooBar1111111111"));
        assertEquals("oufoufoufoufoufoufoufou1111", caverphone.caverphone("fooBar1111"));
    }

    @Test
    public void testHandleLeadingAndTrailingZerosWithTruncation() {
        Caverphone caverphone = new Caverphone();
        assertEquals("oufoufoufoufoufoufoufou", caverphone.caverphone("fooBar"));
    }

    @Test
    public void testEquals() {
        Caverphone caverphone = new Caverphone();
        String code1 = caverphone.caverphone("fooBar");
        String code2 = caverphone.caverphone("fooBar");
        assertTrue(caverphone.isCaverphoneEqual(code1, code2));
    }

    @Test
    public void testNotEquals() {
        Caverphone caverphone = new Caverphone();
        String code1 = caverphone.caverphone("fooBar");
        String code2 = caverphone.caverphone("fooBaz");
        assertFalse(caverphone.isCaverphoneEqual(code1, code2));
    }

}