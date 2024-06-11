package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

public class CompressorStreamFactoryTest {

    // Test createCompressorInputStream(InputStream in)
    @Test
    public void testCreateCompressorInputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        CompressorInputStream result = factory.createCompressorInputStream(in);
        assertNotNull(result);
        
        in = null;
        try {
            result = factory.createCompressorInputStream(in);
        } catch (IllegalArgumentException e) {
            assertNull(result);
        } catch (Exception e) {
            assertNull(result);
        }
    }
    
    // Test createCompressorInputStream(String name, InputStream in)
    @Test
    public void testCreateCompressorInputStreamWithName() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        CompressorInputStream result = factory.createCompressorInputStream("gzip", in);
        assertNotNull(result);
        
        in = null;
        try {
            result = factory.createCompressorInputStream(null, in);
        } catch (IllegalArgumentException e) {
            assertNull(result);
        } catch (Exception e) {
            assertNull(result);
        }
    }
    
    // Test createCompressorOutputStream(String name, OutputStream out)
    @Test
    public void testCreateCompressorOutputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CompressorOutputStream result = factory.createCompressorOutputStream("gzip", out);
        assertNotNull(result);
        
        out = null;
        try {
            result = factory.createCompressorOutputStream(null, out);
        } catch (IllegalArgumentException e) {
            assertNull(result);
        } catch (Exception e) {
            assertNull(result);
        }
    }

    // Test setDecompressConcatenated(boolean decompressConcatenated)
    @Test
    public void testSetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        
        factory.setDecompressConcatenated(true);
        boolean result = factory.getDecompressConcatenated();
        assertEquals(true, result);
        
        factory.setDecompressConcatenated(false);
        result = factory.getDecompressConcatenated();
        assertEquals(false, result);
    }
}