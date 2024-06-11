import org.junit.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.junit.Test;

public class CodersTest {
    @Test
    public void testAddDecoder() throws IOException {
        InputStream is = null;
        Coder coder = null;
        byte[] password = null;
        try {
            Coders.addDecoder(is, coder, password);
        } catch (IOException e) {
            Assert.assertEquals(e.getMessage(), "Unsupported compression method []");
        }
    }
    
    @Test
    public void testAddEncoder() throws IOException {
        OutputStream out = null;
        SevenZMethod method = null;
        byte[] password = null;
        try {
            Coders.addEncoder(out, method, password);
        } catch (IOException e) {
            Assert.assertEquals(e.getMessage(), "Unsupported compression method null");
        }
    }

    @Test
    public void testCoderTable() {
        Coders.CoderId[] coderTable = Coders.coderTable;
        Assert.assertNotNull(coderTable);
        Assert.assertEquals(coderTable.length, 6);
    }
    
    @Test
    public void testCopyDecoder() throws IOException {
        Coders.CopyDecoder copyDecoder = new Coders.CopyDecoder();
        InputStream in = null;
        Coders.Coder coder = null;
        byte[] password = null;
        InputStream is = copyDecoder.decode(in, coder, password);
        Assert.assertEquals(is, in);
    }

    @Test
    public void testLZMADecoder() throws IOException {
        Coders.LZMADecoder lzmaDecoder = new Coders.LZMADecoder();
        InputStream in = null;
        Coders.Coder coder = null;
        byte[] password = null;
        InputStream is = lzmaDecoder.decode(in, coder, password);
        Assert.assertNotNull(is);
    }
    
    @Test
    public void testDeflateDecoder() throws IOException {
        Coders.DeflateDecoder deflateDecoder = new Coders.DeflateDecoder();
        InputStream in = null;
        Coders.Coder coder = null;
        byte[] password = null;
        InputStream is = deflateDecoder.decode(in, coder, password);
        Assert.assertNotNull(is);
        
        OutputStream out = null;
        Assert.assertThrows(UnsupportedOperationException.class, () -> {
             deflateDecoder.encode(out, password);
        });
    }

    @Test
    public void testBZIP2Decoder() throws IOException {
        Coders.BZIP2Decoder bzip2Decoder = new Coders.BZIP2Decoder();
        InputStream in = null;
        Coders.Coder coder = null;
        byte[] password = null;
        InputStream is = bzip2Decoder.decode(in, coder, password);
        Assert.assertNotNull(is);
        
        OutputStream out = null;
        OutputStream os = bzip2Decoder.encode(out, password);
        Assert.assertNotNull(os);
    }

    @Test
    public void testAES256SHA256Decoder() throws IOException {
        Coders.AES256SHA256Decoder aesDecoder = new Coders.AES256SHA256Decoder();
        InputStream in = null;
        Coders.Coder coder = new Coders.Coder();
        byte[] passwordBytes = null;
        InputStream is = aesDecoder.decode(in, coder, passwordBytes);
        Assert.assertNotNull(is);
    }

    @Test
    public void testDummyByteAddingInputStream() throws IOException {
        InputStream in = null;
        Coders.DummyByteAddingInputStream stream = new Coders.DummyByteAddingInputStream(in);
        int result = stream.read();
        Assert.assertEquals(result, 0);
        
        byte[] b = new byte[10];
        int off = 0;
        int len = 10;
        result = stream.read(b, off, len);
        Assert.assertEquals(result, 1);
    }
}