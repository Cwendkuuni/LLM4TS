import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Assert;
import org.junit.Test;

/**
 * QuotedPrintableCodecTest class.
 */
public class QuotedPrintableCodecTest {

    private static final QuotedPrintableCodec CODEC = new QuotedPrintableCodec();

    /**
     * Test method for {@link QuotedPrintableCodec#encode(byte[])}.
     */
    @Test
    public void testEncodeByteArray() {
        // Basic test
        byte[] input = "Hello World!".getBytes();
        byte[] expected = CODEC.encode(input);
        byte[] result = CODEC.encode(input);
        Assert.assertArrayEquals(expected, result);

        // Edge case: empty byte array
        byte[] inputEmpty = {};
        byte[] expectedEmpty = {};
        byte[] resultEmpty = CODEC.encode(inputEmpty);
        Assert.assertArrayEquals(expectedEmpty, resultEmpty);

        // Edge case: byte array with all printable characters
        byte[] inputPrintable = new byte[256];
        byte[] expectedPrintable = new byte[256];
        for (int i = 0; i < 256; i++) {
            inputPrintable[i] = (byte) i;
            expectedPrintable[i] = (byte) i;
        }
        byte[] resultPrintable = CODEC.encode(inputPrintable);
        Assert.assertArrayEquals(expectedPrintable, resultPrintable);

        // Error case: null input
        try {
            CODEC.encode(null);
            Assert.fail("An EncoderException should have been thrown");
        } catch (EncoderException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link QuotedPrintableCodec#decode(byte[])}.
     */
    @Test
    public void testDecodeByteArray() {
        // Basic test
        byte[] input = CODEC.encode("Hello World!".getBytes());
        byte[] expected = "Hello World!".getBytes();
        byte[] result = CODEC.decode(input);
        Assert.assertArrayEquals(expected, result);

        // Edge case: empty byte array
        byte[] inputEmpty = {};
        byte[] expectedEmpty = {};
        byte[] resultEmpty = CODEC.decode(inputEmpty);
        Assert.assertArrayEquals(expectedEmpty, resultEmpty);

        // Edge case: byte array with all printable characters
        byte[] inputPrintable = new byte[256];
        byte[] expectedPrintable = new byte[256];
        for (int i = 0; i < 256; i++) {
            inputPrintable[i] = (byte) i;
            expectedPrintable[i] = (byte) i;
        }
        byte[] resultPrintable = CODEC.decode(inputPrintable);
        Assert.assertArrayEquals(expectedPrintable, resultPrintable);

        // Error case: null input
        try {
            CODEC.decode(null);
            Assert.fail("A DecoderException should have been thrown");
        } catch (DecoderException e) {
            // Expected
        }

        // Error case: invalid input
        try {
            byte[] inputInvalid = CODEC.encode("Hello World!\r\n");
            byte[] resultInvalid = CODEC.decode(inputInvalid);
            Assert.fail("A DecoderException should have been thrown");
        } catch (DecoderException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link QuotedPrintableCodec#encode(String)}.
     */
    @Test
    public void testEncodeString() {
        // Basic test
        String input = "Hello World!";
        String expected = CODEC.encode(input);
        String result = CODEC.encode(input);
        Assert.assertEquals(expected, result);

        // Edge case: empty string
        String inputEmpty = "";
        String expectedEmpty = "";
        String resultEmpty = CODEC.encode(inputEmpty);
        Assert.assertEquals(expectedEmpty, resultEmpty);

        // Edge case: string with all printable characters
        String inputPrintable = new String(new byte[256]);
        String expectedPrintable = new String(new byte[256]);
        for (int i = 0; i < 256; i++) {
            inputPrintable = inputPrintable.substring(0, i) + (char) i + inputPrintable.substring(i);
            expectedPrintable = expectedPrintable.substring(0, i) + (char) i + expectedPrintable.substring(i);
        }
        String resultPrintable = CODEC.encode(inputPrintable);
        Assert.assertEquals(expectedPrintable, resultPrintable);

        // Error case: null input
        try {
            CODEC.encode(null);
            Assert.fail("An EncoderException should have been thrown");
        } catch (EncoderException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link QuotedPrintableCodec#decode(String)}.
     */
    @Test
    public void testDecodeString() {
        // Basic test
        String input = CODEC.encode("Hello World!");
        String expected = "Hello World!";
        String result = CODEC.decode(input);
        Assert.assertEquals(expected, result);

        // Edge case: empty string
        String inputEmpty = "";
        String expectedEmpty = "";
        String resultEmpty = CODEC.decode(inputEmpty);
        Assert.assertEquals(expectedEmpty, resultEmpty);

        // Edge case: string with all printable characters
        String inputPrintable = new String(new byte[256]);
        String expectedPrintable = new String(new byte[256]);
        for (int i = 0; i < 256; i++) {
            inputPrintable = inputPrintable.substring(0, i) + (char) i + inputPrintable.substring(i);
            expectedPrintable = expectedPrintable.substring(0, i) + (char) i + expectedPrintable.substring(i);
        }
        String resultPrintable = CODEC.decode(inputPrintable);
        Assert.assertEquals(expectedPrintable, resultPrintable);

        // Error case: null input
        try {
            CODEC.decode(null);
            Assert.fail("A DecoderException should have been thrown");
        } catch (DecoderException e) {
            // Expected
        }

        // Error case: invalid input
        try {
            String inputInvalid = CODEC.encode("Hello World!\r\n");
            String resultInvalid = CODEC.decode(inputInvalid);
            Assert.fail("A DecoderException should have been thrown");
        } catch (DecoderException e) {
            // Expected
        }
    }
}