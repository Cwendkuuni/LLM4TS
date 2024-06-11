import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

public class BaseNCodecInputStreamTest {

    @Test
    public void testRead_singleByte() throws IOException {
        byte[] testData = {65, 66, 67, 68}; // ASCII values for A, B, C, D
        InputStream in = new ByteArrayInputStream(testData);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, false);

        int result = inputStream.read();

        assertEquals(65, result); // First byte should be 'A' (ASCII value 65)
    }

    @Test
    public void testRead_multipleBytes() throws IOException {
        byte[] testData = {65, 66, 67, 68}; // ASCII values for A, B, C, D
        byte[] resultData = new byte[4];
        InputStream in = new ByteArrayInputStream(testData);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, false);

        int result = inputStream.read(resultData, 0, 4);

        assertEquals(4, result); // Should read 4 bytes
        assertArrayEquals(testData, resultData); // Result should match input
    }

    @Test(expected = NullPointerException.class)
    public void testRead_nullByteArray() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, true);

        inputStream.read(null, 0, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_invalidOffset() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, true);

        inputStream.read(new byte[1], 2, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_invalidLength() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, true);

        inputStream.read(new byte[2], 0, 3);
    }

    @Test
    public void testMarkSupported() {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, false);

        assertFalse(inputStream.markSupported()); // Mark should not be supported
    }

}