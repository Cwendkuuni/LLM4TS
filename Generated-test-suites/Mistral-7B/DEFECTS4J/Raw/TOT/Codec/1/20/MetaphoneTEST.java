package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Assert;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphoneEncodedStringsAreEqual() {
        Metaphone metaphone = new Metaphone();

        String str1 = "apple";
        String str2 = "appal";
        String expectedCode1 = metaphone.metaphone(str1);
        String expectedCode2 = metaphone.metaphone(str2);

        String actualCode1 = metaphone.encode(str1);
        String actualCode2 = metaphone.encode(str2);

        Assert.assertEquals(expectedCode1, expectedCode2);
        Assert.assertEquals(expectedCode1, actualCode1);
        Assert.assertEquals(expectedCode2, actualCode2);
    }

    @Test
    public void testMetaphoneEncodesDifferentStringsDifferently() {
        Metaphone metaphone = new Metaphone();

        String str1 = "banana";
        String str2 = "orange";
        String expectedCode1 = metaphone.metaphone(str1);
        String expectedCode2 = metaphone.metaphone(str2);

        String actualCode1 = metaphone.encode(str1);
        String actualCode2 = metaphone.encode(str2);

        Assert.assertNotEquals(expectedCode1, expectedCode2);
        Assert.assertNotEquals(expectedCode1, actualCode2);
        Assert.assertNotEquals(expectedCode2, actualCode1);
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneThrowsExceptionForNonStringInput() {
        Metaphone metaphone = new Metaphone();

        int number = 123;
        metaphone.encode(number);
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneThrowsExceptionForEmptyStringInput() {
        Metaphone metaphone = new Metaphone();

        String emptyString = "";
        metaphone.encode(emptyString);
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneThrowsExceptionForNullInput() {
        Metaphone metaphone = new Metaphone();

        String nullInput = null;
        metaphone.encode(nullInput);
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneThrowsExceptionForSpecialCharacterInput() {
        Metaphone metaphone = new Metaphone();

        String specialCharacterInput = "!@#$%^&*()_+-=[]{}|;:,.<>/?`~";
        metaphone.encode(specialCharacterInput);
    }

    @Test
    public void testMetaphoneEncodesLongString() {
        Metaphone metaphone = new Metaphone();

        String longString = "this is a really long string that needs to be encoded";
        String expectedCode = metaphone.metaphone(longString);
        String actualCode = metaphone.encode(longString);

        Assert.assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testMetaphoneEncodesWordsWithInitialExceptions() {
        Metaphone metaphone = new Metaphone();

        String str1 = "knowledge";
        String expectedCode1 = metaphone.metaphone(str1);
        String actualCode1 = metaphone.encode(str1);

        Assert.assertEquals(expectedCode1.substring(0, 1), "K");
        Assert.assertEquals(expectedCode1.substring(1, expectedCode1.length()), metaphone.metaphone(str1.substring(1)).toString());

        String str2 = "academic";
        String expectedCode2 = metaphone.metaphone(str2);
        String actualCode2 = metaphone.encode(str2);

        Assert.assertEquals(expectedCode2.substring(0, 1), "K");
        Assert.assertEquals(expectedCode2.substring(1, expectedCode2.length()), metaphone.metaphone(str2.substring(1)).toString());
    }

    @Test
    public void testMetaphoneEncodesWordsWithSilentG() {
        Metaphone metaphone = new Metaphone();

        String str1 = "bargain";
        String expectedCode1 = metaphone.metaphone(str1);
        String actualCode1 = metaphone.encode(str1);

        Assert.assertEquals(expectedCode1, "BG");

        String str2 = "counterfeit";
        String expectedCode2 = metaphone.metaphone(str2);
        String actualCode2 = metaphone.encode(str2);

        Assert.assertEquals(expectedCode2, "CF");

        String str3 = "inauguration";
        String expectedCode3 = metaphone.metaphone(str3);
        String actualCode3 = metaphone.encode(str3);

        Assert.assertEquals(expectedCode3, "NG");
    }

    @Test
    public void testMetaphoneEncodesWordsWithMultipleExceptions() {
        Metaphone metaphone = new Metaphone();

        String str1 = "metaphone";
        String expectedCode1 = metaphone.metaphone(str1);
        String actualCode1 = metaphone.encode(str1);

        Assert.assertEquals(expectedCode1, "MTPHN");

        String str2 = "schism";
        String expectedCode2 = metaphone.metaphone(str2);
        String actualCode2 = metaphone.encode(str2);

        Assert.assertEquals(expectedCode2, "SKSM");
    }

    @Test
    public void testMetaphoneEncodesWordsWithMultipleExceptionsAndSilentG() {
        Metaphone metaphone = new Metaphone();

        String str1 = "encountered";
        String expectedCode1 = metaphone.metaphone(str1);
        String actualCode1 = metaphone.encode(str1);

        Assert.assertEquals(expectedCode1, "ENCTR");

        String str2 = "inauguration";
        String expectedCode2 = metaphone.metaphone(str2);
        String actualCode2 = metaphone.encode(str2);

        Assert.assertEquals(expectedCode2, "NG");
    }

    @Test
    public void testMetaphoneEncodesWordsWithDifferentLastLetters() {
        Metaphone metaphone = new Metaphone();

        String str1 = "bubble";
        String expectedCode1 = metaphone.metaphone(str1);
        String actualCode1 = metaphone.encode(str1);

        Assert.assertEquals(expectedCode1, "BBL");

        String str2 = "bubblegum";
        String expectedCode2 = metaphone.metaphone(str2);
        String actualCode2 = metaphone.encode(str2);

        Assert.assertEquals(expectedCode2, "BBGL");
    }

    @Test
    public void testMetaphoneEncodesWordsWithMultipleLastLetters() {
        Metaphone metaphone = new Metaphone();

        String str1 = "bananas";
        String expectedCode1 = metaphone.metaphone(str1);
        String actualCode1 = metaphone.encode(str1);

        Assert.assertEquals(expectedCode1, "BNNS");

        String str2 = "bananasplit";
        String expectedCode2 = metaphone.metaphone(str2);
        String actualCode2 = metaphone.encode(str2);

        Assert.assertEquals(expectedCode2, "BNSPLT");
    }

    @Test
    public void testMetaphoneEncodesWordsWithSameLastLettersButDifferentVowels() {
        Metaphone metaphone = new Metaphone();

        String str1 = "cat";
        String expectedCode1 = metaphone.metaphone(str1);
        String actualCode1 = metaphone.encode(str1);

        Assert.assertEquals(expectedCode1, "KT");

        String str2 = "catatron";
        String expectedCode2 = metaphone.metaphone(str2);
        String actualCode2 = metaphone.encode(str2);

        Assert.assertEquals(expectedCode2, "KTTRN");
    }

    @Test
    public void testMetaphoneEncodesWordsWithSameLastLettersButSameVowels() {
        Metaphone metaphone = new Metaphone();

        String str1 = "apple";
        String expectedCode1 = metaphone.metaphone(str1);
        String actualCode1 = metaphone.encode(str1);

        Assert.assertEquals(expectedCode1, "PL");

        String str2 = "apples";
        String expectedCode2 = metaphone.metaphone(str2);
        String actualCode2 = metaphone.encode(str2);

        Assert.assertEquals(expectedCode2, "PLS");
    }

    @Test
    public void testMetaphoneEncodesWordsWithInitialX() {
        Metaphone metaphone = new Metaphone();

        String str1 = "xylophone";
        String expectedCode1 = metaphone.metaphone(str1);
        String actualCode1 = metaphone.encode(str1);

        Assert.assertEquals(expectedCode1, "XLFON");
    }

    @Test
    public void testMetaphoneEncodesWordsWithInitialQ() {
        Metaphone metaphone = new Metaphone();

        String str1 = "queen";
        String expectedCode1 = metaphone.metaphone(str1);
        String actualCode1 = metaphone.encode(str1);

        Assert.assertEquals(expectedCode1, "KN");
    }
}