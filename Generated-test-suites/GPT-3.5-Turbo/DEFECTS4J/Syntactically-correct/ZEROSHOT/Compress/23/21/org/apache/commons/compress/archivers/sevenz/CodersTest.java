import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class CodersTest {

    @Test
    public void testAddDecoder() throws Exception {
        Coders.Coder coder = new Coders.Coder();
        byte[] password = new byte[]{(byte) 0x61, (byte) 0x62, (byte) 0x63};
        InputStream is = new ByteArrayInputStream(new byte[0]);
        Coders.addDecoder(is, coder, password);
    }

    @Test(expected = IOException.class)
    public void testAddDecoderUnsupportedMethod() throws Exception {
        Coders.Coder coder = new Coders.Coder();
        byte[] password = new byte[]{(byte) 0x61, (byte) 0x62, (byte) 0x63};
        InputStream is = new ByteArrayInputStream(new byte[0]);
        Coders.addDecoder(is, coder, password);
    }

    @Test
    public void testAddEncoder() throws Exception {
        OutputStream out = new ByteArrayOutputStream();
        Coders.SevenZMethod method = new Coders.SevenZMethod();
        byte[] password = new byte[]{(byte) 0x61, (byte) 0x62, (byte) 0x63};
        Coders.addEncoder(out, method, password);
    }

    @Test(expected = IOException.class)
    public void testAddEncoderUnsupportedMethod() throws Exception {
        OutputStream out = new ByteArrayOutputStream();
        Coders.SevenZMethod method = new Coders.SevenZMethod();
        byte[] password = new byte[]{(byte) 0x61, (byte) 0x62, (byte) 0x63};
        Coders.addEncoder(out, method, password);
    }

    @Test
    public void testCopyDecoderDecode() throws Exception {
        Coders.Coder coder = new Coders.Coder();
        Coders.CopyDecoder copyDecoder = new Coders.CopyDecoder();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        copyDecoder.decode(in, coder, null);
    }

    @Test
    public void testCopyDecoderEncode() throws Exception {
        Coders.CopyDecoder copyDecoder = new Coders.CopyDecoder();
        OutputStream out = new ByteArrayOutputStream();
        copyDecoder.encode(out, null);
    }

    @Test
    public void testLZMADecoderDecode() throws Exception {
        Coders.Coder coder = new Coders.Coder();
        Coders.LZMADecoder lzmADecoder = new Coders.LZMADecoder();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        lzmADecoder.decode(in, coder, null);
    }

    @Test
    public void testDeflateDecoderDecode() throws Exception {
        Coders.Coder coder = new Coders.Coder();
        Coders.DeflateDecoder deflateDecoder = new Coders.DeflateDecoder();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        deflateDecoder.decode(in, coder, null);
    }

    @Test
    public void testDeflateDecoderEncode() throws Exception {
        Coders.DeflateDecoder deflateDecoder = new Coders.DeflateDecoder();
        OutputStream out = new ByteArrayOutputStream();
        deflateDecoder.encode(out, null);
    }

    @Test
    public void testBZIP2DecoderDecode() throws Exception {
        Coders.Coder coder = new Coders.Coder();
        Coders.BZIP2Decoder bzip2Decoder = new Coders.BZIP2Decoder();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        bzip2Decoder.decode(in, coder, null);
    }

    @Test
    public void testBZIP2DecoderEncode() throws Exception {
        Coders.BZIP2Decoder bzip2Decoder = new Coders.BZIP2Decoder();
        OutputStream out = new ByteArrayOutputStream();
        bzip2Decoder.encode(out, null);
    }

    @Test
    public void testAES256SHA256DecoderDecode() throws Exception {
        Coders.Coder coder = new Coders.Coder();
        Coders.AES256SHA256Decoder AES256SHA256Decoder = new Coders.AES256SHA256Decoder();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        AES256SHA256Decoder.decode(in, coder, null);
    }

    @Test
    public void testDummyByteAddingInputStreamRead() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        Coders.DummyByteAddingInputStream dummyByteAddingInputStream = new Coders.DummyByteAddingInputStream(in);
        dummyByteAddingInputStream.read();
    }

    @Test
    public void testDummyByteAddingInputStreamReadByteArray() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        Coders.DummyByteAddingInputStream dummyByteAddingInputStream = new Coders.DummyByteAddingInputStream(in);
        byte[] b = new byte[0];
        dummyByteAddingInputStream.read(b, 0, 0);
    }

    private static class Coders extends org.apache.commons.compress.archivers.sevenz.Coders {
        static class Coder extends org.apache.commons.compress.archivers.sevenz.Coders.CoderBase {}
        static class SevenZMethod extends org.apache.commons.compress.archivers.sevenz.Coders.CoderId {}

        static class CopyDecoder extends org.apache.commons.compress.archivers.sevenz.Coders.CopyDecoder {}

        static class LZMADecoder extends org.apache.commons.compress.archivers.sevenz.Coders.LZMADecoder {}

        static class DeflateDecoder extends org.apache.commons.compress.archivers.sevenz.Coders.DeflateDecoder {}

        static class BZIP2Decoder extends org.apache.commons.compress.archivers.sevenz.Coders.BZIP2Decoder {}

        static class AES256SHA256Decoder extends org.apache.commons.compress.archivers.sevenz.Coders.AES256SHA256Decoder {}

        private static class DummyByteAddingInputStream extends org.apache.commons.compress.archivers.sevenz.Coders.DummyByteAddingInputStream {

            private DummyByteAddingInputStream(InputStream in) {
                super(in);
            }

            @Override
            public int read() throws IOException {
                return super.read();
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                return super.read(b, off, len);
            }
        }
    }
}