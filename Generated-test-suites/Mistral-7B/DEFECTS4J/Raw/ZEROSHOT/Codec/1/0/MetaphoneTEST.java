package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone_EmptyString() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("");
        assertNull(actual);
    }

    @Test
    public void testMetaphone_OneCharacter() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("A");
        assertEquals("A", actual);
    }

    @Test
    public void testMetaphone_SingleCharacterException() {
        Metaphone metaphone = new Metaphone();
        try {
            metaphone.metaphone(null);
            assert false;
        } catch (EncoderException e) {
            // expected
        }
    }

    @Test
    public void testMetaphone_MultipleCharacters() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("Apple");
        assertEquals("A0P0L0E", actual);
    }

    @Test
    public void testMetaphone_MultipleCharactersWithInitialExceptions() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("Knife");
        assertEquals("K1F", actual);
    }

    @Test
    public void testMetaphone_MultipleCharactersWithInitialVowelExceptions() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("Ape");
        assertEquals("A0P0E", actual);
    }

    @Test
    public void testMetaphone_MultipleCharactersWithInitialWhExceptions() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("Whale");
        assertEquals("W1H", actual);
    }

    @Test
    public void testMetaphone_MultipleCharactersWithInitialXExceptions() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("Xylophone");
        assertEquals("X0L0F0N", actual);
    }

    @Test
    public void testMetaphone_MultipleCharactersWithDoubleLetter() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("Bubble");
        assertEquals("B1B0L0E", actual);
    }

    @Test
    public void testMetaphone_MultipleCharactersWithSilentGExceptions() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("Bargain");
        assertEquals("B1RG", actual);
    }

    @Test
    public void testMetaphone_MultipleCharactersWithSilentGAndCExceptions() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("Bargainier");
        assertEquals("B1RG0N1R", actual);
    }

    @Test
    public void testMetaphone_MultipleCharactersWithInitialSAndInitialC() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("Scissors");
        assertEquals("S1S0R", actual);
    }

    @Test
    public void testMetaphone_MultipleCharactersWithChExceptions() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("Machine");
        assertEquals("M1S1N", actual);
    }

    @Test
    public void testMetaphone_MultipleCharactersWithDgExceptions() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("Budget");
        assertEquals("B1D0J", actual);
    }

    @Test
    public void testMetaphone_MultipleCharactersWithInitialH() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("Happy");
        assertEquals("H1P", actual);
    }

    @Test
    public void testMetaphone_MultipleCharactersWithInitialF() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("Fruit");
        assertEquals("F1R0T", actual);
    }

    @Test
    public void testMetaphone_MultipleCharactersWithInitialJ() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("Joyful");
        assertEquals("J1F0L", actual);
    }

    @Test
    public void testMetaphone_MultipleCharactersWithInitialQ() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("Queen");
        assertEquals("K1N", actual);
    }

    @Test
    public void testMetaphone_MultipleCharactersWithInitialS() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("Sunshine");
        assertEquals("S1S0N1H", actual);
    }

    @Test
    public void testMetaphone_MultipleCharactersWithInitialT() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("Thought");
        assertEquals("T0", actual);
    }

    @Test
    public void testMetaphone_MultipleCharactersWithInitialV() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("Vehicle");
        assertEquals("F1V0E0L", actual);
    }

    @Test
    public void testMetaphone_MultipleCharactersWithInitialW() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("Whisky");
        assertEquals("W1S1K", actual);
    }

    @Test
    public void testMetaphone_MultipleCharactersWithInitialY() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("Yellow");
        assertEquals("Y0L0", actual);
    }

    @Test
    public void testMetaphone_MultipleCharactersWithInitialZ() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("Zebra");
        assertEquals("S0", actual);
    }

    @Test
    public void testMetaphone_MultipleCharactersWithVowels() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("Elephant");
        assertEquals("E1L0P0H0N0T", actual);
    }

    @Test
    public void testMetaphone_MultipleCharactersWithConsonants() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("Rubber");
        assertEquals("R1B0BR", actual);
    }

    @Test
    public void testMetaphone_MultipleCharactersMixed() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("Banana");
        assertEquals("B1N1N0A", actual);
    }

    @Test
    public void testMetaphone_Encode() {
        Metaphone metaphone = new Metaphone();
        Object actual = metaphone.encode("Apple");
        assertEquals("A0P0L0E", actual);
    }

    @Test
    public void testMetaphone_EncodeNull() {
        Metaphone metaphone = new Metaphone();
        try {
            metaphone.encode(null);
            assert false;
        } catch (EncoderException e) {
            // expected
        }
    }

    @Test
    public void testMetaphone_EncodeNonString() {
        Metaphone metaphone = new Metaphone();
        Object actual = metaphone.encode(new Object());
        assertNull(actual);
    }

    @Test
    public void testMetaphone_IsMetaphoneEqual_SameString() {
        Metaphone metaphone = new Metaphone();
        assertTrue(metaphone.isMetaphoneEqual("Apple", "Apple"));
    }

    @Test
    public void testMetaphone_IsMetaphoneEqual_DifferentStrings() {
        Metaphone metaphone = new Metaphone();
        assertFalse(metaphone.isMetaphoneEqual("Apple", "Banana"));
    }

    @Test
    public void testMetaphone_IsMetaphoneEqual_NullString() {
        Metaphone metaphone = new Metaphone();
        String left = null;
        String right = "Apple";
        assertFalse(metaphone.isMetaphoneEqual(left, right));
    }

    @Test
    public void testMetaphone_IsMetaphoneEqual_EmptyString() {
        Metaphone metaphone = new Metaphone();
        String left = "";
        String right = "Apple";
        assertFalse(metaphone.isMetaphoneEqual(left, right));
    }

    @Test
    public void testMetaphone_MaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        int actual = metaphone.getMaxCodeLen();
        assertEquals(4, actual);
    }

    @Test
    public void testMetaphone_MaxCodeLenSetter() {
        Metaphone metaphone = new Metaphone();
        metaphone.setMaxCodeLen(5);
        int actual = metaphone.getMaxCodeLen();
        assertEquals(5, actual);
    }

}