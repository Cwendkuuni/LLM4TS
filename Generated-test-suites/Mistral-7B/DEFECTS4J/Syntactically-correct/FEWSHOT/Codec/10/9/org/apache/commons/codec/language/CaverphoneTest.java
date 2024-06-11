package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class CaverphoneTest {

    private String input;
    private String expectedOutput;

    public CaverphoneTest(String input, String expectedOutput) {
        this.input = input;
        this.expectedOutput = expectedOutput;
    }

    @Parameterized.Parameters(name = "{index}: input={0} -> expectedOutput={1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"", "1111111111"},
                {"abc", "a2a3a"},
                {"CoughCake", "cou2fkae"},
                {"CavershamProject", "ca2v3rsh4p2o3j3t2i3m3"},
                {"The quick brown fox jumps over the lazy dog", "th3qu1ckbr3wnf0xj2mp3sov3rth3l4z3d3g"},
                {"rhythmandblues", "rhy3thm4ndbl3u3s"},
                {"CaverphoneAlgorithmTest", "ca2v3rp0h0n3alg0rithm3t3st"}
        });
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.encode(input);
        assertEquals("Expected output does not match actual output", expectedOutput, result);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeWithInvalidInput() {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(123);
    }
}