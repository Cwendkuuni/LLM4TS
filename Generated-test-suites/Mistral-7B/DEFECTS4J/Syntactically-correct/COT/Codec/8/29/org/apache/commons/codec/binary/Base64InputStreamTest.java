package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Test;

/**
 * Test class for Base64InputStream
 */
public class Base64InputStreamTest {

    /**
     * Test constructor Base64InputStream(InputStream in)
     *
     * @throws IOException
     */
    @Test
    public void testBase64InputStream() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);
        assertEquals("Test", base64InputStream.readAllBytes());
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode)
     *
     * @throws IOException
     */
    @Test
    public void testBase64InputStreamDecode() throws IOException {
        InputStream input = new ByteArrayInputStream("Rm9udCAoKSB7".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, false);
        assertEquals("Test", base64InputStream.readAllBytes());
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode, int lineLength, byte[] lineSeparator)
     *
     * @throws IOException
     */
    @Test
    public void testBase64InputStreamDecodeWithLineLength() throws IOException {
        InputStream input = new ByteArrayInputStream("Rm9udCAoKSB7CiAg".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, false, 0, null);
        assertEquals("Test\n", base64InputStream.readAllBytes());

        byte[] lineSeparator = new byte[] { 13, 10 };
        Base64InputStream base64InputStreamWithLineSeparator = new Base64InputStream(input, false, 0, lineSeparator);
        assertEquals("Test\r\n", base64InputStreamWithLineSeparator.readAllBytes());
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode) with encoding
     *
     * @throws IOException
     */
    @Test
    public void testBase64InputStreamEncode() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        Base64 base64 = new Base64();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, false);
        base64OutputStream.write(input.readAllBytes());
        base64OutputStream.flush();
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(outputStream.toByteArray()), true);
        assertEquals("Rm9udCAoKSB7", base64InputStream.readAllBytes().toString());
    }

    /**
     * Test method markSupported()
     */
    @Test
    public void testMarkSupported() {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]), false);
        assertFalse(base64InputStream.markSupported());
    }

    /**
     * Test method read()
     *
     * @throws IOException
     */
    @Test
    public void testRead() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, false);
        int byteRead = base64InputStream.read();
        assertEquals(54, byteRead); // 'T' ASCII code is 54
        byteRead = base64InputStream.read();
        assertEquals(69, byteRead); // 'e' ASCII code is 69
        byteRead = base64InputStream.read();
        assertEquals(83, byteRead); // 's' ASCII code is 83
        byteRead = base64InputStream.read();
        assertEquals(76, byteRead); // 'T' ASCII code is 76
        byteRead = base64InputStream.read();
        assertEquals(-1, byteRead); // EOF
    }

    /**
     * Test method read(byte[], int, int)
     *
     * @throws IOException
     */
    @Test
    public void testReadByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, false);
        byte[] expected = new byte[] { 'T', 'e', 's', 't' };
        byte[] received = new byte[4];
        int length = base64InputStream.read(received, 0, 4);
        assertEquals(4, length);
        for (int i = 0; i < 4; i++) {
            assertEquals(expected[i], received[i]);
        }
    }

    /**
     * Test method read(byte[]) when the specified byte array is too small
     *
     * @throws IOException
     */
    @Test
    public void testReadByteArrayTooSmall() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, false);
        byte[] expected = new byte[] { 'T', 'e', 's', 't' };
        byte[] received = new byte[3];
        int length = base64InputStream.read(received, 0, 3);
        assertEquals(3, length);
        for (int i = 0; i < 3; i++) {
            assertEquals(expected[i], received[i]);
        }
        assertEquals(-1, base64InputStream.read(received, 0, 1)); // EOF
    }

    /**
     * Test method read(byte[]) when the specified byte array is the right size
     *
     * @throws IOException
     */
    @Test
    public void testReadByteArrayRightSize() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, false);
        byte[] expected = new byte[] { 'T', 'e', 's', 't' };
        byte[] received = new byte[4];
        int length = base64InputStream.read(received);
        assertEquals(4, length);
        for (int i = 0; i < 4; i++) {
            assertEquals(expected[i], received[i]);
        }
    }

    /**
     * Test method read(byte[]) when the specified byte array is larger than the data available
     *
     * @throws IOException
     */
    @Test
    public void testReadByteArrayLargerThanDataAvailable() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, false);
        byte[] expected = new byte[] { 'T', 'e', 's', 't' };
        byte[] received = new byte[10];
        int length = base64InputStream.read(received);
        assertEquals(4, length);
        for (int i = 0; i < 4; i++) {
            assertEquals(expected[i], received[i]);
        }
        assertEquals(-1, base64InputStream.read(received, 4, 5)); // EOF
    }

    /**
     * Test method read(byte[]) when the specified byte array is null
     *
     * @throws IOException
     */
    @Test(expected = NullPointerException.class)
    public void testReadByteArrayNull() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, false);
        base64InputStream.read(null, 0, 1);
    }

    /**
     * Test method read(byte[], int, int) with a negative offset
     *
     * @throws IOException
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayNegativeOffset() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, false);
        byte[] array = new byte[5];
        base64InputStream.read(array, -1, 1);
    }

    /**
     * Test method read(byte[], int, int) with a negative length
     *
     * @throws IOException
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayNegativeLength() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, false);
        byte[] array = new byte[5];
        base64InputStream.read(array, 0, -1);
    }

    /**
     * Test method read(byte[], int, int) with an offset bigger than the array size
     *
     * @throws IOException
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayOffsetBiggerThanArraySize() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, false);
        byte[] array = new byte[3];
        base64InputStream.read(array, 3, 1);
    }

    /**
     * Test method read(byte[], int, int) with a length bigger than the remaining data
     *
     * @throws IOException
     */
    @Test
    public void testReadByteArrayLengthBiggerThanRemainingData() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, false);
        byte[] array = new byte[10];
        int length = base64InputStream.read(array, 0, 5);
        assertEquals(4, length);
        for (int i = 0; i < 4; i++) {
            assertEquals('T', array[i]);
        }
        assertEquals(-1, base64InputStream.read(array, 4, 1)); // EOF
    }

    /**
     * Test method readAllBytes()
     *
     * @throws IOException
     */
    @Test
    public void testReadAllBytes() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, false);
        byte[] expected = new byte[] { 'T', 'e', 's', 't' };
        byte[] received = base64InputStream.readAllBytes();
        for (int i = 0; i < 4; i++) {
            assertEquals(expected[i], received[i]);
        }
    }

    /**
     * Test method readAllBytes() when no data is available
     *
     * @throws IOException
     */
    @Test
    public void testReadAllBytesNoDataAvailable() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(input, false);
        byte[] received = base64InputStream.readAllBytes();
        assertNull(received);
    }

    /**
     * Test method readAllBytes() when an IOException is thrown
     *
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testReadAllBytesIOException() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]) {
            @Override
            public int read() throws IOException {
                throw new IOException();
            }
        };
        Base64InputStream base64InputStream = new Base64InputStream(input, false);
        base64InputStream.readAllBytes();
    }
}