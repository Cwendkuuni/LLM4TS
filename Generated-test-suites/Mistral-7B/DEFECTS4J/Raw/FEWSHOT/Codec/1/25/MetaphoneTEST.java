package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphoneEncodedValuesAreEqualForEqualStrings() {
        Metaphone metaphone = new Metaphone();
        String str1 = "apple";
        String str2 = "apple";
        String metaphoneCode1 = metaphone.metaphone(str1);
        String metaphoneCode2 = metaphone.metaphone(str2);
        assertEquals(metaphoneCode1, metaphoneCode2);
    }

    @Test
    public void testMetaphoneEncodedValuesAreEqualForSimilarStrings() {
        Metaphone metaphone = new Metaphone();
        String str1 = "banana";
        String str2 = "appela";
        String metaphoneCode1 = metaphone.metaphone(str1);
        String metaphoneCode2 = metaphone.metaphone(str2);
        assertEquals(metaphoneCode1, metaphoneCode2);
    }

    @Test
    public void testMetaphoneEncodedValuesAreNotEqualForDifferentStrings() {
        Metaphone metaphone = new Metaphone();
        String str1 = "apple";
        String str2 = "banana";
        String metaphoneCode1 = metaphone.metaphone(str1);
        String metaphoneCode2 = metaphone.metaphone(str2);
        assertNotEquals(metaphoneCode1, metaphoneCode2);
    }

    @Test
    public void testMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        assertEquals(4, metaphone.getMaxCodeLen());
    }

    @Test(expected = EncoderException.class)
    public void testEncodeExceptionForNonString() {
        Metaphone metaphone = new Metaphone();
        int number = 5;
        metaphone.encode(number);
    }

}