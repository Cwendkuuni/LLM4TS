package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class BaseNCodecInputStreamTest {

    private BaseNCodecInputStream inputStream;

    @Before
    public void setUp() throws Exception {
        BaseNCodec baseNCodec = new BaseNCodec() {

            @Override
            void encode(byte[] pArray, int i, int length, Context context) throws IOException {
                // TODO Auto-generated method stub

            }

            @Override
            void decode(byte[] pArray, int i, int length, Context context) throws IOException {
                // TODO Auto-generated method stub

            }

            @Override
            protected boolean isInAlphabet(byte[] arrayOctet, boolean allowWspad, boolean strict) {
                // TODO Auto-generated method stub
                return false;
            }

        };

        byte[] data = new byte[] { 1, 2, 3, 4, 5 };
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        inputStream = new BaseNCodecInputStream(byteArrayInputStream, baseNCodec, false);
    }

    @Test
    public void testRead() throws IOException {
        assertEquals(1, inputStream.read());
        assertEquals(2, inputStream.read());
        assertEquals(3, inputStream.read());
        assertEquals(4, inputStream.read());
        assertEquals(5, inputStream.read());
        assertEquals(-1, inputStream.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] expected = new byte[] { 1, 2, 3, 4, 5 };
        byte[] actual = new byte[5];
        int bytesRead = inputStream.read(actual);

        assertEquals(5, bytesRead);
        assertArrayEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testReadByteArray_NullArray() throws IOException {
        inputStream.read(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArray_NegativeOffset() throws IOException {
        byte[] array = new byte[5];
        inputStream.read(array, -1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArray_NegativeLen() throws IOException {
        byte[] array = new byte[5];
        inputStream.read(array, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArray_OffsetTooHigh() throws IOException {
        byte[] array = new byte[5];
        inputStream.read(array, 6, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArray_OffsetPlusLenTooHigh() throws IOException {
        byte[] array = new byte[5];
        inputStream.read(array, 4, 2);
    }

    @Test
    public void testMarkSupported() {
        assertEquals(false, inputStream.markSupported());
    }

    @Test
    public void testAvailable() throws IOException {
        assertEquals(5, inputStream.available());
        inputStream.read();
        assertEquals(4, inputStream.available());
    }
}