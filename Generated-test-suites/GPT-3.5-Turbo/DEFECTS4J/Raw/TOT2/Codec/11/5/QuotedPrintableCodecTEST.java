import static org.junit.Assert.*;

import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // Typical use case
        byte[] input = "Hello World!".getBytes();
        byte[] expected = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33};
        byte[] result = codec.encode(input);
        assertArrayEquals(expected, result);

        // Edge case: empty input
        input = new byte[0];
        expected = new byte[0];
        result = codec.encode(input);
        assertArrayEquals(expected, result);

        // Error scenario: null input
        input = null;
        assertNull(codec.encode(input));
    }

    @Test
    public void testDecode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // Typical use case
        byte[] input = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33};
        String expected = "Hello World!";
        String result = codec.decode(input);
        assertEquals(expected, result);

        // Edge case: empty input
        input = new byte[0];
        expected = "";
        result = codec.decode(input);
        assertEquals(expected, result);

        // Error scenario: null input
        input = null;
        assertNull(codec.decode(input));
    }

    @Test
    public void testEncodeString() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // Typical use case
        String input = "This is a test!";
        String expected = "This=20is=20a=20test!";
        String result = codec.encode(input);
        assertEquals(expected, result);

        // Edge case: empty input
        input = "";
        expected = "";
        result = codec.encode(input);
        assertEquals(expected, result);

        // Error scenario: null input
        input = null;
        assertNull(codec.encode(input));
    }

    @Test
    public void testDecodeString() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // Typical use case
        String input = "This=20is=20a=20test!";
        String expected = "This is a test!";
        String result = codec.decode(input);
        assertEquals(expected, result);

        // Edge case: empty input
        input = "";
        expected = "";
        result = codec.decode(input);
        assertEquals(expected, result);

        // Error scenario: null input
        input = null;
        assertNull(codec.decode(input));
    }

    @Test
    public void testEncodeWithSpecificCharset() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-16");

        // Typical use case
        String input = "Encoded string";
        String expected = "E=00n=00c=00o=00d=00e=00d=00=20=00s=00t=00r=00i=00n=00g=00";
        String result = codec.encode(input);
        assertEquals(expected, result);

        // Edge case: empty input
        input = "";
        expected = "";
        result = codec.encode(input);
        assertEquals(expected, result);

        // Error scenario: null input
        input = null;
        assertNull(codec.encode(input));
    }

}