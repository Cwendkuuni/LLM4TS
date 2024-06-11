package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.DeflateParameters;
import org.apache.commons.compress.utils.IOUtils;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testCreateWithInputStream() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x78, 0x5E, 0x06, 0x08, 0x00, 0x14, 0x00, 0x00, 0x00, 0x0F, 0x40, 0x00, 0x14, 0x09, 0x00, 0x60, 0x52, 0x61, 0x79, 0x01, 0x00});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        assertNotNull(deflateCompressorInputStream);
    }

    @Test
    public void testCreateWithInputStreamAndParameters() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x78, 0x5E, 0x06, 0x08, 0x00, 0x14, 0x00, 0x00, 0x00, 0x0F, 0x40, 0x00, 0x14, 0x09, 0x00, 0x60, 0x52, 0x61, 0x79, 0x01, 0x00});
        DeflateParameters parameters = new DeflateParameters();
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, parameters);
        assertNotNull(deflateCompressorInputStream);
        assertEquals(false, parameters.isWithZlibHeader());
    }

    @Test
    public void testReadByte() throws IOException {
        byte[] inputData = new byte[]{0x01, 0x02, 0x03};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        byte readByte = (byte) deflateCompressorInputStream.read();
        assertEquals(inputData[0], readByte);

        readByte = (byte) deflateCompressorInputStream.read();
        assertEquals(inputData[1], readByte);

        readByte = (byte) deflateCompressorInputStream.read();
        assertEquals(inputData[2], readByte);

        readByte = (byte) deflateCompressorInputStream.read(); // read past the end of the input stream
        assertEquals(-1, readByte);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] inputData = new byte[]{0x01, 0x02, 0x03};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] readData = new byte[3];
        int readLength = deflateCompressorInputStream.read(readData, 0, 3);
        assertEquals(3, readLength);

        for (int i = 0; i < readLength; i++) {
            assertEquals(inputData[i], readData[i]);
        }

        readLength = deflateCompressorInputStream.read(readData, 0, 3); // read past the end of the input stream
        assertEquals(-1, readLength);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] inputData = new byte[]{0x01, 0x02, 0x03};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        long skippedBytes = deflateCompressorInputStream.skip(10);
        assertEquals(0, skippedBytes);

        skippedBytes = deflateCompressorInputStream.skip(inputData.length - 3);
        assertEquals(3, skippedBytes);

        skippedBytes = deflateCompressorInputStream.skip(1); // skip past the end of the input stream
        assertEquals(0, skippedBytes);
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] inputData = new byte[]{0x01, 0x02, 0x03};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        assertEquals(inputData.length, deflateCompressorInputStream.available());
    }

    @Test
    public void testClose() throws IOException {
        byte[] inputData = new byte[]{0x01, 0x02, 0x03};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        deflateCompressorInputStream.close();

        try {
            deflateCompressorInputStream.read();
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testSignatureWithZlibHeader() {
        byte[] inputData = new byte[]{0x78, 0x5E, 0x06, 0x08, 0x00, 0x14, 0x00, 0x00, 0x00, 0x0F, 0x40, 0x00, 0x14, 0x09, 0x00, 0x60, 0x52, 0x61, 0x79, 0x01, 0x00};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        assertTrue(deflateCompressorInputStream.checkSignature(inputData, inputData.length));
    }

    @Test
    public void testSignatureWithoutZlibHeader() {
        byte[] inputData = new byte[]{0x01, 0x02, 0x03};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());

        assertFalse(deflateCompressorInputStream.checkSignature(inputData, inputData.length));
    }

    @Test
    public void testReadByteArrayWithZlibHeader() throws IOException {
        byte[] inputData = new byte[]{0x78, 0x5E, 0x06, 0x08, 0x00, 0x14, 0x00, 0x00, 0x00, 0x0F, 0x40, 0x00, 0x14, 0x09, 0x00, 0x60, 0x52, 0x61, 0x79, 0x01, 0x00, 0x01, 0x02, 0x03};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] readData = new byte[3];
        int readLength = deflateCompressorInputStream.read(readData, 0, 3);
        assertEquals(3, readLength);

        for (int i = 0; i < readLength; i++) {
            assertEquals(inputData[i + 13], readData[i]);
        }
    }
}