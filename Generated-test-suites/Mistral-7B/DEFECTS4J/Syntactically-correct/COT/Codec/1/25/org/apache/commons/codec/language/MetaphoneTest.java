package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Assert;
import org.junit.Test;

public class MetaphoneTest {

    

    @Test
    public void testMetaphoneEmptyString() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("");
        String expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneSingleCharacter() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("A");
        String expected = "A";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneMultipleCharacters() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("Banana");
        String expected = "B321";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneWithDifferentCase() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("Apple");
        String expected = "A123";
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneNullString() {
        Metaphone metaphone = new Metaphone();
        metaphone.encode(null);
    }

    @Test
    public void testMetaphoneWithSpecialCharacters() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("Ra!dio");
        String expected = "R260";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneWithNumbers() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("Number123");
        String expected = "N260";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneWithDifferentWords() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.isMetaphoneEqual("Apple", "Banana");
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneWithSameWords() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.isMetaphoneEqual("Apple", "Apple");
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneWithMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        metaphone.setMaxCodeLen(5);
        String actual = metaphone.metaphone("AppleAppleAppleAppleApple");
        String expected = "A123A123A123A123A123";
        Assert.assertEquals(expected, actual);
    }

    
}