import static org.junit.Assert.*;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] input = {0x48, 0x65, 0x6C, 0x6C, 0x6F}; // "Hello"
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        int result = deflateInputStream.read();
        
        assertEquals('H', result);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] input = {0x48, 0x65, 0x6C, 0x6C, 0x6F}; // "Hello"
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] output = new byte[5];

        int result = deflateInputStream.read(output, 0, 5);
        
        assertArrayEquals(input, output);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] input = {0x48, 0x65, 0x6C, 0x6C, 0x6F}; // "Hello"
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        long result = deflateInputStream.skip(2);
        
        assertEquals(2, result);
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] input = {0x48, 0x65, 0x6C, 0x6C, 0x6F}; // "Hello"
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        int result = deflateInputStream.available();
        
        assertEquals(5, result);
    }

    @Test
    public void testClose() throws IOException {
        byte[] input = {0x48, 0x65, 0x6C, 0x6C, 0x6F}; // "Hello"
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        deflateInputStream.close();
        
        try {
            deflateInputStream.read();
            fail("An IOException should have been thrown after closing the stream");
        } catch (IOException e) {
            // Exception thrown as expected
        }
    }

    @Test
    public void testSignatureCheck() {
        byte[] signature = {31, -117, 8}; // zlib header bytes
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(null);

        boolean result = deflateInputStream.checkSignature(signature, 3);
        
        assertTrue(result);
    }
}