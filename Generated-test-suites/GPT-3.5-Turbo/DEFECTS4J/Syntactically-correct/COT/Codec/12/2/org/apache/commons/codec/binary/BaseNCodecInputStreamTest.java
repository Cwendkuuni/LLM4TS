
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class BaseNCodecInputStreamTest {

    private BaseNCodecInputStream stream;

    @Before
    public void setUp() {
        // Create a mock input stream for testing
        byte[] testData = new byte[]{0, 1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);

        // Create the BaseNCodecInputStream
        BaseNCodec baseNCodec = new BaseNCodec() {
            @Override
            void encode(byte[] pArray, int i, int length) throws IOException {

            }

            @Override
            void decode(byte[] pArray, int i, int length) throws IOException {

            }

            @Override
            boolean hasData() {
                return false;
            }

            @Override
            int readResults(byte[] pArray, int i, int length) throws IOException {
                return 0;
            }
        };

        stream = new BaseNCodecInputStream(inputStream, baseNCodec, true);
    }

    @Test
    public void testRead() throws IOException {
        assertEquals(0, stream.read());
        assertEquals(1, stream.read());
        assertEquals(2, stream.read());
        assertEquals(3, stream.read());
        assertEquals(4, stream.read());
        assertEquals(5, stream.read());
        assertEquals(BaseNCodec.EOF, stream.read());
    }

    @Test
    public void testReadWithBufferAndOffset() throws IOException {
        byte[] buffer = new byte[3];
        assertEquals(3, stream.read(buffer, 0, 3));
        assertArrayEquals(new byte[]{0, 1, 2}, buffer);

        buffer = new byte[3];
        assertEquals(3, stream.read(buffer, 0, 3));
        assertArrayEquals(new byte[]{3, 4, 5}, buffer);

        buffer = new byte[3];
        assertEquals(BaseNCodec.EOF, stream.read(buffer, 0, 3));
        assertArrayEquals(new byte[]{0, 0, 0}, buffer);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws IOException {
        stream.read(null, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeOffset() throws IOException {
        stream.read(new byte[1], -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeLength() throws IOException {
        stream.read(new byte[1], 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        stream.read(new byte[1], 2, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        stream.read(new byte[1], 0, 2);
    }

    @Test
    public void testMarkSupported() {
        assertFalse(stream.markSupported());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkipWithNegativeLength() {
        try {
            stream.skip(-1);
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    @Test
    public void testAvailable() throws IOException {
        assertEquals(1, stream.available());
    }
}

