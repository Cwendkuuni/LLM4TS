import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Metaphone;
import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphone() throws DecoderException {
        Metaphone metaphone = new Metaphone();

        // Test empty string
        assertEquals("", metaphone.metaphone(""));

        // Test single character
        assertEquals("A", metaphone.metaphone("A"));
        assertEquals("B", metaphone.metaphone("B"));

        // Test basic words
        assertEquals("A000", metaphone.metaphone("APPLE"));
        assertEquals("A000", metaphone.metaphone("APPLES"));
        assertEquals("A010", metaphone.metaphone("BANANA"));
        assertEquals("A010", metaphone.metaphone("BANANAS"));
        assertEquals("B222", metaphone.metaphone("CAT"));
        assertEquals("C322", metaphone.metaphone("DOG"));
        assertEquals("D300", metaphone.metaphone("EGG"));
        assertEquals("E111", metaphone.metaphone("FOOT"));
        assertEquals("F111", metaphone.metaphone("GOAT"));
        assertEquals("G111", metaphone.metaphone("HELL"));
        assertEquals("H101", metaphone.metaphone("HOUSE"));
        assertEquals("H222", metaphone.metaphone("INTERNET"));
        assertEquals("I111", metaphone.metaphone("JAM"));
        assertEquals("J111", metaphone.metaphone("KANGAROO"));
        assertEquals("K111", metaphone.metaphone("LION"));
        assertEquals("L111", metaphone.metaphone("MONKEY"));
        assertEquals("M111", metaphone.metaphone("NEW"));
        assertEquals("N111", metaphone.metaphone("OCEAN"));
        assertEquals("O111", metaphone.metaphone("PIZZA"));
        assertEquals("P111", metaphone.metaphone("QUICK"));
        assertEquals("Q111", metaphone.metaphone("RABBIT"));
        assertEquals("R111", metaphone.metaphone("SHEEP"));
        assertEquals("S111", metaphone.metaphone("TREE"));
        assertEquals("T111", metaphone.metaphone("UNIVERSITY"));
        assertEquals("U111", metaphone.metaphone("VANILLA"));
        assertEquals("V111", metaphone.metaphone("WATER"));
        assertEquals("W111", metaphone.metaphone("XMAS"));
        assertEquals("X111", metaphone.metaphone("YOU"));
        assertEquals("Y111", metaphone.metaphone("ZEBRA"));
        assertEquals("Z111", metaphone.metaphone("APPLESAUCE"));

        // Test exceptions
        try {
            metaphone.metaphone(null);
            fail("IllegalArgumentException expected for null input");
        } catch (IllegalArgumentException e) {
        }

        try {
            metaphone.metaphone("");
            fail("IllegalArgumentException expected for empty input");
        } catch (IllegalArgumentException e) {
        }

        try {
            metaphone.metaphone("123");
            fail("IllegalArgumentException expected for non-String input");
        } catch (IllegalArgumentException e) {
        }

        try {
            metaphone.metaphone("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
            fail("DecoderException expected for input containing only letters");
        } catch (DecoderException e) {
        }

        try {
            metaphone.metaphone("ABC");
            fail("DecoderException expected for input shorter than 2 characters");
        } catch (DecoderException e) {
        }
    }
}