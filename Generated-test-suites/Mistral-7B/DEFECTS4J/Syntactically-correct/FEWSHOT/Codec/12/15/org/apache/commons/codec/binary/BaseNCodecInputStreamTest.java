package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodecInputStream.EOF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class BaseNCodecInputStreamTest {

    @Test
    public void testBasicFunctionality() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream("TestData".getBytes());
        BaseNCodecInputStream testInput = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] readByteArray = new byte[5];
        int bytesRead = 0;
        while (bytesRead < readByteArray.length) {
            int numBytes = testInput.read(readByteArray, bytesRead, readByteArray.length - bytesRead);
            if (numBytes < 0) {
                break;
            }
            bytesRead += numBytes;
        }

        assertEquals(5, bytesRead);
        for (int i = 0; i < bytesRead; i++) {
            assertEquals("TestData".charAt(i), (char) readByteArray[i]);
        }
    }

    @Test
    public void testReadEncodedData() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("-----".getBytes());
        baseNCodec.encode("TestData".getBytes(), outputStream.toByteArray(), outputStream.size());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        BaseNCodecInputStream testInput = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        byte[] readByteArray = new byte[5];
        int bytesRead = 0;
        while (bytesRead < readByteArray.length) {
            int numBytes = testInput.read(readByteArray, bytesRead, readByteArray.length - bytesRead);
            if (numBytes < 0) {
                break;
            }
            bytesRead += numBytes;
        }

        assertEquals(5, bytesRead);
        for (int i = 0; i < bytesRead; i++) {
            assertEquals("TestData".charAt(i), (char) readByteArray[i]);
        }
    }

    @Test
    public void testReadDecodedData() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("TestData".getBytes());
        baseNCodec.decode(outputStream.toByteArray(), 0, outputStream.size());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        BaseNCodecInputStream testInput = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] readByteArray = new byte[5];
        int bytesRead = 0;
        while (bytesRead < readByteArray.length) {
            int numBytes = testInput.read(readByteArray, bytesRead, readByteArray.length - bytesRead);
            if (numBytes < 0) {
                break;
            }
            bytesRead += numBytes;
        }

        assertEquals(5, bytesRead);
        for (int i = 0; i < bytesRead; i++) {
            assertEquals("TestData".charAt(i), (char) readByteArray[i]);
        }
    }

    @Test
    public void testMarkSupported() {
        BaseNCodecInputStream testInput = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), new BaseNCodec(), false);
        assertFalse(testInput.markSupported());
    }

    @Test
    public void testSkip() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("TestData".getBytes());
        baseNCodec.decode(outputStream.toByteArray(), 0, outputStream.size());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        BaseNCodecInputStream testInput = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        long skipAmount = 5;
        int bytesSkipped = 0;
        while (bytesSkipped < (int) skipAmount) {
            int numBytesSkipped = testInput.skip(skipAmount - bytesSkipped);
            if (numBytesSkipped < 0) {
                break;
            }
            bytesSkipped += numBytesSkipped;
        }

        assertEquals(skipAmount, bytesSkipped);
    }

    @Test
    public void testHasData() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("TestData".getBytes());
        baseNCodec.decode(outputStream.toByteArray(), 0, outputStream.size());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        BaseNCodecInputStream testInput = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        assertTrue(testInput.hasData());

        byte[] readByteArray = new byte[5];
        int bytesRead = testInput.read(readByteArray, 0, readByteArray.length);
        assertTrue(bytesRead > 0);

        assertTrue(testInput.hasData());

        bytesRead = testInput.read(readByteArray, 0, readByteArray.length);
        assertTrue(bytesRead > 0);

        assertTrue(testInput.hasData());

        bytesRead = testInput.read(readByteArray, 0, readByteArray.length);
        assertTrue(bytesRead > 0);

        assertTrue(testInput.hasData());

        bytesRead = testInput.read(readByteArray, 0, readByteArray.length);
        assertTrue(bytesRead == EOF);

        assertFalse(testInput.hasData());
    }

    @Test
    public void testReadLimit() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("TestData".getBytes());
        baseNCodec.decode(outputStream.toByteArray(), 0, outputStream.size());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        BaseNCodecInputStream testInput = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] readByteArray = new byte[10];
        int bytesRead = testInput.read(readByteArray, 0, readByteArray.length);
        assertEquals(5, bytesRead);

        assertEquals("TestData".charAt(0), (char) readByteArray[0]);
        assertEquals("TestData".charAt(1), (char) readByteArray[1]);
        assertEquals("TestData".charAt(2), (char) readByteArray[2]);
        assertEquals("TestData".charAt(3), (char) readByteArray[3]);
        assertEquals("TestData".charAt(4), (char) readByteArray[4]);

        bytesRead = testInput.read(readByteArray, 5, readByteArray.length - 5);
        assertEquals(5, bytesRead);

        assertEquals("TestData".charAt(5), (char) readByteArray[5]);
        assertEquals("TestData".charAt(6), (char) readByteArray[6]);
        assertEquals("TestData".charAt(7), (char) readByteArray[7]);
        assertEquals("TestData".charAt(8), (char) readByteArray[8]);
        assertEquals("TestData".charAt(9), (char) readByteArray[9]);

        assertNull(testInput.read(readByteArray, 0, readByteArray.length));
    }
}