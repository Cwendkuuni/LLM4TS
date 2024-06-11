import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        byte[] data = "Test".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        CompressorInputStream stream = factory.createCompressorInputStream("gzip", in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int bytesRead;
        byte[] buffer = new byte[1024];
        while ((bytesRead = stream.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        stream.close();
        in.close();
        out.close();
        byte[] decompressedData = out.toByteArray();
        String decompressedString = new String(decompressedData);
        Assert.assertEquals("Test", decompressedString);
    }

    @Test
    public void testCreateCompressorOutputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CompressorOutputStream stream = factory.createCompressorOutputStream("gzip", out);
        String data = "Test";
        stream.write(data.getBytes());
        stream.close();
        out.close();
        byte[] compressedData = out.toByteArray();
        Assert.assertTrue(compressedData.length > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStreamNullName() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        ByteArrayInputStream in = new ByteArrayInputStream("Test".getBytes());
        CompressorInputStream stream = factory.createCompressorInputStream(null, in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStreamNullStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        CompressorInputStream stream = factory.createCompressorInputStream("gzip", null);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStreamUnknownName() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        ByteArrayInputStream in = new ByteArrayInputStream("Test".getBytes());
        CompressorInputStream stream = factory.createCompressorInputStream("unknown", in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorOutputStreamNullName() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CompressorOutputStream stream = factory.createCompressorOutputStream(null, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorOutputStreamNullStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        CompressorOutputStream stream = factory.createCompressorOutputStream("gzip", null);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStreamUnknownName() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CompressorOutputStream stream = factory.createCompressorOutputStream("unknown", out);
    }
}