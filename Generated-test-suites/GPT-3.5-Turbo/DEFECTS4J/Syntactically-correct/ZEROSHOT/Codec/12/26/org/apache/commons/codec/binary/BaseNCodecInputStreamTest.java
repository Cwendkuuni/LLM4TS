import org.junit.Test;

import static org.junit.Assert.*;

public class BaseNCodecInputStreamTest {

    @Test
    public void testRead() {
        // Test for single byte read
        InputStream in = new InputStream() {
            @Override
            public int read() throws IOException {
                return 65; // ASCII value for 'A'
            }
        };
        BaseNCodec baseNCodec = new Base64();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, baseNCodec, false);

        try {
            int result = baseNCodecInputStream.read();
            assertEquals(65, result);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testReadArray() {
        // Test for reading byte array
        byte[] input = new byte[]{65, 66, 67, 68};
        InputStream in = new InputStream() {
            private int index = 0;

            @Override
            public int read() throws IOException {
                if (index < input.length) {
                    return input[index++];
                }
                return -1;
            }

            @Override
            public int read(byte[] b, int offset, int len) throws IOException {
                int bytesRead = 0;
                while (bytesRead < len && index < input.length) {
                    b[offset + bytesRead++] = input[index++];
                }
                return bytesRead;
            }
        };
        BaseNCodec baseNCodec = new Base64();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, baseNCodec, false);

        byte[] output = new byte[4];
        try {
            int bytesRead = baseNCodecInputStream.read(output, 0, 4);
            assertEquals(4, bytesRead);
            assertArrayEquals(input, output);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testMarkSupported() {
        InputStream in = new InputStream() {
            @Override
            public int read() throws IOException {
                return -1;
            }
        };
        BaseNCodec baseNCodec = new Base64();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, baseNCodec, false);

        assertFalse(baseNCodecInputStream.markSupported());
    }

    @Test
    public void testAvailable() {
        InputStream in = new InputStream() {
            @Override
            public int read() throws IOException {
                return -1;
            }
        };
        BaseNCodec baseNCodec = new Base64();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, baseNCodec, false);

        try {
            int result = baseNCodecInputStream.available();
            assertEquals(0, result);
        } catch (IOException e) {
            fail();
        }
    }
}