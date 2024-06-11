package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodecInputStream.EOF;
import static org.apache.commons.codec.binary.BaseNCodecInputStream.doEncode;
import static org.apache.commons.codec.binary.BaseNCodecInputStream.markSupported;
import static org.apache.commons.codec.binary.BaseNCodecInputStream.read;
import static org.apache.commons.codec.binary.BaseNCodecInputStream.read;
import static org.apache.commons.codec.binary.BaseNCodecInputStream.skip;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class BaseNCodecInputStreamTest {

    @Test
    public void testMarkSupported() {
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[]{}), new BaseNCodec(), doEncode);
        assertFalse(inputStream.markSupported());
    }

    @Test
    public void testReadSingleByte() throws IOException {
        final byte[] inputData = {0x61, 0x61, 0x61, 0x61, 0x61, 0x61, 0x61, 0x61, 0x61};
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new BaseNCodec(), doEncode);

        final byte[] outputData = new byte[10];

        for (int i = 0; i < 10; i++) {
            final int read = baseNCodecInputStream.read(outputData, 0, 1);
            assertEquals(1, read);
            assertEquals((byte) i + 65, outputData[0]);
        }

        assertEquals(BaseNCodecInputStream.EOF, baseNCodecInputStream.read(outputData, 0, 1));
    }

    @Test
    public void testReadMultipleBytes() throws IOException {
        final byte[] inputData = {0x61, 0x61, 0x61, 0x61, 0x61, 0x61, 0x61, 0x61, 0x61, 0x61, 0x61, 0x61, 0x61, 0x61, 0x61, 0x61, 0x61, 0x61, 0x61};
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new BaseNCodec(), doEncode);

        final byte[] outputData = new byte[11];

        final int numBytesToRead = 5;
        final int read = baseNCodecInputStream.read(outputData, 0, numBytesToRead);
        assertEquals(numBytesToRead, read);
        for (int i = 0; i < numBytesToRead; i++) {
            assertEquals((byte) (i + 65), outputData[i]);
        }

        final int remainingBytesToRead = 7;
        final int remainingOutputDataLength = 11 - numBytesToRead;
        final int index = 0;
        for (int i = 0; i < remainingBytesToRead; i++) {
            final int read = baseNCodecInputStream.read(outputData, index, 1);
            index++;
            assertEquals(1, read);
            assertEquals((byte) (numBytesToRead + i + 65), outputData[index - 1]);
        }

        assertEquals(BaseNCodecInputStream.EOF, baseNCodecInputStream.read(outputData, 0, 1));
    }

    @Test
    public void testSkip() throws IOException {
        final byte[] inputData = {0x61, 0x61, 0x61, 0x61, 0x61, 0x61, 0x61, 0x61, 0x61, 0x61, 0x61};
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new BaseNCodec(), doEncode);

        final int numBytesToSkip = 5;
        final int skippedBytes = baseNCodecInputStream.skip(numBytesToSkip);
        assertEquals(numBytesToSkip, skippedBytes);

        final int remainingBytesToRead = 5;
        final int read = baseNCodecInputStream.read(new byte[remainingBytesToRead], 0, remainingBytesToRead);
        assertEquals(remainingBytesToRead, read);
    }

    @Test
    public void testClose() throws IOException {
        final byte[] inputData = {0x61, 0x61, 0x61, 0x61, 0x61, 0x61, 0x61, 0x61, 0x61, 0x61, 0x61};
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new BaseNCodec(), doEncode);

        // Test reading all bytes
        final byte[] outputData = new byte[11];
        final int numBytesToRead = 5;
        int read = baseNCodecInputStream.read(outputData, 0, numBytesToRead);
        assertEquals(numBytesToRead, read);
        for (int i = 0; i < numBytesToRead; i++) {
            assertEquals((byte) (i + 65), outputData[i]);
        }

        // Test reading EOF
        assertEquals(BaseNCodecInputStream.EOF, baseNCodecInputStream.read(outputData, 0, 1));

        // Test closing the stream
        baseNCodecInputStream.close();
        assertEquals(BaseNCodecInputStream.EOF, baseNCodecInputStream.read(outputData, 0, 1));
    }

    @Test
    public void testReadAndDecodeData() throws IOException {
        final byte[] inputData = {0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x3A};
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        final BaseNCodec baseNCodec = new BaseNCodec();
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final byte[] outputData = outputStream.toByteArray();

        // Test decoding data
        final int numBytesToDecode = 5;
        final int read = baseNCodecInputStream.read(outputData, 0, numBytesToDecode * 4);
        assertEquals(numBytesToDecode * 4, read);
        final String decodedData = new String(outputData, 0, numBytesToDecode * 4);
        assertEquals("012345", decodedData);

        // Test decoding all data
        final int remainingBytesToDecode = inputData.length - numBytesToDecode;
        final int remainingOutputDataLength = outputData.length - numBytesToDecode * 4;
        final int index = numBytesToDecode * 4;
        for (int i = 0; i < remainingBytesToDecode; i++) {
            final int read = baseNCodecInputStream.read(outputData, index, 4);
            index += read;
        }

        final String decodedAllData = new String(outputData, 0, outputData.length);
        assertEquals("0123456789", decodedAllData);
    }

    @Test
    public void testEncodeData() throws IOException {
        final String inputData = "0123456789";
        final byte[] encodedData = inputData.getBytes();
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedData);
        final BaseNCodec baseNCodec = new BaseNCodec();
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Test encoding data
        final int numBytesToEncode = 5;
        final int read = baseNCodecInputStream.read(new byte[numBytesToEncode], 0, numBytesToEncode);
        assertEquals(numBytesToEncode, read);
        final String encodedDataString = new String(new byte[] { read(baseNCodecInputStream, 0, 1), read(baseNCodecInputStream, 0, 1), read(baseNCodecInputStream, 0, 1), read(baseNCodecInputStream, 0, 1), read(baseNCodecInputStream, 0, 1) });
        assertEquals("303132333435", encodedDataString);

        // Test encoding all data
        final int remainingBytesToEncode = inputData.length() - numBytesToEncode;
        final int remainingOutputDataLength = outputStream.size() - numBytesToEncode;
        final int index = numBytesToEncode;
        for (int i = 0; i < remainingBytesToEncode; i++) {
            final int read = baseNCodecInputStream.read(outputStream.toByteArray(), index, 1);
            index++;
        }

        final String encodedAllData = new String(outputStream.toByteArray());
        assertEquals("3031323334356789", encodedAllData);
    }
}