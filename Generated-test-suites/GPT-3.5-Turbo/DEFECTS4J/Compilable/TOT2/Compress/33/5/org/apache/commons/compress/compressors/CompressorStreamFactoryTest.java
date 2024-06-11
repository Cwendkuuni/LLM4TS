package org.apache.commons.compress.compressors;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorException;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_BZip2() throws IOException, CompressorException {
        byte[] data = "test data".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        CompressorInputStream inputStream = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.BZIP2, in);
        assertNotNull(inputStream);
    }

    @Test
    public void testCreateCompressorInputStream_Gzip() throws IOException, CompressorException {
        byte[] data = "test data".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        CompressorInputStream inputStream = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.GZIP, in);
        assertNotNull(inputStream);
    }

    // Add more test cases for other compressor types

    @Test
    public void testCreateCompressorOutputStream_BZip2() throws IOException, CompressorException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CompressorOutputStream outputStream = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.BZIP2, out);
        assertNotNull(outputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_Gzip() throws IOException, CompressorException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CompressorOutputStream outputStream = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.GZIP, out);
        assertNotNull(outputStream);
    }

    // Add more test cases for other compressor types

    @Test
    public void testDecompressConcatenatedSetting() {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        assertTrue(factory.getDecompressConcatenated());
    }

}