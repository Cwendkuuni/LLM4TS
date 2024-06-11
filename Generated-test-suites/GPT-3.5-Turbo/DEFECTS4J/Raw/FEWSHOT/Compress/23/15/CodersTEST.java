import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.Coders.*;
import org.apache.commons.compress.archivers.sevenz.Coders.CoderId;
import org.apache.commons.compress.archivers.sevenz.Coders.CoderBase;
import org.apache.commons.compress.archivers.sevenz.Coders.CopyDecoder;
import org.apache.commons.compress.archivers.sevenz.Coders.LZMADecoder;
import org.apache.commons.compress.archivers.sevenz.Coders.DeflateDecoder;
import org.apache.commons.compress.archivers.sevenz.Coders.BZIP2Decoder;
import org.apache.commons.compress.archivers.sevenz.Coders.AES256SHA256Decoder;
import org.tukaani.xz.UnsupportedOptionsException;
import java.io.IOException;

public class CodersTest {
    @Test
    public void testAddDecoder() throws IOException {
        byte[] data = new byte[10];
        InputStream is = new ByteArrayInputStream(data);
        Coder coder = new Coder(SevenZMethod.COPY, new byte[]{});
        byte[] password = new byte[]{};

        assertThrows(IOException.class, () -> Coders.addDecoder(is, coder, password));
    }

    @Test
    public void testAddEncoder() throws IOException {
        byte[] data = new byte[10];
        OutputStream out = new ByteArrayOutputStream();
        SevenZMethod method = new SevenZMethod();
        byte[] password = new byte[]{};

        assertThrows(IOException.class, () -> Coders.addEncoder(out, method, password));
    }

    @Test
    public void testCoderId() {
        SevenZMethod method = new SevenZMethod();
        CoderBase coder = new CoderBase() {
            @Override
            InputStream decode(InputStream in, Coder coder, byte[] password) throws IOException {
                return null;
            }
        };
        Coders.CoderId coderId = new Coders.CoderId(method, coder);

        assertEquals(method, coderId.method);
        assertEquals(coder, coderId.coder);
    }

    @Test
    public void testCopyDecoder() {
        CopyDecoder decoder = new CopyDecoder();

        InputStream in = null;
        OutputStream out = null;
        Decoder decoderObj = new Decoder();

        assertThrows(UnsupportedOperationException.class, () -> decoderObj.encode(in, out));
        assertEquals(in, decoderObj.decode(in, new Coder(), new byte[]{}));
    }

    @Test
    public void testLZMADecoder() throws IOException {
        byte[] properties = new byte[]{1, 2, 3, 4, 5};
        Coder coder = new Coder();
        coder.properties = properties;
        coder.decompressionMethodId = new byte[]{1, 2, 3};
        LZMADecoder decoder = new LZMADecoder();
        InputStream in = null;
        InputStream expected = new LZMAInputStream(in, -1, properties[0], ((long) properties[1]) & 0xFF | ((long) properties[2]) << 8 & 0xFF00 | ((long) properties[3]) << 16 & 0xFF0000 | ((long) properties[4]) << 24 & 0xFF000000);

        assertEquals(expected.getClass(), decoder.decode(in, coder, new byte[]{}).getClass());
    }

    @Test
    public void testDeflateDecoder() {
        DeflateDecoder decoder = new DeflateDecoder();

        InputStream in = null;
        InflaterInputStream expected = new InflaterInputStream(new Coders.DummyByteAddingInputStream(in), new Inflater(true));
        OutputStream out = new ByteArrayOutputStream();
        DeflaterOutputStream expected2 = new DeflaterOutputStream(out, new Deflater(9, true));

        assertEquals(expected.getClass(), decoder.decode(in, new Coder(), new byte[]{}).getClass());
        assertThrows(UnsupportedOperationException.class, () -> decoder.encode(out, new byte[]{}));
    }

    @Test
    public void testBZIP2Decoder() throws IOException {
        BZIP2Decoder decoder = new BZIP2Decoder();
        InputStream in = null;
        InputStream expected = new BZip2CompressorInputStream(in);
        OutputStream out = new ByteArrayOutputStream();
        OutputStream expected2 = new BZip2CompressorOutputStream(out);

        assertEquals(expected.getClass(), decoder.decode(in, new Coder(), new byte[]{}).getClass());
        assertEquals(expected2.getClass(), decoder.encode(out, new byte[]{}).getClass());
    }

    @Test
    public void testAES256SHA256Decoder() {
        AES256SHA256Decoder decoder = new AES256SHA256Decoder();
        InputStream in = null;
        Coder coder = new Coder();
        coder.properties = new byte[]{1, 2, 3, 4, 5};
        coder.password = new byte[]{};
        InputStream expected = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                return 0;
            }

            @Override
            public void close() {
            }
        };

        assertEquals(expected.getClass(), decoder.decode(in, coder, coder.password).getClass());
    }
}