import org.junit.Test;
import static org.junit.Assert.*;

public class BaseNCodecInputStreamTest {

    @Test
    public void testRead() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream stream = new BaseNCodecInputStream(null, codec, true);
        int result = stream.read();
        assertEquals(-1, result);
    }

    @Test
    public void testReadWithValidInput() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream("Test Input".getBytes());
        BaseNCodecInputStream stream = new BaseNCodecInputStream(input, codec, false);

        int[] expectedOutput = {84, 101, 115, 116, 32, 73, 110, 112, 117, 116};
        int[] actualOutput = new int[expectedOutput.length];

        for (int i = 0; i < expectedOutput.length; i++) {
            actualOutput[i] = stream.read();
        }

        assertArrayEquals(expectedOutput, actualOutput);

        int eof = stream.read();
        assertEquals(-1, eof);
    }

    @Test
    public void testReadWithEmptyInput() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream stream = new BaseNCodecInputStream(input, codec, false);

        int eof = stream.read();
        assertEquals(-1, eof);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullInput() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream stream = new BaseNCodecInputStream(null, codec, false);

        stream.read();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream("Test Input".getBytes());
        BaseNCodecInputStream stream = new BaseNCodecInputStream(input, codec, false);

        byte[] buffer = new byte[10];
        stream.read(buffer, -1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream("Test Input".getBytes());
        BaseNCodecInputStream stream = new BaseNCodecInputStream(input, codec, false);

        byte[] buffer = new byte[10];
        stream.read(buffer, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferSize() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream("Test Input".getBytes());
        BaseNCodecInputStream stream = new BaseNCodecInputStream(input, codec, false);

        byte[] buffer = new byte[5];
        stream.read(buffer, 0, 10);
    }

    @Test
    public void testMarkSupported() {
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream stream = new BaseNCodecInputStream(null, codec, true);

        boolean supported = stream.markSupported();
        assertFalse(supported);
    }

    @Test
    public void testAvailable() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream("Test Input".getBytes());
        BaseNCodecInputStream stream = new BaseNCodecInputStream(input, codec, false);

        int available = stream.available();
        assertEquals(1, available);
    }
}