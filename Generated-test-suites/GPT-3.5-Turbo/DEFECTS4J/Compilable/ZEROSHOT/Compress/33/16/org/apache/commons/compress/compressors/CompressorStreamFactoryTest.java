package org.apache.commons.compress.compressors;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.*;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream() {
        try {
            CompressorStreamFactory factory = new CompressorStreamFactory();
            InputStream in = new ByteArrayInputStream(new byte[10]);
            CompressorInputStream compressIn = factory.createCompressorInputStream("gzip", in);
            assertNotNull(compressIn);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testCreateCompressorOutputStream() {
        try {
            CompressorStreamFactory factory = new CompressorStreamFactory();
            OutputStream out = new ByteArrayOutputStream();
            CompressorOutputStream compressOut = factory.createCompressorOutputStream("gzip", out);
            assertNotNull(compressOut);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

}