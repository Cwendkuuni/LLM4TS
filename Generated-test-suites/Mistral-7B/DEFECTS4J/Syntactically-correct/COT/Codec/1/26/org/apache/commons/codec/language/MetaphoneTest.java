package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphoneInstance() {
        Metaphone metaphone = new Metaphone();
        assertNotNull(metaphone);
    }

    @Test
    public void testMetaphoneNullInput() {
        Metaphone metaphone = new Metaphone();
        assertEquals("", metaphone.metaphone(null));
    }

    @Test
    public void testMetaphoneEmptyInput() {
        Metaphone metaphone = new Metaphone();
        assertEquals("", metaphone.metaphone(""));
    }

    @Test
    public void testMetaphoneSingleCharacter() {
        Metaphone metaphone = new Metaphone();
        assertEquals("A", metaphone.metaphone("A"));
    }

    @Test
    public void testMetaphoneTwoCharacters() {
        Metaphone metaphone = new Metaphone();
        assertEquals("BA", metaphone.metaphone("Banana"));
    }

    @Test
    public void testMetaphoneInitialKG() {
        Metaphone metaphone = new Metaphone();
        assertEquals("KN", metaphone.metaphone("Knife"));
        assertEquals("KN", metaphone.metaphone("Knight"));
    }

    @Test
    public void testMetaphoneInitialAE() {
        Metaphone metaphone = new Metaphone();
        assertEquals("AE", metaphone.metaphone("Apple"));
        assertEquals("AE", metaphone.metaphone("Apricot"));
    }

    @Test
    public void testMetaphoneInitialWR() {
        Metaphone metaphone = new Metaphone();
        assertEquals("R", metaphone.metaphone("Water"));
        assertEquals("W", metaphone.metaphone("Whiskey"));
    }

    @Test
    public void testMetaphoneInitialX() {
        Metaphone metaphone = new Metaphone();
        assertEquals("S", metaphone.metaphone("Xylophone"));
    }

    @Test
    public void testMetaphoneInitialKN() {
        Metaphone metaphone = new Metaphone();
        assertEquals("KN", metaphone.metaphone("Knowledge"));
        assertEquals("KN", metaphone.metaphone("Knuckle"));
    }

    @Test
    public void testMetaphoneInitialAEIA() {
        Metaphone metaphone = new Metaphone();
        assertEquals("AEIA", metaphone.metaphone("Applepie"));
    }

    @Test
    public void testMetaphoneInitialB() {
        Metaphone metaphone = new Metaphone();
        assertEquals("B", metaphone.metaphone("Bottle"));
    }

    @Test
    public void testMetaphoneInitialC() {
        Metaphone metaphone = new Metaphone();
        assertEquals("C", metaphone.metaphone("Cat"));
    }

    @Test
    public void testMetaphoneInitialDGE() {
        Metaphone metaphone = new Metaphone();
        assertEquals("J", metaphone.metaphone("Dog"));
    }

    @Test
    public void testMetaphoneInitialG() {
        Metaphone metaphone = new Metaphone();
        assertEquals("G", metaphone.metaphone("Giraffe"));
        assertEquals("K", metaphone.metaphone("Grapefruit"));
    }

    @Test
    public void testMetaphoneInitialH() {
        Metaphone metaphone = new Metaphone();
        assertEquals("H", metaphone.metaphone("Hat"));
    }

    @Test
    public void testMetaphoneInitialF() {
        Metaphone metaphone = new Metaphone();
        assertEquals("F", metaphone.metaphone("Fence"));
    }

    @Test
    public void testMetaphoneInitialJ() {
        Metaphone metaphone = new Metaphone();
        assertEquals("J", metaphone.metaphone("Jazz"));
    }

    @Test
    public void testMetaphoneInitialL() {
        Metaphone metaphone = new Metaphone();
        assertEquals("L", metaphone.metaphone("Lion"));
    }

    @Test
    public void testMetaphoneInitialM() {
        Metaphone metaphone = new Metaphone();
        assertEquals("M", metaphone.metaphone("Moon"));
    }

    @Test
    public void testMetaphoneInitialN() {
        Metaphone metaphone = new Metaphone();
        assertEquals("N", metaphone.metaphone("Nose"));
    }

    @Test
    public void testMetaphoneInitialR() {
        Metaphone metaphone = new Metaphone();
        assertEquals("R", metaphone.metaphone("Rose"));
    }

    @Test
    public void testMetaphoneInitialP() {
        Metaphone metaphone = new Metaphone();
        assertEquals("P", metaphone.metaphone("Pencil"));
        assertEquals("PH", metaphone.metaphone("Pineapple"));
    }

    @Test
    public void testMetaphoneInitialQ() {
        Metaphone metaphone = new Metaphone();
        assertEquals("K", metaphone.metaphone("Quiver"));
    }

    @Test
    public void testMetaphoneInitialS() {
        Metaphone metaphone = new Metaphone();
        assertEquals("S", metaphone.metaphone("Sun"));
        assertEquals("SH", metaphone.metaphone("Shirt"));
        assertEquals("X", metaphone.metaphone("Sea"));
    }

    @Test
    public void testMetaphoneInitialT() {
        Metaphone metaphone = new Metaphone();
        assertEquals("T", metaphone.metaphone("Tree"));
        assertEquals("X", metaphone.metaphone("Theatre"));
        assertEquals("0", metaphone.metaphone("Theta"));
    }

    @Test
    public void testMetaphoneInitialV() {
        Metaphone metaphone = new Metaphone();
        assertEquals("F", metaphone.metaphone("Vehicle"));
    }

    @Test
    public void testMetaphoneInitialW() {
        Metaphone metaphone = new Metaphone();
        assertEquals("W", metaphone.metaphone("Watermelon"));
        assertEquals("", metaphone.metaphone("Whisper"));
    }

    @Test
    public void testMetaphoneInitialX() {
        Metaphone metaphone = new Metaphone();
        assertEquals("KS", metaphone.metaphone("Xylophone"));
    }

    @Test
    public void testMetaphoneInitialZ() {
        Metaphone metaphone = new Metaphone();
        assertEquals("S", metaphone.metaphone("Zebra"));
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneInvalidInput() {
        Metaphone metaphone = new Metaphone();
        metaphone.metaphone("123"); // Input format is expected to be a single ASCII word with only characters in the A - Z range, no punctuation or numbers.
    }

    @Test
    public void testMetaphoneIdenticalStrings() {
        Metaphone metaphone = new Metaphone();
        String str1 = "Apple";
        String str2 = "Apple";
        assertEquals(metaphone.metaphone(str1), metaphone.metaphone(str2));
    }

    @Test
    public void testMetaphoneNonIdenticalStrings() {
        Metaphone metaphone = new Metaphone();
        String str1 = "Apple";
        String str2 = "Apricot";
        assertNotEquals(metaphone.metaphone(str1), metaphone.metaphone(str2));
    }

    @Test
    public void testMetaphoneMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        assertEquals(4, metaphone.getMaxCodeLen());
    }

    @Test
    public void testMetaphoneSetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        metaphone.setMaxCodeLen(5);
        assertEquals(5, metaphone.getMaxCodeLen());
    }
}