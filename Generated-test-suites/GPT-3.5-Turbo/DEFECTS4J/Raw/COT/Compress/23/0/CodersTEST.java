import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static org.junit.Assert.*;

public class CodersTest {

    @Test
    public void testAddDecoder() throws IOException {
        byte[] password = new byte[]{1, 2, 3, 4};
        InputStream is = InputStream.nullInputStream();
        Coder coder = new Coder();
        Coders.addDecoder(is, coder, password);
        // Your test code here
    }

    @Test
    public void testAddEncoder() throws IOException {
        byte[] password = new byte[]{1, 2, 3, 4};
        OutputStream out = OutputStream.nullOutputStream();
        SevenZMethod method = new SevenZMethod();
        Coders.addEncoder(out, method, password);
        // Your test code here
    }

    @Test(expected = IOException.class)
    public void testAddDecoderUnsupportedCompressionMethod() throws IOException {
        byte[] password = new byte[]{1, 2, 3, 4};
        InputStream is = InputStream.nullInputStream();
        Coder coder = new Coder();
        Coders.addDecoder(is, coder, password);
    }

    @Test(expected = IOException.class)
    public void testAddEncoderUnsupportedCompressionMethod() throws IOException {
        byte[] password = new byte[]{1, 2, 3, 4};
        OutputStream out = OutputStream.nullOutputStream();
        SevenZMethod method = new SevenZMethod();
        Coders.addEncoder(out, method, password);
    }

    @Test
    public void testCopyDecoderDecode() throws IOException {
        InputStream in = InputStream.nullInputStream();
        Coder coder = new Coder();
        byte[] password = new byte[]{1, 2, 3, 4};
        InputStream result = new Coders.CopyDecoder().decode(in, coder, password);
        // Your test code here
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCopyDecoderEncode() throws IOException {
        OutputStream out = OutputStream.nullOutputStream();
        byte[] password = new byte[]{1, 2, 3, 4};
        new Coders.CopyDecoder().encode(out, password);
    }

    @Test
    public void testLZMADecoderDecode() throws IOException {
        InputStream in = InputStream.nullInputStream();
        Coder coder = new Coder();
        byte[] password = new byte[]{1, 2, 3, 4};
        InputStream result = new Coders.LZMADecoder().decode(in, coder, password);
        // Your test code here
    }

    @Test
    public void testDeflateDecoderDecode() throws IOException {
        InputStream in = InputStream.nullInputStream();
        Coder coder = new Coder();
        byte[] password = new byte[]{1, 2, 3, 4};
        InputStream result = new Coders.DeflateDecoder().decode(in, coder, password);
        // Your test code here
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeflateDecoderEncode() throws IOException {
        OutputStream out = OutputStream.nullOutputStream();
        byte[] password = new byte[]{1, 2, 3, 4};
        new Coders.DeflateDecoder().encode(out, password);
    }

    @Test
    public void testBZIP2DecoderDecode() throws IOException {
        InputStream in = InputStream.nullInputStream();
        Coder coder = new Coder();
        byte[] password = new byte[]{1, 2, 3, 4};
        InputStream result = new Coders.BZIP2Decoder().decode(in, coder, password);
        // Your test code here
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBZIP2DecoderEncode() throws IOException {
        OutputStream out = OutputStream.nullOutputStream();
        byte[] password = new byte[]{1, 2, 3, 4};
        new Coders.BZIP2Decoder().encode(out, password);
    }

    @Test
    public void testAES256SHA256DecoderDecode() throws IOException {
        InputStream in = InputStream.nullInputStream();
        Coder coder = new Coder();
        byte[] password = new byte[]{1, 2, 3, 4};
        InputStream result = new Coders.AES256SHA256Decoder().decode(in, coder, password);
        // Your test code here
    }

}