import org.apache.commons.codec.binary.BaseNCodec;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static org.junit.Assert.*;

public class BaseNCodecInputStreamTest {

    private BaseNCodecInputStream baseNCodecInputStream;

    @Before
    public void setUp() {
        byte[] data = "Test data".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);
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
                System.arraycopy(data, i, pArray, 0, length);
                return length;
            }
        };

        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
    }

    @After
    public void tearDown() {
        try {
            baseNCodecInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRead() throws IOException {
        int actual = baseNCodecInputStream.read();
        assertEquals(84, actual); // ASCII value of 'T'
    }

    @Test
    public void testReadWithEmptyInputStream() throws IOException {
        BaseNCodecInputStream emptyInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), new BaseNCodec() {
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
                return -1;
            }
        }, false);

        int actual = emptyInputStream.read();
        assertEquals(-1, actual);
    }

    @Test
    public void testReadMultipleBytes() throws IOException {
        byte[] expected = "Test".getBytes();
        byte[] actual = new byte[expected.length];
        int bytesRead = baseNCodecInputStream.read(actual, 0, expected.length);

        assertEquals(expected.length, bytesRead);
        assertTrue(Arrays.equals(expected, actual));
    }

    @Test
    public void testReadWithNullByteArray() {
        try {
            baseNCodecInputStream.read(null, 0, 1);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertNotNull(e);
        } catch (IOException e) {
            fail("Unexpected IOException was thrown");
        }
    }

    @Test
    public void testReadWithInvalidOffset() {
        try {
            baseNCodecInputStream.read(new byte[1], -1, 1);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertNotNull(e);
        } catch (IOException e) {
            fail("Unexpected IOException was thrown");
        }
    }

    @Test
    public void testReadWithInvalidLength() {
        try {
            baseNCodecInputStream.read(new byte[1], 0, -1);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertNotNull(e);
        } catch (IOException e) {
            fail("Unexpected IOException was thrown");
        }
    }

    @Test
    public void testReadWithInvalidBufferLength() {
        try {
            baseNCodecInputStream.read(new byte[1], 0, 2);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertNotNull(e);
        } catch (IOException e) {
            fail("Unexpected IOException was thrown");
        }
    }

    @Test
    public void testMarkSupported() {
        assertFalse(baseNCodecInputStream.markSupported());
    }

    @Test
    public void testAvailable() throws IOException {
        int available = baseNCodecInputStream.available();
        assertEquals(1, available);
    }

    @Test
    public void testAvailableWithEOF() throws IOException {
        BaseNCodecInputStream emptyInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), new BaseNCodec() {
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
                return -1;
            }
        }, false);

        int available = emptyInputStream.available();
        assertEquals(0, available);
    }

}