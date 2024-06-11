import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SoundexTest {
    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        // Test cases from the original code
        assertEquals("S530", soundex.soundex("Smith"));
        assertEquals("T522", soundex.soundex("Taylor"));
        assertEquals("A261", soundex.soundex("Andrew"));
        assertEquals("R163", soundex.soundex("Robert"));
        
        // Additional test cases
        assertEquals("A261", soundex.soundex("ANDREW"));
        assertEquals("A000", soundex.soundex("A"));
        assertEquals("B626", soundex.soundex("Bob"));
        assertEquals("D000", soundex.soundex("Dog"));
        assertEquals("G635", soundex.soundex("George"));
        assertEquals("H000", soundex.soundex("Horse"));
        assertEquals("K460", soundex.soundex("Karl"));
        assertEquals("L555", soundex.soundex("Liam"));
        assertEquals("M235", soundex.soundex("Matthew"));
        assertEquals("P652", soundex.soundex("Paul"));
        assertEquals("T200", soundex.soundex("Tom"));
        assertEquals("W243", soundex.soundex("William"));
        assertEquals("Y225", soundex.soundex("Yvonne"));
    }
}