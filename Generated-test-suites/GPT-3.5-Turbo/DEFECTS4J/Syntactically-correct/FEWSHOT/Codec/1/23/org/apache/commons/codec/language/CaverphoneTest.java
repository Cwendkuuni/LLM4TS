import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CaverphoneTest {
    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));

        assertEquals("1111111111", caverphone.caverphone("Upton"));
        assertEquals("1N11111111", caverphone.caverphone("Alison"));
        assertEquals("2321111111", caverphone.caverphone("Phoebe"));
        assertEquals("PHY1BY", caverphone.caverphone("Fiona"));
        assertEquals("###########################", caverphone.caverphone("abcdefghijklmnopqrstuvwxyz"));

        //Test with special characters and numbers
        assertEquals("T111111111", caverphone.caverphone("6eph!nt"));
        assertEquals("S111111111", caverphone.caverphone("@nimals"));
        assertEquals("D111111111", caverphone.caverphone("$og"));
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));

        assertEquals("1111111111", caverphone.encode("Upton"));
        assertEquals("1N11111111", caverphone.encode("Alison"));
        assertEquals("2321111111", caverphone.encode("Phoebe"));
        assertEquals("PHY1BY", caverphone.encode("Fiona"));
        assertEquals("###########################", caverphone.encode("abcdefghijklmnopqrstuvwxyz"));

        //Test with special characters and numbers
        assertEquals("T111111111", caverphone.encode("6eph!nt"));
        assertEquals("S111111111", caverphone.encode("@nimals"));
        assertEquals("D111111111", caverphone.encode("$og"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals(true, caverphone.isCaverphoneEqual("", ""));
        assertEquals(true, caverphone.isCaverphoneEqual(null, null));
        assertEquals(true, caverphone.isCaverphoneEqual("", null));

        assertEquals(false, caverphone.isCaverphoneEqual("Upton", "Alison"));
        assertEquals(true, caverphone.isCaverphoneEqual("Alison", "Al1s0n"));
        assertEquals(false, caverphone.isCaverphoneEqual("Phoebe", "PHY1BY"));
        assertEquals(true, caverphone.isCaverphoneEqual("abcdefghijklmnopqrstuvwxyz", "abcdefghijklmnopqrstuvwxy1"));

        //Test with special characters and numbers
        assertEquals(false, caverphone.isCaverphoneEqual("6eph!nt", "$z9r"));
        assertEquals(true, caverphone.isCaverphoneEqual("@nimals", "@n!m@1$"));
        assertEquals(true, caverphone.isCaverphoneEqual("$og", "Dollarog"));
    }
}