
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CodersTest {
    @Test
    public void testAddDecoder() throws IOException {
        Coders.Coder coder = new Coders.Coder();
        coder.decompressionMethodId = new byte[] { 0x00, 0x01 };
        byte[] password = "password".getBytes();
        InputStream is = new ByteArrayInputStream(new byte[0]);

        try {
            Coders.addDecoder(is, coder, password);
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("Unsupported compression method [0, 1]", e.getMessage());
        }
    }

    @Test
    public void testAddEncoder() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.LZMA;
        byte[] password = "password".getBytes();

        try {
            Coders.addEncoder(out, method, password);
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("Unsupported compression method LZMA", e.getMessage());
        }
    }

    @Test
    public void testDecodeLZMA() throws IOException {
        Coders.Coder coder = new Coders.Coder();
        coder.properties = new byte[] { 0, 0, 0, 0, 0 };
        InputStream in = new ByteArrayInputStream(new byte[0]);

        try {
            Coders.LZMADecoder decoder = new Coders.LZMADecoder();
            decoder.decode(in, coder, null);
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("Dictionary larger than 4GiB maximum size", e.getMessage());
        }
    }

    @Test
    public void testDecodeDeflate() throws IOException {
        Coders.Coder coder = new Coders.Coder();
        InputStream in = new ByteArrayInputStream(new byte[0]);

        Coders.DeflateDecoder decoder = new Coders.DeflateDecoder();
        assertNotNull(decoder.decode(in, coder, null));
    }

    @Test
    public void testDecodeBZIP2() throws IOException {
        Coders.Coder coder = new Coders.Coder();
        InputStream in = new ByteArrayInputStream(new byte[0]);

        Coders.BZIP2Decoder decoder = new Coders.BZIP2Decoder();
        assertNotNull(decoder.decode(in, coder, null));
    }

    @Test
    public void testDecodeAES256SHA256() throws IOException {
        Coders.Coder coder = new Coders.Coder();
        coder.properties = new byte[] { 0, 0, 0, 0, 0 };
        InputStream in = new ByteArrayInputStream(new byte[0]);

        try {
            Coders.AES256SHA256Decoder decoder = new Coders.AES256SHA256Decoder();
            decoder.decode(in, coder, null);
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("Cannot read encrypted files without a password", e.getMessage());
        }
    }

    @Test
    public void testDummyByteAddingInputStream() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        Coders.DummyByteAddingInputStream dummyStream = new Coders.DummyByteAddingInputStream(in);

        assertEquals(0, dummyStream.read());
        assertEquals(-1, dummyStream.read());
        assertEquals(1, dummyStream.read(new byte[1], 0, 1));
    }
}

