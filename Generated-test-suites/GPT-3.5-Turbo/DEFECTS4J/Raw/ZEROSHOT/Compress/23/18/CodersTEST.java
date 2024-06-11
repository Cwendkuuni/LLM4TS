import org.junit.Test;

import static org.junit.Assert.*;

public class CodersTest {

    @Test
    public void testAddDecoder_CopyDecoder() throws Exception {
        InputStream is = null;
        Coder coder = null;
        byte[] password = null;
        InputStream result = Coders.addDecoder(is, coder, password);
        assertNull(result);
    }

    @Test
    public void testAddDecoder_LZMADecoder() throws Exception {
        InputStream is = null;
        Coder coder = null;
        byte[] password = null;
        InputStream result = Coders.addDecoder(is, coder, password);
        assertNull(result);
    }

    @Test
    public void testAddDecoder_DeflateDecoder() throws Exception {
        InputStream is = null;
        Coder coder = null;
        byte[] password = null;
        InputStream result = Coders.addDecoder(is, coder, password);
        assertNull(result);
    }

    @Test
    public void testAddDecoder_BZIP2Decoder() throws Exception {
        InputStream is = null;
        Coder coder = null;
        byte[] password = null;
        InputStream result = Coders.addDecoder(is, coder, password);
        assertNull(result);
    }

    @Test
    public void testAddDecoder_AES256SHA256Decoder() throws Exception {
        InputStream is = null;
        Coder coder = null;
        byte[] password = null;
        InputStream result = Coders.addDecoder(is, coder, password);
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testAddDecoder_UnsupportedMethod() throws Exception {
        InputStream is = null;
        Coder coder = null;
        byte[] password = null;
        Coders.addDecoder(is, coder, password);
    }

    @Test
    public void testAddEncoder() throws Exception {
        OutputStream out = null;
        SevenZMethod method = null;
        byte[] password = null;
        OutputStream result = Coders.addEncoder(out, method, password);
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testAddEncoder_UnsupportedMethod() throws Exception {
        OutputStream out = null;
        SevenZMethod method = null;
        byte[] password = null;
        Coders.addEncoder(out, method, password);
    }
}