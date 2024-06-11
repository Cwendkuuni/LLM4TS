
package org.apache.commons.compress.archivers.sevenz;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CodersTest {
    @Test
    public void testAddDecoder() throws IOException {
        // Create test data
        byte[] input = "Hello, World!".getBytes();
        byte[] password = "password".getBytes();
        byte[] compressedData = compressData(input, password, SevenZMethod.LZMA);

        // Create input stream from compressed data
        ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedData);

        // Test decoder
        InputStream result = Coders.addDecoder(inputStream, new Coder(SevenZMethod.LZMA, null), password);

        // Read decoded data from result stream
        byte[] decodedData = new byte[input.length];
        result.read(decodedData);

        // Assert equality
        assertArrayEquals(input, decodedData);
    }

    @Test(expected = IOException.class)
    public void testAddDecoderUnsupportedMethod() throws IOException {
        // Create test data
        byte[] compressedData = new byte[0];
        byte[] password = "password".getBytes();

        // Create input stream from compressed data
        ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedData);

        // Test decoder
        Coders.addDecoder(inputStream, new Coder(SevenZMethod.BZIP2, null), password);
    }

    @Test
    public void testAddEncoder() throws IOException {
        // Create test data
        byte[] input = "Hello, World!".getBytes();
        byte[] password = "password".getBytes();

        // Create output stream to store compressed data
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Test encoder
        OutputStream result = Coders.addEncoder(outputStream, SevenZMethod.LZMA, password);

        // Write input data to encoder
        result.write(input);
        result.close();

        // Get compressed data from output stream
        byte[] compressedData = outputStream.toByteArray();

        // Assert not empty
        assertTrue(compressedData.length > 0);
    }

    @Test(expected = IOException.class)
    public void testAddEncoderUnsupportedMethod() throws IOException {
        // Create test data
        byte[] password = "password".getBytes();

        // Create output stream to store compressed data
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Test encoder
        Coders.addEncoder(outputStream, SevenZMethod.BZIP2, password);
    }

    private byte[] compressData(byte[] input, byte[] password, SevenZMethod method) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream encoder = Coders.addEncoder(outputStream, method, password);
        encoder.write(input);
        encoder.close();
        return outputStream.toByteArray();
    }
}
