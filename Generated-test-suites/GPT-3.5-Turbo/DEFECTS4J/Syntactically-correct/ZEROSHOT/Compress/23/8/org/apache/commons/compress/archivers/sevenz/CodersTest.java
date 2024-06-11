import org.junit.*;
import static org.junit.Assert.*;

public class CodersTest {

    @Test
    public void testAddDecoder_CopyMethod() {
        Coder coder = new Coder();
        coder.decompressionMethodId = SevenZMethod.COPY.getId();
        byte[] password = null;
        try {
            InputStream result = Coders.addDecoder(null, coder, password);
            assertNull(result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testAddDecoder_LZMAMethod() {
        Coder coder = new Coder();
        coder.decompressionMethodId = SevenZMethod.LZMA.getId();
        byte[] password = null;
        try {
            InputStream result = Coders.addDecoder(null, coder, password);
            assertNotNull(result);
            assertTrue(result instanceof LZMAInputStream);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testAddDecoder_LZMA2Method() {
        Coder coder = new Coder();
        coder.decompressionMethodId = SevenZMethod.LZMA2.getId();
        byte[] password = null;
        try {
            InputStream result = Coders.addDecoder(null, coder, password);
            assertNotNull(result);
            assertTrue(result instanceof LZMAInputStream);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testAddDecoder_DeflateMethod() {
        Coder coder = new Coder();
        coder.decompressionMethodId = SevenZMethod.DEFLATE.getId();
        byte[] password = null;
        try {
            InputStream result = Coders.addDecoder(null, coder, password);
            assertNotNull(result);
            assertTrue(result instanceof InflaterInputStream);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testAddDecoder_BZIP2Method() {
        Coder coder = new Coder();
        coder.decompressionMethodId = SevenZMethod.BZIP2.getId();
        byte[] password = null;
        try {
            InputStream result = Coders.addDecoder(null, coder, password);
            assertNotNull(result);
            assertTrue(result instanceof BZip2CompressorInputStream);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testAddDecoder_AES256SHA256Method() {
        Coder coder = new Coder();
        coder.decompressionMethodId = SevenZMethod.AES256SHA256.getId();
        byte[] password = null;
        try {
            InputStream result = Coders.addDecoder(null, coder, password);
            assertNotNull(result);
            assertTrue(result instanceof Coders.AES256SHA256Decoder.InputStream);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = IOException.class)
    public void testAddDecoder_UnsupportedMethod() throws IOException {
        Coder coder = new Coder();
        coder.decompressionMethodId = new byte[]{0x7F};
        byte[] password = null;
        Coders.addDecoder(null, coder, password);
    }

    @Test
    public void testAddEncoder_CopyMethod() {
        OutputStream out = null;
        SevenZMethod method = SevenZMethod.COPY;
        byte[] password = null;
        try {
            OutputStream result = Coders.addEncoder(out, method, password);
            assertNull(result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testAddEncoder_DeflateMethod() {
        OutputStream out = null;
        SevenZMethod method = SevenZMethod.DEFLATE;
        byte[] password = null;
        try {
            OutputStream result = Coders.addEncoder(out, method, password);
            assertNotNull(result);
            assertTrue(result instanceof DeflaterOutputStream);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testAddEncoder_BZIP2Method() {
        OutputStream out = null;
        SevenZMethod method = SevenZMethod.BZIP2;
        byte[] password = null;
        try {
            OutputStream result = Coders.addEncoder(out, method, password);
            assertNotNull(result);
            assertTrue(result instanceof BZip2CompressorOutputStream);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = IOException.class)
    public void testAddEncoder_UnsupportedMethod() throws IOException {
        OutputStream out = null;
        SevenZMethod method = new SevenZMethod(new byte[]{0x7F});
        byte[] password = null;
        Coders.addEncoder(out, method, password);
    }

}