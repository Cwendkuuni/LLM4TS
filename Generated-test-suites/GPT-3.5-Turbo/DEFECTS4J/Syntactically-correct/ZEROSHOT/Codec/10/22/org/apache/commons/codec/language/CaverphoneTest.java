import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();

        // Test cases with null or empty input
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        
        // Test cases with valid input
        assertEquals("kma1111111", caverphone.caverphone("kamau"));
        assertEquals("wka1111111", caverphone.caverphone("wakaba"));

        // Test cases with special start options
        assertEquals("cou2fkma111", caverphone.caverphone("coughkamau"));
        assertEquals("rou2fwka111", caverphone.caverphone("roughwakaba"));
        assertEquals("tou2fkma111", caverphone.caverphone("toughkamau"));
        assertEquals("enou2fkma111", caverphone.caverphone("enoughkamau"));
        assertEquals("trou2fkma111", caverphone.caverphone("troughkamau"));
        assertEquals("2nkma1111", caverphone.caverphone("gnkamau"));

        // Test cases with end option
        assertEquals("m2kma11111", caverphone.caverphone("mbkamau"));

        // Test cases with replacements
        assertEquals("2qma111111", caverphone.caverphone("cqmau"));
        assertEquals("sima111111", caverphone.caverphone("cima"));
        assertEquals("sema111111", caverphone.caverphone("cema"));
        assertEquals("syma111111", caverphone.caverphone("cyma"));
        assertEquals("2chma11111", caverphone.caverphone("tchmau"));
        assertEquals("kma1111111", caverphone.caverphone("c"));
        assertEquals("kma1111111", caverphone.caverphone("q"));
        assertEquals("kma1111111", caverphone.caverphone("x"));
        assertEquals("fma1111111", caverphone.caverphone("v"));
        assertEquals("2gma111111", caverphone.caverphone("dgmau"));
        assertEquals("sio1111111", caverphone.caverphone("tioma"));
        assertEquals("sia1111111", caverphone.caverphone("tioma"));
        assertEquals("tma1111111", caverphone.caverphone("d"));
        assertEquals("fhma111111", caverphone.caverphone("phmau"));
        assertEquals("pma1111111", caverphone.caverphone("b"));
        assertEquals("s2ma111111", caverphone.caverphone("shma"));
        assertEquals("sma1111111", caverphone.caverphone("z"));
        assertEquals("Am111111111", caverphone.caverphone("a"));
        assertEquals("3m111111111", caverphone.caverphone("e"));

        // Test cases with removals
        assertEquals("ma111111111", caverphone.caverphone("2"));
        assertEquals("Ama11111111", caverphone.caverphone("3"));
        assertEquals("kama1111111", caverphone.caverphone("kkkimau"));

        // Test cases with additional characters
        assertEquals("33mKmS2DFW3", caverphone.caverphone("aeioustctrpbqklsxvgzjyhwfrelnmwy"));
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        // Test cases with valid input
        assertEquals("kma1111111", caverphone.encode("kamau"));
        assertEquals("wka1111111", caverphone.encode("wakaba"));

        // Test case with invalid input
        // EncoderException is expected
        try {
            caverphone.encode(1234);
            fail("Expected EncoderException not thrown");
        } catch (EncoderException e) {
            // expected
        }
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        // Test cases with identical caverphones
        assertTrue(caverphone.isCaverphoneEqual("kamau", "kama"));
        assertFalse(caverphone.isCaverphoneEqual("kamau", "wakaba"));
    }
}