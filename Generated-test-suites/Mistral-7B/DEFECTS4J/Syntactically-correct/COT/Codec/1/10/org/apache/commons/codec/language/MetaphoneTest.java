package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Assert;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphoneEncoderInstance() {
        Metaphone metaphone = new Metaphone();
        Assert.assertNotNull(metaphone);
    }

    @Test
    public void testEmptyString() {
        Metaphone metaphone = new Metaphone();
        String emptyStringMetaphone = metaphone.metaphone("");
        Assert.assertEquals("", emptyStringMetaphone);
    }

    @Test(expected = EncoderException.class)
    public void testNullString() {
        Metaphone metaphone = new Metaphone();
        String nullString = null;
        metaphone.metaphone(nullString);
    }

    @Test
    public void testSingleCharacterString() {
        Metaphone metaphone = new Metaphone();
        String singleCharacterMetaphone = metaphone.metaphone("A");
        Assert.assertEquals("A", singleCharacterMetaphone);
    }

    @Test
    public void testStringWithInitials() {
        Metaphone metaphone = new Metaphone();
        String stringWithInitials = "KNIGHT";
        String expectedMetaphone = "N2T";
        String actualMetaphone = metaphone.metaphone(stringWithInitials);
        Assert.assertEquals(expectedMetaphone, actualMetaphone);
    }

    @Test
    public void testStringWithVowels() {
        Metaphone metaphone = new Metaphone();
        String stringWithVowels = "APE";
        String expectedMetaphone = "A0P";
        String actualMetaphone = metaphone.metaphone(stringWithVowels);
        Assert.assertEquals(expectedMetaphone, actualMetaphone);
    }

    @Test
    public void testStringWithDoubleLetter() {
        Metaphone metaphone = new Metaphone();
        String stringWithDoubleLetter = "BABY";
        String expectedMetaphone = "B1B";
        String actualMetaphone = metaphone.metaphone(stringWithDoubleLetter);
        Assert.assertEquals(expectedMetaphone, actualMetaphone);
    }

    @Test
    public void testStringEndsWithH() {
        Metaphone metaphone = new Metaphone();
        String stringEndsWithH = "BABYAH";
        String expectedMetaphone = "B1B";
        String actualMetaphone = metaphone.metaphone(stringEndsWithH);
        Assert.assertEquals(expectedMetaphone, actualMetaphone);
    }

    @Test
    public void testStringStartsWithDoubleConsonant() {
        Metaphone metaphone = new Metaphone();
        String stringStartsWithDoubleConsonant = "SPRINTS";
        String expectedMetaphone = "S2T";
        String actualMetaphone = metaphone.metaphone(stringStartsWithDoubleConsonant);
        Assert.assertEquals(expectedMetaphone, actualMetaphone);
    }

    @Test
    public void testStringWithMultipleExceptions() {
        Metaphone metaphone = new Metaphone();
        String stringWithMultipleExceptions = "CHILDREN";
        String expectedMetaphone = "XL0R";
        String actualMetaphone = metaphone.metaphone(stringWithMultipleExceptions);
        Assert.assertEquals(expectedMetaphone, actualMetaphone);
    }

    @Test
    public void testStringWithQu() {
        Metaphone metaphone = new Metaphone();
        String stringWithQu = "QUICK";
        String expectedMetaphone = "Q0K";
        String actualMetaphone = metaphone.metaphone(stringWithQu);
        Assert.assertEquals(expectedMetaphone, actualMetaphone);
    }

    @Test
    public void testStringWithMultipleWords() {
        Metaphone metaphone = new Metaphone();
        String stringWithMultipleWords = "THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG";
        String expectedMetaphone = "T2X KW2R F1KS P2S T3H L2Z D3G";
        String actualMetaphone = metaphone.metaphone(stringWithMultipleWords);
        Assert.assertEquals(expectedMetaphone, actualMetaphone);
    }
}