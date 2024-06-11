package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class MetaphoneTest {

    private Metaphone metaphone;
    private String input;
    private String expectedOutput;
    private String differentInput;

    @Parameters
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                {"apple", "APEL"},
                {"banana", "BANA"},
                {"cat", "KAT"},
                {"dog", "DOK"},
                {"elephant", "ELPHENT"},
                {"elegant", "ELEGANT"},
                {"euphony", "EUPHONI"},
                {"metaphone", "MTAPHN"},
                {"phoneme", "FONEM"},
                {"rhinoceros", "RHINOS"},
                {"silence", "SILNS"},
                {"test", "TES"},
                {"tomato", "TMATO"},
                {"watermelon", "WTERMLN"},
                {"yes", "YS"},
                {"zebra", "ZEBR"},
                // edge cases
                {"aardvark", "AQRVK"},
                {"almond", "ELMND"},
                {"baboon", "BABUN"},
                {"caramel", "KRMML"},
                {"doughnut", "DGNUT"},
                {"echo", "EKO"},
                {"fizz", "FITZ"},
                {"gymnasium", "JMNSM"},
                {"hospital", "HSPTL"},
                {"indigo", "IDKO"},
                {"jazz", "JETS"},
                {"kangaroo", "KNGPR"},
                {"lighthouse", "LTHHS"},
                {"mango", "MNG"},
                {"noodle", "NLDL"},
                {"often", "OFN"},
                {"quarantine", "KRNTN"},
                {"rubber", "RBAR"},
                {"sunset", "SNST"},
                {"tangerine", "TNGRN"},
                {"university", "VRNSVT"},
                {"volcano", "WLKNS"},
                {"whisper", "WISPR"},
                {"xylophone", "KSFPN"},
                {"zucchini", "ZKCHNI"},
                // exception handling
                {null, null},
                { "", ""},
                { "123", ""},
                { "abc!def", ""},
                { "Metaphone", ""}
        });
    }

    public MetaphoneTest(String input, String expectedOutput, String differentInput) {
        this.input = input;
        this.expectedOutput = expectedOutput;
        this.differentInput = differentInput;
        this.metaphone = new Metaphone();
    }

    @Test
    public void testMetaphone() throws EncoderException {
        String output = metaphone.metaphone(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = EncoderException.class)
    public void testNullInput() {
        metaphone.metaphone(null);
    }

    @Test
    public void testEmptyInput() {
        String output = metaphone.metaphone("");
        assertNull(output);
    }

    @Test
    public void testDigitInput() {
        String output = metaphone.metaphone("123");
        assertNull(output);
    }

    @Test
    public void testSpecialCharInput() {
        String output = metaphone.metaphone("abc!def");
        assertNull(output);
    }

    @Test
    public void testMetaphoneEqual() {
        assertTrue(metaphone.isMetaphoneEqual(input, expectedOutput));
    }

    @Test
    public void testMetaphoneNotEqual() {
        assertFalse(metaphone.isMetaphoneEqual(input, differentInput));
        assertNotEquals(expectedOutput, differentInput);
    }

    @Test
    public void testGetMaxCodeLen() {
        assertEquals(4, metaphone.getMaxCodeLen());
    }
}