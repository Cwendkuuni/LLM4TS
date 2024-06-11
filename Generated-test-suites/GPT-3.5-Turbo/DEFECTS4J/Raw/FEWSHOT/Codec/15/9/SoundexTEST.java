import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SoundexTest {

    @Test
    public void testSoundex1() {
        String result = Soundex.soundex(null);
        assertNull(result);
    }

    @Test
    public void testSoundex2() {
        String result = Soundex.soundex("");
        assertEquals("", result);
    }

    @Test
    public void testSoundex3() {
        String result = Soundex.soundex("A");
        assertEquals("A000", result);
    }

    @Test
    public void testSoundex4() {
        String result = Soundex.soundex("APPLE");
        assertEquals("A140", result);
    }

    @Test
    public void testSoundex5() {
        String result = Soundex.soundex("BANANA");
        assertEquals("B550", result);
    }

    @Test
    public void testSoundex6() {
        String result = Soundex.soundex("CAT");
        assertEquals("C300", result);
    }

    @Test
    public void testSoundex7() {
        String result = Soundex.soundex("DOG");
        assertEquals("D200", result);
    }

    @Test
    public void testSoundex8() {
        String result = Soundex.soundex("ELEPHANT");
        assertEquals("E415", result);
    }

    @Test
    public void testSoundex9() {
        String result = Soundex.soundex("FOX");
        assertEquals("F200", result);
    }

    @Test
    public void testSoundex10() {
        String result = Soundex.soundex("GIRAFFE");
        assertEquals("G611", result);
    }

    @Test
    public void testSoundex11() {
        String result = Soundex.soundex("HORSE");
        assertEquals("H620", result);
    }

    @Test
    public void testSoundex12() {
        String result = Soundex.soundex("IGUANA");
        assertEquals("I250", result);
    }

    @Test
    public void testSoundex13() {
        String result = Soundex.soundex("JAGUAR");
        assertEquals("J260", result);
    }

    @Test
    public void testSoundex14() {
        String result = Soundex.soundex("KANGAROO");
        assertEquals("K526", result);
    }

    @Test
    public void testSoundex15() {
        String result = Soundex.soundex("LEOPARD");
        assertEquals("L163", result);
    }

    @Test
    public void testSoundex16() {
        String result = Soundex.soundex("MONKEY");
        assertEquals("M520", result);
    }

    @Test
    public void testSoundex17() {
        String result = Soundex.soundex("NEWT");
        assertEquals("N300", result);
    }

    @Test
    public void testSoundex18() {
        String result = Soundex.soundex("OCTOPUS");
        assertEquals("O322", result);
    }

    @Test
    public void testSoundex19() {
        String result = Soundex.soundex("PENGUIN");
        assertEquals("P525", result);
    }

    @Test
    public void testSoundex20() {
        String result = Soundex.soundex("QUOKKA");
        assertEquals("Q220", result);
    }

    @Test
    public void testSoundex21() {
        String result = Soundex.soundex("RHINOCEROS");
        assertEquals("R526", result);
    }

    @Test
    public void testSoundex22() {
        String result = Soundex.soundex("SEAL");
        assertEquals("S400", result);
    }

    @Test
    public void testSoundex23() {
        String result = Soundex.soundex("TURTLE");
        assertEquals("T643", result);
    }

    @Test
    public void testSoundex24() {
        String result = Soundex.soundex("UNICORN");
        assertEquals("U526", result);
    }

    @Test
    public void testSoundex25() {
        String result = Soundex.soundex("VULTURE");
        assertEquals("V436", result);
    }

    @Test
    public void testSoundex26() {
        String result = Soundex.soundex("WHALE");
        assertEquals("W440", result);
    }

    @Test
    public void testSoundex27() {
        String result = Soundex.soundex("XYLOPHONE");
        assertEquals("X415", result);
    }

    @Test
    public void testSoundex28() {
        String result = Soundex.soundex("YAK");
        assertEquals("Y200", result);
    }

    @Test
    public void testSoundex29() {
        String result = Soundex.soundex("ZEBRA");
        assertEquals("Z160", result);
    }
}