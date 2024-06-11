package org.apache.commons.compress.compressors;
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_BZip2() throws IOException, CompressorException {
        byte[] data = {66, 90, 104, 58, 0};
        InputStream in = new ByteArrayInputStream(data);
        CompressorStreamFactory factory = new CompressorStreamFactory();
        CompressorInputStream inputStream = factory.createCompressorInputStream(in);
        assertNotNull(inputStream);
    }

    @Test
    public void testCreateCompressorInputStream_Unknown() throws IOException {
        byte[] data = {0, 0, 0, 0};
        InputStream in = new ByteArrayInputStream(data);
        CompressorStreamFactory factory = new CompressorStreamFactory();
        try {
            factory.createCompressorInputStream(in);
            fail("Expected CompressorException to be thrown");
        } catch (CompressorException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorOutputStream_Gzip() throws IOException, CompressorException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CompressorStreamFactory factory = new CompressorStreamFactory();
        CompressorOutputStream outputStream = factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, out);
        assertNotNull(outputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_Unknown() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CompressorStreamFactory factory = new CompressorStreamFactory();
        try {
            factory.createCompressorOutputStream("unknown", out);
            fail("Expected CompressorException to be thrown");
        } catch (CompressorException e) {
            // Expected
        }
    }

    @Test
    public void testDecompressConcatenatedOption() {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        assertTrue(factory.getDecompressConcatenated());
    }

}