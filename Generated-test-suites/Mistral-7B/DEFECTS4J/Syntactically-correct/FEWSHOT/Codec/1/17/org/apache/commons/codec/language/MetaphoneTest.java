package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class MetaphoneTest {
    private static final Metaphone METAPHONE = new Metaphone();

    @Test
    public void testMetaphoneEmptyString() {
        assertEquals("", METAPHONE.metaphone(""));
    }

    @Test
    public void testMetaphoneSingleCharacter() {
        assertEquals("A", METAPHONE.metaphone("A"));
        assertEquals("B", METAPHONE.metaphone("B"));
        // Test edge cases
        assertEquals("0", METAPHONE.metaphone("TH"));
        assertEquals("X", METAPHONE.metaphone("CH"));
        assertEquals("S", METAPHONE.metaphone("SH"));
        assertEquals("X", METAPHONE.metaphone("SIA"));
        assertEquals("X", METAPHONE.metaphone("SIO"));
    }

    @Test
    public void testMetaphoneDoubleVowels() {
        assertEquals("AE", METAPHONE.metaphone("AIE"));
        assertEquals("AI", METAPHONE.metaphone("AIE"));
    }

    @Test
    public void testMetaphoneInitialDoubleLetter() {
        assertEquals("KN", METAPHONE.metaphone("KNIGHT"));
        assertEquals("AA", METAPHONE.metaphone("APPLE"));
        assertEquals("WR", METAPHONE.metaphone("WRATH"));
        assertEquals("WH", METAPHONE.metaphone("WHALE"));
        assertEquals("XS", METAPHONE.metaphone("SQUASH"));
    }

    @Test
    public void testMetaphoneDoubleConsontant() {
        assertEquals("CT", METAPHONE.metaphone("COAT"));
        assertEquals("PT", METAPHONE.metaphone("POT"));
        assertEquals("PR", METAPHONE.metaphone("PROPER"));
        assertEquals("TK", METAPHONE.metaphone("TACK"));
        assertEquals("TS", METAPHONE.metaphone("TOES"));
        assertEquals("SK", METAPHONE.metaphone("SACK"));
    }

    @Test
    public void testMetaphoneOneVowelOneConsonant() {
        assertEquals("A", METAPHONE.metaphone("APPLE"));
        assertEquals("A", METAPHONE.metaphone("BAPPLE"));
        assertEquals("B", METAPHONE.metaphone("BANANA"));
        assertEquals("B", METAPHONE.metaphone("ABANANA"));
        assertEquals("E", METAPHONE.metaphone("EGG"));
        assertEquals("E", METAPHONE.metaphone("AEGG"));
        assertEquals("I", METAPHONE.metaphone("ICE"));
        assertEquals("I", METAPHONE.metaphone("AICE"));
        assertEquals("O", METAPHONE.metaphone("OAT"));
        assertEquals("O", METAPHONE.metaphone("AOAT"));
        assertEquals("U", METAPHONE.metaphone("UME"));
        assertEquals("U", METAPHONE.metaphone("AUME"));
    }

    @Test
    public void testMetaphoneSilentG() {
        // Test G at end of word
        assertEquals("K", METAPHONE.metaphone("DOG"));
        assertEquals("J", METAPHONE.metaphone("LOG"));
        assertEquals("J", METAPHONE.metaphone("BLOG"));
        assertEquals("J", METAPHONE.metaphone("FLOG"));
        // Test G before consonant
        assertEquals("J", METAPHONE.metaphone("DAG"));
        assertEquals("J", METAPHONE.metaphone("NAG"));
        assertEquals("J", METAPHONE.metaphone("GANG"));
        assertEquals("J", METAPHONE.metaphone("CANG"));
        assertEquals("J", METAPHONE.metaphone("BANG"));
        assertEquals("J", METAPHONE.metaphone("SANG"));
        // Test G before vowel
        assertEquals("J", METAPHONE.metaphone("DAGUE"));
        assertEquals("J", METAPHONE.metaphone("NAGUE"));
        assertEquals("J", METAPHONE.metaphone("GANGE"));
        assertEquals("J", METAPHONE.metaphone("CANGE"));
        assertEquals("J", METAPHONE.metaphone("BANGE"));
        assertEquals("J", METAPHONE.metaphone("SANGE"));
    }

    @Test
    public void testMetaphoneCh() {
        assertEquals("X", METAPHONE.metaphone("CHILD"));
        assertEquals("K", METAPHONE.metaphone("CHILDREN"));
        assertEquals("H", METAPHONE.metaphone("CHAIR"));
        assertEquals("H", METAPHONE.metaphone("CHAIRS"));
        assertEquals("X", METAPHONE.metaphone("CHOICE"));
        assertEquals("X", METAPHONE.metaphone("CHOOSE"));
        assertEquals("X", METAPHONE.metaphone("CHAMPION"));
        assertEquals("X", METAPHONE.metaphone("CHAMPIONS"));
        assertEquals("X", METAPHONE.metaphone("CHATTER"));
        assertEquals("X", METAPHONE.metaphone("CHATTERS"));
        assertEquals("X", METAPHONE.metaphone("CHAOS"));
        assertEquals("X", METAPHONE.metaphone("CHAOS"));
    }

    @Test
    public void testMetaphoneDg() {
        assertEquals("J", METAPHONE.metaphone("DAGGER"));
        assertEquals("T", METAPHONE.metaphone("DAGGERS"));
        assertEquals("J", METAPHONE.metaphone("BEDGE"));
        assertEquals("T", METAPHONE.metaphone("BEDGES"));
        assertEquals("J", METAPHONE.metaphone("FOGED"));
        assertEquals("T", METAPHONE.metaphone("FOGEDS"));
        assertEquals("J", METAPHONE.metaphone("LOGED"));
        assertEquals("T", METAPHONE.metaphone("LOGEDS"));
    }

    @Test
    public void testMetaphoneQ() {
        assertEquals("K", METAPHONE.metaphone("QUEEN"));
        assertEquals("K", METAPHONE.metaphone("QUICK"));
        assertEquals("K", METAPHONE.metaphone("QUICKER"));
        assertEquals("K", METAPHONE.metaphone("QUICKLY"));
        assertEquals("K", METAPHONE.metaphone("QUIET"));
        assertEquals("K", METAPHONE.metaphone("QUIETER"));
        assertEquals("K", METAPHONE.metaphone("QUOTE"));
        assertEquals("K", METAPHONE.metaphone("QUOTED"));
    }

    @Test
    public void testMetaphoneS() {
        assertEquals("S", METAPHONE.metaphone("SHELL"));
        assertEquals("S", METAPHONE.metaphone("SHELLS"));
        assertEquals("S", METAPHONE.metaphone("SHINE"));
        assertEquals("S", METAPHONE.metaphone("SHINES"));
        assertEquals("X", METAPHONE.metaphone("SHIPS"));
        assertEquals("X", METAPHONE.metaphone("SHOES"));
        assertEquals("X", METAPHONE.metaphone("SHORT"));
        assertEquals("X", METAPHONE.metaphone("SHOCK"));
        assertEquals("X", METAPHONE.metaphone("SCHOOL"));
        assertEquals("X", METAPHONE.metaphone("SCHOOLS"));
    }

    @Test
    public void testMetaphoneT() {
        assertEquals("T", METAPHONE.metaphone("TOES"));
        assertEquals("T", METAPHONE.metaphone("TOE"));
        assertEquals("T", METAPHONE.metaphone("THIS"));
        assertEquals("T", METAPHONE.metaphone("THAT"));
        assertEquals("T", METAPHONE.metaphone("THISIS"));
        assertEquals("T", METAPHONE.metaphone("THATTHAT"));
        assertEquals("T", METAPHONE.metaphone("THE"));
        assertEquals("T", METAPHONE.metaphone("THEM"));
        assertEquals("T", METAPHONE.metaphone("THEMSELVES"));
        assertEquals("T", METAPHONE.metaphone("THEIR"));
        assertEquals("T", METAPHONE.metaphone("THEIRS"));
    }

    @Test
    public void testMetaphoneV() {
        assertEquals("F", METAPHONE.metaphone("VANILLA"));
        assertEquals("F", METAPHONE.metaphone("VANILLAH"));
        assertEquals("F", METAPHONE.metaphone("VINE"));
        assertEquals("F", METAPHONE.metaphone("VINES"));
        assertEquals("F", METAPHONE.metaphone("VEGETABLE"));
        assertEquals("F", METAPHONE.metaphone("VEGETABLES"));
        assertEquals("F", METAPHONE.metaphone("VALUE"));
        assertEquals("F", METAPHONE.metaphone("VALUES"));
    }

    @Test
    public void testMetaphoneW() {
        assertEquals("W", METAPHONE.metaphone("WALL"));
        assertEquals("W", METAPHONE.metaphone("WALLS"));
        assertEquals("W", METAPHONE.metaphone("WATER"));
        assertEquals("W", METAPHONE.metaphone("WATERS"));
        assertEquals("W", METAPHONE.metaphone("WAS"));
        assertEquals("W", METAPHONE.metaphone("WASN'T"));
        assertEquals("W", METAPHONE.metaphone("WASN'T IT"));
        assertEquals("W", METAPHONE.metaphone("WASN'T IT SO"));
        assertEquals("W", METAPHONE.metaphone("WASN'T IT SO BEAUTIFUL"));
    }

    @Test
    public void testMetaphoneY() {
        assertEquals("F", METAPHONE.metaphone("YELLOW"));
        assertEquals("F", METAPHONE.metaphone("YELLOWS"));
        assertEquals("F", METAPHONE.metaphone("YOUNG"));
        assertEquals("F", METAPHONE.metaphone("YOUNGER"));
        assertEquals("F", METAPHONE.metaphone("YOUNGSTER"));
    }

    @Test
    public void testMetaphoneEquals() {
        assertEquals(METAPHONE.metaphone("APPLE"), METAPHONE.metaphone("APPLE"));
        assertEquals(METAPHONE.metaphone("APPLE"), METAPHONE.metaphone("APEL"));
        assertEquals(METAPHONE.metaphone("CHILD"), METAPHONE.metaphone("CHILDE"));
        assertEquals(METAPHONE.metaphone("CHILD"), METAPHONE.metaphone("CHILDS"));
        assertEquals(METAPHONE.metaphone("DOG"), METAPHONE.metaphone("DOGS"));
        assertEquals(METAPHONE.metaphone("DOG"), METAPHONE.metaphone("DOGE"));
        assertEquals(METAPHONE.metaphone("DAGGER"), METAPHONE.metaphone("DAGGERS"));
        assertEquals(METAPHONE.metaphone("QUEEN"), METAPHONE.metaphone("QUEENS"));
        assertEquals(METAPHONE.metaphone("QUICK"), METAPHONE.metaphone("QUICKER"));
        assertEquals(METAPHONE.metaphone("QUICKLY"), METAPHONE.metaphone("QUICKERLY"));
        assertEquals(METAPHONE.metaphone("CHOICE"), METAPHONE.metaphone("CHOOSE"));
        assertEquals(METAPHONE.metaphone("SCHOOL"), METAPHONE.metaphone("SCHOOLS"));
        assertEquals(METAPHONE.metaphone("SHIPS"), METAPHONE.metaphone("SHOES"));
        assertEquals(METAPHONE.metaphone("SHORT"), METAPHONE.metaphone("SHOCK"));
        assertEquals(METAPHONE.metaphone("TOES"), METAPHONE.metaphone("TOE"));
        assertEquals(METAPHONE.metaphone("THIS"), METAPHONE.metaphone("THAT"));
        assertEquals(METAPHONE.metaphone("THE"), METAPHONE.metaphone("THEM"));
        assertEquals(METAPHONE.metaphone("THEMSELVES"), METAPHONE.metaphone("THEIR"));
        assertEquals(METAPHONE.metaphone("THEIRS"), METAPHONE.metaphone("THEM"));
        assertEquals(METAPHONE.metaphone("VEGETABLE"), METAPHONE.metaphone("VEGETABLES"));
        assertEquals(METAPHONE.metaphone("VALUE"), METAPHONE.metaphone("VALUES"));
        assertEquals(METAPHONE.metaphone("WALL"), METAPHONE.metaphone("WALLS"));
        assertEquals(METAPHONE.metaphone("WATER"), METAPHONE.metaphone("WATERS"));
        assertEquals(METAPHONE.metaphone("WAS"), METAPHONE.metaphone("WASN'T"));
        assertEquals(METAPHONE.metaphone("WASN'T IT"), METAPHONE.metaphone("WASN'T IT SO"));
        assertEquals(METAPHONE.metaphone("WASN'T IT SO BEAUTIFUL"), METAPHONE.metaphone("WASN'T IT SO BEAUTIFUL"));
    }

    @Test
    public void testMetaphoneMaxCodeLen() {
        assertEquals("F0X", METAPHONE.metaphone("VANILLAFOB"));
        assertEquals("F0X", METAPHONE.metaphone("VANILLAFOBS"));
        assertEquals("F0X", METAPHONE.metaphone("VANILLAFOBSTER"));
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneInvalidInput() {
        METAPHONE.metaphone(null);
        METAPHONE.metaphone("");
        METAPHONE.metaphone("123");
    }
}