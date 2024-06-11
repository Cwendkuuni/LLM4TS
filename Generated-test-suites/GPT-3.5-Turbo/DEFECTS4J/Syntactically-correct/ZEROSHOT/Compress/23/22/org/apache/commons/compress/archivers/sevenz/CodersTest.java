import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import org.apache.commons.compress.archivers.sevenz.*;

public class CodersTest {

    @Test
    public void testAddDecoder() {
        InputStream is = new ByteArrayInputStream("test string".getBytes());
        Coder coder = new Coder();
        byte[] password = "password".getBytes();
        try {
            InputStream result = Coders.addDecoder(is, coder, password);
            assertNotNull(result);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testAddDecoderWithUnsupportedCompressionMethod() {
        InputStream is = new ByteArrayInputStream("test string".getBytes());
        Coder coder = createDummyCoder();
        byte[] password = "password".getBytes();
        try {
            InputStream result = Coders.addDecoder(is, coder, password);
            fail("IOException not thrown");
        } catch (IOException e) {
            assertEquals("Unsupported compression method [1, 2, 3]", e.getMessage());
        }
    }

    @Test
    public void testAddEncoder() {
        OutputStream out = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.LZMA;
        byte[] password = "password".getBytes();
        try {
            OutputStream result = Coders.addEncoder(out, method, password);
            assertNotNull(result);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testAddEncoderWithUnsupportedCompressionMethod() {
        OutputStream out = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.ZSTD;
        byte[] password = "password".getBytes();
        try {
            OutputStream result = Coders.addEncoder(out, method, password);
            fail("IOException not thrown");
        } catch (IOException e) {
            assertEquals("Unsupported compression method ZSTD", e.getMessage());
        }
    }

    @Test
    public void testCopyDecoderDecode() {
        InputStream in = new ByteArrayInputStream("test string".getBytes());
        Coder coder = createCoderWithMethod(SevenZMethod.COPY);
        byte[] password = "password".getBytes();
        try {
            InputStream result = Coders.CoderId[0].coder.decode(in, coder, password);
            assertEquals(in, result);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testCopyDecoderEncode() {
        OutputStream out = new ByteArrayOutputStream();
        byte[] password = "password".getBytes();
        try {
            OutputStream result = Coders.CoderId[0].coder.encode(out, password);
            assertEquals(out, result);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testLZMADecoderDecode() {
        InputStream in = new ByteArrayInputStream("test string".getBytes());
        Coder coder = createCoderWithMethod(SevenZMethod.LZMA);
        byte[] password = "password".getBytes();
        try {
            InputStream result = Coders.CoderId[1].coder.decode(in, coder, password);
            assertNotNull(result);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testDeflateDecoderDecode() {
        InputStream in = new ByteArrayInputStream("test string".getBytes());
        Coder coder = createCoderWithMethod(SevenZMethod.DEFLATE);
        byte[] password = "password".getBytes();
        try {
            InputStream result = Coders.CoderId[3].coder.decode(in, coder, password);
            assertNotNull(result);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testDeflateDecoderEncode() {
        OutputStream out = new ByteArrayOutputStream();
        byte[] password = "password".getBytes();
        try {
            OutputStream result = Coders.CoderId[3].coder.encode(out, password);
            assertNotNull(result);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testBZIP2DecoderDecode() {
        InputStream in = new ByteArrayInputStream("test string".getBytes());
        Coder coder = createCoderWithMethod(SevenZMethod.BZIP2);
        byte[] password = "password".getBytes();
        try {
            InputStream result = Coders.CoderId[4].coder.decode(in, coder, password);
            assertNotNull(result);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testBZIP2DecoderEncode() {
        OutputStream out = new ByteArrayOutputStream();
        byte[] password = "password".getBytes();
        try {
            OutputStream result = Coders.CoderId[4].coder.encode(out, password);
            assertNotNull(result);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testAES256SHA256DecoderDecode() {
        InputStream in = new ByteArrayInputStream("test string".getBytes());
        Coder coder = createCoderWithMethod(SevenZMethod.AES256SHA256);
        byte[] password = "password".getBytes();
        try {
            InputStream result = Coders.CoderId[5].coder.decode(in, coder,
                    password);
            assertNotNull(result);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    private Coder createDummyCoder() {
        Coder coder = new Coder();
        coder.decompressionMethodId = new byte[]{1, 2, 3};
        return coder;
    }

    private Coder createCoderWithMethod(SevenZMethod method) {
        Coder coder = new Coder();
        coder.decompressionMethodId = method.getId();
        return coder;
    }
}