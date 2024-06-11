package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Test;

/**
 * Test class for {@link Base64InputStream}.
 */
public class Base64InputStreamTest {

    /**
     * Test constructor with default parameters.
     *
     * @throws IOException
     *             if an I/O error occurs while testing
     */
    @Test
    public void testDefaultConstructor() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        assertSame(inputStream, base64InputStream.in);
        assertNull(base64InputStream.base64);
        assertFalse(base64InputStream.doEncode);
    }

    /**
     * Test constructor with doEncode set to true.
     *
     * @throws IOException
     *             if an I/O error occurs while testing
     */
    @Test
    public void testEncodeConstructor() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        Base64 base64 = mock(Base64.class);
        when(base64.encode(anyByteArray(), anyInt(), anyInt())).thenReturn(new byte[0]);

        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, null, null);
        assertSame(inputStream, base64InputStream.in);
        assertSame(base64, base64InputStream.base64);
        assertTrue(base64InputStream.doEncode);
    }

    /**
     * Test constructor with doEncode set to false and lineLength and lineSeparator defined.
     *
     * @throws IOException
     *             if an I/O error occurs while testing
     */
    @Test
    public void testDecodeConstructorWithLineLengthAndLineSeparator() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        Base64 base64 = mock(Base64.class);
        when(base64.decode(anyByteArray(), anyInt(), anyInt())).thenReturn(new byte[0]);

        int lineLength = 10;
        byte[] lineSeparator = new byte[] { 0x0D, 0x0A };

        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, lineLength, lineSeparator);
        assertSame(inputStream, base64InputStream.in);
        assertSame(base64, base64InputStream.base64);
        assertFalse(base64InputStream.doEncode);
        assertEquals(lineLength, base64InputStream.base64.lineLength);
        assertArrayEquals(lineSeparator, base64InputStream.base64.lineSeparator);
    }

    /**
     * Test read method with default constructor.
     *
     * @throws IOException
     *             if an I/O error occurs while testing
     */
    @Test
    public void testReadWithDefaultConstructor() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        int readResult = base64InputStream.read();
        assertEquals(0x01, readResult);
    }

    /**
     * Test read method with encode constructor.
     *
     * @throws IOException
     *             if an I/O error occurs while testing
     */
    @Test
    public void testReadWithEncodeConstructor() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01 });
        Base64 base64 = mock(Base64.class);
        byte[] expectedEncodedByte = new byte[] { 0x3C, 0x3F };
        when(base64.encode(anyByteArray(), anyInt(), anyInt())).thenReturn(expectedEncodedByte);

        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, null, null);

        int readResult = base64InputStream.read();
        byte[] decodedByte = new byte[1];
        int decodedBytesRead = base64InputStream.read(decodedByte, 0, 1);
        assertEquals(1, decodedBytesRead);
        assertArrayEquals(new byte[] { 0x01 }, decodedByte);

        assertEquals(expectedEncodedByte[0], base64InputStream.read());
        assertEquals(expectedEncodedByte[1], base64InputStream.read());
        assertNull(base64InputStream.read());
    }

    /**
     * Test read method with decode constructor.
     *
     * @throws IOException
     *             if an I/O error occurs while testing
     */
    @Test
    public void testReadWithDecodeConstructor() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x3C, 0x3F, 0x01 });
        Base64 base64 = mock(Base64.class);
        when(base64.decode(anyByteArray(), anyInt(), anyInt())).thenReturn(new byte[] { 0x01 });

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, null, null);

        int readResult = base64InputStream.read();
        byte[] decodedByte = new byte[1];
        int decodedBytesRead = base64InputStream.read(decodedByte, 0, 1);
        assertEquals(1, decodedBytesRead);
        assertArrayEquals(new byte[] { 0x01 }, decodedByte);

        assertNull(base64InputStream.read());
        assertArrayEquals(outputStream.toByteArray(), base64InputStream.in.readAllBytes());
    }

    /**
     * Test read method with decode constructor and lineLength and lineSeparator defined.
     *
     * @throws IOException
     *             if an I/O error occurs while testing
     */
    @Test
    public void testReadWithDecodeConstructorAndLineLengthAndLineSeparator() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x3C, 0x3F, 0x01, 0x0D, 0x0A });
        Base64 base64 = mock(Base64.class);
        when(base64.decode(anyByteArray(), anyInt(), anyInt())).thenReturn(new byte[] { 0x01 });

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 10, new byte[] { 0x0D, 0x0A });

        int readResult = base64InputStream.read();
        byte[] decodedByte = new byte[1];
        int decodedBytesRead = base64InputStream.read(decodedByte, 0, 1);
        assertEquals(1, decodedBytesRead);
        assertArrayEquals(new byte[] { 0x01 }, decodedByte);

        assertNull(base64InputStream.read());
        assertArrayEquals(outputStream.toByteArray(), base64InputStream.in.readAllBytes());
    }

}