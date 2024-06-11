package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Assert;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphoneEncodingsAreEqual() {
        Metaphone metaphoneEncoder = new Metaphone();
        String str1 = "apple";
        String str2 = "appel";
        String str3 = "apples";
        String str4 = "apaloosa";
        String str5 = "administrator";

        String metaphoneCode1 = metaphoneEncoder.metaphone(str1);
        String metaphoneCode2 = metaphoneEncoder.metaphone(str2);
        String metaphoneCode3 = metaphoneEncoder.metaphone(str3);
        String metaphoneCode4 = metaphoneEncoder.metaphone(str4);
        String metaphoneCode5 = metaphoneEncoder.metaphone(str5);

        Assert.assertEquals(metaphoneCode1, metaphoneCode2); // Test for same word with different casing
        Assert.assertEquals(metaphoneCode1, metaphoneCode3.substring(0, Math.min(metaphoneCode3.length(), metaphoneCode1.length()))); // Test for plural form
        Assert.assertNotEquals(metaphoneCode1, metaphoneCode4); // Test for different words
        Assert.assertEquals(metaphoneCode5, "A000"); // Test for special case
    }

    @Test(expected = EncoderException.class)
    public void testEmptyString() {
        Metaphone metaphoneEncoder = new Metaphone();
        String emptyString = "";
        metaphoneEncoder.metaphone(emptyString);
    }

    @Test(expected = EncoderException.class)
    public void testNullString() {
        Metaphone metaphoneEncoder = new Metaphone();
        String nullString = null;
        metaphoneEncoder.metaphone(nullString);
    }

    @Test(expected = EncoderException.class)
    public void testNonAlphaNumericString() {
        Metaphone metaphoneEncoder = new Metaphone();
        String nonAlphaNumericString = "1234abcd!@#$%^&*()_+={}[]|\\:;\"'<>,.?/";
        metaphoneEncoder.metaphone(nonAlphaNumericString);
    }

    @Test
    public void testMaxCodeLen() {
        Metaphone metaphoneEncoder = new Metaphone();
        String longString = "thisisalongstringattemptingtomakeaslongaspossiblemetaphonecode";
        String metaphoneCodeLongString = metaphoneEncoder.metaphone(longString);
        int maxCodeLen = metaphoneEncoder.getMaxCodeLen();
        Assert.assertEquals(metaphoneCodeLongString.length(), maxCodeLen);
    }
}