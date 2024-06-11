import org.apache.commons.compress.compressors.CompressorInputStream;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = {};
        InputStream is = new ByteArrayInputStream(data);
        CompressorInputStream cis = new DeflateCompressorInputStream(is);
        int result = cis.read();
        // assert statements here
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] data = {};
        InputStream is = new ByteArrayInputStream(data);
        CompressorInputStream cis = new DeflateCompressorInputStream(is);
        byte[] buffer = new byte[10];
        int result = cis.read(buffer, 0, 10);
        // assert statements here
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = {};
        InputStream is = new ByteArrayInputStream(data);
        CompressorInputStream cis = new DeflateCompressorInputStream(is);
        long result = cis.skip(10);
        // assert statements here
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] data = {};
        InputStream is = new ByteArrayInputStream(data);
        CompressorInputStream cis = new DeflateCompressorInputStream(is);
        int result = cis.available();
        // assert statements here
    }

    @Test
    public void testClose() throws IOException {
        byte[] data = {};
        InputStream is = new ByteArrayInputStream(data);
        CompressorInputStream cis = new DeflateCompressorInputStream(is);
        cis.close();
        // assert statements here
    }
}