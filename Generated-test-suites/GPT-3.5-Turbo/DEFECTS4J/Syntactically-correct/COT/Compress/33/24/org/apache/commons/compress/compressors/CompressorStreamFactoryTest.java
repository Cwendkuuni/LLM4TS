import org.junit.Test;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static org.junit.Assert.*;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStreamGZIP() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[1024]);
        CompressorInputStream stream = factory.createCompressorInputStream(CompressorStreamFactory.GZIP, input);
        assertNotNull(stream);
        stream.close();
    }

    @Test
    public void testCreateCompressorInputStreamBZIP2() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[1024]);
        CompressorInputStream stream = factory.createCompressorInputStream(CompressorStreamFactory.BZIP2, input);
        assertNotNull(stream);
        stream.close();
    }

    @Test
    public void testCreateCompressorInputStreamXZ() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[1024]);
        CompressorInputStream stream = factory.createCompressorInputStream(CompressorStreamFactory.XZ, input);
        assertNotNull(stream);
        stream.close();
    }

    @Test
    public void testCreateCompressorInputStreamPACK200() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[1024]);
        CompressorInputStream stream = factory.createCompressorInputStream(CompressorStreamFactory.PACK200, input);
        assertNotNull(stream);
        stream.close();
    }

    @Test
    public void testCreateCompressorInputStreamDEFLATE() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[1024]);
        CompressorInputStream stream = factory.createCompressorInputStream(CompressorStreamFactory.DEFLATE, input);
        assertNotNull(stream);
        stream.close();
    }

    @Test
    public void testCreateCompressorOutputStreamGZIP() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream stream = factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, output);
        assertNotNull(stream);
        stream.close();
    }

    @Test
    public void testCreateCompressorOutputStreamBZIP2() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream stream = factory.createCompressorOutputStream(CompressorStreamFactory.BZIP2, output);
        assertNotNull(stream);
        stream.close();
    }

    @Test
    public void testCreateCompressorOutputStreamXZ() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream stream = factory.createCompressorOutputStream(CompressorStreamFactory.XZ, output);
        assertNotNull(stream);
        stream.close();
    }

    @Test
    public void testCreateCompressorOutputStreamPACK200() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream stream = factory.createCompressorOutputStream(CompressorStreamFactory.PACK200, output);
        assertNotNull(stream);
        stream.close();
    }

    @Test
    public void testCreateCompressorOutputStreamDEFLATE() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream stream = factory.createCompressorOutputStream(CompressorStreamFactory.DEFLATE, output);
        assertNotNull(stream);
        stream.close();
    }
}

