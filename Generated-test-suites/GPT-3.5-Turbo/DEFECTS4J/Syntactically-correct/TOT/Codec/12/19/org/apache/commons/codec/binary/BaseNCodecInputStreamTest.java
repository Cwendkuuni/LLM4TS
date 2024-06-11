import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class BaseNCodecInputStreamTest {

    private BaseNCodecInputStream baseNCodecInputStream;
    private BaseNCodec baseNCodec;

    @Before
    public void setup() {
        baseNCodec = new BaseNCodec() {
            @Override
            void encode(byte[] src, int off, int len) {

            }

            @Override
            void decode(byte[] src, int off, int len) {

            }

            @Override
            int readResults(byte[] dest, int destOff, int maxDestLen) {
                return 0;
            }

            @Override
            boolean hasData() {
                return false;
            }
        };
    }

    @Test
    public void testRead() throws IOException {
        byte[] input = new byte[]{104,101,108,108,111}; // "hello" as bytes
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        baseNCodecInputStream = new BaseNCodecInputStream(in, baseNCodec, false);

        // Read all bytes of input
        for (int i = 0; i < input.length; i++) {
            int result = baseNCodecInputStream.read();
            assertEquals((int) input[i], result);
        }

        // Read beyond end of input
        int result = baseNCodecInputStream.read();
        assertEquals(-1, result);
    }

    @Test
    public void testReadWithZeroLengthInput() throws IOException {
        byte[] input = new byte[0];
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        baseNCodecInputStream = new BaseNCodecInputStream(in, baseNCodec, false);

        // Read from empty input
        int result = baseNCodecInputStream.read();
        assertEquals(-1, result);
    }

    @Test
    public void testReadWithZeroBytesRead() throws IOException {
        byte[] input = new byte[]{104,101,108,108,111}; // "hello" as bytes
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        baseNCodecInputStream = new BaseNCodecInputStream(in, baseNCodec, false) {
            @Override
            public int read(byte b[], int offset, int len) throws IOException {
                return 0; // simulate 0 bytes read
            }
        };

        // Read until bytes are available
        int result = baseNCodecInputStream.read();
        assertEquals((int) input[0], result);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullInputArray() throws IOException {
        byte[] input = null;
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        baseNCodecInputStream = new BaseNCodecInputStream(in, baseNCodec, false);

        // Expect NullPointerException
        baseNCodecInputStream.read();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeOffset() throws IOException {
        byte[] input = new byte[]{104,101,108,108,111}; // "hello" as bytes
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        baseNCodecInputStream = new BaseNCodecInputStream(in, baseNCodec, false);

        // Expect IndexOutOfBoundsException
        baseNCodecInputStream.read(input, -1, input.length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffsetAndLength() throws IOException {
        byte[] input = new byte[]{104,101,108,108,111}; // "hello" as bytes
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        baseNCodecInputStream = new BaseNCodecInputStream(in, baseNCodec, false);

        // Expect IndexOutOfBoundsException
        baseNCodecInputStream.read(input, 0, input.length + 1);
    }

    @Test(expected = IOException.class)
    public void testReadWithIOException() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]) {
            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                throw new IOException("Test IOException");
            }
        };
        baseNCodecInputStream = new BaseNCodecInputStream(in, baseNCodec, false);

        // Expect IOException
        baseNCodecInputStream.read();
    }

    @Test
    public void testMarkSupported() {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        baseNCodecInputStream = new BaseNCodecInputStream(in, baseNCodec, false);
        boolean result = baseNCodecInputStream.markSupported();

        // Marking is not supported
        assertEquals(false, result);
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] input = new byte[]{104,101,108,108,111}; // "hello" as bytes
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        baseNCodecInputStream = new BaseNCodecInputStream(in, baseNCodec, false);

        // All bytes are available
        assertEquals(input.length, baseNCodecInputStream.available());

        // Read one byte
        baseNCodecInputStream.read();

        // Four bytes are available
        assertEquals(input.length - 1, baseNCodecInputStream.available());

        // Read remaining bytes
        baseNCodecInputStream.read();
        baseNCodecInputStream.read();
        baseNCodecInputStream.read();
        baseNCodecInputStream.read();

        // No byte is available
        assertEquals(0, baseNCodecInputStream.available());
    }

}