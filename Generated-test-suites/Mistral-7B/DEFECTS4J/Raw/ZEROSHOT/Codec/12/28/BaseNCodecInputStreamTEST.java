package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.apache.commons.codec.binary.BaseNCodec.NO_MORE_BYTES;
import static org.apache.commons.codec.binary.BaseNCodecInputStream.NO_MORE_BYTES_EXCEPTION_MESSAGE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Test;

/**
 * Unit tests for {@link BaseNCodecInputStream}.
 *
 * @author <a href="mailto:stefan.birkner@gmx.de">Stefan Birkner</a>
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test for {@link BaseNCodecInputStream#BaseNCodecInputStream(InputStream, BaseNCodec, boolean)}.
     */
    @Test
    public void testConstructor() throws IOException {
        InputStream is = new ByteArrayInputStream("test".getBytes());
        BaseNCodec codec = new Base32();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(is, codec, false);
        assertNotNull(inputStream);
        assertEquals(is, inputStream.in);
        assertEquals(codec, inputStream.baseNCodec);
        assertFalse(inputStream.doEncode);
    }

    /**
     * Test for {@link BaseNCodecInputStream#BaseNCodecInputStream(InputStream, BaseNCodec, boolean)}.
     */
    @Test
    public void testConstructorEncode() throws IOException {
        InputStream is = new ByteArrayInputStream("test".getBytes());
        BaseNCodec codec = new Base32();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(is, codec, true);
        assertNotNull(inputStream);
        assertEquals(is, inputStream.in);
        assertEquals(codec, inputStream.baseNCodec);
        assertTrue(inputStream.doEncode);
    }

    /**
     * Test for {@link BaseNCodecInputStream#read()}.
     */
    @Test
    public void testRead() throws IOException {
        InputStream is = new ByteArrayInputStream("test".getBytes());
        BaseNCodec codec = new Base32();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(is, codec, false);

        byte readByte = inputStream.read();
        assertEquals(0, readByte);

        readByte = inputStream.read();
        assertEquals('t', readByte);

        readByte = inputStream.read();
        assertEquals('e', readByte);

        readByte = inputStream.read();
        assertEquals('s', readByte);

        readByte = inputStream.read();
        assertEquals('t', readByte);

        readByte = inputStream.read();
        assertEquals(EOF, readByte);
    }

    /**
     * Test for {@link BaseNCodecInputStream#read(byte[], int, int)}.
     */
    @Test
    public void testReadByteArray() throws IOException {
        InputStream is = new ByteArrayInputStream("test".getBytes());
        BaseNCodec codec = new Base32();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(is, codec, false);

        byte[] buffer = new byte[3];

        int readLength = inputStream.read(buffer, 0, 3);
        assertEquals(3, readLength);
        assertEquals('t', buffer[0]);
        assertEquals('e', buffer[1]);
        assertEquals('s', buffer[2]);

        assertEquals(EOF, inputStream.read(buffer, 0, 1));
    }

    /**
     * Test for {@link BaseNCodecInputStream#read(byte[], int, int)}.
     */
    @Test
    public void testReadByteArrayDecode() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[]{'S', 't', 'e', 's', 't'});
        BaseNCodec codec = new Base32();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(is, codec, false);

        byte[] buffer = new byte[5];

        int readLength = inputStream.read(buffer, 0, 5);
        assertEquals(5, readLength);
        assertEquals('S', buffer[0]);
        assertEquals('t', buffer[1]);
        assertEquals('e', buffer[2]);
        assertEquals('s', buffer[3]);
        assertEquals('t', buffer[4]);

        assertEquals(EOF, inputStream.read(buffer, 0, 1));
    }

    /**
     * Test for {@link BaseNCodecInputStream#read(byte[], int, int)}.
     */
    @Test
    public void testReadByteArrayEncode() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        BaseNCodec codec = new Base32();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(os.toByteArray()), codec, true);

        byte[] buffer = new byte[5];
        os.write("test".getBytes());
        os.flush();

        int readLength = inputStream.read(buffer, 0, 5);
        assertEquals(5, readLength);
        assertEquals('t', buffer[0]);
        assertEquals('e', buffer[1]);
        assertEquals('s', buffer[2]);
        assertEquals('t', buffer[3]);
        assertEquals(' ', buffer[4]);

        assertEquals(EOF, inputStream.read(buffer, 0, 1));
    }

    /**
     * Test for {@link BaseNCodecInputStream#markSupported()}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testMarkSupported() throws IOException {
        InputStream is = new ByteArrayInputStream("test".getBytes());
        BaseNCodec codec = new Base32();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(is, codec, false);
        inputStream.markSupported();
    }

    /**
     * Test for {@link BaseNCodecInputStream#skip(long)}.
     */
    @Test
    public void testSkip() throws IOException {
        InputStream is = new ByteArrayInputStream("test".getBytes());
        BaseNCodec codec = new Base32();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(is, codec, false);

        long skippedBytes = inputStream.skip(2);
        assertEquals(2, skippedBytes);

        int readByte = inputStream.read();
        assertEquals('s', readByte);

        skippedBytes = inputStream.skip(11);
        assertEquals(NO_MORE_BYTES, skippedBytes);

        byte[] buffer = new byte[3];
        int readLength = inputStream.read(buffer, 0, 3);
        assertEquals(1, readLength);
        assertEquals(EOF, buffer[0]);

        // test exception
        inputStream.skip(1);
    }

    /**
     * Test for {@link BaseNCodecInputStream#available()}.
     */
    @Test
    public void testAvailable() throws IOException {
        InputStream is = new ByteArrayInputStream("test".getBytes());
        BaseNCodec codec = new Base32();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(is, codec, false);

        assertEquals(4, inputStream.available());

        byte[] buffer = new byte[3];
        int readLength = inputStream.read(buffer, 0, 3);
        assertEquals(3, readLength);

        assertEquals(1, inputStream.available());

        readLength = inputStream.read(buffer, 0, 1);
        assertEquals(EOF, buffer[0]);

        assertEquals(0, inputStream.available());
    }

    /**
     * Test for {@link BaseNCodecInputStream#close()}.
     */
    @Test
    public void testClose() throws IOException {
        InputStream is = new ByteArrayInputStream("test".getBytes());
        BaseNCodec codec = new Base32();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(is, codec, false);

        inputStream.close();
        try {
            inputStream.read();
            fail("IOException expected");
        } catch (IOException e) {
            // expected
        }
    }

    /**
     * Test for {@link BaseNCodecInputStream#read(byte[], int, int)}.
     */
    @Test
    public void testReadFailsWithNoMoreBytes() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[0][0]);
        BaseNCodec codec = new Base32();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(is, codec, false);

        byte[] buffer = new byte[3];
        int readLength = inputStream.read(buffer, 0, 3);
        assertEquals(NO_MORE_BYTES, readLength);

        try {
            inputStream.read(buffer, 0, 1);
            fail("IOException expected");
        } catch (IOException e) {
            assertEquals(NO_MORE_BYTES_EXCEPTION_MESSAGE, e.getMessage());
        }
    }

    /**
     * Test for {@link BaseNCodecInputStream#read(byte[], int, int)}.
     */
    @Test
    public void testReadWithNoData() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[0][0]);
        BaseNCodec codec = new Base32();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(is, codec, false);

        byte[] buffer = new byte[3];
        int readLength = inputStream.read(buffer, 0, 3);
        assertEquals(0, readLength);

        try {
            inputStream.read(buffer, 0, 1);
            fail("IOException expected");
        } catch (IOException e) {
            assertEquals(NO_MORE_BYTES_EXCEPTION_MESSAGE, e.getMessage());
        }

        try {
            inputStream.read();
            fail("IOException expected");
        } catch (IOException e) {
            assertEquals(NO_MORE_BYTES_EXCEPTION_MESSAGE, e.getMessage());
        }
    }
}