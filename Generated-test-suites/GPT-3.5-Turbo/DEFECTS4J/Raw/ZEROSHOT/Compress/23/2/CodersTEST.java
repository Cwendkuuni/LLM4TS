import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.tukaani.xz.LZMAInputStream;

public class CodersTest {

    @Test
    public void testAddDecoder_copy() throws IOException {
        InputStream is = null;
        Coder coder = new Coder();
        byte[] password = null;
        InputStream result = Coders.addDecoder(is, coder, password);
        assertNull(result);
    }

    @Test
    public void testAddDecoder_lzma() throws IOException {
        InputStream is = null;
        Coder coder = new Coder();
        coder.decompressionMethodId = SevenZMethod.LZMA.getId();
        byte[] password = null;
        InputStream result = Coders.addDecoder(is, coder, password);
        assertNotNull(result);
        assertTrue(result instanceof LZMAInputStream);
    }

    @Test
    public void testAddDecoder_lzma2() throws IOException {
        InputStream is = null;
        Coder coder = new Coder();
        coder.decompressionMethodId = SevenZMethod.LZMA2.getId();
        byte[] password = null;
        InputStream result = Coders.addDecoder(is, coder, password);
        assertNull(result);
    }

    @Test
    public void testAddDecoder_deflate() throws IOException {
        InputStream is = null;
        Coder coder = new Coder();
        coder.decompressionMethodId = SevenZMethod.DEFLATE.getId();
        byte[] password = null;
        InputStream result = Coders.addDecoder(is, coder, password);
        assertNotNull(result);
        assertTrue(result instanceof InflaterInputStream);
    }

    @Test
    public void testAddDecoder_bzip2() throws IOException {
        InputStream is = null;
        Coder coder = new Coder();
        coder.decompressionMethodId = SevenZMethod.BZIP2.getId();
        byte[] password = null;
        InputStream result = Coders.addDecoder(is, coder, password);
        assertNotNull(result);
        assertTrue(result instanceof BZip2CompressorInputStream);
    }

    @Test
    public void testAddDecoder_aes256sha256() throws IOException {
        InputStream is = null;
        Coder coder = new Coder();
        coder.decompressionMethodId = SevenZMethod.AES256SHA256.getId();
        byte[] password = null;
        InputStream result = Coders.addDecoder(is, coder, password);
        assertNotNull(result);
    }

    @Test(expected = IOException.class)
    public void testAddDecoder_unsupportedMethod() throws IOException {
        InputStream is = null;
        Coder coder = new Coder();
        coder.decompressionMethodId = new int[]{1, 2, 3};
        byte[] password = null;
        InputStream result = Coders.addDecoder(is, coder, password);
    }

    @Test
    public void testAddEncoder_copy() throws IOException {
        OutputStream out = null;
        SevenZMethod method = SevenZMethod.COPY;
        byte[] password = null;
        OutputStream result = Coders.addEncoder(out, method, password);
        assertNull(result);
    }

    @Test
    public void testAddEncoder_lzma() throws IOException {
        OutputStream out = null;
        SevenZMethod method = SevenZMethod.LZMA;
        byte[] password = null;
        OutputStream result = Coders.addEncoder(out, method, password);
        assertNull(result);
    }

    @Test
    public void testAddEncoder_lzma2() throws IOException {
        OutputStream out = null;
        SevenZMethod method = SevenZMethod.LZMA2;
        byte[] password = null;
        OutputStream result = Coders.addEncoder(out, method, password);
        assertNull(result);
    }

    @Test
    public void testAddEncoder_deflate() throws IOException {
        OutputStream out = null;
        SevenZMethod method = SevenZMethod.DEFLATE;
        byte[] password = null;
        OutputStream result = Coders.addEncoder(out, method, password);
        assertNotNull(result);
        assertTrue(result instanceof DeflaterOutputStream);
    }

    @Test
    public void testAddEncoder_bzip2() throws IOException {
        OutputStream out = null;
        SevenZMethod method = SevenZMethod.BZIP2;
        byte[] password = null;
        OutputStream result = Coders.addEncoder(out, method, password);
        assertNotNull(result);
        assertTrue(result instanceof BZip2CompressorOutputStream);
    }

    @Test
    public void testAddEncoder_aes256sha256() throws IOException {
        OutputStream out = null;
        SevenZMethod method = SevenZMethod.AES256SHA256;
        byte[] password = null;
        OutputStream result = Coders.addEncoder(out, method, password);
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testAddEncoder_unsupportedMethod() throws IOException {
        OutputStream out = null;
        SevenZMethod method = new SevenZMethod(1, "dummy");
        byte[] password = null;
        OutputStream result = Coders.addEncoder(out, method, password);
    }

}