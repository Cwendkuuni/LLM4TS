package org.apache.commons.compress.compressors.deflate;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import static org.junit.Assert.*;

public class DeflateCompressorInputStreamTest {

    private DeflateCompressorInputStream compressStream;
    private byte[] compressedData;

    @Before
    public void setup() throws IOException {
        String testData = "This is a test string.";
        byte[] testDataBytes = testData.getBytes();

        // Compress the test data
        Deflater deflater = new Deflater();
        deflater.setInput(testDataBytes);
        deflater.finish();
        byte[] buffer = new byte[1024];
        int compressedDataSize = deflater.deflate(buffer);
        compressedData = new byte[compressedDataSize];
        System.arraycopy(buffer, 0, compressedData, 0, compressedDataSize);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedData);
        compressStream = new DeflateCompressorInputStream(inputStream);
    }

    @Test
    public void testRead() throws IOException {
        int readData = compressStream.read(); // Reading a single byte
        assertEquals('T', readData);
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = compressStream.read(buffer, 0, buffer.length);

        assertEquals(10, bytesRead);
        assertEquals('T', buffer[0]);
        assertEquals('h', buffer[1]);
        assertEquals('i', buffer[2]);
        assertEquals('s', buffer[3]);
        assertEquals(' ', buffer[4]);
        assertEquals('i', buffer[5]);
        assertEquals('s', buffer[6]);
        assertEquals(' ', buffer[7]);
        assertEquals('a', buffer[8]);
        assertEquals(' ', buffer[9]);
    }

    @Test
    public void testSkip() throws IOException {
        long skipBytes = compressStream.skip(5); // Skipping 5 bytes
        assertEquals(5, skipBytes);

        int readData = compressStream.read(); // Reading a single byte after skip
        assertEquals(' ', readData);
    }

    @Test
    public void testAvailable() throws IOException {
        int availableBytes = compressStream.available();
        assertEquals(compressedData.length, availableBytes);
    }

    @Test
    public void testClose() throws IOException {
        compressStream.close();
        int readData = compressStream.read(); // Attempting to read after closing the stream

        assertEquals(-1, readData);
    }

    @Test
    public void testDeflateInputStreamWithZlibHeader() throws IOException {
        DeflateParameters parameters = new DeflateParameters();
        parameters.setWithZlibHeader(true);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream compressStreamWithHeader = new DeflateCompressorInputStream(inputStream, parameters);
        
        byte[] buffer = new byte[10];
        int bytesRead = compressStreamWithHeader.read(buffer, 0, buffer.length);

        assertEquals(10, bytesRead);
        assertEquals('T', buffer[0]);
        assertEquals('h', buffer[1]);
        assertEquals('i', buffer[2]);
        assertEquals('s', buffer[3]);
        assertEquals(' ', buffer[4]);
        assertEquals('i', buffer[5]);
        assertEquals('s', buffer[6]);
        assertEquals(' ', buffer[7]);
        assertEquals('a', buffer[8]);
        assertEquals(' ', buffer[9]);
        
        compressStreamWithHeader.close();
    }
}