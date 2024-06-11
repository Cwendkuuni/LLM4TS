package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Assert.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for the Metaphone class.
 */
public class MetaphoneTest {

    private static final Metaphone METAPHONE = new Metaphone();

    /**
     * Test the constructor.
     */
    @Test
    public void testMetaphoneConstructor() {
        Metaphone metaphone = new Metaphone();
        assertNotNull(metaphone);
    }

    /**
     * Test metaphone method with empty and null strings.
     */
    @Test
    public void testMetaphoneEmptyAndNullStrings() {
        assertEquals("", METAPHONE.metaphone(null));
        assertEquals("", METAPHONE.metaphone(""));
    }

    /**
     * Test metaphone method with single character strings.
     */
    @Test
    public void testMetaphoneSingleCharacterStrings() {
        assertEquals("A", METAPHONE.metaphone("A"));
        assertEquals("B", METAPHONE.metaphone("B"));
        // Test with uppercase letters
        assertEquals("C", METAPHONE.metaphone("C"));
        assertEquals("D", METAPHONE.metaphone("D"));
        assertEquals("E", METAPHONE.metaphone("E"));
        assertEquals("F", METAPHONE.metaphone("F"));
        assertEquals("G", METAPHONE.metaphone("G"));
        assertEquals("H", METAPHONE.metaphone("H"));
        assertEquals("I", METAPHONE.metaphone("I"));
        assertEquals("J", METAPHONE.metaphone("J"));
        assertEquals("K", METAPHONE.metaphone("K"));
        assertEquals("L", METAPHONE.metaphone("L"));
        assertEquals("M", METAPHONE.metaphone("M"));
        assertEquals("N", METAPHONE.metaphone("N"));
        assertEquals("O", METAPHONE.metaphone("O"));
        assertEquals("P", METAPHONE.metaphone("P"));
        assertEquals("Q", METAPHONE.metaphone("Q"));
        assertEquals("R", METAPHONE.metaphone("R"));
        assertEquals("S", METAPHONE.metaphone("S"));
        assertEquals("T", METAPHONE.metaphone("T"));
        assertEquals("U", METAPHONE.metaphone("U"));
        assertEquals("V", METAPHONE.metaphone("V"));
        assertEquals("W", METAPHONE.metaphone("W"));
        assertEquals("X", METAPHONE.metaphone("X"));
        assertEquals("Y", METAPHONE.metaphone("Y"));
        assertEquals("Z", METAPHONE.metaphone("Z"));
    }

    /**
     * Test metaphone method with various strings.
     */
    @Test
    public void testMetaphoneVariousStrings() {
        assertEquals("A0E", METAPHONE.metaphone("Apple"));
        assertEquals("A0P", METAPHONE.metaphone("Applepie"));
        assertEquals("B3P", METAPHONE.metaphone("Banana"));
        assertEquals("B3N", METAPHONE.metaphone("Bananaana"));
        assertEquals("C2T", METAPHONE.metaphone("Cat"));
        assertEquals("C2T", METAPHONE.metaphone("Cats"));
        assertEquals("C2S", METAPHONE.metaphone("Catsup"));
        assertEquals("D2G", METAPHONE.metaphone("Dog"));
        assertEquals("D2J", METAPHONE.metaphone("Dogwood"));
        assertEquals("E2T", METAPHONE.metaphone("Elephant"));
        assertEquals("E2H", METAPHONE.metaphone("Elephone"));
        assertEquals("F2P", METAPHONE.metaphone("Fireplace"));
        assertEquals("F2R", METAPHONE.metaphone("Firetruck"));
        assertEquals("G2J", METAPHONE.metaphone("Giraffe"));
        assertEquals("G2K", METAPHONE.metaphone("Giraffic"));
        assertEquals("H2T", METAPHONE.metaphone("Hippopotamus"));
        assertEquals("H2T", METAPHONE.metaphone("Hippopotamus"));
        assertEquals("I1G", METAPHONE.metaphone("Iguana"));
        assertEquals("I1N", METAPHONE.metaphone("Iguanana"));
        assertEquals("J2T", METAPHONE.metaphone("Jaguar"));
        assertEquals("J2T", METAPHONE.metaphone("Jaguarundi"));
        assertEquals("K2P", METAPHONE.metaphone("Kangaroo"));
        assertEquals("K2P", METAPHONE.metaphone("Kangaroopie"));
        assertEquals("L2T", METAPHONE.metaphone("Lion"));
        assertEquals("L2V", METAPHONE.metaphone("Lioness"));
        assertEquals("M2P", METAPHONE.metaphone("Marmot"));
        assertEquals("M2T", METAPHONE.metaphone("Marmott"));
        assertEquals("N2D", METAPHONE.metaphone("Nutmeg"));
        assertEquals("N2T", METAPHONE.metaphone("Nutmegtree"));
        assertEquals("O1F", METAPHONE.metaphone("Octopus"));
        assertEquals("O1F", METAPHONE.metaphone("Octopuses"));
        assertEquals("P2T", METAPHONE.metaphone("Penguin"));
        assertEquals("P2T", METAPHONE.metaphone("Penguins"));
        assertEquals("Q1K", METAPHONE.metaphone("Quokka"));
        assertEquals("Q1K", METAPHONE.metaphone("Quokkae"));
        assertEquals("R2S", METAPHONE.metaphone("Rhinoceros"));
        assertEquals("R2S", METAPHONE.metaphone("Rhinoceroses"));
        assertEquals("S2T", METAPHONE.metaphone("Snake"));
        assertEquals("S2T", METAPHONE.metaphone("Snakes"));
        assertEquals("T2P", METAPHONE.metaphone("Tiger"));
        assertEquals("T2P", METAPHONE.metaphone("Tigers"));
        assertEquals("U1S", METAPHONE.metaphone("Umbrella"));
        assertEquals("U1S", METAPHONE.metaphone("Umbrellas"));
        assertEquals("V2P", METAPHONE.metaphone("Vulture"));
        assertEquals("V2P", METAPHONE.metaphone("Vultures"));
        assertEquals("W1R", METAPHONE.metaphone("Walrus"));
        assertEquals("W1R", METAPHONE.metaphone("Walruses"));
        assertEquals("X1K", METAPHONE.metaphone("Xylophone"));
        assertEquals("X1K", METAPHONE.metaphone("Xylophones"));
        assertEquals("Y1P", METAPHONE.metaphone("Yak"));
        assertEquals("Y1P", METAPHONE.metaphone("Yaks"));
        assertEquals("Z2S", METAPHONE.metaphone("Zebra"));
        assertEquals("Z2S", METAPHONE.metaphone("Zebras"));
    }

    /**
     * Test encode method.
     */
    @Test
    public void testEncode() throws EncoderException {
        assertEquals("A0E", METAPHONE.encode("Apple"));
        assertEquals("A0P", METAPHONE.encode("Applepie"));
        assertEquals("B3P", METAPHONE.encode("Banana"));
        assertEquals("B3N", METAPHONE.encode("Bananaana"));
        assertEquals("C2T", METAPHONE.encode("Cat"));
        assertEquals("C2T", METAPHONE.encode("Cats"));
        assertEquals("C2S", METAPHONE.encode("Catsup"));
        assertEquals("D2G", METAPHONE.encode("Dog"));
        assertEquals("D2J", METAPHONE.encode("Dogwood"));
        assertEquals("E2T", METAPHONE.encode("Elephant"));
        assertEquals("E2H", METAPHONE.encode("Elephone"));
        assertEquals("F2P", METAPHONE.encode("Fireplace"));
        assertEquals("F2R", METAPHONE.encode("Firetruck"));
        assertEquals("G2J", METAPHONE.encode("Giraffe"));
        assertEquals("G2K", METAPHONE.encode("Giraffic"));
        assertEquals("H2T", METAPHONE.encode("Hippopotamus"));
        assertEquals("H2T", METAPHONE.encode("Hippopotamus"));
        assertEquals("I1G", METAPHONE.encode("Iguana"));
        assertEquals("I1N", METAPHONE.encode("Iguanana"));
        assertEquals("J2T", METAPHONE.encode("Jaguar"));
        assertEquals("J2T", METAPHONE.encode("Jaguarundi"));
        assertEquals("K2P", METAPHONE.encode("Kangaroo"));
        assertEquals("K2P", METAPHONE.encode("Kangaroopie"));
        assertEquals("L2T", METAPHONE.encode("Lion"));
        assertEquals("L2V", METAPHONE.encode("Lioness"));
        assertEquals("M2P", METAPHONE.encode("Marmot"));
        assertEquals("M2T", METAPHONE.encode("Marmott"));
        assertEquals("N2D", METAPHONE.encode("Nutmeg"));
        assertEquals("N2T", METAPHONE.encode("Nutmegtree"));
        assertEquals("O1F", METAPHONE.encode("Octopus"));
        assertEquals("O1F", METAPHONE.encode("Octopuses"));
        assertEquals("P2T", METAPHONE.encode("Penguin"));
        assertEquals("P2T", METAPHONE.encode("Penguins"));
        assertEquals("Q1K", METAPHONE.encode("Quokka"));
        assertEquals("Q1K", METAPHONE.encode("Quokkae"));
        assertEquals("R2S", METAPHONE.encode("Rhinoceros"));
        assertEquals("R2S", METAPHONE.encode("Rhinoceroses"));
        assertEquals("S2T", METAPHONE.encode("Snake"));
        assertEquals("S2T", METAPHONE.encode("Snakes"));
        assertEquals("T2P", METAPHONE.encode("Tiger"));
        assertEquals("T2P", METAPHONE.encode("Tigers"));
        assertEquals("U1S", METAPHONE.encode("Umbrella"));
        assertEquals("U1S", METAPHONE.encode("Umbrellas"));
        assertEquals("V2P", METAPHONE.encode("Vulture"));
        assertEquals("V2P", METAPHONE.encode("Vultures"));
        assertEquals("W1R", METAPHONE.encode("Walrus"));
        assertEquals("W1R", METAPHONE.encode("Walruses"));
        assertEquals("X1K", METAPHONE.encode("Xylophone"));
        assertEquals("X1K", METAPHONE.encode("Xylophones"));
        assertEquals("Y1P", METAPHONE.encode("Yak"));
        assertEquals("Y1P", METAPHONE.encode("Yaks"));
        assertEquals("Z2S", METAPHONE.encode("Zebra"));
        assertEquals("Z2S", METAPHONE.encode("Zebras"));
    }

    /**
     * Test isMetaphoneEqual method.
     */
    @Test
    public void testIsMetaphoneEqual() {
        assertTrue(MEAPHONE.isMetaphoneEqual("Apple", "Apple"));
        assertTrue(MEAPHONE.isMetaphoneEqual("Apple", "Applepie"));
        assertFalse(MEAPHONE.isMetaphoneEqual("Apple", "Orange"));
        assertFalse(MEAPHONE.isMetaphoneEqual(null, "Apple"));
        assertFalse(MEAPHONE.isMetaphoneEqual("Apple", null));
        assertFalse(MEAPHONE.isMetaphoneEqual("Apple", ""));
        assertFalse(MEAPHONE.isMetaphoneEqual("", "Apple"));
    }

    /**
     * Test getMaxCodeLen method.
     */
    @Test
    public void testGetMaxCodeLen() {
        assertEquals(4, METAPHONE.getMaxCodeLen());
    }

    /**
     * Test setMaxCodeLen method.
     */
    @Test
    public void testSetMaxCodeLen() {
        METAPHONE.setMaxCodeLen(5);
        assertEquals(5, METAPHONE.getMaxCodeLen());
    }
}