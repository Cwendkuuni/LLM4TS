import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMACompressorInputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorInputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorOutputStream;
import org.apache.commons.compress.compressors.snappy.FramedSnappyCompressorInputStream;
import org.apache.commons.compress.compressors.snappy.SnappyCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.junit.Test;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_WithValidInputStream() throws IOException, CompressorException {
        byte[] data = {0x42, 0x5A, 0x68};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        
        CompressorStreamFactory factory = new CompressorStreamFactory();
        BZip2CompressorInputStream compressorInputStream = factory.createCompressorInputStream(inputStream);
        
        assertNotNull(compressorInputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_WithValidOutputStream() throws IOException, CompressorException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputSream();
        
        CompressorStreamFactory factory = new CompressorStreamFactory();
        BZip2CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.BZIP2, outputStream);
        
        assertNotNull(compressorOutputStream);
    }

    @Test
    public void testCreateCompressorInputStream_WithInvalidInputStream() {
        InputStream inputStream = null;
        
        CompressorStreamFactory factory = new CompressorStreamFactory();
        
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createCompressorInputStream(inputStream);
        });
    }

    @Test
    public void testCreateCompressorOutputStream_WithInvalidOutputStream() {
        OutputStream outputStream = null;

        CompressorStreamFactory factory = new CompressorStreamFactory();
        
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createCompressorOutputStream(CompressorStreamFactory.BZIP2, outputStream);
        });
    }

    @Test
    public void testCreateCompressorInputStream_WithUnknownCompressor() {
        byte[] data = {0x42, 0x5A, 0x68};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        
        CompressorStreamFactory factory = new CompressorStreamFactory();
        
        assertThrows(CompressorException.class, () -> {
            factory.createCompressorInputStream("unknown", inputStream);
        });
    }

    @Test
    public void testCreateCompressorOutputStream_WithUnknownCompressor() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        CompressorStreamFactory factory = new CompressorStreamFactory();
        
        assertThrows(CompressorException.class, () -> {
            factory.createCompressorOutputStream("unknown", outputStream);
        });
    }

}