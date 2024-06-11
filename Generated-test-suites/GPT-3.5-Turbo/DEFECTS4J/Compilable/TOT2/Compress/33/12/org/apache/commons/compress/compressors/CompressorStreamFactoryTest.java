package org.apache.commons.compress.compressors;
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.CompressorException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class CompressorStreamFactoryTest {
    
    @Test
    public void testCreateCompressorInputStreamFromStream() throws CompressorException, IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        byte[] data = new byte[]{0x42, 0x5A, 0x68, 0x37, 0x32}; // BZip2 magic number
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        factory.createCompressorInputStream(in); // Should not throw exceptions
    }
    
    @Test
    public void testCreateCompressorInputStreamFromName() throws CompressorException, IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[]{});
        factory.createCompressorInputStream(CompressorStreamFactory.BZIP2, in); // Should not throw exceptions
    }
    
    @Test
    public void testCreateCompressorOutputStream() throws CompressorException, IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.createCompressorOutputStream(CompressorStreamFactory.BZIP2, System.out); // Should not throw exceptions
    }
    
    @Test
    public void testCreateCompressorInputStreamFromUnknownStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        byte[] data = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05}; // Unknown magic number
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        try {
            factory.createCompressorInputStream(in);
            fail("Expected CompressorException");
        } catch (CompressorException e) {
            assertEquals("No Compressor found for the stream signature.", e.getMessage());
        }
    }
    
    @Test
    public void testCreateCompressorOutputStreamWithUnknownName() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        try {
            factory.createCompressorOutputStream("unknown", System.out);
            fail("Expected CompressorException");
        } catch (CompressorException e) {
            assertEquals("Compressor: unknown not found.", e.getMessage());
        }
    }
}