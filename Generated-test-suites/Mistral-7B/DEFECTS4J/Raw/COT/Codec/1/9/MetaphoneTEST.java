package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Assert;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphoneEncodesSingleCharacter() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("A");
        Assert.assertEquals("A", actual);
    }

    @Test
    public void testMetaphoneEncodesMultipleVowels() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("AIEIOU");
        Assert.assertEquals("A0U0", actual);
    }

    @Test
    public void testMetaphoneEncodesMultipleConsonants() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("BCDFGHJKLMPQRSTVWXZ");
        Assert.assertEquals("BKPQXZ", actual);
    }

    @Test
    public void testMetaphoneEncodesInitialK() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("KIT");
        Assert.assertEquals("K0T", actual);
    }

    @Test
    public void testMetaphoneEncodesSilentG() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("BING");
        Assert.assertEquals("J0NG", actual);
    }

    @Test
    public void testMetaphoneEncodesSilentH() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("ATHENA");
        Assert.assertEquals("A0T", actual);
    }

    @Test
    public void testMetaphoneEncodesCH() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("CHILDREN");
        Assert.assertEquals("CH0LDX", actual);
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneThrowsExceptionForNullInput() {
        Metaphone metaphone = new Metaphone();
        metaphone.metaphone(null);
    }

    @Test
    public void testMetaphoneEncodesEmptyString() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("");
        Assert.assertEquals("", actual);
    }

    @Test
    public void testMetaphoneEncodesInputWithPunctuation() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("Hello, World!");
        Assert.assertEquals("H0L0", actual);
    }

    @Test
    public void testMetaphoneEncodesInputWithNumbers() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("One, Two, Three");
        Assert.assertEquals("W1T0", actual);
    }

    @Test
    public void testMetaphoneEncodesInputWithMultipleWords() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("The quick brown fox jumps over the lazy dog.");
        Assert.assertEquals("T2Q0B0R0X0F0X0SK0J0MP0S0V0T0H0D0G0", actual);
    }

    @Test
    public void testEncodeFunctionality() {
        Metaphone metaphone = new Metaphone();
        Assert.assertEquals(metaphone.metaphone("AIEIOU"), metaphone.encode("AIEIOU"));
        Assert.assertEquals(metaphone.metaphone("BCDFGHJKLMPQRSTVWXZ"), metaphone.encode("BCDFGHJKLMPQRSTVWXZ"));
        Assert.assertEquals(metaphone.metaphone("KIT"), metaphone.encode("KIT"));
        Assert.assertEquals(metaphone.metaphone("BING"), metaphone.encode("BING"));
        Assert.assertEquals(metaphone.metaphone("ATHENA"), metaphone.encode("ATHENA"));
        Assert.assertEquals(metaphone.metaphone("CHILDREN"), metaphone.encode("CHILDREN"));
    }

    @Test
    public void testIsMetaphoneEqualFunctionality() {
        Metaphone metaphone = new Metaphone();
        Assert.assertTrue(metaphone.isMetaphoneEqual("AIEIOU", "AIEIOU"));
        Assert.assertTrue(metaphone.isMetaphoneEqual("BCDFGHJKLMPQRSTVWXZ", "BCDFGHJKLMPQRSTVWXZ"));
        Assert.assertTrue(metaphone.isMetaphoneEqual("KIT", "KIT"));
        Assert.assertTrue(metaphone.isMetaphoneEqual("BING", "BING"));
        Assert.assertTrue(metaphone.isMetaphoneEqual("ATHENA", "ATHENA"));
        Assert.assertTrue(metaphone.isMetaphoneEqual("CHILDREN", "CHILDREN"));
        Assert.assertFalse(metaphone.isMetaphoneEqual("AIEIOU", "AIEIO"));
        Assert.assertFalse(metaphone.isMetaphoneEqual("BCDFGHJKLMPQRSTVWXZ", "BCDFGHJKLMPQRSTVWX"));
        Assert.assertFalse(metaphone.isMetaphoneEqual("KIT", "KITT"));
        Assert.assertFalse(metaphone.isMetaphoneEqual("BING", "BINGS"));
        Assert.assertFalse(metaphone.isMetaphoneEqual("ATHENA", "ATHEN"));
        Assert.assertFalse(metaphone.isMetaphoneEqual("CHILDREN", "CHILDRE"));
    }

    @Test
    public void testMaxCodeLenFunctionality() {
        Metaphone metaphone = new Metaphone();
        Assert.assertEquals(4, metaphone.getMaxCodeLen());
        metaphone.setMaxCodeLen(5);
        String actual = metaphone.metaphone("ATHENA");
        Assert.assertEquals("A0T0H0N0A", actual);
    }
}