package org.apache.commons.compress.compressors;

import org.junit.Test;

import static org.junit.Assert.*;

public class CompressorStreamFactoryTest {

    private final CompressorStreamFactory factory = new CompressorStreamFactory();

    @Test
    public void testCreateCompressorInputStreamWithNullStream() {
        try {
            factory.createCompressorInputStream(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Stream must not be null.", e.getMessage());
        }
    }

    //TODO: Add more test cases to test all compressor input stream creation methods

    @Test
    public void testCreateCompressorOutputStreamWithNullName() {
        try {
            factory.createCompressorOutputStream(null, System.out);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Compressor name and stream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateCompressorOutputStreamWithNullStream() {
        try {
            factory.createCompressorOutputStream("gzip", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Compressor name and stream must not be null.", e.getMessage());
        }
    }

    //TODO: Add more test cases to test all compressor output stream creation methods

    //TODO: Add more test cases to test the createCompressorInputStream method

}