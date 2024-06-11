package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for Metaphone class.
 */
public class MetaphoneTest {

    private Metaphone metaphone;

    @Before
    public void setUp() {
        metaphone = new Metaphone();
    }

    @Test
    public void testEmptyString() {
        Assert.assertEquals("", metaphone.metaphone(""));
    }

    @Test
    public void testSingleCharacterString() {
        for (char c = 'A'; c <= 'Z'; c++) {
            Assert.assertEquals(Character.toString(c), metaphone.metaphone(Character.toString(c)));
        }
    }

    @Test
    public void testBasicStrings() {
        Assert.assertEquals("A1", metaphone.metaphone("Apple"));
        Assert.assertEquals("A1", metaphone.metaphone("Apricot"));
        Assert.assertEquals("A003", metaphone.metaphone("Metaphone"));
        Assert.assertEquals("B3", metaphone.metaphone("Banana"));
        Assert.assertEquals("B3", metaphone.metaphone("Bunny"));
        Assert.assertEquals("C3", metaphone.metaphone("Cat"));
        Assert.assertEquals("C3", metaphone.metaphone("Can"));
        Assert.assertEquals("D2", metaphone.metaphone("Dog"));
        Assert.assertEquals("E1", metaphone.metaphone("Elephant"));
        Assert.assertEquals("G000", metaphone.metaphone("Giraffe"));
        Assert.assertEquals("H0", metaphone.metaphone("Hippopotamus"));
        Assert.assertEquals("I1", metaphone.metaphone("Igloo"));
        Assert.assertEquals("J1", metaphone.metaphone("Jumbo"));
        Assert.assertEquals("K1", metaphone.metaphone("Kangaroo"));
        Assert.assertEquals("N2", metaphone.metaphone("November"));
        Assert.assertEquals("O1", metaphone.metaphone("Orange"));
        Assert.assertEquals("P1", metaphone.metaphone("Pineapple"));
        Assert.assertEquals("Q000", metaphone.metaphone("Quincy"));
        Assert.assertEquals("R1", metaphone.metaphone("Raspberry"));
        Assert.assertEquals("S1", metaphone.metaphone("Shark"));
        Assert.assertEquals("T1", metaphonge.metaphone("Tomato"));
        Assert.assertEquals("V1", metaphone.metaphone("Violet"));
        Assert.assertEquals("X1", metaphone.metaphone("Xylophone"));
        Assert.assertEquals("Z1", metaphone.metaphone("Zebra"));
    }

    @Test(expected = EncoderException.class)
    public void testNullInput() {
        metaphone.metaphone(null);
    }

    @Test(expected = EncoderException.class)
    public void testEmptyInputArray() {
        metaphone.metaphone(new String[0]);
    }

    @Test
    public void testMultipleWords() {
        Assert.assertEquals("A1B3", metaphone.metaphone("Apple Banana"));
        Assert.assertEquals("A1C3D2", metaphone.metaphone("Apple Cat Dog"));
    }

    @Test
    public void testEdgeCases() {
        Assert.assertEquals("A002", metaphone.metaphone("Aardvark"));
        Assert.assertEquals("A001", metaphone.metaphone("Aluminium"));
        Assert.assertEquals("A010", metaphone.metaphone("Echidna"));
        Assert.assertEquals("A004", metaphone.metaphone("Metaphonetic"));
        Assert.assertEquals("B003", metaphone.metaphone("Bamboozle"));
        Assert.assertEquals("C003", metaphone.metaphone("Caramel"));
        Assert.assertEquals("D002", metaphone.metaphone("Diphthong"));
        Assert.assertEquals("E001", metaphone.metaphone("Encapsulate"));
        Assert.assertEquals("G002", metaphone.metaphone("Gorilla"));
        Assert.assertEquals("H001", metaphone.metaphone("Herpetology"));
        Assert.assertEquals("J002", metaphone.metaphone("Jujubee"));
        Assert.assertEquals("K003", metaphone.metaphone("Kangaroos"));
        Assert.assertEquals("N003", metaphone.metaphone("Neurotic"));
        Assert.assertEquals("P002", metaphone.metaphone("Pneumonia"));
        Assert.assertEquals("R002", metaphone.metaphone("Rhinoceros"));
        Assert.assertEquals("S003", metaphone.metaphone("Sloth"));
        Assert.assertEquals("T003", metaphone.metaphone("Toucan"));
        Assert.assertEquals("V001", metaphone.metaphone("Velociraptor"));
        Assert.assertEquals("X002", metaphone.metaphone("Xylophonist"));
        Assert.assertEquals("Z001", metaphone.metaphone("Zeppelin"));
    }

    @Test
    public void testMaxCodeLength() {
        Assert.assertEquals("A1B2C3D", metaphone.metaphone("Apple Banana Cat Dog"));
        Assert.assertEquals("A1B2C3D4", metaphone.metaphone("Apple Banana Cat Dog Elephant"));
    }
}