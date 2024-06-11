import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMACompressorInputStream;
import org.apache.commons.compress.compressors.lzma.LZMAUtils;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZUtils;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorInputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorOutputStream;
import org.apache.commons.compress.compressors.snappy.FramedSnappyCompressorInputStream;
import org.apache.commons.compress.compressors.snappy.SnappyCompressorInputStream;
import org.apache.commons.compress.compressors.z.ZCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStreamAutoDetect() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream in = getClass().getResourceAsStream("test.txt.bz2");
        CompressorInputStream cis = factory.createCompressorInputStream(in);
        assertTrue(cis instanceof BZip2CompressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStreamBZip2() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream in = getClass().getResourceAsStream("test.txt.bz2");
        CompressorInputStream cis = factory.createCompressorInputStream("bz2", in);
        assertTrue(cis instanceof BZip2CompressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStreamGzip() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream in = getClass().getResourceAsStream("test.txt.gz");
        CompressorInputStream cis = factory.createCompressorInputStream("gz", in);
        assertTrue(cis instanceof GzipCompressorInputStream);
    }
    
    //...continue testing for other methods
}