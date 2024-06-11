import static org.mockito.Mockito.*;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class BaseNCodecInputStreamTest {

    private InputStream mockInputStream;
    private BaseNCodec mockCodec;
    private BaseNCodecInputStream codecInputStream;

    @Before
    public void setUp() {
        mockInputStream = mock(InputStream.class);
        mockCodec = mock(BaseNCodec.class);
        codecInputStream = new BaseNCodecInputStream(mockInputStream, mockCodec, true);
    }

    @Test
    public void testRead_singleByte() throws IOException {
        int expected = 65;
        byte[] bytes = new byte[1];
        bytes[0] = (byte) expected;

        when(mockInputStream.read(bytes, 0, 1)).thenReturn(1);
        when(mockCodec.readResults(bytes, 0, 1)).thenReturn(1);

        int actual = codecInputStream.read();

        assertEquals(expected, actual);
    }

    @Test
    public void testRead_multipleBytes() throws IOException {
        int expected = 6578;
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (expected >> 8);
        bytes[1] = (byte) expected;

        when(mockInputStream.read(bytes, 0, 2)).thenReturn(2);
        when(mockCodec.readResults(bytes, 0, 2)).thenReturn(2);

        byte[] buffer = new byte[2];

        int actual = codecInputStream.read(buffer, 0, 2);

        assertEquals(expected, actual);
        assertArrayEquals(bytes, buffer);
    }

    @Test
    public void testRead_EOF() throws IOException {
        int expected = -1;
        byte[] bytes = new byte[1];
        bytes[0] = (byte) expected;

        when(mockInputStream.read(bytes, 0, 1)).thenReturn(-1);
        when(mockCodec.readResults(bytes, 0, 1)).thenReturn(-1);

        int actual = codecInputStream.read();

        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testRead_NullArray() throws IOException {
        codecInputStream.read(null, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_NegativeOffset() throws IOException {
        codecInputStream.read(new byte[1], -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_NegativeLength() throws IOException {
        codecInputStream.read(new byte[1], 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_OffsetOverflow() throws IOException {
        codecInputStream.read(new byte[1], 2, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_LengthOverflow() throws IOException {
        codecInputStream.read(new byte[1], 0, 2);
    }

    @Test(expected = NullPointerException.class)
    public void testReadResults_NullArray() throws IOException {
        codecInputStream.read(new byte[1], 0, 1);
        codecInputStream.read(null, 0, 1);
    }

    @Test
    public void testMarkSupported() {
        boolean actual = codecInputStream.markSupported();

        assertFalse(actual);
    }

    @Test
    public void testSkip() throws IOException {
        long expected = 2;

        when(mockInputStream.skip(expected)).thenReturn(expected);

        long actual = codecInputStream.skip(expected);

        assertEquals(expected, actual);
    }

    @Test
    public void testAvailable() throws IOException {
        int expected = 1;

        when(mockInputStream.available()).thenReturn(1);

        int actual = codecInputStream.available();

        assertEquals(expected, actual);
    }

}