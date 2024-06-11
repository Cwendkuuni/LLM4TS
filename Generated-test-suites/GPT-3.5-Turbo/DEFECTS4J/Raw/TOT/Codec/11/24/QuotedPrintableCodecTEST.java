import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class QuotedPrintableCodecTest {

    private QuotedPrintableCodec codec;

    @Before
    public void setUp() {
        codec = new QuotedPrintableCodec();
    }

    @Test
    public void testEncode_byteArray_success() {
        byte[] input = "Test".getBytes();
        byte[] expectedOutput = {84, 101, 115, 116};
        byte[] actualOutput = codec.encode(input);
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testEncode_byteArray_emptyInput() {
        byte[] input = new byte[0];
        byte[] expectedOutput = new byte[0];
        byte[] actualOutput = codec.encode(input);
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDecode_byteArray_success() {
        byte[] input = {84, 101, 115, 116};
        byte[] expectedOutput = "Test".getBytes();
        byte[] actualOutput = codec.decode(input);
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDecode_byteArray_emptyInput() {
        byte[] input = new byte[0];
        byte[] expectedOutput = new byte[0];
        byte[] actualOutput = codec.decode(input);
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testEncode_String_success() {
        String input = "Hello World!";
        String expectedOutput = "Hello World!";
        String actualOutput = codec.encode(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDecode_String_success() {
        String input = "Hello World!";
        String expectedOutput = "Hello World!";
        String actualOutput = codec.decode(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDecode_String_emptyInput() {
        String input = "";
        String expectedOutput = "";
        String actualOutput = codec.decode(input);
        assertEquals(expectedOutput, actualOutput);
    }
}