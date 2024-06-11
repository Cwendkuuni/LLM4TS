package org.apache.commons.compress.compressors;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import static org.junit.Assert.*;

public class CompressorStreamFactoryTest {

    private CompressorStreamFactory compressorStreamFactory;

    @Before
    public void setUp() {
        compressorStreamFactory = new CompressorStreamFactory();
    }

    @Test
    public void testCreateCompressorInputStream() throws Exception {
        byte[] input = "test input".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);

        CompressorInputStream compressorInputStream = compressorStreamFactory.createCompressorInputStream(inputStream);

        assertNotNull(compressorInputStream);

        byte[] output = new byte[input.length];
        compressorInputStream.read(output);
        compressorInputStream.close();

        assertArrayEquals(input, output);
    }

    @Test
    public void testCreateCompressorInputStreamWithName() throws Exception {
        byte[] input = "test input".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);

        CompressorInputStream compressorInputStream = compressorStreamFactory.createCompressorInputStream("gz", inputStream);

        assertNotNull(compressorInputStream);

        byte[] output = new byte[input.length];
        compressorInputStream.read(output);
        compressorInputStream.close();

        assertArrayEquals(input, output);
    }

    @Test
    public void testCreateCompressorOutputStream() throws Exception {
        byte[] input = "test input".getBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        CompressorOutputStream compressorOutputStream = compressorStreamFactory.createCompressorOutputStream("gz", outputStream);

        assertNotNull(compressorOutputStream);

        compressorOutputStream.write(input);
        compressorOutputStream.close();

        byte[] output = outputStream.toByteArray();

        assertArrayEquals(input, output);
    }

    @Test
    public void testGetDecompressConcatenated() {
        boolean decompressConcatenated = compressorStreamFactory.getDecompressConcatenated();
        assertFalse(decompressConcatenated);
    }

}