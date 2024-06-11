package org.apache.commons.codec.language.test;

import org.apache.commons.codec.language.Metaphone;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MetaphoneTest {

    private Metaphone metaphone;

    @Before
    public void setUp() {
        metaphone = new Metaphone();
    }

    

    // Test case for the constructor
    @Test
    public void testMetaphoneConstructor() {
        Metaphone metaphone = new Metaphone();
        Assert.assertNotNull(metaphone);
    }

    // Test case for metaphone method with basic input
    @Test
    public void testMetaphoneBasicInput() {
        String input = "Apple";
        String expected = "A0P0L1E0";
        String output = metaphone.metaphone(input);
        Assert.assertEquals(expected, output);
    }

    // Test case for metaphone method with initial exceptions
    @Test
    public void testMetaphoneInitialExceptions() {
        String[] inputs = {"App", "", "Apple123", null};
        String[] expecteds = {"A0P0", "", "", null};
        for (int i = 0; i < inputs.length; i++) {
            String input = inputs[i];
            String output = metaphone.metaphone(input);
            Assert.assertEquals(expecteds[i], output);
        }
    }

    // Test case for metaphone method with edge cases
    @Test
    public void testMetaphoneEdgeCases() {
        String[] inputs = {"AARDVARK", "ALMOND", "BANANA", "CHEESE", "DOLPHIN", "ECHO", "FOX", "GORILLA", "HAMMER", "INDIA",
                "JUNGLE", "KANGAROO", "LION", "MOUNTAIN", "NOODLES", "ORANGE", "PIZZA", "QUICK", "RASPBERRY", "SHARK",
                "TOMATO", "UMBRELLA", "VIOLIN", "WATERMELON", "XMAS", "YAK", "ZIPPER"};
        String[] expecteds = {"A0000R0D1V1K", "A00L0M0N0D", "A00B0N0N0N0A", "A00S0E0S0E", "A00D0L0F0N", "A00E0H0O", "A00F0X0S",
                "A00G0R0L0L0A", "A00H0M0M0R", "A00I0N0D1A", "A00J0N0G1L1E", "A00K0N0G1R1L1A", "A00L0I0N", "A00M0T0T0M0T",
                "A00Q0I0K0", "A00R1B1S1B1R1B1B1", "A00S1P1B0B1R1B1B1", "A00T0M0T0M0T1O", "A00U0M1B0L1N1", "A00V1O1L1N",
                "A00P1Z1Z1P1P1Z1P1", "A00Q1C1K0", "A00R1B1S1B1B1", "A00S1W1A1T1M1E1L1O", "A00T0M1T1O1M1T1O",
                "A00X1M1A1S1X1M1A1S1X1", "A00Y1A0K1", "A00Z1P1P1Z1P1"};
        for (int i = 0; i < inputs.length; i++) {
            String input = inputs[i];
            String output = metaphone.metaphone(input);
            Assert.assertEquals(expecteds[i], output);
        }
    }

    // Test case for encode method
    @Test
    public void testEncode() {
        String input = "Apple";
        String expected = "A0P0L1E0";
        String output = metaphone.encode(input);
        Assert.assertEquals(expected, output);
    }

    // Test case for encode method with null input
    @Test(expected = EncoderException.class)
    public void testEncodeNullInput() {
        metaphone.encode(null);
    }

    // Test case for isMetaphoneEqual method
    @Test
    public void testIsMetaphoneEqual() {
        String str1 = "Apple";
        String str2 = "Apple";
        boolean expected = true;
        boolean output = metaphone.isMetaphoneEqual(str1, str2);
        Assert.assertEquals(expected, output);
    }

    // Test case for isMetaphoneEqual method with different inputs
    @Test
    public void testIsMetaphoneEqualDifferentInputs() {
        String str1 = "Apple";
        String str2 = "Banana";
        boolean expected = false;
        boolean output = metaphone.isMetaphoneEqual(str1, str2);
        Assert.assertEquals(expected, output);
    }

    // Test case for getMaxCodeLen method
    @Test
    public void testGetMaxCodeLen() {
        int expected = 4;
        int output = metaphone.getMaxCodeLen();
        Assert.assertEquals(expected, output);
    }

    // Test case for setMaxCodeLen method
    @Test
    public void testSetMaxCodeLen() {
        int maxCodeLen = 5;
        metaphone.setMaxCodeLen(maxCodeLen);
        int output = metaphone.getMaxCodeLen();
        Assert.assertEquals(maxCodeLen, output);
    }

    
}