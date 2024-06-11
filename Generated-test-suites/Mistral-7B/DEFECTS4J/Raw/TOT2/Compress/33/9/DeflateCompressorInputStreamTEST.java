import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testDeflateCompressorInputStream_withInputStream() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0xDA, (byte) 0x10, (byte) 0x12});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        assertNotNull(deflateCompressorInputStream);
    }
}