import org.junit.Test;
import org.junit.Assert;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.sevenz.Coders.*;
import org.apache.commons.compress.archivers.sevenz.Coders.CoderBase.*;

public class CodersTest {

    @Test
    public void testAddDecoder() throws IOException {
        InputStream is = null;
        Coder coder = null;
        byte[] password = null;

        try {
            Coders.addDecoder(is, coder, password);
        } catch (IOException e) {
            Assert.fail("Error occurred while adding decoder");
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
            Assert.fail("Error occurred while adding encoder");
        }
    }

    @Test
    public void testCoderId() {
        SevenZMethod method = null;
        CoderBase coder = null;

        CoderId coderId = new CoderId(method, coder);

        Assert.assertEquals(method, coderId.method);
        Assert.assertEquals(coder, coderId.coder);
    }

    @Test
    public void testCopyDecoder() {
        CopyDecoder copyDecoder = new CopyDecoder();

        InputStream in = null;
        Coder coder = null;
        byte[] password = null;

        try {
            copyDecoder.decode(in, coder, password);
        } catch (IOException e) {
            Assert.fail("Error occurred while decoding with CopyDecoder");
        }

        OutputStream out = null;
        byte[] password = null;

        try {
            copyDecoder.encode(out, password);
        } catch (UnsupportedOperationException e) {
            Assert.fail("Error occurred while encoding with CopyDecoder");
        }
    }

    @Test
    public void testLZMADecoder() {
        LZMADecoder lzmDecoder = new LZMADecoder();

        InputStream in = null;
        Coder coder = null;
        byte[] password = null;

        try {
            lzmDecoder.decode(in, coder, password);
        } catch (IOException e) {
            Assert.fail("Error occurred while decoding with LZMADecoder");
        }
    }

    @Test
    public void testDeflateDecoder() {
        DeflateDecoder deflateDecoder = new DeflateDecoder();

        InputStream in = null;
        Coder coder = null;
        byte[] password = null;

        try {
            deflateDecoder.decode(in, coder, password);
        } catch (IOException e) {
            Assert.fail("Error occurred while decoding with DeflateDecoder");
        }

        OutputStream out = null;
        byte[] password = null;

        try {
            deflateDecoder.encode(out, password);
        } catch (UnsupportedOperationException e) {
            Assert.fail("Error occurred while encoding with DeflateDecoder");
        }
    }

    @Test
    public void testBZIP2Decoder() {
        BZIP2Decoder bzip2Decoder = new BZIP2Decoder();

        InputStream in = null;
        Coder coder = null;
        byte[] password = null;

        try {
            bzip2Decoder.decode(in, coder, password);
        } catch (IOException e) {
            Assert.fail("Error occurred while decoding with BZIP2Decoder");
        }

        OutputStream out = null;
        byte[] password = null;

        try {
            bzip2Decoder.encode(out, password);
        } catch (IOException e) {
            Assert.fail("Error occurred while encoding with BZIP2Decoder");
        }
    }

    @Test
    public void testAES256SHA256Decoder() {
        AES256SHA256Decoder aesDecoder = new AES256SHA256Decoder();

        InputStream in = null;
        Coder coder = null;
        byte[] passwordBytes = null;

        try {
            aesDecoder.decode(in, coder, passwordBytes);
        } catch (IOException e) {
            Assert.fail("Error occurred while decoding with AES256SHA256Decoder");
        }
    }

    @Test
    public void testDummyByteAddingInputStream() throws IOException {
        InputStream in = null;

        DummyByteAddingInputStream dummyStream = new DummyByteAddingInputStream(in);

        try {
            dummyStream.read();
        } catch (IOException e) {
            Assert.fail("Error occurred while reading from DummyByteAddingInputStream");
        }

        byte[] b = null;
        int off = 0;
        int len = 0;

        try {
            dummyStream.read(b, off, len);
        } catch (IOException e) {
            Assert.fail("Error occurred while reading from DummyByteAddingInputStream");
        }
    }
}