import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.Assert;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncode_ByteArray_Success() {
        // Arrange
        byte[] input = "Test".getBytes();
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // Act
        byte[] result = codec.encode(input);

        // Assert
        Assert.assertArrayEquals(new byte[]{84, 101, 115, 116}, result);
    }

    @Test
    public void testDecode_ByteArray_Success() throws DecoderException {
        // Arrange
        byte[] input = new byte[]{84, 101, 115, 116};
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // Act
        byte[] result = codec.decode(input);

        // Assert
        Assert.assertArrayEquals("Test".getBytes(), result);
    }

    @Test
    public void testEncode_String_Success() throws EncoderException {
        // Arrange
        String input = "Test";
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // Act
        String result = codec.encode(input);

        // Assert
        Assert.assertEquals("Test", result);
    }

    @Test
    public void testDecode_String_Success() throws DecoderException {
        // Arrange
        String input = "Test";
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // Act
        String result = codec.decode(input);

        // Assert
        Assert.assertEquals("Test", result);
    }

    @Test
    public void testEncode_Object_ByteArray_Success() throws EncoderException {
        // Arrange
        Object input = "Test";
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // Act
        Object result = codec.encode(input);

        // Assert
        Assert.assertArrayEquals("Test".getBytes(), (byte[]) result);
    }

    @Test
    public void testDecode_Object_ByteArray_Success() throws DecoderException {
        // Arrange
        Object input = new byte[]{84, 101, 115, 116};
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // Act
        Object result = codec.decode(input);

        // Assert
        Assert.assertEquals("Test", result);
    }

    @Test
    public void testDecode_String_Charset_Success() throws DecoderException, UnsupportedEncodingException {
        // Arrange
        String input = "Test";
        String charset = "UTF-8";
        QuotedPrintableCodec codec = new QuotedPrintableCodec(charset);

        // Act
        String result = codec.decode(input, charset);

        // Assert
        Assert.assertEquals("Test", result);
    }
}