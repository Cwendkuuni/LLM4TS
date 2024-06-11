import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.tukaani.xz.LZMAInputStream;

public class CodersTest {

    @Test
    public void testAddDecoder() throws IOException {
        InputStream is = null;
        Coder coder = null;
        byte[] password = null;
        try {
            InputStream result = Coders.addDecoder(is, coder, password);
            assertNotNull(result);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testAddEncoder() throws IOException {
        OutputStream out = null;
        SevenZMethod method = null;
        byte[] password = null;
        try {
            OutputStream result = Coders.addEncoder(out, method, password);
            assertNotNull(result);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testCopyDecoderDecode() throws IOException {
        InputStream in = null;
        Coder coder = null;
        byte[] password = null;
        try {
            InputStream result = new Coders.CopyDecoder().decode(in, coder, password);
            assertNotNull(result);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testCopyDecoderEncode() {
        OutputStream out = null;
        byte[] password = null;
        try {
            OutputStream result = new Coders.CopyDecoder().encode(out, password);
            assertNotNull(result);
        } catch (UnsupportedOperationException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testLZMADecoderDecode() throws IOException {
        InputStream in = null;
        Coder coder = null;
        byte[] password = null;
        try {
            InputStream result = new Coders.LZMADecoder().decode(in, coder, password);
            assertNotNull(result);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testDeflateDecoderDecode() throws IOException {
        InputStream in = null;
        Coder coder = null;
        byte[] password = null;
        try {
            InputStream result = new Coders.DeflateDecoder().decode(in, coder, password);
            assertNotNull(result);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testDeflateDecoderEncode() {
        OutputStream out = null;
        byte[] password = null;
        try {
            OutputStream result = new Coders.DeflateDecoder().encode(out, password);
            assertNotNull(result);
        } catch (UnsupportedOperationException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testBZIP2DecoderDecode() throws IOException {
        InputStream in = null;
        Coder coder = null;
        byte[] password = null;
        try {
            InputStream result = new Coders.BZIP2Decoder().decode(in, coder, password);
            assertNotNull(result);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testBZIP2DecoderEncode() throws IOException {
        OutputStream out = null;
        byte[] password = null;
        try {
            OutputStream result = new Coders.BZIP2Decoder().encode(out, password);
            assertNotNull(result);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testAES256SHA256DecoderDecode() throws IOException {
        InputStream in = null;
        Coder coder = null;
        byte[] password = null;
        try {
            InputStream result = new Coders.AES256SHA256Decoder().decode(in, coder, password);
            assertNotNull(result);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testDummyByteAddingInputStream() throws IOException {
        InputStream in = null;
        Coders.DummyByteAddingInputStream dummyInputStream = new Coders.DummyByteAddingInputStream(in);
        int result = dummyInputStream.read();
        assertEquals(0, result);
    }
}