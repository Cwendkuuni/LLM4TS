import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.sevenz.Coders.*;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;

public class CodersTest {

    @Test
    public void testAddDecoder() {
        InputStream is = null;
        Coder coder = null;
        byte[] password = null;
        try {
            Coders.addDecoder(is, coder, password);
            fail("IOException should have been thrown");
        } catch (IOException e) {
            assertEquals("Unsupported compression method null", e.getMessage());
        }
    }

    @Test
    public void testAddEncoder() {
        OutputStream out = null;
        SevenZMethod method = null;
        byte[] password = null;
        try {
            Coders.addEncoder(out, method, password);
            fail("IOException should have been thrown");
        } catch (IOException e) {
            assertEquals("Unsupported compression method null", e.getMessage());
        }
    }

    @Test
    public void testCoderIdConstruction() {
        SevenZMethod method = SevenZMethod.COPY;
        CoderBase coder = new CopyDecoder();
        CoderId coderId = new CoderId(method, coder);
        assertEquals(method, coderId.method);
        assertEquals(coder, coderId.coder);
    }

    @Test
    public void testCopyDecoderDecode() throws IOException {
        InputStream in = null;
        Coder coder = null;
        byte[] password = null;
        InputStream result = new CopyDecoder().decode(in, coder, password);
        assertEquals(in, result);
    }
    
    @Test
    public void testCopyDecoderEncode() {
        OutputStream out = null;
        byte[] password = null;
        OutputStream result = new CopyDecoder().encode(out, password);
        assertEquals(out, result);
    }

    @Test
    public void testLZMADecoderDecode() throws IOException {
        InputStream in = null;
        Coder coder = null;
        byte[] password = null;
        InputStream result = new LZMADecoder().decode(in, coder, password);
        assertNotNull(result);
    }

    @Test
    public void testDeflateDecoderDecode() throws IOException {
        InputStream in = null;
        Coder coder = null;
        byte[] password = null;
        InputStream result = new DeflateDecoder().decode(in, coder, password);
        assertNotNull(result);
    }
    
    @Test
    public void testDeflateDecoderEncode() {
        OutputStream out = null;
        byte[] password = null;
        OutputStream result = new DeflateDecoder().encode(out, password);
        assertNotNull(result);
    }

    @Test
    public void testBZIP2DecoderDecode() throws IOException {
        InputStream in = null;
        Coder coder = null;
        byte[] password = null;
        InputStream result = new BZIP2Decoder().decode(in, coder, password);
        assertNotNull(result);
    }

    @Test
    public void testBZIP2DecoderEncode() throws IOException {
        OutputStream out = null;
        byte[] password = null;
        OutputStream result = new BZIP2Decoder().encode(out, password);
        assertNotNull(result);
    }

    @Test
    public void testAES256SHA256DecoderDecode() throws IOException {
        InputStream in = null;
        Coder coder = null;
        byte[] passwordBytes = null;
        InputStream result = new AES256SHA256Decoder().decode(in, coder, passwordBytes);
        assertNotNull(result);
    }
}