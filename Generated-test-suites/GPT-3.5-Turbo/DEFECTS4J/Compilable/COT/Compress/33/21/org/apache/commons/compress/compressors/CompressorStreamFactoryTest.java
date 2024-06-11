package org.apache.commons.compress.compressors;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream() throws IOException, CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        
        // Test valid input stream with known compressor
        InputStream in = new ByteArrayInputStream(new byte[0]);
        CompressorInputStream cis = factory.createCompressorInputStream(CompressorStreamFactory.GZIP, in);
        cis.close();
        
        // Test valid input stream with unknown compressor
        try {
            factory.createCompressorInputStream("unknown", in);
            assert false; // Expected CompressorException
        } catch (CompressorException e) {
            assert true; // Expected CompressorException
        }
        
        // Test null input stream
        try {
            factory.createCompressorInputStream(CompressorStreamFactory.GZIP, null);
            assert false; // Expected IllegalArgumentException
        } catch (IllegalArgumentException e) {
            assert true; // Expected IllegalArgumentException
        }
    }

    @Test
    public void testCreateCompressorOutputStream() throws IOException, CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        
        // Test valid output stream with known compressor
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CompressorOutputStream cos = factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, out);
        cos.close();
        
        // Test valid output stream with unknown compressor
        try {
            factory.createCompressorOutputStream("unknown", out);
            assert false; // Expected CompressorException
        } catch (CompressorException e) {
            assert true; // Expected CompressorException
        }
        
        // Test null output stream
        try {
            factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, null);
            assert false; // Expected IllegalArgumentException
        } catch (IllegalArgumentException e) {
            assert true; // Expected IllegalArgumentException
        }
    }

    @Test
    public void testSetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        
        // Test setting decompressConcatenated to true
        factory.setDecompressConcatenated(true);
        assert factory.getDecompressConcatenated() == true;
        
        // Test setting decompressConcatenated to false
        factory.setDecompressConcatenated(false);
        assert factory.getDecompressConcatenated() == false;
    }
}