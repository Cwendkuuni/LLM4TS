package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.StringUtils;
import org.junit.Test;

public class CaverphoneTest {

    private static final String TEST_DATA[] = {"apple", "banana", "Caversham", "Caverphone", "encoding", "example", "test", "unicode", "util", "water", "white", "Wikipedia"};

    @Test
    public void testEncode() throws EncoderException {
        Caverphone encoder = new Caverphone();
        for (String input : TEST_DATA) {
            String output = encoder.encode(input);
            assertEquals(output.length(), 10);
            assertNotNull(output);
        }
    }

    @Test
    public void testEncodeInvalidInput() throws EncoderException {
        Caverphone encoder = new Caverphone();
        assertThrows(EncoderException.class, () -> encoder.encode(null));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone encoder = new Caverphone();
        String appleCaverphone = encoder.caverphone("apple");
        String bananaCaverphone = encoder.caverphone("banana");
        assertEquals(appleCaverphone, bananaCaverphone);
    }

    @Test
    public void testIsCaverphoneNotEqual() {
        Caverphone encoder = new Caverphone();
        String appleCaverphone = encoder.caverphone("apple");
        String bananaCaverphone = encoder.caverphone("banana");
        String appleCaverphoneAnother = encoder.caverphone("appleApple");
        assertNotEquals(appleCaverphone, appleCaverphoneAnother);
    }

    @Test
    public void testStripLeadingAndTrailingHyphens() {
        String testInput = "--abc--def----";
        String expectedOutput = "abcdef";
        assertEquals(new String(Util.stripLeadingHyphens(Util.stripLeadingHyphens(testInput))), expectedOutput);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String testInput = "\"\"abc\"\"def\"\"";
        String expectedOutput = "abcdef";
        assertEquals(new String(Util.stripLeadingAndTrailingQuotes(Util.stripLeadingAndTrailingQuotes(testInput))), expectedOutput);
    }

    @Test
    public void testCaverphoneLowercase() {
        String testInput = "Apple";
        String expectedOutput = "a11ppl11e";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);
    }

    @Test
    public void testCaverphoneUppercase() {
        String testInput = "APPLE";
        String expectedOutput = "A11ppl11e";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);
    }

    @Test
    public void testCaverphoneEmpty() {
        String testInput = "";
        String expectedOutput = "1111111111";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);
    }

    @Test
    public void testCaverphoneNonLetter() {
        String testInput = "#Apple";
        String expectedOutput = "A11ppl11e";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);
    }

    @Test
    public void testCaverphoneStartOptions() {
        String testInput = "tough";
        String expectedOutput = "tou2f";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "trough";
        expectedOutput = "trou2f";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "enough";
        expectedOutput = "enou2f";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "cough";
        expectedOutput = "cou2f";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "rough";
        expectedOutput = "rou2f";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "gn";
        expectedOutput = "2n";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "mb";
        expectedOutput = "m2";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);
    }

    @Test
    public void testCaverphoneReplacements() {
        String testInput = "cq";
        String expectedOutput = "2q";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "ci";
        expectedOutput = "si";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "ce";
        expectedOutput = "se";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "cy";
        expectedOutput = "sy";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "tch";
        expectedOutput = "2ch";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "c";
        expectedOutput = "k";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "q";
        expectedOutput = "k";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "x";
        expectedOutput = "k";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "v";
        expectedOutput = "f";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "dg";
        expectedOutput = "2g";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "tio";
        expectedOutput = "sio";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "tia";
        expectedOutput = "sia";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "d";
        expectedOutput = "t";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "ph";
        expectedOutput = "fh";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "b";
        expectedOutput = "p";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "sh";
        expectedOutput = "s2";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "z";
        expectedOutput = "s";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "w3";
        expectedOutput = "W3";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "wh3";
        expectedOutput = "Wh3";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "why";
        expectedOutput = "Why";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "w";
        expectedOutput = "2";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "h";
        expectedOutput = "A";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "r3";
        expectedOutput = "R3";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "r";
        expectedOutput = "2";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "l3";
        expectedOutput = "L3";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "l";
        expectedOutput = "2";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "j";
        expectedOutput = "y";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);
    }

    @Test
    public void testCaverphoneHandlesRemovals() {
        String testInput = "2";
        String expectedOutput = "";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "3$";
        expectedOutput = "A";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);

        testInput = "3";
        expectedOutput = "";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);
    }

    @Test
    public void testCaverphoneHandles111111() {
        String testInput = "1111111111";
        String expectedOutput = "1111111111";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);
    }

    @Test
    public void testCaverphoneHandles1111() {
        String testInput = "1111";
        String expectedOutput = "";
        assertEquals(new String(new Caverphone().caverphone(testInput)), expectedOutput);
    }

    @Test
    public void testCaverphoneHandlesSubString() {
        String testInput = "tough";
        String expectedOutput = "tou2f";
        assertEquals(new String(new Caverphone().caverphone(testInput).substring(0, 10)), expectedOutput);
    }

    @Test
    public void testCaverphoneHandlesTruncation() {
        String testInput = "tough";
        String expectedOutput = "tou2f";
        assertEquals(StringUtils.substring(new String(new Caverphone().caverphone(testInput)), 0, 10), expectedOutput);
    }
}