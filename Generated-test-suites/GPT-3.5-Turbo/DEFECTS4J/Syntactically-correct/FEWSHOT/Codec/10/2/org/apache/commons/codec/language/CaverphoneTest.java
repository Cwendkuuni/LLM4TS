package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CaverphoneTest {
    @Test
    public void testCaverphone() {
        Caverphone caverphoneObj = new Caverphone();

        assertEquals("1111111111", caverphoneObj.caverphone(null));
        assertEquals("1111111111", caverphoneObj.caverphone(""));

        assertEquals("kfa1111111", caverphoneObj.caverphone("kaufman"));
        assertEquals("smt22k1111", caverphoneObj.caverphone("smith"));
        assertEquals("tom1111111", caverphoneObj.caverphone("thomas"));
        assertEquals("kvelbrck111", caverphoneObj.caverphone("knevelsbrueck"));
        assertEquals("ysfsn+A1111", caverphoneObj.caverphone("juicyfruit"));
        assertEquals("crnp1111111", caverphoneObj.caverphone("cranapple"));

        assertEquals("bnbr1111111", caverphoneObj.caverphone("bonobo"));
        assertEquals("mkmbro11111", caverphoneObj.caverphone("monkeybrother"));
        assertEquals("chmchr11111", caverphoneObj.caverphone("chimichanga"));
        assertEquals("dng+j2rs111", caverphoneObj.caverphone("dingledores"));
        assertEquals("pyr11111111", caverphoneObj.caverphone("pyramid"));

        assertEquals("3111111111", caverphoneObj.caverphone("check"));
        assertEquals("T111111111", caverphoneObj.caverphone("too"));
        assertEquals("6111111111", caverphoneObj.caverphone("try"));
        assertEquals("T+1t111111", caverphoneObj.caverphone("teeth"));
        assertEquals("prsn1111111", caverphoneObj.caverphone("piercednose"));

        assertEquals("natan111111", caverphoneObj.caverphone("nathan"));
        assertEquals("rad+2k11111", caverphoneObj.caverphone("rodriguezko"));
        assertEquals("trS21111111", caverphoneObj.caverphone("transtech"));
        assertEquals("b3lFS211111", caverphoneObj.caverphone("beliefsystem"));
        assertEquals("ppl11111111", caverphoneObj.caverphone("people"));

        assertEquals("ph+2n111111", caverphoneObj.caverphone("phan"));
        assertEquals("StRO2221111", caverphoneObj.caverphone("stroll"));
        assertEquals("prty1111111", caverphoneObj.caverphone("party"));
        assertEquals("tBld1111111", caverphoneObj.caverphone("tumbleweed"));
        assertEquals("2822222222", caverphoneObj.caverphone("88"));

        assertEquals("S222222222", caverphoneObj.caverphone("1000"));
        assertEquals("1211111111", caverphoneObj.caverphone("2020"));
        assertEquals("1111111111", caverphoneObj.caverphone("00000"));
        assertEquals("W+22221111", caverphoneObj.caverphone("W42"));
        assertEquals("1111514342", caverphoneObj.caverphone("koffee"));

        assertEquals("1kty2222222", caverphoneObj.caverphone("akty"));

        assertEquals("4511111111", caverphoneObj.caverphone("flutterby"));
        assertEquals("Tm0m2222222", caverphoneObj.caverphone("tomato"));
        assertEquals("mSTA1111111", caverphoneObj.caverphone("masterpasta"));
    }

    @Test
    public void testEncode() {
        Caverphone caverphoneObj = new Caverphone();

        assertEquals("1111111111", caverphoneObj.encode(null));
        assertEquals("1111111111", caverphoneObj.encode(""));

        assertEquals("kfa1111111", caverphoneObj.encode("kaufman"));
        assertEquals("smt22k1111", caverphoneObj.encode("smith"));
        assertEquals("tom1111111", caverphoneObj.encode("thomas"));
        assertEquals("kvelbrck111", caverphoneObj.encode("knevelsbrueck"));
        assertEquals("ysfsn+A1111", caverphoneObj.encode("juicyfruit"));
        assertEquals("crnp1111111", caverphoneObj.encode("cranapple"));

        assertEquals("bnbr1111111", caverphoneObj.encode("bonobo"));
        assertEquals("mkmbro11111", caverphoneObj.encode("monkeybrother"));
        assertEquals("chmchr11111", caverphoneObj.encode("chimichanga"));
        assertEquals("dng+j2rs111", caverphoneObj.encode("dingledores"));
        assertEquals("pyr11111111", caverphoneObj.encode("pyramid"));

        assertEquals("3111111111", caverphoneObj.encode("check"));
        assertEquals("T111111111", caverphoneObj.encode("too"));
        assertEquals("6111111111", caverphoneObj.encode("try"));
        assertEquals("T+1t111111", caverphoneObj.encode("teeth"));
        assertEquals("prsn1111111", caverphoneObj.encode("piercednose"));

        assertEquals("natan111111", caverphoneObj.encode("nathan"));
        assertEquals("rad+2k11111", caverphoneObj.encode("rodriguezko"));
        assertEquals("trS21111111", caverphoneObj.encode("transtech"));
        assertEquals("b3lFS211111", caverphoneObj.encode("beliefsystem"));
        assertEquals("ppl11111111", caverphoneObj.encode("people"));

        assertEquals("ph+2n111111", caverphoneObj.encode("phan"));
        assertEquals("StRO2221111", caverphoneObj.encode("stroll"));
        assertEquals("prty1111111", caverphoneObj.encode("party"));
        assertEquals("tBld1111111", caverphoneObj.encode("tumbleweed"));
        assertEquals("2822222222", caverphoneObj.encode("88"));

        assertEquals("S222222222", caverphoneObj.encode("1000"));
        assertEquals("1211111111", caverphoneObj.encode("2020"));
        assertEquals("1111111111", caverphoneObj.encode("00000"));
        assertEquals("W+22221111", caverphoneObj.encode("W42"));
        assertEquals("1111514342", caverphoneObj.encode("koffee"));

        assertEquals("1kty2222222", caverphoneObj.encode("akty"));

        assertEquals("4511111111", caverphoneObj.encode("flutterby"));
        assertEquals("Tm0m2222222", caverphoneObj.encode("tomato"));
        assertEquals("mSTA1111111", caverphoneObj.encode("masterpasta"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphoneObj = new Caverphone();

        assertEquals(true, caverphoneObj.isCaverphoneEqual(null, null));
        assertEquals(false, caverphoneObj.isCaverphoneEqual("", null));
        assertEquals(false, caverphoneObj.isCaverphoneEqual(null, ""));
        assertEquals(true, caverphoneObj.isCaverphoneEqual("", ""));
        assertEquals(true, caverphoneObj.isCaverphoneEqual("kaufman", "kaufman"));
        assertEquals(false, caverphoneObj.isCaverphoneEqual("john", "joan"));
        assertEquals(false, caverphoneObj.isCaverphoneEqual("smith", "jones"));
    }
}